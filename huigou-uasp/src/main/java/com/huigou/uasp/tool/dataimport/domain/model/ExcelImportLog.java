package com.huigou.uasp.tool.dataimport.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.uasp.bmp.opm.domain.model.org.CreatorOrgNodeData;

@Entity
@Table(name = "SA_ExcelImportLog")
public class ExcelImportLog extends AbstractEntity {

    private static final long serialVersionUID = 477876004890888727L;

    @ManyToOne()
    @JoinColumn(name = "template_id")
    private ExcelImportTemplate excelImportTemplate;

    @Embedded
    private CreatorOrgNodeData creatorOrgNodeData;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "error_code")
    private Integer errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "error_count")
    private Integer errorCount;

    @Column(name = "success_count")
    private Integer successCount;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public ExcelImportTemplate getExcelImportTemplate() {
        return excelImportTemplate;
    }

    public void setExcelImportTemplate(ExcelImportTemplate excelImportTemplate) {
        this.excelImportTemplate = excelImportTemplate;
    }

    public CreatorOrgNodeData getCreatorOrgNodeData() {
        return creatorOrgNodeData;
    }

    public void setCreatorOrgNodeData(CreatorOrgNodeData creatorOrgNodeData) {
        this.creatorOrgNodeData = creatorOrgNodeData;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
