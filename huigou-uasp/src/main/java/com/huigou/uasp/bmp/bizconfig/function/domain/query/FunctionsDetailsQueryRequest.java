package com.huigou.uasp.bmp.bizconfig.function.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 业务功能对应连接
 * 
 * @author
 *         BPM_FUNCTIONS_DETAILS
 * @date 2018-03-28 11:16
 */
public class FunctionsDetailsQueryRequest extends QueryAbstractRequest {

    /**
     * 业务功能ID
     **/
    protected String bpmFunctionsId;

    /**
     * 分组ID
     **/
    protected String functionsGroupId;

    /**
     * 对应功能编码
     **/
    protected String code;

    /**
     * 中文名
     **/
    protected String nameZh;

    public String getBpmFunctionsId() {
        return bpmFunctionsId;
    }

    public void setBpmFunctionsId(String bpmFunctionsId) {
        this.bpmFunctionsId = bpmFunctionsId;
    }

    public String getFunctionsGroupId() {
        return functionsGroupId;
    }

    public void setFunctionsGroupId(String functionsGroupId) {
        this.functionsGroupId = functionsGroupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public void checkConstraints() {
        Assert.hasText(bpmFunctionsId, "bpmFunctionsId不能为空!");
    }

}
