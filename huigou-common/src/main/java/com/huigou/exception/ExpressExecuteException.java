package com.huigou.exception;

/**
 *  表达式执行异常
 * @author Admin
 *
 */
public class ExpressExecuteException extends ApplicationException {

    private static final long serialVersionUID = -6430038434806350647L;

    public ExpressExecuteException() {
    }

    public ExpressExecuteException(String message) {
        super(message);
    }

    public ExpressExecuteException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ExpressExecuteException(Throwable throwable) {
        super(throwable);
    }
}
