package com.huigou.data.domain;

/**
 * 实体被引用异常
 * @author gongmm
 *
 */
public class EntityIsReferencedException extends RuntimeException {


    private static final long serialVersionUID = -2033357935689935799L;

    public EntityIsReferencedException() {
        super();
    }

    public EntityIsReferencedException(String msg) {
        super(msg);
    }

    public EntityIsReferencedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EntityIsReferencedException(Throwable cause) {
        super(cause);
    }
}
