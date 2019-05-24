package com.huigou.uasp.bmp.bizconfig.process.domain.query;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;

/**
 * 流程定义
 *
 * @author xx
 * @date 2017-04-07 14:00
 */
public class BusinessProcessQueryRequest extends ParentAndCodeAndNameQueryRequest {

    /**
     * 全路径ID
     **/
    protected String fullId;

    /**
     * 是否末级
     **/
    private Integer isFinal;

    /**
     * E化
     **/
    private Integer isElectronization;

    /**
     * 用户编号
     **/
    private String userCode;

    /**
     * 所属人ID
     **/
    private String ownerId;

    public String getFullId() {
        return this.fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public Integer getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Integer isFinal) {
        this.isFinal = isFinal;
    }

    public Integer getIsElectronization() {
        return isElectronization;
    }

    public void setIsElectronization(Integer isElectronization) {
        this.isElectronization = isElectronization;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
