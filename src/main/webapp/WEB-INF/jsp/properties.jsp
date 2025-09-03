<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Browse all properties - Real Estate Investment Platform">
    <title>Properties - PropInvest</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Custom CSS -->
    <link href="/css/style.css" rel="stylesheet">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="bg-dark-theme">
    <div id="scrollProgress"></div>
    
    <!-- Include Navigation -->
    <jsp:include page="includes/navbar.jsp"/>
    
    <!-- Hero Section -->
    <section class="hero-section" style="min-height: 60vh;">
        <div class="hero-content">
            <h1 class="hero-title">All Properties</h1>
            <p class="hero-subtitle">
                Discover your next investment opportunity from our extensive portfolio of premium properties.
            </p>
        </div>
    </section>
    
    <!-- Properties Section -->
    <section class="properties-section">
        <div class="container">
            <!-- Filter Section -->
            <div class="row mb-5">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Filter Properties</h5>
                            <form method="get" action="/properties" class="row g-3">
                                <div class="col-md-4">
                                    <label for="location" class="form-label">Location</label>
                                    <input type="text" class="form-control" id="location" name="location" placeholder="Enter city or state">
                                </div>
                                <div class="col-md-4">
                                    <label for="type" class="form-label">Property Type</label>
                                    <select class="form-select" id="type" name="type">
                                        <option value="">All Types</option>
                                        <option value="HOUSE">House</option>
                                        <option value="APARTMENT">Apartment</option>
                                        <option value="CONDO">Condo</option>
                                        <option value="VILLA">Villa</option>
                                        <option value="TOWNHOUSE">Townhouse</option>
                                    </select>
                                </div>
                                <div class="col-md-4 d-flex align-items-end">
                                    <button type="submit" class="btn btn-primary w-100">
                                        <i class="bi bi-search me-2"></i>Filter
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Properties Grid -->
            <div class="properties-grid">
                <c:choose>
                    <c:when test="${not empty properties}">
                        <c:forEach items="${properties}" var="property" varStatus="status">
                            <div data-aos="fade-up" data-aos-delay="${status.index * 50}">
                                <div class="property-card" data-tilt data-tilt-max="6" data-tilt-speed="400">
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
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12 text-center py-5">
                            <i class="bi bi-house-door display-1 text-muted mb-4"></i>
                            <h3>No Properties Found</h3>
                            <p class="text-muted">Try adjusting your search criteria or check back later for new listings.</p>
                            <a href="/properties" class="btn btn-primary">
                                <i class="bi bi-arrow-clockwise me-2"></i>Reset Filters
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
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
    <script src="https://cdn.jsdelivr.net/npm/vanilla-tilt@1.8.1/dist/vanilla-tilt.min.js"></script>
    
    <script>
        AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
        if (window.VanillaTilt) { VanillaTilt.init(document.querySelectorAll('[data-tilt]')); }
        
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