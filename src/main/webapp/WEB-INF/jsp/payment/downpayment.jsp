<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pay Downpayment - Real Estate Investment Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .downpayment-container {
            max-width: 500px;
            width: 100%;
            padding: 20px;
        }
        .downpayment-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
            transform: translateY(0);
            transition: transform 0.3s;
        }
        .downpayment-card:hover {
            transform: translateY(-5px);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
            position: relative;
        }
        .card-header h2 {
            margin: 0;
            font-size: 28px;
            font-weight: bold;
        }
        .card-header p {
            margin: 10px 0 0;
            opacity: 0.9;
        }
        .badge-downpayment {
            background: rgba(255,255,255,0.2);
            padding: 5px 15px;
            border-radius: 20px;
            display: inline-block;
            margin-top: 10px;
            font-size: 14px;
        }
        .card-body {
            padding: 40px;
        }
        .amount-display {
            text-align: center;
            margin-bottom: 30px;
        }
        .amount-display .label {
            color: #666;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 10px;
        }
        .amount-display .amount {
            font-size: 48px;
            font-weight: bold;
            color: #667eea;
            line-height: 1;
        }
        .amount-display .currency {
            font-size: 24px;
            vertical-align: top;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            color: #666;
            font-size: 14px;
        }
        .info-value {
            font-weight: 600;
            color: #333;
        }
        .benefits-list {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
        }
        .benefits-list h5 {
            color: #667eea;
            font-size: 16px;
            margin-bottom: 15px;
        }
        .benefits-list ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .benefits-list li {
            padding: 8px 0;
            padding-left: 25px;
            position: relative;
        }
        .benefits-list li:before {
            content: "✓";
            position: absolute;
            left: 0;
            color: #28a745;
            font-weight: bold;
        }
        .pay-button {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .pay-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);
        }
        .pay-button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }
        .security-note {
            text-align: center;
            margin-top: 20px;
            color: #666;
            font-size: 12px;
        }
        .security-note i {
            color: #28a745;
            margin-right: 5px;
        }
        .loading-spinner {
            display: none;
            margin-right: 10px;
        }
        .loading .loading-spinner {
            display: inline-block;
        }
        .error-alert {
            display: none;
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .success-alert {
            display: none;
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: white;
            text-decoration: none;
            opacity: 0.8;
        }
        .back-link:hover {
            opacity: 1;
            color: white;
        }
    </style>
</head>
<body>
    <div class="downpayment-container">
        <div class="downpayment-card">
            <div class="card-header">
                <h2><i class="bi bi-shield-check"></i> Secure Downpayment</h2>
                <p>Property ID: ${propertyId}</p>
                <span class="badge-downpayment">One-Time Booking Fee</span>
            </div>

            <div class="card-body">
                <div class="error-alert" id="errorMessage"></div>
                <div class="success-alert" id="successMessage"></div>

                <div class="amount-display">
                    <div class="label">Downpayment Amount</div>
                    <div class="amount">
                        <span class="currency">₹</span>500
                    </div>
                </div>

                <div class="benefits-list">
                    <h5>What You Get:</h5>
                    <ul>
                        <li>Priority booking for this property</li>
                        <li>Lock current price for 30 days</li>
                        <li>Detailed property reports</li>
                        <li>Exclusive investor benefits</li>
                        <li>Refundable within 7 days</li>
                    </ul>
                </div>

                <div class="info-row">
                    <span class="info-label">Investor Name</span>
                    <span class="info-value">${investor.name}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Email</span>
                    <span class="info-value">${investor.email}</span>
                </div>
                <div class="info-row">
                    <span class="info-label">Payment Method</span>
                    <span class="info-value">Razorpay Secure</span>
                </div>

                <button class="pay-button" id="payButton" onclick="initiateDownpayment()">
                    <span class="loading-spinner spinner-border spinner-border-sm"></span>
                    <span class="btn-text">Pay ₹500 Now</span>
                </button>

                <div class="security-note">
                    <i class="bi bi-shield-lock"></i>
                    Your payment is secured by Razorpay
                    <br>
                    256-bit SSL Encryption
                </div>
            </div>
        </div>

        <a href="/properties" class="back-link">
            <i class="bi bi-arrow-left"></i> Back to Properties
        </a>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        const razorpayKeyId = '${razorpayKeyId}';
        const propertyId = ${propertyId};
        const investorName = '${investor.name}';
        const investorEmail = '${investor.email}';
        const investorPhone = '${investor.phone}';

        function showError(message) {
            const errorDiv = document.getElementById('errorMessage');
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
            setTimeout(() => {
                errorDiv.style.display = 'none';
            }, 5000);
        }

        function showSuccess(message) {
            const successDiv = document.getElementById('successMessage');
            successDiv.textContent = message;
            successDiv.style.display = 'block';
        }

        function initiateDownpayment() {
            const payButton = document.getElementById('payButton');
            payButton.disabled = true;
            payButton.classList.add('loading');

            // Create downpayment order via AJAX
            $.ajax({
                url: '/api/payment/create-downpayment',
                type: 'POST',
                data: {
                    propertyId: propertyId
                },
                success: function(response) {
                    payButton.disabled = false;
                    payButton.classList.remove('loading');

                    // Initialize Razorpay
                    const options = {
                        key: razorpayKeyId,
                        amount: response.amount * 100, // Amount in paise
                        currency: response.currency,
                        name: 'Real Estate Investment Platform',
                        description: 'Downpayment for Property #' + propertyId,
                        order_id: response.orderId,
                        prefill: {
                            name: investorName,
                            email: investorEmail,
                            contact: investorPhone || ''
                        },
                        theme: {
                            color: '#667eea'
                        },
                        notes: {
                            propertyId: propertyId,
                            paymentType: 'DOWNPAYMENT'
                        },
                        handler: function(razorpayResponse) {
                            // Payment successful, verify on backend
                            verifyPayment(razorpayResponse);
                        },
                        modal: {
                            ondismiss: function() {
                                payButton.disabled = false;
                                showError('Payment cancelled by user');
                            }
                        }
                    };

                    const rzp = new Razorpay(options);
                    rzp.on('payment.failed', function(response) {
                        payButton.disabled = false;
                        showError('Payment failed: ' + response.error.description);
                    });
                    rzp.open();
                },
                error: function(xhr) {
                    payButton.disabled = false;
                    payButton.classList.remove('loading');

                    let errorMessage = 'Failed to create payment order';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showError(errorMessage);
                }
            });
        }

        function verifyPayment(razorpayResponse) {
            const payButton = document.getElementById('payButton');
            payButton.disabled = true;
            payButton.classList.add('loading');
            document.querySelector('.btn-text').textContent = 'Verifying Payment...';

            $.ajax({
                url: '/api/payment/verify',
                type: 'POST',
                data: {
                    razorpay_order_id: razorpayResponse.razorpay_order_id,
                    razorpay_payment_id: razorpayResponse.razorpay_payment_id,
                    razorpay_signature: razorpayResponse.razorpay_signature
                },
                success: function(response) {
                    showSuccess('Downpayment successful! Redirecting...');

                    // Show success animation
                    document.querySelector('.downpayment-card').style.transform = 'scale(0.95)';
                    setTimeout(() => {
                        document.querySelector('.downpayment-card').style.transform = 'scale(1)';
                    }, 200);

                    setTimeout(() => {
                        window.location.href = '/investor/payment/success?orderId=' +
                                              razorpayResponse.razorpay_order_id;
                    }, 2000);
                },
                error: function(xhr) {
                    payButton.disabled = false;
                    payButton.classList.remove('loading');
                    document.querySelector('.btn-text').textContent = 'Pay ₹500 Now';

                    let errorMessage = 'Payment verification failed';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMessage = xhr.responseJSON.message;
                    }
                    showError(errorMessage);
                }
            });
        }
    </script>
</body>
</html>