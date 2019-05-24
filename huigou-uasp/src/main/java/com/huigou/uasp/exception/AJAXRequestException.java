package com.huigou.uasp.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 程序异常
 * 
 * @author
 */
public class AJAXRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> errorParam;

    public AJAXRequestException(String message) {
        super(message);
        errorParam = new HashMap<>();
    }

    public AJAXRequestException(String message, Throwable throwable) {
        super(message, throwable);
        errorParam = new HashMap<>();
    }

    public AJAXRequestException(Throwable throwable) {
        super(throwable);
        errorParam = new HashMap<>();
    }

    public void put(String key, Object obj) {
        this.errorParam.put(key, obj);
    }

    public void putAll(Map<String, Object> map) {
        this.errorParam.putAll(map);
    }

    public Map<String, Object> getErrorParam() {
        return errorParam;
    }

}
