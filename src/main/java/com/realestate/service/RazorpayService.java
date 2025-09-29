package com.realestate.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.realestate.model.entity.Payment;
import com.realestate.model.entity.Property;
import com.realestate.repository.PaymentRepository;
import com.realestate.repository.PropertyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RazorpayService {

    @Value("${razorpay.key.id:rzp_test_XXXXXXXXXXX}")
    private String keyId;

    @Value("${razorpay.key.secret:test_secret_XXXXXXXXXXX}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    private final PaymentRepository paymentRepository;
    private final PropertyRepository propertyRepository;

    public RazorpayService(PaymentRepository paymentRepository, PropertyRepository propertyRepository) {
        this.paymentRepository = paymentRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
        log.info("RazorpayClient initialized with Key ID: {}", keyId.substring(0, Math.min(keyId.length(), 10)) + "...");
    }

    public Order createOrder(int amountInPaise) throws RazorpayException {
        return createOrderForProperty(amountInPaise, null, "INVESTMENT");
    }

    public Order createOrderForProperty(int amountInPaise, Long propertyId, String paymentType) throws RazorpayException {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + UUID.randomUUID().toString());

            // Add property notes if available
            if (propertyId != null) {
                JSONObject notes = new JSONObject();
                notes.put("propertyId", propertyId.toString());
                notes.put("paymentType", paymentType);
                orderRequest.put("notes", notes);
            }

            Order order = razorpayClient.orders.create(orderRequest);
            log.info("Razorpay order created: {}", order.get("id").toString());

            // Save order details to database
            Payment payment = new Payment();
            payment.setOrderId(order.get("id"));
            payment.setRazorpayOrderId(order.get("id"));
            payment.setAmountInRupees(amountInPaise / 100.0);
            payment.setAmount(BigDecimal.valueOf(amountInPaise / 100.0));
            payment.setInvestmentAmount(BigDecimal.valueOf(amountInPaise / 100.0));
            payment.setCurrency("INR");
            payment.setStatus(Payment.PaymentStatus.PENDING);
            payment.setCreatedAt(LocalDateTime.now());

            // Set property if provided
            if (propertyId != null) {
                Optional<Property> property = propertyRepository.findById(propertyId);
                property.ifPresent(payment::setProperty);

                // Set payment type
                try {
                    payment.setPaymentType(Payment.PaymentType.valueOf(paymentType));
                } catch (IllegalArgumentException e) {
                    payment.setPaymentType(Payment.PaymentType.DOWNPAYMENT);
                }
            }

            paymentRepository.save(payment);

            return order;
        } catch (RazorpayException e) {
            log.error("Error creating order: ", e);
            throw e;
        }
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);
            log.info("Payment signature verification result: {}", isValid);
            return isValid;

        } catch (Exception e) {
            log.error("Error verifying payment signature: ", e);
            return false;
        }
    }

    @Transactional
    public Payment savePaymentDetails(String orderId, String paymentId, String signature) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .or(() -> paymentRepository.findByRazorpayOrderId(orderId))
                .orElse(new Payment());

        payment.setOrderId(orderId);
        payment.setRazorpayOrderId(orderId);
        payment.setPaymentId(paymentId);
        payment.setRazorpayPaymentId(paymentId);
        payment.setSignature(signature);
        payment.setRazorpaySignature(signature);
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        if (payment.getCreatedAt() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }

        return paymentRepository.save(payment);
    }

    public String getKeyId() {
        return keyId;
    }

    // Manual signature verification (alternative method)
    public String createPaymentLink(Double amountInRupees, Long propertyId) throws RazorpayException {
        try {
            int amountInPaise = (int)(amountInRupees * 100);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amountInPaise);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", propertyId != null ? "Property Payment #" + propertyId : "Real Estate Investment");

            // Add customer details
            JSONObject customer = new JSONObject();
            customer.put("name", "Customer");
            customer.put("email", "customer@example.com");
            customer.put("contact", "+919999999999");
            paymentLinkRequest.put("customer", customer);

            // Add notes
            JSONObject notes = new JSONObject();
            if (propertyId != null) {
                notes.put("propertyId", propertyId.toString());
            }
            notes.put("paymentType", "DOWNPAYMENT");
            paymentLinkRequest.put("notes", notes);

            // Add callback URL and method
            paymentLinkRequest.put("callback_url", "http://localhost:9090/api/razorpay/payment-success");
            paymentLinkRequest.put("callback_method", "get");

            com.razorpay.PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            String shortUrl = paymentLink.get("short_url");

            log.info("Payment link created: {}", shortUrl);

            // Save payment record
            Payment payment = new Payment();
            payment.setOrderId(paymentLink.get("id"));
            payment.setAmountInRupees(amountInRupees);
            payment.setAmount(BigDecimal.valueOf(amountInRupees));
            payment.setInvestmentAmount(BigDecimal.valueOf(amountInRupees));
            payment.setCurrency("INR");
            payment.setStatus(Payment.PaymentStatus.PENDING);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setPaymentType(Payment.PaymentType.DOWNPAYMENT);

            if (propertyId != null) {
                Optional<Property> property = propertyRepository.findById(propertyId);
                property.ifPresent(payment::setProperty);
            }

            paymentRepository.save(payment);

            return shortUrl;

        } catch (Exception e) {
            log.error("Error creating payment link: ", e);
            throw new RazorpayException("Failed to create payment link: " + e.getMessage());
        }
    }

    private boolean verifySignatureManually(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String generatedSignature = hexString.toString();
            boolean isValid = generatedSignature.equals(signature);

            log.debug("Manual signature verification - Expected: {}, Received: {}, Valid: {}",
                     generatedSignature, signature, isValid);

            return isValid;
        } catch (Exception e) {
            log.error("Error in manual signature verification: ", e);
            return false;
        }
    }
}