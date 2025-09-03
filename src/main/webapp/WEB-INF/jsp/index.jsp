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
    
    <!-- Bootstrap 5 CSS -->
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Custom CSS -->
    <link href="/css/style.css" rel="stylesheet">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="/favicon.ico">

    <!-- Google Fonts Poppins -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

</head>
<body>
    <!-- Scroll Progress Bar -->
    <div id="scrollProgress"></div>
    <!-- Include Navigation -->
    <jsp:include page="includes/navbar.jsp"/>
    
    <!-- 🚀 ULTRA MODERN HERO SECTION -->
    <section class="hero-section">
        <div class="hero-content">
            <h1 class="hero-title">Smart Real Estate Investment Platform</h1>
            <p class="hero-subtitle">
                Discover smart investment opportunities with AI-powered analytics and comprehensive market insights. 
                Make informed decisions with our cutting-edge platform and join thousands of successful investors.
            </p>
            
            <!-- Hero Action Buttons -->
            <div class="hero-buttons">
                <a href="/properties" class="btn btn-hero-primary">
                    <i class="bi bi-search me-2"></i>Browse Properties
                </a>
                <a href="/calculator" class="btn btn-hero-secondary">
                    <i class="bi bi-calculator me-2"></i>Investment Calculator
                </a>
            </div>
        </div>
    </section>

    <!-- 🎯 IMPRESSIVE STATISTICS SECTION -->
    <section class="stats-section">
        <div class="container">
            <div class="stats-container" data-aos="fade-up">
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="bi bi-buildings"></i>
                    </div>
                    <div class="stat-number" data-count="${propertyCount > 0 ? propertyCount : 500}">0</div>
                    <div class="stat-label">Properties Listed</div>
                </div>
                
                <div class="stat-card" data-aos="fade-up" data-aos-delay="100">
                    <div class="stat-icon">
                        <i class="bi bi-people-fill"></i>
                    </div>
                    <div class="stat-number" data-count="${userCount > 0 ? userCount : 10000}">0</div>
                    <div class="stat-label">Registered Users</div>
                </div>
                
                <div class="stat-card" data-aos="fade-up" data-aos-delay="200">
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
                <div class="col-lg-8 mx-auto text-center mb-5" data-aos="fade-up">
                    <h2 class="section-title">Why Choose PropInvest?</h2>
                    <p class="section-subtitle">
                        Discover the powerful features that make us the leading platform for smart real estate investments
                    </p>
                </div>
            </div>
            
            <div class="row g-4">
                <!-- Feature 1: Advanced Analytics -->
                <div class="col-lg-4 col-md-6" data-aos="fade-up" data-aos-delay="100">
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
                <div class="col-lg-4 col-md-6" data-aos="fade-up" data-aos-delay="200">
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
                <div class="col-lg-4 col-md-6" data-aos="fade-up" data-aos-delay="300">
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
                <div class="col-lg-4 col-md-6" data-aos="fade-up" data-aos-delay="400">
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
                <div class="col-lg-4 col-md-6" data-aos="fade-up" data-aos-delay="500">
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
                <div class="col-lg-4 col-md-6" data-aos="fade-up" data-aos-delay="600">
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
    <section class="properties-section section-alt" data-aos="fade-up">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center mb-5" data-aos="fade-up">
                    <h2 class="section-title">Featured Properties</h2>
                    <p class="section-subtitle">
                        Discover our handpicked selection of premium investment opportunities with high ROI potential
                    </p>
                </div>
            </div>
            
            <div class="properties-grid mb-5">
                <c:choose>
                    <c:when test="${not empty properties}">
                        <c:forEach items="${properties}" var="property" varStatus="status">
                            <c:if test="${status.index < 12}">
                                <div data-aos="fade-up" data-aos-delay="${status.index * 50}">
                                    <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                        <!-- Property Image -->
                                        <div class="property-image">
                                            <c:choose>
                                                <c:when test="${not empty property.imageUrl}">
                                                    <img src="${property.imageUrl}" alt="${property.title}" loading="lazy">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="https://source.unsplash.com/400x250/?${property.propertyType != null ? property.propertyType.toLowerCase() : 'house'},property" alt="${property.title}" loading="lazy">
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
                                                $<fmt:formatNumber value="${property.price}" pattern=",###"/>
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
                                                        <i class="bi bi-rulers me-1"></i><fmt:formatNumber value="${property.area}" pattern=",###"/> sq ft
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
                        <!-- Demo Properties when no data available - 12 Properties in 3 columns -->
                        <div data-aos="fade-up">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?luxury,villa" alt="Modern Luxury Villa" loading="lazy">
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
                        
                        <div data-aos="fade-up" data-aos-delay="50">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?penthouse,apartment" alt="Downtown Penthouse" loading="lazy">
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
                        
                        <div data-aos="fade-up" data-aos-delay="100">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?suburban,family,house" alt="Family Suburban Home" loading="lazy">
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
                        
                        <!-- Row 2 -->
                        <div data-aos="fade-up" data-aos-delay="150">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?condo,modern" alt="Modern Condo" loading="lazy">
                                    <div class="property-badge">Condo</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Modern Waterfront Condo</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Miami, FL
                                    </div>
                                    <div class="property-price">$950,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>2 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>2 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>1,500 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div data-aos="fade-up" data-aos-delay="200">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?loft,industrial" alt="Industrial Loft" loading="lazy">
                                    <div class="property-badge">Loft</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Industrial Loft</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Brooklyn, NY
                                    </div>
                                    <div class="property-price">$1,200,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>1 Bed</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>1 Bath</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>1,800 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div data-aos="fade-up" data-aos-delay="250">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?townhouse,modern" alt="Modern Townhouse" loading="lazy">
                                    <div class="property-badge">Townhouse</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Modern Townhouse</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>San Francisco, CA
                                    </div>
                                    <div class="property-price">$1,750,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>3 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>3 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>2,200 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Row 3 -->
                        <div data-aos="fade-up" data-aos-delay="300">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?mansion,estate" alt="Luxury Estate" loading="lazy">
                                    <div class="property-badge">Estate</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Luxury Estate</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Malibu, CA
                                    </div>
                                    <div class="property-price">$5,200,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>7 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>8 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>8,500 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div data-aos="fade-up" data-aos-delay="350">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?studio,apartment" alt="Urban Studio" loading="lazy">
                                    <div class="property-badge">Studio</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Urban Studio</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Seattle, WA
                                    </div>
                                    <div class="property-price">$425,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>Studio</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>1 Bath</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>650 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div data-aos="fade-up" data-aos-delay="400">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?cottage,cozy" alt="Cozy Cottage" loading="lazy">
                                    <div class="property-badge">Cottage</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Cozy Mountain Cottage</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Denver, CO
                                    </div>
                                    <div class="property-price">$575,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>2 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>1 Bath</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>1,200 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Row 4 -->
                        <div data-aos="fade-up" data-aos-delay="450">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?duplex,modern" alt="Modern Duplex" loading="lazy">
                                    <div class="property-badge">Duplex</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Modern Duplex</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Phoenix, AZ
                                    </div>
                                    <div class="property-price">$725,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>3 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>2 Baths</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>1,950 sq ft</span>
                                    </div>
                                    <a href="/properties" class="btn btn-property">
                                        <i class="bi bi-eye me-2"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div data-aos="fade-up" data-aos-delay="500">
                            <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400" data-tilt-glare data-tilt-max-glare="0.08">
                                <div class="property-image">
                                    <img src="https://source.unsplash.com/400x250/?bungalow,charming" alt="Charming Bungalow" loading="lazy">
                                    <div class="property-badge">Bungalow</div>
                                </div>
                                <div class="property-content">
                                    <h5 class="property-title">Charming Bungalow</h5>
                                    <div class="property-location">
                                        <i class="bi bi-geo-alt-fill"></i>Portland, OR
                                    </div>
                                    <div class="property-price">$485,000</div>
                                    <div class="property-features">
                                        <span class="feature-badge"><i class="bi bi-door-open me-1"></i>2 Beds</span>
                                        <span class="feature-badge"><i class="bi bi-droplet me-1"></i>1 Bath</span>
                                        <span class="feature-badge"><i class="bi bi-rulers me-1"></i>1,100 sq ft</span>
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
            <div class="text-center" data-aos="fade-up">
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
                <div class="col-lg-8 mx-auto text-center mb-5" data-aos="fade-up">
                    <h2 class="section-title">What Our Investors Say</h2>
                    <p class="section-subtitle">
                        Hear from successful investors who have transformed their portfolios with PropInvest
                    </p>
                </div>
            </div>
            
            <div class="row g-4">
                <div class="col-lg-4" data-aos="fade-up">
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
                
                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="100">
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
                
                <div class="col-lg-4" data-aos="fade-up" data-aos-delay="200">
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

    <!-- 🚀 INVESTMENT CALCULATOR CTA SECTION -->
    <section class="cta-section" data-aos="fade-up">
        <div class="container">
            <div class="cta-content">
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
                <div class="row text-center" data-aos="fade-up" data-aos-delay="200">
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

    <!-- 🤖 AI ASSISTANT PROMO SECTION -->
    <section class="features-section" data-aos="fade-up">
        <div class="container">
            <div class="row align-items-center g-4">
                <div class="col-lg-6">
                    <div class="rounded-custom shadow-custom overflow-hidden" data-tilt data-tilt-max="5" data-tilt-speed="400">
                        <div id="lottie-ai" style="width:100%;height:400px;background:linear-gradient(135deg, rgba(37,99,235,.05), rgba(29,78,216,.05));"></div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <h3 class="h2 mb-3">Meet Your AI Investment Assistant</h3>
                    <p class="lead mb-4">Ask questions, get property recommendations, and make smarter decisions faster.</p>
                    <a class="btn btn-primary btn-lg" href="/chatbot">
                        <i class="bi bi-robot me-2"></i> Chat with AI
                    </a>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp"/>
    
    <!-- Scripts -->
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    
    <!-- AOS (Animate on Scroll) -->
    <link href="https://unpkg.com/aos@2.3.4/dist/aos.css" rel="stylesheet">
    <script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
    <script>
        AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
    </script>

    <!-- VanillaTilt for parallax tilt -->
    <script src="https://cdn.jsdelivr.net/npm/vanilla-tilt@1.8.1/dist/vanilla-tilt.min.js"></script>
    <script>
        if (window.VanillaTilt) {
            VanillaTilt.init(document.querySelectorAll('[data-tilt]'));
        }
    </script>

    <!-- Lottie for subtle animations -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lottie-web/5.12.2/lottie.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const lottieContainer = document.getElementById('lottie-ai');
            if (lottieContainer && window.lottie) {
                window.lottie.loadAnimation({
                    container: lottieContainer,
                    renderer: 'svg',
                    loop: true,
                    autoplay: true,
                    path: 'https://assets6.lottiefiles.com/private_files/lf30_mznpvt7n.json'
                });
            }
        });
    </script>
    
    <!-- Custom JavaScript -->
    <script>
        // Animated Counter Function
        function animateCounter(element, start, end, duration) {
            const startTime = performance.now();
            const formatter = new Intl.NumberFormat('en-US');
            function easeOutQuad(t) { return t * (2 - t); }
            function frame(now) {
                const elapsed = now - startTime;
                const progress = Math.min(1, elapsed / duration);
                const eased = easeOutQuad(progress);
                const value = Math.round(start + (end - start) * eased);
                element.textContent = formatter.format(value);
                if (progress < 1) requestAnimationFrame(frame);
            }
            requestAnimationFrame(frame);
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
        
        // Scroll-based animations
        function initScrollAnimations() {
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('animate');
                    }
                });
            }, { threshold: 0.1 });
            
            document.querySelectorAll('[data-aos]').forEach(el => {
                el.classList.add('animate-on-scroll');
                observer.observe(el);
            });
        }
        
        // Initialize all functions when DOM is ready
        document.addEventListener('DOMContentLoaded', function() {
            initCounters();
            initNavbarScroll();
            initSmoothScroll();
            initScrollAnimations();
            
            // Add loading complete class for final animations
            setTimeout(() => { document.body.classList.add('loaded'); }, 100);
        });

        // Scroll progress bar
        (function() {
            const bar = document.getElementById('scrollProgress');
            if (!bar) return;
            const update = () => {
                const scrollTop = window.scrollY;
                const docHeight = document.documentElement.scrollHeight - window.innerHeight;
                const width = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0;
                bar.style.width = width + '%';
            };
            window.addEventListener('scroll', update);
            window.addEventListener('resize', update);
            update();
        })();
        
        // Preload hero background image
        const heroImg = new Image();
        heroImg.src = 'https://source.unsplash.com/1600x900/?luxury-house,architecture,city';
        
        // Form validation helpers
        function validateEmail(email) {
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(email);
        }
        
        // Utility functions
        window.PropInvest = {
            utils: {
                formatCurrency: function(amount) {
                    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
                },
                formatNumber: function(number) {
                    return new Intl.NumberFormat('en-US').format(number);
                },
                debounce: function(func, wait) {
                    let timeout;
                    return function executedFunction(...args) {
                        const later = () => { clearTimeout(timeout); func(...args); };
                        clearTimeout(timeout);
                        timeout = setTimeout(later, wait);
                    };
                }
            }
        };
    </script>
    
    <!-- Include main.js if it exists -->
    <script src="/js/main.js" onerror="console.log('main.js not found, using inline scripts only')"></script>

    <!-- Back to Top Button -->
    <button id="backToTop" aria-label="Back to top" title="Back to top">
        <i class="bi bi-arrow-up"></i>
    </button>

    <script>
        (function() {
            const btn = document.getElementById('backToTop');
            const toggleBtn = () => {
                if (window.scrollY > 400) {
                    btn.classList.add('show');
                } else {
                    btn.classList.remove('show');
                }
            };
            window.addEventListener('scroll', toggleBtn);
            btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
            toggleBtn();
        })();
    </script>
</body>
</html>