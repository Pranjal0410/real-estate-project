<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    <link href="/css/components.css" rel="stylesheet">
    <link href="/css/navbar.css" rel="stylesheet">
    <link href="/css/sections.css" rel="stylesheet">
    <link href="/css/properties-listing.css" rel="stylesheet">
    
    
</head>
<body class="properties-page">
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
    <section class="py-5" style="background: var(--background); padding-top: 140px;">
        <div class="container-center">
            <!-- Search & Filter Bar -->
            <div class="row mb-5">
                <div class="col-12">
                    <div class="search-filter-card">
                        <div class="card-body">
                            <h5 class="card-title">
                                <i class="bi bi-funnel me-2" style="color: var(--primary);"></i>Find Your Perfect Property
                            </h5>
                            <form method="get" action="/properties" class="row g-3">
                                <div class="col-md-3">
                                    <label for="location" class="form-label fw-semibold">Location</label>
                                    <select class="form-select" id="location" name="location" onchange="this.form.submit()">
                                        <option value="">All Cities</option>
                                        <option value="pune" ${param.location == 'pune' ? 'selected' : ''}>Pune</option>
                                        <option value="chandigarh" ${param.location == 'chandigarh' ? 'selected' : ''}>Chandigarh</option>
                                        <option value="delhi" ${param.location == 'delhi' ? 'selected' : ''}>Delhi</option>
                                        <option value="bangalore" ${param.location == 'bangalore' ? 'selected' : ''}>Bangalore</option>
                                    </select>
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
            <div class="results-header">
                <div class="d-flex justify-content-between align-items-center">
                    <h3>
                        <c:choose>
                            <c:when test="${not empty properties}">
                                ${fn:length(properties)} Properties Found
                            </c:when>
                            <c:otherwise>
                                No Properties Found
                            </c:otherwise>
                        </c:choose>
                    </h3>
                    <div class="sort-controls">
                        <span class="text-muted">Sort by:</span>
                        <select class="form-select">
                            <option>Price: Low to High</option>
                            <option>Price: High to Low</option>
                            <option>Newest First</option>
                            <option>Area: Largest First</option>
                        </select>
                    </div>
                </div>
            </div>
            
            <!-- Enhanced Property Grid -->
            <div class="row g-4" id="propertyGrid">
                <c:choose>
                    <c:when test="${not empty properties}">
                        <c:forEach items="${properties}" var="property" varStatus="status">
                            <div class="col-12">
                                <div class="property-card-professional">
                                    <div class="row g-0">
                                        <!-- Property Image -->
                                        <div class="col-md-4">
                                            <div class="property-image-professional">
                                                <c:choose>
                                                    <c:when test="${not empty property.imageUrl}">
                                                        <img src="${property.imageUrl}" alt="${property.title}" loading="lazy">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300&q=80"
                                                             alt="${property.title}" loading="lazy">
                                                    </c:otherwise>
                                                </c:choose>

                                                <!-- Property Badge -->
                                                <div class="property-badge-professional">${property.propertyType}</div>

                                                <!-- Favorite Button -->
                                                <button class="favorite-btn" onclick="toggleFavorite(${property.id})">
                                                    <i class="bi bi-heart"></i>
                                                </button>
                                            </div>
                                        </div>

                                        <!-- Property Details -->
                                        <div class="col-md-5">
                                            <div class="property-details-professional">
                                                <h3 class="property-title-professional">${property.title}</h3>

                                                <div class="property-location-professional">
                                                    <i class="bi bi-geo-alt-fill"></i>
                                                    ${property.location}
                                                </div>

                                                <div class="property-features-professional">
                                                    <c:if test="${property.bedrooms != null && property.bedrooms > 0}">
                                                        <span class="feature-badge-professional">
                                                            <i class="bi bi-door-open"></i>${property.bedrooms} BHK
                                                        </span>
                                                    </c:if>
                                                    <c:if test="${property.bathrooms != null && property.bathrooms > 0}">
                                                        <span class="feature-badge-professional">
                                                            <i class="bi bi-droplet"></i>${property.bathrooms} Bath
                                                        </span>
                                                    </c:if>
                                                    <c:if test="${property.area != null && property.area > 0}">
                                                        <span class="feature-badge-professional">
                                                            <i class="bi bi-rulers"></i><fmt:formatNumber value="${property.area}" pattern=",###"/> sq ft
                                                        </span>
                                                    </c:if>
                                                    <span class="feature-badge-professional">
                                                        <i class="bi bi-check-circle"></i>Ready to Move
                                                    </span>
                                                </div>

                                                <p class="property-description-professional">
                                                    Premium ${property.propertyType.toLowerCase()} in ${property.location} with modern amenities and excellent connectivity.
                                                </p>

                                                <div class="property-meta-professional">
                                                    <i class="bi bi-person"></i> By Owner
                                                    <span>•</span>
                                                    <i class="bi bi-clock"></i> 2 days ago
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Property Price & Actions -->
                                        <div class="col-md-3">
                                            <div class="property-price-section-professional">
                                                <div>
                                                    <div class="property-price-professional">
                                                        ₹<fmt:formatNumber value="${property.price / 100000}" pattern="##.##"/> L
                                                    </div>
                                                    <c:if test="${property.area != null && property.area > 0}">
                                                        <div class="price-per-sqft-professional">
                                                            ₹<fmt:formatNumber value="${(property.price / property.area)}" pattern=",###"/> per sq ft
                                                        </div>
                                                    </c:if>
                                                </div>

                                                <div class="property-actions-professional">
                                                    <a href="/property/${property.id}" class="btn-view-details-professional">
                                                        <i class="bi bi-eye"></i>View Details
                                                    </a>
                                                    <button class="btn-contact-professional" onclick="contactOwner(${property.id})">
                                                        <i class="bi bi-telephone"></i>Contact
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <div class="no-results-professional">
                                <i class="bi bi-house-door"></i>
                                <h3>No Properties Found</h3>
                                <p>Try adjusting your search criteria or check back later for new listings.</p>
                                <a href="/properties" class="btn-reset-filters">
                                    <i class="bi bi-arrow-clockwise"></i>Reset Filters
                                </a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- Pagination -->
            <c:if test="${not empty properties}">
                <div class="pagination-professional">
                    <nav aria-label="Properties pagination">
                        <ul class="pagination">
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
            </c:if>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp"/>
    
    <!-- Back to Top Button -->
    <button id="backToTop" aria-label="Back to top" title="Back to top">
        <i class="bi bi-arrow-up"></i>
    </button>
    
    <!-- Scripts from CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script>
        
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