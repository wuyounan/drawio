package com.huigou.uasp.bmp.bizClassification.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.huigou.data.domain.model.AbstractEntity;


@MappedSuperclass
public class BizClassifyAbstractEntity extends AbstractEntity {


    private static final long serialVersionUID = 2447326206225353679L;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "biz_code")
    private String bizCode;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

}
