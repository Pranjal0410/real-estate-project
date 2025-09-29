package com.realestate.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.realestate.model.entity.Payment;
import com.realestate.model.entity.Property;
import com.realestate.repository.PropertyRepository;
import com.realestate.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/api/razorpay")
@RequiredArgsConstructor
public class RazorpayController {

    private final RazorpayService razorpayService;
    private final PropertyRepository propertyRepository;

    @PostMapping("/create-order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Get amount and property details from request
            Double amountInRupees = Double.parseDouble(requestData.getOrDefault("amount", "100").toString());
            Long propertyId = requestData.containsKey("propertyId") ?
                Long.parseLong(requestData.get("propertyId").toString()) : null;
            String paymentType = requestData.getOrDefault("paymentType", "DOWNPAYMENT").toString();

            int amountInPaise = (int)(amountInRupees * 100); // Convert to paise

            Order order = razorpayService.createOrderForProperty(amountInPaise, propertyId, paymentType);

            response.put("success", true);
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("keyId", razorpayService.getKeyId());
            response.put("propertyId", propertyId);
            response.put("paymentType", paymentType);

            log.info("Order created successfully: {}", order.get("id").toString());
            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {
            log.error("Error creating Razorpay order: ", e);
            response.put("success", false);
            response.put("message", "Failed to create order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/verify-payment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody Map<String, String> paymentDetails) {
        Map<String, Object> response = new HashMap<>();

        String orderId = paymentDetails.get("orderId");
        String paymentId = paymentDetails.get("paymentId");
        String signature = paymentDetails.get("signature");

        try {
            boolean isValid = razorpayService.verifyPaymentSignature(orderId, paymentId, signature);

            if (isValid) {
                Payment payment = razorpayService.savePaymentDetails(orderId, paymentId, signature);

                response.put("success", true);
                response.put("message", "Payment verified successfully");
                response.put("paymentId", payment.getId());

                log.info("Payment verified successfully for order: {}", orderId);
            } else {
                response.put("success", false);
                response.put("message", "Payment signature verification failed");

                log.warn("Payment verification failed for order: {}", orderId);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error verifying payment: ", e);
            response.put("success", false);
            response.put("message", "Error verifying payment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/test-payment")
    public String testPaymentPage() {
        return "payment/checkout";
    }

    @GetMapping("/simple-payment")
    public String simplePaymentPage(@RequestParam(required = false) Long propertyId,
                                    @RequestParam(required = false, defaultValue = "100") Double amount,
                                    Model model) {
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("amount", amount);
        return "payment/simple-payment";
    }

    @PostMapping("/create-payment-link")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createPaymentLink(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();

        try {
            Double amountInRupees = Double.parseDouble(requestData.getOrDefault("amount", "100").toString());
            Long propertyId = requestData.containsKey("propertyId") && requestData.get("propertyId") != null ?
                Long.parseLong(requestData.get("propertyId").toString()) : null;

            String paymentLink = razorpayService.createPaymentLink(amountInRupees, propertyId);

            response.put("success", true);
            response.put("paymentLink", paymentLink);
            response.put("message", "Payment link created successfully");

            log.info("Payment link created: {}", paymentLink);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error creating payment link: ", e);
            response.put("success", false);
            response.put("message", "Failed to create payment link: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/payment-page")
    public String paymentPage(@RequestParam(required = false) Long propertyId, Model model) {
        if (propertyId != null) {
            Optional<Property> property = propertyRepository.findById(propertyId);
            property.ifPresent(p -> {
                model.addAttribute("property", p);
                model.addAttribute("propertyId", propertyId);
                // Calculate downpayment (e.g., 20% of property value)
                double downpayment = p.getPrice().doubleValue() * 0.20;
                model.addAttribute("downpaymentAmount", downpayment);
            });
        }
        return "payment/checkout";
    }

    @GetMapping("/property-payment/{propertyId}")
    public String propertyPaymentPage(@PathVariable Long propertyId, Model model) {
        Optional<Property> property = propertyRepository.findById(propertyId);
        if (property.isPresent()) {
            Property prop = property.get();
            model.addAttribute("property", prop);
            model.addAttribute("propertyId", propertyId);
            // Calculate different payment options
            double fullAmount = prop.getPrice().doubleValue();
            double downpayment = fullAmount * 0.20; // 20% downpayment
            double booking = 50000; // Fixed booking amount

            model.addAttribute("fullAmount", fullAmount);
            model.addAttribute("downpaymentAmount", downpayment);
            model.addAttribute("bookingAmount", booking);
        }
        return "payment/property-payment";
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(@RequestParam(required = false) String razorpay_payment_id,
                                  @RequestParam(required = false) String razorpay_payment_link_id,
                                  @RequestParam(required = false) String razorpay_payment_link_reference_id,
                                  @RequestParam(required = false) String razorpay_payment_link_status,
                                  @RequestParam(required = false) String razorpay_signature,
                                  Model model) {
        log.info("Payment success callback received - Payment ID: {}, Link ID: {}, Status: {}",
                razorpay_payment_id, razorpay_payment_link_id, razorpay_payment_link_status);

        model.addAttribute("paymentId", razorpay_payment_id);
        model.addAttribute("status", razorpay_payment_link_status);
        model.addAttribute("message", "Payment completed successfully!");

        return "payment/success";
    }
}