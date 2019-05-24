package com.huigou.uasp.bmp.bizconfig.function.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 业务功能分组
 * 
 * @author
 *         BPM_FUNCTIONS_GROUP
 * @date 2018-03-28 11:16
 */
public class FunctionsGroupQueryRequest extends QueryAbstractRequest {

    /**
	*
	**/
    protected String bpmFunctionsId;

    /**
     * 中文名称
     **/
    protected String nameZh;

    public String getBpmFunctionsId() {
        return bpmFunctionsId;
    }

    public void setBpmFunctionsId(String bpmFunctionsId) {
        this.bpmFunctionsId = bpmFunctionsId;
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
