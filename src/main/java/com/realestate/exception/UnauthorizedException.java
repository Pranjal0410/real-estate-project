package com.realestate.exception;

/**
 * Exception thrown when access is unauthorized
 */
public class UnauthorizedException extends RuntimeException {
    
    private String resource;
    private String action;
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnauthorizedException(String resource, String action) {
        super(String.format("Unauthorized access to %s for action: %s", resource, action));
        this.resource = resource;
        this.action = action;
    }
    
    public UnauthorizedException(String resource, String action, String additionalInfo) {
        super(String.format("Unauthorized access to %s for action: %s. %s", resource, action, additionalInfo));
        this.resource = resource;
        this.action = action;
    }
    
    public String getResource() {
        return resource;
    }
    
    public String getAction() {
        return action;
    }
}