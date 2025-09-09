<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="About PropInvest - Your trusted real estate investment platform">
    <title>About Us - PropInvest</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Custom CSS -->
    <link href="/css/style.css" rel="stylesheet">
    <link href="/css/global.css" rel="stylesheet">
    <link href="/css/magicbricks-exact.css" rel="stylesheet">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="bg-dark-theme">
    <div id="scrollProgress"></div>
    
    <!-- Include Navigation -->
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <!-- Hero Section -->
    <section class="hero-section" style="min-height: 60vh;">
        <div class="hero-content">
            <h1 class="hero-title">About PropInvest</h1>
            <p class="hero-subtitle">
                Your trusted partner in smart real estate investments with AI-powered insights and comprehensive market analysis.
            </p>
        </div>
    </section>
    
    <!-- About Content -->
    <section class="section-padding">
        <div class="container">
            <!-- Mission Section -->
            <div class="row mb-5" data-aos="fade-up">
                <div class="col-lg-8 mx-auto text-center">
                    <h2 class="section-title">Our Mission</h2>
                    <p class="section-subtitle">
                        To democratize real estate investment by providing intelligent tools, comprehensive analytics, 
                        and expert guidance that empower investors to make informed decisions and build lasting wealth.
                    </p>
                </div>
            </div>
            
            <!-- Features Grid -->
            <div class="row g-4 mb-5">
                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="100">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-graph-up-arrow"></i>
                        </div>
                        <h4 class="feature-title">Advanced Analytics</h4>
                        <p class="feature-description">
                            Our platform provides sophisticated analytical tools including ROI calculators, 
                            rental yield analysis, cap rate computation, and comprehensive market insights.
                        </p>
                    </div>
                </div>
                
                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="200">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-robot"></i>
                        </div>
                        <h4 class="feature-title">AI-Powered Assistant</h4>
                        <p class="feature-description">
                            Get personalized investment recommendations and expert advice from our 
                            intelligent chatbot powered by advanced machine learning algorithms.
                        </p>
                    </div>
                </div>
                
                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="300">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-shield-check"></i>
                        </div>
                        <h4 class="feature-title">Enterprise Security</h4>
                        <p class="feature-description">
                            Bank-level security with JWT authentication, encrypted data transmission, 
                            and role-based access control to protect your investments.
                        </p>
                    </div>
                </div>
            </div>
            
            <!-- Technology Stack -->
            <div class="row" data-aos="fade-up">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <h3 class="card-title text-center mb-4">Built with Modern Technology</h3>
                            <div class="row g-4 text-center">
                                <div class="col-md-3">
                                    <div class="tech-item">
                                        <i class="bi bi-code-square fs-1 text-primary mb-3"></i>
                                        <h5>Java 17+</h5>
                                        <p class="text-muted">Modern Java with latest features</p>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="tech-item">
                                        <i class="bi bi-gear fs-1 text-primary mb-3"></i>
                                        <h5>Spring Boot</h5>
                                        <p class="text-muted">Robust enterprise framework</p>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="tech-item">
                                        <i class="bi bi-database fs-1 text-primary mb-3"></i>
                                        <h5>MySQL</h5>
                                        <p class="text-muted">Reliable database management</p>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="tech-item">
                                        <i class="bi bi-bootstrap fs-1 text-primary mb-3"></i>
                                        <h5>Bootstrap</h5>
                                        <p class="text-muted">Modern responsive design</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Call to Action -->
    <section class="cta-section" data-aos="fade-up">
        <div class="container">
            <div class="cta-content">
                <h2 class="cta-title">Ready to Start Investing?</h2>
                <p class="cta-subtitle">
                    Join thousands of successful investors who trust PropInvest for their real estate investment needs.
                </p>
                
                <div class="hero-buttons">
                    <a href="/register" class="btn btn-hero-primary">
                        <i class="bi bi-person-plus me-2"></i>Get Started Free
                    </a>
                    <a href="/properties" class="btn btn-hero-secondary">
                        <i class="bi bi-buildings me-2"></i>Browse Properties
                    </a>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp"/>
    
    <!-- Back to Top Button -->
    <button id="backToTop" aria-label="Back to top" title="Back to top">
        <i class="bi bi-arrow-up"></i>
    </button>
    
    <!-- Scripts -->
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
    
    <script>
        AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
        
        // Back to top functionality
        (function(){
            const btn = document.getElementById('backToTop');
            const bar = document.getElementById('scrollProgress');
            const toggleBtn = () => {
                const scrollY = window.scrollY;
                btn.classList.toggle('show', scrollY > 400);
                
                // Update progress bar
                const docHeight = document.documentElement.scrollHeight - window.innerHeight;
                const width = docHeight > 0 ? (scrollY / docHeight) * 100 : 0;
                if (bar) bar.style.width = width + '%';
            };
            
            window.addEventListener('scroll', toggleBtn);
            btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
            toggleBtn();
        })();
    </script>
</body>
</html>