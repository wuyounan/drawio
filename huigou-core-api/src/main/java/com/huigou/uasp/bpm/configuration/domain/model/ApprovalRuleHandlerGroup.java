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

    public TaskExecuteMode getTaskExecuteMode() {
        return taskExecuteMode;
    }

    public void setTaskExecuteMode(TaskExecuteMode taskExecuteMode) {
        this.taskExecuteMode = taskExecuteMode;
    }

}
