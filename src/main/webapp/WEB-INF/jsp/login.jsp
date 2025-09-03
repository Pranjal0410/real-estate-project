<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Login to PropInvest - Access your real estate investment dashboard and portfolio">
    <title>Login - PropInvest</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Custom CSS -->
    <link href="/css/style.css" rel="stylesheet">
    <link href="https://unpkg.com/aos@2.3.4/dist/aos.css" rel="stylesheet">
</head>
<body class="bg-dark-theme">
    <div id="scrollProgress"></div>
    <!-- Include Navigation -->
    <jsp:include page="includes/navbar.jsp"/>
    
    <!-- Login Section -->
    <section class="section-padding" data-aos="fade-up">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-5 col-md-7" data-aos="zoom-in" data-aos-delay="100">
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