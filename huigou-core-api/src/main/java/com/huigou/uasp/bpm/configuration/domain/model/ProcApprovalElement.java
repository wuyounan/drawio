package com.huigou.uasp.bpm.configuration.domain.model;

import javax.persistence.*;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;

@Entity
@Table(name="WF_ProcApprovalElement")
public class ProcApprovalElement extends AbstractEntity {

    private static final long serialVersionUID = -2423858122678646335L;

    /**
     * 流程定义
     */
    @ManyToOne()
    @JoinColumn(name = "proc_id")
    private ProcDefinition procDefinition;

    /**
     * 审批要素
     */
    @ManyToOne()
    @JoinColumn(name="element_id")
    private ApprovalElement approvalElement;
    
    private Integer sequence;
    
    public ProcDefinition getProcDefinition() {
        return procDefinition;
    }

    public void setProcDefinition(ProcDefinition procDefinition) {
        this.procDefinition = procDefinition;
    }

    public ApprovalElement getApprovalElement() {
        return approvalElement;
    }

    public void setApprovalElement(ApprovalElement approvalElement) {
        this.approvalElement = approvalElement;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
