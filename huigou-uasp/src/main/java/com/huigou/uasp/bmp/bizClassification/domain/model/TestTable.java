package com.huigou.uasp.bmp.bizClassification.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * HR测试表
 * 
 * @author xx
 *         HR_TEST_TABLE
 * @date 2017-04-05 15:46
 */
@Entity
@Table(name = "HR_TEST_TABLE")
public class TestTable extends BizClassifyAbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     **/
    @Column(name = "name", length = 32)
    private String name;

    /**
     * 编码
     **/
    @Column(name = "code", length = 32)
    private String code;

    /**
     * 备注
     **/
    @Column(name = "remark", length = 128)
    private String remark;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
