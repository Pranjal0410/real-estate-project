<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EMI Calculator - PropInvest</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary: #8b5cf6;
            --primary-dark: #7c3aed;
            --primary-light: #a78bfa;
            --secondary: #10b981;
            --background: #0f0f23;
            --card-bg: rgba(30, 30, 50, 0.8);
            --text-primary: #ffffff;
            --text-secondary: #94a3b8;
            --border-color: rgba(139, 92, 246, 0.2);
            --gradient: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f0f23 0%, #1a1a3e 100%);
            color: var(--text-primary);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding-top: 60px;
        }

        .page-title {
            text-align: center;
            margin-bottom: 50px;
            animation: fadeInDown 0.8s ease;
        }

        .page-title h1 {
            font-size: 3rem;
            font-weight: 800;
            background: var(--gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 10px;
        }

        .page-title p {
            color: var(--text-secondary);
            font-size: 1.1rem;
        }

        .calculator-wrapper {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            animation: fadeInUp 0.8s ease;
        }

        @media (max-width: 992px) {
            .calculator-wrapper {
                grid-template-columns: 1fr;
            }
        }

        .calculator-card {
            background: var(--card-bg);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 35px;
            border: 1px solid var(--border-color);
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .calculator-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 50px rgba(139, 92, 246, 0.2);
        }

        .input-section h3,
        .result-section h3 {
            color: var(--text-primary);
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 25px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .input-section h3 i,
        .result-section h3 i {
            color: var(--primary);
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-label {
            display: block;
            color: var(--text-secondary);
            font-size: 0.9rem;
            font-weight: 500;
            margin-bottom: 10px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .input-wrapper {
            position: relative;
            display: flex;
            align-items: center;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            background: rgba(255, 255, 255, 0.05);
            border: 2px solid var(--border-color);
            border-radius: 10px;
            color: var(--text-primary);
            font-size: 1.1rem;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary);
            background: rgba(139, 92, 246, 0.05);
            box-shadow: 0 0 20px rgba(139, 92, 246, 0.2);
        }

        .input-suffix {
            position: absolute;
            right: 15px;
            color: var(--primary);
            font-weight: 600;
            pointer-events: none;
        }

        .range-slider {
            margin-top: 10px;
        }

        .range-input {
            width: 100%;
            height: 6px;
            background: linear-gradient(to right, var(--primary) 0%, var(--primary) var(--value), rgba(255,255,255,0.1) var(--value), rgba(255,255,255,0.1) 100%);
            border-radius: 5px;
            outline: none;
            -webkit-appearance: none;
            cursor: pointer;
        }

        .range-input::-webkit-slider-thumb {
            -webkit-appearance: none;
            width: 20px;
            height: 20px;
            background: var(--primary);
            border-radius: 50%;
            cursor: pointer;
            box-shadow: 0 0 10px rgba(139, 92, 246, 0.5);
            transition: all 0.3s ease;
        }

        .range-input::-webkit-slider-thumb:hover {
            transform: scale(1.2);
            box-shadow: 0 0 20px rgba(139, 92, 246, 0.8);
        }

        .range-input::-moz-range-thumb {
            width: 20px;
            height: 20px;
            background: var(--primary);
            border-radius: 50%;
            cursor: pointer;
            border: none;
            box-shadow: 0 0 10px rgba(139, 92, 246, 0.5);
        }

        .range-values {
            display: flex;
            justify-content: space-between;
            margin-top: 5px;
            font-size: 0.8rem;
            color: var(--text-secondary);
        }

        .calculate-btn {
            width: 100%;
            padding: 15px;
            background: var(--gradient);
            color: white;
            font-size: 1.1rem;
            font-weight: 600;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-top: 20px;
        }

        .calculate-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(139, 92, 246, 0.4);
        }

        .result-box {
            background: rgba(139, 92, 246, 0.1);
            border: 2px solid var(--border-color);
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 20px;
        }

        .result-label {
            color: var(--text-secondary);
            font-size: 0.9rem;
            margin-bottom: 5px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .result-value {
            font-size: 2rem;
            font-weight: 700;
            color: var(--primary);
            margin-bottom: 10px;
        }

        .result-details {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-top: 20px;
        }

        .detail-box {
            background: rgba(255, 255, 255, 0.05);
            padding: 15px;
            border-radius: 10px;
            border: 1px solid var(--border-color);
        }

        .detail-label {
            color: var(--text-secondary);
            font-size: 0.85rem;
            margin-bottom: 5px;
        }

        .detail-value {
            color: var(--text-primary);
            font-size: 1.3rem;
            font-weight: 600;
        }

        .chart-container {
            margin-top: 30px;
            padding: 20px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 15px;
            border: 1px solid var(--border-color);
        }

        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .back-button {
            position: fixed;
            top: 20px;
            left: 20px;
            padding: 10px 20px;
            background: rgba(139, 92, 246, 0.2);
            border: 2px solid var(--border-color);
            border-radius: 10px;
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .back-button:hover {
            background: var(--gradient);
            color: white;
            transform: translateX(-5px);
        }

        .info-tooltip {
            display: inline-block;
            margin-left: 5px;
            color: var(--text-secondary);
            cursor: help;
            font-size: 0.9rem;
        }

        .info-tooltip:hover {
            color: var(--primary);
        }

        /* Pie Chart */
        .pie-chart {
            width: 200px;
            height: 200px;
            margin: 20px auto;
        }
    </style>
</head>
<body>
    <a href="/" class="back-button">
        <i class="bi bi-arrow-left"></i>
        Back to Home
    </a>

    <div class="container">
        <div class="page-title">
            <h1>EMI Calculator</h1>
            <p>Calculate your monthly EMI and total interest for your loan</p>
        </div>

        <div class="calculator-wrapper">
            <!-- Input Section -->
            <div class="calculator-card input-section">
                <h3><i class="bi bi-calculator"></i> Loan Details</h3>

                <div class="form-group">
                    <label class="form-label">
                        Loan Amount
                        <i class="bi bi-info-circle info-tooltip" title="Total loan amount you want to borrow"></i>
                    </label>
                    <div class="input-wrapper">
                        <input type="number"
                               id="loanAmount"
                               class="form-control"
                               value="5000000"
                               min="100000"
                               max="100000000"
                               step="100000">
                        <span class="input-suffix">₹</span>
                    </div>
                    <div class="range-slider">
                        <input type="range"
                               id="loanAmountSlider"
                               class="range-input"
                               min="100000"
                               max="100000000"
                               value="5000000"
                               step="100000">
                        <div class="range-values">
                            <span>₹1L</span>
                            <span>₹10Cr</span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">
                        Interest Rate (% per annum)
                        <i class="bi bi-info-circle info-tooltip" title="Annual interest rate charged by the lender"></i>
                    </label>
                    <div class="input-wrapper">
                        <input type="number"
                               id="interestRate"
                               class="form-control"
                               value="8.5"
                               min="1"
                               max="30"
                               step="0.1">
                        <span class="input-suffix">%</span>
                    </div>
                    <div class="range-slider">
                        <input type="range"
                               id="interestRateSlider"
                               class="range-input"
                               min="1"
                               max="30"
                               value="8.5"
                               step="0.1">
                        <div class="range-values">
                            <span>1%</span>
                            <span>30%</span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">
                        Loan Tenure (Years)
                        <i class="bi bi-info-circle info-tooltip" title="Duration of the loan in years"></i>
                    </label>
                    <div class="input-wrapper">
                        <input type="number"
                               id="loanTenure"
                               class="form-control"
                               value="20"
                               min="1"
                               max="30"
                               step="1">
                        <span class="input-suffix">Yrs</span>
                    </div>
                    <div class="range-slider">
                        <input type="range"
                               id="loanTenureSlider"
                               class="range-input"
                               min="1"
                               max="30"
                               value="20"
                               step="1">
                        <div class="range-values">
                            <span>1 Year</span>
                            <span>30 Years</span>
                        </div>
                    </div>
                </div>

                <button class="calculate-btn" onclick="calculateEMI()">
                    <i class="bi bi-calculator-fill"></i> Calculate EMI
                </button>
            </div>

            <!-- Result Section -->
            <div class="calculator-card result-section">
                <h3><i class="bi bi-graph-up"></i> Calculation Results</h3>

                <div class="result-box">
                    <div class="result-label">Monthly EMI</div>
                    <div class="result-value" id="monthlyEMI">₹0</div>
                </div>

                <div class="result-details">
                    <div class="detail-box">
                        <div class="detail-label">Principal Amount</div>
                        <div class="detail-value" id="principalAmount">₹0</div>
                    </div>
                    <div class="detail-box">
                        <div class="detail-label">Total Interest</div>
                        <div class="detail-value" id="totalInterest">₹0</div>
                    </div>
                    <div class="detail-box">
                        <div class="detail-label">Total Amount</div>
                        <div class="detail-value" id="totalAmount">₹0</div>
                    </div>
                    <div class="detail-box">
                        <div class="detail-label">Total Payments</div>
                        <div class="detail-value" id="totalPayments">0</div>
                    </div>
                </div>

                <div class="chart-container">
                    <canvas id="emiChart" width="400" height="200"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <script>
        // Initialize chart variable
        let emiChart = null;

        // Sync input fields with sliders
        document.getElementById('loanAmount').addEventListener('input', function() {
            document.getElementById('loanAmountSlider').value = this.value;
            updateSliderBackground('loanAmountSlider');
        });

        document.getElementById('loanAmountSlider').addEventListener('input', function() {
            document.getElementById('loanAmount').value = this.value;
            updateSliderBackground('loanAmountSlider');
        });

        document.getElementById('interestRate').addEventListener('input', function() {
            document.getElementById('interestRateSlider').value = this.value;
            updateSliderBackground('interestRateSlider');
        });

        document.getElementById('interestRateSlider').addEventListener('input', function() {
            document.getElementById('interestRate').value = this.value;
            updateSliderBackground('interestRateSlider');
        });

        document.getElementById('loanTenure').addEventListener('input', function() {
            document.getElementById('loanTenureSlider').value = this.value;
            updateSliderBackground('loanTenureSlider');
        });

        document.getElementById('loanTenureSlider').addEventListener('input', function() {
            document.getElementById('loanTenure').value = this.value;
            updateSliderBackground('loanTenureSlider');
        });

        function updateSliderBackground(sliderId) {
            const slider = document.getElementById(sliderId);
            const min = parseFloat(slider.min);
            const max = parseFloat(slider.max);
            const value = parseFloat(slider.value);
            const percentage = ((value - min) / (max - min)) * 100;
            slider.style.setProperty('--value', percentage + '%');
        }

        function formatCurrency(amount) {
            return new Intl.NumberFormat('en-IN', {
                style: 'currency',
                currency: 'INR',
                minimumFractionDigits: 0,
                maximumFractionDigits: 0
            }).format(amount);
        }

        function calculateEMI() {
            // Get input values
            const principal = parseFloat(document.getElementById('loanAmount').value);
            const annualRate = parseFloat(document.getElementById('interestRate').value);
            const years = parseFloat(document.getElementById('loanTenure').value);

            // Calculate EMI
            const monthlyRate = annualRate / (12 * 100);
            const months = years * 12;

            // EMI Formula: P * r * (1 + r)^n / ((1 + r)^n - 1)
            const emi = principal * monthlyRate * Math.pow(1 + monthlyRate, months) /
                       (Math.pow(1 + monthlyRate, months) - 1);

            const totalAmount = emi * months;
            const totalInterest = totalAmount - principal;

            // Update results
            document.getElementById('monthlyEMI').textContent = formatCurrency(emi);
            document.getElementById('principalAmount').textContent = formatCurrency(principal);
            document.getElementById('totalInterest').textContent = formatCurrency(totalInterest);
            document.getElementById('totalAmount').textContent = formatCurrency(totalAmount);
            document.getElementById('totalPayments').textContent = months;

            // Update chart
            updateChart(principal, totalInterest);
        }

        function updateChart(principal, interest) {
            const ctx = document.getElementById('emiChart').getContext('2d');

            // Destroy existing chart if it exists
            if (emiChart) {
                emiChart.destroy();
            }

            // Create new chart
            emiChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: ['Principal', 'Interest'],
                    datasets: [{
                        data: [principal, interest],
                        backgroundColor: [
                            '#10b981',
                            '#8b5cf6'
                        ],
                        borderColor: [
                            'rgba(16, 185, 129, 0.2)',
                            'rgba(139, 92, 246, 0.2)'
                        ],
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                color: '#ffffff',
                                font: {
                                    size: 14,
                                    weight: '500'
                                },
                                padding: 20
                            }
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    label += formatCurrency(context.parsed);

                                    // Add percentage
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = ((context.parsed / total) * 100).toFixed(1);
                                    label += ' (' + percentage + '%)';

                                    return label;
                                }
                            }
                        }
                    }
                }
            });
        }

        // Initialize sliders background
        updateSliderBackground('loanAmountSlider');
        updateSliderBackground('interestRateSlider');
        updateSliderBackground('loanTenureSlider');

        // Calculate initial EMI on page load
        calculateEMI();
    </script>
</body>
</html>