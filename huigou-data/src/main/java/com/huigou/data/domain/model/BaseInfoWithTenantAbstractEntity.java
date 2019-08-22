package com.huigou.data.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.huigou.annotation.JsonIgnore;

@MappedSuperclass
public abstract class BaseInfoWithTenantAbstractEntity extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = -3862020626108262078L;

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