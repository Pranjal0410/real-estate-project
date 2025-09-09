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
            max-width: 400px;
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
        
        .forgot-password {
            text-align: center;
            margin-top: var(--space-4);
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
            background: var(--destructive);
            color: var(--destructive-foreground);
            padding: var(--space-3) var(--space-4);
            border-radius: var(--radius-md);
            font-size: var(--text-sm);
            margin-bottom: var(--space-4);
        }
    </style>
</head>
<body>
    <!-- Include Navigation -->
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <!-- Login Section -->
    <div class="login-container">
        <div class="login-card animate-fadeInScale">
            <div class="login-header">
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
            <form action="/login" method="post" novalidate>
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
                            <label class="form-check-label" for="remember" style="color: var(--muted-foreground); font-size: var(--text-sm);">
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
                <button class="btn btn-outline flex-1">
                    <i class="bi bi-google"></i>
                </button>
                <button class="btn btn-outline flex-1">
                    <i class="bi bi-facebook"></i>
                </button>
                <button class="btn btn-outline flex-1">
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
            
            // Form validation
            $('form').on('submit', function(e) {
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
            });
            
            // Clear errors on input
            $('#username, #password').on('input', function() {
                $('.error-message').fadeOut(300);
            });
        });
    </script>
</body>
</html>
                    <!-- Login Card -->
                    <div class="card rounded-lg shadow-lg-custom border-0" data-tilt data-tilt-max="6" data-tilt-speed="400">
                        <div class="card-body p-5">
                            <!-- Header -->
                            <div class="text-center mb-4">
                                <div class="d-flex justify-content-center align-items-center mb-3">
                                    <i class="bi bi-house-door-fill text-primary fs-3 me-2"></i>
                                    <h2 class="text-gradient mb-0">PropInvest</h2>
                                </div>
                                <h3 class="card-title mb-2">Welcome Back</h3>
                                <p class="text-muted">Sign in to access your investment dashboard</p>
                            </div>
                            
                            <!-- Alert Message -->
                            <div id="alertMessage" class="alert d-none rounded-lg"></div>
                            
                            <!-- Login Form -->
                            <form id="loginForm">
                                <div class="mb-4">
                                    <label for="username" class="form-label">
                                        <i class="bi bi-person me-1"></i>Username or Email
                                    </label>
                                    <input type="text" 
                                           class="form-control form-control-lg" 
                                           id="username" 
                                           name="username" 
                                           placeholder="Enter your username or email"
                                           required>
                                </div>
                                
                                <div class="mb-4">
                                    <label for="password" class="form-label">
                                        <i class="bi bi-lock me-1"></i>Password
                                    </label>
                                    <div class="position-relative">
                                        <input type="password" 
                                               class="form-control form-control-lg" 
                                               id="password" 
                                               name="password" 
                                               placeholder="Enter your password"
                                               required>
                                        <button type="button" 
                                                class="btn btn-link position-absolute top-50 end-0 translate-middle-y pe-3" 
                                                id="togglePassword"
                                                style="z-index: 10;">
                                            <i class="bi bi-eye"></i>
                                        </button>
                                    </div>
                                </div>
                                
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" id="rememberMe">
                                        <label class="form-check-label" for="rememberMe">
                                            Remember me
                                        </label>
                                    </div>
                                    <a href="/forgot-password" class="text-primary text-decoration-none">
                                        Forgot password?
                                    </a>
                                </div>
                                
                                <button type="submit" class="btn btn-primary btn-lg w-100 mb-4">
                                    <span class="btn-text">
                                        <i class="bi bi-box-arrow-in-right me-2"></i>Sign In
                                    </span>
                                    <div class="loading-spinner d-none">
                                        <div class="spinner-border spinner-border-sm me-2" role="status">
                                            <span class="visually-hidden">Loading...</span>
                                        </div>
                                        Signing in...
                                    </div>
                                </button>
                            </form>
                            
                            <!-- Social Login -->
                            <div class="text-center mb-4">
                                <p class="text-muted mb-3">Or continue with</p>
                                <div class="d-flex gap-2 justify-content-center">
                                    <button class="btn btn-outline-secondary flex-fill">
                                        <i class="bi bi-google me-1"></i>Google
                                    </button>
                                    <button class="btn btn-outline-secondary flex-fill">
                                        <i class="bi bi-microsoft me-1"></i>Microsoft
                                    </button>
                                </div>
                            </div>
                            
                            <!-- Register Link -->
                            <div class="text-center">
                                <p class="mb-0">Don't have an account? 
                                    <a href="/register" class="text-primary fw-semibold text-decoration-none">
                                        Create one now
                                    </a>
                                </p>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Additional Info -->
                    <div class="text-center mt-4">
                        <div class="row g-3">
                            <div class="col-4">
                                <div class="d-flex flex-column align-items-center">
                                    <i class="bi bi-shield-check text-primary fs-4 mb-1"></i>
                                    <small class="text-muted">Secure Login</small>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="d-flex flex-column align-items-center">
                                    <i class="bi bi-graph-up-arrow text-primary fs-4 mb-1"></i>
                                    <small class="text-muted">Track Investments</small>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="d-flex flex-column align-items-center">
                                    <i class="bi bi-robot text-primary fs-4 mb-1"></i>
                                    <small class="text-muted">AI Insights</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp"/>
    
    <!-- Scripts -->
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vanilla-tilt@1.8.1/dist/vanilla-tilt.min.js"></script>
    
    <script>
        AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
        if (window.VanillaTilt) { VanillaTilt.init(document.querySelectorAll('[data-tilt]')); }
        (function(){
            const bar = document.getElementById('scrollProgress');
            const update = () => {
                const scrollTop = window.scrollY;
                const docHeight = document.documentElement.scrollHeight - window.innerHeight;
                const width = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0;
                if (bar) bar.style.width = width + '%';
            };
            window.addEventListener('scroll', update);
            window.addEventListener('resize', update);
            update();
        })();
        $(document).ready(function() {
            // Password visibility toggle
            $('#togglePassword').on('click', function() {
                const passwordField = $('#password');
                const toggleIcon = $(this).find('i');
                
                if (passwordField.attr('type') === 'password') {
                    passwordField.attr('type', 'text');
                    toggleIcon.removeClass('bi-eye').addClass('bi-eye-slash');
                } else {
                    passwordField.attr('type', 'password');
                    toggleIcon.removeClass('bi-eye-slash').addClass('bi-eye');
                }
            });
            
            // Form submission with loading state
            $('#loginForm').on('submit', function(e) {
                e.preventDefault();
                
                const submitBtn = $(this).find('button[type="submit"]');
                const btnText = submitBtn.find('.btn-text');
                const loadingSpinner = submitBtn.find('.loading-spinner');
                
                // Show loading state
                btnText.addClass('d-none');
                loadingSpinner.removeClass('d-none');
                submitBtn.prop('disabled', true);
                
                const loginData = {
                    username: $('#username').val().trim(),
                    password: $('#password').val()
                };
                
                // Validate form
                if (!loginData.username || !loginData.password) {
                    showAlert('Please fill in all fields', 'danger');
                    resetButton();
                    return;
                }
                
                $.ajax({
                    url: '/api/auth/login',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(loginData),
                    timeout: 10000,
                    success: function(response) {
                        if (response.success && response.data) {
                            // Store user data
                            localStorage.setItem('token', response.data.token);
                            localStorage.setItem('username', response.data.username);
                            localStorage.setItem('role', response.data.role);
                            
                            showAlert('Login successful! Redirecting to dashboard...', 'success');
                            
                            // Redirect after short delay
                            setTimeout(() => {
                                window.location.href = '/dashboard';
                            }, 1500);
                        } else {
                            showAlert('Login failed. Please check your credentials.', 'danger');
                            resetButton();
                        }
                    },
                    error: function(xhr) {
                        let errorMessage = 'Login failed. Please try again.';
                        
                        if (xhr.status === 401) {
                            errorMessage = 'Invalid username or password.';
                        } else if (xhr.status === 0) {
                            errorMessage = 'Connection error. Please check your internet connection.';
                        } else if (xhr.status >= 500) {
                            errorMessage = 'Server error. Please try again later.';
                        }
                        
                        showAlert(errorMessage, 'danger');
                        resetButton();
                    }
                });
                
                function resetButton() {
                    btnText.removeClass('d-none');
                    loadingSpinner.addClass('d-none');
                    submitBtn.prop('disabled', false);
                }
            });
            
            // Alert helper function
            function showAlert(message, type) {
                const alertElement = $('#alertMessage');
                alertElement.removeClass('d-none alert-success alert-danger alert-warning alert-info')
                           .addClass('alert-' + type)
                           .text(message);
                
                // Auto-hide success messages
                if (type === 'success') {
                    setTimeout(() => {
                        alertElement.addClass('d-none');
                    }, 5000);
                }
            }
            
            // Form validation on input
            $('#username, #password').on('input', function() {
                if ($(this).val().trim()) {
                    $(this).removeClass('is-invalid');
                }
            });
            
            // Clear alert when user starts typing
            $('#username, #password').on('focus', function() {
                $('#alertMessage').addClass('d-none');
            });
            
            // Auto-focus first input
            $('#username').focus();
        });
    </script>
</body>
</html>