package com.huigou.uasp.bpm.managment.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bpm.MergeHandlerKind;
import com.huigou.uasp.bpm.managment.application.ProcDefinitionApplication;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("procDefinition")
public class ProcDefinitionController extends CommonController {

    @Autowired
    private ProcDefinitionApplication procDefinitionApplication;

    protected String getPagePath() {
        return "/system/configtool/";
    }

    @RequiresPermissions("ProcDefinition:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到流程管理列表页面")
    public String forwardList() {
        return forward("ProcessManage");
    }
    
    //@RequiresPermissions(value={"ProcDefinition:query", "ProcApprovalRuleConfig:query"}, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程管理")
    public String queryProcDefinitions() {
        SDO params = this.getSDO();
        boolean inculdeProcUnit = "1".equals(params.getString("inculdeProcUnit"));
        ParentAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(ParentAndCodeAndNameQueryRequest.class);
        Map<String, Object> result = this.procDefinitionApplication.queryProcDefinitions(inculdeProcUnit, queryRequest);
        return toResult(result);
    }

    @RequiresPermissions("ProcDefinition:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询流程环节")
    public String queryProcUnitsFromActiviti() {
        SDO params = this.getSDO();
        String procId = params.getString("procId");
        List<Map<String, Object>> result = this.procDefinitionApplication.queryProcUnitsFromActiviti(procId);
        return toResult(result);
    }

    @RequiresPermissions("ProcDefinition:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加流程管理明细页面")
    public String showInsertProcDefinition() {
        SDO params = this.getSDO();

        String parentId = params.getString("parentId");
        Integer sequence = this.procDefinitionApplication.getProcDefinitionNextSequence(parentId);
        params.putProperty("sequence", sequence);
        this.putAttribute("mergeHandlerKindList", MergeHandlerKind.getMap());
        this.putAttribute("mergeHandlerKind", MergeHandlerKind.ADJACENT.getId());//默认相邻合并
        return forward("ProcessManageDetail", params);
    }

    @RequiresPermissions("ProcDefinition:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加流程管理")
    public String insertProcDefinition() {
        SDO params = this.getSDO();
        ProcDefinition procDefinition = params.toObject(ProcDefinition.class);
        this.procDefinitionApplication.insertProcDefinition(procDefinition);
        return success();
    }

    @RequiresPermissions("ProcDefinition:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改流程管理明细页面")
    public String showUpdateProcDefinition() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        ProcDefinition procDefinition = this.procDefinitionApplication.loadProcDefinition(id);
        this.putAttribute("mergeHandlerKindList", MergeHandlerKind.getMap());
        return forward("ProcessManageDetail", procDefinition);
    }

    @RequiresPermissions("ProcDefinition:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改流程管理")
    public String updateProcDefinition() {
        SDO params = this.getSDO();
        ProcDefinition procDefinition = params.toObject(ProcDefinition.class);
        this.procDefinitionApplication.updateProcDefinition(procDefinition);
        return success();
    }

    @RequiresPermissions("ProcDefinition:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改流程管理排序号")
    public String updateProcDefinitionSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap("data");
        this.procDefinitionApplication.updateProcDefinitionSequence(data);
        return success();
    }

    @RequiresPermissions("ProcDefinition:link")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "链接流程")
    public String bindActivitiProcDefinition() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        String procId = params.getString("procId");
        this.procDefinitionApplication.bindActivitiProcDefinition(id, procId);
        return success();
    }

    @RequiresPermissions("ProcDefinition:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除流程管理")
    public String deleteProcDefinitions() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.procDefinitionApplication.deleteProcDefinitions(ids);
        return success();
    }

    @RequiresPermissions("ProcDefinition:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动流程管理")
    public String moveProcess() {
        SDO params = getSDO();
        List<String> ids = params.getStringList("ids");
        String parentId = params.getString("parentId");
        this.procDefinitionApplication.moveProcDefinitions(ids, parentId);
        return success();
    }

    @RequiresPermissions("ProcDefinition:import")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "导入流程环节")
    public String importProcUnits() {
        SDO params = getSDO();
        String parentId = params.getString("parentId");
        this.procDefinitionApplication.importProcUnits(parentId);
        return success();
    }

    public String showSelectProcDialog() {
        return this.forward("SelectProcDialog");
    }

//    public String queryOneLevelProcDefinitions() {
//        List<Map<String, Object>> result = this.procDefinitionApplication.queryOneLevelProcDefinitions();
//        return toResult(result);
//    }
}
