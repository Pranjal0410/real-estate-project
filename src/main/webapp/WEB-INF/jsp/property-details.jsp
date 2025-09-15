<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Property Details - PropInvest</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">

    <link href="/css/shadcn-enhanced.css" rel="stylesheet">
    <link href="/css/components.css" rel="stylesheet">
    <link href="/css/navbar.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="includes/navbar-real.jsp"/>

    <div class="container mt-5 pt-5">
        <div class="row">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-body">
                        <h1 class="card-title">${property.title}</h1>
                        <p class="text-muted mb-3">
                            <i class="bi bi-geo-alt"></i> ${property.location}
                        </p>

                        <div class="row mb-4">
                            <div class="col-md-6">
                                <img src="https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&h=400&q=80"
                                     alt="${property.title}" class="img-fluid rounded">
                            </div>
                            <div class="col-md-6">
                                <div class="property-details">
                                    <h3 class="text-primary">₹<fmt:formatNumber value="${property.price / 100000}" pattern="##.##"/> Lakh</h3>

                                    <div class="row mt-3">
                                        <div class="col-6">
                                            <div class="text-center p-3 bg-light rounded">
                                                <i class="bi bi-door-open fs-4 text-primary"></i>
                                                <div class="fw-bold">${property.bedrooms} BHK</div>
                                            </div>
                                        </div>
                                        <div class="col-6">
                                            <div class="text-center p-3 bg-light rounded">
                                                <i class="bi bi-droplet fs-4 text-primary"></i>
                                                <div class="fw-bold">${property.bathrooms} Bath</div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row mt-2">
                                        <div class="col-6">
                                            <div class="text-center p-3 bg-light rounded">
                                                <i class="bi bi-rulers fs-4 text-primary"></i>
                                                <div class="fw-bold"><fmt:formatNumber value="${property.area}" pattern=",###"/> sq ft</div>
                                            </div>
                                        </div>
                                        <div class="col-6">
                                            <div class="text-center p-3 bg-light rounded">
                                                <i class="bi bi-building fs-4 text-primary"></i>
                                                <div class="fw-bold">${property.propertyType}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="description">
                            <h4>Description</h4>
                            <p>${property.description}</p>
                        </div>

                        <div class="amenities mt-4">
                            <h4>Amenities</h4>
                            <div class="row">
                                <div class="col-md-6">
                                    <ul class="list-unstyled">
                                        <li><i class="bi bi-check-circle text-success me-2"></i>24/7 Security</li>
                                        <li><i class="bi bi-check-circle text-success me-2"></i>Power Backup</li>
                                        <li><i class="bi bi-check-circle text-success me-2"></i>Lift</li>
                                    </ul>
                                </div>
                                <div class="col-md-6">
                                    <ul class="list-unstyled">
                                        <li><i class="bi bi-check-circle text-success me-2"></i>Parking</li>
                                        <li><i class="bi bi-check-circle text-success me-2"></i>Club House</li>
                                        <li><i class="bi bi-check-circle text-success me-2"></i>Swimming Pool</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card">
                    <div class="card-body">
                        <h5>Contact Owner</h5>
                        <form>
                            <div class="mb-3">
                                <label for="name" class="form-label">Your Name</label>
                                <input type="text" class="form-control" id="name" required>
                            </div>
                            <div class="mb-3">
                                <label for="phone" class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" id="phone" required>
                            </div>
                            <div class="mb-3">
                                <label for="message" class="form-label">Message</label>
                                <textarea class="form-control" id="message" rows="3">I am interested in this property. Please contact me.</textarea>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-telephone me-2"></i>Contact Owner
                            </button>
                        </form>

                        <hr>

                        <div class="d-grid gap-2">
                            <button class="btn btn-outline-primary">
                                <i class="bi bi-heart me-2"></i>Add to Wishlist
                            </button>
                            <button class="btn btn-outline-success">
                                <i class="bi bi-calculator me-2"></i>Calculate EMI
                            </button>
                        </div>
                    </div>
                </div>

                <div class="card mt-4">
                    <div class="card-body">
                        <h5>Property Features</h5>
                        <ul class="list-unstyled">
                            <li class="mb-2">
                                <strong>Property Type:</strong> ${property.propertyType}
                            </li>
                            <li class="mb-2">
                                <strong>Year Built:</strong> ${property.yearBuilt}
                            </li>
                            <li class="mb-2">
                                <strong>Status:</strong> ${property.status}
                            </li>
                            <li class="mb-2">
                                <strong>Price per sq ft:</strong> ₹<fmt:formatNumber value="${property.price / property.area}" pattern=",###"/>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="includes/footer.jsp"/>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>