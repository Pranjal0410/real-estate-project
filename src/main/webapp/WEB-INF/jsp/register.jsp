<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - PropInvest</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 20px;
        }

        .register-container {
            width: 100%;
            max-width: 500px;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            animation: slideUp 0.5s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .register-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }

        .register-header h2 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
            margin-bottom: 10px;
        }

        .register-header p {
            margin: 0;
            opacity: 0.9;
            font-size: 14px;
        }

        .logo {
            font-size: 40px;
            margin-bottom: 15px;
        }

        .register-body {
            padding: 40px 35px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            font-weight: 500;
            color: #333;
            margin-bottom: 8px;
            font-size: 14px;
        }

        .form-control {
            border: 2px solid #e1e8ed;
            border-radius: 10px;
            padding: 12px 15px;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.1);
            outline: none;
        }

        .form-select {
            border: 2px solid #e1e8ed;
            border-radius: 10px;
            padding: 12px 15px;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.1);
        }

        .password-wrapper {
            position: relative;
        }

        .password-toggle {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            color: #999;
        }

        .btn-register {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            margin-top: 10px;
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-register:active {
            transform: translateY(0);
        }

        .form-check {
            margin-bottom: 20px;
        }

        .form-check-input {
            width: 18px;
            height: 18px;
            margin-top: 3px;
        }

        .form-check-label {
            margin-left: 8px;
            font-size: 14px;
            color: #666;
        }

        .form-check-label a {
            color: #667eea;
            text-decoration: none;
        }

        .form-check-label a:hover {
            text-decoration: underline;
        }

        .login-link {
            text-align: center;
            margin-top: 25px;
            padding-top: 25px;
            border-top: 1px solid #e1e8ed;
            font-size: 14px;
            color: #666;
        }

        .login-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
        }

        .login-link a:hover {
            text-decoration: underline;
        }

        .alert {
            border-radius: 10px;
            padding: 12px 15px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-danger {
            background-color: #fee;
            border: 1px solid #fcc;
            color: #c33;
        }

        .alert-success {
            background-color: #efe;
            border: 1px solid #cfc;
            color: #363;
        }

        .divider {
            text-align: center;
            margin: 25px 0;
            position: relative;
        }

        .divider::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            width: 100%;
            height: 1px;
            background: #e1e8ed;
        }

        .divider span {
            background: white;
            padding: 0 15px;
            position: relative;
            color: #999;
            font-size: 13px;
        }

        .social-login {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .social-btn {
            flex: 1;
            padding: 10px;
            border: 2px solid #e1e8ed;
            background: white;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 14px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .social-btn:hover {
            background: #f8f9fa;
            border-color: #667eea;
        }

        @media (max-width: 480px) {
            .register-body {
                padding: 30px 20px;
            }

            .register-header h2 {
                font-size: 24px;
            }
        }
    </style>
</head>
<body>
    <div class="register-container">
        <div class="register-header">
            <div class="logo">
                <i class="bi bi-house-door-fill"></i>
            </div>
            <h2>Create Your Account</h2>
            <p>Join PropInvest and start your real estate journey</p>
        </div>

        <div class="register-body">
            <div id="alertMessage" class="alert d-none"></div>

            <form id="registerForm">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="firstName" class="form-label">First Name</label>
                            <input type="text" class="form-control" id="firstName" name="firstName" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="lastName" class="form-label">Last Name</label>
                            <input type="text" class="form-control" id="lastName" name="lastName" required>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required minlength="3">
                </div>

                <div class="form-group">
                    <label for="email" class="form-label">Email Address</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="phone" class="form-label">Phone Number (Optional)</label>
                    <input type="tel" class="form-control" id="phone" name="phone" placeholder="+1234567890">
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Password</label>
                    <div class="password-wrapper">
                        <input type="password" class="form-control" id="password" name="password" required minlength="6">
                        <i class="bi bi-eye password-toggle" id="togglePassword"></i>
                    </div>
                    <small class="text-muted">Minimum 6 characters</small>
                </div>

                <div class="form-group">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <div class="password-wrapper">
                        <input type="password" class="form-control" id="confirmPassword" required>
                        <i class="bi bi-eye password-toggle" id="toggleConfirmPassword"></i>
                    </div>
                </div>

                <div class="form-group">
                    <label for="role" class="form-label">I want to</label>
                    <select class="form-select" id="role" name="role">
                        <option value="USER">Invest in Properties</option>
                        <option value="PROPERTY_OWNER">List My Properties</option>
                    </select>
                </div>

                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="terms" required>
                    <label class="form-check-label" for="terms">
                        I agree to the <a href="#">Terms and Conditions</a> and <a href="#">Privacy Policy</a>
                    </label>
                </div>

                <button type="submit" class="btn btn-register">
                    Create Account
                </button>
            </form>

            <div class="divider">
                <span>OR</span>
            </div>

            <div class="social-login">
                <button class="social-btn" type="button">
                    <i class="bi bi-google"></i>
                    Google
                </button>
                <button class="social-btn" type="button">
                    <i class="bi bi-facebook"></i>
                    Facebook
                </button>
            </div>

            <div class="login-link">
                Already have an account? <a href="/login">Sign In</a>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        $(document).ready(function() {
            // Password visibility toggle
            $('#togglePassword').click(function() {
                const passwordField = $('#password');
                const type = passwordField.attr('type') === 'password' ? 'text' : 'password';
                passwordField.attr('type', type);
                $(this).toggleClass('bi-eye bi-eye-slash');
            });

            $('#toggleConfirmPassword').click(function() {
                const passwordField = $('#confirmPassword');
                const type = passwordField.attr('type') === 'password' ? 'text' : 'password';
                passwordField.attr('type', type);
                $(this).toggleClass('bi-eye bi-eye-slash');
            });

            // Form submission
            $('#registerForm').on('submit', function(e) {
                e.preventDefault();

                // Clear previous messages
                $('#alertMessage').addClass('d-none').removeClass('alert-success alert-danger');

                // Validate passwords match
                if ($('#password').val() !== $('#confirmPassword').val()) {
                    $('#alertMessage').removeClass('d-none').addClass('alert-danger');
                    $('#alertMessage').text('Passwords do not match');
                    return;
                }

                // Prepare user data
                const phoneValue = $('#phone').val().trim();
                const userData = {
                    firstName: $('#firstName').val(),
                    lastName: $('#lastName').val(),
                    username: $('#username').val(),
                    email: $('#email').val(),
                    phoneNumber: phoneValue === '' ? null : phoneValue,
                    password: $('#password').val(),
                    role: $('#role').val() === 'USER' ? 'INVESTOR' : $('#role').val(),
                    isEnabled: true
                };

                // Submit registration
                $.ajax({
                    url: '/api/auth/register',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(userData),
                    success: function(response) {
                        if (response.success) {
                            $('#alertMessage').removeClass('d-none alert-danger').addClass('alert-success');
                            $('#alertMessage').html('<i class="bi bi-check-circle"></i> Registration successful! Redirecting to login...');

                            // Clear form
                            $('#registerForm')[0].reset();

                            // Redirect after 2 seconds
                            setTimeout(function() {
                                window.location.href = '/login';
                            }, 2000);
                        }
                    },
                    error: function(xhr) {
                        const errorMsg = xhr.responseJSON?.message || 'Registration failed. Please try again.';
                        $('#alertMessage').removeClass('d-none alert-success').addClass('alert-danger');
                        $('#alertMessage').html('<i class="bi bi-exclamation-triangle"></i> ' + errorMsg);
                    }
                });
            });
        });
    </script>
</body>
</html>