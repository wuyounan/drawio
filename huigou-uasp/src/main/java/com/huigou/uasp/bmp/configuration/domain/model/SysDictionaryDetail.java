package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 系统字典明细
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_DictionaryDetail")
public class SysDictionaryDetail extends AbstractEntity {

    private static final long serialVersionUID = 907425757949408149L;

    private String name;

    private String value;

    private Integer status;

    @Column(name = "type_id")
    private String typeId;

    private String remark;

    private Integer sequence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
