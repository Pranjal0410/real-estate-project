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
    
    <!-- Google Fonts for Shadcn-style typography -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link href="/css/magicbricks-exact.css" rel="stylesheet">
    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, var(--background) 0%, var(--card) 100%);
            min-height: 100vh;
            padding-top: 0;
        }
        
        .login-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: var(--space-6);
        }
        
        .login-card {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: var(--radius-2xl);
            box-shadow: var(--shadow-2xl);
            padding: var(--space-8);
            width: 100%;
            max-width: 420px;
            backdrop-filter: blur(10px);
        }
        
        .login-header {
            text-align: center;
            margin-bottom: var(--space-8);
        }
        
        .login-title {
            font-size: var(--text-3xl);
            font-weight: 800;
            color: var(--foreground);
            margin-bottom: var(--space-2);
        }
        
        .login-subtitle {
            color: var(--muted-foreground);
            font-size: var(--text-sm);
        }
        
        .form-field {
            margin-bottom: var(--space-6);
        }
        
        .form-field:last-child {
            margin-bottom: 0;
        }
        
        .login-actions {
            display: flex;
            flex-direction: column;
            gap: var(--space-4);
            margin-top: var(--space-6);
        }
        
        .divider {
            display: flex;
            align-items: center;
            text-align: center;
            margin: var(--space-6) 0;
        }
        
        .divider::before,
        .divider::after {
            content: '';
            flex: 1;
            border-bottom: 1px solid var(--border);
        }
        
        .divider span {
            padding: 0 var(--space-3);
            color: var(--muted-foreground);
            font-size: var(--text-sm);
        }
        
        .social-login {
            display: flex;
            gap: var(--space-3);
        }
        
        .forgot-password a {
            color: var(--primary);
            text-decoration: none;
            font-size: var(--text-sm);
            transition: color var(--duration-150) ease;
        }
        
        .forgot-password a:hover {
            color: var(--accent);
        }
        
        .signup-link {
            text-align: center;
            margin-top: var(--space-6);
            padding-top: var(--space-6);
            border-top: 1px solid var(--border);
        }
        
        .signup-link a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 500;
        }
        
        .signup-link a:hover {
            color: var(--accent);
        }
        
        .error-message {
            background: rgba(239, 68, 68, 0.1);
            color: #fca5a5;
            border: 1px solid rgba(239, 68, 68, 0.3);
            padding: var(--space-3) var(--space-4);
            border-radius: var(--radius-md);
            font-size: var(--text-sm);
            margin-bottom: var(--space-4);
            display: flex;
            align-items: center;
        }
        
        .form-check-input {
            background-color: var(--input);
            border-color: var(--border);
        }
        
        .form-check-input:checked {
            background-color: var(--primary);
            border-color: var(--primary);
        }
        
        .form-check-label {
            color: var(--muted-foreground);
            font-size: var(--text-sm);
        }
    </style>
</head>
<body>
    <!-- Login Section -->
    <div class="login-container">
        <div class="login-card animate-fadeInScale">
            <div class="login-header">
                <div class="d-flex justify-content-center align-items-center mb-3">
                    <i class="bi bi-house-door-fill me-2" style="color: var(--primary); font-size: var(--text-2xl);"></i>
                    <h2 style="color: var(--primary); font-weight: 800; margin: 0;">PropInvest</h2>
                </div>
                <h1 class="login-title">Welcome Back</h1>
                <p class="login-subtitle">Sign in to your PropInvest account</p>
            </div>
            
            <!-- Error Message -->
            <c:if test="${param.error != null}">
                <div class="error-message">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    Invalid username or password. Please try again.
                </div>
            </c:if>
            
            <!-- Login Form -->
            <form id="loginForm" action="/login" method="post" novalidate>
                <div class="form-field">
                    <div class="form-group">
                        <label for="username" class="form-label">
                            <i class="bi bi-person me-2"></i>Email or Username
                        </label>
                        <input type="text" 
                               class="form-input focus-ring" 
                               id="username" 
                               name="username" 
                               placeholder="Enter your email or username"
                               required>
                    </div>
                </div>
                
                <div class="form-field">
                    <div class="form-group">
                        <label for="password" class="form-label">
                            <i class="bi bi-lock me-2"></i>Password
                        </label>
                        <input type="password" 
                               class="form-input focus-ring" 
                               id="password" 
                               name="password" 
                               placeholder="Enter your password"
                               required>
                    </div>
                </div>
                
                <div class="form-field">
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="form-check">
                            <input type="checkbox" 
                                   class="form-check-input" 
                                   id="remember" 
                                   name="remember-me">
                            <label class="form-check-label" for="remember">
                                Remember me
                            </label>
                        </div>
                        
                        <div class="forgot-password">
                            <a href="/forgot-password">Forgot Password?</a>
                        </div>
                    </div>
                </div>
                
                <div class="login-actions">
                    <button type="submit" class="btn btn-primary btn-lg w-100">
                        <i class="bi bi-box-arrow-in-right me-2"></i>
                        Sign In
                    </button>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            
            <div class="divider">
                <span>or continue with</span>
            </div>
            
            <div class="social-login">
                <button type="button" class="btn btn-outline flex-1" onclick="alert('Google login coming soon!')">
                    <i class="bi bi-google"></i>
                </button>
                <button type="button" class="btn btn-outline flex-1" onclick="alert('Facebook login coming soon!')">
                    <i class="bi bi-facebook"></i>
                </button>
                <button type="button" class="btn btn-outline flex-1" onclick="alert('Apple login coming soon!')">
                    <i class="bi bi-apple"></i>
                </button>
            </div>
            
            <div class="signup-link">
                <span style="color: var(--muted-foreground); font-size: var(--text-sm);">
                    Don't have an account? 
                    <a href="/register">Create an account</a>
                </span>
            </div>
        </div>
    </div>
    
    <!-- Scripts from CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    
    <script>
        // Form enhancement and validation
        $(document).ready(function() {
            // Auto-focus first input
            $('#username').focus();
            
            // Form validation and submission - use traditional form submission
            $('#loginForm').on('submit', function(e) {
                const username = $('#username').val().trim();
                const password = $('#password').val().trim();

                if (!username || !password) {
                    e.preventDefault();
                    $('.error-message').remove();

                    const errorHtml = `
                        <div class="error-message">
                            <i class="bi bi-exclamation-triangle me-2"></i>
                            Please fill in all required fields.
                        </div>
                    `;

                    $('.login-header').after(errorHtml);
                    return false;
                }

                // Show loading state
                const submitBtn = $('button[type="submit"]');
                submitBtn.prop('disabled', true);
                submitBtn.html('<i class="spinner-border spinner-border-sm me-2"></i>Signing in...');

                // Let the form submit naturally - Spring Security will handle it
                return true;
            });
            
            // Clear errors on input
            $('#username, #password').on('input', function() {
                $('.error-message').fadeOut(300);
            });
            
            // Enhanced form interactions
            $('.form-input').on('focus', function() {
                $(this).closest('.form-group').addClass('focused');
            });
            
            $('.form-input').on('blur', function() {
                $(this).closest('.form-group').removeClass('focused');
            });
        });
    </script>
</body>
</html>