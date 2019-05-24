package com.huigou.uasp.bmp.bizconfig.function.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 业务功能
 * 
 * @author
 *         BPM_FUNCTIONS
 * @date 2018-03-28 11:15
 */
@Entity
@Table(name = "BPM_FUNCTIONS")
public class BpmFunctions extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = -3154474700798330360L;

    /**
	*
	**/
    @Column(name = "remark", length = 256)
    private String remark;

    /**
	*
	**/
    @Column(name = "check_bean_name", length = 64)
    private String checkBeanName;

    /**
	*
	**/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    @Column(name = "is_to_hide", length = 22)
    private Integer isToHide;

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCheckBeanName() {
        return this.checkBeanName;
    }

    public void setCheckBeanName(String checkBeanName) {
        this.checkBeanName = checkBeanName;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIsToHide() {
        return isToHide;
    }

    public void setIsToHide(Integer isToHide) {
        this.isToHide = isToHide;
    }

}
