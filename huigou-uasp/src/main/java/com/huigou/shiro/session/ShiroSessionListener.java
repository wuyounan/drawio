package com.huigou.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huigou.cache.service.ICache;
import com.huigou.util.Constants;

/**
 * 类ShiroSessionListener的功能描述:
 * 发现用户登出后，Session没有从Redis中销毁，虽然当前重新new了一个，但会对统计带来干扰，通过SessionListener解决这个问题
 * 
 * @auther xiex
 * @date 2018-01-25 11:45:11
 */
public class ShiroSessionListener extends SessionListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

    private AbstractSessionDAO sessionDAO;

    private ICache icache;

    public void setSessionDAO(AbstractSessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    public void setIcache(ICache icache) {
        this.icache = icache;
    }

    @Override
    public void onStart(Session session) {
        // 会话创建时触发
        logger.debug("ShiroSessionListener session {} 被创建", session.getId());
    }

    @Override
    public void onStop(Session session) {
        this.deleteSession(session);
        // 会话被停止时触发
        logger.debug("ShiroSessionListener session {} 被销毁", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        this.deleteSession(session);
        // 会话过期时触发
        logger.debug("ShiroSessionListener session {} 过期", session.getId());
    }

    private void deleteSession(Session session) {
        String ticket = (String) session.getAttribute(Constants.SERVICE_TICKET);
        if (ticket != null && icache != null) {
            logger.debug("remove serviceTicket: " + ticket);
            icache.delete(ticket);
        }
        sessionDAO.delete(session);
    }
}
