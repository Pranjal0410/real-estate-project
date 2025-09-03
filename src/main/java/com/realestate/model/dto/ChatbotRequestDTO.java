package com.realestate.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequestDTO {

    @NotBlank(message = "Message is required")
    @Size(min = 1, max = 2000, message = "Message must be between 1 and 2000 characters")
    private String message;

    private String sessionId;
    private Long userId;

    // Context information for better responses
    private String context;
    private ChatIntent intent;

    // Property-specific context
    private Long propertyId;
    private String propertyLocation;
    private String propertyType;

    // Investment calculation context
    private Long calculationId;

    // User preferences
    @Builder.Default
    private String preferredLanguage = "en";
    private String timezone;

    // Additional parameters for specific intents
    private Map<String, String> parameters;

    // Conversation metadata
    @Builder.Default
    private Boolean isFollowUp = false;
    private String previousMessageId;

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