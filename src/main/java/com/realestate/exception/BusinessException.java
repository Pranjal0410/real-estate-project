package com.realestate.exception;

/**
 * Exception thrown when business logic rules are violated
 */
public class BusinessException extends RuntimeException {
    
    private String businessRule;
    private String context;
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BusinessException(String businessRule, String context, String message) {
        super(String.format("Business rule violation '%s' in context '%s': %s", businessRule, context, message));
        this.businessRule = businessRule;
        this.context = context;
    }
    
    public BusinessException(String businessRule, String message) {
        super(String.format("Business rule violation '%s': %s", businessRule, message));
        this.businessRule = businessRule;
    }
    
    public String getBusinessRule() {
        return businessRule;
    }
    
    public String getContext() {
        return context;
    }
}