package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 数据管理权限业务过滤字段定义
 * 
 * @author xx
 *         SA_OPDATAMANAGEBUSINESS_FIELD
 * @date 2018-09-27 12:04
 */
@Entity
@Table(name = "SA_OPDATAMANAGEBUSINESS_FIELD")
public class OpdatamanagebusinessField extends AbstractEntity {

    private static final long serialVersionUID = 1878907276722953961L;

    /**
     * 数据管理权限业务类型ID
     **/
    @Column(name = "datamanagebusiness_id", length = 32)
    private String datamanagebusinessId;

    /**
     * 特殊组织条件
     **/
    @Column(name = "is_org_condition", length = 22)
    private Integer isOrgCondition;

    /**
     * 资源维度名称
     **/
    @Column(name = "data_kind_name", length = 32)
    private String dataKindName;

    /**
     * 资源维度编码
     **/
    @Column(name = "data_kind_code", length = 128)
    private String dataKindCode;

    /**
     * column
     **/
    @Column(name = "table_column", length = 32)
    private String tableColumn;

    /**
     * type
     **/
    @Column(name = "column_data_type", length = 64)
    private String columnDataType;

    /**
     * symbol
     **/
    @Column(name = "column_symbol", length = 32)
    private String columnSymbol;

    /**
     * alias
     **/
    @Column(name = "table_alias", length = 32)
    private String tableAlias;

    /**
     * Formula
     **/
    @Column(name = "formula", length = 256)
    private String formula;

    /**
     * 类型
     **/
    @Column(name = "data_kind", length = 32)
    private String dataKind;

    /**
     * 数据管理权限编码
     **/
    @Column(name = "manage_type", length = 32)
    private String manageType;

    /**
     * SEQUENCE
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    public String getDatamanagebusinessId() {
        return this.datamanagebusinessId;
    }

    public void setDatamanagebusinessId(String datamanagebusinessId) {
        this.datamanagebusinessId = datamanagebusinessId;
    }

    public Integer getIsOrgCondition() {
        return this.isOrgCondition;
    }

    public void setIsOrgCondition(Integer isOrgCondition) {
        this.isOrgCondition = isOrgCondition;
    }

    public String getDataKindName() {
        return this.dataKindName;
    }

    public void setDataKindName(String dataKindName) {
        this.dataKindName = dataKindName;
    }

    public String getDataKindCode() {
        return this.dataKindCode;
    }

    public void setDataKindCode(String dataKindCode) {
        this.dataKindCode = dataKindCode;
    }

    public String getTableColumn() {
        return this.tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getColumnDataType() {
        return this.columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getColumnSymbol() {
        return this.columnSymbol;
    }

    public void setColumnSymbol(String columnSymbol) {
        this.columnSymbol = columnSymbol;
    }

    public String getTableAlias() {
        return this.tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getDataKind() {
        return this.dataKind;
    }

    public void setDataKind(String dataKind) {
        this.dataKind = dataKind;
    }

    public String getManageType() {
        return this.manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public void checkConstraints() {
        Assert.hasText(datamanagebusinessId, "datamanagebusinessId不能为空!");
        Assert.hasText(dataKindCode, "dataKindCode不能为空!");
        if (this.isOrgCondition == null) {
            this.setIsOrgCondition(0);
        }
    }
}
