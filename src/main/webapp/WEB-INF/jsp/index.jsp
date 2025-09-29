<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Smart Real Estate Investment Platform - Discover intelligent investment opportunities with AI-powered analytics and comprehensive market insights">
    <meta name="keywords" content="real estate, investment, property, AI analytics, market insights, Razorpay, secure payments">
    <meta name="author" content="PropInvest">

    <title>PropInvest - Smart Real Estate Investment Platform</title>

    <!-- Bootstrap 5 CSS from CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&family=Poppins:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <!-- Razorpay Script -->
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>

    <!-- Custom CSS -->
    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    <link href="/css/components.css" rel="stylesheet">
    <link href="/css/navbar.css" rel="stylesheet">

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="/favicon.ico">

    <style>
        /* Enhanced Dark Purple Theme for Homepage */
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
            --success: #10b981;
            --gradient-primary: linear-gradient(135deg, #7c3aed 0%, #6d28d9 100%);
            --gradient-hero: linear-gradient(135deg, #0f0f1e 0%, #1a1a2e 50%, #2d1b69 100%);
        }

        body {
            font-family: 'Inter', sans-serif;
            background: var(--background);
            color: var(--text);
            overflow-x: hidden;
        }

        /* Enhanced Hero Section */
        .hero {
            min-height: 100vh;
            background: var(--gradient-hero);
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background:
                radial-gradient(circle at 20% 80%, rgba(124, 58, 237, 0.3) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(16, 185, 129, 0.2) 0%, transparent 50%),
                radial-gradient(circle at 40% 40%, rgba(167, 139, 250, 0.1) 0%, transparent 50%);
            animation: backgroundShift 20s ease-in-out infinite;
            z-index: 1;
        }

        @keyframes backgroundShift {
            0%, 100% { transform: scale(1) rotate(0deg); }
            50% { transform: scale(1.1) rotate(5deg); }
        }

        .hero-content {
            position: relative;
            z-index: 2;
            text-align: center;
            max-width: 1000px;
            padding: 0 2rem;
        }

        .hero-title {
            font-size: clamp(2.5rem, 6vw, 4.5rem);
            font-weight: 900;
            margin-bottom: 1.5rem;
            background: linear-gradient(135deg, #ffffff 0%, #a78bfa 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            line-height: 1.1;
        }

        .hero-subtitle {
            font-size: 1.25rem;
            color: rgba(255, 255, 255, 0.9);
            margin-bottom: 3rem;
            line-height: 1.6;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        .hero-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            flex-wrap: wrap;
            margin-bottom: 4rem;
        }

        .btn-hero-primary {
            background: var(--gradient-primary);
            border: none;
            color: white;
            padding: 1rem 2rem;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1.1rem;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 20px rgba(124, 58, 237, 0.3);
        }

        .btn-hero-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 30px rgba(124, 58, 237, 0.4);
            color: white;
        }

        .btn-hero-secondary {
            background: rgba(255, 255, 255, 0.1);
            border: 2px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 1rem 2rem;
            border-radius: 12px;
            font-weight: 600;
            font-size: 1.1rem;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            transition: all 0.3s ease;
            backdrop-filter: blur(10px);
        }

        .btn-hero-secondary:hover {
            background: rgba(255, 255, 255, 0.2);
            border-color: var(--primary-light);
            color: white;
            transform: translateY(-2px);
        }

        /* Investment CTA Section */
        .investment-cta {
            background: rgba(26, 26, 46, 0.8);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border);
            border-radius: 24px;
            padding: 3rem;
            margin: 4rem auto;
            max-width: 800px;
            text-align: center;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
        }

        .investment-title {
            font-size: 2.5rem;
            font-weight: 800;
            margin-bottom: 1rem;
            color: var(--primary-light);
        }

        .investment-subtitle {
            font-size: 1.2rem;
            color: var(--text-muted);
            margin-bottom: 2rem;
        }

        .investment-features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            margin: 2rem 0;
        }

        .investment-feature {
            background: rgba(124, 58, 237, 0.1);
            border: 1px solid rgba(124, 58, 237, 0.3);
            border-radius: 12px;
            padding: 1.5rem;
            text-align: center;
        }

        .investment-feature i {
            font-size: 2rem;
            color: var(--primary);
            margin-bottom: 1rem;
        }

        .btn-invest {
            background: var(--gradient-primary);
            border: none;
            color: white;
            padding: 1.25rem 3rem;
            border-radius: 12px;
            font-weight: 700;
            font-size: 1.2rem;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.75rem;
            transition: all 0.3s ease;
            box-shadow: 0 6px 25px rgba(124, 58, 237, 0.4);
            margin-top: 2rem;
        }

        .btn-invest:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 35px rgba(124, 58, 237, 0.5);
            color: white;
        }

        /* Section Styling */
        .section {
            padding: 5rem 0;
            background: var(--background);
        }

        .section-title {
            font-size: 2.5rem;
            font-weight: 800;
            text-align: center;
            margin-bottom: 1rem;
            color: var(--text);
        }

        .section-subtitle {
            font-size: 1.2rem;
            color: var(--text-muted);
            text-align: center;
            margin-bottom: 3rem;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        /* Enhanced Search Form */
        .hero-search {
            background: rgba(26, 26, 46, 0.9);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 2.5rem;
            margin-top: 3rem;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
        }

        .search-form-label {
            color: rgba(255, 255, 255, 0.9);
            font-weight: 600;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .search-form-input {
            background: rgba(255, 255, 255, 0.95);
            border: 2px solid rgba(167, 139, 250, 0.3);
            border-radius: 10px;
            color: #1a1a2e;
            padding: 0.875rem 1rem;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .search-form-input:focus {
            background: white;
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.25);
            color: #1a1a2e;
        }

        .search-form-input::placeholder {
            color: #6b7280;
        }

        .search-btn {
            background: var(--gradient-primary);
            border: none;
            color: white;
            padding: 0.875rem 2rem;
            border-radius: 10px;
            font-weight: 600;
            width: 100%;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        .search-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(124, 58, 237, 0.4);
        }

        /* Quick Action Buttons */
        .quick-actions {
            display: flex;
            justify-content: center;
            gap: 1rem;
            margin-top: 2rem;
            flex-wrap: wrap;
        }

        .btn-quick {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 0.75rem 1.5rem;
            border-radius: 25px;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 500;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .btn-quick:hover {
            background: rgba(124, 58, 237, 0.3);
            border-color: var(--primary);
            color: white;
            transform: translateY(-1px);
        }

        /* Other section styles will continue... */
        /* Responsive Design */
        @media (max-width: 768px) {
            .hero-title {
                font-size: 2.5rem;
            }

            .hero-buttons {
                flex-direction: column;
                align-items: center;
            }

            .btn-hero-primary,
            .btn-hero-secondary {
                width: 100%;
                max-width: 300px;
            }

            .hero-search {
                padding: 1.5rem;
                margin: 2rem 1rem 0;
            }

            .investment-cta {
                margin: 2rem 1rem;
                padding: 2rem;
            }

            .investment-title {
                font-size: 2rem;
            }

            .section-title {
                font-size: 2rem;
            }
        }

        /* Animations */
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .animate-fade-in-up {
            animation: fadeInUp 0.6s ease-out;
        }

        /* Staggered animation delays */
        .animate-delay-1 { animation-delay: 0.1s; }
        .animate-delay-2 { animation-delay: 0.2s; }
        .animate-delay-3 { animation-delay: 0.3s; }
        .animate-delay-4 { animation-delay: 0.4s; }
    </style>
</head>
<body>
    <!-- Include Magicbricks-Style Navigation -->
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <!-- 🚀 ENHANCED HERO SECTION -->
    <section class="hero">
        <div class="container-center">
            <div class="hero-content animate-fade-in-up">
                <h1 class="hero-title">
                    Invest Smart. Build Wealth. Own Tomorrow.
                </h1>
                <p class="hero-subtitle">
                    Join India's most trusted real estate investment platform. Discover premium properties,
                    AI-powered insights, and secure payments with Razorpay. Start building your property portfolio today.
                </p>

                <!-- Call-to-Action Buttons -->
                <div class="hero-buttons animate-fade-in-up animate-delay-1">
                    <sec:authorize access="!isAuthenticated()">
                        <a href="/register" class="btn-hero-primary">
                            <i class="bi bi-person-plus"></i>Start Investing Free
                        </a>
                        <a href="/login" class="btn-hero-secondary">
                            <i class="bi bi-box-arrow-in-right"></i>Sign In
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <a href="https://rzp.io/rzp/H82EaBe" class="btn-hero-primary">
                            <i class="bi bi-currency-rupee"></i>Invest Now
                        </a>
                        <a href="/properties" class="btn-hero-secondary">
                            <i class="bi bi-search"></i>Browse Properties
                        </a>
                    </sec:authorize>
                    <a href="/emi-calculator" class="btn-hero-secondary">
                        <i class="bi bi-calculator"></i>EMI Calculator
                    </a>
                </div>

                <!-- Investment CTA Section -->
                <div class="investment-cta animate-fade-in-up animate-delay-2">
                    <h2 class="investment-title">
                        <i class="bi bi-shield-check me-3"></i>Secure Investments
                    </h2>
                    <p class="investment-subtitle">
                        Start with as low as ₹10,000. Powered by Razorpay for secure, instant transactions.
                    </p>

                    <div class="investment-features">
                        <div class="investment-feature">
                            <i class="bi bi-shield-lock"></i>
                            <h4>Bank-Level Security</h4>
                            <p>Your investments are protected with enterprise-grade security</p>
                        </div>
                        <div class="investment-feature">
                            <i class="bi bi-graph-up-arrow"></i>
                            <h4>High Returns</h4>
                            <p>Average 15-25% annual returns on verified properties</p>
                        </div>
                        <div class="investment-feature">
                            <i class="bi bi-clock"></i>
                            <h4>Instant Processing</h4>
                            <p>Quick approvals and immediate investment confirmations</p>
                        </div>
                    </div>

                    <sec:authorize access="isAuthenticated()">
                        <a href="https://rzp.io/rzp/H82EaBe" class="btn-invest">
                            <i class="bi bi-credit-card"></i>Invest with Razorpay
                        </a>
                    </sec:authorize>
                    <sec:authorize access="!isAuthenticated()">
                        <a href="/register" class="btn-invest">
                            <i class="bi bi-person-plus"></i>Sign Up to Invest
                        </a>
                    </sec:authorize>
                </div>

                <!-- Enhanced Search Bar -->
                <div class="hero-search animate-fade-in-up animate-delay-3">
                    <h3 style="color: white; margin-bottom: 1.5rem; text-align: center;">
                        <i class="bi bi-search me-2"></i>Find Your Perfect Property
                    </h3>
                    <form class="row g-3" action="/properties" method="get" novalidate id="propertySearchForm">
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label for="searchLocation" class="search-form-label">
                                    <i class="bi bi-geo-alt-fill"></i>Location
                                </label>
                                <input type="text" class="search-form-input" id="searchLocation" name="location"
                                       placeholder="Enter city or locality" required>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label for="propertyType" class="search-form-label">
                                    <i class="bi bi-buildings"></i>Property Type
                                </label>
                                <select class="search-form-input" id="propertyType" name="type">
                                    <option value="">All Types</option>
                                    <option value="APARTMENT">Apartment</option>
                                    <option value="RESIDENTIAL">Independent House</option>
                                    <option value="VILLA">Villa</option>
                                    <option value="COMMERCIAL">Commercial</option>
                                    <option value="OFFICE">Office</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="form-group">
                                <label for="budget" class="search-form-label">
                                    <i class="bi bi-currency-rupee"></i>Budget
                                </label>
                                <select class="search-form-input" id="budget" name="budget">
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
                                <label class="search-form-label">&nbsp;</label>
                                <button type="submit" class="search-btn">
                                    <i class="bi bi-search"></i>Search Properties
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Quick Action Buttons -->
                    <div class="quick-actions">
                        <a href="/properties" class="btn-quick">
                            <i class="bi bi-house"></i>Browse All
                        </a>
                        <a href="/properties?location=mumbai" class="btn-quick">
                            <i class="bi bi-star"></i>Mumbai Premium
                        </a>
                        <a href="/emi-calculator" class="btn-quick">
                            <i class="bi bi-calculator"></i>EMI Calculator
                        </a>
                        <a href="/chatbot" class="btn-quick">
                            <i class="bi bi-robot"></i>Expert Advice
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 🏠 FEATURED PROPERTIES SECTION -->
    <section class="section">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5">
                    <h2 class="section-title">Premium Investment Properties</h2>
                    <p class="section-subtitle">
                        Explore verified properties with high ROI potential in India's top investment destinations.
                        All properties are pre-verified and ready for investment.
                    </p>
                    <div class="d-flex justify-content-center gap-3 mb-4 flex-wrap">
                        <a href="/properties?location=pune" class="btn-quick">
                            <i class="bi bi-geo-alt"></i>Pune
                        </a>
                        <a href="/properties?location=mumbai" class="btn-quick">
                            <i class="bi bi-geo-alt"></i>Mumbai
                        </a>
                        <a href="/properties?location=delhi" class="btn-quick">
                            <i class="bi bi-geo-alt"></i>Delhi
                        </a>
                        <a href="/properties?location=bangalore" class="btn-quick">
                            <i class="bi bi-geo-alt"></i>Bangalore
                        </a>
                    </div>
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
                                            <img src="https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=250&q=80" alt="${property.title}">
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
                                                ₹<fmt:formatNumber value="${property.price}" pattern="#,###"/>
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
                                        <i class="bi bi-geo-alt-fill"></i>Bandra West, Mumbai
                                    </div>
                                    <div class="property-price">₹2,50,00,000</div>
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
                                        <i class="bi bi-geo-alt-fill"></i>Connaught Place, Delhi
                                    </div>
                                    <div class="property-price">₹1,85,00,000</div>
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
                                        <i class="bi bi-geo-alt-fill"></i>Whitefield, Bangalore
                                    </div>
                                    <div class="property-price">₹65,00,000</div>
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
                <a href="/properties" class="btn btn-primary btn-lg px-5 py-3">
                    <i class="bi bi-grid me-2"></i>Browse All 24 Properties
                </a>
                <p class="mt-3 text-muted">
                    <small>✓ Verified listings ✓ No hidden fees ✓ Direct owner contact</small>
                </p>
            </div>
        </div>
    </section>

    <!-- 🔄 HOW IT WORKS SECTION -->
    <section class="how-it-works">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5">
                    <h2 class="section-title">How PropInvest Works</h2>
                    <p class="section-subtitle">
                        Start your real estate investment journey in just 4 simple steps.
                        Our platform makes property investment accessible to everyone.
                    </p>
                </div>
            </div>

            <div class="steps-grid">
                <div class="step-card animate-fade-in-up animate-delay-1">
                    <div class="step-number">1</div>
                    <h3 class="step-title">Create Account</h3>
                    <p class="step-description">
                        Sign up for free and complete your KYC verification.
                        No hidden fees, no lengthy paperwork required.
                    </p>
                </div>

                <div class="step-card animate-fade-in-up animate-delay-2">
                    <div class="step-number">2</div>
                    <h3 class="step-title">Browse Properties</h3>
                    <p class="step-description">
                        Explore our curated collection of verified properties.
                        Use AI-powered insights to make informed decisions.
                    </p>
                </div>

                <div class="step-card animate-fade-in-up animate-delay-3">
                    <div class="step-number">3</div>
                    <h3 class="step-title">Invest Securely</h3>
                    <p class="step-description">
                        Make payments through Razorpay's secure gateway.
                        Start with as low as ₹10,000 investment.
                    </p>
                </div>

                <div class="step-card animate-fade-in-up animate-delay-4">
                    <div class="step-number">4</div>
                    <h3 class="step-title">Track Returns</h3>
                    <p class="step-description">
                        Monitor your investment performance in real-time.
                        Receive regular updates and rental income.
                    </p>
                </div>
            </div>

            <div class="text-center mt-5">
                <sec:authorize access="!isAuthenticated()">
                    <a href="/register" class="btn-hero-primary">
                        <i class="bi bi-person-plus"></i>Get Started Today
                    </a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <a href="https://rzp.io/rzp/H82EaBe" class="btn-hero-primary">
                        <i class="bi bi-currency-rupee"></i>Start Investing
                    </a>
                </sec:authorize>
            </div>
        </div>
    </section>

    <!-- 💬 TESTIMONIALS SECTION -->
    <section class="testimonials">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5">
                    <h2 class="section-title">What Our Investors Say</h2>
                    <p class="section-subtitle">
                        Hear from successful investors who have transformed their portfolios with PropInvest
                    </p>
                </div>
            </div>

            <div class="testimonials-grid">
                <div class="testimonial-card animate-fade-in-up animate-delay-1">
                    <div class="testimonial-quote">
                        <i class="bi bi-quote"></i>
                    </div>
                    <div class="testimonial-content">
                        "PropInvest's AI analytics helped me identify undervalued properties that generated 22% ROI in my first year. The platform's insights are invaluable."
                    </div>
                    <div class="testimonial-author">Rajesh Sharma</div>
                    <div class="testimonial-role">Real Estate Investor</div>
                </div>

                <div class="testimonial-card animate-fade-in-up animate-delay-2">
                    <div class="testimonial-quote">
                        <i class="bi bi-quote"></i>
                    </div>
                    <div class="testimonial-content">
                        "The Razorpay integration makes investing so seamless. I can track all my investments in one place and the security gives me complete peace of mind."
                    </div>
                    <div class="testimonial-author">Priya Nair</div>
                    <div class="testimonial-role">Portfolio Manager</div>
                </div>

                <div class="testimonial-card animate-fade-in-up animate-delay-3">
                    <div class="testimonial-quote">
                        <i class="bi bi-quote"></i>
                    </div>
                    <div class="testimonial-content">
                        "As a first-time investor, PropInvest guided me through every step. I've already invested in three profitable properties with excellent returns."
                    </div>
                    <div class="testimonial-author">Arjun Patel</div>
                    <div class="testimonial-role">New Investor</div>
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
                    <div class="stat-number" data-count="${propertyCount > 0 ? propertyCount : 2500}">0</div>
                    <div class="stat-label">Premium Properties</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-people-fill"></i>
                    </div>
                    <div class="stat-number" data-count="${userCount > 0 ? userCount : 50000}">0</div>
                    <div class="stat-label">Active Investors</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-currency-rupee"></i>
                    </div>
                    <div class="stat-number" data-count="500">0</div>
                    <div class="stat-label">Crores+ Invested</div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-graph-up"></i>
                    </div>
                    <div class="stat-number" data-count="22">0</div>
                    <div class="stat-label">Average ROI %</div>
                </div>
            </div>
        </div>
    </section>

    <!-- 🌟 FEATURES SECTION -->
    <section class="section">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5">
                    <h2 class="section-title">Why Choose PropInvest?</h2>
                    <p class="section-subtitle">
                        Discover the powerful features that make us India's leading platform for smart real estate investments
                    </p>
                </div>
            </div>

            <div class="features-grid">
                <div class="feature-card animate-fade-in-up animate-delay-1">
                    <div class="feature-icon">
                        <i class="bi bi-shield-check"></i>
                    </div>
                    <h4 class="feature-title">Razorpay Secure Payments</h4>
                    <p class="feature-description">
                        Bank-level security with Razorpay integration, encrypted transactions, and
                        role-based access control to protect your investments.
                    </p>
                </div>

                <div class="feature-card animate-fade-in-up animate-delay-2">
                    <div class="feature-icon">
                        <i class="bi bi-graph-up-arrow"></i>
                    </div>
                    <h4 class="feature-title">AI-Powered Analytics</h4>
                    <p class="feature-description">
                        Calculate ROI, rental yield, and comprehensive investment metrics with our
                        sophisticated analytical tools and real-time market data.
                    </p>
                </div>

                <div class="feature-card animate-fade-in-up animate-delay-3">
                    <div class="feature-icon">
                        <i class="bi bi-robot"></i>
                    </div>
                    <h4 class="feature-title">Smart Recommendations</h4>
                    <p class="feature-description">
                        Get instant property recommendations and personalized investment advice from our
                        intelligent AI assistant powered by machine learning.
                    </p>
                </div>

                <div class="feature-card animate-fade-in-up animate-delay-4">
                    <div class="feature-icon">
                        <i class="bi bi-pie-chart"></i>
                    </div>
                    <h4 class="feature-title">Portfolio Tracking</h4>
                    <p class="feature-description">
                        Monitor your investment portfolio performance with detailed analytics,
                        automated reporting, and real-time performance updates.
                    </p>
                </div>

                <div class="feature-card animate-fade-in-up animate-delay-1">
                    <div class="feature-icon">
                        <i class="bi bi-clock"></i>
                    </div>
                    <h4 class="feature-title">Instant Processing</h4>
                    <p class="feature-description">
                        Quick property verification, instant payment processing, and
                        immediate investment confirmations with zero delays.
                    </p>
                </div>

                <div class="feature-card animate-fade-in-up animate-delay-2">
                    <div class="feature-icon">
                        <i class="bi bi-headset"></i>
                    </div>
                    <h4 class="feature-title">Expert Support</h4>
                    <p class="feature-description">
                        Get round-the-clock support from our experienced team of real estate professionals
                        and investment advisors whenever you need assistance.
                    </p>
                </div>
            </div>
        </div>
    </section>

    <!-- 🚀 FINAL CTA SECTION -->
    <section class="final-cta">
        <div class="container">
            <div class="cta-content">
                <h2 class="cta-title">Ready to Start Your Investment Journey?</h2>
                <p class="cta-subtitle">
                    Join thousands of successful investors who trust PropInvest for their real estate investment needs.
                    Start building your wealth today with our AI-powered platform and Razorpay secure payments.
                </p>

                <div class="hero-buttons">
                    <sec:authorize access="!isAuthenticated()">
                        <a href="/register" class="btn-hero-primary">
                            <i class="bi bi-person-plus"></i>Get Started Free
                        </a>
                        <a href="/emi-calculator" class="btn-hero-secondary">
                            <i class="bi bi-calculator"></i>Try EMI Calculator
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <a href="https://rzp.io/rzp/H82EaBe" class="btn-hero-primary">
                            <i class="bi bi-credit-card"></i>Start Investing Now
                        </a>
                        <a href="/properties" class="btn-hero-secondary">
                            <i class="bi bi-search"></i>Browse Properties
                        </a>
                    </sec:authorize>
                </div>

                <!-- Trust Indicators -->
                <div class="row text-center mt-5">
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-shield-check" style="font-size: 2rem; color: var(--primary); margin-bottom: 1rem;"></i>
                            <small style="color: var(--text-muted);">Razorpay Secured</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-clock" style="font-size: 2rem; color: var(--primary); margin-bottom: 1rem;"></i>
                            <small style="color: var(--text-muted);">24/7 Support</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-graph-up" style="font-size: 2rem; color: var(--primary); margin-bottom: 1rem;"></i>
                            <small style="color: var(--text-muted);">22% Avg Returns</small>
                        </div>
                    </div>
                    <div class="col-md-3 col-6 mb-4">
                        <div class="d-flex flex-column align-items-center">
                            <i class="bi bi-people" style="font-size: 2rem; color: var(--primary); margin-bottom: 1rem;"></i>
                            <small style="color: var(--text-muted);">50K+ Investors</small>
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
    
    <!-- Enhanced JavaScript with Razorpay Integration -->
    <script>
        // Razorpay Payment Integration
        function initiatePayment() {
            const options = {
                "key": "rzp_test_your_key_here", // Replace with your Razorpay key
                "amount": "1000000", // Amount in paise (₹10,000)
                "currency": "INR",
                "name": "PropInvest",
                "description": "Real Estate Investment",
                "image": "/images/logo.png",
                "order_id": "", // Will be generated from backend
                "handler": function (response) {
                    console.log('Payment Success:', response);
                    // Redirect to success page
                    window.location.href = '/payment/success?payment_id=' + response.razorpay_payment_id;
                },
                "prefill": {
                    "name": "${pageContext.request.userPrincipal.name || ''}",
                    "email": "",
                    "contact": ""
                },
                "notes": {
                    "address": "Real Estate Investment"
                },
                "theme": {
                    "color": "#7c3aed"
                },
                "modal": {
                    "ondismiss": function() {
                        console.log('Payment cancelled by user');
                    }
                }
            };

            const rzp = new Razorpay(options);
            rzp.on('payment.failed', function (response) {
                console.error('Payment Failed:', response.error);
                alert('Payment failed: ' + response.error.description);
            });
            rzp.open();
        }

        // Enhanced Form Validation
        function validateSearchForm() {
            const form = document.getElementById('propertySearchForm');
            const location = document.getElementById('searchLocation').value.trim();

            if (!location) {
                showNotification('Please enter a location to search', 'warning');
                return false;
            }

            return true;
        }

        // Notification System
        function showNotification(message, type) {
            if (!type) type = 'info';
            const notification = document.createElement('div');
            notification.className = 'notification notification-' + type;

            var iconClass = type === 'success' ? 'check-circle' : (type === 'warning' ? 'exclamation-triangle' : 'info-circle');
            var bgColor = type === 'success' ? '#10b981' : (type === 'warning' ? '#f59e0b' : '#3b82f6');

            notification.innerHTML = '<div class="notification-content">' +
                '<i class="bi bi-' + iconClass + '"></i>' +
                '<span>' + message + '</span>' +
                '<button onclick="this.parentElement.parentElement.remove()" class="notification-close">' +
                    '<i class="bi bi-x"></i>' +
                '</button>' +
                '</div>';

            // Add styles
            notification.style.cssText = 'position: fixed; ' +
                'top: 100px; ' +
                'right: 20px; ' +
                'z-index: 10000; ' +
                'background: ' + bgColor + '; ' +
                'color: white; ' +
                'padding: 1rem 1.5rem; ' +
                'border-radius: 8px; ' +
                'box-shadow: 0 4px 12px rgba(0,0,0,0.3); ' +
                'transform: translateX(100%); ' +
                'transition: transform 0.3s ease; ' +
                'max-width: 400px;';

            document.body.appendChild(notification);

            // Animate in
            setTimeout(() => {
                notification.style.transform = 'translateX(0)';
            }, 100);

            // Auto remove after 5 seconds
            setTimeout(() => {
                notification.style.transform = 'translateX(100%)';
                setTimeout(() => notification.remove(), 300);
            }, 5000);
        }

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

        // Initialize scroll animations
        function initScrollAnimations() {
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                    }
                });
            }, { threshold: 0.1 });

            document.querySelectorAll('.animate-fade-in-up').forEach(el => {
                el.style.opacity = '0';
                el.style.transform = 'translateY(30px)';
                el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                observer.observe(el);
            });
        }

        // Enhanced form handling
        function initFormHandling() {
            const searchForm = document.getElementById('propertySearchForm');
            if (searchForm) {
                searchForm.addEventListener('submit', function(e) {
                    if (!validateSearchForm()) {
                        e.preventDefault();
                    } else {
                        showNotification('Searching for properties...', 'info');
                    }
                });
            }

            // Add input animations and validation
            document.querySelectorAll('.search-form-input').forEach(input => {
                input.addEventListener('focus', function() {
                    this.style.transform = 'scale(1.02)';
                    this.style.boxShadow = '0 0 0 4px rgba(124, 58, 237, 0.25)';
                });

                input.addEventListener('blur', function() {
                    this.style.transform = 'scale(1)';
                    this.style.boxShadow = 'none';
                });
            });
        }

        // Investment amount calculation
        function calculateInvestmentReturn(amount) {
            const roi = 0.22; // 22% average return
            const annualReturn = amount * roi;
            const monthlyReturn = annualReturn / 12;

            return {
                principal: amount,
                annualReturn: annualReturn,
                monthlyReturn: monthlyReturn,
                totalAfterYear: amount + annualReturn
            };
        }

        // Initialize all functions when DOM is ready
        document.addEventListener('DOMContentLoaded', function() {
            initCounters();
            initScrollAnimations();
            initFormHandling();

            // Add loading complete class for final animations
            setTimeout(() => {
                document.body.classList.add('loaded');
                showNotification('Welcome to PropInvest! Start your investment journey today.', 'success');
            }, 1000);
        });

        // Utility functions
        window.PropInvest = {
            payment: {
                initiate: initiatePayment,
                calculate: calculateInvestmentReturn
            },
            utils: {
                formatCurrency: function(amount) {
                    return new Intl.NumberFormat('en-IN', {
                        style: 'currency',
                        currency: 'INR'
                    }).format(amount);
                },

                formatNumber: function(number) {
                    return new Intl.NumberFormat('en-IN').format(number);
                },

                showNotification: showNotification
            }
        };
    </script>
    
    <!-- Include main.js if it exists -->
    <script src="/js/main.js" onerror="console.log('main.js not found, using inline scripts only')"></script>
</body>
</html>