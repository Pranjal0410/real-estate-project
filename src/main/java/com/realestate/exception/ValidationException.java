package com.realestate.exception;

import java.util.Map;
import java.util.HashMap;

/**
 * Exception thrown when validation fails
 */
public class ValidationException extends RuntimeException {
    
    private String field;
    private Object rejectedValue;
    private Map<String, String> validationErrors;
    
    public ValidationException(String message) {
        super(message);
        this.validationErrors = new HashMap<>();
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = new HashMap<>();
    }
    
    public ValidationException(String field, Object rejectedValue, String message) {
        super(String.format("Validation failed for field '%s' with value '%s': %s", field, rejectedValue, message));
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.validationErrors = new HashMap<>();
        this.validationErrors.put(field, message);
    }
    
    public ValidationException(Map<String, String> validationErrors) {
        super("Validation failed for multiple fields");
        this.validationErrors = validationErrors != null ? validationErrors : new HashMap<>();
    }
    
    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message));
        this.field = field;
        this.validationErrors = new HashMap<>();
        this.validationErrors.put(field, message);
    }
    
    public String getField() {
        return field;
    }
    
    public Object getRejectedValue() {
        return rejectedValue;
    }
    
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
    
    public void addValidationError(String field, String message) {
        this.validationErrors.put(field, message);
    }
}