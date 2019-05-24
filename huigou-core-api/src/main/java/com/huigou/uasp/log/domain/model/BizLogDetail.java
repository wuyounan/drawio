package com.huigou.uasp.log.domain.model;

/**
 * 日志明细实体接口
 * 
 * @author gongmm
 */
public interface BizLogDetail {
    String getId();

    void setId(String id);

    String getParams();

    void setParams(String params);

    String getErrorMessage();

    void setErrorMessage(String errorMessage);

    String getDescription();

    void setDescription(String description);

    String getBeforeImage();

    void setBeforeImage(String beforeImage);

    String getAfterImage();

    void setAfterImage(String afterImage);

    String getBizLogId();

    void setBizLogId(String bizLogId);

    Long getVersion();

    void setVersion(Long version);

    /**
     * 获取简要的描述
     * 
     * @return
     */
    String getBriefDescription();
}
