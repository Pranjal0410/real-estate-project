<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Browse premium properties - Real Estate Investment Platform">
    <title>Properties - PropInvest</title>
    
    <!-- Bootstrap 5 CSS from CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Google Fonts for Shadcn-style typography -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link href="/css/magicbricks-exact.css" rel="stylesheet">
    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- AOS Animation -->
    <link href="https://unpkg.com/aos@2.3.4/dist/aos.css" rel="stylesheet">
</head>
<body>
    <div id="scrollProgress"></div>
    
    <!-- Include Magicbricks-Style Navigation -->
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <!-- Hero Section -->
    <section class="hero-section" style="min-height: 40vh; background: var(--gradient-primary);">
        <div class="container">
            <div class="hero-content">
                <h1 class="hero-title text-white" data-aos="fade-up">Premium Properties</h1>
                <p class="hero-subtitle text-white" data-aos="fade-up" data-aos-delay="200">
                    Discover your next investment opportunity from our curated collection of premium properties.
                </p>
            </div>
        </div>
    </section>
    
    <!-- Properties Section -->
    <section class="py-5" style="background: var(--bg-primary); padding-top: 140px;">
        <div class="container">
            <!-- Search & Filter Bar -->
            <div class="row mb-5">
                <div class="col-12">
                    <div class="card shadow-sm border-0" data-aos="fade-up">
                        <div class="card-body p-4">
                            <h5 class="card-title mb-4 text-dark">
                                <i class="bi bi-funnel me-2 text-danger"></i>Find Your Perfect Property
                            </h5>
                            <form method="get" action="/properties" class="row g-3">
                                <div class="col-md-3">
                                    <label for="location" class="form-label fw-semibold">Location</label>
                                    <input type="text" class="form-control" id="location" name="location" 
                                           placeholder="Enter city or state" value="${param.location}">
                                </div>
                                <div class="col-md-2">
                                    <label for="type" class="form-label fw-semibold">Type</label>
                                    <select class="form-select" id="type" name="type">
                                        <option value="">All Types</option>
                                        <option value="HOUSE" ${param.type == 'HOUSE' ? 'selected' : ''}>House</option>
                                        <option value="APARTMENT" ${param.type == 'APARTMENT' ? 'selected' : ''}>Apartment</option>
                                        <option value="CONDO" ${param.type == 'CONDO' ? 'selected' : ''}>Condo</option>
                                        <option value="VILLA" ${param.type == 'VILLA' ? 'selected' : ''}>Villa</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="minPrice" class="form-label fw-semibold">Min Price</label>
                                    <select class="form-select" id="minPrice" name="minPrice">
                                        <option value="">Any</option>
                                        <option value="100000" ${param.minPrice == '100000' ? 'selected' : ''}>$100K</option>
                                        <option value="500000" ${param.minPrice == '500000' ? 'selected' : ''}>$500K</option>
                                        <option value="1000000" ${param.minPrice == '1000000' ? 'selected' : ''}>$1M</option>
                                        <option value="2000000" ${param.minPrice == '2000000' ? 'selected' : ''}>$2M</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="maxPrice" class="form-label fw-semibold">Max Price</label>
                                    <select class="form-select" id="maxPrice" name="maxPrice">
                                        <option value="">Any</option>
                                        <option value="500000" ${param.maxPrice == '500000' ? 'selected' : ''}>$500K</option>
                                        <option value="1000000" ${param.maxPrice == '1000000' ? 'selected' : ''}>$1M</option>
                                        <option value="2000000" ${param.maxPrice == '2000000' ? 'selected' : ''}>$2M</option>
                                        <option value="5000000" ${param.maxPrice == '5000000' ? 'selected' : ''}>$5M</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <label for="bedrooms" class="form-label fw-semibold">Bedrooms</label>
                                    <select class="form-select" id="bedrooms" name="bedrooms">
                                        <option value="">Any</option>
                                        <option value="1" ${param.bedrooms == '1' ? 'selected' : ''}>1+</option>
                                        <option value="2" ${param.bedrooms == '2' ? 'selected' : ''}>2+</option>
                                        <option value="3" ${param.bedrooms == '3' ? 'selected' : ''}>3+</option>
                                        <option value="4" ${param.bedrooms == '4' ? 'selected' : ''}>4+</option>
                                    </select>
                                </div>
                                <div class="col-md-1 d-flex align-items-end">
                                    <button type="submit" class="btn btn-danger w-100">
                                        <i class="bi bi-search"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Results Header -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="d-flex justify-content-between align-items-center">
                        <h3 class="text-dark mb-0">
                            <c:choose>
                                <c:when test="${not empty properties}">
                                    ${fn:length(properties)} Properties Found
                                </c:when>
                                <c:otherwise>
                                    No Properties Found
                                </c:otherwise>
                            </c:choose>
                        </h3>
                        <div class="d-flex align-items-center gap-3">
                            <span class="text-muted">Sort by:</span>
                            <select class="form-select" style="width: auto;">
                                <option>Price: Low to High</option>
                                <option>Price: High to Low</option>
                                <option>Newest First</option>
                                <option>Area: Largest First</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Magicbricks-Style Property Cards -->
            <div class="properties-list">
                <c:choose>
                    <c:when test="${not empty properties}">
                        <c:forEach items="${properties}" var="property" varStatus="status">
                            <div class="property-card-magicbricks" data-aos="fade-up" data-aos-delay="${status.index * 50}">
                                <!-- Property Image Section -->
                                <div class="property-image-section">
                                    <c:choose>
                                        <c:when test="${not empty property.imageUrl}">
                                            <img src="${property.imageUrl}" alt="${property.title}" loading="lazy">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=250&q=80" 
                                                 alt="${property.title}" loading="lazy">
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    <!-- Image Overlays -->
                                    <div class="photos-overlay">
                                        <i class="bi bi-images"></i> 5+ Photos
                                    </div>
                                    <div class="updated-overlay">New</div>
                                    <c:if test="${property.verified == true}">
                                        <div class="verified-badge">Verified</div>
                                    </c:if>
                                </div>
                                
                                <!-- Property Details Section -->
                                <div class="property-details-section">
                                    <h3 class="property-title-magicbricks">${property.title}</h3>
                                    
                                    <!-- Property Badges -->
                                    <div class="property-badges">
                                        <span class="property-badge status-ready">
                                            <i class="bi bi-check-circle"></i> Ready to Move
                                        </span>
                                        <c:if test="${property.bedrooms != null && property.bedrooms > 0}">
                                            <span class="property-badge">
                                                <i class="bi bi-door-open"></i> ${property.bedrooms} BHK
                                            </span>
                                        </c:if>
                                        <c:if test="${property.area != null && property.area > 0}">
                                            <span class="property-badge">
                                                <i class="bi bi-rulers"></i> <fmt:formatNumber value="${property.area}" pattern=",###"/> sq ft
                                            </span>
                                        </c:if>
                                        <span class="property-badge">
                                            <i class="bi bi-buildings"></i> ${property.propertyType}
                                        </span>
                                    </div>
                                    
                                    <p class="property-description-magicbricks">
                                        Premium ${property.propertyType.toLowerCase()} located in ${property.location} with modern amenities and excellent connectivity. Perfect for ${property.bedrooms > 2 ? 'families' : 'young professionals'}.
                                    </p>
                                    
                                    <div class="owner-details">
                                        <i class="bi bi-person"></i> By Owner | 
                                        <i class="bi bi-geo-alt"></i> ${property.location}
                                    </div>
                                </div>
                                
                                <!-- Property Price Section -->
                                <div class="property-price-section">
                                    <div class="price-container">
                                        <h2 class="property-price-magicbricks">
                                            ₹<fmt:formatNumber value="${property.price / 100000}" pattern="##.##"/> L
                                        </h2>
                                        <c:if test="${property.area != null && property.area > 0}">
                                            <p class="price-per-sqft">
                                                ₹<fmt:formatNumber value="${(property.price / property.area)}" pattern=",###"/> per sq ft
                                            </p>
                                        </c:if>
                                    </div>
                                    
                                    <div class="property-actions">
                                        <a href="/property/${property.id}" class="btn-contact-owner">
                                            <i class="bi bi-eye"></i> View Details
                                        </a>
                                        <button class="btn-get-phone" onclick="contactOwner(${property.id})">
                                            <i class="bi bi-telephone"></i> Contact Owner
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <div class="property-card-magicbricks">
                                <div class="text-center w-100 py-5">
                                    <i class="bi bi-house-door display-1 mb-4" style="color: var(--accent-primary);"></i>
                                    <h3 style="color: var(--text-primary);">No Properties Found</h3>
                                    <p style="color: var(--text-secondary);">Try adjusting your search criteria or check back later for new listings.</p>
                                    <a href="/properties" class="btn-contact-owner">
                                        <i class="bi bi-arrow-clockwise me-2"></i>Reset Filters
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- Pagination -->
            <c:if test="${not empty properties}">
                <div class="row mt-5">
                    <div class="col-12">
                        <nav aria-label="Properties pagination">
                            <ul class="pagination justify-content-center">
                                <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                                <li class="page-item active"><a class="page-link" href="#">1</a></li>
                                <li class="page-item"><a class="page-link" href="#">2</a></li>
                                <li class="page-item"><a class="page-link" href="#">3</a></li>
                                <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </c:if>
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
        
        // Contact functions
        function contactOwner(propertyId) {
            alert('Contact Owner functionality for Property ID: ' + propertyId + '\n\nThis would open contact form or show owner details.');
        }
        
        function toggleFavorite(propertyId) {
            const heartIcon = event.target.closest('button').querySelector('i');
            if (heartIcon.classList.contains('bi-heart')) {
                heartIcon.classList.remove('bi-heart');
                heartIcon.classList.add('bi-heart-fill', 'text-danger');
            } else {
                heartIcon.classList.remove('bi-heart-fill', 'text-danger');
                heartIcon.classList.add('bi-heart');
            }
        }
    </script>
</body>
</html>