package com.huigou.uasp.bmp.flexfield.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

/**
 * 弹性域分组定义
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_FlexFieldBizGroup")
public class FlexFieldBizGroup extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = -5535062243759788510L;

    /**
     * 是否显示
     */
    private Integer visible;

    /**
     * 业务编码
     */
    @Column(name = "biz_code")
    private String bizCode;

    /**
     * 是否明细表
     */
    @Column(name = "is_detail_table")
    private Integer isDetailTable;

    /**
     * UI风格
     */
    @Column(name = "ui_style")
    private Integer uiStyle;

    private Integer cols;

    private String remark;

    /**
     * 显示模式 1 table 2 div
     */
    @Column(name = "show_model")
    private Integer showModel;

    /**
     * TABLE布局模式
     */
    @Column(name = "table_layout")
    private String tableLayout;

    @Column(name = "sequence")
    private Integer sequence;

    @Transient
    private List<FlexFieldBizGroupField> detailData;

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public Integer getIsDetailTable() {
        return isDetailTable;
    }

    public void setIsDetailTable(Integer isDetailTable) {
        this.isDetailTable = isDetailTable;
    }

    public Integer getUiStyle() {
        return uiStyle;
    }

    public void setUiStyle(Integer uiStyle) {
        this.uiStyle = uiStyle;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getShowModel() {
        return showModel;
    }

    public void setShowModel(Integer showModel) {
        this.showModel = showModel;
    }

    public String getTableLayout() {
        return tableLayout;
    }

    public void setTableLayout(String tableLayout) {
        this.tableLayout = tableLayout;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<FlexFieldBizGroupField> getDetailData() {
        return detailData;
    }

    public void setDetailData(List<FlexFieldBizGroupField> detailData) {
        this.detailData = detailData;
    }

}
