package com.huigou.report.cubesviewer.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;

/**
 * CubesViewer用户查询方案
 * 
 * @author
 *         gongmm
 */
@Entity
@Table(name = "RP_CV_OPERATOR_QUERY_SCHEME")
@EntityListeners({ CreatorAndModifierListener.class })
public class CubesViewerOperatorQueryScheme extends AbstractEntity {

    private static final long serialVersionUID = 557599247340529597L;

    /**
     * 功能编码
     **/
    @Column(name = "function_code", length = 64)
    private String functionCode;

    /**
     * 方案名称
     **/
    @Column(name = "name", length = 128)
    private String name;

    /**
     * 是否默认
     **/
    @Column(name = "is_default")
    private Integer isDefault;

    /**
     * JSON数据
     **/
    @Column(name = "json", length = 4000)
    private String json;

    @Embedded
    private Creator creator;

    public String getFunctionCode() {
        return this.functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    @Override
    public void checkConstraints() {
        Assert.hasText(this.name, "方案名称不能为空。");
        Assert.hasText(this.functionCode, "功能名称不能为空。");
        Assert.hasText(this.json, "json不能为空。");
    }

}
