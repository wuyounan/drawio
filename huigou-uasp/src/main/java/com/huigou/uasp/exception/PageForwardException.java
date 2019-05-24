package com.huigou.uasp.exception;

import com.huigou.exception.ApplicationException;

public class PageForwardException extends ApplicationException {

    private static final long serialVersionUID = 4409797221623345038L;

    public PageForwardException() {
    }

    public PageForwardException(String message) {
        super(message);
    }

    public PageForwardException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PageForwardException(Throwable throwable) {
        super(throwable);
    }
}
