package com.huigou.uasp.tool.dataimport.domain.model;

import javax.persistence.*;

import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "SA_ExcelImportTemplateDetail")
public class ExcelImportTemplateDetail extends AbstractEntity {

    private static final long serialVersionUID = 5463400407657581628L;

    @Column(name = "excel_column_number")
    private Integer excelColumnNumber;

    @Column(name = "excel_column_name")
    private String excelColumnName;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "column_description")
    private String columnDescription;

    private Integer length;

    public Integer getExcelColumnNumber() {
        return excelColumnNumber;
    }

    public void setExcelColumnNumber(Integer excelColumnNumber) {
        this.excelColumnNumber = excelColumnNumber;
    }

    public String getExcelColumnName() {
        return excelColumnName;
    }

    public void setExcelColumnName(String excelColumnName) {
        this.excelColumnName = excelColumnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDescription() {
        return columnDescription;
    }

    public void setColumnDescription(String columnDescription) {
        this.columnDescription = columnDescription;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
