package com.realestate.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.realestate.exception.PaymentException;
import com.realestate.model.entity.Payment;
import com.realestate.model.entity.Payment.PaymentStatus;
import com.realestate.model.entity.Property;
import com.realestate.model.entity.User;
import com.realestate.repository.PaymentRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Value("${app.email.from:noreply@realestate.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:8080}")
    private String frontendUrl;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        log.info("Razorpay client initialized successfully");
    }

    public Map<String, Object> createOrder(Long investorId, Long propertyId, BigDecimal amount) {
        return createOrder(investorId, propertyId, amount, Payment.PaymentType.INVESTMENT);
    }

    public Map<String, Object> createDownpaymentOrder(Long userId, Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new PaymentException("Property not found"));

        BigDecimal downpaymentAmount = property.getDownpaymentAmount() != null
            ? property.getDownpaymentAmount()
            : new BigDecimal("500.00");

        return createOrder(userId, propertyId, downpaymentAmount, Payment.PaymentType.DOWNPAYMENT);
    }

    public Map<String, Object> createOrder(Long userId, Long propertyId, BigDecimal amount, Payment.PaymentType paymentType) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new PaymentException("User not found"));

            Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PaymentException("Property not found"));

            // Check for duplicate pending payment
            if (paymentRepository.existsByInvestorAndPropertyAndStatus(user, property, PaymentStatus.PENDING)) {
                throw new PaymentException("A pending payment already exists for this property");
            }

            // Create Razorpay order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

            JSONObject notes = new JSONObject();
            notes.put("user_id", userId);
            notes.put("property_id", propertyId);
            notes.put("property_name", property.getTitle());
            orderRequest.put("notes", notes);

            Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            // Save payment record
            String paymentNotes = paymentType == Payment.PaymentType.DOWNPAYMENT
                ? "Downpayment for " + property.getTitle()
                : "Investment in " + property.getTitle();

            Payment payment = Payment.builder()
                .investor(user)
                .property(property)
                .amount(amount)
                .investmentAmount(amount)
                .paymentType(paymentType)
                .status(PaymentStatus.PENDING)
                .razorpayOrderId(razorpayOrder.get("id"))
                .currency("INR")
                .notes(paymentNotes)
                .build();

            paymentRepository.save(payment);

            log.info("Payment order created: {} for user: {} and property: {}",
                razorpayOrder.get("id"), userId, propertyId);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", razorpayOrder.get("id"));
            response.put("amount", amount);
            response.put("currency", "INR");
            response.put("keyId", razorpayKeyId);
            response.put("investorName", user.getFirstName() + " " + user.getLastName());
            response.put("investorEmail", user.getEmail());
            response.put("investorPhone", user.getPhoneNumber());
            response.put("propertyName", property.getTitle());
            response.put("paymentId", payment.getId());

            return response;

        } catch (RazorpayException e) {
            log.error("Error creating Razorpay order: ", e);
            throw new PaymentException("Failed to create payment order: " + e.getMessage());
        }
    }

    public Payment verifyPayment(String orderId, String paymentId, String signature) {
        try {
            Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new PaymentException("Payment not found for order: " + orderId));

            // Verify signature
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean isValid = Utils.verifyPaymentSignature(options, razorpayKeySecret);

            if (isValid) {
                payment.markAsSuccess(paymentId, signature);
                payment = paymentRepository.save(payment);

                log.info("Payment verified successfully: {}", paymentId);

                // Send async email notification
                sendPaymentSuccessEmail(payment);

                // Clear cache
                clearPaymentCaches(payment.getInvestor().getId(), payment.getProperty().getId());

                return payment;
            } else {
                payment.markAsFailed("Invalid payment signature");
                paymentRepository.save(payment);
                throw new PaymentException("Payment verification failed: Invalid signature");
            }

        } catch (RazorpayException e) {
            log.error("Error verifying payment: ", e);
            throw new PaymentException("Payment verification failed: " + e.getMessage());
        }
    }

    @Async
    public CompletableFuture<Void> sendPaymentSuccessEmail(Payment payment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(payment.getInvestor().getEmail());
            message.setSubject("Investment Successful - " + payment.getProperty().getTitle());

            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Your investment has been successfully processed!\n\n" +
                "Investment Details:\n" +
                "Property: %s\n" +
                "Investment Amount: ₹%s\n" +
                "Transaction ID: %s\n" +
                "Date: %s\n\n" +
                "You can view your investment details at: %s/investor/payments\n\n" +
                "Thank you for investing with us!\n\n" +
                "Best Regards,\n" +
                "Real Estate Investment Platform",
                payment.getInvestor().getFirstName() + " " + payment.getInvestor().getLastName(),
                payment.getProperty().getTitle(),
                payment.getInvestmentAmount(),
                payment.getRazorpayPaymentId(),
                payment.getPaidAt(),
                frontendUrl
            );

            message.setText(emailBody);
            mailSender.send(message);

            payment.setEmailSent(true);
            paymentRepository.save(payment);

            log.info("Payment success email sent to: {}", payment.getInvestor().getEmail());

        } catch (Exception e) {
            log.error("Failed to send payment email: ", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Cacheable(value = "investorPayments", key = "#investorId")
    public List<Payment> getInvestorPayments(Long investorId) {
        User investor = userRepository.findById(investorId)
            .orElseThrow(() -> new PaymentException("Investor not found"));
        return paymentRepository.findByInvestorOrderByCreatedAtDesc(investor);
    }

    public Page<Payment> getInvestorPaymentsPaginated(Long investorId, Pageable pageable) {
        User investor = userRepository.findById(investorId)
            .orElseThrow(() -> new PaymentException("Investor not found"));
        return paymentRepository.findByInvestor(investor, pageable);
    }

    @Cacheable(value = "propertyInvestments", key = "#propertyId")
    public List<Payment> getPropertyInvestments(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new PaymentException("Property not found"));
        return paymentRepository.findByPropertyOrderByCreatedAtDesc(property)
            .stream()
            .filter(Payment::isPaid)
            .collect(Collectors.toList());
    }

    public BigDecimal getTotalInvestmentByInvestor(Long investorId) {
        BigDecimal total = paymentRepository.getTotalInvestmentByInvestor(investorId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalInvestmentForProperty(Long propertyId) {
        BigDecimal total = paymentRepository.getTotalInvestmentForProperty(propertyId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Map<String, Object> getInvestorStatistics(Long investorId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInvestment", getTotalInvestmentByInvestor(investorId));
        stats.put("successfulPayments", paymentRepository.getSuccessfulPaymentCountByInvestor(investorId));
        stats.put("recentPayments", paymentRepository.findInvestorPaymentsByDateRange(
            investorId,
            LocalDateTime.now().minusDays(30),
            LocalDateTime.now()
        ));
        return stats;
    }

    public List<Object[]> getTopInvestedProperties(Pageable pageable) {
        return paymentRepository.getTopInvestedProperties(pageable);
    }

    @Transactional
    public void expireOldPayments() {
        LocalDateTime expiryTime = LocalDateTime.now().minusHours(1);
        int expiredCount = paymentRepository.expireOldPayments(expiryTime);
        if (expiredCount > 0) {
            log.info("Expired {} old pending payments", expiredCount);
        }
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentException("Payment not found with id: " + paymentId));
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId)
            .orElseThrow(() -> new PaymentException("Payment not found for order: " + orderId));
    }

    @CacheEvict(value = {"investorPayments", "propertyInvestments"}, allEntries = true)
    public void clearPaymentCaches(Long investorId, Long propertyId) {
        log.debug("Cleared payment caches for investor: {} and property: {}", investorId, propertyId);
    }

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }
}