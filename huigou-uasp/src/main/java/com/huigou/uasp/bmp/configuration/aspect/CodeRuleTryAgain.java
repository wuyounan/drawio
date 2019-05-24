package com.huigou.uasp.bmp.configuration.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Component;

import com.huigou.uasp.annotation.CodeRuleIsTryAgain;

/**
 * 单据编号取号失败重试
 * 
 * @author xx
 */
@Aspect
@Component
public class CodeRuleTryAgain implements Ordered {

    private static final int DEFAULT_MAX_RETRIES = 2;

    private int maxRetries = DEFAULT_MAX_RETRIES;

    private int order = 1;

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Around(value = "@annotation(codeRuleIsTryAgain)")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp, CodeRuleIsTryAgain codeRuleIsTryAgain) throws Throwable {
        int numAttempts = 0;
        PessimisticLockingFailureException lockFailureException = null;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            } catch (PessimisticLockingFailureException ex) {
                lockFailureException = ex;
            }
        } while (numAttempts <= this.maxRetries);
        throw lockFailureException;
    }

}