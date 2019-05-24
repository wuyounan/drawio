package com.huigou.uasp.bpm.managment.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.ProcessTaskContants;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.managment.application.ProcessInstanceOperationApplication;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("processInstanceOperation")
public class ProcessInstanceOperationController extends CommonController {

    private static final String FLOW_TASK_HANDLER_MANAGE_PAGE = "FlowTaskHandlerManage";

    @Autowired
    private ProcessInstanceOperationApplication processInstanceOperationApplication;

    @Autowired
    private ProcUnitHandlerApplication procUnitHandlerApplication;

    @Autowired
    private WorkflowApplication workflowApplication;

    protected String getPagePath() {
        return "/system/configtool/approvalRule/";
    }

    @RequiresPermissions("FlowTaskManagement:query")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.LIST, description = "跳转到流程任务维护列表页面")
    public String forwardFlowTaskHandlerManage() {
        return forward(FLOW_TASK_HANDLER_MANAGE_PAGE);
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "更新任务处理人")
    public String updateTaskHandler() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        String personMemberId = params.getString("personMemberId");
        processInstanceOperationApplication.updateTaskHandler(taskId, personMemberId);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:query")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.QUERY, description = "根据业务编码查询流程环节处理人")
    public String queryProcunitHandlersByBizCode() {
        SDO params = this.getSDO();
        String bizCode = params.getString("bizCode");
        Map<String, Object> data = this.procUnitHandlerApplication.queryProcunitHandlersByBizCode(bizCode);
        return this.toResult(data);
    }

    @RequiresPermissions("FlowTaskManagement:query")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.QUERY, description = "根据业务编码查询历史流程环节处理人")
    public String queryHistoricProcUnitHandlers() {
        SDO params = this.getSDO();
        String bizCode = params.getString("bizCode");
        Map<String, Object> data = this.procUnitHandlerApplication.queryHistoricProcUnitHandlers(bizCode);
        return this.toResult(data);
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "更新任务状态")
    public String updateHistoricTaskInstanceStatus() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        String status = params.getString("status");
        this.workflowApplication.updateHistoricTaskInstanceStatus(taskId, status);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:query")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.QUERY, description = "通过编号查询历史任务")
    public String queryHiTaskInstsByBizCode() {
        SDO params = this.getSDO();
        String bizCode = params.getProperty("bizCode", String.class);
        Map<String, Object> data = this.workflowApplication.getActApplication().queryHiTaskInstsByBizCode(bizCode);
        return success(this.toResult(data));
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "保存环节处理人")
    public String saveProcUnitHandlers() {
        SDO params = this.getSDO();
        List<ProcUnitHandler> procUnitHandlers = params.getList("data", ProcUnitHandler.class);
        this.workflowApplication.getProcUnitHandlerService().saveProcUnitHandlers(procUnitHandlers, false);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:delete")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.DELETE, description = "删除环节处理人")
    public String deleteProcUnitHandlers() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.workflowApplication.getProcUnitHandlerService().deleteProcUnitHandlers(ids);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:mend")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.ADD, description = "发起补审")
    public String launchMendTask() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");
        this.workflowApplication.launchMendTask(bizId, procUnitId);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "更新任务标题")
    public String updateTasksDescription() {
        SDO params = this.getSDO();
        Map<String, Object> data = params.getObjectMap("data");
        this.workflowApplication.updateTasksDescription(data);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:create")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.ADD, description = "转交")
    public String transmit() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.TRANSMIT);
        ThreadLocalUtil.putVariable(Constants.SDO, params);

        String catalogId = params.getString("catalogId");
        String taskId = params.getString("taskId");
        String procUnitHandlerId = params.getString("currentHandleId");
        String executorId = params.getString("executorId");

        Integer sendMessage = params.getInteger("sendMessage");

        workflowApplication.transmit(catalogId, taskId, procUnitHandlerId, executorId, sendMessage);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "通过流程编号终止流程")
    public String abortProcessInstanceByBizCode() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ABORT_PROCESS_INSTANCE);

        String bizCode = params.getProperty(ProcessTaskContants.BIZ_CODE, String.class);
        this.workflowApplication.abortProcessInstanceByBizCode(bizCode);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "交接任务")
    public String handTasks() {
        SDO params = this.getSDO();
        String fromPersonId = params.getString("fromPersonId");
        String toPsmId = params.getString("toPsmId");
        this.workflowApplication.handTasks(fromPersonId, toPsmId);
        return success();
    }

    @RequiresPermissions("FlowTaskManagement:update")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.UPDATE, description = "修改任务计时状态")
    public String updateTaskInstExtensionNeedTiming() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        Integer needTiming = params.getInteger("needTiming");

        this.workflowApplication.getActApplication().updateTaskExtensionNeedTiming(taskId, needTiming);
        return success();
    }
}
