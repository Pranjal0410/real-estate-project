<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Modern Real Estate Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white position-sticky top-0 w-100 shadow-sm" id="mainNavbar">
    <div class="container-fluid">
        <!-- Brand Logo -->
        <a class="navbar-brand d-flex align-items-center" href="/" aria-label="PropInvest Home">
            <i class="bi bi-house-door-fill me-2 fs-4 text-danger" aria-hidden="true"></i>
            <span class="fw-bold text-danger">PropInvest</span>
        </a>
        
        <!-- Mobile Menu Toggle -->
        <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse" 
                data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" 
                aria-label="Toggle navigation menu">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <!-- Navigation Menu -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Main Navigation Links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${pageContext.request.requestURI eq '/' ? 'active' : ''}" href="#" id="homeDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-house me-1" aria-hidden="true"></i>Home
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="homeDropdown">
                        <li><a class="dropdown-item" href="/"><i class="bi bi-house me-2"></i>Dashboard</a></li>
                        <li><a class="dropdown-item" href="/#features"><i class="bi bi-star me-2"></i>Features</a></li>
                        <li><a class="dropdown-item" href="/#testimonials"><i class="bi bi-quote me-2"></i>Testimonials</a></li>
                    </ul>
                </li>
                
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${fn:contains(pageContext.request.requestURI, '/properties') ? 'active' : ''}" href="#" id="propertiesDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-buildings me-1" aria-hidden="true"></i>Properties
                    </a>
                    <ul class="dropdown-menu mega-dropdown" aria-labelledby="propertiesDropdown">
                        <div class="row">
                            <div class="col-md-3">
                                <h6 class="dropdown-header">Property Types</h6>
                                <li><a class="dropdown-item" href="/properties?type=APARTMENT"><i class="bi bi-building me-2"></i>Apartments</a></li>
                                <li><a class="dropdown-item" href="/properties?type=HOUSE"><i class="bi bi-house me-2"></i>Houses</a></li>
                                <li><a class="dropdown-item" href="/properties?type=VILLA"><i class="bi bi-house-heart me-2"></i>Villas</a></li>
                                <li><a class="dropdown-item" href="/properties?type=CONDO"><i class="bi bi-building-add me-2"></i>Condos</a></li>
                            </div>
                            <div class="col-md-3">
                                <h6 class="dropdown-header">Popular Locations</h6>
                                <li><a class="dropdown-item" href="/properties?location=Manhattan"><i class="bi bi-geo-alt me-2"></i>Manhattan, NY</a></li>
                                <li><a class="dropdown-item" href="/properties?location=Beverly Hills"><i class="bi bi-geo-alt me-2"></i>Beverly Hills, CA</a></li>
                                <li><a class="dropdown-item" href="/properties?location=Austin"><i class="bi bi-geo-alt me-2"></i>Austin, TX</a></li>
                                <li><a class="dropdown-item" href="/properties?location=Miami"><i class="bi bi-geo-alt me-2"></i>Miami, FL</a></li>
                            </div>
                            <div class="col-md-3">
                                <h6 class="dropdown-header">Budget Range</h6>
                                <li><a class="dropdown-item" href="/properties?maxPrice=500000"><i class="bi bi-currency-dollar me-2"></i>Under $500K</a></li>
                                <li><a class="dropdown-item" href="/properties?minPrice=500000&maxPrice=1000000"><i class="bi bi-currency-dollar me-2"></i>$500K - $1M</a></li>
                                <li><a class="dropdown-item" href="/properties?minPrice=1000000&maxPrice=2000000"><i class="bi bi-currency-dollar me-2"></i>$1M - $2M</a></li>
                                <li><a class="dropdown-item" href="/properties?minPrice=2000000"><i class="bi bi-currency-dollar me-2"></i>Above $2M</a></li>
                            </div>
                            <div class="col-md-3">
                                <h6 class="dropdown-header">Quick Actions</h6>
                                <li><a class="dropdown-item" href="/properties?featured=true"><i class="bi bi-star-fill me-2"></i>Featured Properties</a></li>
                                <li><a class="dropdown-item" href="/properties?new=true"><i class="bi bi-plus-circle me-2"></i>New Listings</a></li>
                                <li><a class="dropdown-item" href="/properties?verified=true"><i class="bi bi-shield-check me-2"></i>Verified Properties</a></li>
                                <li><a class="dropdown-item" href="/properties"><i class="bi bi-search me-2"></i>Advanced Search</a></li>
                            </div>
                        </div>
                    </ul>
                </li>
                
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${fn:contains(pageContext.request.requestURI, '/calculator') ? 'active' : ''}" href="#" id="calculatorDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-calculator me-1" aria-hidden="true"></i>Calculator
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="calculatorDropdown">
                        <li><a class="dropdown-item" href="/calculator"><i class="bi bi-calculator me-2"></i>Investment Calculator</a></li>
                        <li><a class="dropdown-item" href="/calculator?type=mortgage"><i class="bi bi-bank me-2"></i>Mortgage Calculator</a></li>
                        <li><a class="dropdown-item" href="/calculator?type=roi"><i class="bi bi-graph-up me-2"></i>ROI Calculator</a></li>
                        <li><a class="dropdown-item" href="/calculator?type=rental"><i class="bi bi-house-gear me-2"></i>Rental Yield</a></li>
                    </ul>
                </li>
                
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${fn:contains(pageContext.request.requestURI, '/chatbot') ? 'active' : ''}" href="#" id="aiDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-robot me-1" aria-hidden="true"></i>AI Assistant
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="aiDropdown">
                        <li><a class="dropdown-item" href="/chatbot"><i class="bi bi-chat-dots me-2"></i>Chat with AI</a></li>
                        <li><a class="dropdown-item" href="/chatbot?mode=recommendations"><i class="bi bi-lightbulb me-2"></i>Get Recommendations</a></li>
                        <li><a class="dropdown-item" href="/chatbot?mode=analysis"><i class="bi bi-graph-up-arrow me-2"></i>Market Analysis</a></li>
                        <li><a class="dropdown-item" href="/chatbot?mode=faq"><i class="bi bi-question-circle me-2"></i>FAQ</a></li>
                    </ul>
                </li>
                
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${fn:contains(pageContext.request.requestURI, '/about') ? 'active' : ''}" href="#" id="aboutDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-info-circle me-1" aria-hidden="true"></i>About
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="aboutDropdown">
                        <li><a class="dropdown-item" href="/about"><i class="bi bi-info-circle me-2"></i>About Us</a></li>
                        <li><a class="dropdown-item" href="/about#team"><i class="bi bi-people me-2"></i>Our Team</a></li>
                        <li><a class="dropdown-item" href="/about#careers"><i class="bi bi-briefcase me-2"></i>Careers</a></li>
                        <li><a class="dropdown-item" href="/about#press"><i class="bi bi-newspaper me-2"></i>Press</a></li>
                    </ul>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link ${fn:contains(pageContext.request.requestURI, '/contact') ? 'active' : ''}" href="/contact">
                        <i class="bi bi-telephone me-1" aria-hidden="true"></i>Contact
                    </a>
                </li>
            </ul>
            
            <!-- User Authentication Links -->
            <ul class="navbar-nav">
                <sec:authorize access="isAuthenticated()">
                    <!-- Authenticated User Menu -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" 
                           role="button" data-bs-toggle="dropdown" aria-expanded="false" aria-label="User menu">
                            <i class="bi bi-person-circle me-1" aria-hidden="true"></i>
                            <sec:authentication property="name"/>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end shadow-sm" aria-labelledby="userDropdown">
                            <li><a class="dropdown-item" href="/dashboard">
                                <i class="bi bi-speedometer2 me-2" aria-hidden="true"></i>Dashboard
                            </a></li>
                            <li><a class="dropdown-item" href="/profile">
                                <i class="bi bi-person me-2" aria-hidden="true"></i>Profile
                            </a></li>
                            <sec:authorize access="hasRole('ADMIN')">
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="/admin">
                                    <i class="bi bi-gear me-2" aria-hidden="true"></i>Admin Panel
                                </a></li>
                            </sec:authorize>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="#" onclick="logout()" role="button">
                                <i class="bi bi-box-arrow-right me-2" aria-hidden="true"></i>Logout
                            </a></li>
                        </ul>
                    </li>
                </sec:authorize>
                
                <sec:authorize access="!isAuthenticated()">
                    <!-- Guest User Menu -->
                    <li class="nav-item me-2">
                        <a class="nav-link" href="/login">
                            <i class="bi bi-box-arrow-in-right me-1" aria-hidden="true"></i>Login
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-primary rounded-pill px-3" href="/register">
                            <i class="bi bi-person-plus me-1" aria-hidden="true"></i>Get Started
                        </a>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>

<script>
function logout() {
    fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    }).then(() => {
        localStorage.removeItem('token');
        window.location.href = '/';
    });
}
</script>