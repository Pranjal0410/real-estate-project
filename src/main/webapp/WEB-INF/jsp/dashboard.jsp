<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="PropInvest Dashboard - Your real estate investment control center">
    <title>Dashboard - PropInvest</title>

    <!-- Bootstrap 5 CSS from CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">

    <!-- Google Fonts for Shadcn-style typography -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    <link href="/css/components.css" rel="stylesheet">

    <style>
        body {
            background: var(--background);
            min-height: 100vh;
        }

        .dashboard-container {
            padding: var(--space-6);
        }

        .welcome-section {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: var(--radius-2xl);
            padding: var(--space-8);
            margin-bottom: var(--space-6);
            box-shadow: var(--shadow-lg);
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: var(--space-6);
            margin-bottom: var(--space-8);
        }

        .stat-card {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: var(--radius-xl);
            padding: var(--space-6);
            box-shadow: var(--shadow-md);
            text-align: center;
        }

        .stat-value {
            font-size: var(--text-3xl);
            font-weight: 800;
            color: var(--primary);
            margin-bottom: var(--space-2);
        }

        .stat-label {
            color: var(--muted-foreground);
            font-size: var(--text-sm);
            text-transform: uppercase;
            letter-spacing: 0.1em;
        }

        .quick-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: var(--space-4);
            margin-top: var(--space-6);
        }

        .action-card {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            padding: var(--space-4);
            text-decoration: none;
            color: var(--foreground);
            transition: all var(--duration-200) ease;
            display: flex;
            align-items: center;
            gap: var(--space-3);
        }

        .action-card:hover {
            background: var(--accent);
            border-color: var(--primary);
            color: var(--foreground);
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg);
        }

        .action-icon {
            font-size: var(--text-xl);
            color: var(--primary);
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(var(--primary-rgb), 0.1);
            border-radius: var(--radius-md);
        }

        .properties-section {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: var(--radius-xl);
            padding: var(--space-6);
            box-shadow: var(--shadow-md);
        }

        .property-item {
            padding: var(--space-4);
            border-bottom: 1px solid var(--border);
            display: flex;
            justify-content: between;
            align-items: center;
        }

        .property-item:last-child {
            border-bottom: none;
        }
    </style>
</head>
<body>
    <jsp:include page="includes/navbar-real.jsp"/>

    <div class="dashboard-container">
        <!-- Welcome Section -->
        <div class="welcome-section animate-fadeInScale">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="h2 mb-2" style="color: var(--foreground); font-weight: 700;">
                        Welcome back<c:if test="${not empty user}">, ${user.firstName}</c:if>!
                    </h1>
                    <p class="text-muted-foreground mb-0">
                        Here's what's happening with your real estate investments today.
                    </p>
                </div>
                <div class="col-md-4 text-md-end">
                    <div class="d-flex align-items-center justify-content-md-end gap-3">
                        <i class="bi bi-house-heart" style="font-size: var(--text-3xl); color: var(--primary);"></i>
                        <div>
                            <div style="color: var(--muted-foreground); font-size: var(--text-sm);">
                                <c:choose>
                                    <c:when test="${not empty user}">
                                        Role: ${user.role}
                                    </c:when>
                                    <c:otherwise>
                                        Welcome to PropInvest
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Stats Grid -->
        <div class="stats-grid">
            <div class="stat-card animate-fadeInScale">
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty properties}">
                            ${properties.size()}
                        </c:when>
                        <c:otherwise>
                            0
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Total Properties</div>
            </div>

            <div class="stat-card animate-fadeInScale">
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty stats and not empty stats['House']}">
                            ${stats['House']}
                        </c:when>
                        <c:otherwise>
                            0
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Houses</div>
            </div>

            <div class="stat-card animate-fadeInScale">
                <div class="stat-value">
                    <c:choose>
                        <c:when test="${not empty stats and not empty stats['Apartment']}">
                            ${stats['Apartment']}
                        </c:when>
                        <c:otherwise>
                            0
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-label">Apartments</div>
            </div>

            <div class="stat-card animate-fadeInScale">
                <div class="stat-value">$2.4M</div>
                <div class="stat-label">Portfolio Value</div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="row">
            <div class="col-md-8">
                <div class="properties-section">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h3 style="color: var(--foreground); font-weight: 600;">Recent Properties</h3>
                        <a href="/properties" class="btn btn-outline">View All</a>
                    </div>

                    <c:choose>
                        <c:when test="${not empty properties}">
                            <c:forEach items="${properties}" var="property" begin="0" end="4">
                                <div class="property-item">
                                    <div class="d-flex align-items-center gap-3">
                                        <div class="action-icon">
                                            <i class="bi bi-house"></i>
                                        </div>
                                        <div>
                                            <h6 class="mb-1" style="color: var(--foreground);">${property.title}</h6>
                                            <small style="color: var(--muted-foreground);">${property.location}</small>
                                        </div>
                                    </div>
                                    <div class="text-end">
                                        <div style="color: var(--primary); font-weight: 600;">
                                            $<fmt:formatNumber value="${property.price}" pattern="#,###" />
                                        </div>
                                        <small style="color: var(--muted-foreground);">${property.status}</small>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5">
                                <i class="bi bi-house" style="font-size: var(--text-4xl); color: var(--muted-foreground); opacity: 0.5;"></i>
                                <p style="color: var(--muted-foreground); margin-top: var(--space-3);">
                                    No properties found. <a href="/properties" class="text-primary">Browse available properties</a>
                                </p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="col-md-4">
                <div class="properties-section">
                    <h3 style="color: var(--foreground); font-weight: 600;" class="mb-4">Quick Actions</h3>

                    <div class="quick-actions">
                        <a href="/calculator" class="action-card">
                            <div class="action-icon">
                                <i class="bi bi-calculator"></i>
                            </div>
                            <div>
                                <strong>Calculator</strong>
                                <div style="font-size: var(--text-sm); color: var(--muted-foreground);">
                                    Investment metrics
                                </div>
                            </div>
                        </a>

                        <a href="/properties" class="action-card">
                            <div class="action-icon">
                                <i class="bi bi-search"></i>
                            </div>
                            <div>
                                <strong>Browse</strong>
                                <div style="font-size: var(--text-sm); color: var(--muted-foreground);">
                                    Find properties
                                </div>
                            </div>
                        </a>

                        <a href="/chatbot" class="action-card">
                            <div class="action-icon">
                                <i class="bi bi-chat-dots"></i>
                            </div>
                            <div>
                                <strong>AI Assistant</strong>
                                <div style="font-size: var(--text-sm); color: var(--muted-foreground);">
                                    Get help
                                </div>
                            </div>
                        </div>

                        <a href="/profile" class="action-card">
                            <div class="action-icon">
                                <i class="bi bi-person"></i>
                            </div>
                            <div>
                                <strong>Profile</strong>
                                <div style="font-size: var(--text-sm); color: var(--muted-foreground);">
                                    Manage account
                                </div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="includes/footer.jsp"/>

    <!-- Scripts from CDN -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

    <script>
        $(document).ready(function() {
            // Add animation delays to stat cards
            $('.stat-card').each(function(index) {
                $(this).css('animation-delay', (index * 100) + 'ms');
            });

            // Add smooth hover effects
            $('.action-card').hover(
                function() {
                    $(this).find('.action-icon').css('transform', 'scale(1.1)');
                },
                function() {
                    $(this).find('.action-icon').css('transform', 'scale(1)');
                }
            );
        });
    </script>
</body>
</html>