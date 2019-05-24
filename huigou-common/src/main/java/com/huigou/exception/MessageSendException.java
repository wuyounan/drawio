package com.huigou.exception;

/**
 * 消息发送异常
 * 
 * @author
 */
public class MessageSendException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MessageSendException() {
    }

    public MessageSendException(String message) {
        super(message);
    }

    public MessageSendException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MessageSendException(Throwable throwable) {
        super(throwable);
    }
}
