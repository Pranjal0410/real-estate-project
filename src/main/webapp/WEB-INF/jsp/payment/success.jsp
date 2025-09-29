<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Successful - Real Estate Investment Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .success-container {
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
        }
        .success-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 0 30px rgba(0,0,0,0.1);
            padding: 40px;
        }
        .success-icon {
            width: 100px;
            height: 100px;
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
            animation: scaleIn 0.5s ease-out;
        }
        .success-icon i {
            font-size: 48px;
            color: white;
        }
        @keyframes scaleIn {
            from {
                transform: scale(0);
            }
            to {
                transform: scale(1);
            }
        }
        .confetti {
            position: fixed;
            width: 10px;
            height: 10px;
            background: linear-gradient(45deg, #ff6b6b, #4ecdc4, #45b7d1);
            animation: fall linear infinite;
        }
        @keyframes fall {
            to {
                transform: translateY(100vh) rotate(360deg);
            }
        }
        .payment-details {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 30px 0;
            text-align: left;
        }
        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/navbar.jsp"/>

    <div class="container success-container">
        <div class="success-card">
            <div class="success-icon">
                <i class="bi bi-check-lg"></i>
            </div>

            <h2 class="mb-3">Payment Successful!</h2>
            <p class="lead">Your investment has been processed successfully.</p>

            <c:if test="${not empty payment}">
                <div class="payment-details">
                    <h5 class="mb-3">Transaction Details</h5>
                    <div class="row">
                        <div class="col-6">
                            <p class="mb-2"><strong>Property:</strong></p>
                            <p>${payment.property.title}</p>
                        </div>
                        <div class="col-6">
                            <p class="mb-2"><strong>Amount:</strong></p>
                            <p class="fs-4 text-success">₹<fmt:formatNumber value="${payment.investmentAmount}" type="number" maxFractionDigits="2"/></p>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-6">
                            <p class="mb-2"><strong>Transaction ID:</strong></p>
                            <p class="small text-muted">${payment.razorpayPaymentId}</p>
                        </div>
                        <div class="col-6">
                            <p class="mb-2"><strong>Date:</strong></p>
                            <p><fmt:formatDate value="${payment.paidAt}" pattern="dd MMM yyyy, HH:mm"/></p>
                        </div>
                    </div>
                </div>

                <div class="alert alert-info">
                    <i class="bi bi-envelope"></i> A confirmation email has been sent to your registered email address.
                </div>
            </c:if>

            <div class="action-buttons">
                <a href="/investor/payments" class="btn btn-outline-primary">
                    <i class="bi bi-clock-history"></i> View History
                </a>
                <a href="/properties" class="btn btn-primary">
                    <i class="bi bi-search"></i> Browse More Properties
                </a>
                <c:if test="${not empty payment}">
                    <button onclick="downloadReceipt(${payment.id})" class="btn btn-success">
                        <i class="bi bi-download"></i> Download Receipt
                    </button>
                </c:if>
            </div>
        </div>
    </div>

    <script>
        // Create confetti effect
        function createConfetti() {
            for (let i = 0; i < 50; i++) {
                const confetti = document.createElement('div');
                confetti.className = 'confetti';
                confetti.style.left = Math.random() * 100 + '%';
                confetti.style.animationDuration = Math.random() * 3 + 2 + 's';
                confetti.style.animationDelay = Math.random() * 2 + 's';
                document.body.appendChild(confetti);

                setTimeout(() => confetti.remove(), 5000);
            }
        }

        function downloadReceipt(paymentId) {
            window.open('/investor/payment/receipt/' + paymentId, '_blank');
        }

        // Run confetti on load
        window.addEventListener('load', createConfetti);
    </script>
</body>
</html>