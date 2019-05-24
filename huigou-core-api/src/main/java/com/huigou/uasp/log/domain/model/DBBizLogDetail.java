package com.huigou.uasp.log.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.util.StringUtil;

/**
 * DB数据库日志明细实体
 * 
 * @author yuanwf
 */
@Entity
@Table(name = "SA_OperationLogDetail")
public class DBBizLogDetail extends AbstractEntity implements BizLogDetail {

    private static final long serialVersionUID = -2614756860142185287L;

    /**
     * 日志ID
     */
    @Column(name = "OperationLog_Id")
    private String bizLogId;

    /**
     * 参数详细
     */
    @Column(name = "Params")
    private String params;

    /**
     * 错误信息
     */
    @Column(name = "Error_Message")
    private String errorMessage;

    /**
     * 摘要
     */
    @Column(name = "Description")
    private String description;

    /**
     * 前象
     */
    @Column(name = "Before_Image")
    private String beforeImage;

    /**
     * 后象
     */
    @Column(name = "After_Image")
    private String afterImage;

    public String getBizLogId() {
        return bizLogId;
    }

    public void setBizLogId(String bizLogId) {
        this.bizLogId = bizLogId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public void setBeforeImage(String beforeImage) {
        this.beforeImage = beforeImage;
    }

    public String getAfterImage() {
        return afterImage;
    }

    public void setAfterImage(String afterImage) {
        this.afterImage = afterImage;
    }

    @Override
    public String getBriefDescription() {
        if (StringUtil.isNotBlank(description)) {
            if (description.length() > BizLog.DESCRIPTION_MAX_LENGTH) {
                return description.substring(0, BizLog.DESCRIPTION_MAX_LENGTH);
            }
        }
        return description;
    }
}
