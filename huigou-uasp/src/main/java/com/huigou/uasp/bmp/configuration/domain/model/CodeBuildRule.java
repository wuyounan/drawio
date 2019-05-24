package com.huigou.uasp.bmp.configuration.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.MessageConstants;

/**
 * 单据编码生成规则
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_CodeBuildRule")
public class CodeBuildRule extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = -7148653369282197893L;

    private String rule;

    @Column(name = "current_value")
    private Integer currentValue;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Transient
    private Integer lastValue;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getLastValue() {
        return lastValue;
    }

    public void setLastValue(Integer lastValue) {
        this.lastValue = lastValue;
    }

    public void checkConstraints(CodeBuildRule other) {
        super.checkConstraints();
        if (other != null) {
            Assert.isTrue(!this.getCode().equals(other.getCode()), MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
            Assert.isTrue(!this.getName().equals(other.getName()), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
    }
}
