package com.realestate.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RestController)")
    public void restControllerPointcut() {}

    @Pointcut("@annotation(org.springframework.stereotype.Service)")
    public void servicePointcut() {}

    @Pointcut("@annotation(org.springframework.stereotype.Repository)")
    public void repositoryPointcut() {}

    @Before("restControllerPointcut() || servicePointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("Entering method: {} with arguments: {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "restControllerPointcut() || servicePointcut()", 
                   returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.debug("Method {} returned: {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "restControllerPointcut() || servicePointcut()", 
                  throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method {}: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }

    @Around("@annotation(com.realestate.aspect.TrackExecutionTime)")
    public Object trackExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            stopWatch.stop();
            log.info("Method {} executed in {} ms",
                    joinPoint.getSignature().getName(),
                    stopWatch.getTotalTimeMillis());
        }
    }

    @Around("repositoryPointcut()")
    public Object logRepositoryAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Repository method called: {}", joinPoint.getSignature().getName());
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            log.debug("Repository method {} completed in {} ms",
                    joinPoint.getSignature().getName(),
                    stopWatch.getTotalTimeMillis());
            
            return result;
        } catch (Exception e) {
            log.error("Repository method {} failed: {}",
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        }
    }
}