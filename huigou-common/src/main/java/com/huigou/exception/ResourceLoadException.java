package com.huigou.exception;

public class ResourceLoadException extends ApplicationException {

    private static final long serialVersionUID = -3014655199467570421L;

    public ResourceLoadException() {
    }

    public ResourceLoadException(String message) {
        super(message);
    }

    public ResourceLoadException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ResourceLoadException(Throwable throwable) {
        super(throwable);
    }
}
