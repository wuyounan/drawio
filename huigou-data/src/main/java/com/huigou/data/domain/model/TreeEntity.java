package com.huigou.data.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.huigou.util.CommonUtil;

@MappedSuperclass
public abstract class TreeEntity extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = 3002868618050177385L;

    /**
     * 父节点ID
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * ID全路径
     */
    @Column(name = "full_id", length = 1024)
    private String fullId;

    /**
     * 名称全路径
     */
    @Column(name = "full_name", length = 1024)
    private String fullName;

    private Integer sequence;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void buildFullIdAndName(TreeEntity parent) {
        String fullId = CommonUtil.createFileFullName(parent == null || parent.getFullId() == null ? "" : parent.getFullId(), this.getId().toString(), "");
        String fullName = CommonUtil.createFileFullName(parent == null || parent.getFullName() == null ? "" : parent.getFullName(), this.getName(), "");
        this.setFullId(fullId);
        this.setFullName(fullName);
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
