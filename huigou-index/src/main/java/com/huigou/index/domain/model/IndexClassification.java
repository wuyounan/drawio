package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.TreeEntity;

/**
 * 指标分类
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_CLASSIFICATION")
public class IndexClassification extends TreeEntity {

    private static final long serialVersionUID = 1840595772123743777L;

    public static final String ROOT_ID = "1";

    public static final String ROOT_PARENT_ID = "0";

    private String icon;

    /**
     * 指标分类维度ID
     **/
    @Column(name = "dim_id", length = 32)
    private String dimId;

    public String getDimId() {
        return this.dimId;
    }

    public void setDimId(String dimId) {
        this.dimId = dimId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void buildFullIdAndName(TreeEntity parent) {
        if (parent != null) {
            super.buildFullIdAndName(parent);
        }
    }

}
