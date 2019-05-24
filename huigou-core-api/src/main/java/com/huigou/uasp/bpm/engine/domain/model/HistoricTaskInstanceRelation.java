package com.huigou.uasp.bpm.engine.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;

@Entity
@Table(name = "ACT_HI_Taskinst_Relation")
@EntityListeners({ CreatorAndModifierListener.class })
public class HistoricTaskInstanceRelation extends AbstractEntity {

    private static final long serialVersionUID = -3888214882718096340L;

    /**
     * 创建人
     */
    @Embedded
    private Creator creator;

    /**
     * 流程实例ID
     */
    @Column(name = "proc_inst_id")
    private String procInstId;

    /**
     * 业务ID
     */
    @Column(name = "business_key")
    private String businessKey;

    @Column(name = "task_id")
    private String taskId;

    /**
     * 关联任务ID
     */
    @Column(name = "related_task_id")
    private String relatedTaskId;

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRelatedTaskId() {
        return relatedTaskId;
    }

    public void setRelatedTaskId(String relatedTaskId) {
        this.relatedTaskId = relatedTaskId;
    }

}
