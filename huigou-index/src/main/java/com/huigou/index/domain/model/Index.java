package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 指标
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX")
public class Index extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = -6361029926512804803L;

    @Column(name = "start_process")
    private Integer startProcess;

    /**
     * 业务类型
     **/
    @Column(name = "index_biz_kind", length = 32)
    private String indexBizKind;

    /**
     * 描述
     **/
    @Column(name = "description", length = 256)
    private String description;

    /**
     * 详细信息
     **/
    @Column(name = "detail_description", length = 2048)
    private String detailDescription;

    /**
     * 周期类型
     **/
    @Column(name = "index_period_kind", length = 32)
    private String indexPeriodKind;

    /**
     * 指标级别
     **/
    @Column(name = "index_grade", length = 32)
    private String indexGrade;

    /**
     * 指标显示顺序
     **/
    @Column(name = "display_sequence")
    private Integer displaySequence;

    @Column(name = "county_handle")
    private Integer countyHandle;

    @Column(name = "city_handle")
    private Integer cityHandle;

    @Column(name = "province_handle")
    private Integer provinceHandle;

    /**
     * 排序号
     **/
    private Integer sequence;

    @Column(name = "classification_id", length = 32)
    private String classificationId;

    public Integer getStartProcess() {
        return startProcess;
    }

    public void setStartProcess(Integer startProcess) {
        this.startProcess = startProcess;
    }

    public String getIndexBizKind() {
        return this.indexBizKind;
    }

    public void setIndexBizKind(String indexBizKind) {
        this.indexBizKind = indexBizKind;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailDescription() {
        return this.detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getIndexPeriodKind() {
        return indexPeriodKind;
    }

    public void setIndexPeriodKind(String indexPeriodKind) {
        this.indexPeriodKind = indexPeriodKind;
    }

    public String getIndexGrade() {
        return indexGrade;
    }

    public void setIndexGrade(String indexGrade) {
        this.indexGrade = indexGrade;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getDisplaySequence() {
        return displaySequence;
    }
    
    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }

    public Integer getCountyHandle() {
        return countyHandle;
    }

    public void setCountyHandle(Integer countyHandle) {
        this.countyHandle = countyHandle;
    }

    public Integer getCityHandle() {
        return cityHandle;
    }

    public void setCityHandle(Integer cityHandle) {
        this.cityHandle = cityHandle;
    }

    public Integer getProvinceHandle() {
        return provinceHandle;
    }

    public void setProvinceHandle(Integer provinceHandle) {
        this.provinceHandle = provinceHandle;
    }



    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

}
