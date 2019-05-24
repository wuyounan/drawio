package com.huigou.uasp.bmp.opm;

import java.util.HashMap;
import java.util.Map;

import com.huigou.exception.ApplicationException;

/**
 * 登录状态
 * 
 * @author gongmm
 */
public enum LoginStatus {
    UNKNOWN_ERROR(0, "未知错误"),
    SUCCESS(1, "验证成功"),
    USER_NOT_EXIST_OR_PASSWORD_ERROR(2, "用户或密码错误"),
    USER_DISABLED(3, "用户已禁用"),
    USER_LOGIC_DELETE(4, "用户已删除"),
    USER_LOCKED(5, "用户已锁定"),
    LOGIN_LIMIT(6, "你不能在当前机器上登录"),
    SECURITY_POLICY(7, "无效或未配置安全策略");

    private final int id;

    private final String message;

    private String additionMessage;

    private LoginStatus(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public static LoginStatus fromNumber(int id) {
        switch (id) {
        case 0:
            return UNKNOWN_ERROR;
        case 1:
            return SUCCESS;
        case 2:
            return USER_NOT_EXIST_OR_PASSWORD_ERROR;
        case 3:
            return USER_DISABLED;
        case 4:
            return USER_LOGIC_DELETE;
        case 5:
            return USER_LOCKED;
        case 6:
            return LOGIN_LIMIT;
        case 7:
            return SECURITY_POLICY;
        default:
            throw new ApplicationException(String.format("无效的登录状态“%s”。", id));
        }
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public Integer getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getAdditionMessage() {
        return additionMessage;
    }

    public void setAdditionMessage(String additionMessage) {
        this.additionMessage = additionMessage;
    }

    public Map<String, Object> getLoginStatus() {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("status", id);
        result.put("message", getMessage());
        return result;
    }
}
