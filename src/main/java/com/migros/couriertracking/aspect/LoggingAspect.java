package com.migros.couriertracking.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.migros.couriertracking.service.*.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        log.info("Method called: [{}] with args: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());

        Object result = null;
        try {
            result = joinPoint.proceed();
            log.info("Method executed successfully: [{}], returned: {}",
                    joinPoint.getSignature().getName(), result);
        } catch (Exception e) {
            log.error("Exception in method: [{}], with message: {}",
                    joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info("Method execution finished: [{}], duration: {} ms", joinPoint.getSignature().getName(), duration);

        return result;
    }

}
