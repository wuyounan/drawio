package com.huigou.uasp.bmp.opm.domain.model.management;

import javax.persistence.*;

import com.huigou.data.domain.model.TreeEntity;

@Entity
@Table(name = "SA_OPBizManagementType")
public class BizManagementType extends TreeEntity {

    private static final long serialVersionUID = 2951719566658268931L;

    /**
     * 类别
     */
    @Column(name = "kind_id")
    private Integer kindId;

    /**
     * 节点类型ID
     */
    @Column(name = "node_kind_id")
    private Integer nodeKindId;

    /**
     * 备注
     */
    private String remark;

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public Integer getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(Integer nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
