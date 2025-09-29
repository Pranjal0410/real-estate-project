<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Invest in Property - Real Estate Investment Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <style>
        .investment-container {
            max-width: 600px;
            margin: 50px auto;
        }
        .investment-card {
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            border-radius: 10px;
            overflow: hidden;
        }
        .investment-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .amount-input {
            font-size: 24px;
            font-weight: bold;
            text-align: center;
        }
        .amount-buttons {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin: 20px 0;
        }
        .amount-btn {
            padding: 10px 20px;
            border: 2px solid #667eea;
            background: white;
            color: #667eea;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .amount-btn:hover, .amount-btn.active {
            background: #667eea;
            color: white;
        }
        .invest-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 15px 40px;
            font-size: 18px;
            border-radius: 30px;
            color: white;
            cursor: pointer;
            transition: transform 0.3s;
        }
        .invest-btn:hover {
            transform: translateY(-2px);
        }
        .invest-btn:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }
        .loading-spinner {
            display: none;
        }
        .loading .loading-spinner {
            display: inline-block;
        }
        .error-message {
            display: none;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;
        }
        .success-message {
            display: none;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/navbar.jsp"/>

    <div class="container investment-container">
        <div class="investment-card">
            <div class="investment-header">
                <h2><i class="bi bi-currency-rupee"></i> Make an Investment</h2>
                <p class="mb-0">Property ID: ${propertyId}</p>
            </div>

            <div class="card-body p-4">
                <div class="error-message" id="errorMessage"></div>
                <div class="success-message" id="successMessage"></div>

                <div class="mb-4">
                    <label for="investmentAmount" class="form-label fs-5">Investment Amount (₹)</label>
                    <input type="number"
                           class="form-control amount-input"
                           id="investmentAmount"
                           placeholder="Enter amount"
                           min="10000"
                           step="1000"
                           value="50000">

                    <div class="amount-buttons">
                        <button class="amount-btn" data-amount="25000">₹25,000</button>
                        <button class="amount-btn active" data-amount="50000">₹50,000</button>
                        <button class="amount-btn" data-amount="100000">₹1,00,000</button>
                        <button class="amount-btn" data-amount="250000">₹2,50,000</button>
                        <button class="amount-btn" data-amount="500000">₹5,00,000</button>
                    </div>
                </div>

                <div class="investor-details mb-4">
                    <h5>Investor Details</h5>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Name:</strong> ${investor.name}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Email:</strong> ${investor.email}</p>
                        </div>
                    </div>
                </div>

                <div class="text-center">
                    <button class="invest-btn" id="payButton" onclick="initiatePayment()">
                        <span class="loading-spinner spinner-border spinner-border-sm me-2"></span>
                        <span class="btn-text">Proceed to Pay</span>
                    </button>
                </div>

                <div class="mt-4 text-center text-muted">
                    <small>
                        <i class="bi bi-shield-check"></i> Secured by Razorpay<br>
                        Your payment information is encrypted and secure
                    </small>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const razorpayKeyId = '${razorpayKeyId}';
        const propertyId = ${propertyId};
        const investorName = '${investor.name}';
        const investorEmail = '${investor.email}';
        const investorPhone = '${investor.phone}';

        // Amount button functionality
        document.querySelectorAll('.amount-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                document.querySelectorAll('.amount-btn').forEach(b => b.classList.remove('active'));
                this.classList.add('active');
                document.getElementById('investmentAmount').value = this.dataset.amount;
            });
        });

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

        function initiatePayment() {
            const amount = document.getElementById('investmentAmount').value;

            if (!amount || amount < 10000) {
                showError('Minimum investment amount is ₹10,000');
                return;
            }

            const payButton = document.getElementById('payButton');
            payButton.disabled = true;
            payButton.classList.add('loading');

            // Create order via AJAX
            $.ajax({
                url: '/api/payment/create-order',
                type: 'POST',
                data: {
                    propertyId: propertyId,
                    amount: amount
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
                        description: 'Investment in ' + response.propertyName,
                        order_id: response.orderId,
                        prefill: {
                            name: investorName,
                            email: investorEmail,
                            contact: investorPhone || ''
                        },
                        theme: {
                            color: '#667eea'
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
                    showSuccess('Payment successful! Redirecting...');
                    setTimeout(() => {
                        window.location.href = '/investor/payment/success?orderId=' +
                                              razorpayResponse.razorpay_order_id;
                    }, 2000);
                },
                error: function(xhr) {
                    payButton.disabled = false;
                    payButton.classList.remove('loading');
                    document.querySelector('.btn-text').textContent = 'Proceed to Pay';

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