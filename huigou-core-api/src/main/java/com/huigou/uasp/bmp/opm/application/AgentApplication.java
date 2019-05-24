package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.DateStartAndEndQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;

/**
 * 代理应用
 * 
 * @author gongmm
 */
public interface AgentApplication {

    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 保存代理
     * 
     * @param agent
     *            代理对象
     * @return
     *         代理ID
     */
    String saveAgent(Agent agent);

    /**
     * 加载代理
     * 
     * @param id
     *            代理ID
     * @return
     *         代理实体
     */
    Agent loadAgent(String id);

    /**
     * 加载有效的代理
     * 
     * @param clientPsmId
     *            委托人人员成员ID
     * @return
     */
    Agent loadValidAgent(String clientPsmId);
    
    /**
     * 删除代理
     * 
     * @param ids
     *            代理ID列表
     */
    void deleteAgents(List<String> ids);

    /**
     * 分页查询代理
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryAgents(DateStartAndEndQueryRequest queryRequest);

    /**
     * 分页查询代理人的委托
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryAgentsByAgentId(DateStartAndEndQueryRequest queryRequest);

    /**
     * 分页查询委托人的代理
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryAgentsByClientId(DateStartAndEndQueryRequest queryRequest);

    /**
     * 删除代理流程
     * 
     * @param agentId
     *            代理ID
     * @param ids
     *            代理流程ID列表
     */
    void deleteAgentProcs(String agentId, List<String> ids);

    /**
     * 分页查询代理流程
     * 
     * @param agentId
     * @param queryModel
     * @return
     */
    Map<String, Object> slicedQueryAgentProcs(ParentIdQueryRequest queryRequest);

}
