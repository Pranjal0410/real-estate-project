package com.realestate.controller;

import com.realestate.model.dto.ApiResponse;
import com.realestate.model.dto.ChatbotRequestDTO;
import com.realestate.model.dto.ChatbotResponseDTO;
import com.realestate.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final Random random = new Random();

    @PostMapping("/message")
    public CompletableFuture<ResponseEntity<ApiResponse<ChatbotResponseDTO>>> sendMessage(
            @Valid @RequestBody ChatbotRequestDTO request) {
        log.info("Chatbot request from user: {}", request.getUserId());
        
        return chatbotService.processMessage(request)
            .thenApply(response -> ResponseEntity.ok(
                ApiResponse.success(response, "Message processed")));
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<String>> clearSession(@PathVariable String sessionId) {
        chatbotService.clearConversationHistory(sessionId);
        return ResponseEntity.ok(ApiResponse.success("Conversation history cleared", "Session cleared"));
    }

    @GetMapping("/cached/{message}")
    public ResponseEntity<ApiResponse<String>> getCachedResponse(@PathVariable String message) {
        String response = chatbotService.getCachedResponse(message);
        return ResponseEntity.ok(ApiResponse.success(response, "Cached response"));
    }
    
    @PostMapping("/simple-message")
    public ResponseEntity<ApiResponse<String>> sendSimpleMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        log.info("Simple chatbot received message: {}", userMessage);
        
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Message cannot be empty"));
        }
        
        String response = generateSimpleResponse(userMessage.toLowerCase().trim());
        
        return ResponseEntity.ok(
            ApiResponse.success(response, "Message processed successfully"));
    }
    
    private String generateSimpleResponse(String message) {
        if (message.contains("property") || message.contains("properties")) {
            return "I can help you explore our property listings! We have various types including houses, condos, and commercial properties. What specific type interests you?";
        }
        
        if (message.contains("roi") || message.contains("return") || message.contains("investment")) {
            return "ROI (Return on Investment) is crucial for property investing! Our calculator can help you analyze potential returns. Generally, 8-12% ROI is considered good for real estate.";
        }
        
        if (message.contains("calculate") || message.contains("calculator")) {
            return "Our investment calculator can analyze any property! Input the price, rental income, and expenses to get ROI, cash flow, cap rate, and rental yield calculations.";
        }
        
        if (message.contains("hello") || message.contains("hi") || message.contains("hey")) {
            return "Hello! I'm your AI investment assistant. I can help with property analysis, investment calculations, and market insights. What would you like to explore?";
        }
        
        if (message.contains("help")) {
            return "I can help you with:\n• Property recommendations\n• Investment calculations (ROI, cap rate, cash flow)\n• Market insights\n• Using our calculator and platform features\n\nWhat interests you?";
        }
        
        return "That's an interesting question! I specialize in real estate investment advice. Could you tell me more about what specific property or investment topic you'd like to explore?";
    }
}