package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 数据管理权限取值定义
 * 
 * @author xx
 *         SA_OPDATAMANAGDETAIL
 * @date 2018-09-05 17:15
 */
@Entity
@Table(name = "SA_OPDATAMANAGEDETAIL")
public class Opdatamanagedetail extends AbstractEntity {

    private static final long serialVersionUID = 4153398583607751379L;

    /**
     * 数据管理权限类别
     **/
    @Column(name = "data_manage_id", length = 32)
    private String dataManageId;

    /**
     * 名称
     **/
    @Column(name = "name", length = 128)
    private String name;

    /**
     * 备注
     **/
    @Column(name = "remark", length = 256)
    private String remark;

    /**
     * SEQUENCE
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    public String getDataManageId() {
        return this.dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
