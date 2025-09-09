<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="AI-powered real estate investment assistant">
    <title>AI Assistant - PropInvest</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css">
    
    <!-- Custom CSS -->
    <link href="/css/style.css" rel="stylesheet">
    <link href="/css/global.css" rel="stylesheet">
    <link href="/css/magicbricks-exact.css" rel="stylesheet">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        .chat-container {
            background: rgba(30, 41, 59, 0.8);
            border-radius: var(--radius-2xl);
            border: 1px solid rgba(148,163,184,0.15);
            height: 600px;
            display: flex;
            flex-direction: column;
        }
        
        .chat-header {
            background: var(--gradient-primary);
            color: white;
            padding: var(--space-lg);
            border-radius: var(--radius-2xl) var(--radius-2xl) 0 0;
            text-align: center;
        }
        
        .chat-messages {
            flex: 1;
            padding: var(--space-lg);
            overflow-y: auto;
            background: rgba(2,6,23,0.6);
        }
        
        .chat-input {
            padding: var(--space-lg);
            border-top: 1px solid rgba(148,163,184,0.15);
            background: rgba(15,23,42,0.8);
            border-radius: 0 0 var(--radius-2xl) var(--radius-2xl);
        }
        
        .message {
            margin-bottom: var(--space-lg);
            display: flex;
            align-items: flex-start;
        }
        
        .message.user {
            justify-content: flex-end;
        }
        
        .message.bot {
            justify-content: flex-start;
        }
        
        .message-content {
            max-width: 70%;
            padding: var(--space-md);
            border-radius: var(--radius-lg);
            position: relative;
        }
        
        .message.user .message-content {
            background: var(--gradient-primary);
            color: white;
            margin-left: var(--space-lg);
        }
        
        .message.bot .message-content {
            background: rgba(148,163,184,0.15);
            color: var(--gray-800);
            margin-right: var(--space-lg);
        }
        
        .message-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: var(--text-lg);
            margin: 0 var(--space-sm);
        }
        
        .message.user .message-avatar {
            background: var(--gradient-accent);
            color: white;
        }
        
        .message.bot .message-avatar {
            background: var(--gradient-primary);
            color: white;
        }
        
        .quick-suggestions {
            display: flex;
            gap: var(--space-sm);
            margin-bottom: var(--space-md);
            flex-wrap: wrap;
        }
        
        .suggestion-btn {
            background: rgba(16, 185, 129, 0.1);
            border: 1px solid var(--primary);
            color: var(--primary);
            padding: var(--space-sm) var(--space-md);
            border-radius: var(--radius-full);
            font-size: var(--text-sm);
            cursor: pointer;
            transition: all var(--transition-fast);
        }
        
        .suggestion-btn:hover {
            background: var(--primary);
            color: white;
        }
    </style>
</head>
<body class="bg-dark-theme">
    <div id="scrollProgress"></div>
    
    <!-- Include Navigation -->
    <jsp:include page="includes/navbar-real.jsp"/>
    
    <!-- Hero Section -->
    <section class="hero-section" style="min-height: 40vh;">
        <div class="hero-content">
            <h1 class="hero-title">AI Investment Assistant</h1>
            <p class="hero-subtitle">
                Get personalized investment advice and property recommendations from our AI-powered assistant.
            </p>
        </div>
    </section>
    
    <!-- Chat Section -->
    <section class="section-padding">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="chat-container" data-aos="fade-up">
                        <!-- Chat Header -->
                        <div class="chat-header">
                            <h4 class="mb-0">
                                <i class="bi bi-robot me-2"></i>
                                PropInvest AI Assistant
                            </h4>
                            <p class="mb-0 mt-2 opacity-90">Ask me about properties, investments, or market analysis!</p>
                        </div>
                        
                        <!-- Chat Messages -->
                        <div class="chat-messages" id="chatMessages">
                            <!-- Welcome Message -->
                            <div class="message bot">
                                <div class="message-avatar">
                                    <i class="bi bi-robot"></i>
                                </div>
                                <div class="message-content">
                                    <p class="mb-2">👋 Hello! I'm your AI investment assistant. I can help you with:</p>
                                    <ul class="mb-0">
                                        <li>Property recommendations</li>
                                        <li>Investment analysis</li>
                                        <li>Market insights</li>
                                        <li>ROI calculations</li>
                                        <li>General real estate advice</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Chat Input -->
                        <div class="chat-input">
                            <!-- Quick Suggestions -->
                            <div class="quick-suggestions">
                                <button class="suggestion-btn" onclick="sendSuggestion('What are the best properties for investment?')">
                                    Best properties for investment?
                                </button>
                                <button class="suggestion-btn" onclick="sendSuggestion('How do I calculate ROI?')">
                                    How to calculate ROI?
                                </button>
                                <button class="suggestion-btn" onclick="sendSuggestion('Show me properties under $500k')">
                                    Properties under $500k
                                </button>
                            </div>
                            
                            <!-- Input Form -->
                            <form id="chatForm" class="d-flex gap-2">
                                <input type="text" 
                                       id="messageInput" 
                                       class="form-control form-control-lg" 
                                       placeholder="Ask me anything about real estate investments..."
                                       required>
                                <button type="submit" class="btn btn-primary px-4">
                                    <i class="bi bi-send"></i>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Feature Cards -->
            <div class="row g-4 mt-5">
                <div class="col-md-4" data-aos="fade-up" data-aos-delay="100">
                    <div class="feature-card text-center">
                        <div class="feature-icon">
                            <i class="bi bi-lightbulb"></i>
                        </div>
                        <h5 class="feature-title">Smart Recommendations</h5>
                        <p class="feature-description">Get personalized property suggestions based on your preferences and budget.</p>
                    </div>
                </div>
                
                <div class="col-md-4" data-aos="fade-up" data-aos-delay="200">
                    <div class="feature-card text-center">
                        <div class="feature-icon">
                            <i class="bi bi-graph-up"></i>
                        </div>
                        <h5 class="feature-title">Market Analysis</h5>
                        <p class="feature-description">Real-time market insights and trends to help you make informed decisions.</p>
                    </div>
                </div>
                
                <div class="col-md-4" data-aos="fade-up" data-aos-delay="300">
                    <div class="feature-card text-center">
                        <div class="feature-icon">
                            <i class="bi bi-calculator"></i>
                        </div>
                        <h5 class="feature-title">Investment Calculations</h5>
                        <p class="feature-description">Instant ROI, rental yield, and cash flow calculations for any property.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Include Footer -->
    <jsp:include page="includes/footer.jsp"/>
    
    <!-- Back to Top Button -->
    <button id="backToTop" aria-label="Back to top" title="Back to top">
        <i class="bi bi-arrow-up"></i>
    </button>
    
    <!-- Scripts -->
    <script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/aos@2.3.4/dist/aos.js"></script>
    
    <script>
        AOS.init({ once: true, duration: 700, easing: 'ease-out-cubic' });
        
        // Chat functionality
        const chatMessages = document.getElementById('chatMessages');
        const chatForm = document.getElementById('chatForm');
        const messageInput = document.getElementById('messageInput');
        
        // Handle form submission
        chatForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const message = messageInput.value.trim();
            if (message) {
                sendMessage(message);
                messageInput.value = '';
            }
        });
        
        function sendMessage(message) {
            // Add user message
            addMessage(message, 'user');
            
            // Show typing indicator
            showTypingIndicator();
            
            // Call actual API
            $.ajax({
                url: '/api/chatbot/simple-message',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ message: message }),
                timeout: 5000
            }).done(function(response) {
                hideTypingIndicator();
                if (response.success && response.data) {
                    addMessage(response.data, 'bot');
                } else {
                    addMessage('Sorry, I encountered an error. Please try again.', 'bot');
                }
            }).fail(function() {
                hideTypingIndicator();
                addMessage('Sorry, I\'m having trouble connecting. Please try again later.', 'bot');
            });
        }
        
        function sendSuggestion(suggestion) {
            sendMessage(suggestion);
        }
        
        function addMessage(message, sender) {
            const messageDiv = document.createElement('div');
            messageDiv.className = `message ${sender}`;
            
            const avatar = document.createElement('div');
            avatar.className = 'message-avatar';
            avatar.innerHTML = sender === 'user' ? '<i class="bi bi-person-fill"></i>' : '<i class="bi bi-robot"></i>';
            
            const content = document.createElement('div');
            content.className = 'message-content';
            content.innerHTML = `<p class="mb-0">${message}</p>`;
            
            if (sender === 'user') {
                messageDiv.appendChild(content);
                messageDiv.appendChild(avatar);
            } else {
                messageDiv.appendChild(avatar);
                messageDiv.appendChild(content);
            }
            
            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
        
        function showTypingIndicator() {
            const typing = document.createElement('div');
            typing.id = 'typingIndicator';
            typing.className = 'message bot';
            typing.innerHTML = `
                <div class="message-avatar">
                    <i class="bi bi-robot"></i>
                </div>
                <div class="message-content">
                    <p class="mb-0">
                        <span class="typing-dots">
                            <span>.</span><span>.</span><span>.</span>
                        </span>
                        Typing...
                    </p>
                </div>
            `;
            chatMessages.appendChild(typing);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
        
        function hideTypingIndicator() {
            const typing = document.getElementById('typingIndicator');
            if (typing) {
                typing.remove();
            }
        }
        
        function generateAIResponse(message) {
            const responses = {
                'properties': "I'd be happy to help you find properties! Based on our current listings, here are some great investment opportunities. Would you like me to filter by location, price range, or property type?",
                'investment': "Great question about investments! For successful real estate investing, consider factors like location, rental yield, cap rate, and long-term appreciation potential. Our calculator can help you analyze any property's ROI.",
                'roi': "ROI (Return on Investment) is calculated as: (Annual Rental Income - Annual Expenses) / Total Investment × 100. For example, if you invest $100k and earn $8k annually after expenses, your ROI is 8%. Use our calculator for detailed analysis!",
                'market': "The current real estate market shows strong fundamentals with increasing demand in urban areas. Interest rates and local economic factors are key considerations. Would you like analysis for a specific location?",
                'default': "That's an interesting question! I'm here to help with real estate investments, property analysis, market insights, and investment calculations. Could you be more specific about what you'd like to know?"
            };
            
            const lowerMessage = message.toLowerCase();
            
            if (lowerMessage.includes('property') || lowerMessage.includes('properties')) {
                return responses.properties;
            } else if (lowerMessage.includes('investment') || lowerMessage.includes('invest')) {
                return responses.investment;
            } else if (lowerMessage.includes('roi') || lowerMessage.includes('return')) {
                return responses.roi;
            } else if (lowerMessage.includes('market') || lowerMessage.includes('trend')) {
                return responses.market;
            } else {
                return responses.default;
            }
        }
        
        // Back to top functionality
        (function(){
            const btn = document.getElementById('backToTop');
            const bar = document.getElementById('scrollProgress');
            const toggleBtn = () => {
                const scrollY = window.scrollY;
                btn.classList.toggle('show', scrollY > 400);
                
                // Update progress bar
                const docHeight = document.documentElement.scrollHeight - window.innerHeight;
                const width = docHeight > 0 ? (scrollY / docHeight) * 100 : 0;
                if (bar) bar.style.width = width + '%';
            };
            
            window.addEventListener('scroll', toggleBtn);
            btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
            toggleBtn();
        })();
    </script>
</body>
</html>