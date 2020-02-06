package com.huigou.index.domain.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.AbstractEntity;

/**
 * 指标明细
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_ENTRY")
public class IndexEntry extends AbstractEntity {

    private static final long serialVersionUID = -115984400894304425L;

    public static final String INDEX_ENTRY_FORMULA_PARAM_FIELD_NAME = "inputIndexEntryFormulaParams_";

    public static final String INDEX_ENTRY_UI_PARAM_FIELD_NAME = "inputIndexEntryUIParams_";

    public static final String INDEX_ENTRY_TAB_FIELD_NAME = "inputIndexEntryTabs_";

    /**
     * 指标ID
     **/
    @Column(name = "index_id", length = 32)
    private String indexId;

    /**
     * 时间维度
     **/
    @Column(name = "time_dim", length = 32)
    private String timeDim;

    /**
     * 组织维度
     **/
    @Column(name = "organ_dim", length = 32)
    private String organDim;

    /**
     * 正常上限值
     **/
    @Column(name = "upper_limit", precision = 22, scale = 2)
    private BigDecimal upperLimit;

    /**
     * 正常下限值
     **/
    @Column(name = "lower_limit", precision = 22, scale = 2)
    private BigDecimal lowerLimit;

    /**
     * 公式
     **/
    @Column(name = "formula", length = 128)
    private String formula;

    /**
     * 指标展示类型
     */
    @Column(name = "view_kind", length = 32)
    private String viewKind;

    /**
     * url
     */
    @Column(name = "url", length = 128)
    private String url;

    /**
     * 排序号
     **/
    private Integer sequence;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "entry_id")
    private List<IndexEntryFormulaParam> indexEntryFormulaParams;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "entry_id")
    private List<IndexEntryUIParam> indexEntryUIParams;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "entry_id")
    @OrderBy("sequence")
    private List<IndexEntryTab> indexEntryTabs;

    @Transient
    private List<IndexEntryUIParam> inputIndexEntryUIParams_;

    @Transient
    private List<IndexEntryTab> inputIndexEntryTabs_;

    @Transient
    private List<IndexEntryFormulaParam> inputIndexEntryFormulaParams_;

    public String getIndexId() {
        return this.indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getTimeDim() {
        return this.timeDim;
    }

    public void setTimeDim(String timeDim) {
        this.timeDim = timeDim;
    }

    public String getOrganDim() {
        return this.organDim;
    }

    public void setOrganDim(String organDim) {
        this.organDim = organDim;
    }

    public BigDecimal getUpperLimit() {
        return this.upperLimit;
    }

    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public BigDecimal getLowerLimit() {
        return this.lowerLimit;
    }

    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getViewKind() {
        return viewKind;
    }

    public void setViewKind(String viewKind) {
        this.viewKind = viewKind;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonIgnore
    public List<IndexEntryFormulaParam> getIndexEntryFormulaParams() {
        return indexEntryFormulaParams;
    }

    public void setIndexEntryFormulaParam(List<IndexEntryFormulaParam> indexEntryFormulaParams) {
        this.indexEntryFormulaParams = indexEntryFormulaParams;
    }

    @JsonIgnore
    public List<IndexEntryUIParam> getIndexEntryUIParams() {
        return indexEntryUIParams;
    }

    public void setIndexEntryUIParams(List<IndexEntryUIParam> indexEntryUIParams) {
        this.indexEntryUIParams = indexEntryUIParams;
    }

    @JsonIgnore
    public List<IndexEntryTab> getIndexEntryTabs() {
        return indexEntryTabs;
    }

    public void setIndexEntryTabs(List<IndexEntryTab> indexEntryTabs) {
        this.indexEntryTabs = indexEntryTabs;
    }

    @JsonIgnore
    public List<IndexEntryFormulaParam> getInputIndexEntryFormulaParams_() {
        return inputIndexEntryFormulaParams_;
    }

    @JsonIgnore
    public void setInputIndexEntryFormulaParams_(List<IndexEntryFormulaParam> inputIndexEntryFormulaParams_) {
        this.inputIndexEntryFormulaParams_ = inputIndexEntryFormulaParams_;
    }

    @JsonIgnore
    public List<IndexEntryUIParam> getInputIndexEntryUIParams_() {
        return inputIndexEntryUIParams_;
    }

    @JsonIgnore
    public void setInputIndexEntryUIParams_(List<IndexEntryUIParam> inputIndexEntryUIParams_) {
        this.inputIndexEntryUIParams_ = inputIndexEntryUIParams_;
    }

    @JsonIgnore
    public List<IndexEntryTab> getInputIndexEntryTabs_() {
        return inputIndexEntryTabs_;
    }

    @JsonIgnore
    public void setInputIndexEntryTabs_(List<IndexEntryTab> inputIndexEntryTabs_) {
        this.inputIndexEntryTabs_ = inputIndexEntryTabs_;
    }

    @Override
    public void buildDetails() {
        this.buildDetails(this.getIndexEntryFormulaParams(), this.getInputIndexEntryFormulaParams_());
        this.buildDetails(this.getIndexEntryUIParams(), this.getInputIndexEntryUIParams_());
        this.buildDetails(this.getIndexEntryTabs(), this.getInputIndexEntryTabs_());
    }

    public IndexEntryTab findIndexEntryTabById(String tabId) {
        for (IndexEntryTab indexEntryTab : indexEntryTabs) {

            if (indexEntryTab.getId().equals(tabId)) {
                return indexEntryTab;
            }

        }
        return null;
    }

}
