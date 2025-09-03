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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatbotController {

    private final ChatbotService chatbotService;

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
}