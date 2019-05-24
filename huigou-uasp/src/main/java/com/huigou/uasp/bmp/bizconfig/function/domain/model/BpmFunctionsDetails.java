package com.huigou.uasp.bmp.bizconfig.function.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 业务功能对应连接
 * 
 * @author
 *         BPM_FUNCTIONS_DETAILS
 * @date 2018-03-28 11:16
 */
@Entity
@Table(name = "BPM_FUNCTIONS_DETAILS")
public class BpmFunctionsDetails extends AbstractEntity {

    private static final long serialVersionUID = -4389285411718084160L;

    /**
     * 业务功能ID
     **/
    @Column(name = "bpm_functions_id", length = 32)
    private String bpmFunctionsId;

    /**
     * 分组ID
     **/
    @Column(name = "functions_group_id", length = 32)
    private String functionsGroupId;

    /**
     * 对应功能编码
     **/
    @Column(name = "code", length = 32)
    private String code;

    /**
     * 中文名
     **/
    @Column(name = "name_zh", length = 64)
    private String nameZh;

    /**
     * 英文名
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

    /**
     * 颜色
     **/
    @Column(name = "url", length = 128)
    private String url;

    @Transient
    private String funUrl;

    @Transient
    private String icon;

    public String getBpmFunctionsId() {
        return this.bpmFunctionsId;
    }

    public void setBpmFunctionsId(String bpmFunctionsId) {
        this.bpmFunctionsId = bpmFunctionsId;
    }

    public String getFunctionsGroupId() {
        return this.functionsGroupId;
    }

    public void setFunctionsGroupId(String functionsGroupId) {
        this.functionsGroupId = functionsGroupId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFunUrl() {
        return funUrl;
    }

    public void setFunUrl(String funUrl) {
        this.funUrl = funUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
