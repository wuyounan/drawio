package com.huigou.data.domain;

/**
 * 属性重复异常
 * 
 * @author gongmm
 */
public class PropertyDuplicateException extends RuntimeException {

    private static final long serialVersionUID = -3868448906899575968L;

    public PropertyDuplicateException() {
        super();
    }

    public PropertyDuplicateException(String msg) {
        super(msg);
    }

    public PropertyDuplicateException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PropertyDuplicateException(Throwable cause) {
        super(cause);
    }
}
