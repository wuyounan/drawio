package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 指标公式关系
 * 
 * @author
 *         NG_INDEX_ENTRY_RELATION
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_ENTRY_RELATION")
public class IndexEntryRelation extends AbstractEntity {

    private static final long serialVersionUID = -2570043723551936459L;

    /**
     * 明细ID
     **/
    @Column(name = "entry_id", length = 32)
    private String entryId;

    /**
     * 父ID
     **/
    @Column(name = "parent_id", length = 32)
    private String parentId;

    public String getEntryId() {
        return this.entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
