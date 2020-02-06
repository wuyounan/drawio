package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 公式参数
 * 
 * @author
 *         NG_INDEX_ENTRY_PARAM
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_ENTRY_PARAM")
public class IndexEntryFormulaParam extends AbstractEntity {

    private static final long serialVersionUID = 1113143817765200127L;

//    /**
//     * 明细ID
//     **/
//    @Column(name = "entry_id", length = 32)
//    private String entryId;

    /**
     * 编码
     **/
    @Column(name = "code", length = 32)
    private String code;

    /**
     * 名称
     **/
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 数据类型
     **/
    @Column(name = "data_type", length = 32)
    private String dataType;

    /**
     * 参数值
     **/
    @Column(name = "param_value", length = 64)
    private String paramValue;

    /**
     * 排序号
     **/
    private Integer sequence;

//    public String getEntryId() {
//        return this.entryId;
//    }
//
//    public void setEntryId(String entryId) {
//        this.entryId = entryId;
//    }

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

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
