package com.huigou.uasp.bmp.opm.proxy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.context.Operator;
import com.huigou.uasp.bmp.opm.application.AuthenticationApplication;
import com.huigou.uasp.bmp.opm.impl.AuthenticationApplicationImpl;

@Service("authenticationApplicationProxy")
public class AuthenticationApplicationProxy {

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private AuthenticationApplication authenticationApplication;

    private AuthenticationApplication getAuthenticationApplication() {
        if (authenticationApplication == null) {
           // synchronized (AuthenticationApplicationProxy.class) {
                if (authenticationApplication == null) {
                    AuthenticationApplicationImpl authenticationApplicationImpl = coreApplicationFactory.getAuthenticationApplication();
                    authenticationApplication = authenticationApplicationImpl;
                }
          //  }
        }
        return authenticationApplication;
    }

    public void setOperatorContext(Operator operator, String personMemberId) {
        getAuthenticationApplication().setOperatorContext(operator, personMemberId);

    }

    public Map<String, Object> login(String loginName, String password) {
        return getAuthenticationApplication().login(loginName, password);
    }

    public Operator createOperatorByPersonMemberId(String personMemberId) {
        return getAuthenticationApplication().createOperatorByPersonMemberId(personMemberId);
    }

    public Operator createOperatorByLoginName(String loginName) {
        return getAuthenticationApplication().createOperatorByLoginName(loginName);
    }

    public Map<String, Object> loginFromErp(String loginName, String password) {
        return getAuthenticationApplication().loginFromErp(loginName, password);
    }

    public Map<String, Object> loginFromErp(String loginName) {
        return getAuthenticationApplication().loginFromErp(loginName);
    }

}
