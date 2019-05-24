package com.huigou.uasp.bmp.opm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.DateStartAndEndQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.opm.application.AgentApplication;
import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;
import com.huigou.uasp.bmp.opm.repository.agent.AgentRepository;

public class AgentApplicationImpl extends BaseApplication implements AgentApplication {

    private AgentRepository agentRepository;

    public void setAgentRepository(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

   
    @Override
    public String saveAgent(Agent agent) {
        Assert.notNull(agent, "参数agent不能为空。");
        Agent dbAgent = this.commonDomainService.loadAndFillinProperties(agent, Agent.class);
        dbAgent.buildDetails();
        dbAgent.checkConstraints();
        dbAgent = agentRepository.save(dbAgent);
        return dbAgent.getId();
    }

    @Override
    public Agent loadAgent(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Agent agent = this.agentRepository.findOne(id);
        Assert.notNull(agent, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "代理"));
        return agent;
    }

    @Override
    public Agent loadValidAgent(String clientPsmId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "agent");
        String sql = queryDescriptor.getSqlByName("loadValid");
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("psmId", clientPsmId);

        @SuppressWarnings("unchecked")
        List<Agent> agents = this.generalRepository.query(sql, params);
        if (agents.size() > 0) {
            return agents.get(0);
        }
        return null;
    }

    @Override
    public void deleteAgents(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        List<Agent> agents = this.agentRepository.findAll(ids);
        Assert.isTrue(ids.size() == agents.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "代理"));

        for (Agent agent : agents) {
            Assert.isTrue(agent.canModify(), String.format("您不能删除“%s”创建的代理。", agent.getClient().getName()));
        }

        this.agentRepository.delete(agents);
    }

    @Override
    public void deleteAgentProcs(String agentId, List<String> ids) {
        Assert.hasText(agentId, "参数agentId不能为空。");
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        Agent agent = this.agentRepository.findOne(agentId);
        Assert.notNull(agent, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, agentId, "代理"));
        agent.removeDetails(ids);

        this.agentRepository.save(agent);
    }

    private Map<String, Object> internalSlicedQueryAgents(DateStartAndEndQueryRequest queryRequest, String sqlName) {
        Operator operator = ThreadLocalUtil.getOperator();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "agent");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, sqlName);
        queryModel.putStartWithParam("personId", operator.getUserId());
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public Map<String, Object> slicedQueryAgents(DateStartAndEndQueryRequest queryRequest) {
        return internalSlicedQueryAgents(queryRequest, "query");
    }

    @Override
    public Map<String, Object> slicedQueryAgentsByAgentId(DateStartAndEndQueryRequest queryRequest) {
        return internalSlicedQueryAgents(queryRequest, "queryByAgentId");
    }

    @Override
    public Map<String, Object> slicedQueryAgentsByClientId(DateStartAndEndQueryRequest queryRequest) {
        return internalSlicedQueryAgents(queryRequest, "queryByClientId");
    }

    @Override
    public Map<String, Object> slicedQueryAgentProcs(ParentIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "agent");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest, "queryProc");
    }

}
