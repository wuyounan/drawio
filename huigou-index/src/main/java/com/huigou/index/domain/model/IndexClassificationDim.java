package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 指标分类维度
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_CLASSIFICATION_DIM")
public class IndexClassificationDim extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = 8078337074255183430L;

    public static final Integer DEFAULT_YES_VALUE = 1;

    public static final Integer DEFAULT_NO_VALUE = 0;

    public static final String IS_DEFAULT_FIELD_NAME = "isDefault";

    /**
     * 默认
     **/
    @Column(name = "is_default")
    private Integer isDefault;

    /**
     * 排序号
     **/
    @Column(name = "sequence")
    private Integer sequence;

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
