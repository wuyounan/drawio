package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 数据管理权限维度定义
 * 
 * @author xx
 *         SA_OPDATAKIND
 * @date 2018-09-04 10:52
 */
public class OpdatakindQueryRequest extends QueryAbstractRequest {

    /**
     * 编码
     **/
    protected String code;

    /**
     * 名称
     **/
    protected String name;

    /**
     * 类型
     **/
    protected String dataKind;

    private Integer status;

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

    public String getDataKind() {
        return this.dataKind;
    }

    public void setDataKind(String dataKind) {
        this.dataKind = dataKind;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
