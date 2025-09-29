<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simple Razorpay Payment</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            padding: 50px;
            max-width: 500px;
            width: 100%;
            text-align: center;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 32px;
        }

        .amount {
            font-size: 48px;
            color: #667eea;
            font-weight: bold;
            margin: 30px 0;
        }

        .description {
            color: #666;
            font-size: 18px;
            margin-bottom: 40px;
        }

        .pay-button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 18px 50px;
            font-size: 20px;
            border-radius: 50px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
            text-decoration: none;
            display: inline-block;
            font-weight: 600;
        }

        .pay-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
        }

        .pay-button:active {
            transform: translateY(0);
        }

        .loading {
            display: none;
            color: #667eea;
            margin-top: 20px;
        }

        .loading.show {
            display: block;
        }

        .error {
            color: #e74c3c;
            margin-top: 20px;
            display: none;
        }

        .error.show {
            display: block;
        }

        .powered-by {
            margin-top: 40px;
            color: #999;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Make a Payment</h1>

        <div class="amount">₹<span id="amountDisplay">100</span></div>

        <p class="description">
            Click the button below to proceed with secure payment
        </p>

        <a href="https://rzp.io/rzp/H82EaBe" class="pay-button">
            Pay Now with Razorpay
        </a>

        <p class="powered-by">Powered by Razorpay</p>
    </div>
</body>
</html>