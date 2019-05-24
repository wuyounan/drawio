package com.huigou.uasp.bmp.opm.domain.model.access;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 界面元素权限
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPUIElementPermission")
public class UIElementPermission extends AbstractEntity {

    private static final long serialVersionUID = 3873727591303057426L;
    
    /**
     * 类别ID 主集 子集 按钮
     */
    @Column(name = "kind_id")
    private String kindId;
    
    private String code;
    
    private String name;
    
    @Column(name="resource_id")
    private String resourceId;
    
    @Column(name="permission_id")
    private String permissionId;

    @Column(name = "operation_id")
    private String operationId;

    private Integer sequence;
    
    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
    
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
