<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment History - Real Estate Investment Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .history-container {
            margin: 30px auto;
            max-width: 1200px;
        }
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
        }
        .stats-card h3 {
            margin: 0;
        }
        .payment-table {
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .table thead {
            background-color: #f8f9fa;
        }
        .status-badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-success {
            background-color: #d4edda;
            color: #155724;
        }
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        .status-failed {
            background-color: #f8d7da;
            color: #721c24;
        }
        .status-processing {
            background-color: #cce5ff;
            color: #004085;
        }
        .payment-id {
            font-family: monospace;
            font-size: 12px;
            color: #6c757d;
        }
        .pagination {
            margin-top: 20px;
        }
        .empty-state {
            text-align: center;
            padding: 60px 20px;
        }
        .empty-state i {
            font-size: 48px;
            color: #dee2e6;
        }
    </style>
</head>
<body>
    <jsp:include page="../common/navbar.jsp"/>

    <div class="container history-container">
        <h1 class="mb-4"><i class="bi bi-clock-history"></i> Payment History</h1>

        <div class="stats-card">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h3>Total Investment</h3>
                    <h2 class="mb-0">₹<fmt:formatNumber value="${totalInvestment}" type="number" maxFractionDigits="2"/></h2>
                </div>
                <div class="col-md-6 text-end">
                    <button class="btn btn-light" onclick="exportHistory()">
                        <i class="bi bi-download"></i> Export History
                    </button>
                </div>
            </div>
        </div>

        <div class="payment-table">
            <c:choose>
                <c:when test="${not empty payments}">
                    <table class="table table-hover mb-0">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Property</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Transaction ID</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${payments}" var="payment">
                                <tr>
                                    <td>
                                        <fmt:formatDate value="${payment.createdAt}" pattern="dd MMM yyyy"/>
                                        <br>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${payment.createdAt}" pattern="HH:mm:ss"/>
                                        </small>
                                    </td>
                                    <td>
                                        <strong>${payment.property.title}</strong>
                                        <br>
                                        <small class="text-muted">${payment.property.location}</small>
                                    </td>
                                    <td>
                                        <strong>₹<fmt:formatNumber value="${payment.investmentAmount}" type="number" maxFractionDigits="2"/></strong>
                                    </td>
                                    <td>
                                        <span class="status-badge status-${payment.status.toString().toLowerCase()}">
                                            ${payment.status}
                                        </span>
                                    </td>
                                    <td>
                                        <c:if test="${not empty payment.razorpayPaymentId}">
                                            <span class="payment-id">${payment.razorpayPaymentId}</span>
                                        </c:if>
                                        <c:if test="${empty payment.razorpayPaymentId}">
                                            <span class="text-muted">-</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary" onclick="viewDetails(${payment.id})">
                                            <i class="bi bi-eye"></i> View
                                        </button>
                                        <c:if test="${payment.status == 'SUCCESS'}">
                                            <button class="btn btn-sm btn-outline-success" onclick="downloadReceipt(${payment.id})">
                                                <i class="bi bi-receipt"></i> Receipt
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Payment history pagination">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}">Previous</a>
                                </li>
                                <c:forEach begin="0" end="${totalPages - 1}" var="page">
                                    <li class="page-item ${page == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?page=${page}">${page + 1}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <h4 class="mt-3">No Payment History</h4>
                        <p class="text-muted">You haven't made any investments yet.</p>
                        <a href="/properties" class="btn btn-primary">
                            <i class="bi bi-search"></i> Browse Properties
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Payment Details Modal -->
    <div class="modal fade" id="paymentDetailsModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Payment Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body" id="paymentDetailsContent">
                    <!-- Details will be loaded here -->
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function viewDetails(paymentId) {
            $.ajax({
                url: '/api/payment/' + paymentId,
                type: 'GET',
                success: function(payment) {
                    const content = `
                        <div class="row">
                            <div class="col-md-6">
                                <h6>Transaction Information</h6>
                                <p><strong>Payment ID:</strong> \${payment.id}</p>
                                <p><strong>Razorpay Order ID:</strong> \${payment.razorpayOrderId || '-'}</p>
                                <p><strong>Razorpay Payment ID:</strong> \${payment.razorpayPaymentId || '-'}</p>
                                <p><strong>Status:</strong> \${payment.status}</p>
                                <p><strong>Created At:</strong> \${new Date(payment.createdAt).toLocaleString()}</p>
                                <p><strong>Paid At:</strong> \${payment.paidAt ? new Date(payment.paidAt).toLocaleString() : '-'}</p>
                            </div>
                            <div class="col-md-6">
                                <h6>Investment Details</h6>
                                <p><strong>Property:</strong> \${payment.property.title}</p>
                                <p><strong>Location:</strong> \${payment.property.location}</p>
                                <p><strong>Amount:</strong> ₹\${payment.investmentAmount}</p>
                                <p><strong>Currency:</strong> \${payment.currency}</p>
                                <p><strong>Email Sent:</strong> \${payment.emailSent ? 'Yes' : 'No'}</p>
                            </div>
                        </div>
                        \${payment.failureReason ? '<div class="alert alert-danger mt-3"><strong>Failure Reason:</strong> ' + payment.failureReason + '</div>' : ''}
                        \${payment.notes ? '<div class="mt-3"><strong>Notes:</strong> ' + payment.notes + '</div>' : ''}
                    `;
                    $('#paymentDetailsContent').html(content);
                    $('#paymentDetailsModal').modal('show');
                },
                error: function() {
                    alert('Failed to load payment details');
                }
            });
        }

        function downloadReceipt(paymentId) {
            window.open('/investor/payment/receipt/' + paymentId, '_blank');
        }

        function exportHistory() {
            window.location.href = '/investor/payment/export';
        }
    </script>
</body>
</html>