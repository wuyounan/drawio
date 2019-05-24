package com.huigou.uasp.exception;

/**
 * App认证异常
 * 
 * @author
 */
public class AppAuthRequestException extends RuntimeException {

    private static final long serialVersionUID = 2186753625400729468L;

    public AppAuthRequestException() {
    }

    public AppAuthRequestException(String message) {
        super(message);
    }

    public AppAuthRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AppAuthRequestException(Throwable throwable) {
        super(throwable);
    }
}
