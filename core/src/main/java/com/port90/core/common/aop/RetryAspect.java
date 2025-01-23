package com.port90.core.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Integer.MAX_VALUE - 1)
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        for (int i = 0; i < retry.maxRetries(); i++) {
            try {
                if (i != 0) {
                    log.info("충돌로 인한 Retry ({}번째)", i);
                }
                return joinPoint.proceed();
            } catch (ObjectOptimisticLockingFailureException e) {
                if (i == retry.maxRetries() - 1) {
                    break;
                }
                Thread.sleep(retry.delay());
            }
        }
        throw new RuntimeException("Retry Failed");
    }
}
