package com.huigou.uasp.bpm.configuration.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bpm.configuration.application.ApprovalRuleApplication;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalHandlerKind;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandler;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerAssist;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerGroup;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerUIElmentPermission;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleScope;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.uasp.bpm.configuration.domain.query.ProcApprovalElementDesc;
import com.huigou.uasp.bpm.managment.application.ProcDefinitionApplication;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("approvalRule")
public class ApprovalRuleController extends CommonController {

    @Autowired
    private ApprovalRuleApplication approvalRuleApplication;

    @Autowired
    private ProcDefinitionApplication procDefinitionApplication;

    protected String getPagePath() {
        return "/system/configtool/approvalRule/";
    }

    @RequiresPermissions("ApprovalElement:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到审批要素配置列表页面")
    public String forwardApprovalElement() {
        return forward("ApprovalElement");
    }

    @RequiresPermissions("ApprovalElement:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询审批要素配置")
    public String slicedQueryApprovalElements() {
        SDO params = this.getSDO();
        CodeAndNameQueryRequest queryRequest = params.toQueryRequest(CodeAndNameQueryRequest.class);
        Map<String, Object> data = this.approvalRuleApplication.slicedQueryApprovalElements(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("ApprovalElement:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加审批要素配置明细页面")
    public String showInsertApprovalElement() {
        Integer sequence = this.approvalRuleApplication.getApprovalElementNextSequence();

        this.putAttribute("kindList", ApprovalElement.Kind.getData());
        this.putAttribute("kindId", 1);
        // this.putAttribute("dataSourceList",
        // ApprovalElement.DataSource.getData());
        // this.putAttribute("dataSourceId",
        // ApprovalElement.DataSource.ORG.getId());
        this.putAttribute("sequence", sequence);

        return forward("ApprovalElementDetail");
    }

    @RequiresPermissions("ApprovalElement:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改审批要素配置明细页面")
    public String showUpdateApprovalElement() {
        SDO params = this.getSDO();
        String id = params.getString("id");

        this.putAttribute("kindList", ApprovalElement.Kind.getData());

        ApprovalElement approvalElement = approvalRuleApplication.loadApprovalElement(id);
        return forward("ApprovalElementDetail", approvalElement);
    }

    @RequiresPermissions("ApprovalElement:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加审批要素配置")
    public String insertApprovalElement() {
        SDO params = this.getSDO();
        ApprovalElement approvalElement = params.toObject(ApprovalElement.class);
        String id = this.approvalRuleApplication.saveApprovalElement(approvalElement);
        return success(id);
    }

    @RequiresPermissions("ApprovalElement:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改审批要素配置")
    public String updateApprovalElement() {
        SDO params = this.getSDO();
        ApprovalElement approvalElement = params.toObject(ApprovalElement.class);
        approvalRuleApplication.saveApprovalElement(approvalElement);
        return success();
    }

    @RequiresPermissions("ApprovalElement:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改审批要素配置排序号")
    public String updateApprovalElementsSequence() {
        Map<String, Integer> approvalElements = this.getSDO().getStringMap("data");
        approvalRuleApplication.updateApprovalElementsSequence(approvalElements);
        return success();
    }

    @RequiresPermissions("ApprovalElement:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除审批要素配置")
    public String deleteApprovalElement() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        approvalRuleApplication.deleteApprovalElements(ids);
        return success();
    }

    @RequiresPermissions("ApprovalHandlerKind:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到审批处理人类别列表页面")
    public String forwardApprovalHandlerKind() {
        return forward("ApprovalHandlerKind");
    }

    @RequiresPermissions("ApprovalHandlerKind:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询审批处理人类别")
    public String slicedQueryApprovalHandlerKinds() {
        SDO params = this.getSDO();
        CodeAndNameQueryRequest queryRequest = params.toQueryRequest(CodeAndNameQueryRequest.class);
        Map<String, Object> data = approvalRuleApplication.slicedQueryApprovalHandlerKinds(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("ApprovalHandlerKind:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加审批处理人类别明细页面")
    public String showInsertApprovalHandlerKind() {
        Integer sequence = approvalRuleApplication.getApprovalHandlerKindNextSequence();

        this.putAttribute("dataSourceList", ApprovalHandlerKind.DataSource.getData());
        this.putAttribute("dataSourceId", ApprovalHandlerKind.DataSource.ORG.getId());
        this.putAttribute("sequence", sequence);

        return forward("ApprovalHandlerKindDetail");
    }

    @RequiresPermissions("ApprovalHandlerKind:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改审批处理人类别明细页面")
    public String showUpdateApprovalHandlerKind() {
        SDO params = this.getSDO();
        String id = params.getString("id");

        ApprovalHandlerKind approvalHandlerKind = approvalRuleApplication.loadApprovalHandlerKind(id);

        this.putAttribute("dataSourceList", ApprovalHandlerKind.DataSource.getData());
        return forward("ApprovalHandlerKindDetail", approvalHandlerKind);
    }

    @RequiresPermissions("ApprovalHandlerKind:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加审批处理人类别")
    public String insertApprovalHandlerKind() {
        SDO params = this.getSDO();
        ApprovalHandlerKind approvalHandlerKind = params.toObject(ApprovalHandlerKind.class);
        String id = approvalRuleApplication.saveApprovalHandlerKind(approvalHandlerKind);
        return success(id);
    }

    @RequiresPermissions("ApprovalHandlerKind:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改审批处理人类别")
    public String updateApprovalHandlerKind() {
        SDO params = this.getSDO();
        ApprovalHandlerKind approvalHandlerKind = params.toObject(ApprovalHandlerKind.class);
        approvalRuleApplication.saveApprovalHandlerKind(approvalHandlerKind);
        return success();
    }

    @RequiresPermissions("ApprovalHandlerKind:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改审批处理人类别排序号")
    public String updateApprovalHandlerKindsSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> approvalHandlerKinds = params.getStringMap("data");
        approvalRuleApplication.updateApprovalHandlerKindsSequence(approvalHandlerKinds);
        return success();
    }

    @RequiresPermissions("ApprovalHandlerKind:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除审批处理人类别")
    public String deleteApprovalHandlerKind() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        approvalRuleApplication.deleteApprovalHandlerKinds(ids);
        return success();
    }

    @RequiresPermissions("ProcApprovalElementConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到流程审批要素配置列表页面")
    public String forwardProcApprovalElement() {
        return forward("ProcApprovalElement");
    }

    @RequiresPermissions("ProcApprovalElementConfig:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加流程审批要素配置")
    public String insertProcApprovalElement() {
        SDO params = this.getSDO();
        String procId = params.getString("procKey");
        String procUnitId = params.getString("procUnitId");
        List<String> approvalElementIds = params.getStringList("approvalElementIds");
        approvalRuleApplication.saveProcApprovalElement(procId, procUnitId, approvalElementIds);
        return success();
    }

    @RequiresPermissions("ProcApprovalElementConfig:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除流程审批要素配置")
    public String deleteProcApprovalElement() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        approvalRuleApplication.deleteProcApprovalElements(ids);
        return success();
    }

    @RequiresPermissions("ProcApprovalElementConfig:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改流程审批要素配置排序号")
    public String updateProcApprovalElementSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> approvalElements = params.getStringMap("data");
        approvalRuleApplication.updateProcApprovalElementSequence(approvalElements);
        return success();
    }

    @RequiresPermissions("ProcApprovalElementConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程审批要素配置")
    public String queryProcApprovalElement() {
        SDO params = this.getSDO();
        String procId = params.getString("procKey");
        String procUnitId = params.getString("procUnitId");
        List<ProcApprovalElementDesc> data = this.approvalRuleApplication.queryProcApprovalElements(procId, procUnitId);
        return this.packGridDataAndResult(data);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到流程审批规则配置列表页面")
    public String forwardApprovalRule() {
        this.putAttribute("nodeKindList", ApprovalRule.NodeKind.getData());
        this.putAttribute("statusList", ValidStatus.getData());
        this.putAttribute("operatorKindList", ApprovalRuleElement.OperatorKind.getData());
        return forward("ApprovalRule");
    }

    // public String loadApprovalRule() {
    // SDO params = this.getSDO();
    // String id = params.getString("id");
    // ApprovalRule approvalRule = this.approvalRuleApplication.loadApprovalRule(id);
    // return toResult(approvalRule);
    // }
    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程审批规则配置")
    public String queryApprovalRule() {
        SDO params = this.getSDO();
        try {
            String orgId = params.getString("orgId");
            String procId = params.getString("procId");
            String procUnitId = params.getString("procUnitId");
            String procUnitName = params.getString("procName") + "." + params.getString("procUnitName");
            String parentId = params.getString("parentId");

            Map<String, Object> data = this.approvalRuleApplication.queryApprovalRules(orgId, procId, procUnitId, procUnitName, parentId);
            return toResult(data);
        } catch (Exception e) {
            return error(e);
        }
    }

    @RequiresPermissions("ProcApprovalRuleConfig:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加流程审批规则配置明细页面")
    public String showInsertApprovalRule() {
        this.putAttribute("nodeKindList", ApprovalRule.NodeKind.getData());
        this.putAttribute("statusList", ValidStatus.getData());

        this.putAttribute("nodeKindId", ApprovalRule.NodeKind.CATEGORY.getId());
        this.putAttribute("status", ValidStatus.ENABLED.getId());

        return forward("ApprovalRuleDetail", this.getSDO());
    }

    @RequiresPermissions("ProcApprovalRuleConfig:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加流程审批规则配置")
    public String insertApprovalRule() {
        SDO params = this.getSDO();
        ApprovalRule approvalRule = params.toObject(ApprovalRule.class);
        List<ApprovalRuleScope> approvalRuleScopes = params.getList("detailData", ApprovalRuleScope.class);
        approvalRule.setInputApprovalRuleScopes_(approvalRuleScopes);
        approvalRule.addUpdateFields_("inputApprovalRuleScopes_");
        String id = this.approvalRuleApplication.insertApprovalRule(approvalRule);
        return success(id);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改流程审批规则配置")
    public String updateApprovalRule() {
        SDO params = this.getSDO();
        ApprovalRule approvalRule = params.toObject(ApprovalRule.class);
        List<ApprovalRuleScope> approvalRuleScopes = params.getList("detailData", ApprovalRuleScope.class);
        approvalRule.setInputApprovalRuleScopes_(approvalRuleScopes);
        approvalRule.addUpdateFields_("inputApprovalRuleScopes_");
        this.approvalRuleApplication.updateApprovalRule(approvalRule);
        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改流程审批规则配置明细页面")
    public String showUpdateApprovalRule() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        this.putAttribute("nodeKindList", ApprovalRule.NodeKind.getData());
        this.putAttribute("statusList", ValidStatus.getData());

        ApprovalRule approvalRule = this.approvalRuleApplication.loadApprovalRule(id);
        return forward("ApprovalRuleDetail", approvalRule);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动流程审批规则配置")
    public String moveApprovalRules() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        String parentId = params.getString("newParentId");
        this.approvalRuleApplication.moveApprovalRules(id, parentId);
        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除流程审批规则配置")
    public String deleteApprovalRule() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.approvalRuleApplication.deleteApprovalRules(ids);
        return success();
    }

    @RequiresPermissions(value = { "ProcApprovalRuleConfig:create", "ProcApprovalRuleConfig:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存流程审批规则处理人")
    public String saveApprovalRuleHandlers() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");

        List<ApprovalRuleHandler> approvalRuleHandlers = params.getList("data", ApprovalRuleHandler.class);

        this.approvalRuleApplication.saveApprovalRuleHandlers(approvalRuleId, approvalRuleHandlers);

        return success();
    }

    @RequiresPermissions(value = { "ProcApprovalRuleConfig:create", "ProcApprovalRuleConfig:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存流程审批规则处理人")
    public String saveApprovalRuleHandler() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        // 基本信息
        ApprovalRuleHandler approvalRuleHandler = params.toObject(ApprovalRuleHandler.class);
        // 分组 任务执行模式
        TaskExecuteMode taskExecuteMode = TaskExecuteMode.fromId(params.getString("taskExecuteMode"));
        // 协审
        List<ApprovalRuleHandlerAssist> assistants = params.getList("assistantList", ApprovalRuleHandlerAssist.class);
        // 抄送
        List<ApprovalRuleHandlerAssist> ccs = params.getList("ccList", ApprovalRuleHandlerAssist.class);
        // 字段权限
        List<ApprovalRuleHandlerUIElmentPermission> uiElmentPermissions = params.getList("fieldPermissionList", ApprovalRuleHandlerUIElmentPermission.class);

        this.approvalRuleApplication.saveApprovalRuleHandler(approvalRuleId, approvalRuleHandler, taskExecuteMode, assistants, ccs, uiElmentPermissions);

        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除流程审批规则处理人")
    public String deleteApprovalRuleHandlers() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        List<String> ids = params.getStringList("ids");

        approvalRuleApplication.deleteApprovalRuleHandlers(approvalRuleId, ids);

        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程审批规则处理人")
    public String queryApprovalRuleHandlers() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = approvalRuleApplication.queryApprovalRuleHandlers(approvalRuleId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程审批规则审批要素")
    public String queryApprovalRuleElements() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = approvalRuleApplication.queryApprovalRuleElements(approvalRuleId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions(value = { "ProcApprovalRuleConfig:create", "ProcApprovalRuleConfig:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存流程审批规则审批要素")
    public String saveApprovalRuleElements() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");

        List<ApprovalRuleElement> approvalRuleElements = params.getList("data", ApprovalRuleElement.class);
        this.approvalRuleApplication.saveApprovalRuleElements(approvalRuleId, approvalRuleElements);

        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除流程审批规则审批要素")
    public String deleteApprovalRuleElements() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        List<String> ids = params.getStringList("ids");

        this.approvalRuleApplication.deleteApprovalRuleElements(approvalRuleId, ids);
        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到流程审批人明细页面")
    public String forwardApprovalHandlerDetail() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        String approvalRuleHandlerId = params.getString("id");

        ApprovalRule approvalRule = this.approvalRuleApplication.loadApprovalRule(approvalRuleId);
        ApprovalRuleHandler approvalRuleHandler = approvalRule.findApprovalRuleHandler(approvalRuleHandlerId);
        ApprovalRuleHandlerGroup approvalRuleHandlerGroup = approvalRule.findApprovalRuleHandlerGroup(approvalRuleHandler.getGroupId());

        this.putAttribute("taskExecuteModes", TaskExecuteMode.getData());
        this.putAttribute("approvalRuleId", params.getProperty("approvalRuleId"));

        if (approvalRuleHandlerGroup != null) {
            this.putAttribute("taskExecuteMode", approvalRuleHandlerGroup.getTaskExecuteMode());
        }

        return forward("ApprovalHandlerDetailConfig", approvalRuleHandler);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询协审人员")
    public String queryAssistantHandlers() {
        SDO params = this.getSDO();
        String approvalRuleHandlerId = params.getString("chiefId");
        Map<String, Object> assistantData = this.approvalRuleApplication.queryAssistantHandlers(approvalRuleHandlerId);
        return toResult(assistantData);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询抄送人员")
    public String queryCCHandlers() {
        SDO params = this.getSDO();
        try {
            String approvalRuleHandlerId = params.getString("chiefId");
            Map<String, Object> ccData = this.approvalRuleApplication.queryCCHandlers(approvalRuleHandlerId);
            return toResult(ccData);
        } catch (Exception e) {
            return error(e);
        }
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询界面元素权限")
    public String queryUIElementPermissions() {
        SDO params = this.getSDO();
        String approvalRuleHandlerId = params.getString("approvalRuleHandlerId");
        Map<String, Object> data = this.approvalRuleApplication.queryUIElementPermissions(approvalRuleHandlerId);
        return toResult(data);
    }

    /**
     * 进入审批规则应用页面
     * 
     * @return
     */
    public String forwardApprovalRuleApply() {
        return forward("ApprovalRuleApply");
    }

    /**
     * 查询审批规则应用
     * 
     * @return
     */
    public String queryApprovalRuleApply() {
        SDO params = this.getSDO();
        String procId = params.getProperty("procId", String.class);
        String procUnitId = params.getProperty("procUnitId", String.class);
        Map<String, Object> bizProcessParams = params.getObjectMap("bizParams");
        Map<String, Object> result = this.approvalRuleApplication.queryApprovalRuleApply(procId, procUnitId, bizProcessParams);
        return toResult(result);
    }

    @RequiresPermissions("ProcApprovalRuleConfig:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除流程审批规则适用范围")
    public String deleteApprovalRuleScopes() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        List<String> ids = params.getStringList("ids");
        this.approvalRuleApplication.deleteApprovalRuleScopes(approvalRuleId, ids);
        return success();
    }

    @RequiresPermissions("ProcApprovalRuleConfig:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程审批规则适用范围")
    public String queryApprovalRuleScopes() {
        SDO params = this.getSDO();
        String approvalRuleId = params.getString("approvalRuleId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> result = this.approvalRuleApplication.queryApprovalRuleScopes(approvalRuleId, queryRequest);
        return toResult(result);
    }

    /**
     * 同步审批规则
     * 
     * @return
     */
    @RequiresPermissions("ProcApprovalRuleConfig:syn")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.CACHE, description = "同步流程审批规则")
    public String synApprovalRule() {
        SDO params = this.getSDO();
        String orgId = params.getProperty("orgId", String.class);
        String procId = params.getProperty("procKey", String.class);
        Long approveRuleId = params.getProperty("approveRuleId", Long.class);
        Map<String, Object> result = this.approvalRuleApplication.synApprovalRule(orgId, procId, approveRuleId);
        return toResult(result);
    }

}