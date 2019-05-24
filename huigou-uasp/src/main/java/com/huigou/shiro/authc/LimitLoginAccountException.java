package com.huigou.shiro.authc;

import org.apache.shiro.authc.DisabledAccountException;

public class LimitLoginAccountException extends DisabledAccountException {

    private static final long serialVersionUID = -476313062328245608L;

    /**
     * Creates a new LimitLoginAccountException.
     */
    public LimitLoginAccountException() {
        super();
    }

    /**
     * Constructs a new LimitLoginAccountException.
     *
     * @param message
     *            the reason for the exception
     */
    public LimitLoginAccountException(String message) {
        super(message);
    }

    /**
     * Constructs a new LimitLoginAccountException.
     *
     * @param cause
     *            the underlying Throwable that caused this exception to be thrown.
     */
    public LimitLoginAccountException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new LimitLoginAccountException.
     *
     * @param message
     *            the reason for the exception
     * @param cause
     *            the underlying Throwable that caused this exception to be thrown.
     */
    public LimitLoginAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
