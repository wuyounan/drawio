package com.huigou.uasp.bpm.configuration.domain.model;

import javax.persistence.*;

import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "WF_ApprovalRuleScope")
public class ApprovalRuleScope extends AbstractEntity {

    private static final long serialVersionUID = 6328954622254123308L;

    @Column(name = "org_id")
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
