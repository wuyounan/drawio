package com.huigou.uasp.bpm.engine.domain.dto;

public class BackTaskInfo {

    private Integer processDefinitionGroupId;

    private String taskDefKey;

   

    private String id;

    private String name;

    private String subProcUnitName;

    private String executorFullName;

    private String statusName;

    private Integer groupId;

    private String procUnitHandlerId;

    public Integer getProcessDefinitionGroupId() {
        return processDefinitionGroupId;
    }

    public void setProcessDefinitionGroupId(Integer processDefinitionGroupId) {
        this.processDefinitionGroupId = processDefinitionGroupId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubProcUnitName() {
        return subProcUnitName;
    }

    public void setSubProcUnitName(String subProcUnitName) {
        this.subProcUnitName = subProcUnitName;
    }

    public String getExecutorFullName() {
        return executorFullName;
    }

    public void setExecutorFullName(String executorFullName) {
        this.executorFullName = executorFullName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getProcUnitHandlerId() {
        return procUnitHandlerId;
    }

    public void setProcUnitHandlerId(String procUnitHandlerId) {
        this.procUnitHandlerId = procUnitHandlerId;
    }

}
