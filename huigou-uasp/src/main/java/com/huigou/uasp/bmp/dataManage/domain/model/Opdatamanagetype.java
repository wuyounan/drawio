package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.model.TreeEntity;

/**
 * 数据管理权限类型
 * 
 * @author xx
 *         SA_OPDATAMANAGETYPE
 * @date 2018-09-04 11:58
 */
@Entity
@Table(name = "SA_OPDATAMANAGETYPE")
public class Opdatamanagetype extends TreeEntity {

    private static final long serialVersionUID = 462537657855307761L;

    /**
     * 节点类型(分类，数据权限)
     **/
    @Column(name = "node_kind_id", length = 32)
    private String nodeKindId;

    /**
     * REMARK
     **/
    @Column(name = "remark", length = 256)
    private String remark;

    @Transient
    private Integer hasChildren;

    public String getNodeKindId() {
        return this.nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getRemark() {
        return this.remark;
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

}
