package com.realestate.service;

import com.realestate.exception.BusinessException;
import com.realestate.model.dto.ChatbotRequestDTO;
import com.realestate.model.dto.ChatbotResponseDTO;
import com.realestate.model.dto.PropertyDTO;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final PropertyService propertyService;
    private final InvestmentCalculatorService investmentCalculatorService;
    
    @Value("${openai.api.key:demo-key}")
    private String openAiApiKey;
    
    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;
    
    private OpenAiService openAiService;
    
    private final Map<String, List<String>> conversationHistory = new HashMap<>();
    
    @Async
    public CompletableFuture<ChatbotResponseDTO> processMessage(ChatbotRequestDTO request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing chatbot message: {}", request.getMessage());
            
            ChatbotResponseDTO response = new ChatbotResponseDTO();
            response.setUserId(request.getUserId());
            response.setSessionId(request.getSessionId());
            
            try {
                String messageResponse = handleMessage(request);
                response.setMessage(messageResponse);
                response.setSuccess(true);
                
                saveConversationHistory(request.getSessionId(), request.getMessage(), messageResponse);
                
            } catch (Exception e) {
                log.error("Error processing chatbot message", e);
                response.setMessage("I'm sorry, I couldn't process your request. Please try again.");
                response.setSuccess(false);
            }
            
            return response;
        });
    }
    
    private String handleMessage(ChatbotRequestDTO request) {
        String message = request.getMessage().toLowerCase();
        
        if (containsPropertyQuery(message)) {
            return handlePropertyQuery(message);
        } else if (containsInvestmentQuery(message)) {
            return handleInvestmentQuery(message);
        } else if (containsPriceQuery(message)) {
            return handlePriceQuery(message);
        } else if (containsCalculationQuery(message)) {
            return handleCalculationQuery(message);
        } else {
            return getAIResponse(request);
        }
    }
    
    private boolean containsPropertyQuery(String message) {
        List<String> keywords = Arrays.asList("property", "properties", "house", "apartment", 
            "condo", "listing", "available", "show", "find");
        return keywords.stream().anyMatch(message::contains);
    }
    
    private boolean containsInvestmentQuery(String message) {
        List<String> keywords = Arrays.asList("invest", "roi", "return", "yield", 
            "cash flow", "cap rate");
        return keywords.stream().anyMatch(message::contains);
    }
    
    private boolean containsPriceQuery(String message) {
        List<String> keywords = Arrays.asList("price", "cost", "expensive", "cheap", 
            "afford", "budget");
        return keywords.stream().anyMatch(message::contains);
    }
    
    private boolean containsCalculationQuery(String message) {
        List<String> keywords = Arrays.asList("calculate", "mortgage", "payment", 
            "monthly", "loan");
        return keywords.stream().anyMatch(message::contains);
    }
    
    private String handlePropertyQuery(String message) {
        StringBuilder response = new StringBuilder();
        
        Optional<String> location = extractLocation(message);
        Optional<BigDecimal> priceLimit = extractPrice(message);
        
        List<PropertyDTO> properties;
        
        if (location.isPresent()) {
            properties = propertyService.findByLocation(location.get());
            response.append("Here are properties in ").append(location.get()).append(":\n\n");
        } else if (priceLimit.isPresent()) {
            properties = propertyService.findByPriceRange(BigDecimal.ZERO, priceLimit.get());
            response.append("Here are properties within your budget:\n\n");
        } else {
            properties = propertyService.findAll();
            if (properties.size() > 5) {
                properties = properties.subList(0, 5);
            }
            response.append("Here are some featured properties:\n\n");
        }
        
        if (properties.isEmpty()) {
            return "I couldn't find any properties matching your criteria. Would you like to adjust your search?";
        }
        
        for (PropertyDTO property : properties) {
            response.append(formatPropertyInfo(property)).append("\n");
        }
        
        response.append("\nWould you like more details about any of these properties?");
        
        return response.toString();
    }
    
    private String handleInvestmentQuery(String message) {
        StringBuilder response = new StringBuilder();
        response.append("I can help you with investment calculations. Here's what I can calculate:\n\n");
        response.append("1. ROI (Return on Investment)\n");
        response.append("2. Rental Yield\n");
        response.append("3. Cap Rate\n");
        response.append("4. Cash Flow Analysis\n");
        response.append("5. Mortgage Payments\n\n");
        response.append("Please provide the property details and I'll calculate the metrics for you.");
        
        return response.toString();
    }
    
    private String handlePriceQuery(String message) {
        Optional<PropertyDTO> cheapest = propertyService.findCheapestProperty();
        Optional<PropertyDTO> expensive = propertyService.findMostExpensiveProperty();
        
        StringBuilder response = new StringBuilder();
        
        if (message.contains("cheap") || message.contains("affordable")) {
            cheapest.ifPresent(property -> {
                response.append("The most affordable property available:\n\n");
                response.append(formatPropertyInfo(property));
            });
        } else if (message.contains("expensive") || message.contains("luxury")) {
            expensive.ifPresent(property -> {
                response.append("Our premium property:\n\n");
                response.append(formatPropertyInfo(property));
            });
        } else {
            response.append("Property Price Range:\n\n");
            cheapest.ifPresent(p -> response.append("Starting from: $")
                .append(p.getPrice()).append("\n"));
            expensive.ifPresent(p -> response.append("Up to: $")
                .append(p.getPrice()).append("\n"));
        }
        
        return response.toString();
    }
    
    private String handleCalculationQuery(String message) {
        Optional<BigDecimal> amount = extractPrice(message);
        
        if (amount.isPresent()) {
            BigDecimal loanAmount = amount.get();
            BigDecimal monthlyPayment = investmentCalculatorService.calculateMortgagePayment(
                loanAmount, new BigDecimal("4.5"), 360);
            
            StringBuilder response = new StringBuilder();
            response.append("Mortgage Calculation:\n\n");
            response.append("Loan Amount: $").append(loanAmount).append("\n");
            response.append("Interest Rate: 4.5% (current average)\n");
            response.append("Term: 30 years\n");
            response.append("Monthly Payment: $").append(monthlyPayment).append("\n\n");
            response.append("Note: This is an estimate. Actual rates may vary based on credit score and down payment.");
            
            return response.toString();
        }
        
        return "Please provide the loan amount for mortgage calculation. Example: 'Calculate mortgage for $500,000'";
    }
    
    private String getAIResponse(ChatbotRequestDTO request) {
        if (openAiApiKey.equals("demo-key")) {
            return getDemoResponse(request.getMessage());
        }
        
        try {
            if (openAiService == null) {
                openAiService = new OpenAiService(openAiApiKey, Duration.ofSeconds(30));
            }
            
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage("system", 
                "You are a helpful real estate investment advisor. Provide professional advice about properties and investments."));
            
            List<String> history = conversationHistory.getOrDefault(request.getSessionId(), new ArrayList<>());
            for (String hist : history) {
                messages.add(new ChatMessage("assistant", hist));
            }
            
            messages.add(new ChatMessage("user", request.getMessage()));
            
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .temperature(0.7)
                .maxTokens(500)
                .build();
            
            ChatCompletionResult result = openAiService.createChatCompletion(completionRequest);
            
            return result.getChoices().get(0).getMessage().getContent();
            
        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            return getDemoResponse(request.getMessage());
        }
    }
    
    private String getDemoResponse(String message) {
        List<String> responses = Arrays.asList(
            "I understand you're interested in real estate investment. How can I help you today?",
            "That's a great question about property investment. Let me provide you with some insights.",
            "Based on current market trends, I recommend considering properties in growing neighborhoods.",
            "Investment properties can provide both rental income and long-term appreciation.",
            "Would you like me to show you some available properties or calculate investment metrics?"
        );
        
        Random random = new Random();
        return responses.get(random.nextInt(responses.size()));
    }
    
    private String formatPropertyInfo(PropertyDTO property) {
        StringBuilder info = new StringBuilder();
        info.append("📍 ").append(property.getTitle()).append("\n");
        info.append("Location: ").append(property.getLocation()).append("\n");
        info.append("Price: $").append(property.getPrice()).append("\n");
        info.append("Type: ").append(property.getPropertyType()).append("\n");
        info.append("Bedrooms: ").append(property.getBedrooms());
        info.append(" | Area: ").append(property.getArea()).append(" sq ft\n");
        
        return info.toString();
    }
    
    private Optional<String> extractLocation(String message) {
        List<String> locations = Arrays.asList("new york", "los angeles", "chicago", 
            "houston", "phoenix", "philadelphia", "san antonio", "san diego", "dallas");
        
        return locations.stream()
            .filter(message::contains)
            .findFirst();
    }
    
    private Optional<BigDecimal> extractPrice(String message) {
        Pattern pattern = Pattern.compile("\\$?([0-9,]+)");
        Matcher matcher = pattern.matcher(message);
        
        if (matcher.find()) {
            String priceStr = matcher.group(1).replace(",", "");
            try {
                return Optional.of(new BigDecimal(priceStr));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }
    
    private void saveConversationHistory(String sessionId, String userMessage, String botResponse) {
        List<String> history = conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
        history.add("User: " + userMessage);
        history.add("Bot: " + botResponse);
        
        if (history.size() > 20) {
            history.subList(0, 2).clear();
        }
    }
    
    @Cacheable(value = "chatbot-responses", key = "#message")
    public String getCachedResponse(String message) {
        return handlePropertyQuery(message.toLowerCase());
    }
    
    public void clearConversationHistory(String sessionId) {
        conversationHistory.remove(sessionId);
    }
}