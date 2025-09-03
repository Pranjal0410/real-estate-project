package com.realestate.aspect;

import com.realestate.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Pointcut("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public void preAuthorizePointcut() {}

    @Before("preAuthorizePointcut()")
    public void checkAuthentication(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthorized access attempt to method: {}", 
                    joinPoint.getSignature().getName());
            throw new UnauthorizedException("User is not authenticated");
        }
        
        log.debug("User {} accessing secured method: {}",
                authentication.getName(),
                joinPoint.getSignature().getName());
    }

    @Before("@annotation(com.realestate.aspect.AdminOnly)")
    public void checkAdminRole(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin) {
            log.warn("Non-admin user {} attempted to access admin method: {}",
                    authentication.getName(),
                    joinPoint.getSignature().getName());
            throw new UnauthorizedException("Admin access required");
        }
    }
}