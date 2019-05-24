package com.huigou.uasp.bmp.opm.domain.model.access;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.TreeEntity;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;

/**
 * 权限
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPPermission")
public class Permission extends TreeEntity {

    private static final long serialVersionUID = 3643036146649583444L;

    public static String ROOT_ID = "0";

    public static String FUN_ROOT_ID = "1";

    public static String ALL_OPERATOR_ID = "*";

    /**
     * 资源类别
     */
    @Column(name = "resource_kind_id")
    private String resourceKindId;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "operation_id")
    private String operationId;

    @Column(name = "node_kind_id")
    private String nodeKindId;

    private String remark;

    public String getResourceKindId() {
        return resourceKindId;
    }

    public void setResourceKindId(String resourceKindId) {
        this.resourceKindId = resourceKindId;
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

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonIgnore
    public boolean isFunctionResource() {
        return PermissionResourceKind.FUN.getId().equals(this.getResourceKindId());
    }

    @JsonIgnore
    public boolean isRemindResource() {
        return PermissionResourceKind.REMIND.getId().equals(this.getResourceKindId());
    }

    @JsonIgnore
    public boolean isBusinessClassResource() {
        return PermissionResourceKind.BUSINESSCLASS.getId().equals(this.getResourceKindId());
    }

    @JsonIgnore
    public boolean isPermission() {
        return PermissionNodeKind.PERMISSION.getId().equals(this.nodeKindId);
    }
    
    @JsonIgnore
    public boolean isFirstLevel(){
        return "0".equals(this.getParentId());
    }

}
