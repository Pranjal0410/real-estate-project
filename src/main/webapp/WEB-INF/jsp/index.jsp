<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Smart Real Estate Investment Platform - Discover intelligent investment opportunities with AI-powered analytics and comprehensive market insights">
    <meta name="keywords" content="real estate, investment, property, AI analytics, market insights">
    <meta name="author" content="PropInvest">
    
    <title>PropInvest - Smart Real Estate Investment Platform</title>
    
    <!-- Bootstrap 5 CSS from CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Google Fonts for Shadcn-style typography -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    <link href="/css/components.css" rel="stylesheet">
    <link href="/css/navbar.css" rel="stylesheet">
    <link href="/css/sections.css" rel="stylesheet">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
</head>
<body>
    <!-- Include Magicbricks-Style Navigation -->
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <!-- 🚀 ENHANCED HERO SECTION -->
    <section class="hero">
        <div class="container-center">
            <div class="hero-content">
                <h1 class="hero-title">
                    Find Your Perfect Property
                </h1>
                <p class="hero-subtitle">
                    Discover premium real estate opportunities with AI-powered insights. 
                    Buy, sell, or rent properties with confidence on India's leading platform.
                </p>
                
                <!-- Enhanced Search Bar -->
                <div class="hero-search">
                    <form class="row g-3" action="/properties" method="get" novalidate>
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label for="searchLocation" class="form-label">
                                    <i class="bi bi-geo-alt-fill me-2"></i>Location
                                </label>
                                <input type="text" class="form-input" id="searchLocation" name="location" 
                                       placeholder="Enter city or locality">
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label for="propertyType" class="form-label">
                                    <i class="bi bi-buildings me-2"></i>Property Type
                                </label>
                                <select class="form-input form-select" id="propertyType" name="type">
                                    <option value="">All Types</option>
                                    <option value="APARTMENT">Apartment</option>
                                    <option value="HOUSE">Independent House</option>
                                    <option value="VILLA">Villa</option>
                                    <option value="PLOT">Plot/Land</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label for="budget" class="form-label">
                                    <i class="bi bi-currency-rupee me-2"></i>Budget
                                </label>
                                <select class="form-input form-select" id="budget" name="budget">
                                    <option value="">Any Budget</option>
                                    <option value="0-2500000">Under ₹25 Lakh</option>
                                    <option value="2500000-5000000">₹25 - ₹50 Lakh</option>
                                    <option value="5000000-7500000">₹50 - ₹75 Lakh</option>
                                    <option value="7500000-10000000">₹75 Lakh - ₹1 Cr</option>
                                    <option value="10000000+">Above ₹1 Cr</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label class="form-label">&nbsp;</label>
                                <button type="submit" class="btn btn-primary btn-lg w-100">
                                    <i class="bi bi-search me-2"></i>Search Properties
                                </button>
                            </div>
                        </div>
                    </form>
                    
                    <!-- Quick Action Buttons -->
                    <div class="d-flex justify-content-center gap-3 mt-4 flex-wrap">
                        <a href="/properties?featured=true" class="btn btn-outline btn-sm">
                            <i class="bi bi-star me-2"></i>Featured
                        </a>
                        <a href="/calculator" class="btn btn-outline btn-sm">
                            <i class="bi bi-calculator me-2"></i>Calculator
                        </a>
                        <a href="/chatbot" class="btn btn-outline btn-sm">
                            <i class="bi bi-robot me-2"></i>AI Assistant
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 🎯 IMPRESSIVE STATISTICS SECTION -->
    <section class="stats-section">
        <div class="container">
            <div class="stats-container">
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-buildings"></i>
                    </div>
                    <div class="stat-number" data-count="${propertyCount > 0 ? propertyCount : 500}">0</div>
                    <div class="stat-label">Properties Listed</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-people-fill"></i>
                    </div>
                    <div class="stat-number" data-count="${userCount > 0 ? userCount : 10000}">0</div>
                    <div class="stat-label">Registered Users</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-currency-dollar"></i>
                    </div>
                    <div class="stat-number" data-count="50">0</div>
                    <div class="stat-label">Million+ Invested</div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- 🌟 FEATURES SECTION -->
    <section class="features-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5" >
                    <h2 class="section-title">Why Choose PropInvest?</h2>
                    <p class="section-subtitle">
                        Discover the powerful features that make us the leading platform for smart real estate investments
                    </p>
                </div>
            </div>
            
            <div class="row g-4">
                <!-- Feature 1: Advanced Analytics -->
                <div class="col-lg-4 col-md-6"  >
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-graph-up-arrow"></i>
                        </div>
                        <h4 class="feature-title">Advanced Analytics</h4>
                        <p class="feature-description">
                            Calculate ROI, rental yield, cap rate, and comprehensive investment metrics with our 
                            sophisticated analytical tools and real-time market data.
                        </p>
                    </div>
                </div>
                
                <!-- Feature 2: AI Assistant -->
                <div class="col-lg-4 col-md-6"  >
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-robot"></i>
                        </div>
                        <h4 class="feature-title">AI-Powered Assistant</h4>
                        <p class="feature-description">
                            Get instant property recommendations and personalized investment advice from our 
                            intelligent chatbot powered by advanced machine learning algorithms.
                        </p>
                    </div>
                </div>
                
                <!-- Feature 3: Secure Platform -->
                <div class="col-lg-4 col-md-6"  >
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-shield-check"></i>
                        </div>
                        <h4 class="feature-title">Enterprise Security</h4>
                        <p class="feature-description">
                            Bank-level security with JWT authentication, encrypted data transmission, and 
                            role-based access control to protect your investments and personal information.
                        </p>
                    </div>
                </div>
                
                <!-- Feature 4: Market Insights -->
                <div class="col-lg-4 col-md-6"  >
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-bar-chart-line"></i>
                        </div>
                        <h4 class="feature-title">Market Insights</h4>
                        <p class="feature-description">
                            Access comprehensive market analysis, trend predictions, and neighborhood comparisons 
                            to make informed investment decisions.
                        </p>
                    </div>
                </div>
                
                <!-- Feature 5: Portfolio Management -->
                <div class="col-lg-4 col-md-6"  >
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-pie-chart"></i>
                        </div>
                        <h4 class="feature-title">Portfolio Tracking</h4>
                        <p class="feature-description">
                            Monitor your investment portfolio performance with detailed analytics, 
                            automated reporting, and customizable dashboards.
                        </p>
                    </div>
                </div>
                
                <!-- Feature 6: 24/7 Support -->
                <div class="col-lg-4 col-md-6"  >
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="bi bi-headset"></i>
                        </div>
                        <h4 class="feature-title">24/7 Support</h4>
                        <p class="feature-description">
                            Get round-the-clock support from our experienced team of real estate professionals 
                            and technical experts whenever you need assistance.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- 🏠 FEATURED PROPERTIES SECTION -->
    <section class="properties-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5" >
                    <h2 class="section-title">Featured Properties</h2>
                    <p class="section-subtitle">
                        Discover our handpicked selection of premium investment opportunities with high ROI potential
                    </p>
                </div>
            </div>
            
            <div class="row g-4 mb-5">
                <c:choose>
                    <c:when test="${not empty properties}">
                        <c:forEach items="${properties}" var="property" varStatus="status">
                            <c:if test="${status.index < 6}">
                                <div class="col-xl-4 col-lg-6"  >
                                    <div class="property-card">
                                        <!-- Property Image -->
                                        <div class="property-image">
                                            <c:choose>
                                                <c:when test="${not empty property.imageUrl}">
                                                    <img src="${property.imageUrl}" alt="${property.title}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="https://source.unsplash.com/400x250/?${property.propertyType != null ? property.propertyType.toLowerCase() : 'house'},property" alt="${property.title}">
                                                </c:otherwise>
                                            </c:choose>
                                            <!-- Property Type Badge -->
                                            <div class="property-badge">${property.propertyType}</div>
                                        </div>
                                        
                                        <!-- Property Details -->
                                        <div class="property-content">
                                            <h5 class="property-title">${property.title}</h5>
                                            
                                            <!-- Location -->
                                            <div class="property-location">
                                                <i class="bi bi-geo-alt-fill"></i>
                                                ${property.location}
                                            </div>
                                            
                                            <!-- Price -->
                                            <div class="property-price">
                                                $<fmt:formatNumber value="${property.price}" pattern="#,###"/>
                                            </div>
                                            
                                            <!-- Property Features -->
                                            <div class="property-features">
                                                <c:if test="${property.bedrooms != null && property.bedrooms > 0}">
                                                    <span class="feature-badge">
                                                        <i class="bi bi-door-open me-1"></i>${property.bedrooms} Beds
                                                    </span>
                                                </c:if>
                                                <c:if test="${property.bathrooms != null && property.bathrooms > 0}">
                                                    <span class="feature-badge">
                                                        <i class="bi bi-droplet me-1"></i>${property.bathrooms} Baths
                                                    </span>
                                                </c:if>
                                                <c:if test="${property.area != null && property.area > 0}">
                                                    <span class="feature-badge">
                                                        <i class="bi bi-rulers me-1"></i><fmt:formatNumber value="${property.area}" pattern="#,###"/> sq ft
                                                    </span>
                                                </c:if>
                                            </div>
                                            
                                            <!-- View Details Button -->
                                            <a href="/property/${property.id}" class="btn btn-property">
                                                <i class="bi bi-eye me-2"></i>View Details
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Demo Properties when no data available -->
                        <div class="col-xl-4 col-lg-6" >
                            <div class="property-card">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?luxury,villa" alt="Modern Luxury Villa">
                                    <div class="property-badge">Villa</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Modern Luxury Villa</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Beverly Hills, CA
                                    </div>
                                    <div class="property-price">$2,500,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>5 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>4 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>4,200 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-xl-4 col-lg-6"  >
                            <div class="property-card">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?penthouse,apartment" alt="Downtown Penthouse">
                                    <div class="property-badge">Penthouse</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Downtown Penthouse</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Manhattan, NY
                                    </div>
                                    <div class="property-price">$1,850,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>3 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>2 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>2,100 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-xl-4 col-lg-6"  >
                            <div class="property-card">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?suburban,family,house" alt="Family Suburban Home">
                                    <div class="property-badge">House</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Family Suburban Home</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Austin, TX
                                    </div>
                                    <div class="property-price">$650,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>4 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>3 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>2,800 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- View All Properties Button -->
            <div class="text-center" >
                <a href="/properties" class="btn btn-primary btn-lg">
                    <i class="bi bi-grid me-2"></i>View All Properties
                </a>
            </div>
        </div>
    </section>
    
    <!-- 💬 TESTIMONIALS SECTION -->
    <section class="testimonials-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5" >
                    <h2 class="section-title">What Our Investors Say</h2>
                    <p class="section-subtitle">
                        Hear from successful investors who have transformed their portfolios with PropInvest
                    </p>
                </div>
            </div>
            
            <div class="row g-4">
                <div class="col-lg-4" >
                    <div class="testimonial-card">
                        <div class="testimonial-quote">
                            <i class="bi bi-quote"></i>
                        </div>
                        <div class="testimonial-content">
                            "PropInvest's AI analytics helped me identify undervalued properties that generated 18% ROI in my first year. The platform's insights are invaluable."
                        </div>
                        <div class="testimonial-author">Sarah Johnson</div>
                        <div class="testimonial-role">Real Estate Investor</div>
                    </div>
                </div>
                
                <div class="col-lg-4"  >
                    <div class="testimonial-card">
                        <div class="testimonial-quote">
                            <i class="bi bi-quote"></i>
                        </div>
                        <div class="testimonial-content">
                            "The portfolio tracking feature is exceptional. I can monitor all my investments in one place and make data-driven decisions with confidence."
                        </div>
                        <div class="testimonial-author">Michael Chen</div>
                        <div class="testimonial-role">Portfolio Manager</div>
                    </div>
                </div>
                
                <div class="col-lg-4"  >
                    <div class="testimonial-card">
                        <div class="testimonial-quote">
                            <i class="bi bi-quote"></i>
                        </div>
                        <div class="testimonial-content">
                            "As a first-time investor, the AI assistant guided me through every step. I've already purchased two profitable properties through the platform."
                        </div>
                        <div class="testimonial-author">Emily Rodriguez</div>
                        <div class="testimonial-role">New Investor</div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- 🚀 CTA SECTION -->
    <section class="cta-section">
        <div class="container">
            <div class="cta-content" >
                <h2 class="cta-title">Ready to Start Your Investment Journey?</h2>
                <p class="cta-subtitle">
                    Join thousands of successful investors who trust PropInvest for their real estate investment needs. 
                    Start building your wealth today with our AI-powered platform.
                </p>
                
                <div class="hero-buttons mb-5">
                    <a href="/register" class="btn btn-hero-primary">
                        <i class="bi bi-person-plus me-2"></i>Get Started Free
                    </a>
                    <a href="/calculator" class="btn btn-hero-secondary">
                        <i class="bi bi-calculator me-2"></i>Try Calculator
                    </a>
                </div>
                
                <!-- Trust Indicators -->
                <div class="row text-center"  >
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-shield-check fs-2 mb-3 opacity-75"></i>
                            <small class="opacity-90">Bank-Level Security</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-clock fs-2 mb-3 opacity-75"></i>
                            <small class="opacity-90">24/7 Support</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-graph-up fs-2 mb-3 opacity-75"></i>
                            <small class="opacity-90">Proven Results</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-people fs-2 mb-3 opacity-75"></i>
                            <small class="opacity-90">Expert Team</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp"/>
    
    <!-- Scripts from CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    
    <!-- Custom JavaScript -->
    <script>
        // Animated Counter Function
        function animateCounter(element, start, end, duration) {
            let current = start;
            const range = end - start;
            const increment = end > start ? 1 : -1;
            const stepTime = Math.abs(Math.floor(duration / range));
            
            const timer = setInterval(() => {
                current += increment;
                element.textContent = current.toLocaleString();
                if (current === end) {
                    clearInterval(timer);
                }
            }, stepTime);
        }
        
        // Initialize counters when they come into view
        function initCounters() {
            const counters = document.querySelectorAll('.stat-number');
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting && !entry.target.classList.contains('animated')) {
                        const target = parseInt(entry.target.getAttribute('data-count'));
                        entry.target.classList.add('animated');
                        animateCounter(entry.target, 0, target, 2000);
                    }
                });
            });
            
            counters.forEach(counter => observer.observe(counter));
        }
        
        // Navbar scroll effect
        function initNavbarScroll() {
            const navbar = document.getElementById('mainNavbar');
            if (navbar) {
                window.addEventListener('scroll', () => {
                    if (window.scrollY > 100) {
                        navbar.classList.add('scrolled');
                    } else {
                        navbar.classList.remove('scrolled');
                    }
                });
            }
        }
        
        // Smooth scroll for anchor links
        function initSmoothScroll() {
            document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                anchor.addEventListener('click', function (e) {
                    e.preventDefault();
                    const target = document.querySelector(this.getAttribute('href'));
                    if (target) {
                        target.scrollIntoView({
                            behavior: 'smooth',
                            block: 'start'
                        });
                    }
                });
            });
        }
        
        
        // Initialize all functions when DOM is ready
        document.addEventListener('DOMContentLoaded', function() {
            initCounters();
            initNavbarScroll();
            initSmoothScroll();
            
            // Add loading complete class for final animations
            setTimeout(() => {
                document.body.classList.add('loaded');
            }, 100);
        });
        
        // Preload hero background image
        const heroImg = new Image();
        heroImg.src = 'https://source.unsplash.com/1600x900/?real-estate,city';
        
        // Form validation helpers
        function validateEmail(email) {
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(email);
        }
        
        // Add some utility functions for future use
        window.PropInvest = {
            utils: {
                formatCurrency: function(amount) {
                    return new Intl.NumberFormat('en-US', {
                        style: 'currency',
                        currency: 'USD'
                    }).format(amount);
                },
                
                formatNumber: function(number) {
                    return new Intl.NumberFormat('en-US').format(number);
                },
                
                debounce: function(func, wait) {
                    let timeout;
                    return function executedFunction(...args) {
                        const later = () => {
                            clearTimeout(timeout);
                            func(...args);
                        };
                        clearTimeout(timeout);
                        timeout = setTimeout(later, wait);
                    };
                }
            }
        };
    </script>
    
    <!-- Include main.js if it exists -->
    <script src="/js/main.js" onerror="console.log('main.js not found, using inline scripts only')"></script>
</body>
</html>