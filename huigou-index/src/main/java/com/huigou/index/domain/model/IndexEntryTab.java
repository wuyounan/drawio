package com.huigou.index.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 指标展示页面定义
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "NG_INDEX_ENTRY_TAB")
public class IndexEntryTab extends AbstractEntity {

    private static final long serialVersionUID = 1105173276774943519L;

    private String code;

    private String name;

    @Column(name = "dataset_id")
    private Integer datasetId;

    @Column(name = "dataset_name")
    private String datasetName;

    private String cascade;

    @Column(name = "sort_json")
    private String sortJson;

    @Lob
    @Column(name = "table_columns_json")
    private String tableColumnsJson;

    @Column(name = "province_enable")
    private Integer provinceEnable;

    @Column(name = "city_enable")
    private Integer cityEnable;

    @Column(name = "county_enable")
    private Integer countyEnable;

    private Integer sequence;

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

    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getCascade() {
        return cascade;
    }

    public void setCascade(String cascade) {
        this.cascade = cascade;
    }

    public String getSortJson() {
        return sortJson;
    }

    public void setSortJson(String sortJson) {
        this.sortJson = sortJson;
    }

    public String getTableColumnsJson() {
        return tableColumnsJson;
    }

    public void setTableColumnsJson(String tableColumnsJson) {
        this.tableColumnsJson = tableColumnsJson;
    }
    
    public Integer getProvinceEnable() {
        return provinceEnable;
    }

    public void setProvinceEnable(Integer provinceEnable) {
        this.provinceEnable = provinceEnable;
    }

    public Integer getCityEnable() {
        return cityEnable;
    }

    public void setCityEnable(Integer cityEnable) {
        this.cityEnable = cityEnable;
    }

    public Integer getCountyEnable() {
        return countyEnable;
    }

    public void setCountyEnable(Integer countyEnable) {
        this.countyEnable = countyEnable;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
