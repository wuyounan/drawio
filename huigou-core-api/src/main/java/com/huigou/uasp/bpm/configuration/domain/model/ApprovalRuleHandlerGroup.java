package com.huigou.uasp.bpm.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "WF_ApprovalRuleHandlerGroup")
public class ApprovalRuleHandlerGroup extends AbstractEntity {

    private static final long serialVersionUID = 6191422828869578545L;

    @Column(name = "group_id")
    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 任务执行
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "task_execute_mode_id")
    private TaskExecuteMode taskExecuteMode;
    /**
     * 最少审批人数
     *
     * @since 1.3.3
     */
    @Column(name = "limit_handler")
    private Integer limitHandler;

    public TaskExecuteMode getTaskExecuteMode() {
        return taskExecuteMode;
    }

    public void setTaskExecuteMode(TaskExecuteMode taskExecuteMode) {
        this.taskExecuteMode = taskExecuteMode;
    }

    public Integer getLimitHandler() {
        return limitHandler;
    }

    public void setLimitHandler(Integer limitHandler) {
        this.limitHandler = limitHandler;
    }
}
