package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.Operator;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.query.DateStartAndEndQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;
import com.huigou.uasp.bmp.opm.domain.model.agent.AgentProc;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.AgentApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("agent")
public class AgentController extends CommonController {

    private static final String AGENT_PAGE = "Agent";

    private static final String AGENT_DETAIL_PAGE = "AgentDetail";

    @Autowired
    private AgentApplicationProxy agentApplication;

    @Autowired
    private OrgApplicationProxy orgApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/agent/";
    }

    @RequiresPermissions("Agent:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "跳转到代理列表页面")
    public String forwardAgent() {
        return this.forward(AGENT_PAGE);
    }

    @RequiresPermissions("Agent:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加代理明细页面")
    public String showInsertAgent() {
        SDO params = this.getSDO();
        Operator opr = this.getOperator();

        params.putProperty("clientId", opr.getPersonMemberId());
        params.putProperty("clientName", opr.getName());
        params.putProperty("status", ValidStatus.ENABLED.getId());

        return forward(AGENT_DETAIL_PAGE, params);
    }

    @RequiresPermissions("Agent:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "添加代理")
    public String insertAgent() {
        SDO params = this.getSDO();

        this.internalSaveAgent(params);

        return success();
    }

    private void internalSaveAgent(SDO params) {
        String clientId = params.getString("clientId");
        String agentId = params.getString("agentId");

        Agent agent = params.toObject(Agent.class);
        List<AgentProc> agentProcs = params.getList("detailData", AgentProc.class);
        if (agent.isNew()) {
            agent.setCreator(Creator.newInstance());
        }

        Org clientPsm = this.orgApplication.loadOrg(clientId);
        Org agentPsm = this.orgApplication.loadOrg(agentId);

        agent.setClient(clientPsm);
        agent.setAgent(agentPsm);

        agentProcs = agent.removeDuplicateAgentProcs(agentProcs);
        agent.setInputDetails_(agentProcs);
        agent.addUpdateFields_(Agent.INPUT_DETAILS_FIELD_NAME, "client", "agent");

        this.agentApplication.saveAgent(agent);
    }

    @RequiresPermissions("Agent:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改代理明细页面")
    public String showUpdateAgent() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        Agent agent = this.agentApplication.loadAgent(id);

        this.putAttribute("clientId", agent.getClient().getId());
        this.putAttribute("clientName", agent.getClient().getName());
        this.putAttribute("agentId", agent.getAgent().getId());
        this.putAttribute("agentName", agent.getAgent().getName());

        return this.forward(AGENT_DETAIL_PAGE, agent);
    }

    @RequiresPermissions("Agent:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "修改代理")
    public String updateAgent() {
        SDO params = this.getSDO();
        this.internalSaveAgent(params);
        return success();
    }

    @RequiresPermissions("Agent:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除代理")
    public String deleteAgents() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.agentApplication.deleteAgents(ids);
        return success();
    }

    @RequiresPermissions("Agent:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询代理")
    public String slicedQueryAgents() {
        SDO params = this.getSDO();
        DateStartAndEndQueryRequest queryRequest = params.toQueryRequest(DateStartAndEndQueryRequest.class);
        Map<String, Object> data = this.agentApplication.slicedQueryAgents(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Agent:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询代理")
    public String slicedQueryAgentsByAgentId() {
        SDO params = this.getSDO();
        DateStartAndEndQueryRequest queryRequest = params.toQueryRequest(DateStartAndEndQueryRequest.class);
        Map<String, Object> data = this.agentApplication.slicedQueryAgentsByAgentId(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Agent:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询代理")
    public String slicedQueryAgentsByClientId() {
        SDO params = this.getSDO();
        DateStartAndEndQueryRequest queryRequest = params.toQueryRequest(DateStartAndEndQueryRequest.class);
        Map<String, Object> data = this.agentApplication.slicedQueryAgentsByClientId(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Agent:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询代理流程")
    public String slicedQueryAgentProcs() {
        SDO params = this.getSDO();
        String agentId = params.getString("agentId");
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        queryRequest.setParentId(agentId);
        Map<String, Object> data = this.agentApplication.slicedQueryAgentProcs(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Agent:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除代理流程")
    public String deleteAgentProcs() {
        SDO params = this.getSDO();
        String agentId = params.getString("agentId");
        List<String> ids = params.getStringList("ids");
        this.agentApplication.deleteAgentProcs(agentId, ids);
        return success();
    }

}
