package com.huigou.uasp.exception;

import com.huigou.context.MessageSourceContext;

/**
 * 程序异常
 * 
 * @author
 */
public class AuthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthException() {
        super(MessageSourceContext.getMessageAsDefault("common.auth.exception"));
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AuthException(Throwable throwable) {
        super(throwable);
    }
}
