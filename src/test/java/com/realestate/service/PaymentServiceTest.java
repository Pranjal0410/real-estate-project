package com.realestate.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.realestate.exception.PaymentException;
import com.realestate.model.entity.Payment;
import com.realestate.model.entity.Payment.PaymentStatus;
import com.realestate.model.entity.Property;
import com.realestate.model.entity.User;
import com.realestate.repository.PaymentRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RazorpayClient razorpayClient;

    @InjectMocks
    private PaymentService paymentService;

    private User testInvestor;
    private Property testProperty;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        // Set test values for @Value fields
        ReflectionTestUtils.setField(paymentService, "razorpayKeyId", "test_key_id");
        ReflectionTestUtils.setField(paymentService, "razorpayKeySecret", "test_key_secret");
        ReflectionTestUtils.setField(paymentService, "fromEmail", "test@example.com");
        ReflectionTestUtils.setField(paymentService, "frontendUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(paymentService, "razorpayClient", razorpayClient);

        // Initialize test data
        testInvestor = new User();
        testInvestor.setId(1L);
        testInvestor.setFirstName("John");
        testInvestor.setLastName("Doe");
        testInvestor.setEmail("john@example.com");
        testInvestor.setPhoneNumber("9876543210");

        testProperty = new Property();
        testProperty.setId(1L);
        testProperty.setTitle("Test Property");
        testProperty.setLocation("Test Location");

        testPayment = Payment.builder()
            .id(1L)
            .investor(testInvestor)
            .property(testProperty)
            .amount(new BigDecimal("100000"))
            .investmentAmount(new BigDecimal("100000"))
            .status(PaymentStatus.PENDING)
            .razorpayOrderId("order_test123")
            .currency("INR")
            .build();
    }

    @Test
    void createOrder_Success() throws Exception {
        // Arrange
        Long investorId = 1L;
        Long propertyId = 1L;
        BigDecimal amount = new BigDecimal("100000");

        when(userRepository.findById(investorId)).thenReturn(Optional.of(testInvestor));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(testProperty));
        when(paymentRepository.existsByInvestorAndPropertyAndStatus(any(), any(), any())).thenReturn(false);

        Order mockOrder = mock(Order.class);
        when(mockOrder.get("id")).thenReturn("order_test123");
        when(razorpayClient.orders).thenReturn(mock(com.razorpay.OrderClient.class));
        when(razorpayClient.orders.create(any(JSONObject.class))).thenReturn(mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // Act
        Map<String, Object> result = paymentService.createOrder(investorId, propertyId, amount);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("orderId")).isEqualTo("order_test123");
        assertThat(result.get("amount")).isEqualTo(amount);
        assertThat(result.get("currency")).isEqualTo("INR");
        assertThat(result.get("keyId")).isEqualTo("test_key_id");
        assertThat(result.get("investorName")).isEqualTo("John Doe");

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createOrder_InvestorNotFound_ThrowsException() {
        // Arrange
        Long investorId = 999L;
        Long propertyId = 1L;
        BigDecimal amount = new BigDecimal("100000");

        when(userRepository.findById(investorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> paymentService.createOrder(investorId, propertyId, amount))
            .isInstanceOf(PaymentException.class)
            .hasMessage("Investor not found");
    }

    @Test
    void createOrder_PropertyNotFound_ThrowsException() {
        // Arrange
        Long investorId = 1L;
        Long propertyId = 999L;
        BigDecimal amount = new BigDecimal("100000");

        when(userRepository.findById(investorId)).thenReturn(Optional.of(testInvestor));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> paymentService.createOrder(investorId, propertyId, amount))
            .isInstanceOf(PaymentException.class)
            .hasMessage("Property not found");
    }

    @Test
    void createOrder_DuplicatePendingPayment_ThrowsException() {
        // Arrange
        Long investorId = 1L;
        Long propertyId = 1L;
        BigDecimal amount = new BigDecimal("100000");

        when(userRepository.findById(investorId)).thenReturn(Optional.of(testInvestor));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(testProperty));
        when(paymentRepository.existsByInvestorAndPropertyAndStatus(
            testInvestor, testProperty, PaymentStatus.PENDING)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> paymentService.createOrder(investorId, propertyId, amount))
            .isInstanceOf(PaymentException.class)
            .hasMessage("A pending payment already exists for this property");
    }

    @Test
    void verifyPayment_Success() throws Exception {
        // Arrange
        String orderId = "order_test123";
        String paymentId = "pay_test123";
        String signature = "test_signature";

        when(paymentRepository.findByRazorpayOrderId(orderId)).thenReturn(Optional.of(testPayment));

        // Mock static method Utils.verifyPaymentSignature
        try (MockedStatic<com.razorpay.Utils> utilsMock = mockStatic(com.razorpay.Utils.class)) {
            utilsMock.when(() -> com.razorpay.Utils.verifyPaymentSignature(any(JSONObject.class), anyString()))
                .thenReturn(true);

            when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

            // Act
            Payment result = paymentService.verifyPayment(orderId, paymentId, signature);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
            assertThat(result.getRazorpayPaymentId()).isEqualTo(paymentId);
            assertThat(result.getRazorpaySignature()).isEqualTo(signature);

            verify(paymentRepository, times(1)).save(any(Payment.class));
        }
    }

    @Test
    void verifyPayment_InvalidSignature_ThrowsException() throws Exception {
        // Arrange
        String orderId = "order_test123";
        String paymentId = "pay_test123";
        String signature = "invalid_signature";

        when(paymentRepository.findByRazorpayOrderId(orderId)).thenReturn(Optional.of(testPayment));

        try (MockedStatic<com.razorpay.Utils> utilsMock = mockStatic(com.razorpay.Utils.class)) {
            utilsMock.when(() -> com.razorpay.Utils.verifyPaymentSignature(any(JSONObject.class), anyString()))
                .thenReturn(false);

            when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

            // Act & Assert
            assertThatThrownBy(() -> paymentService.verifyPayment(orderId, paymentId, signature))
                .isInstanceOf(PaymentException.class)
                .hasMessage("Payment verification failed: Invalid signature");

            assertThat(testPayment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        }
    }

    @Test
    void verifyPayment_PaymentNotFound_ThrowsException() {
        // Arrange
        String orderId = "order_not_found";
        String paymentId = "pay_test123";
        String signature = "test_signature";

        when(paymentRepository.findByRazorpayOrderId(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> paymentService.verifyPayment(orderId, paymentId, signature))
            .isInstanceOf(PaymentException.class)
            .hasMessageContaining("Payment not found for order");
    }

    @Test
    void sendPaymentSuccessEmail_Success() {
        // Arrange
        testPayment.setRazorpayPaymentId("pay_test123");
        testPayment.setPaidAt(LocalDateTime.now());

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // Act
        CompletableFuture<Void> result = paymentService.sendPaymentSuccessEmail(testPayment);

        // Assert
        assertThat(result).isNotNull();
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void getInvestorPayments_Success() {
        // Arrange
        Long investorId = 1L;
        List<Payment> expectedPayments = Arrays.asList(testPayment);

        when(userRepository.findById(investorId)).thenReturn(Optional.of(testInvestor));
        when(paymentRepository.findByInvestorOrderByCreatedAtDesc(testInvestor))
            .thenReturn(expectedPayments);

        // Act
        List<Payment> result = paymentService.getInvestorPayments(investorId);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testPayment);
    }

    @Test
    void getTotalInvestmentByInvestor_ReturnsCorrectTotal() {
        // Arrange
        Long investorId = 1L;
        BigDecimal expectedTotal = new BigDecimal("500000");

        when(paymentRepository.getTotalInvestmentByInvestor(investorId)).thenReturn(expectedTotal);

        // Act
        BigDecimal result = paymentService.getTotalInvestmentByInvestor(investorId);

        // Assert
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalInvestmentByInvestor_NoInvestments_ReturnsZero() {
        // Arrange
        Long investorId = 1L;

        when(paymentRepository.getTotalInvestmentByInvestor(investorId)).thenReturn(null);

        // Act
        BigDecimal result = paymentService.getTotalInvestmentByInvestor(investorId);

        // Assert
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void expireOldPayments_Success() {
        // Arrange
        int expectedExpiredCount = 5;
        when(paymentRepository.expireOldPayments(any(LocalDateTime.class)))
            .thenReturn(expectedExpiredCount);

        // Act
        paymentService.expireOldPayments();

        // Assert
        verify(paymentRepository, times(1)).expireOldPayments(any(LocalDateTime.class));
    }

    @Test
    void getPaymentById_Success() {
        // Arrange
        Long paymentId = 1L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(testPayment));

        // Act
        Payment result = paymentService.getPaymentById(paymentId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(paymentId);
    }

    @Test
    void getPaymentById_NotFound_ThrowsException() {
        // Arrange
        Long paymentId = 999L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> paymentService.getPaymentById(paymentId))
            .isInstanceOf(PaymentException.class)
            .hasMessageContaining("Payment not found with id");
    }
}