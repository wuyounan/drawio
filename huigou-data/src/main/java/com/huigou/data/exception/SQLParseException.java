package com.huigou.data.exception;

import com.huigou.exception.ApplicationException;

public class SQLParseException extends ApplicationException {

    private static final long serialVersionUID = 162660618159933312L;

    public SQLParseException() {
    }

    public SQLParseException(String message) {
        super(message);
    }

    public SQLParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SQLParseException(Throwable throwable) {
        super(throwable);
    }
}
