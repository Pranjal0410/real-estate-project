<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Investment Calculator - Real Estate Platform</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">
    <link href="/css/global.css" rel="stylesheet">
    <link href="/css/magicbricks-exact.css" rel="stylesheet">
    <link href="https://unpkg.com/aos@2.3.4/dist/aos.css" rel="stylesheet">
</head>
<body>
    <div id="scrollProgress"></div>
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <div class="container mt-5" data-aos="fade-up">
        <h1 class="text-center mb-4">Investment Calculator</h1>
        
        <div class="row">
            <div class="col-md-6" data-aos="fade-right">
                <div class="card shadow" data-tilt data-tilt-max="6" data-tilt-speed="400">
                    <div class="card-header bg-primary text-white">
                        <h4>Property Details</h4>
                    </div>
                    <div class="card-body">
                        <form id="calculatorForm">
                            <div class="mb-3">
                                <label for="propertyPrice" class="form-label">Property Price ($)</label>
                                <input type="number" class="form-control" id="propertyPrice" required min="0" step="1000">
                            </div>
                            
                            <div class="mb-3">
                                <label for="downPayment" class="form-label">Down Payment (%)</label>
                                <input type="number" class="form-control" id="downPayment" value="20" min="0" max="100">
                            </div>
                            
                            <div class="mb-3">
                                <label for="loanRate" class="form-label">Interest Rate (%)</label>
                                <input type="number" class="form-control" id="loanRate" value="4.5" min="0" max="20" step="0.1">
                            </div>
                            
                            <div class="mb-3">
                                <label for="loanTerm" class="form-label">Loan Term (years)</label>
                                <input type="number" class="form-control" id="loanTerm" value="30" min="1" max="40">
                            </div>
                            
                            <div class="mb-3">
                                <label for="monthlyRental" class="form-label">Expected Monthly Rental ($)</label>
                                <input type="number" class="form-control" id="monthlyRental" required min="0">
                            </div>
                            
                            <div class="mb-3">
                                <label for="annualExpenses" class="form-label">Annual Expenses ($)</label>
                                <input type="number" class="form-control" id="annualExpenses" min="0">
                                <small class="text-muted">Property tax, insurance, maintenance, etc.</small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="appreciationRate" class="form-label">Expected Appreciation Rate (%)</label>
                                <input type="number" class="form-control" id="appreciationRate" value="3" min="0" max="20" step="0.1">
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100">Calculate Investment Metrics</button>
                        </form>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6" data-aos="fade-left">
                <div class="card shadow" data-tilt data-tilt-max="6" data-tilt-speed="400">
                    <div class="card-header bg-success text-white">
                        <h4>Investment Analysis</h4>
                    </div>
                    <div class="card-body" id="resultsSection" style="display: none;">
                        <div class="mb-3">
                            <h5>Mortgage Details</h5>
                            <p class="mb-1">Loan Amount: $<span id="loanAmount">0</span></p>
                            <p class="mb-1">Monthly Payment: $<span id="monthlyPayment">0</span></p>
                        </div>
                        
                        <hr>
                        
                        <div class="mb-3">
                            <h5>Investment Metrics</h5>
                            <p class="mb-1">ROI: <span id="roi" class="fw-bold">0</span>%</p>
                            <p class="mb-1">Rental Yield: <span id="rentalYield" class="fw-bold">0</span>%</p>
                            <p class="mb-1">Cap Rate: <span id="capRate" class="fw-bold">0</span>%</p>
                            <p class="mb-1">Monthly Cash Flow: $<span id="cashFlow" class="fw-bold">0</span></p>
                        </div>
                        
                        <hr>
                        
                        <div class="mb-3">
                            <h5>Long-term Projections</h5>
                            <p class="mb-1">Break-even Point: <span id="breakEven">0</span> years</p>
                            <p class="mb-1">Property Value (5 years): $<span id="futureValue">0</span></p>
                        </div>
                        
                        <div class="alert alert-info mt-3" id="recommendation">
                            <strong>Recommendation:</strong> <span id="recommendationText"></span>
                        </div>
                    </div>
                    
                    <div class="card-body" id="emptyResults">
                        <p class="text-muted text-center">Fill in the property details and click Calculate to see investment metrics</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row mt-4" data-aos="fade-up">
            <div class="col-12">
                <div class="card shadow" data-tilt data-tilt-max="4" data-tilt-speed="400">
                    <div class="card-body">
                        <h5>Investment Calculator Guide</h5>
                        <ul>
                            <li><strong>ROI:</strong> Return on Investment - Annual net income as percentage of initial investment</li>
                            <li><strong>Rental Yield:</strong> Annual rental income as percentage of property price</li>
                            <li><strong>Cap Rate:</strong> Net Operating Income divided by property value</li>
                            <li><strong>Cash Flow:</strong> Monthly rental income minus all monthly expenses</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="includes/footer.jsp"/>
    <button id="backToTop" aria-label="Back to top" title="Back to top"><i class="bi bi-arrow-up"></i></button>
    
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vanilla-tilt@1.8.1/dist/vanilla-tilt.min.js"></script>
    <script>
    AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
    if (window.VanillaTilt) { VanillaTilt.init(document.querySelectorAll('[data-tilt]')); }
    (function(){
        const bar = document.getElementById('scrollProgress');
        const btn = document.getElementById('backToTop');
        const update = () => {
            const scrollTop = window.scrollY;
            const docHeight = document.documentElement.scrollHeight - window.innerHeight;
            const width = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0;
            if (bar) bar.style.width = width + '%';
            if (btn) btn.classList.toggle('show', scrollTop > 400);
        };
        window.addEventListener('scroll', update);
        window.addEventListener('resize', update);
        if (btn) btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
        update();
    })();
    $(document).ready(function() {
        $('#calculatorForm').on('submit', function(e) {
            e.preventDefault();
            
            // Clear previous errors
            $('.form-control').removeClass('is-invalid');
            $('.invalid-feedback').remove();
            
            // Get form values
            const propertyPrice = parseFloat($('#propertyPrice').val());
            const downPayment = parseFloat($('#downPayment').val()) / 100;
            const loanRate = parseFloat($('#loanRate').val());
            const loanTerm = parseInt($('#loanTerm').val());
            const monthlyRental = parseFloat($('#monthlyRental').val());
            const annualExpenses = parseFloat($('#annualExpenses').val()) || 0;
            const appreciationRate = parseFloat($('#appreciationRate').val());
            
            // Validate inputs
            let hasErrors = false;
            
            if (!propertyPrice || propertyPrice <= 0) {
                showFieldError('#propertyPrice', 'Property price must be greater than 0');
                hasErrors = true;
            }
            
            if (!monthlyRental || monthlyRental <= 0) {
                showFieldError('#monthlyRental', 'Monthly rental must be greater than 0');
                hasErrors = true;
            }
            
            if (downPayment < 0 || downPayment > 1) {
                showFieldError('#downPayment', 'Down payment must be between 0 and 100%');
                hasErrors = true;
            }
            
            if (hasErrors) return;
            
            // Show loading state
            const submitBtn = $(this).find('button[type="submit"]');
            const originalText = submitBtn.html();
            submitBtn.addClass('loading').prop('disabled', true);
            
            const loanAmount = propertyPrice * (1 - downPayment);
            
            // Use the calculate endpoint
            const requestData = {
                propertyPrice: propertyPrice,
                downPaymentPercentage: downPayment * 100,
                interestRate: loanRate,
                loanTermYears: loanTerm,
                monthlyRental: monthlyRental,
                annualExpenses: annualExpenses,
                appreciationRate: appreciationRate
            };
            
            // Calculate using individual endpoints for compatibility
            const monthlyExpenses = annualExpenses / 12;
            
            // Calculate mortgage payment first
            $.get('/api/investments/mortgage', {
                loanAmount: loanAmount,
                annualRate: loanRate,
                months: loanTerm * 12
            }).then(function(mortgageResponse) {
                const monthlyPayment = mortgageResponse.data;
                
                // Calculate all metrics in parallel
                const promises = [
                    $.get('/api/investments/roi', {
                        propertyPrice: propertyPrice,
                        monthlyRental: monthlyRental,
                        annualExpenses: annualExpenses
                    }),
                    $.get('/api/investments/rental-yield', {
                        propertyPrice: propertyPrice,
                        monthlyRental: monthlyRental
                    }),
                    $.get('/api/investments/cap-rate', {
                        propertyPrice: propertyPrice,
                        monthlyRental: monthlyRental,
                        annualExpenses: annualExpenses
                    }),
                    $.get('/api/investments/break-even', {
                        propertyPrice: propertyPrice,
                        monthlyRental: monthlyRental,
                        annualExpenses: annualExpenses
                    }),
                    $.get('/api/investments/appreciation', {
                        initialValue: propertyPrice,
                        appreciationRate: appreciationRate,
                        years: 5
                    })
                ];
                
                Promise.all(promises).then(function(responses) {
                    const [roiResp, yieldResp, capRateResp, breakEvenResp, appreciationResp] = responses;
                    
                    // Calculate cash flow
                    const cashFlow = monthlyRental - monthlyPayment - monthlyExpenses;
                    
                    // Display results
                    $('#loanAmount').text(formatCurrency(loanAmount));
                    $('#monthlyPayment').text(formatCurrency(monthlyPayment));
                    $('#roi').text(roiResp.data.toFixed(2));
                    $('#rentalYield').text(yieldResp.data.toFixed(2));
                    $('#capRate').text(capRateResp.data.toFixed(2));
                    $('#cashFlow').text(formatCurrency(cashFlow));
                    $('#breakEven').text(breakEvenResp.data.toFixed(1));
                    $('#futureValue').text(formatCurrency(appreciationResp.data));
                    
                    // Generate recommendation
                    let recommendation = "";
                    const roi = roiResp.data;
                    
                    if (roi > 8 && cashFlow > 0) {
                        recommendation = "Excellent investment opportunity with strong ROI and positive cash flow!";
                        $('#recommendation').removeClass('alert-warning alert-danger').addClass('alert-success');
                    } else if (roi > 5 && cashFlow >= 0) {
                        recommendation = "Good investment with decent returns and break-even or positive cash flow.";
                        $('#recommendation').removeClass('alert-danger alert-success').addClass('alert-info');
                    } else if (cashFlow < 0) {
                        recommendation = "Warning: Negative cash flow. Consider negotiating price or increasing rental income.";
                        $('#recommendation').removeClass('alert-success alert-info').addClass('alert-warning');
                    } else {
                        recommendation = "Low returns. This may not be an ideal investment opportunity.";
                        $('#recommendation').removeClass('alert-success alert-info').addClass('alert-danger');
                    }
                    $('#recommendationText').text(recommendation);
                    
                    $('#emptyResults').hide();
                    $('#resultsSection').show();
                    
                }).catch(function(error) {
                    console.error('Calculation error:', error);
                    showAlert('Some calculations failed. Please try again.', 'warning');
                });
                
            }).catch(function(error) {
                console.error('Mortgage calculation failed:', error);
                showAlert('Calculation failed. Please check your inputs and try again.', 'danger');
            }).always(function() {
                // Reset button
                submitBtn.removeClass('loading').prop('disabled', false).html(originalText);
            });
        });
        
        // Helper functions
        function formatCurrency(amount) {
            return new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
                minimumFractionDigits: 0,
                maximumFractionDigits: 0
            }).format(amount);
        }
        
        function showFieldError(fieldSelector, message) {
            const field = $(fieldSelector);
            field.addClass('is-invalid');
            if (field.siblings('.invalid-feedback').length === 0) {
                field.after(`<div class="invalid-feedback">${message}</div>`);
            }
        }
        
        function showAlert(message, type) {
            const alertHtml = `
                <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            `;
            $('#resultsSection').prepend(alertHtml);
        }
        
        // Clear errors on input
        $('.form-control').on('input', function() {
            $(this).removeClass('is-invalid');
            $(this).siblings('.invalid-feedback').remove();
        });
    });
    </script>
</body>
</html>