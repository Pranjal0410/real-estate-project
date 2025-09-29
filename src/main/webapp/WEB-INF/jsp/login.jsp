<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Login to PropInvest - Access your real estate investment dashboard and portfolio">
    <title>Login - PropInvest</title>

    <!-- Bootstrap 5 CSS from CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&family=Poppins:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary: #7c3aed;
            --primary-dark: #6d28d9;
            --primary-light: #a78bfa;
            --secondary: #10b981;
            --background: #0f0f1e;
            --card: #1a1a2e;
            --card-hover: #252542;
            --text: #ffffff;
            --text-muted: #a0a0b8;
            --border: #2a2a4a;
            --error: #ef4444;
            --success: #10b981;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: var(--background);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow-x: hidden;
            position: relative;
        }

        /* Animated Background */
        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background:
                radial-gradient(circle at 20% 80%, rgba(124, 58, 237, 0.3) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(16, 185, 129, 0.2) 0%, transparent 50%),
                radial-gradient(circle at 40% 40%, rgba(167, 139, 250, 0.1) 0%, transparent 50%);
            animation: backgroundShift 20s ease-in-out infinite;
            z-index: -2;
        }

        /* Floating particles */
        .particle {
            position: fixed;
            pointer-events: none;
            opacity: 0.1;
            z-index: -1;
        }

        .particle:nth-child(1) {
            width: 80px;
            height: 80px;
            background: var(--primary);
            border-radius: 50%;
            top: 10%;
            left: 10%;
            animation: float 20s infinite ease-in-out;
        }

        .particle:nth-child(2) {
            width: 60px;
            height: 60px;
            background: var(--secondary);
            border-radius: 50%;
            top: 70%;
            right: 10%;
            animation: float 25s infinite ease-in-out reverse;
        }

        .particle:nth-child(3) {
            width: 100px;
            height: 100px;
            background: var(--primary-light);
            border-radius: 50%;
            bottom: 10%;
            left: 50%;
            animation: float 30s infinite ease-in-out;
        }

        @keyframes float {
            0%, 100% {
                transform: translateY(0) translateX(0) rotate(0deg);
            }
            33% {
                transform: translateY(-30px) translateX(30px) rotate(120deg);
            }
            66% {
                transform: translateY(30px) translateX(-30px) rotate(240deg);
            }
        }

        @keyframes backgroundShift {
            0%, 100% {
                transform: scale(1) rotate(0deg);
            }
            50% {
                transform: scale(1.1) rotate(5deg);
            }
        }

        /* Main Container */
        .login-container {
            width: 100%;
            max-width: 1400px;
            padding: 20px;
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 60px;
            align-items: center;
        }

        /* Left Side - Welcome Section */
        .welcome-section {
            color: var(--text);
            padding: 40px;
            animation: fadeInLeft 0.8s ease;
        }

        .logo {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 50px;
        }

        .logo i {
            font-size: 40px;
            color: var(--primary);
        }

        .logo-text {
            font-size: 28px;
            font-weight: 800;
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .welcome-title {
            font-size: 48px;
            font-weight: 800;
            margin-bottom: 20px;
            line-height: 1.2;
        }

        .welcome-subtitle {
            font-size: 20px;
            color: var(--text-muted);
            margin-bottom: 40px;
            line-height: 1.6;
        }

        .features-list {
            list-style: none;
            padding: 0;
        }

        .feature-item {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 20px;
            color: var(--text-muted);
            font-size: 16px;
        }

        .feature-item i {
            color: var(--secondary);
            font-size: 20px;
        }

        /* Right Side - Login Card */
        .login-card {
            background: rgba(26, 26, 46, 0.9);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border);
            border-radius: 24px;
            padding: 50px 40px;
            box-shadow:
                0 20px 60px rgba(0, 0, 0, 0.3),
                0 0 100px rgba(124, 58, 237, 0.1);
            animation: fadeInRight 0.8s ease;
        }

        .card-header {
            text-align: center;
            margin-bottom: 40px;
        }

        .card-title {
            font-size: 32px;
            font-weight: 700;
            color: var(--text);
            margin-bottom: 10px;
        }

        .card-subtitle {
            color: var(--text-muted);
            font-size: 16px;
        }

        /* Form Styles */
        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            color: var(--text-muted);
            font-size: 14px;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .input-wrapper {
            position: relative;
        }

        .input-icon {
            position: absolute;
            left: 16px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-muted);
            font-size: 18px;
            transition: color 0.3s ease;
        }

        .form-input {
            width: 100%;
            padding: 14px 16px 14px 45px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid var(--border);
            border-radius: 12px;
            color: var(--text);
            font-size: 16px;
            transition: all 0.3s ease;
        }

        .form-input:focus {
            outline: none;
            background: rgba(255, 255, 255, 0.08);
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(124, 58, 237, 0.1);
        }

        .form-input:focus ~ .input-icon {
            color: var(--primary);
        }

        .form-input::placeholder {
            color: rgba(160, 160, 184, 0.5);
        }

        /* Password toggle */
        .password-toggle {
            position: absolute;
            right: 16px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: var(--text-muted);
            cursor: pointer;
            padding: 5px;
            transition: color 0.3s ease;
        }

        .password-toggle:hover {
            color: var(--primary);
        }

        /* Remember & Forgot */
        .form-options {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .checkbox-wrapper {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .checkbox-wrapper input[type="checkbox"] {
            width: 18px;
            height: 18px;
            accent-color: var(--primary);
            cursor: pointer;
        }

        .checkbox-wrapper label {
            color: var(--text-muted);
            font-size: 14px;
            cursor: pointer;
            user-select: none;
        }

        .forgot-link {
            color: var(--primary);
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .forgot-link:hover {
            color: var(--primary-light);
            text-decoration: underline;
        }

        /* Submit Button */
        .btn-login {
            width: 100%;
            padding: 14px 24px;
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(124, 58, 237, 0.4);
        }

        .btn-login:active {
            transform: translateY(0);
        }

        /* Divider */
        .divider {
            display: flex;
            align-items: center;
            margin: 30px 0;
            color: var(--text-muted);
            font-size: 14px;
        }

        .divider::before,
        .divider::after {
            content: '';
            flex: 1;
            height: 1px;
            background: var(--border);
        }

        .divider span {
            padding: 0 16px;
        }

        /* Social Login */
        .social-buttons {
            display: flex;
            gap: 12px;
            margin-bottom: 30px;
        }

        .btn-social {
            flex: 1;
            padding: 12px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid var(--border);
            border-radius: 12px;
            color: var(--text);
            font-size: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .btn-social:hover {
            background: rgba(255, 255, 255, 0.1);
            border-color: var(--primary);
            transform: translateY(-2px);
        }

        /* Sign Up Link */
        .signup-link {
            text-align: center;
            color: var(--text-muted);
            font-size: 14px;
        }

        .signup-link a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }

        .signup-link a:hover {
            color: var(--primary-light);
            text-decoration: underline;
        }

        /* Error Message */
        .error-message {
            background: rgba(239, 68, 68, 0.1);
            border: 1px solid rgba(239, 68, 68, 0.3);
            border-radius: 12px;
            padding: 14px 16px;
            margin-bottom: 20px;
            color: #fca5a5;
            font-size: 14px;
            display: flex;
            align-items: center;
            gap: 10px;
            animation: shake 0.5s ease;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        /* Animations */
        @keyframes fadeInLeft {
            from {
                opacity: 0;
                transform: translateX(-30px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        @keyframes fadeInRight {
            from {
                opacity: 0;
                transform: translateX(30px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        /* Mobile Responsive */
        @media (max-width: 968px) {
            .login-container {
                grid-template-columns: 1fr;
                max-width: 500px;
            }

            .welcome-section {
                display: none;
            }

            .login-card {
                padding: 40px 30px;
            }
        }

        @media (max-width: 480px) {
            .login-card {
                padding: 30px 20px;
            }

            .card-title {
                font-size: 28px;
            }

            .welcome-title {
                font-size: 36px;
            }
        }

        /* Loading Spinner */
        .spinner {
            display: inline-block;
            width: 18px;
            height: 18px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            border-top-color: white;
            animation: spin 0.6s linear infinite;
            margin-right: 8px;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <!-- Floating Particles -->
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>

    <div class="login-container">
        <!-- Left Side - Welcome Section -->
        <div class="welcome-section">
            <div class="logo">
                <i class="bi bi-buildings-fill"></i>
                <span class="logo-text">PropInvest</span>
            </div>

            <h1 class="welcome-title">
                Welcome to the Future of Real Estate Investment
            </h1>

            <p class="welcome-subtitle">
                Join thousands of investors making smarter property decisions with our advanced platform
            </p>

            <ul class="features-list">
                <li class="feature-item">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>Smart property recommendations based on AI</span>
                </li>
                <li class="feature-item">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>Real-time market analysis and insights</span>
                </li>
                <li class="feature-item">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>Secure transactions with Razorpay</span>
                </li>
                <li class="feature-item">
                    <i class="bi bi-check-circle-fill"></i>
                    <span>24/7 customer support</span>
                </li>
            </ul>
        </div>

        <!-- Right Side - Login Card -->
        <div class="login-card">
            <div class="card-header">
                <h2 class="card-title">Sign In</h2>
                <p class="card-subtitle">Enter your credentials to continue</p>
            </div>

            <!-- Error Message -->
            <c:if test="${param.error != null}">
                <div class="error-message">
                    <i class="bi bi-exclamation-circle-fill"></i>
                    Invalid username or password. Please try again.
                </div>
            </c:if>

            <!-- Login Form -->
            <form id="loginForm" action="/login" method="post" novalidate>
                <div class="form-group">
                    <label for="username" class="form-label">Email or Username</label>
                    <div class="input-wrapper">
                        <i class="bi bi-person input-icon"></i>
                        <input type="text"
                               class="form-input"
                               id="username"
                               name="username"
                               placeholder="Enter your email or username"
                               required
                               autocomplete="username">
                    </div>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Password</label>
                    <div class="input-wrapper">
                        <i class="bi bi-lock input-icon"></i>
                        <input type="password"
                               class="form-input"
                               id="password"
                               name="password"
                               placeholder="Enter your password"
                               required
                               autocomplete="current-password">
                        <button type="button" class="password-toggle" onclick="togglePassword()">
                            <i class="bi bi-eye" id="passwordToggleIcon"></i>
                        </button>
                    </div>
                </div>

                <div class="form-options">
                    <div class="checkbox-wrapper">
                        <input type="checkbox" id="remember" name="remember-me">
                        <label for="remember">Remember me</label>
                    </div>
                    <a href="/forgot-password" class="forgot-link">Forgot Password?</a>
                </div>

                <button type="submit" class="btn-login" id="loginBtn">
                    Sign In
                </button>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>

            <div class="divider">
                <span>or continue with</span>
            </div>

            <div class="social-buttons">
                <button type="button" class="btn-social" onclick="socialLogin('google')">
                    <i class="bi bi-google"></i>
                </button>
                <button type="button" class="btn-social" onclick="socialLogin('facebook')">
                    <i class="bi bi-facebook"></i>
                </button>
                <button type="button" class="btn-social" onclick="socialLogin('apple')">
                    <i class="bi bi-apple"></i>
                </button>
                <button type="button" class="btn-social" onclick="socialLogin('linkedin')">
                    <i class="bi bi-linkedin"></i>
                </button>
            </div>

            <div class="signup-link">
                Don't have an account? <a href="/register">Sign Up Now</a>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Password Toggle
        function togglePassword() {
            const passwordInput = document.getElementById('password');
            const toggleIcon = document.getElementById('passwordToggleIcon');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleIcon.classList.remove('bi-eye');
                toggleIcon.classList.add('bi-eye-slash');
            } else {
                passwordInput.type = 'password';
                toggleIcon.classList.remove('bi-eye-slash');
                toggleIcon.classList.add('bi-eye');
            }
        }

        // Social Login
        function socialLogin(provider) {
            // Show coming soon message
            alert(`${provider.charAt(0).toUpperCase() + provider.slice(1)} login coming soon!`);
        }

        // Form Validation and Submission
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();
            const loginBtn = document.getElementById('loginBtn');

            if (!username || !password) {
                e.preventDefault();

                // Remove existing error
                const existingError = document.querySelector('.error-message');
                if (existingError) {
                    existingError.remove();
                }

                // Add new error
                const errorHtml = `
                    <div class="error-message">
                        <i class="bi bi-exclamation-circle-fill"></i>
                        Please fill in all required fields
                    </div>
                `;
                document.querySelector('.card-header').insertAdjacentHTML('afterend', errorHtml);
                return false;
            }

            // Show loading state
            loginBtn.disabled = true;
            loginBtn.innerHTML = '<span class="spinner"></span>Signing in...';
        });

        // Auto-focus first input
        document.getElementById('username').focus();

        // Clear error on input
        document.querySelectorAll('.form-input').forEach(input => {
            input.addEventListener('input', function() {
                const errorMessage = document.querySelector('.error-message');
                if (errorMessage) {
                    errorMessage.style.opacity = '0';
                    setTimeout(() => errorMessage.remove(), 300);
                }
            });
        });

        // Add input animation
        document.querySelectorAll('.form-input').forEach(input => {
            input.addEventListener('focus', function() {
                this.parentElement.style.transform = 'scale(1.02)';
            });

            input.addEventListener('blur', function() {
                this.parentElement.style.transform = 'scale(1)';
            });
        });
    </script>
</body>
</html>