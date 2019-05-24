package com.huigou.exception;

/**
 * 应用程序异常
 * 
 * @author
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApplicationException(Throwable throwable) {
        super(throwable);
    }
}
