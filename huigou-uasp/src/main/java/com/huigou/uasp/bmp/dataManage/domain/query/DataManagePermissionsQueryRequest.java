package com.huigou.uasp.bmp.dataManage.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.util.StringUtil;

public class DataManagePermissionsQueryRequest extends QueryAbstractRequest {

    private String dataManagedetalId;

    private String keyword;

    private String singlePerson;

    public String getDataManagedetalId() {
        return dataManagedetalId;
    }

    public void setDataManagedetalId(String dataManagedetalId) {
        this.dataManagedetalId = dataManagedetalId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSinglePerson() {
        return singlePerson;
    }

    public void setSinglePerson(String singlePerson) {
        this.singlePerson = singlePerson;
    }

    public boolean isSingle() {
        if (StringUtil.isBlank(singlePerson)) {
            return false;
        }
        return singlePerson.equals("1");
    }

    @Override
    public void checkConstraints() {
        Assert.hasText(this.dataManagedetalId, "参数dataManagedetalId不能为空。");
    }

}
