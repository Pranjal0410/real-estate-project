package com.realestate.exception;

/**
 * Exception thrown when a request is invalid or malformed
 */
public class BadRequestException extends RuntimeException {
    
    private String field;
    private Object rejectedValue;
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BadRequestException(String field, Object rejectedValue, String message) {
        super(String.format("Bad request for field '%s' with value '%s': %s", field, rejectedValue, message));
        this.field = field;
        this.rejectedValue = rejectedValue;
    }
    
    public BadRequestException(String field, String message) {
        super(String.format("Bad request for field '%s': %s", field, message));
        this.field = field;
    }
    
    public String getField() {
        return field;
    }
    
    public Object getRejectedValue() {
        return rejectedValue;
    }
}