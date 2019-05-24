package com.huigou.uasp.exception;

import com.huigou.context.MessageSourceContext;

/**
 * 错误请求异常
 * 
 * @author
 */
public class VisitErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VisitErrorException() {
        super(MessageSourceContext.getMessageAsDefault("common.visit.exception"));
    }

    public VisitErrorException(String message) {
        super(message);
    }

    public VisitErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public VisitErrorException(Throwable throwable) {
        super(throwable);
    }
}
