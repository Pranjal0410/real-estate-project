<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Razorpay Payment Demo</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }

        .payment-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 500px;
            width: 100%;
            text-align: center;
        }

        .logo {
            font-size: 50px;
            margin-bottom: 10px;
        }

        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 28px;
        }

        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 16px;
        }

        .product-info {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
        }

        .product-title {
            font-size: 18px;
            color: #333;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .amount {
            font-size: 36px;
            color: #667eea;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .amount-description {
            color: #888;
            font-size: 14px;
        }

        .pay-button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 15px 40px;
            font-size: 18px;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
            font-weight: 600;
            letter-spacing: 0.5px;
        }

        .pay-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
        }

        .pay-button:active {
            transform: translateY(0);
        }

        .pay-button:disabled {
            opacity: 0.7;
            cursor: not-allowed;
        }

        .security-info {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .security-badge {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            color: #666;
            font-size: 14px;
        }

        .lock-icon {
            font-size: 16px;
        }

        .test-mode-banner {
            background: #fff3cd;
            color: #856404;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
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

        .loading {
            display: none;
            color: #667eea;
            margin-top: 20px;
        }

        .loading.show {
            display: block;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        .spinner {
            display: inline-block;
            width: 20px;
            height: 20px;
            border: 3px solid #f3f3f3;
            border-top: 3px solid #667eea;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin-right: 10px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
    <div class="payment-container">
        <div class="logo">💳</div>
        <h1>Razorpay Payment Demo</h1>
        <p class="subtitle">Complete Working Example</p>

        <div class="test-mode-banner">
            ⚠️ TEST MODE - Use test cards for payment
        </div>

        <div class="product-info">
            <div class="product-title">Demo Product</div>
            <div class="amount">₹100</div>
            <div class="amount-description">One-time payment</div>
        </div>

        <button id="payButton" class="pay-button" onclick="initiatePayment()">
            Pay Now
        </button>

        <div class="loading" id="loading">
            <div class="spinner"></div>
            Processing...
        </div>

        <div id="statusMessage" class="status-message"></div>

        <div class="security-info">
            <div class="security-badge">
                <span class="lock-icon">🔒</span>
                <span>Secured by Razorpay</span>
            </div>
        </div>
    </div>

    <!-- Razorpay Checkout Script -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>

    <script>
        function initiatePayment() {
            const payButton = document.getElementById('payButton');
            const loading = document.getElementById('loading');
            const statusMessage = document.getElementById('statusMessage');

            // Reset status
            statusMessage.className = 'status-message';
            statusMessage.textContent = '';

            // Show loading
            payButton.disabled = true;
            loading.classList.add('show');

            // Call backend to create order
            fetch('/api/razorpay/create-order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
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
                        description: 'Demo Payment',
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
                            address: 'Demo Address'
                        },
                        theme: {
                            color: '#667eea'
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
                    showStatus('✅ Payment verified successfully! Payment ID: ' + data.paymentId, 'success');
                    // You can redirect to success page here
                    // window.location.href = '/payment/success';
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

        // Test card details (for reference)
        console.log('Test Card Details:');
        console.log('Card Number: 4111 1111 1111 1111');
        console.log('Expiry: Any future date');
        console.log('CVV: Any 3 digits');
        console.log('OTP: Not required in test mode');
    </script>
</body>
</html>