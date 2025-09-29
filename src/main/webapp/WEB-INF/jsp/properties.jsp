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
            <!-- Enhanced Search & Filter Bar -->
            <div class="row mb-5">
                <div class="col-12">
                    <div class="search-filter-card">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h5 class="card-title mb-0">
                                    <i class="bi bi-funnel me-2" style="color: var(--primary);"></i>Find Your Perfect Property
                                </h5>
                                <c:if test="${hasFilters}">
                                    <a href="/properties" class="btn btn-outline-secondary btn-sm">
                                        <i class="bi bi-arrow-clockwise me-1"></i>Reset Filters
                                    </a>
                                </c:if>
                            </div>

                            <!-- Active Filters Display -->
                            <c:if test="${hasFilters}">
                                <div class="active-filters mb-3">
                                    <small class="text-muted">Active filters:</small>
                                    <div class="d-flex flex-wrap gap-2 mt-1">
                                        <c:if test="${not empty selectedLocation}">
                                            <span class="badge bg-primary">
                                                Location: ${selectedLocation}
                                                <a href="/properties?type=${selectedType}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&bedrooms=${selectedBedrooms}&budget=${selectedBudget}"
                                                   class="text-white ms-1" style="text-decoration: none;">×</a>
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty selectedType}">
                                            <span class="badge bg-primary">
                                                Type: ${selectedType}
                                                <a href="/properties?location=${selectedLocation}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&bedrooms=${selectedBedrooms}&budget=${selectedBudget}"
                                                   class="text-white ms-1" style="text-decoration: none;">×</a>
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty selectedMinPrice}">
                                            <span class="badge bg-primary">
                                                Min Price: ₹<fmt:formatNumber value="${selectedMinPrice / 100000}" pattern="##.##"/>L
                                                <a href="/properties?location=${selectedLocation}&type=${selectedType}&maxPrice=${selectedMaxPrice}&bedrooms=${selectedBedrooms}&budget=${selectedBudget}"
                                                   class="text-white ms-1" style="text-decoration: none;">×</a>
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty selectedMaxPrice}">
                                            <span class="badge bg-primary">
                                                Max Price: ₹<fmt:formatNumber value="${selectedMaxPrice / 100000}" pattern="##.##"/>L
                                                <a href="/properties?location=${selectedLocation}&type=${selectedType}&minPrice=${selectedMinPrice}&bedrooms=${selectedBedrooms}&budget=${selectedBudget}"
                                                   class="text-white ms-1" style="text-decoration: none;">×</a>
                                            </span>
                                        </c:if>
                                        <c:if test="${not empty selectedBedrooms}">
                                            <span class="badge bg-primary">
                                                Bedrooms: ${selectedBedrooms}+
                                                <a href="/properties?location=${selectedLocation}&type=${selectedType}&minPrice=${selectedMinPrice}&maxPrice=${selectedMaxPrice}&budget=${selectedBudget}"
                                                   class="text-white ms-1" style="text-decoration: none;">×</a>
                                            </span>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>

                            <form method="get" action="/properties" id="propertySearchForm">
                                <div class="row g-3">
                                    <!-- Location Filter -->
                                    <div class="col-lg-3 col-md-6">
                                        <label for="location" class="form-label fw-semibold">
                                            <i class="bi bi-geo-alt me-1"></i>Location
                                        </label>
                                        <select class="form-select" id="location" name="location">
                                            <option value="">All Cities</option>
                                            <option value="pune" ${param.location == 'pune' ? 'selected' : ''}>Pune</option>
                                            <option value="mumbai" ${param.location == 'mumbai' ? 'selected' : ''}>Mumbai</option>
                                            <option value="delhi" ${param.location == 'delhi' ? 'selected' : ''}>Delhi</option>
                                            <option value="bangalore" ${param.location == 'bangalore' ? 'selected' : ''}>Bangalore</option>
                                            <option value="hyderabad" ${param.location == 'hyderabad' ? 'selected' : ''}>Hyderabad</option>
                                            <option value="chennai" ${param.location == 'chennai' ? 'selected' : ''}>Chennai</option>
                                        </select>
                                    </div>

                                    <!-- Property Type Filter -->
                                    <div class="col-lg-2 col-md-6">
                                        <label for="type" class="form-label fw-semibold">
                                            <i class="bi bi-house me-1"></i>Type
                                        </label>
                                        <select class="form-select" id="type" name="type">
                                            <option value="">All Types</option>
                                            <option value="RESIDENTIAL" ${param.type == 'RESIDENTIAL' ? 'selected' : ''}>Residential</option>
                                            <option value="COMMERCIAL" ${param.type == 'COMMERCIAL' ? 'selected' : ''}>Commercial</option>
                                            <option value="APARTMENT" ${param.type == 'APARTMENT' ? 'selected' : ''}>Apartment</option>
                                            <option value="VILLA" ${param.type == 'VILLA' ? 'selected' : ''}>Villa</option>
                                            <option value="OFFICE" ${param.type == 'OFFICE' ? 'selected' : ''}>Office</option>
                                            <option value="RETAIL" ${param.type == 'RETAIL' ? 'selected' : ''}>Retail</option>
                                        </select>
                                    </div>

                                    <!-- Budget Range -->
                                    <div class="col-lg-3 col-md-6">
                                        <label for="budget" class="form-label fw-semibold">
                                            <i class="bi bi-currency-rupee me-1"></i>Budget Range
                                        </label>
                                        <select class="form-select" id="budget" name="budget">
                                            <option value="">Any Budget</option>
                                            <option value="0-2500000" ${param.budget == '0-2500000' ? 'selected' : ''}>₹0 - ₹25L</option>
                                            <option value="2500000-5000000" ${param.budget == '2500000-5000000' ? 'selected' : ''}>₹25L - ₹50L</option>
                                            <option value="5000000-10000000" ${param.budget == '5000000-10000000' ? 'selected' : ''}>₹50L - ₹1Cr</option>
                                            <option value="10000000-25000000" ${param.budget == '10000000-25000000' ? 'selected' : ''}>₹1Cr - ₹2.5Cr</option>
                                            <option value="25000000-50000000" ${param.budget == '25000000-50000000' ? 'selected' : ''}>₹2.5Cr - ₹5Cr</option>
                                            <option value="50000000-999999999" ${param.budget == '50000000-999999999' ? 'selected' : ''}>₹5Cr+</option>
                                        </select>
                                    </div>

                                    <!-- Bedrooms -->
                                    <div class="col-lg-2 col-md-6">
                                        <label for="bedrooms" class="form-label fw-semibold">
                                            <i class="bi bi-door-open me-1"></i>Bedrooms
                                        </label>
                                        <select class="form-select" id="bedrooms" name="bedrooms">
                                            <option value="">Any</option>
                                            <option value="1" ${param.bedrooms == '1' ? 'selected' : ''}>1+</option>
                                            <option value="2" ${param.bedrooms == '2' ? 'selected' : ''}>2+</option>
                                            <option value="3" ${param.bedrooms == '3' ? 'selected' : ''}>3+</option>
                                            <option value="4" ${param.bedrooms == '4' ? 'selected' : ''}>4+</option>
                                            <option value="5" ${param.bedrooms == '5' ? 'selected' : ''}>5+</option>
                                        </select>
                                    </div>

                                    <!-- Search Button -->
                                    <div class="col-lg-2 col-md-12 d-flex align-items-end">
                                        <div class="d-grid w-100">
                                            <button type="submit" class="btn btn-danger">
                                                <i class="bi bi-search me-1"></i>Search Properties
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- Advanced Filters Toggle -->
                                <div class="row mt-3">
                                    <div class="col-12">
                                        <button type="button" class="btn btn-link p-0" data-bs-toggle="collapse"
                                                data-bs-target="#advancedFilters" aria-expanded="false">
                                            <i class="bi bi-chevron-down me-1"></i>Advanced Filters
                                        </button>
                                    </div>
                                </div>

                                <!-- Advanced Filters Collapsible Section -->
                                <div class="collapse" id="advancedFilters">
                                    <div class="row g-3 mt-2 pt-3 border-top">
                                        <div class="col-md-3">
                                            <label for="minPrice" class="form-label fw-semibold">Min Price (₹)</label>
                                            <select class="form-select" id="minPrice" name="minPrice">
                                                <option value="">Any</option>
                                                <option value="1000000" ${param.minPrice == '1000000' ? 'selected' : ''}>₹10L</option>
                                                <option value="2500000" ${param.minPrice == '2500000' ? 'selected' : ''}>₹25L</option>
                                                <option value="5000000" ${param.minPrice == '5000000' ? 'selected' : ''}>₹50L</option>
                                                <option value="10000000" ${param.minPrice == '10000000' ? 'selected' : ''}>₹1Cr</option>
                                                <option value="25000000" ${param.minPrice == '25000000' ? 'selected' : ''}>₹2.5Cr</option>
                                                <option value="50000000" ${param.minPrice == '50000000' ? 'selected' : ''}>₹5Cr</option>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label for="maxPrice" class="form-label fw-semibold">Max Price (₹)</label>
                                            <select class="form-select" id="maxPrice" name="maxPrice">
                                                <option value="">Any</option>
                                                <option value="2500000" ${param.maxPrice == '2500000' ? 'selected' : ''}>₹25L</option>
                                                <option value="5000000" ${param.maxPrice == '5000000' ? 'selected' : ''}>₹50L</option>
                                                <option value="10000000" ${param.maxPrice == '10000000' ? 'selected' : ''}>₹1Cr</option>
                                                <option value="25000000" ${param.maxPrice == '25000000' ? 'selected' : ''}>₹2.5Cr</option>
                                                <option value="50000000" ${param.maxPrice == '50000000' ? 'selected' : ''}>₹5Cr</option>
                                                <option value="100000000" ${param.maxPrice == '100000000' ? 'selected' : ''}>₹10Cr</option>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label fw-semibold">Quick Price Range</label>
                                            <div class="d-flex gap-2">
                                                <button type="button" class="btn btn-outline-primary btn-sm quick-budget" data-budget="0-2500000">Under ₹25L</button>
                                                <button type="button" class="btn btn-outline-primary btn-sm quick-budget" data-budget="2500000-10000000">₹25L-1Cr</button>
                                                <button type="button" class="btn btn-outline-primary btn-sm quick-budget" data-budget="10000000-999999999">₹1Cr+</button>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label fw-semibold">Actions</label>
                                            <div class="d-grid">
                                                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="clearFilters()">
                                                    <i class="bi bi-arrow-clockwise me-1"></i>Clear All
                                                </button>
                                            </div>
                                        </div>
                                    </div>
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
                                                <img src="https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300&q=80"
                                                     alt="${property.title}" loading="lazy">

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
                                                    Premium ${property.propertyType} in ${property.location} with modern amenities and excellent connectivity.
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
                                                    <a href="https://rzp.io/rzp/H82EaBe" class="btn btn-success btn-sm">
                                                        <i class="bi bi-currency-rupee"></i>Pay Downpayment
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

        // Enhanced Filter Functions
        function clearFilters() {
            window.location.href = '/properties';
        }

        // Quick budget selection
        document.querySelectorAll('.quick-budget').forEach(button => {
            button.addEventListener('click', function() {
                const budget = this.getAttribute('data-budget');
                document.getElementById('budget').value = budget;

                // Update visual state
                document.querySelectorAll('.quick-budget').forEach(btn => btn.classList.remove('btn-primary'));
                document.querySelectorAll('.quick-budget').forEach(btn => btn.classList.add('btn-outline-primary'));
                this.classList.remove('btn-outline-primary');
                this.classList.add('btn-primary');

                // Auto-submit form
                document.getElementById('propertySearchForm').submit();
            });
        });

        // Auto-submit on filter change (optional - can be enabled/disabled)
        function enableAutoSubmit() {
            const form = document.getElementById('propertySearchForm');
            const selects = form.querySelectorAll('select');

            selects.forEach(select => {
                select.addEventListener('change', function() {
                    // Add small delay to allow user to see the change
                    setTimeout(() => {
                        form.submit();
                    }, 300);
                });
            });
        }

        // Advanced filters toggle icon rotation
        document.querySelector('[data-bs-target="#advancedFilters"]').addEventListener('click', function() {
            const icon = this.querySelector('i');
            if (icon.classList.contains('bi-chevron-down')) {
                icon.classList.remove('bi-chevron-down');
                icon.classList.add('bi-chevron-up');
            } else {
                icon.classList.remove('bi-chevron-up');
                icon.classList.add('bi-chevron-down');
            }
        });

        // Initialize enhanced filters
        document.addEventListener('DOMContentLoaded', function() {
            // Highlight active quick budget button
            const currentBudget = new URLSearchParams(window.location.search).get('budget');
            if (currentBudget) {
                document.querySelectorAll('.quick-budget').forEach(button => {
                    if (button.getAttribute('data-budget') === currentBudget) {
                        button.classList.remove('btn-outline-primary');
                        button.classList.add('btn-primary');
                    }
                });
            }

            // Optional: Enable auto-submit (uncomment if desired)
            // enableAutoSubmit();
        });

        // Mobile-friendly filter enhancements
        function toggleMobileFilters() {
            const filtersCard = document.querySelector('.search-filter-card');
            filtersCard.classList.toggle('mobile-expanded');
        }

        // Add responsive behavior for mobile
        if (window.innerWidth <= 768) {
            // Add mobile-specific enhancements
            document.querySelector('.search-filter-card').classList.add('mobile-optimized');
        }
    </script>
</body>
</html>