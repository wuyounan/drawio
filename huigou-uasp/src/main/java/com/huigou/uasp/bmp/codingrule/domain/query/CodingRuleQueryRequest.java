package com.huigou.uasp.bmp.codingrule.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class CodingRuleQueryRequest extends QueryAbstractRequest {

    private String code;

    private String name;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
