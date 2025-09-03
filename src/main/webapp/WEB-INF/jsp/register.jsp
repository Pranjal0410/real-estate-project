<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Real Estate Platform</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">
    <link href="https://unpkg.com/aos@2.3.4/dist/aos.css" rel="stylesheet">
</head>
<body>
    <div id="scrollProgress"></div>
    <jsp:include page="includes/navbar.jsp"/>
    
    <div class="container mt-5" data-aos="fade-up">
        <div class="row justify-content-center">
            <div class="col-md-6" data-aos="zoom-in" data-aos-delay="100">
                <div class="card shadow" data-tilt data-tilt-max="6" data-tilt-speed="400">
                    <div class="card-body">
                        <h3 class="card-title text-center mb-4">Create Account</h3>
                        
                        <div id="alertMessage" class="alert d-none"></div>
                        
                        <form id="registerForm">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="firstName" class="form-label">First Name</label>
                                    <input type="text" class="form-control" id="firstName" name="firstName" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="lastName" class="form-label">Last Name</label>
                                    <input type="text" class="form-control" id="lastName" name="lastName" required>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" required minlength="3">
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="phone" class="form-label">Phone</label>
                                <input type="tel" class="form-control" id="phone" name="phone">
                            </div>
                            
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" required minlength="6">
                                <small class="text-muted">Minimum 6 characters</small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirm Password</label>
                                <input type="password" class="form-control" id="confirmPassword" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="role" class="form-label">Account Type</label>
                                <select class="form-control" id="role" name="role">
                                    <option value="USER">Investor</option>
                                    <option value="PROPERTY_OWNER">Property Owner</option>
                                </select>
                            </div>
                            
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="terms" required>
                                <label class="form-check-label" for="terms">
                                    I agree to the <a href="#">Terms and Conditions</a>
                                </label>
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100">Register</button>
                        </form>
                        
                        <div class="mt-3 text-center">
                            <p>Already have an account? <a href="/login">Login here</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="includes/footer.jsp"/>
    <button id="backToTop" aria-label="Back to top" title="Back to top"><i class="bi bi-arrow-up"></i></button>
    
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vanilla-tilt@1.8.1/dist/vanilla-tilt.min.js"></script>
    <script>
    AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
    if (window.VanillaTilt) { VanillaTilt.init(document.querySelectorAll('[data-tilt]')); }
    (function(){
        const bar = document.getElementById('scrollProgress');
        const btn = document.getElementById('backToTop');
        const update = () => {
            const scrollTop = window.scrollY;
            const docHeight = document.documentElement.scrollHeight - window.innerHeight;
            const width = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0;
            if (bar) bar.style.width = width + '%';
            if (btn) btn.classList.toggle('show', scrollTop > 400);
        };
        window.addEventListener('scroll', update);
        window.addEventListener('resize', update);
        if (btn) btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
        update();
    })();
    $(document).ready(function() {
        $('#registerForm').on('submit', function(e) {
            e.preventDefault();
            
            if ($('#password').val() !== $('#confirmPassword').val()) {
                $('#alertMessage').removeClass('d-none alert-success').addClass('alert-danger');
                $('#alertMessage').text('Passwords do not match');
                return;
            }
            
            const userData = {
                firstName: $('#firstName').val(),
                lastName: $('#lastName').val(),
                username: $('#username').val(),
                email: $('#email').val(),
                phone: $('#phone').val(),
                password: $('#password').val(),
                role: 'ROLE_' + $('#role').val(),
                enabled: true
            };
            
            $.ajax({
                url: '/api/auth/register',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(userData),
                success: function(response) {
                    if (response.success) {
                        $('#alertMessage').removeClass('d-none alert-danger').addClass('alert-success');
                        $('#alertMessage').text('Registration successful! Redirecting to login...');
                        
                        setTimeout(function() {
                            window.location.href = '/login';
                        }, 2000);
                    }
                },
                error: function(xhr) {
                    const errorMsg = xhr.responseJSON?.message || 'Registration failed. Please try again.';
                    $('#alertMessage').removeClass('d-none alert-success').addClass('alert-danger');
                    $('#alertMessage').text(errorMsg);
                }
            });
        });
    });
    </script>
</body>
</html>