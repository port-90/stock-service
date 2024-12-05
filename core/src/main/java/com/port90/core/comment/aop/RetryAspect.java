package com.port90.core.comment.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object retry(final ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        for (int i = 0; i < retry.maxRetries(); i++) {
            try {
                return joinPoint.proceed();
            } catch (OptimisticLockingFailureException e) {
                if (i == retry.maxRetries() - 1) {
                    throw e;
                }
                Thread.sleep(retry.retryDelay());
            }
        }
        return null;
    }
}
