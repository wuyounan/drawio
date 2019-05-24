package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.MessageConstants;

/**
 * 数据库数据国际化支持
 * 
 * @author xx
 *         SA_I18NPROPERTIES
 * @date 2017-09-29 10:23
 */
@Entity
@Table(name = "SA_I18NPROPERTIES")
public class I18nproperties extends AbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 4737005452994043121L;

    /**
     * i18n 编码
     **/
    @Column(name = "code", length = 32)
    private String code;

    /**
     * 语言值1(如中文显示数据)
     **/
    @Column(name = "value1", length = 256)
    private String value1;

    /**
     * 语言值2(如英文显示数据)
     **/
    @Column(name = "value2", length = 256)
    private String value2;

    /**
     * 语言值3
     **/
    @Column(name = "value3", length = 256)
    private String value3;

    /**
     * 语言值4
     **/
    @Column(name = "value4", length = 256)
    private String value4;

    /**
     * 语言值5
     **/
    @Column(name = "value5", length = 256)
    private String value5;

    /**
	*
	**/
    @Column(name = "remark", length = 512)
    private String remark;

    /**
     * 类别
     **/
    @Column(name = "folder_id", length = 32)
    private String folderId;

    @Column(name = "resource_kind", length = 32)
    private String resourceKind;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue1() {
        return this.value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return this.value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return this.value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return this.value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        return this.value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getResourceKind() {
        return resourceKind;
    }

    public void setResourceKind(String resourceKind) {
        this.resourceKind = resourceKind;
    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();
        Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
        Assert.hasText(resourceKind, MessageSourceContext.getMessage("resourceKind"));
    }

    public void checkConstraints(I18nproperties other) {
        checkConstraints();
        if (other != null) {
            Assert.isTrue(!this.getCode().equalsIgnoreCase(other.getCode()), MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
        }
    }
}
