package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.model.TreeEntity;

/**
 * 数据管理权限业务类型
 * 
 * @author xx
 *         SA_OPDATAMANAGEBUSINESS
 * @date 2018-09-27 12:04
 */
@Entity
@Table(name = "SA_OPDATAMANAGEBUSINESS")
public class Opdatamanagebusiness extends TreeEntity {

    private static final long serialVersionUID = -8092369670776787172L;

    /**
     * 节点类型(分类，类型)
     **/
    @Column(name = "node_kind_id", length = 32)
    private String nodeKindId;

    /**
     * REMARK
     **/
    @Column(name = "remark", length = 256)
    private String remark;

    /**
     * 数据管理权限ID
     **/
    @Column(name = "data_manage_id", length = 32)
    private String dataManageId;

    @Transient
    private String dataManageName;

    @Transient
    private Integer hasChildren;

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

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getDataManageName() {
        return dataManageName;
    }

    public void setDataManageName(String dataManageName) {
        this.dataManageName = dataManageName;
    }

    public String getDataManageId() {
        return dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

}
