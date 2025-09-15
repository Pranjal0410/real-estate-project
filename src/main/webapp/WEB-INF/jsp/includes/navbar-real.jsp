<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- 🏙️ Magicbricks-Style Dark Purple Navigation -->
<header class="magicbricks-header">
    <!-- Top White Navigation -->
    <div class="top-navigation">
        <div class="container-fluid px-4">
            <div class="row align-items-center">
                <!-- Left: Logo & Main Menu -->
                <div class="col-lg-8">
                    <div class="d-flex align-items-center">
                        <!-- Logo -->
                        <a href="/" class="brand-logo">
                            <i class="bi bi-house-door-fill"></i>
                            <span>PropInvest</span>
                        </a>
                        
                        <!-- Main Menu -->
                        <nav class="main-menu">
                            <div class="menu-dropdown">
                                <button class="menu-btn">
                                    Buy <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-content">
                                    <div class="dropdown-section">
                                        <h6>Popular Choices</h6>
                                        <a href="/properties?type=apartment&purpose=buy">Flats in Mumbai</a>
                                        <a href="/properties?type=house&purpose=buy">Independent Houses</a>
                                        <a href="/properties?type=villa&purpose=buy">Villas for Sale</a>
                                    </div>
                                    <div class="dropdown-section">
                                        <h6>Property Type</h6>
                                        <a href="/properties?type=apartment">Apartments</a>
                                        <a href="/properties?type=house">Houses</a>
                                        <a href="/properties?type=villa">Villas</a>
                                        <a href="/properties?type=plot">Plots & Land</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="menu-dropdown">
                                <button class="menu-btn">
                                    Rent <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-content">
                                    <div class="dropdown-section">
                                        <h6>Rental Properties</h6>
                                        <a href="/properties?type=apartment&purpose=rent">Flats for Rent</a>
                                        <a href="/properties?type=house&purpose=rent">Houses for Rent</a>
                                        <a href="/properties?type=villa&purpose=rent">Villas for Rent</a>
                                    </div>
                                    <div class="dropdown-section">
                                        <h6>Co-living</h6>
                                        <a href="/properties?type=pg">PG & Hostels</a>
                                        <a href="/properties?type=coliving">Co-living Spaces</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="menu-dropdown">
                                <button class="menu-btn">
                                    Sell <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-content">
                                    <div class="dropdown-section">
                                        <h6>Sell Property</h6>
                                        <a href="/sell/free-listing">Post Property FREE</a>
                                        <a href="/sell/owner-package">Owner Package</a>
                                        <a href="/sell/pricing">View Pricing</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="menu-dropdown">
                                <button class="menu-btn">
                                    Home Loans <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-content">
                                    <div class="dropdown-section">
                                        <h6>Loan Services</h6>
                                        <a href="/emi-calculator">EMI Calculator</a>
                                        <a href="/loans/eligibility">Eligibility Check</a>
                                        <a href="/loans/application">Apply for Loan</a>
                                    </div>
                                </div>
                            </div>
                        </nav>
                    </div>
                </div>
                
                <!-- Right: Login & Actions -->
                <div class="col-lg-4">
                    <div class="d-flex align-items-center justify-content-end gap-3">
                        <sec:authorize access="!isAuthenticated()">
                            <a href="/login" class="nav-link-action">
                                <i class="bi bi-person"></i> Login
                            </a>
                        </sec:authorize>
                        
                        <sec:authorize access="isAuthenticated()">
                            <div class="user-menu">
                                <button class="nav-link-action dropdown-toggle" data-bs-toggle="dropdown">
                                    <i class="bi bi-person-circle"></i>
                                    <sec:authentication property="name"/>
                                </button>
                                <div class="dropdown-content user-dropdown">
                                    <a href="/dashboard">My Dashboard</a>
                                    <a href="/profile">Profile Settings</a>
                                    <a href="/my-properties">My Properties</a>
                                    <hr class="dropdown-divider">
                                    <a href="#" onclick="logout()">Logout</a>
                                </div>
                            </div>
                        </sec:authorize>
                        
                        <a href="/shortlist" class="nav-link-action">
                            <i class="bi bi-heart"></i> Shortlist
                        </a>
                        
                        <a href="/sell/property" class="btn-post-free">
                            <i class="bi bi-plus"></i> Post Property FREE
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Filter Bar (Red Section) -->
    <div class="filter-navigation">
        <div class="container-fluid px-4">
            <div class="row align-items-center">
                <div class="col-12">
                    <div class="filter-bar">
                        <div class="filter-group">
                            <div class="filter-dropdown">
                                <button class="filter-button" data-bs-toggle="dropdown">
                                    <i class="bi bi-geo-alt"></i>
                                    <span>Location</span>
                                    <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-menu filter-dropdown-menu">
                                    <div class="filter-content">
                                        <h6>Popular Cities</h6>
                                        <a href="/properties?location=Mumbai">Mumbai</a>
                                        <a href="/properties?location=Delhi">Delhi</a>
                                        <a href="/properties?location=Bangalore">Bangalore</a>
                                        <a href="/properties?location=Pune">Pune</a>
                                        <a href="/properties?location=Chennai">Chennai</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="filter-dropdown">
                                <button class="filter-button" data-bs-toggle="dropdown">
                                    <i class="bi bi-currency-rupee"></i>
                                    <span>Budget</span>
                                    <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-menu filter-dropdown-menu">
                                    <div class="filter-content">
                                        <h6>Price Range</h6>
                                        <a href="/properties?budget=0-30">Under ₹30 Lakh</a>
                                        <a href="/properties?budget=30-50">₹30-50 Lakh</a>
                                        <a href="/properties?budget=50-75">₹50-75 Lakh</a>
                                        <a href="/properties?budget=75-100">₹75 Lakh-1 Cr</a>
                                        <a href="/properties?budget=100+">Above ₹1 Cr</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="filter-dropdown">
                                <button class="filter-button" data-bs-toggle="dropdown">
                                    <i class="bi bi-building"></i>
                                    <span>Property Type</span>
                                    <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-menu filter-dropdown-menu">
                                    <div class="filter-content">
                                        <h6>Select Type</h6>
                                        <a href="/properties?type=flat">Flat/Apartment</a>
                                        <a href="/properties?type=house">Independent House</a>
                                        <a href="/properties?type=villa">Builder Villa</a>
                                        <a href="/properties?type=plot">Plot/Land</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="filter-dropdown">
                                <button class="filter-button" data-bs-toggle="dropdown">
                                    <i class="bi bi-door-open"></i>
                                    <span>BHK</span>
                                    <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-menu filter-dropdown-menu">
                                    <div class="filter-content">
                                        <h6>Bedrooms</h6>
                                        <a href="/properties?bhk=1">1 BHK</a>
                                        <a href="/properties?bhk=2">2 BHK</a>
                                        <a href="/properties?bhk=3">3 BHK</a>
                                        <a href="/properties?bhk=4">4+ BHK</a>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="filter-dropdown">
                                <button class="filter-button" data-bs-toggle="dropdown">
                                    <i class="bi bi-person"></i>
                                    <span>Posted By</span>
                                    <i class="bi bi-chevron-down"></i>
                                </button>
                                <div class="dropdown-menu filter-dropdown-menu">
                                    <div class="filter-content">
                                        <h6>Listing Type</h6>
                                        <a href="/properties?postedBy=owner">Owner</a>
                                        <a href="/properties?postedBy=agent">Agent</a>
                                        <a href="/properties?postedBy=builder">Builder</a>
                                    </div>
                                </div>
                            </div>
                            
                            <button class="filter-button more-filters">
                                <i class="bi bi-sliders"></i>
                                <span>More Filters</span>
                            </button>
                        </div>
                        
                        <button class="search-btn">
                            <i class="bi bi-search"></i>
                            Search
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<script>
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/';
}

// Mobile menu toggle
document.addEventListener('DOMContentLoaded', function() {
    const mobileToggle = document.querySelector('.mobile-menu-toggle');
    const mainMenu = document.querySelector('.main-menu');
    
    if (mobileToggle) {
        mobileToggle.addEventListener('click', function() {
            mainMenu.classList.toggle('mobile-active');
        });
    }
});
</script>