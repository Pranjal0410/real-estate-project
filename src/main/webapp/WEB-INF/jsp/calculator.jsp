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
    <link href="https://unpkg.com/aos@2.3.4/dist/aos.css" rel="stylesheet">
</head>
<body>
    <div id="scrollProgress"></div>
    <jsp:include page="includes/navbar.jsp"/>
    
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
            
            const propertyPrice = parseFloat($('#propertyPrice').val());
            const downPayment = parseFloat($('#downPayment').val()) / 100;
            const loanRate = parseFloat($('#loanRate').val());
            const loanTerm = parseInt($('#loanTerm').val());
            const monthlyRental = parseFloat($('#monthlyRental').val());
            const annualExpenses = parseFloat($('#annualExpenses').val()) || 0;
            const appreciationRate = parseFloat($('#appreciationRate').val());
            
            const loanAmount = propertyPrice * (1 - downPayment);
            
            // Calculate mortgage payment
            $.get('/api/investments/mortgage', {
                loanAmount: loanAmount,
                annualRate: loanRate,
                months: loanTerm * 12
            }).done(function(mortgageResponse) {
                const monthlyPayment = mortgageResponse.data;
                
                // Calculate ROI
                $.get('/api/investments/roi', {
                    propertyPrice: propertyPrice,
                    monthlyRental: monthlyRental,
                    annualExpenses: annualExpenses
                }).done(function(roiResponse) {
                    const roi = roiResponse.data;
                    
                    // Calculate Rental Yield
                    $.get('/api/investments/rental-yield', {
                        propertyPrice: propertyPrice,
                        monthlyRental: monthlyRental
                    }).done(function(yieldResponse) {
                        const rentalYield = yieldResponse.data;
                        
                        // Calculate Cap Rate
                        $.get('/api/investments/cap-rate', {
                            propertyPrice: propertyPrice,
                            monthlyRental: monthlyRental,
                            annualExpenses: annualExpenses
                        }).done(function(capRateResponse) {
                            const capRate = capRateResponse.data;
                            
                            // Calculate Cash Flow
                            const monthlyExpenses = annualExpenses / 12;
                            const cashFlow = monthlyRental - monthlyPayment - monthlyExpenses;
                            
                            // Calculate Break Even
                            $.get('/api/investments/break-even', {
                                propertyPrice: propertyPrice,
                                monthlyRental: monthlyRental,
                                annualExpenses: annualExpenses
                            }).done(function(breakEvenResponse) {
                                const breakEven = breakEvenResponse.data;
                                
                                // Calculate Appreciation
                                $.get('/api/investments/appreciation', {
                                    initialValue: propertyPrice,
                                    appreciationRate: appreciationRate,
                                    years: 5
                                }).done(function(appreciationResponse) {
                                    const futureValue = appreciationResponse.data;
                                    
                                    // Display results
                                    $('#loanAmount').text(loanAmount.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                                    $('#monthlyPayment').text(monthlyPayment.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                                    $('#roi').text(roi.toFixed(2));
                                    $('#rentalYield').text(rentalYield.toFixed(2));
                                    $('#capRate').text(capRate.toFixed(2));
                                    $('#cashFlow').text(cashFlow.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                                    $('#breakEven').text(breakEven.toFixed(1));
                                    $('#futureValue').text(futureValue.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
                                    
                                    // Generate recommendation
                                    let recommendation = "";
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
                                });
                            });
                        });
                    });
                });
            });
        });
    });
    </script>
</body>
</html>