package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 数据管理权限包含维度资源
 * 
 * @author xx
 *         SA_OPDATAMANAGDETAILRESOURCE
 * @date 2018-09-05 17:15
 */
@Entity
@Table(name = "SA_OPDATAMANAGEDETAILRESOURCE")
public class Opdatamanagedetailresource extends AbstractEntity {

    private static final long serialVersionUID = 8072625062791736464L;

    /**
     * 数据取值定义ID
     **/
    @Column(name = "data_managedetal_id", length = 32)
    private String dataManagedetalId;

    /**
     * 资源维度ID
     **/
    @Column(name = "data_kind_id", length = 32)
    private String dataKindId;

    /**
     * 资源类型
     **/
    @Column(name = "data_kind", length = 32)
    private String dataKind;

    /**
     * 资源KEY
     **/
    @Column(name = "resource_key", length = 32)
    private String resourceKey;

    /**
     * 资源值
     **/
    @Column(name = "resource_value", length = 128)
    private String resourceValue;

    /**
     * full_id
     **/
    @Column(name = "full_id", length = 1024)
    private String fullId;

    /**
     * full_name
     **/
    @Column(name = "full_name", length = 1024)
    private String fullName;

    /**
     * 组织权限类型
     **/
    @Column(name = "org_data_kind", length = 32)
    private String orgDataKind;

    public String getDataManagedetalId() {
        return this.dataManagedetalId;
    }

    public void setDataManagedetalId(String dataManagedetalId) {
        this.dataManagedetalId = dataManagedetalId;
    }

    public String getDataKindId() {
        return this.dataKindId;
    }

    public void setDataKindId(String dataKindId) {
        this.dataKindId = dataKindId;
    }

    public String getDataKind() {
        return this.dataKind;
    }

    public void setDataKind(String dataKind) {
        this.dataKind = dataKind;
    }

    public String getResourceKey() {
        return this.resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getResourceValue() {
        return this.resourceValue;
    }

    public void setResourceValue(String resourceValue) {
        this.resourceValue = resourceValue;
    }

    public String getFullId() {
        return this.fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrgDataKind() {
        return orgDataKind;
    }

    public void setOrgDataKind(String orgDataKind) {
        this.orgDataKind = orgDataKind;
    }

    public void checkConstraints() {
        Assert.hasText(dataManagedetalId, "dataManagedetalId不能为空!");
        Assert.hasText(dataKindId, "dataKindId不能为空!");
        Assert.hasText(resourceKey, "resourceKey不能为空!");
    }
}
