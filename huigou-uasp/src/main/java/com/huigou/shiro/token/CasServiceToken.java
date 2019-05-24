package com.huigou.shiro.token;

import org.apache.shiro.cas.CasToken;

/**
 * 继承CasToken 增加属性service
 *
 * @author xx
 *         由于cas 认证需要绑定来源 service, service在ticketValidator.validate(ticket, service); 调用中使用
 */
public class CasServiceToken extends CasToken {

    private static final long serialVersionUID = -7551081938018103665L;

    private String service = null;

    public CasServiceToken(String ticket, String service) {
        super(ticket);
        this.service = service;
    }

    public String getService() {
        return service;
    }

}
