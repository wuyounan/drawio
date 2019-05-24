package com.huigou.uasp.tool.remind.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.tool.remind.domain.model.MessageRemind;
import com.huigou.uasp.tool.remind.domain.query.MessageRemindQueryRequest;

/**
 * 消息提醒配置后台
 * 
 * @author xx
 * @date 2017-02-15 14:39
 */
public interface MessageRemindApplication {

    /**
     * 保存 系统消息提醒配置表
     * 
     * @author xx
     * @param params
     */
    public String save(MessageRemind messageRemind);

    /**
     * 加载 系统消息提醒配置表
     * 
     * @author xx
     * @return SDO
     */
    public MessageRemind load(String id);

    /**
     * 删除 系统消息提醒配置表
     * 
     * @author xx
     */
    public void delete(String[] ids);

    /**
     * 查询 系统消息提醒配置表
     * 
     * @author xx
     * @return SDO
     */
    public Map<String, Object> slicedQuery(MessageRemindQueryRequest queryRequest);

    public void updateStatus(List<String> ids, Integer status);

    public void updateFolderId(List<String> ids, String folderId);

    public void updateSequence(Map<String, Integer> params);

    /**
     * 测试提醒执行结果
     * 
     * @author
     * @param id
     *            void
     */
    public List<Object> testParseRemindFun(String id);

    /**
     * 查询用户授权的提醒信息
     * 
     * @param personId
     * @return
     */
    public List<Object> queryRemindByPersonId(String personId);
}
