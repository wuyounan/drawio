package com.huigou.uasp.bmp.bizClassification.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据类型测试
 * 
 * @author xx
 *         HR_TEST_DATA_TYPE
 * @date 2017-04-05 15:46
 */
@Entity
@Table(name = "HR_TEST_DATA_TYPE")
public class TestDataType extends BizClassifyAbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	*
	**/
    @Column(name = "date_time_type", length = 7)
    private Date dateTimeType;

    /**
	*
	**/
    @Column(name = "long_type", length = 22)
    private Integer longType;

    /**
	*
	**/
    @Column(name = "double_type", length = 22)
    private Integer doubleType;

    /**
	*
	**/
    @Column(name = "date_type", length = 7)
    private Date dateType;

    /**
	*
	**/
    @Column(name = "string_type", length = 32)
    private String stringType;

    public Date getDateTimeType() {
        return this.dateTimeType;
    }

    public void setDateTimeType(Date dateTimeType) {
        this.dateTimeType = dateTimeType;
    }

    public Integer getLongType() {
        return this.longType;
    }

    public void setLongType(Integer longType) {
        this.longType = longType;
    }

    public Integer getDoubleType() {
        return this.doubleType;
    }

    public void setDoubleType(Integer doubleType) {
        this.doubleType = doubleType;
    }

    public Date getDateType() {
        return this.dateType;
    }

    public void setDateType(Date dateType) {
        this.dateType = dateType;
    }

    public String getStringType() {
        return this.stringType;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

}
