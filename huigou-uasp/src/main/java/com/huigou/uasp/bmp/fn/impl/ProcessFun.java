package com.huigou.uasp.bmp.fn.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.uasp.bpm.ProcessDefinitionUtil;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;
import com.huigou.uasp.bpm.managment.repository.ProcDefinitionRespository;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 流程实例函数
 * 
 * @author gongmm
 */
@Service("processFun")
public class ProcessFun {

    public static String MANUAL_SELECT_FLAG = "manualProcUnitId";

    @Resource(name = "sqlQuery")
    private SQLQuery sqlQuery;

    @Resource
    private ProcDefinitionRespository procDefinitionRespository;

    /**
     * 查找申请人
     * 
     * @return
     */
    public List<OrgUnit> findApplicant() {
        StringBuilder sb = new StringBuilder();

        sb.append("select t.applicant_full_id_ full_id, t.applicant_full_name_ full_name");
        sb.append("  from act_hi_procinst_extension t");
        sb.append(" where t.business_key_ = ?");

        SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        Assert.notNull(sdo, "执行函数“findApplicant”出错，没有设置环境变量。");

        String bizId = sdo.getString("bizId");

        Assert.hasText(bizId, "执行函数“findApplicant”出错，没有设置bizId变量。");

        List<OrgUnit> result = this.sqlQuery.getJDBCDao().queryToList(sb.toString(), OrgUnit.class, bizId);

        return result;
    }

    /**
     * 从流程定义ID中获取流程Key
     * 
     * @param processDefinitionId
     *            流程定义ID
     * @return
     */
    public String getProcessKeyFromProcessDefinitionId(String processDefinitionId) {
        // Key:2:42314
        Assert.hasText(processDefinitionId, "参数processDefinitionId不能为空。");
        String[] splits = processDefinitionId.split(":");
        Assert.isTrue(splits.length == 3, "流程定义ID格式不正确。");
        return splits[0];
    }

    /**
     * 得到流程定义Key
     * <p>
     * 如设置当前流程使用其他流程的审批规则，返回转换后的流程Key，否则返回当前流程Key
     * 
     * @param processDefinitionId
     *            流程定义ID
     * @return
     */
    public String getProcessApprovalDefinitionKey(String processDefinitionId) {
        Assert.hasText(processDefinitionId, "参数processDefinitionId不能为空。");
        String procId = ProcessDefinitionUtil.getProcessDefinitionKeyFromId(processDefinitionId);
        ProcDefinition procDefinition = this.procDefinitionRespository.findProc(procId);
        if (procDefinition != null && StringUtil.isNotBlank(procDefinition.getApprovalBridgeProcId())) {
            return procDefinition.getApprovalBridgeProcId();
        }
        return procId;
    }

    /**
     * 查询流程当前节点的下一步节点，用于流程提示时的提示。
     * 
     * @param taskId
     *            任务ID
     * @return
     */
    public Map<String, Object> queryNextProcUnits(DelegateTask delegateTask) {
        BpmnModel bpmnModel = Context.getProcessEngineConfiguration().getRepositoryService().getBpmnModel(delegateTask.getExecution().getProcessDefinitionId());

        List<org.activiti.bpmn.model.Process> processes = bpmnModel.getProcesses();
        org.activiti.bpmn.model.Process process = processes.get(0);
        FlowNode source = (FlowNode) process.getFlowElement(delegateTask.getTaskDefinitionKey());
        // 找到当前任务的流程变量
        List<HistoricVariableInstance> variables = Context.getProcessEngineConfiguration().getHistoryService().createHistoricVariableInstanceQuery()
                                                          .processInstanceId(delegateTask.getExecution().getProcessInstanceId()).list();

        Map<String, FlowNode> nextProcUnits = new HashMap<String, FlowNode>();

        Map<String, Boolean> hasGatewayManual = new HashMap<String, Boolean>(1);
        queryNextProcUnits(process, source, variables, nextProcUnits, hasGatewayManual);

        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("nextProcUnits", nextProcUnits);
        result.putAll(hasGatewayManual);

        return result;
    }

    /**
     * 查询流程当前节点的下一步节点，用于流程提示时的提示。
     * 
     * @param process
     *            流程模型
     * @param source
     *            源节点
     * @param variables
     *            流程变量
     * @param result
     *            返回结果
     * @param hasGatewayManual
     *            网关手工选择
     */
    private void queryNextProcUnits(org.activiti.bpmn.model.Process process, FlowNode source, List<HistoricVariableInstance> variables,
                                    Map<String, FlowNode> result, Map<String, Boolean> hasGatewayManual) {
        /**
         * <sequenceFlow id="flow2" sourceRef="exclusiveGw" targetRef="theTask1">
         * <conditionExpression xsi:type="tFormalExpression">${input == 1}</conditionExpression>
         * </sequenceFlow>
         */
        FlowNode target;
        List<SequenceFlow> sequenceFlows = source.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : sequenceFlows) {
            target = (FlowNode) process.getFlowElement(sequenceFlow.getTargetRef());
            if (target instanceof UserTask || target instanceof CallActivity || target instanceof EndEvent) {
                result.put(target.getId(), target);
            } else if (target instanceof ExclusiveGateway) {
                String conditionExpression = sequenceFlow.getConditionExpression();
                // 手工选择
                if (StringUtil.isBlank(conditionExpression) || conditionExpression.contains(MANUAL_SELECT_FLAG)) {
                    hasGatewayManual.put("hasGatewayManual", true);
                    queryNextProcUnits(process, target, variables, result, hasGatewayManual);
                } else {
                    ExpressionFactory factory = new ExpressionFactoryImpl();
                    SimpleContext context = new SimpleContext();
                    for (HistoricVariableInstance var : variables) {
                        context.setVariable(var.getVariableName(), factory.createValueExpression(var.getValue(), var.getValue().getClass()));
                    }
                    ValueExpression e = factory.createValueExpression(context, conditionExpression, boolean.class);
                    if ((Boolean) e.getValue(context)) {
                        result.put(target.getId(), target);
                        break;
                    }
                }
            } else if (target instanceof ParallelGateway) {
                queryNextProcUnits(process, target, variables, result, hasGatewayManual);
            }
        }
    }

}
