package com.realestate.controller;

import com.realestate.exception.PaymentException;
import com.realestate.model.dto.UserDTO;
import com.realestate.model.entity.Payment;
import com.realestate.security.CustomUserDetails;
import com.realestate.service.PaymentService;
import com.realestate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    // REST API Endpoints

    @PostMapping("/api/payment/create-downpayment")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> createDownpaymentOrder(
            @RequestParam Long propertyId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            log.info("Creating downpayment order for user: {} and property: {}",
                userDetails.getId(), propertyId);

            Map<String, Object> orderData = paymentService.createDownpaymentOrder(
                userDetails.getId(),
                propertyId
            );

            return ResponseEntity.ok(orderData);
        } catch (PaymentException e) {
            log.error("Error creating downpayment order: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Unexpected error creating downpayment order: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to create downpayment order"
            ));
        }
    }

    @PostMapping("/api/payment/create-order")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public ResponseEntity<?> createOrder(
            @RequestParam Long propertyId,
            @RequestParam BigDecimal amount,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            log.info("Creating payment order for investor: {} and property: {} with amount: {}",
                userDetails.getId(), propertyId, amount);

            Map<String, Object> orderData = paymentService.createOrder(
                userDetails.getId(),
                propertyId,
                amount
            );

            return ResponseEntity.ok(orderData);
        } catch (PaymentException e) {
            log.error("Error creating payment order: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Unexpected error creating payment order: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to create payment order"
            ));
        }
    }

    @PostMapping("/api/payment/verify")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public ResponseEntity<?> verifyPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            log.info("Verifying payment for order: {} with payment: {}",
                razorpay_order_id, razorpay_payment_id);

            Payment payment = paymentService.verifyPayment(
                razorpay_order_id,
                razorpay_payment_id,
                razorpay_signature
            );

            // Verify the payment belongs to the current user
            if (!payment.getInvestor().getId().equals(userDetails.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "error", true,
                    "message", "Unauthorized access to payment"
                ));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment verified successfully");
            response.put("paymentId", payment.getId());
            response.put("status", payment.getStatus());
            response.put("amount", payment.getInvestmentAmount());

            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            log.error("Payment verification failed: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Unexpected error verifying payment: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Payment verification failed"
            ));
        }
    }

    @GetMapping("/api/payment/history")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public ResponseEntity<?> getPaymentHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Payment> payments = paymentService.getInvestorPaymentsPaginated(userDetails.getId(), pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("payments", payments.getContent());
            response.put("currentPage", payments.getNumber());
            response.put("totalPages", payments.getTotalPages());
            response.put("totalElements", payments.getTotalElements());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching payment history: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to fetch payment history"
            ));
        }
    }

    @GetMapping("/api/payment/statistics")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public ResponseEntity<?> getInvestmentStatistics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Map<String, Object> stats = paymentService.getInvestorStatistics(userDetails.getId());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching investment statistics: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to fetch statistics"
            ));
        }
    }

    @GetMapping("/api/payment/{paymentId}")
    @PreAuthorize("hasRole('INVESTOR')")
    @ResponseBody
    public ResponseEntity<?> getPaymentDetails(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);

            // Verify the payment belongs to the current user
            if (!payment.getInvestor().getId().equals(userDetails.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "error", true,
                    "message", "Unauthorized access to payment"
                ));
            }

            return ResponseEntity.ok(payment);
        } catch (PaymentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching payment details: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to fetch payment details"
            ));
        }
    }

    // MVC Endpoints for JSP Pages

    @GetMapping("/user/downpayment/{propertyId}")
    @PreAuthorize("isAuthenticated()")
    public String showDownpaymentPage(
            @PathVariable Long propertyId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            UserDTO userDTO = userService.findById(userDetails.getId());
            Map<String, Object> user = new HashMap<>();
            user.put("name", userDTO.getFirstName() + " " + userDTO.getLastName());
            user.put("email", userDTO.getEmail());
            user.put("phone", userDTO.getPhoneNumber());

            model.addAttribute("investor", user); // Keep as 'investor' for JSP compatibility
            model.addAttribute("propertyId", propertyId);
            model.addAttribute("razorpayKeyId", paymentService.getRazorpayKeyId());
            model.addAttribute("isDownpayment", true);
            model.addAttribute("amount", 500); // Fixed downpayment amount
            return "payment/downpayment";
        } catch (Exception e) {
            log.error("Error loading downpayment page: ", e);
            model.addAttribute("error", "Failed to load downpayment page");
            return "error";
        }
    }

    @GetMapping("/investor/invest/{propertyId}")
    @PreAuthorize("hasRole('INVESTOR')")
    public String showInvestmentPage(
            @PathVariable Long propertyId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            UserDTO investorDTO = userService.findById(userDetails.getId());
            Map<String, Object> investor = new HashMap<>();
            investor.put("name", investorDTO.getFirstName() + " " + investorDTO.getLastName());
            investor.put("email", investorDTO.getEmail());
            investor.put("phone", investorDTO.getPhoneNumber());
            model.addAttribute("investor", investor);
            model.addAttribute("propertyId", propertyId);
            model.addAttribute("razorpayKeyId", paymentService.getRazorpayKeyId());
            return "payment/invest";
        } catch (Exception e) {
            log.error("Error loading investment page: ", e);
            model.addAttribute("error", "Failed to load investment page");
            return "error";
        }
    }

    @GetMapping("/investor/payments")
    @PreAuthorize("hasRole('INVESTOR')")
    public String showPaymentHistory(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Payment> payments = paymentService.getInvestorPaymentsPaginated(userDetails.getId(), pageable);

            model.addAttribute("payments", payments.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", payments.getTotalPages());
            model.addAttribute("totalInvestment", paymentService.getTotalInvestmentByInvestor(userDetails.getId()));

            return "payment/history";
        } catch (Exception e) {
            log.error("Error loading payment history: ", e);
            model.addAttribute("error", "Failed to load payment history");
            return "error";
        }
    }

    @GetMapping("/investor/payment/success")
    @PreAuthorize("hasRole('INVESTOR')")
    public String paymentSuccess(
            @RequestParam(required = false) String orderId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (orderId != null) {
                Payment payment = paymentService.getPaymentByOrderId(orderId);
                model.addAttribute("payment", payment);
            }
            return "payment/success";
        } catch (Exception e) {
            log.error("Error loading payment success page: ", e);
            return "payment/success";
        }
    }

    @GetMapping("/investor/payment/failure")
    @PreAuthorize("hasRole('INVESTOR')")
    public String paymentFailure(Model model) {
        return "payment/failure";
    }

    // Admin Endpoints

    @GetMapping("/api/admin/payments")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Payment> payments = paymentService.getInvestorPaymentsPaginated(null, pageable);

            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            log.error("Error fetching all payments: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to fetch payments"
            ));
        }
    }

    @GetMapping("/api/admin/payments/top-properties")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> getTopInvestedProperties(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<Object[]> topProperties = paymentService.getTopInvestedProperties(pageable);

            return ResponseEntity.ok(topProperties);
        } catch (Exception e) {
            log.error("Error fetching top invested properties: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", true,
                "message", "Failed to fetch top properties"
            ));
        }
    }
}