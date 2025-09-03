// Main JavaScript for Real Estate Platform

// API Base URL
const API_BASE = '/api';

// Authentication token management
const TokenManager = {
    getToken: () => localStorage.getItem('token'),
    setToken: (token) => localStorage.setItem('token', token),
    removeToken: () => localStorage.removeItem('token'),
    isAuthenticated: () => !!TokenManager.getToken()
};

// API request helper
async function apiRequest(url, options = {}) {
    const token = TokenManager.getToken();
    
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        }
    };
    
    try {
        const response = await fetch(url, { ...defaultOptions, ...options });
        
        if (!response.ok) {
            if (response.status === 401) {
                TokenManager.removeToken();
                window.location.href = '/login';
            }
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API Request failed:', error);
        throw error;
    }
}

// Property search functionality
function searchProperties() {
    const searchForm = document.getElementById('propertySearchForm');
    if (!searchForm) return;
    
    searchForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = new FormData(searchForm);
        const searchParams = Object.fromEntries(formData);
        
        try {
            const response = await apiRequest(`${API_BASE}/properties/search`, {
                method: 'POST',
                body: JSON.stringify(searchParams)
            });
            
            displaySearchResults(response.data);
        } catch (error) {
            showAlert('Search failed. Please try again.', 'danger');
        }
    });
}

// Display search results
function displaySearchResults(properties) {
    const resultsContainer = document.getElementById('searchResults');
    if (!resultsContainer) return;
    
    if (properties.length === 0) {
        resultsContainer.innerHTML = '<p class="text-center">No properties found matching your criteria.</p>';
        return;
    }
    
    const propertiesHtml = properties.map(property => `
        <div class="col-md-4 mb-4">
            <div class="card property-card h-100">
                <div class="card-body">
                    <h5 class="card-title">${property.title}</h5>
                    <p class="text-muted mb-2">
                        <i class="bi bi-geo-alt"></i> ${property.location}
                    </p>
                    <h4 class="text-primary">$${formatNumber(property.price)}</h4>
                    <div class="property-details mt-3">
                        <span class="badge bg-secondary">${property.propertyType}</span>
                        <span class="badge bg-info">${property.bedrooms} Beds</span>
                        <span class="badge bg-info">${property.area} sq ft</span>
                    </div>
                    <a href="/property/${property.id}" class="btn btn-primary btn-sm mt-3 w-100">View Details</a>
                </div>
            </div>
        </div>
    `).join('');
    
    resultsContainer.innerHTML = propertiesHtml;
}

// Investment calculator
function initCalculator() {
    const calculatorForm = document.getElementById('investmentCalculator');
    if (!calculatorForm) return;
    
    calculatorForm.addEventListener('input', (e) => {
        if (e.target.matches('input[type="number"]')) {
            calculateInvestmentMetrics();
        }
    });
}

// Calculate investment metrics
async function calculateInvestmentMetrics() {
    const form = document.getElementById('investmentCalculator');
    if (!form) return;
    
    const formData = new FormData(form);
    const data = Object.fromEntries(formData);
    
    // Perform calculations
    const loanAmount = data.propertyPrice * (1 - data.downPayment / 100);
    const monthlyPayment = calculateMortgage(loanAmount, data.interestRate, data.loanTerm * 12);
    const monthlyRental = parseFloat(data.monthlyRental);
    const monthlyExpenses = parseFloat(data.monthlyExpenses) || 0;
    const cashFlow = monthlyRental - monthlyPayment - monthlyExpenses;
    
    // Update UI
    updateMetricDisplay('loanAmount', loanAmount);
    updateMetricDisplay('monthlyPayment', monthlyPayment);
    updateMetricDisplay('cashFlow', cashFlow);
    
    // Calculate ROI and other metrics via API if needed
    if (data.propertyPrice && data.monthlyRental) {
        try {
            const roiResponse = await apiRequest(
                `${API_BASE}/investments/roi?propertyPrice=${data.propertyPrice}&monthlyRental=${data.monthlyRental}&annualExpenses=${data.annualExpenses || 0}`
            );
            updateMetricDisplay('roi', roiResponse.data);
        } catch (error) {
            console.error('Failed to calculate ROI:', error);
        }
    }
}

// Mortgage calculation
function calculateMortgage(principal, annualRate, months) {
    const monthlyRate = annualRate / 100 / 12;
    if (monthlyRate === 0) {
        return principal / months;
    }
    const payment = principal * monthlyRate * Math.pow(1 + monthlyRate, months) / 
                   (Math.pow(1 + monthlyRate, months) - 1);
    return payment;
}

// Update metric display
function updateMetricDisplay(elementId, value) {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = typeof value === 'number' ? formatNumber(value) : value;
    }
}

// Format numbers with commas
function formatNumber(num) {
    return new Intl.NumberFormat('en-US').format(num);
}

// Show alert message
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer') || document.body;
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.appendChild(alert);
    
    setTimeout(() => {
        alert.remove();
    }, 5000);
}

// Chatbot functionality
class Chatbot {
    constructor() {
        this.sessionId = this.generateSessionId();
        this.messageContainer = document.getElementById('chatMessages');
        this.inputField = document.getElementById('chatInput');
        this.sendButton = document.getElementById('chatSend');
        
        if (this.sendButton) {
            this.sendButton.addEventListener('click', () => this.sendMessage());
        }
        
        if (this.inputField) {
            this.inputField.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    this.sendMessage();
                }
            });
        }
    }
    
    generateSessionId() {
        return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    }
    
    async sendMessage() {
        const message = this.inputField.value.trim();
        if (!message) return;
        
        this.addMessage(message, 'user');
        this.inputField.value = '';
        
        try {
            const response = await apiRequest(`${API_BASE}/chatbot/message`, {
                method: 'POST',
                body: JSON.stringify({
                    message: message,
                    sessionId: this.sessionId,
                    userId: localStorage.getItem('username') || 'anonymous'
                })
            });
            
            if (response.data) {
                this.addMessage(response.data.message, 'bot');
            }
        } catch (error) {
            this.addMessage('Sorry, I encountered an error. Please try again.', 'bot');
        }
    }
    
    addMessage(text, sender) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${sender}-message`;
        messageDiv.textContent = text;
        
        this.messageContainer.appendChild(messageDiv);
        this.messageContainer.scrollTop = this.messageContainer.scrollHeight;
    }
}

// Property favorite functionality
async function toggleFavorite(propertyId) {
    if (!TokenManager.isAuthenticated()) {
        window.location.href = '/login';
        return;
    }
    
    try {
        const response = await apiRequest(`${API_BASE}/users/favorites/${propertyId}`, {
            method: 'POST'
        });
        
        const heartIcon = document.querySelector(`#favorite-${propertyId}`);
        if (heartIcon) {
            heartIcon.classList.toggle('text-danger');
            heartIcon.classList.toggle('text-secondary');
        }
        
        showAlert(response.message, 'success');
    } catch (error) {
        showAlert('Failed to update favorite', 'danger');
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    // Initialize components
    searchProperties();
    initCalculator();
    
    // Initialize chatbot if on chatbot page
    if (document.getElementById('chatMessages')) {
        new Chatbot();
    }
    
    // Update navbar based on authentication status
    updateNavbar();
    
    // Add smooth scrolling
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth' });
            }
        });
    });
});

// Update navbar based on authentication
function updateNavbar() {
    const isAuthenticated = TokenManager.isAuthenticated();
    const authLinks = document.querySelector('.auth-links');
    
    if (authLinks) {
        if (isAuthenticated) {
            authLinks.innerHTML = `
                <li class="nav-item">
                    <a class="nav-link" href="/dashboard">Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="logout()">Logout</a>
                </li>
            `;
        }
    }
}

// Logout function
function logout() {
    TokenManager.removeToken();
    localStorage.clear();
    window.location.href = '/';
}