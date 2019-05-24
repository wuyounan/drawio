package com.huigou.exception;

import java.io.PrintStream;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3252564205146658953L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Exception exception) {
        this(exception, exception.getMessage());
    }

    public NotFoundException(Exception exception, String message) {
        super(message);
        this.exception = exception;
    }

    public NotFoundException(Exception exception, String message, boolean flag) {
        this(exception, message);
        setFatal(flag);
    }

    public boolean isFatal() {
        return fatal;
    }

    public void setFatal(boolean flag) {
        fatal = flag;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (exception != null) exception.printStackTrace();
    }

    public void printStackTrace(PrintStream printstream) {
        super.printStackTrace(printstream);
        if (exception != null) exception.printStackTrace(printstream);
    }

    public String toString() {
        if (exception != null) return super.toString() + " wraps: [" + exception.toString() + "]";
        else
            return super.toString();
    }

    protected Exception exception;

    protected boolean fatal;
}
