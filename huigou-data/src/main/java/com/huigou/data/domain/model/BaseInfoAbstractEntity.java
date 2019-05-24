package com.huigou.data.domain.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.MessageSourceContext;
import com.huigou.util.StringUtil;

/**
 * 基础数据基类
 * 
 * @author gongmm
 */
@MappedSuperclass
public abstract class BaseInfoAbstractEntity extends AbstractEntity {

    private static final long serialVersionUID = 7026342310145192116L;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BaseInfoAbstractEntity() {

    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();

        Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
        Assert.hasText(name, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.STATUS_NOT_BLANK));
    }

    public void checkConstraints(BaseInfoAbstractEntity other) {
        checkConstraints();
        if (other != null) {
            Assert.isTrue(!this.getCode().equalsIgnoreCase(other.getCode()), MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
            Assert.isTrue(!this.getName().equalsIgnoreCase(other.getName()), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
    }

    public boolean isUpdateName(String oldName) {
        if (oldName == null) {
            return false;
        }
        return !this.isNew() && !this.name.equalsIgnoreCase(oldName);
    }

    @JsonIgnore
    public String getTenantField_() {
        return null;
    }

    @JsonIgnore
    public Serializable getTenantId_() {
        return null;
    }

    @JsonIgnore
    public boolean hasTenant() {
        if (this.getTenantId_() == null) {
            return false;
        }
        if (StringUtil.isBlank(this.getTenantField_())) {
            return false;
        }
        return true;
    }

}
