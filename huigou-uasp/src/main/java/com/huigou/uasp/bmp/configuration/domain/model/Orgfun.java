package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 系统可用组织机构函数
 * 
 * @author xx
 *         SA_ORGFUN
 * @date 2018-03-09 10:47
 */
@Entity
@Table(name = "SA_ORGFUN")
public class Orgfun extends BaseInfoAbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -8093346566849754655L;

    /**
     * parent_id
     **/
    @Column(name = "parent_id", length = 32)
    private String parentId;

    /**
     * is_last
     **/
    @Column(name = "is_last", length = 22)
    private Integer isLast;

    /**
     * remark
     **/
    @Column(name = "remark", length = 256)
    private String remark;

    /**
     * sequence
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLast() {
        return this.isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
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
