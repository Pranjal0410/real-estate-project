<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Horizontal Navigation Bar - Always Horizontal -->
<nav class="navbar navbar-dark sticky-top" id="mainNavbar">
    <div class="container-fluid">
        <!-- Brand Logo -->
        <a class="navbar-brand d-flex align-items-center" href="/">
            <i class="bi bi-house-door-fill me-2 text-primary fs-4"></i>
            <span class="fw-bold">PropInvest</span>
        </a>
        
        <!-- Horizontal Navigation - Always Visible -->
        <div class="d-flex align-items-center justify-content-center flex-grow-1">
            <!-- Main Navigation Links - Horizontal -->
            <div class="d-flex align-items-center gap-3">
                <a class="nav-link-horizontal ${pageContext.request.requestURI eq '/' ? 'active' : ''}" href="/">
                    <i class="bi bi-house me-1"></i>Home
                </a>
                <a class="nav-link-horizontal ${fn:contains(pageContext.request.requestURI, '/properties') ? 'active' : ''}" href="/properties">
                    <i class="bi bi-buildings me-1"></i>Properties
                </a>
                <a class="nav-link-horizontal ${fn:contains(pageContext.request.requestURI, '/calculator') ? 'active' : ''}" href="/calculator">
                    <i class="bi bi-calculator me-1"></i>Calculator
                </a>
                <a class="nav-link-horizontal ${fn:contains(pageContext.request.requestURI, '/chatbot') ? 'active' : ''}" href="/chatbot">
                    <i class="bi bi-robot me-1"></i>AI Assistant
                </a>
                <a class="nav-link-horizontal ${fn:contains(pageContext.request.requestURI, '/about') ? 'active' : ''}" href="/about">
                    <i class="bi bi-info-circle me-1"></i>About
                </a>
                <a class="nav-link-horizontal ${fn:contains(pageContext.request.requestURI, '/contact') ? 'active' : ''}" href="/contact">
                    <i class="bi bi-telephone me-1"></i>Contact
                </a>
            </div>
        </div>
        
        <!-- User Authentication Links - Right Side -->
        <div class="d-flex align-items-center">
            <sec:authorize access="isAuthenticated()">
                <!-- Authenticated User Menu -->
                <div class="dropdown">
                    <a class="nav-link-horizontal dropdown-toggle d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle me-1"></i>
                        <sec:authentication property="name"/>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="/dashboard">
                            <i class="bi bi-speedometer2 me-2"></i>Dashboard
                        </a></li>
                        <li><a class="dropdown-item" href="/profile">
                            <i class="bi bi-person me-2"></i>Profile
                        </a></li>
                        <sec:authorize access="hasRole('ADMIN')">
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="/admin">
                                <i class="bi bi-gear me-2"></i>Admin Panel
                            </a></li>
                        </sec:authorize>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#" onclick="logout()">
                            <i class="bi bi-box-arrow-right me-2"></i>Logout
                        </a></li>
                    </ul>
                </div>
            </sec:authorize>
            
            <sec:authorize access="!isAuthenticated()">
                <!-- Guest User Menu -->
                <div class="d-flex align-items-center gap-2">
                    <a class="nav-link-horizontal" href="/login">
                        <i class="bi bi-box-arrow-in-right me-1"></i>Login
                    </a>
                    <a class="btn btn-nav-primary" href="/register">
                        <i class="bi bi-person-plus me-1"></i>Get Started
                    </a>
                </div>
            </sec:authorize>
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