package com.huigou.uasp.bmp.securitypolicy.exception;

import org.apache.shiro.authc.AuthenticationException;

public class SecurityPolicyException extends AuthenticationException {

    private static final long serialVersionUID = -5307749466101510236L;

    public SecurityPolicyException() {
        super();
    }

    public SecurityPolicyException(String message) {
        super(message);
    }

    public SecurityPolicyException(Throwable cause) {
        super(cause);
    }

    public SecurityPolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}
