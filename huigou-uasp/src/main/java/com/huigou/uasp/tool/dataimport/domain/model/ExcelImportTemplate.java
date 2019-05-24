package com.huigou.uasp.tool.dataimport.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.Creator;
import com.huigou.util.StringUtil;

@Entity
@Table(name = "SA_ExcelImportTemplate")
public class ExcelImportTemplate extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 938693645893002373L;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "procedure_name")
    private String procedureName;

    @Column(name = "remark")
    private String remark;

    @Embedded
    private Creator creator;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "template_id")
    @OrderBy("excelColumnNumber asc")
    private List<ExcelImportTemplateDetail> details;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setDetails(List<? extends AbstractEntity> details) {
        this.details = (List<ExcelImportTemplateDetail>) details;
    }

    @Override
    @JsonIgnore
    public List<ExcelImportTemplateDetail> getDetails() {
        return details;
    }

    public boolean hasProcedureName() {
        return StringUtil.isNotBlank(this.procedureName);
    }

    public String buildSelectImportDetailSql(ExcelImportStatus status) {
        StringBuilder sb = new StringBuilder();

        sb.append("select tmp_id,batch_number,status,message");
        for (ExcelImportTemplateDetail item : getDetails()) {
            sb.append(",").append(item.getColumnName());
        }
        sb.append(" from ").append(this.getTableName()).append(" where ");
        sb.append(" batch_number = :batchNumber");
        if (status != null) {
            sb.append(" and status = :status");
        }

        return sb.toString();
    }
}
