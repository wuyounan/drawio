package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.query.DateStartAndEndQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.uasp.bmp.opm.application.AgentApplication;
import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;
import com.huigou.uasp.bmp.opm.impl.AgentApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.agent.AgentRepository;

@Service("agentApplicationProxy")
public class AgentApplicationProxy {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;
    
    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private AgentApplication agentApplication;

    void initProperties(AgentApplicationImpl agentApplicationImpl) {
        agentApplicationImpl.setAgentRepository(agentRepository);
        agentApplicationImpl.setGeneralRepository(generalRepository);
        agentApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        agentApplicationImpl.setCommonDomainService(commonDomainService);
    }

    private AgentApplication getAgentApplication() {
        if (agentApplication == null) {
          //  synchronized (AgentApplicationProxy.class) {
                if (agentApplication == null) {
                    AgentApplicationImpl agentApplicationImpl = coreApplicationFactory.getAgentApplication();
                    agentApplication = agentApplicationImpl;
                }
           // }
        }
        return agentApplication;
    }

    @Transactional
    public String saveAgent(Agent agent) {
        return getAgentApplication().saveAgent(agent);
    }

    public Agent loadAgent(String id) {
        return getAgentApplication().loadAgent(id);
    }

    public Agent loadValidAgent(String clientPsmId) {
        return getAgentApplication().loadValidAgent(clientPsmId);
    }

    @Transactional
    public void deleteAgents(List<String> ids) {
        getAgentApplication().deleteAgents(ids);
    }

    public Map<String, Object> slicedQueryAgents(DateStartAndEndQueryRequest queryRequest) {
        return getAgentApplication().slicedQueryAgents(queryRequest);
    }

    public Map<String, Object> slicedQueryAgentsByAgentId(DateStartAndEndQueryRequest queryRequest) {
        return getAgentApplication().slicedQueryAgentsByAgentId(queryRequest);
    }

    public Map<String, Object> slicedQueryAgentsByClientId(DateStartAndEndQueryRequest queryRequest) {
        return getAgentApplication().slicedQueryAgentsByClientId(queryRequest);
    }

    @Transactional
    public void deleteAgentProcs(String agentId, List<String> ids) {
        getAgentApplication().deleteAgentProcs(agentId, ids);
    }

    public Map<String, Object> slicedQueryAgentProcs(ParentIdQueryRequest queryRequest) {
        return getAgentApplication().slicedQueryAgentProcs(queryRequest);
    }

}
