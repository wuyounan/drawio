package com.huigou.uasp.exception;

import com.huigou.context.MessageSourceContext;

/**
 * 验证码输入错误
 * 
 * @author xx
 */
public class VerifyCodeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VerifyCodeException() {
        super(MessageSourceContext.getMessageAsDefault("common.verifyCode.exception"));
    }

    public VerifyCodeException(String message) {
        super(message);
    }

    public VerifyCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public VerifyCodeException(Throwable throwable) {
        super(throwable);
    }
}
