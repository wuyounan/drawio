package com.huigou.uasp.bpm;

import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 审批参数
 * 
 * @author gongmm
 */
public class ApprovalParameter {
    


    public static final String APPROVAL_PARAMETER_KEY = "_approvalParameter_";

    /**
     * 流程动作
     */
    private String processAction;

    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 流程环节ID
     */
    private String procUnitId;

    /**
     * 任务类别ID
     */
   // private String taskKindId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程类别
     */
    //private String processKind;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 业务编码
     */
    private String bizCode;

    /**
     * 当前处理ID
     */
    private String currentHandleId;

    /**
     * 当前处理序号
     */
    //private Integer currentHandleSequence;

    /**
     * 当前处理分组ID
     */
    private Integer currentHandleGroupId;

    /**
     * 当前处理协助模型ID
     */
    private String currentHandleCooperationModelId;

    /**
     * 处理意见
     */
    private String handleOpinion;

    /**
     * 处理结果
     */
    private Integer handleResult;

    private Boolean hasGatewayManual;

    private Boolean backSaveBizData;

    private Boolean onlySaveHandlerData;

    private Boolean onlyAdvance;

    private String manualProcUnitId;

    public String getProcessAction() {
        return processAction;
    }

    public void setProcessAction(String processAction) {
        this.processAction = processAction;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcUnitId() {
        return procUnitId;
    }

    public void setProcUnitId(String procUnitId) {
        this.procUnitId = procUnitId;
    }

//    public String getTaskKindId() {
//        return taskKindId;
//    }
//
//    public void setTaskKindId(String taskKindId) {
//        this.taskKindId = taskKindId;
//    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

//    public String getProcessKind() {
//        return processKind;
//    }
//
//    public void setProcessKind(String processKind) {
//        this.processKind = processKind;
//    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getCurrentHandleId() {
        return currentHandleId;
    }

    public void setCurrentHandleId(String currentHandleId) {
        this.currentHandleId = currentHandleId;
    }

//    public Integer getCurrentHandleSequence() {
//        return currentHandleSequence;
//    }
//
//    public void setCurrentHandleSequence(Integer currentHandleSequence) {
//        this.currentHandleSequence = currentHandleSequence;
//    }

    public Integer getCurrentHandleGroupId() {
        return currentHandleGroupId == null ? 0 : currentHandleGroupId;
    }

    public void setCurrentHandleGroupId(Integer currentHandleGroupId) {
        this.currentHandleGroupId = currentHandleGroupId;
    }

    public String getCurrentHandleCooperationModelId() {
        return currentHandleCooperationModelId;
    }

    public void setCurrentHandleCooperationModelId(String currentHandleCooperationModelId) {
        this.currentHandleCooperationModelId = currentHandleCooperationModelId;
    }

    public String getHandleOpinion() {
        return handleOpinion;
    }

    public void setHandleOpinion(String handleOpinion) {
        this.handleOpinion = handleOpinion;
    }

    public Integer getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(Integer handleResult) {
        this.handleResult = handleResult;
    }

    public Boolean getGatewayManual() {
        return hasGatewayManual == null ? false : hasGatewayManual;
    }

    public void setHasGatewayManual(Boolean hasGatewayManual) {
        this.hasGatewayManual = hasGatewayManual;
    }

    public Boolean getBackSaveBizData() {
        return backSaveBizData == null ? false : backSaveBizData;
    }

    public void setBackSaveBizData(Boolean backSaveBizData) {
        this.backSaveBizData = backSaveBizData;
    }

    public Boolean getOnlySaveHandlerData() {
        return onlySaveHandlerData == null ? false : onlySaveHandlerData;
    }

    public void setOnlySaveHandlerData(Boolean onlySaveHandlerData) {
        this.onlySaveHandlerData = onlySaveHandlerData;
    }

    public Boolean getOnlyAdvance() {
        return onlyAdvance == null ? false : onlyAdvance;
    }

    public void setOnlyAdvance(Boolean onlyAdvance) {
        this.onlyAdvance = onlyAdvance;
    }

    public String getManualProcUnitId() {
        return manualProcUnitId;
    }

    public void setManualProcUnitId(String manualProcUnitId) {
        this.manualProcUnitId = manualProcUnitId;
    }

    public static ApprovalParameter newInstance(SDO params) {
        ApprovalParameter result = params.toObject(ApprovalParameter.class);
        if (StringUtil.isBlank(result.getBizCode())) {
            result.setBizCode(params.getString("billCode"));
        }
        return result;
    }

    /**
     * 是否为指定的命令
     * 
     * @param processAction
     * @return
     */
    public boolean isSpecifiedProcessAction(String processAction) {
        return this.processAction.equalsIgnoreCase(processAction);
    }

    /**
     * 是否流转
     * 
     * @return 是否流转
     */
    public boolean isAdvanceProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.ADVANCE) || isSpecifiedProcessAction(ProcessAction.QUERY_ADVANCE);
    }

    public boolean isQueryAdvanceProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.QUERY_ADVANCE);
    }

    public boolean isSaveProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.SAVE);
    }

    /**
     * 是否撤回
     * 
     * @return 是否撤回
     */
    public boolean isWithdrawProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.WITHDRAW);
    }

    /**
     * 是否回退操作
     * 
     * @return
     */
    public boolean isBackProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.BACK);
    }

    public boolean isReplenishProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.REPLENISH);
    }

    /**
     * 是否转发操作
     * 
     * @return
     */
    public boolean isTransmitProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.TRANSMIT);
    }

    /**
     * 是否协审操作
     * 
     * @return
     */
    public boolean isAssistProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.ASSIST);
    }

    // /**
    // * 是否终止操作
    // *
    // * @return
    // */
    // public boolean isAbortProcessAction() {
    // return isSpecifiedProcessAction(ProcessAction.ABORT);
    // }

    /**
     * 是否删除流程实例操作
     * 
     * @return
     */
    public boolean isDeleteProcessInstanceProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.DELETE_PROCESS_INSTANCE);
    }

    /**
     * 是否撤销流程实例操作
     * 
     * @return
     */
    public boolean isRecallProcessInstanceProcessAction() {
        return isSpecifiedProcessAction(ProcessAction.RECALL_PROCESS_INSTANCE);
    }

    /**
     * 得到处理结果
     * 
     * @return
     */
    public HandleResult getProcUnitHandlerResult() {
        if (isReplenishProcessAction()) {
            return HandleResult.REPLENISH;
        }

        return HandleResult.fromId(handleResult);
    }

    /**
     * 得到环节处理状态
     * 
     * @return
     */
    public ProcUnitHandler.Status getProcUnitHandlerStatus() {
        return isAdvanceProcessAction() || isReplenishProcessAction() ? ProcUnitHandler.Status.COMPLETED : ProcUnitHandler.Status.READY;
    }

}
