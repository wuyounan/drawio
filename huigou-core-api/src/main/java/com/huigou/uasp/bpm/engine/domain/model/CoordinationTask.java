package com.huigou.uasp.bpm.engine.domain.model;

import java.util.List;
import java.util.Map;

/**
 * 协同任务
 * 
 * @author gongmm
 */
public class CoordinationTask {
    
    /**
     * 发送人员成员ID
     */
    private String initiatorPersonMemberId;

    /**
     * 任务类型
     */
    private String taskKind;

    /**
     * 流程环节ID
     */
    private String procUnitId;

    /**
     * 流程环节名称
     */
    private String procUnitName;

    /**
     * 标题
     */
    private String description;

    /**
     * URL
     */
    private String formKey;

    /**
     * 业务ID
     */
    private String businessKey;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 环节处理人
     */
    private String procUnitHandlerId;

    /**
     * 执行人ID
     */
    List<String> executorIds;

    /**
     * 附加数据
     */
    private Map<String, Object> additiveData;

    public String getInitiatorPersonMemberId() {
        return initiatorPersonMemberId;
    }

    public void setInitiatorPersonMemberId(String initiatorPersonMemberId) {
        this.initiatorPersonMemberId = initiatorPersonMemberId;
    }

    
    public String getTaskKind() {
        return taskKind;
    }

    public void setTaskKind(String taskKind) {
        this.taskKind = taskKind;
    }

    public String getProcUnitId() {
        return procUnitId;
    }

    public void setProcUnitId(String procUnitId) {
        this.procUnitId = procUnitId;
    }

    public String getProcUnitName() {
        return procUnitName;
    }

    public void setProcUnitName(String procUnitName) {
        this.procUnitName = procUnitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getProcUnitHandlerId() {
        return procUnitHandlerId;
    }

    public void setProcUnitHandlerId(String procUnitHandlerId) {
        this.procUnitHandlerId = procUnitHandlerId;
    }

    public List<String> getExecutorIds() {
        return executorIds;
    }

    public void setExecutorIds(List<String> executorIds) {
        this.executorIds = executorIds;
    }

    public Map<String, Object> getAdditiveData() {
        return additiveData;
    }

    public void setAdditiveData(Map<String, Object> additiveData) {
        this.additiveData = additiveData;
    }


}
