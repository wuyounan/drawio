package com.huigou.uasp.bpm;

import com.huigou.uasp.bpm.engine.domain.model.TaskExtension;

public class MessageSendModel {

    private String senderId;

    private String receiverId;

    private String remark;

    private Boolean isSend;

    private TaskExtension taskExtension;

    public MessageSendModel(TaskExtension taskExtension) {
        this.taskExtension = taskExtension;
    }

    public TaskExtension getTaskExtension() {
        return taskExtension;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public String getExecutorUrl() {
        String urlFormat = String.format("bizId=%s&taskId=%s", this.taskExtension.getBusinessKey(), this.taskExtension.getId());
        String executorUrl = this.taskExtension.getExecutorUrl();
        if (executorUrl.indexOf("?") > -1) {
            executorUrl = String.format("%s&%s", executorUrl, urlFormat);
        } else {
            executorUrl = String.format("%s?%s", executorUrl, urlFormat);
        }
        return executorUrl;
    }
}
