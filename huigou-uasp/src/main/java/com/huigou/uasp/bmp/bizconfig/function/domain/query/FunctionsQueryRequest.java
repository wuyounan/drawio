package com.huigou.uasp.bmp.bizconfig.function.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 业务功能
 * 
 * @author
 *         BPM_FUNCTIONS
 * @date 2018-03-28 11:16
 */
public class FunctionsQueryRequest extends QueryAbstractRequest {

    /**
	*
	**/
    protected String code;

    /**
	*
	**/
    protected String name;

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

}
