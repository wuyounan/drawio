package com.huigou.uasp.log.domain.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.shiro.SecurityUtils;

import com.huigou.context.Operator;

/**
 * 在线会话
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OnlineSession")
public class OnlineSession extends AbstractSession {

    private static final long serialVersionUID = 5366437739338037982L;

    public static OnlineSession newInstance(Operator operator) {
        OnlineSession result = new OnlineSession();

        String sessionId = String.valueOf(SecurityUtils.getSubject().getSession().getId());
        result.setId(sessionId);

        result.fillDefaultValues(operator);

        return result;
    }

}
