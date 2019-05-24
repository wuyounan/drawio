package com.huigou.uasp.log.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.huigou.util.StringUtil;

/**
 * 日志明细实体
 * 
 * @author yuanwf
 *
 */
@Document(collection = "SA.OperationLogDetail")
public class MongoDBBizLogDetail implements BizLogDetail {
	/**
	 * ID
	 */
	@Id
	@Field("operationLogDetailId")
	private String id;
	/**
	 * 日志ID
	 */
	@Indexed
	@Field("operationLogId")
	private String bizLogId;
	/**
	 * 参数详细
	 */
	private String params;
	/**
	 * 错误信息
	 */
	private String errorMessage;
	/**
	 * 摘要
	 */
	private String description;
	/**
	 * 前象
	 */
	private String beforeImage;
	/**
	 * 后象
	 */
	private String afterImage;
	/**
	 * 版本号
	 */
	private Long version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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
