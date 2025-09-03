package com.realestate.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatbotResponseDTO {
    
    // Fields used by ChatbotService
    private Long userId;
    private String message;
    private Boolean success;

    private String messageId;
    private String sessionId;

    private String response;
    private String formattedResponse; // HTML formatted response for UI

    private ResponseType responseType;
    private ChatIntent detectedIntent;
    private Double confidenceScore;

    private LocalDateTime timestamp;

    // Structured data for UI components
    private List<PropertyDTO> suggestedProperties;
    private InvestmentCalculationDTO calculationResult;
    private MarketDataDTO marketData;

    // Interactive elements
    private List<QuickReply> quickReplies;
    private List<ActionButton> actionButtons;

    // Follow-up suggestions
    private List<String> followUpQuestions;

    // Context for next interaction
    private Map<String, String> contextParameters;

    // Error handling
    @Builder.Default
    private Boolean hasError = false;
    private String errorMessage;
    private String errorCode;

    // Analytics
    private String responseSource; // AI, template, database, etc.
    private Long responseTimeMs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuickReply {
        private String text;
        private String payload;
        private String type; // text, postback, location, etc.
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionButton {
        private String text;
        private String action; // navigate, search, calculate, etc.
        private String url;
        private Map<String, String> parameters;
    }

    public enum ResponseType {
        TEXT,
        PROPERTY_LIST,
        INVESTMENT_CALCULATION,
        MARKET_ANALYSIS,
        ERROR,
        CONFIRMATION,
        CLARIFICATION_NEEDED
    }

    public enum ChatIntent {
        PROPERTY_SEARCH,
        PROPERTY_DETAILS,
        INVESTMENT_CALCULATION,
        MARKET_DATA_INQUIRY,
        PRICE_ESTIMATION,
        LOCATION_ANALYSIS,
        RENTAL_YIELD_INQUIRY,
        MORTGAGE_CALCULATION,
        GENERAL_INQUIRY,
        USER_SUPPORT,
        PROPERTY_COMPARISON,
        INVESTMENT_ADVICE
    }
}