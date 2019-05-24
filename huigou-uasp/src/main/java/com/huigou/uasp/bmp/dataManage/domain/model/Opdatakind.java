package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 数据管理权限维度定义
 * 
 * @author xx
 *         SA_OPDATAKIND
 * @date 2018-09-04 10:52
 */
@Entity
@Table(name = "SA_OPDATAKIND")
public class Opdatakind extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = 6754811001467922411L;

    /**
     * 类型
     **/
    @Column(name = "data_kind", length = 32)
    private String dataKind;

    /**
     * 数据源
     **/
    @Column(name = "data_source", length = 256)
    private String dataSource;

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

    public String getDataKind() {
        return this.dataKind;
    }

    public void setDataKind(String dataKind) {
        this.dataKind = dataKind;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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
