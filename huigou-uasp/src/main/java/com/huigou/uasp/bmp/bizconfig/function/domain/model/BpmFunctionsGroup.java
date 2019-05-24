package com.huigou.uasp.bmp.bizconfig.function.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 业务功能分组
 * 
 * @author
 *         BPM_FUNCTIONS_GROUP
 * @date 2018-03-28 11:15
 */
@Entity
@Table(name = "BPM_FUNCTIONS_GROUP")
public class BpmFunctionsGroup extends AbstractEntity {

    private static final long serialVersionUID = -7163100618642436364L;

    /**
	*
	**/
    @Column(name = "bpm_functions_id", length = 32)
    private String bpmFunctionsId;

    /**
     * 中文名称
     **/
    @Column(name = "name_zh", length = 64)
    private String nameZh;

    /**
     * 英文名称
     **/
    @Column(name = "name_en", length = 128)
    private String nameEn;

    /**
     * 颜色
     **/
    @Column(name = "color", length = 32)
    private String color;

    /**
     * 序号
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    public String getBpmFunctionsId() {
        return this.bpmFunctionsId;
    }

    public void setBpmFunctionsId(String bpmFunctionsId) {
        this.bpmFunctionsId = bpmFunctionsId;
    }

    public String getNameZh() {
        return this.nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
