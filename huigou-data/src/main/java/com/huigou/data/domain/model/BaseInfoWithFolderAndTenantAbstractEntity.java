package com.huigou.data.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.huigou.annotation.JsonIgnore;

@MappedSuperclass
public abstract class BaseInfoWithFolderAndTenantAbstractEntity extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 7416041676575866641L;

    @Column(name = "tenant_id")
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    @JsonIgnore
    public String getTenantField_() {
        return "tenantId";
    }

    @Override
    @JsonIgnore
    public String getTenantId_() {
        return tenantId;
    }

}
