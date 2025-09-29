<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Failed - Real Estate Investment Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .failure-container {
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
        }
        .failure-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 0 30px rgba(0,0,0,0.1);
            padding: 40px;
        }
        .failure-icon {
            width: 100px;
            height: 100px;
            background: linear-gradient(135deg, #ff6b6b 0%, #ff8e53 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 30px;
            animation: shake 0.5s;
        }
        .failure-icon i {
            font-size: 48px;
            color: white;
        }
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }
        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
        .help-section {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 30px 0;
            text-align: left;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/navbar.jsp"/>

    <div class="container failure-container">
        <div class="failure-card">
            <div class="failure-icon">
                <i class="bi bi-x-lg"></i>
            </div>

            <h2 class="mb-3 text-danger">Payment Failed</h2>
            <p class="lead">We couldn't process your payment at this time.</p>

            <div class="alert alert-warning">
                <i class="bi bi-exclamation-triangle"></i>
                <strong>Don't worry!</strong> No amount has been deducted from your account.
            </div>

            <div class="help-section">
                <h5 class="mb-3">Common reasons for payment failure:</h5>
                <ul class="text-start">
                    <li>Insufficient funds in your account</li>
                    <li>Card details entered incorrectly</li>
                    <li>Transaction limit exceeded</li>
                    <li>Network connectivity issues</li>
                    <li>Bank server downtime</li>
                </ul>

                <hr>

                <h6>What you can do:</h6>
                <ul class="text-start">
                    <li>Check your card details and try again</li>
                    <li>Ensure sufficient balance in your account</li>
                    <li>Try using a different payment method</li>
                    <li>Contact your bank if the issue persists</li>
                </ul>
            </div>

            <div class="action-buttons">
                <button onclick="history.back()" class="btn btn-primary">
                    <i class="bi bi-arrow-repeat"></i> Try Again
                </button>
                <a href="/properties" class="btn btn-outline-primary">
                    <i class="bi bi-house"></i> Browse Properties
                </a>
                <a href="/support" class="btn btn-outline-secondary">
                    <i class="bi bi-headset"></i> Contact Support
                </a>
            </div>
        </div>
    </div>
</body>
</html>