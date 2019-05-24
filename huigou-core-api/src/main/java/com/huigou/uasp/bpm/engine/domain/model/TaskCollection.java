package com.huigou.uasp.bpm.engine.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 任务收藏
 * 
 * @author gongmm
 */
@Entity
@Table(name="WF_TaskCollection")
public class TaskCollection extends AbstractEntity {

    private static final long serialVersionUID = 8943224760386378277L;

    @Column(name="task_id")
    private String taskId;

    @Column(name="person_id")
    private String personId;
    
    @Column(name="created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
