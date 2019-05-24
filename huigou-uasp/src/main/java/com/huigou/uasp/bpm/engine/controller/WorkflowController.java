package com.huigou.uasp.bpm.engine.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.context.ContextUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bpm.ApprovalParameterUtil;
import com.huigou.uasp.bpm.BatchAdvanceParameter;
import com.huigou.uasp.bpm.CounterSignKind;
import com.huigou.uasp.bpm.ProcessAction;
import com.huigou.uasp.bpm.ProcessTaskContants;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.uasp.bpm.ViewTaskKind;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceRelation;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnit;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerManuscript;
import com.huigou.uasp.bpm.engine.domain.query.ProcunitHandlerQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.Constants;
import com.huigou.util.DateRange;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 工作流
 * 
 * @author gongmm
 */
@Controller
@ControllerMapping("workflow")
public class WorkflowController extends CommonController {

    @Autowired
    private WorkflowApplication workflowApplication;

    private static String TASK_CENTER_PAGE = "TaskCenter";

    private static String QUERY_DIMISSION_PERSON_TASK = "QueryDimissionPersonTask";

    private static String SELECT_TASK_DIALOG = "SelectTaskDialog";

    @Override
    protected String getPagePath() {
        return "/system/taskCenter/";
    }

    public String forwardTaskCenter() {
        Map<Integer, String> dataRangeList = DateRange.getData();
        Map<Integer, String> viewTaskKindList = ViewTaskKind.getData();

        this.putAttribute("dateRangeList", dataRangeList);
        this.putAttribute("viewTaskKindList", viewTaskKindList);

        return this.forward(TASK_CENTER_PAGE);
    }

    @RequiresPermissions("TaskQuery:query")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.LIST, description = "跳转到流程任务查询列表页面")
    public String forwardTaskQuery() {
        Map<Integer, String> dataRangeList = DateRange.getData();
        Map<Integer, String> viewTaskKindList = ViewTaskKind.getQueryUseData();

        this.putAttribute("dateRangeList", dataRangeList);
        this.putAttribute("viewTaskKindListData", viewTaskKindList);
        this.putAttribute("viewTaskKindList", ViewTaskKind.COMPLETED.getId());
        return this.forward("TaskQuery");
    }

    public String forwardWriteSignature() {
        SDO params = this.getSDO();
        this.putAttribute("bizId", params.getProperty("bizId"));
        this.putAttribute("procUnitHandlerId", params.getProperty("procUnitHandlerId"));
        this.putAttribute("procUnitId", params.getProperty("procUnitId"));

        return forward("/system/writeSignature/writeSignature.jsp");
    }

    public String forwardQueryDimissionPersonTask() {
        Map<Integer, String> viewTaskKindList = ViewTaskKind.getData();
        this.putAttribute(DateRange.getDataRange(DateRange.DAY_30));
        this.putAttribute("viewTaskKindList", viewTaskKindList);
        return this.forward(QUERY_DIMISSION_PERSON_TASK);
    }

    public String showSelectTaskDialog() {
        Map<Integer, String> dataRangeList = DateRange.getData();
        Map<Integer, String> viewTaskKindList = ViewTaskKind.getData();

        this.putAttribute("dateRangeList", dataRangeList);
        this.putAttribute("viewTaskKindList", viewTaskKindList);

        return this.forward(SELECT_TASK_DIALOG);
    }

    /**
     * @return
     */
    public String showTaskDetail() {
        SDO params = this.getSDO();
        String taskId = params.getProperty("taskId", String.class);
        String kindId = params.getProperty("kindId", String.class);
        Map<String, Object> taskDetail;
        if ("RuntimeTask".equalsIgnoreCase(kindId)) {
            taskDetail = this.workflowApplication.getActApplication().loadRuntimeTaskById(taskId);
        } else {
            taskDetail = this.workflowApplication.getActApplication().loadHistoricTaskById(taskId);
        }
        return forward("/system/bpm/TaskDetail.jsp", taskDetail);
    }

    public String deploy() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        String fileName = params.getString("fileName");

        this.workflowApplication.deploy(id, fileName);
        return success();
    }

    public String startProcessInstanceByKey() {
        SDO params = this.getSDO();
        Map<String, Object> variables = new HashMap<String, Object>();
        String processDefinitionKey = params.getProperty("processDefinitionKey", String.class);
        String showTips = params.getProperty("showTips", String.class, "true");
        ThreadLocalUtil.putVariable(Constants.SDO, params);
        ProcessInstance pi = this.workflowApplication.startProcessInstanceByKey(processDefinitionKey, variables);

        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("procInstId", pi.getId());
        result.put("bizId", pi.getBusinessKey());
        result.put("taskId", ApprovalParameterUtil.getApprovalParameter().getTaskId());
        result.put("bizData", localSdo.getProperty("bizData"));

        if (showTips.equalsIgnoreCase("true")) {
            return success(result);
        } else {
            return this.toResult(result);
        }
    }

    public String showQueryHandlers() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procId = params.getString("processDefinitionKey");
        String procUnitId = params.getString("procUnitId");
        Integer groupId = params.getInteger("groupId");
        String procUnitHandlerId = params.getString("procUnitHandlerId");
        Boolean result = this.workflowApplication.showQueryHandlers(bizId, procId, procUnitId, groupId, procUnitHandlerId);
        return toResult(result);
    }

    public String queryHandlers() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.QUERY_HANDLERS);

        String bizId = params.getProperty("bizId", String.class);
        String taskId = params.getProperty("taskId", String.class);
        Map<String, Object> result = this.workflowApplication.queryHandlers(bizId, taskId);

        return this.toResult(result);
    }

    /**
     * 流转
     * 
     * @return
     */
    public String advance() {
        SDO params = this.getSDO();
        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        localSdo.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ADVANCE);

        Map<String, Object> variables = new HashMap<String, Object>();

        workflowApplication.advance(params.getString("taskId"), variables);

        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put("bizData", localSdo.getProperty("bizData"));

        return success(result);
    }

    public String batchAdvance() {
        SDO params = this.getSDO();
        List<BatchAdvanceParameter> batchAdvanceParameters = params.getList("data", BatchAdvanceParameter.class);
        this.workflowApplication.batchAdvance(batchAdvanceParameters);
        return success();
    }

    public String queryAdvance() {
        SDO params = this.getSDO();
        Map<String, Object> variables = new HashMap<String, Object>();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.QUERY_ADVANCE);
        workflowApplication.advance(params.getString("taskId"), variables);
        return success();
    }

    /**
     * 完成任务
     * 
     * @return
     */
    public String completeTask() {
        SDO params = this.getSDO();
        workflowApplication.completeTask(params.getString(ProcessTaskContants.TASK_ID));
        return success();
    }

    public String completeMendTask() {
        SDO params = this.getSDO();
        params.putProperty("onlySaveHandlerData", "true");
        String bizId = params.getString("bizId");
        String taskId = params.getString("taskId");
        workflowApplication.completeMendTask(bizId, taskId);
        return success();
    }

    public String completeReplenishTask() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ADVANCE);
        String bizId = params.getString("bizId");
        String taskId = params.getString("taskId");
        workflowApplication.completeReplenishTask(bizId, taskId);
        return success();
    }

    /**
     * 转交
     * 
     * @return
     */
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

    public String assign() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ADVANCE);
        String taskId = params.getString("taskId");
        String procUnitHanderId = params.getString("currentHandleId");
        List<String> personMemberIds = params.getStringList("personMemberIds");
        workflowApplication.assign(taskId, procUnitHanderId, personMemberIds);
        return success();
    }

    public String claim() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        String personMemberId = params.getString("personMemberId");
        workflowApplication.claim(taskId, personMemberId);
        return success();
    }

    /**
     * 保存业务数据
     * 
     * @return
     */
    public String saveBizData() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.SAVE);
        String showTips = params.getProperty("showTips", String.class, "true");

        String bizId = params.getString("bizId");
        String taskId = params.getString("taskId");

        this.workflowApplication.saveBizData(bizId, taskId);

        SDO localSdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put("bizData", localSdo.getProperty("bizData"));

        if (showTips.equalsIgnoreCase("true")) {
            return success(result);
        } else {
            return this.toResult(result);
        }
    }

    /**
     * 终止任务
     * 
     * @return
     */
    public String abortTask() {
        SDO params = this.getSDO();
        String taskId = params.getString(ProcessTaskContants.TASK_ID);
        this.workflowApplication.abortTask(taskId);
        return success();
    }

    /**
     * 删除流程实例
     * 
     * @return
     */
    public String deleteProcessInstance() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.DELETE_PROCESS_INSTANCE);
        String processInstanceId = params.getProperty(ProcessTaskContants.PROC_INST_ID, String.class);
        this.workflowApplication.deleteProcessInstance(processInstanceId, ProcessAction.DELETE_PROCESS_INSTANCE);
        return success();
    }

    // @Deprecated
    // public String abort() {
    // SDO params = this.getSDO();
    // try {
    // params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ABORT);
    // ThreadLocalUtil.putVariable(Constants.SDO, params);
    // try {
    // String processInstanceId = params.getProperty(ProcessTaskContants.PROC_INST_ID, String.class);
    // this.workflowApplication.deleteProcessInstance(processInstanceId, ProcessAction.ABORT);
    // } finally {
    // ThreadLocalUtil.removeVariable(Constants.SDO);
    // }
    // return success();
    // } catch (Exception e) {
    // return error(e);
    // }
    // }

    public String abortProcessInstance() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ABORT_PROCESS_INSTANCE);
        String processInstanceId = params.getProperty(ProcessTaskContants.PROC_INST_ID, String.class);
        this.workflowApplication.abortProcessInstance(processInstanceId);
        return success();
    }

    public String abortProcessInstanceByBizId() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ABORT_PROCESS_INSTANCE);

        String bizId = params.getProperty(ProcessTaskContants.BIZ_ID, String.class);
        this.workflowApplication.abortProcessInstanceByBizId(bizId);
        return success();
    }

    public String abortProcessInstanceByBizCode() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ABORT_PROCESS_INSTANCE);

        String bizCode = params.getProperty(ProcessTaskContants.BIZ_CODE, String.class);
        this.workflowApplication.abortProcessInstanceByBizCode(bizCode);
        return success();
    }

    public String recallProcessInstance() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.RECALL_PROCESS_INSTANCE);
        String bizId = params.getString("bizId");
        String processInstanceId = params.getProperty(ProcessTaskContants.PROC_INST_ID, String.class);
        String taskId = params.getProperty(ProcessTaskContants.TASK_ID, String.class);
        this.workflowApplication.recallProcessInstance(processInstanceId, taskId);
        if (StringUtil.isNotBlank(bizId)) {
            Map<String, Object> map = workflowApplication.loadRuntimeTaskByBizId(bizId);
            return toResult(map);
        }
        return success();
    }

    public String withdrawTask() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.WITHDRAW);
        ThreadLocalUtil.putVariable(Constants.SDO, params);

        String previousId = params.getProperty(ProcessTaskContants.PREVIOUS_ID, String.class);
        this.workflowApplication.withdrawTask(params.getString(ProcessTaskContants.TASK_ID), previousId);
        return success();
    }

    public String withdrawTaskByBizId() {
        SDO sdo = this.getSDO();
        String bizId = sdo.getBizId();
        String taskId = sdo.getString(ProcessTaskContants.TASK_ID);
        this.workflowApplication.withdrawTaskByBizId(taskId, bizId);
        Map<String, Object> map = workflowApplication.loadRuntimeTaskByBizId(bizId);
        return toResult(map);
    }

    public String makeACopyFor() {
        SDO params = this.getSDO();
        List<String> executorIds = params.getStringList("receivers");
        this.workflowApplication.makeACopyForEvent(params.getString(ProcessTaskContants.TASK_ID), executorIds);
        return success();
    }

    /**
     * 暂缓
     */
    public String sleep() {
        SDO params = this.getSDO();
        workflowApplication.sleep(params.getString(ProcessTaskContants.TASK_ID));
        return success();
    }

    /**
     * 回退
     */
    public String back() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.BACK);
        String destActivityId = params.getString("destActivityId");
        String backToProcUnitHandlerId = params.getString("backToProcUnitHandlerId");
        String taskId = params.getString(ProcessTaskContants.TASK_ID);
        this.workflowApplication.back(taskId, destActivityId, backToProcUnitHandlerId);
        return success();
    }

    public String backToApplyActivity() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.BACK);
        String procInstId = params.getString("procInstId");
        String taskId = params.getString(ProcessTaskContants.TASK_ID);
        this.workflowApplication.backToApplyActivity(taskId, procInstId);
        return success();
    }

    /**
     * 查询回退Activity
     */
    public String queryBackActivity() {
        SDO params = this.getSDO();
        List<ActivityImpl> ActivityImplList = this.workflowApplication.queryBackActivity(params.getString("taskId"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(ActivityImplList.size());
        for (ActivityImpl activityImpl : ActivityImplList) {
            Map<String, Object> item = new HashMap<String, Object>(2);
            item.put("id", activityImpl.getId());
            item.put("name", activityImpl.getProperty("name"));
            data.add(item);
        }
        return success(this.toResult(data));
    }

    public String getAllProcessDefinitions() {
        List<ProcUnit> processDefinitions = this.workflowApplication.getAllProcessDefinitions();
        return success(processDefinitions);
    }

    public String getUserTaskActivitiesByProcessDefinitionId() {
        SDO params = this.getSDO();
        String processDefinitionId = params.getProperty("processDefinitionId", String.class);
        List<ProcUnit> userTaskActivities = this.workflowApplication.getUserTaskActivitiesByProcessDefinitionId(processDefinitionId);
        return success(userTaskActivities);
    }

    /**
     * 查询待办任务
     * 
     * @return
     */
    public String queryTasks() {
        Map<String, Object> data = workflowApplication.queryTasks(this.getSDO());
        return this.toResult(data);
    }

    public String queryCCProcUnitHandlersByBizId() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        List<ProcUnitHandler> data = workflowApplication.getProcUnitHandlerService().queryCCProcUnitHandlersByBizId(bizId);
        return this.toResult(data);
    }

    public String showFlowChart() {
        SDO params = this.getSDO();
        return forward("/system/bpm/WorkFlowChart.jsp", params);
    }

    public String queryFlowChart() {
        SDO params = this.getSDO();
        String bizId = params.getBizId();
        String ownerOrgId = params.getString("ownerOrgId");
        String isShowAllRules = params.getString("isShowAllRules");
        Map<String, Object> chartData = workflowApplication.queryFlowChart(bizId, ownerOrgId, isShowAllRules);
        return this.toResult(chartData);
    }

    public String showFlowLine() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        List<?> l = workflowApplication.queryFlowChartProcedure(bizId);
        this.putAttribute("procHandlers", l);
        return forward("/system/bpm/WorkFlowLine.jsp");
    }

    /**
     * 获取任务节点信息
     * 
     * @return String
     */
    public String getProcedureInfo() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitHandlerId = params.getString("nodeId");
        Map<String, Object> map = workflowApplication.getProcedureInfo(bizId, procUnitHandlerId);
        return this.toResult(map);
    }

    /**
     * 查询审批历史
     * 
     * @return
     */
    public String queryApprovalHistoryByProcInstId() {
        throw new ApplicationException("not implememts.");
        // SDO params = this.getSDO();
        // try {
        // String procInstId = params.getProperty("procInstId", String.class);
        // Map<String, Object> data = workflowApplication.queryApprovalHistoryByProcInstId(procInstId);
        // return success(toResult(data));
        // } catch (Exception e) {
        // return error(e);
        // }
    }

    public String queryApprovalHistoryByBizId() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        Map<String, Object> data = workflowApplication.queryApprovalHistoryByBizId(bizId);
        return toResult(data);
    }

    /**
     * 显示审批历史
     * 
     * @return
     */
    public String showApprovalHistory() {
        SDO params = this.getSDO();
        return forward("/system/bpm/ApprovalHistory.jsp", params);
    }

    public String showAdvanceQuery() {
        SDO params = this.getSDO();
        return forward("/system/bpm/AdvanceQueryDialog.jsp", params);
    }

    /**
     * 显示回退查询
     * 
     * @return
     */
    public String showBackQuery() {
        SDO params = this.getSDO();
        return forward("/system/bpm/BackQuery.jsp", params);
    }

    public String queryBackTasksByBizCode() {
        SDO params = this.getSDO();
        String bizCode = params.getProperty("bizCode", String.class);
        String procUnitId = params.getProperty("procUnitId", String.class);
        Integer groupId = params.getProperty("groupId", Integer.class);
        Map<String, Object> data = workflowApplication.queryBackTasksByBizCode(bizCode, procUnitId, groupId);
        return success(toResult(data));
    }

    public String queryBackProcUnit() {
        SDO params = this.getSDO();
        String processInstanceId = params.getString("procInstId");
        String approvalRuleId = params.getString("approvalRuleId");
        Integer groupId = params.getInteger("groupId");

        Map<String, Object> data = workflowApplication.queryBackProcUnit(processInstanceId, approvalRuleId, groupId);
        return success(toResult(data));
    }

    public String getProcessInstanceActiveActivityId() {
        SDO params = this.getSDO();
        String processInstanceId = params.getProperty("procInstId", String.class);
        List<ActivityImpl> activeActivities = this.workflowApplication.queryProcessInstanceActiveActivities(processInstanceId);
        ActivityImpl activeActivity = activeActivities.get(0);
        return success(activeActivity.getId());
    }

    // private ActivityImpl buildCounterSignParams(SDO params, String bizId, String processInstanceId) {
    // List<ActivityImpl> activeActivities = this.workflowApplication.queryProcessInstanceActiveActivities(processInstanceId);
    // ActivityImpl activeActivity = activeActivities.get(0);
    // Integer activeGroupId = this.workflowApplication.getProcUnitHandlerService().getActiveProcUnitHanderGroupId(bizId, activeActivity.getId());
    //
    // params.putProperty("procUnitId", activeActivity.getId());
    // params.putProperty("groupId", activeGroupId);
    //
    // return activeActivity;
    // }

    public String showCounterSignDialog() {
        SDO params = this.getSDO();
        // TODO 得到参数放到 service中去
        String processInstanceId = params.getString("procInstId");
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");
        String taskKindId = params.getString("taskKindId");

        String counterSignKindId = params.getProperty("counterSignKindId", String.class, "chief");
        // Long hiProcUnitHandlerInstVersion = 0L;
        // String activeProcUnitId = procUnitId;

        if (CounterSignKind.CHIEF.isSpecifiedKind(counterSignKindId)) {
            // ActivityImpl activeActivity;
            // 申请环节加签、打回任务
            boolean isReplenish = TaskKind.REPLENISH.equalsIgnoreCase(taskKindId);
            if (procUnitId.equalsIgnoreCase("apply") || isReplenish) {
                if (isReplenish) {
                    ProcessInstance pi = this.workflowApplication.getRunTimeService().createProcessInstanceQuery()
                                                                 .processInstanceBusinessKey(String.valueOf(bizId)).singleResult();
                    Assert.notNull(pi, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, "流程实例"));
                    processInstanceId = pi.getProcessInstanceId();
                }
                if (processInstanceId != null) {
                    // activeActivity = buildCounterSignParams(params, bizId, processInstanceId);
                    // activeProcUnitId = activeActivity.getId();
                }
            }
        } else if (CounterSignKind.MEND.isSpecifiedKind(counterSignKindId)) {
            ProcUnitHandler procUnitHandler = this.workflowApplication.getProcUnitHandlerService().queryLastProcUnitHandler(bizId);
            if (procUnitHandler == null) {
                throw new ApplicationException("在审批表中未找到审批人。");
            }
            params.putProperty("procUnitId", procUnitHandler.getProcUnitId());
            params.putProperty("groupId", procUnitHandler.getGroupId());

            // activeProcUnitId = procUnitHandler.getProcUnitId();
        } else if (CounterSignKind.MANUAL_SELECTION.isSpecifiedKind(counterSignKindId)) {

        } else {
            throw new ApplicationException("不支持的加减签类型。");
        }

        // TODO 并行处理

        // HistoricProcUnitHandlerService historicProcUnitHandlerService = this.workflowApplication.getProcUnitHandlerService()
        // .getHistoricProcUnitHandlerService();

        // hiProcUnitHandlerInstVersion = historicProcUnitHandlerService.getHistoricProcUnitHandlerInstVersion(bizId, activeProcUnitId);
        // params.putProperty("hiProcUnitHandlerInstVersion", hiProcUnitHandlerInstVersion);

        return forward("/system/bpm/CounterSignDialog.jsp", params);
    }

    public String queryCounterSignHandler() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");
        Map<String, Object> data = workflowApplication.queryCounterSignHandlers(bizId, procUnitId);
        return success(toResult(data));
    }

    public String saveCounterSignHandler() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");
        String currentProcUnitHandlerId = params.getString("procUnitHandlerId");
        Long version = params.getLong("hiProcUnitHandlerInstVersion");
        List<String> minusSignIds = params.getStringList("deleted");
        List<ProcUnitHandler> countersigns = params.getList("detailData", ProcUnitHandler.class);
        workflowApplication.saveCounterSignHandlers(bizId, procUnitId, version, currentProcUnitHandlerId, minusSignIds, countersigns);
        return success();
    }

    /**
     * 显示协审查询
     * 
     * @return
     */
    public String showAssistDialog() {
        SDO params = this.getSDO();
        return forward("/system/bpm/AssistDialog.jsp", params);
    }

    public String queryAssistHandler() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");
        String chiefId = params.getString("chiefId");

        List<ProcUnitHandler> data = workflowApplication.queryAssistHandlers(bizId, procUnitId, chiefId);

        return this.packGridDataAndResult(data);
        // return success(this.toResult(data));
    }

    /**
     * 协审
     * 
     * @return
     */
    public String assist() {
        SDO params = this.getSDO();

        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.ASSIST);

        String bizId = params.getString("bizId");
        String taskId = params.getString("taskId");
        String procUnitHandlerId = params.getString("procUnitHandlerId");

        List<String> deletedIds = params.getStringList("deleted");
        List<String> executorIds = params.getStringList("executors");
        List<Integer> sendMessages = params.getIntegerList("sendMessages");

        workflowApplication.assist(bizId, taskId, procUnitHandlerId, deletedIds, executorIds, sendMessages);

        return success();

    }

    public String queryHiTaskInstsByBizCode() {
        SDO params = this.getSDO();
        String bizCode = params.getProperty("bizCode", String.class);
        Map<String, Object> data = this.workflowApplication.getActApplication().queryHiTaskInstsByBizCode(bizCode);
        return success(this.toResult(data));
    }

    public String saveProcUnitHandlers() {
        SDO params = this.getSDO();
        List<ProcUnitHandler> procUnitHandlers = params.getList("data", ProcUnitHandler.class);
        this.workflowApplication.getProcUnitHandlerService().saveProcUnitHandlers(procUnitHandlers, false);
        return success();
    }

    public String saveProcUnitHandlerMaunscript() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitHandlerId = params.getString("procUnitHandlerId");
        Integer height = params.getInteger("height");
        String opinion30 = params.getString("opinion30");// ClassHelper.convert(ActionContext.getContext().getParameters().get("opinion30"), String.class);
        String opinion64 = params.getString("opinion64");// ClassHelper.convert(this.getParameter("opinion64"), String.class);
        this.workflowApplication.getProcUnitHandlerService().saveProcUnitHandlerManuscript(bizId, procUnitHandlerId, height, opinion30, opinion64);
        return success();
    }

    public String loadProcUnitHandlerMaunscript() {
        SDO params = this.getSDO();
        String procUnitHandlerId = params.getString("procUnitHandlerId");
        ProcUnitHandlerManuscript procUnitHandlerManuscript = this.workflowApplication.getProcUnitHandlerService()
                                                                                      .loadProcUnitHandlerManuscript(procUnitHandlerId);
        return this.toResult(procUnitHandlerManuscript);
    }

    public String deleteProcUnitHandlers() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.workflowApplication.getProcUnitHandlerService().deleteProcUnitHandlers(ids);
        return success();
    }

//    public String updateHistoricTaskInstanceStatus() {
//        SDO params = this.getSDO();
//        String taskId = params.getString("taskId");
//        String status = params.getString("status");
//        this.workflowApplication.updateHistoricTaskInstanceStatus(taskId, status);
//        return success();
//    }

    public String updateTaskExtensionStatusToExecuting() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        this.workflowApplication.updateTaskExtensionStatus(taskId, TaskStatus.EXECUTING);
        return success();
    }

    public String updateTaskInstExtensionNeedTiming() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        Integer needTiming = params.getInteger("needTiming");

        this.workflowApplication.getActApplication().updateTaskExtensionNeedTiming(taskId, needTiming);
        return success();
    }

    public String updateTasksDescription() {
        SDO params = this.getSDO();
        Map<String, Object> data = params.getObjectMap("data");
        this.workflowApplication.updateTasksDescription(data);
        return success();
    }

    public String launchMendTask() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");
        this.workflowApplication.launchMendTask(bizId, procUnitId);
        return success();
    }

    public String mend() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String procUnitId = params.getString("procUnitId");

        String currentProcUnitHandlerId = params.getString("procUnitHandlerId");
        Long version = params.getLong("hiProcUnitHandlerInstVersion");
        List<ProcUnitHandler> countersigns = params.getList("detailData", ProcUnitHandler.class);

        this.workflowApplication.mend(bizId, procUnitId, currentProcUnitHandlerId, version, countersigns);
        return success();
    }

    public String replenish() {
        SDO params = this.getSDO();
        params.putProperty(ProcessAction.PROCESS_ACTION, ProcessAction.REPLENISH);

        String backToTaskId = params.getString("backToTaskId");
        String currentTaskId = params.getString("taskId");
        String procUnitHandlerId = params.getString("currentHandleId");

        this.workflowApplication.replenish(backToTaskId, currentTaskId, procUnitHandlerId);
        return success();
    }

    public String queryHiTaskinstRelations() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        String bizId = params.getString("bizId");

        Map<String, Object> data = this.workflowApplication.getActApplication().queryHiTaskinstRelations(taskId, bizId);
        return success(this.toResult(data));
    }

    /**
     * 保存关联任务
     * 
     * @return
     * @throws
     */
    public String saveHiTaskinstRelation() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        String bizId = params.getString("bizId");
        String processInstanceId = params.getString("procInstId");

        List<HistoricTaskInstanceRelation> historicTaskInstRelations = params.getList("data", HistoricTaskInstanceRelation.class);
        for (HistoricTaskInstanceRelation item : historicTaskInstRelations) {
            item.setBusinessKey(bizId);
            item.setProcInstId(processInstanceId);
            item.setTaskId(taskId);
        }

        this.workflowApplication.getActApplication().saveHiTaskinstRelations(bizId, historicTaskInstRelations);
        return success();
    }

    public String deleteHiTaskinstRelation() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.workflowApplication.getActApplication().deleteHiTaskinstRelations(ids);
        return success();
    }

    /**
     * 插入评论信息
     * 
     * @return
     * @throws
     */
    public String insertComment() {
        SDO params = this.getSDO();
        String procUnitHandlerId = params.getProperty("handleId", String.class);
        String bizId = params.getProperty("bizId", String.class);
        String message = params.getProperty("message", String.class);
        // procUnitHandlerId --> taskId; bizId --> procInstId
        this.workflowApplication.saveComment(procUnitHandlerId, bizId, message);
        // 保存后执行查询返回全部品论信息
        List<Comment> comments = this.workflowApplication.queryProcUnitHandlerComments(procUnitHandlerId);
        return success(comments);
    }

    /**
     * 查询品论信息
     * 
     * @return
     * @throws
     */
    public String queryProcUnitHandlerComments() {
        SDO params = this.getSDO();
        String procUnitHandlerId = params.getProperty("handleId", String.class);
        List<Comment> comments = this.workflowApplication.queryProcUnitHandlerComments(procUnitHandlerId);
        return this.toResult(comments);
    }

    public String queryApplyTaskIdByBizId() {
        SDO params = this.getSDO();
        String bizId = params.getProperty("bizId", String.class);
        Map<String, Object> applicant = workflowApplication.getActApplication().queryApplicantByBizId(bizId);
        return toResult(applicant.get("id"));
    }

    public String handTasks() {
        SDO params = this.getSDO();
        String fromPersonId = params.getString("fromPersonId");
        String toPsmId = params.getString("toPsmId");
        this.workflowApplication.handTasks(fromPersonId, toPsmId);
        return success();
    }

    public String queryGroupProcUnitHandlers() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String approvalProcUnitId = params.getProperty("approvalProcUnitId", String.class);
        String procUnitId = params.getProperty("procUnitId", String.class);
        String currentTaskId = params.getProperty("currentTaskId", String.class);
        List<Map<String, Object>> handlers = this.workflowApplication.getProcUnitHandlerService().groupProcUnitHandlers(bizId, approvalProcUnitId, procUnitId,
                                                                                                                        currentTaskId,
                                                                                                                        ContextUtil.getOperator().getUserId());
        return toResult(handlers);
    }

    public String queryAllGroupProcUnitHandlers() {
        SDO params = this.getSDO();
        String bizId = params.getString("bizId");
        String processDefinitionKey = params.getString("processDefinitionKey");

        List<Map<String, Object>> handlers = this.workflowApplication.getProcUnitHandlerService().groupProcUnitHandlers(bizId, processDefinitionKey);
        return toResult(handlers);
    }

    public String backupProcUnitHandler() {
        SDO params = this.getSDO();
        String bizCode = params.getString("bizCode");
        this.workflowApplication.getProcUnitHandlerService().backupProcUnitHandler(bizCode);
        return success();
    }

    public String recoverProcUnitHandler() {
        SDO params = this.getSDO();
        String bizCode = params.getString("bizCode");
        this.workflowApplication.getProcUnitHandlerService().recoverProcUnitHandler(bizCode);
        return success();
    }

    public String collectTask() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        this.workflowApplication.collectTask(taskId);
        return success();
    }

    public String cancelCollectionTask() {
        SDO params = this.getSDO();
        String taskId = params.getString("taskId");
        this.workflowApplication.cancelCollectionTask(taskId);
        return success();
    }

    public String queryCurrentOperatorTaskStatistics() {
        Map<String, Object> data = this.workflowApplication.queryCurrentOperatorTaskStatistics();
        return toResult(data);
    }

    public String checkAssistantPassRule() {
        SDO params = this.getSDO();
        String processDefinitionKey = params.getString("processDefinitionKey");
        String taskDefinitionKey = params.getString("taskDefinitionKey");
        String bizId = params.getString("bizId");
        String procUnitHandlerId = params.getString("procUnitHandlerId");
        Boolean result = workflowApplication.checkAssistantPassRule(processDefinitionKey, taskDefinitionKey, bizId, procUnitHandlerId);
        return this.toResult(result);
    }

    @RequiresPermissions("ProcUnitHandlerQuery:query")
    @LogInfo(logType = LogType.WORKFLOW, subType = "", operaionType = OperationType.LIST, description = "跳转到流程处理人查询列表页面")
    public String forwardProcunitHandlerQuery() {
        Map<Integer, String> dataRangeList = DateRange.getData();
        this.putAttribute("dateRangeList", dataRangeList);
        return this.forward("ProcunitHandlerQuery");
    }

    public String slicedQueryProcunitHandler() {
        SDO params = this.getSDO();
        ProcunitHandlerQueryRequest queryRequest = params.toQueryRequest(ProcunitHandlerQueryRequest.class);
        Map<String, Object> data = this.workflowApplication.slicedQueryProcunitHandler(queryRequest);
        return this.toResult(data);
    }
    
    public String queryProcUnitHandler(){
        SDO params = this.getSDO();
        String procUnitHandlerId = params.getString("procUnitHandlerId");
        ProcUnitHandler procUnitHandler = this.workflowApplication.getProcUnitHandlerService().loadProcUnitHandler(procUnitHandlerId);
        return this.toResult(procUnitHandler);
    }

}
