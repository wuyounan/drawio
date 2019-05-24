package com.huigou.data.exception;

import com.huigou.exception.ApplicationException;

/**
 * 实体执行异常
 * 
 * @author Administrator
 */
public class EntityExecutorException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public EntityExecutorException() {
    }

    public EntityExecutorException(String message) {
        super(message);
    }

    public EntityExecutorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EntityExecutorException(Throwable throwable) {
        super(throwable);
    }
}
