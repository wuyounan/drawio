package com.huigou.index.domain.query;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;

/**
 * 指标
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
public class IndexQueryRequest extends CodeAndNameQueryRequest {

    /**
     * 指标ID全路径
     */
    private String fullId;

    /**
     * 周期类型
     **/
    private String indexPeriodKind;

    /**
     * 指标ID
     */
    private String indexId;

    /**
     * 指标明细ID
     */
    private String entryId;

    /**
     * 指标分类ID
     */
    private String classificationId;

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getIndexPeriodKind() {
        return indexPeriodKind;
    }

    public void setIndexPeriodKind(String indexPeriodKind) {
        this.indexPeriodKind = indexPeriodKind;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

}
