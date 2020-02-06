package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 指标归属分配
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_ATTACH_CLASSIFICATION")
public class IndexAttachClassification extends AbstractEntity {

    private static final long serialVersionUID = 5296385492102243138L;

    /**
     * 指标ID
     **/
    @Column(name = "index_id", length = 32)
    private String indexId;

    /**
     * 分类ID
     **/
    @Column(name = "classification_id", length = 32)
    private String classificationId;

    public String getIndexId() {
        return this.indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getClassificationId() {
        return this.classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

}
