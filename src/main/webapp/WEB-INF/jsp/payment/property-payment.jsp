<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Property Payment - ${property.title}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .header {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .header h1 {
            color: #333;
            font-size: 24px;
            margin-bottom: 5px;
        }

        .header .subtitle {
            color: #666;
            font-size: 14px;
        }

        .content-wrapper {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        @media (max-width: 768px) {
            .content-wrapper {
                grid-template-columns: 1fr;
            }
        }

        .property-details {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .property-image {
            width: 100%;
            height: 250px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 48px;
        }

        .property-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .property-location {
            color: #666;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .property-specs {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            margin-bottom: 20px;
        }

        .spec-item {
            padding: 10px;
            background: #f8f9fa;
            border-radius: 8px;
        }

        .spec-label {
            color: #888;
            font-size: 12px;
            margin-bottom: 5px;
        }

        .spec-value {
            color: #333;
            font-size: 16px;
            font-weight: 600;
        }

        .price-section {
            border-top: 1px solid #eee;
            padding-top: 20px;
            margin-top: 20px;
        }

        .total-price {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }

        .price-label {
            color: #666;
            font-size: 16px;
        }

        .price-amount {
            color: #1e3c72;
            font-size: 28px;
            font-weight: bold;
        }

        .payment-options {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .payment-options h2 {
            color: #333;
            font-size: 20px;
            margin-bottom: 20px;
        }

        .payment-type-selector {
            margin-bottom: 20px;
        }

        .payment-type-card {
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .payment-type-card:hover {
            border-color: #1e3c72;
            background: #f8f9fa;
        }

        .payment-type-card.selected {
            border-color: #1e3c72;
            background: linear-gradient(135deg, rgba(30,60,114,0.05) 0%, rgba(42,82,152,0.05) 100%);
        }

        .payment-type-header {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        .payment-type-radio {
            margin-right: 10px;
        }

        .payment-type-title {
            flex: 1;
            font-size: 16px;
            font-weight: 600;
            color: #333;
        }

        .payment-type-amount {
            font-size: 20px;
            font-weight: bold;
            color: #1e3c72;
        }

        .payment-type-description {
            color: #666;
            font-size: 14px;
            margin-left: 25px;
        }

        .custom-amount-section {
            display: none;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            margin-top: 10px;
        }

        .custom-amount-section.active {
            display: block;
        }

        .custom-amount-input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
        }

        .payment-summary {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }

        .summary-label {
            color: #666;
        }

        .summary-value {
            font-weight: 600;
            color: #333;
        }

        .summary-total {
            border-top: 2px solid #ddd;
            padding-top: 10px;
            margin-top: 10px;
        }

        .summary-total .summary-value {
            font-size: 24px;
            color: #1e3c72;
        }

        .pay-button {
            width: 100%;
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            color: white;
            border: none;
            padding: 15px 30px;
            font-size: 18px;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: 600;
        }

        .pay-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(30,60,114,0.3);
        }

        .pay-button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }

        .secure-badge {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            margin-top: 15px;
            color: #666;
            font-size: 14px;
        }

        .test-mode-banner {
            background: #fff3cd;
            color: #856404;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
            text-align: center;
        }

        .loading {
            display: none;
            text-align: center;
            padding: 20px;
        }

        .loading.show {
            display: block;
        }

        .spinner {
            display: inline-block;
            width: 30px;
            height: 30px;
            border: 3px solid #f3f3f3;
            border-top: 3px solid #1e3c72;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        .status-message {
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            display: none;
        }

        .status-message.success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            display: block;
        }

        .status-message.error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            display: block;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Complete Your Property Payment</h1>
            <div class="subtitle">Secure payment powered by Razorpay</div>
        </div>

        <div class="test-mode-banner">
            ⚠️ TEST MODE - Use test card: 4111 1111 1111 1111
        </div>

        <div class="content-wrapper">
            <!-- Property Details -->
            <div class="property-details">
                <div class="property-image">🏠</div>
                <div class="property-title">
                    <c:choose>
                        <c:when test="${not empty property}">
                            ${property.title}
                        </c:when>
                        <c:otherwise>
                            Premium Property
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="property-location">
                    <c:choose>
                        <c:when test="${not empty property}">
                            ${property.location}
                        </c:when>
                        <c:otherwise>
                            Mumbai, Maharashtra
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="property-specs">
                    <div class="spec-item">
                        <div class="spec-label">Property Type</div>
                        <div class="spec-value">
                            <c:choose>
                                <c:when test="${not empty property}">
                                    ${property.propertyType}
                                </c:when>
                                <c:otherwise>
                                    Residential
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="spec-item">
                        <div class="spec-label">Area</div>
                        <div class="spec-value">
                            <c:choose>
                                <c:when test="${not empty property}">
                                    ${property.area} ${property.areaUnit}
                                </c:when>
                                <c:otherwise>
                                    1200 sq.ft
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="spec-item">
                        <div class="spec-label">Bedrooms</div>
                        <div class="spec-value">
                            <c:choose>
                                <c:when test="${not empty property}">
                                    ${property.bedrooms} BHK
                                </c:when>
                                <c:otherwise>
                                    3 BHK
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="spec-item">
                        <div class="spec-label">Status</div>
                        <div class="spec-value">Available</div>
                    </div>
                </div>

                <div class="price-section">
                    <div class="total-price">
                        <span class="price-label">Total Property Value</span>
                        <span class="price-amount">
                            ₹<fmt:formatNumber value="${fullAmount != null ? fullAmount : 5000000}" pattern="#,##,###"/>
                        </span>
                    </div>
                </div>
            </div>

            <!-- Payment Options -->
            <div class="payment-options">
                <h2>Select Payment Option</h2>

                <div class="payment-type-selector">
                    <!-- Booking Amount -->
                    <div class="payment-type-card" onclick="selectPaymentType('BOOKING', ${bookingAmount != null ? bookingAmount : 50000})">
                        <div class="payment-type-header">
                            <input type="radio" name="paymentType" value="BOOKING" class="payment-type-radio">
                            <span class="payment-type-title">Booking Amount</span>
                            <span class="payment-type-amount">₹<fmt:formatNumber value="${bookingAmount != null ? bookingAmount : 50000}" pattern="#,##,###"/></span>
                        </div>
                        <div class="payment-type-description">
                            Reserve this property with a booking amount
                        </div>
                    </div>

                    <!-- Down Payment -->
                    <div class="payment-type-card selected" onclick="selectPaymentType('DOWNPAYMENT', ${downpaymentAmount != null ? downpaymentAmount : 1000000})">
                        <div class="payment-type-header">
                            <input type="radio" name="paymentType" value="DOWNPAYMENT" class="payment-type-radio" checked>
                            <span class="payment-type-title">Down Payment (20%)</span>
                            <span class="payment-type-amount">₹<fmt:formatNumber value="${downpaymentAmount != null ? downpaymentAmount : 1000000}" pattern="#,##,###"/></span>
                        </div>
                        <div class="payment-type-description">
                            Pay 20% of property value as down payment
                        </div>
                    </div>

                    <!-- Full Payment -->
                    <div class="payment-type-card" onclick="selectPaymentType('INVESTMENT', ${fullAmount != null ? fullAmount : 5000000})">
                        <div class="payment-type-header">
                            <input type="radio" name="paymentType" value="INVESTMENT" class="payment-type-radio">
                            <span class="payment-type-title">Full Payment</span>
                            <span class="payment-type-amount">₹<fmt:formatNumber value="${fullAmount != null ? fullAmount : 5000000}" pattern="#,##,###"/></span>
                        </div>
                        <div class="payment-type-description">
                            Complete payment for the property
                        </div>
                    </div>

                    <!-- Custom Amount -->
                    <div class="payment-type-card" onclick="selectPaymentType('CUSTOM', 0)">
                        <div class="payment-type-header">
                            <input type="radio" name="paymentType" value="CUSTOM" class="payment-type-radio">
                            <span class="payment-type-title">Custom Amount</span>
                        </div>
                        <div class="payment-type-description">
                            Enter your preferred payment amount
                        </div>
                    </div>
                    <div class="custom-amount-section" id="customAmountSection">
                        <label for="customAmount">Enter Amount (₹)</label>
                        <input type="number"
                               id="customAmount"
                               class="custom-amount-input"
                               placeholder="Enter amount in rupees"
                               min="1000"
                               onchange="updateCustomAmount(this.value)">
                    </div>
                </div>

                <div class="payment-summary">
                    <div class="summary-row">
                        <span class="summary-label">Payment Type</span>
                        <span class="summary-value" id="selectedPaymentType">Down Payment</span>
                    </div>
                    <div class="summary-row">
                        <span class="summary-label">Processing Fee</span>
                        <span class="summary-value">₹0</span>
                    </div>
                    <div class="summary-row summary-total">
                        <span class="summary-label">Total Amount</span>
                        <span class="summary-value" id="totalAmount">₹<fmt:formatNumber value="${downpaymentAmount != null ? downpaymentAmount : 1000000}" pattern="#,##,###"/></span>
                    </div>
                </div>

                <button id="payButton" class="pay-button" onclick="initiatePayment()">
                    Pay Now
                </button>

                <div class="loading" id="loading">
                    <div class="spinner"></div>
                    <p>Processing your payment...</p>
                </div>

                <div id="statusMessage" class="status-message"></div>

                <div class="secure-badge">
                    <span>🔒</span>
                    <span>Your payment is secured by Razorpay</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Razorpay Checkout Script -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>

    <script>
        let selectedAmount = ${downpaymentAmount != null ? downpaymentAmount : 1000000};
        let selectedPaymentType = 'DOWNPAYMENT';
        let propertyId = ${propertyId != null ? propertyId : 'null'};

        function selectPaymentType(type, amount) {
            // Update selected card UI
            document.querySelectorAll('.payment-type-card').forEach(card => {
                card.classList.remove('selected');
            });
            event.currentTarget.classList.add('selected');

            // Update radio button
            document.querySelectorAll('.payment-type-radio').forEach(radio => {
                radio.checked = false;
            });
            event.currentTarget.querySelector('.payment-type-radio').checked = true;

            // Handle custom amount section
            const customSection = document.getElementById('customAmountSection');
            if (type === 'CUSTOM') {
                customSection.classList.add('active');
                selectedAmount = 0;
            } else {
                customSection.classList.remove('active');
                selectedAmount = amount;
                updatePaymentSummary(type, amount);
            }

            selectedPaymentType = type;
        }

        function updateCustomAmount(value) {
            const amount = parseFloat(value) || 0;
            if (amount >= 1000) {
                selectedAmount = amount;
                updatePaymentSummary('CUSTOM', amount);
            }
        }

        function updatePaymentSummary(type, amount) {
            const typeLabels = {
                'BOOKING': 'Booking Amount',
                'DOWNPAYMENT': 'Down Payment',
                'INVESTMENT': 'Full Payment',
                'CUSTOM': 'Custom Amount'
            };

            document.getElementById('selectedPaymentType').textContent = typeLabels[type];
            document.getElementById('totalAmount').textContent = '₹' + amount.toLocaleString('en-IN');
        }

        function initiatePayment() {
            if (selectedAmount < 1000) {
                showStatus('Please enter a valid amount (minimum ₹1,000)', 'error');
                return;
            }

            const payButton = document.getElementById('payButton');
            const loading = document.getElementById('loading');
            const statusMessage = document.getElementById('statusMessage');

            // Reset status
            statusMessage.className = 'status-message';
            statusMessage.textContent = '';

            // Show loading
            payButton.disabled = true;
            loading.classList.add('show');

            // Prepare request data
            const requestData = {
                amount: selectedAmount,
                paymentType: selectedPaymentType
            };

            if (propertyId) {
                requestData.propertyId = propertyId;
            }

            // Call backend to create order
            fetch('/api/razorpay/create-order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
            .then(response => response.json())
            .then(data => {
                loading.classList.remove('show');
                payButton.disabled = false;

                if (data.success) {
                    // Configure Razorpay options
                    const options = {
                        key: data.keyId,
                        amount: data.amount,
                        currency: data.currency,
                        order_id: data.orderId,
                        name: 'Real Estate Investment',
                        description: selectedPaymentType + ' Payment',
                        image: 'https://via.placeholder.com/150',
                        handler: function(response) {
                            // Payment successful, verify on backend
                            verifyPayment(response);
                        },
                        prefill: {
                            name: 'Test User',
                            email: 'test@example.com',
                            contact: '9999999999'
                        },
                        notes: {
                            propertyId: propertyId,
                            paymentType: selectedPaymentType
                        },
                        theme: {
                            color: '#1e3c72'
                        },
                        modal: {
                            ondismiss: function() {
                                showStatus('Payment cancelled', 'error');
                            }
                        }
                    };

                    // Open Razorpay checkout
                    const razorpay = new Razorpay(options);
                    razorpay.open();
                } else {
                    showStatus('Failed to create order: ' + data.message, 'error');
                }
            })
            .catch(error => {
                loading.classList.remove('show');
                payButton.disabled = false;
                showStatus('Error: ' + error.message, 'error');
            });
        }

        function verifyPayment(paymentResponse) {
            const loading = document.getElementById('loading');
            loading.classList.add('show');

            // Send payment details to backend for verification
            fetch('/api/razorpay/verify-payment', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    orderId: paymentResponse.razorpay_order_id,
                    paymentId: paymentResponse.razorpay_payment_id,
                    signature: paymentResponse.razorpay_signature
                })
            })
            .then(response => response.json())
            .then(data => {
                loading.classList.remove('show');

                if (data.success) {
                    showStatus('✅ Payment successful! Transaction ID: ' + data.paymentId, 'success');
                    // You can redirect to success page here
                    setTimeout(() => {
                        // window.location.href = '/payment/success?id=' + data.paymentId;
                    }, 3000);
                } else {
                    showStatus('❌ Payment verification failed: ' + data.message, 'error');
                }
            })
            .catch(error => {
                loading.classList.remove('show');
                showStatus('Error verifying payment: ' + error.message, 'error');
            });
        }

        function showStatus(message, type) {
            const statusMessage = document.getElementById('statusMessage');
            statusMessage.textContent = message;
            statusMessage.className = 'status-message ' + type;
        }
    </script>
</body>
</html>