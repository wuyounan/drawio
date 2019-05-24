package com.huigou.uasp.bpm;


/**
 * 批量提交任务参数
 * 
 * @author gongmm
 */
public class BatchAdvanceParameter {
    /**
     * 流程实例ID
     */
    private String procInstId;

    /**
     * 流程环节ID
     */
    private String procUnitId;

    /**
     * 任务ID
     */
    private String taskId;

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
     * 当前处理分组ID
     */
    private Integer currentHandleGroupId;

    /**
     * 处理意见
     */
    private String handleOpinion;

    /**
     * 处理结果
     */
    private Integer handleResult;

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

    

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    

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

    

    public Integer getCurrentHandleGroupId() {
        return currentHandleGroupId == null ? 0 : currentHandleGroupId;
    }

    public void setCurrentHandleGroupId(Integer currentHandleGroupId) {
        this.currentHandleGroupId = currentHandleGroupId;
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


    public HandleResult getProcUnitHandlerResult() {
        return HandleResult.fromId(handleResult);
    }
    
    public ApprovalParameter toApprovalParameter(){
        ApprovalParameter approvalParameter  = new ApprovalParameter();
        
        approvalParameter.setProcInstId(procInstId);
        approvalParameter.setProcUnitId(procUnitId);
        approvalParameter.setTaskId(taskId);
        
        approvalParameter.setBizId(bizId);
        approvalParameter.setBizCode(bizCode);
        
        approvalParameter.setCurrentHandleId(currentHandleId);
        approvalParameter.setCurrentHandleGroupId(currentHandleGroupId);
        approvalParameter.setHandleResult(handleResult);
        approvalParameter.setHandleOpinion(handleOpinion);
        
        approvalParameter.setOnlyAdvance(true);
        
        return approvalParameter;
    }

}
