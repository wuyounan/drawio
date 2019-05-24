package com.huigou.uasp.bpm.engine.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 环节处理人手写意见
 * 
 * @author gongmm
 */
@Entity
@Table(name = "WF_ProcUnitHandlerManuscript")
public class ProcUnitHandlerManuscript extends AbstractEntity {

    private static final long serialVersionUID = 3814981128489972823L;
    
    @Column(name="biz_id")
    private String bizId;

    @Column(name="proc_unit_handler_id")
    private String procUnitHandlerId;
    
    private Integer height;
    
    @Column(name="opinion_30")
    private String opinion30;
    
    @Column(name="opinion_64")
    private String opinion64;
    
    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getProcUnitHandlerId() {
        return procUnitHandlerId;
    }

    public void setProcUnitHandlerId(String procUnitHandlerId) {
        this.procUnitHandlerId = procUnitHandlerId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getOpinion30() {
        return opinion30;
    }

    public void setOpinion30(String opinion30) {
        this.opinion30 = opinion30;
    }

    public String getOpinion64() {
        return opinion64;
    }

    public void setOpinion64(String opinion64) {
        this.opinion64 = opinion64;
    }
}
