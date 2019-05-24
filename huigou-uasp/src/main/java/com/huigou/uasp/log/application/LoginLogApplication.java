package com.huigou.uasp.log.application;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huigou.uasp.log.domain.model.HistoricSession;
import com.huigou.uasp.log.domain.model.LogoutKind;
import com.huigou.uasp.log.domain.model.OnlineSession;
import com.huigou.uasp.log.domain.query.LoginQueryRequest;

/**
 * 登录日志
 * 
 * @author gongmm
 */
public interface LoginLogApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/log.xml";

    /**
     * 保存在线会话
     * 
     * @param onlineSession
     *            在线会话
     */
    void saveOnlineSession(OnlineSession onlineSession);

    /**
     * 查询所有在线会话
     * 
     * @return
     */
    List<OnlineSession> queryAllOnlineSessions();

    /**
     * 统计在线人数
     * 
     * @return
     */
    Long countOnlinePersons();

    /**
     * 分页查询在线人员
     * 
     * @param fullId
     *            组织ID全路径
     * @param personName
     *            人员
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @param ip
     *            IP
     * @param pageable
     *            分页
     * @return
     */
    Page<OnlineSession> sliceQueryOnlineSessions(String fullId, String personName, Date beginDate, Date endDate, String ip, Pageable pageable);

    /**
     * 分页查询登录
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> sliceQueryHistoricSessions(LoginQueryRequest queryRequest);

    /**
     * 退出系统
     * 
     * @param session
     *            会话
     * @param logoutKind
     *            退出系统方式
     */
    void logout(Session session, LogoutKind logoutKind);

    /**
     * 验证会话
     */
    void validateSession();

    /**
     * 加载历史会话
     * 
     * @param id
     *            会话ID
     * @return
     */
    HistoricSession loadHistoricSession(String id);

    /**
     * 保存历史会话
     * 
     * @param historicSession
     *            历史会话实体
     */
    void saveHistoricSession(HistoricSession historicSession);
}
