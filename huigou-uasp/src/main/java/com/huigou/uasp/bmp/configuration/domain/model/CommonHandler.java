package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 通用处理人
 */
@Entity
@Table(name = "SA_CommonHandler")
public class CommonHandler extends AbstractEntity {

    private static final long serialVersionUID = -3258585796774549879L;

    @Column(name = "biz_id")
    private String bizId;

    @Column(name = "sub_proc_unit_name")
    private String subProcUnitName;

    @Column(name = "kind_id")
    private String kindId;

    @Column(name = "org_Unit_Id")
    private String orgUnitId;

    @Column(name = "org_Unit_Name")
    private String orgUnitName;

    @Column(name = "org_Kind_Id")
    private String orgKindId;

    @Column(name = "full_Id")
    private String fullId;

    @Column(name = "group_id")
    private Integer groupId;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getSubProcUnitName() {
        return subProcUnitName;
    }

    public void setSubProcUnitName(String subProcUnitName) {
        this.subProcUnitName = subProcUnitName;
    }

    public String getKindId() {
        return this.kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getOrgUnitId() {
        return this.orgUnitId;
    }

    public void setOrgUnitId(String orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitName() {
        return this.orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getOrgKindId() {
        return this.orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    public String getFullId() {
        return this.fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    private Integer sequence;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
