package com.huigou.uasp.log.application.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.context.TmspmConifg;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.LoginStatus;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.log.application.LoginLogApplication;
import com.huigou.uasp.log.domain.model.HistoricSession;
import com.huigou.uasp.log.domain.model.LogoutKind;
import com.huigou.uasp.log.domain.model.OnlineSession;
import com.huigou.uasp.log.domain.query.LoginQueryRequest;
import com.huigou.uasp.log.domain.view.OnlineSessionSpecification;
import com.huigou.uasp.log.repository.HistoricSessionRepository;
import com.huigou.uasp.log.repository.OnlineSessionRepository;
import com.huigou.uasp.log.util.BizLogUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

@Service("loginLogApplication")
public class LoginLogApplicationImpl extends BaseApplication implements LoginLogApplication {

    @Autowired
    private OnlineSessionRepository onlineSessionRepository;

    @Autowired
    private TMAuthorizeRepository tmAuthorizeRepository;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Autowired
    private HistoricSessionRepository historicSessionRepository;

    @Override
    @Transactional
    public void saveOnlineSession(OnlineSession onlineSession) {
        Assert.notNull(onlineSession, "参数onlineSession不能为空。");

        HistoricSession historicSession = this.historicSessionRepository.findOne(onlineSession.getId());
        if (historicSession == null) {
            historicSession = new HistoricSession();
            historicSession.setStatus(LoginStatus.SUCCESS.getId());
        }

        ClassHelper.copyProperties(onlineSession, historicSession);
        this.onlineSessionRepository.save(onlineSession);
        this.historicSessionRepository.save(historicSession);
    }

    @Override
    public List<OnlineSession> queryAllOnlineSessions() {
        return onlineSessionRepository.findAll();
    }

    /**
     * 统计在线人数
     * 
     * @return
     */
    @Override
    public Long countOnlinePersons() {
        return (long) ContextUtil.getSessionDAO().getActiveSessions().size();
        // return onlineSessionRepository.count();
    }

    @Override
    public Page<OnlineSession> sliceQueryOnlineSessions(String fullId, String personName, Date beginDate, Date endDate, String ip, Pageable pageable) {
        OnlineSessionSpecification spec = new OnlineSessionSpecification(fullId, personName, beginDate, endDate, ip);
        return this.onlineSessionRepository.findAll(spec, pageable);
    }

    @Override
    public Map<String, Object> sliceQueryHistoricSessions(LoginQueryRequest queryRequest) {
        if (StringUtil.isBlank(queryRequest.getOperatorRoleKindId()) || StringUtil.isBlank(queryRequest.getPersonMemberId())) {
            return null;
        }
        Map<String, String> map = BizLogUtil.getLogQueryCriteria(queryRequest.getOperatorRoleKindId(), tmAuthorizeRepository, queryRequest.getPersonMemberId(),
                                                              tmspmConifg.isEnableTspm());
        String targetFullId = map.get("targetFullId");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "log");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        if (targetFullId.indexOf("*") < 0 && StringUtil.isNotBlank(targetFullId)) {
            StringBuilder sb = new StringBuilder(queryModel.getSql());
            String[] targetFullIds = targetFullId.split(",");
            int i = 0;
            String paramName;
            sb.append(" and (");
            for (String item : targetFullIds) {
                paramName = String.format(":targetFullId%s", i);
                if (i == 0) {
                    sb.append(String.format(" Full_Id like %s", paramName));
                } else {
                    sb.append(String.format(" or Full_Id like %s", paramName));
                }
                queryModel.putStartWithParam(String.format("targetFullId%s", i), item);
                i++;
            }
            sb.append(")");
            queryModel.setSql(sb.toString());
        }
        if (queryRequest.isQueryInvalidLoginName()) {
            StringBuilder sb = new StringBuilder(queryModel.getSql());
            sb.append(" and status != :status and organ_id is null ");
            queryModel.putParam("status", LoginStatus.SUCCESS.getId());
            queryModel.setSql(sb.toString());
        }
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    @Transactional
    public void logout(Session session, LogoutKind logoutKind) {
        Assert.notNull(session, "参数session不能为空。");

        String sessionId = session.getId().toString();

        OnlineSession onlineSession = this.onlineSessionRepository.findOne(sessionId);
        if (onlineSession != null) {
            this.onlineSessionRepository.delete(onlineSession);

            HistoricSession historicSession = this.historicSessionRepository.findOne(sessionId);
            if (historicSession != null) {
                historicSession.setLogoutDate(new Date());
                historicSession.setLogoutKindId(logoutKind.name());
                Operator operator = (Operator) session.getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
                historicSession.fillLogoutPersonMember(operator);
                this.historicSessionRepository.save(historicSession);
            }
        }
    }

    @Override
    public void validateSession() {
        List<OnlineSession> sessions = this.queryAllOnlineSessions();
        for (OnlineSession session : sessions) {
            try {

                Session s = ContextUtil.getSessionDAO().readSession(session.getId());
                if (s == null) {
                    onlineSessionRepository.delete(session.getId());
                }
            } catch (Exception e) {
                onlineSessionRepository.delete(session.getId());
            }
        }
    }

    @Override
    public HistoricSession loadHistoricSession(String id) {
        Assert.hasText(id, "参数ID不能为空。");
        return this.historicSessionRepository.findOne(id);
    }

    @Override
    public void saveHistoricSession(HistoricSession historicSession) {
        Assert.notNull(historicSession, "参数historicSession不能为空。");
        this.historicSessionRepository.save(historicSession);
    }
}
