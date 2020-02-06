package com.huigou.index.domain.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 指标展示界面参数
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_ENTRY_UI_PARAM")
public class IndexEntryUIParam extends AbstractEntity {

    private static final long serialVersionUID = -6223662816402041805L;

    private String code;
    
    private String name;

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    private Integer sequence;

   
}
