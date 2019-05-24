package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.MessageConstants;

/**
 * 系统参数
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_Parameter")
public class SysParameter extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = -6611101109322215196L;

    private String value;

    private String remark;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void checkConstraints(SysParameter other) {
        super.checkConstraints();
        if (other != null) {
            Assert.isTrue(!this.getCode().equalsIgnoreCase(other.getCode()), MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
            Assert.isTrue(!this.getName().equalsIgnoreCase(other.getName()), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
    }

}
