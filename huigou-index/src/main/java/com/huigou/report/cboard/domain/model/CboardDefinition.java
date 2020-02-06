package com.huigou.report.cboard.domain.model;

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
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

/**
 * CBoard定义
 * 
 * @author gongmm
 */
@Entity
@Table(name = "RP_CBoard_Definition")
public class CboardDefinition extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = -5182615266165109896L;

    public static final String INPUT_UI_PARAM_FIELD_NAME = "inputUIParams_";

    public static final String INPUT_TABLE_FIELD_NAME = "inputTables_";

    private String remark;

    @Column(name = "sequence")
    private Integer sequence;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "definition_id")
    @OrderBy("sequence")
    private List<CboardDefinitionUIParam> uiParams;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "definition_id")
    @OrderBy("sequence")
    private List<CboardDefinitionTable> tables;

    @Transient
    private List<CboardDefinitionUIParam> inputUIParams_;

    @Transient
    private List<CboardDefinitionTable> inputTables_;

    public List<CboardDefinitionUIParam> getUIParams() {
        return uiParams;
    }

    public void setUIParams(List<CboardDefinitionUIParam> uiParams) {
        this.uiParams = uiParams;
    }

    public List<CboardDefinitionTable> getTables() {
        return tables;
    }

    public void setTables(List<CboardDefinitionTable> tables) {
        this.tables = tables;
    }

    @JsonIgnore
    public List<CboardDefinitionUIParam> getInputUIParams_() {
        return inputUIParams_;
    }

    public void setInputUIParams_(List<CboardDefinitionUIParam> inputUIParams_) {
        this.inputUIParams_ = inputUIParams_;
    }

    @JsonIgnore
    public List<CboardDefinitionTable> getInputTables_() {
        return inputTables_;
    }

    public void setInputTables_(List<CboardDefinitionTable> inputTables_) {
        this.inputTables_ = inputTables_;
    }

    @Override
    public void buildDetails() {
        this.buildDetails(this.getUIParams(), this.getInputUIParams_());
        this.buildDetails(this.getTables(), this.getInputTables_());
    }

    public CboardDefinitionTable findTableById(String tableId) {
        for (CboardDefinitionTable cboardDefinitionTable : tables) {
            if (cboardDefinitionTable.getId().equals(tableId)) {
                return cboardDefinitionTable;
            }
        }
        return null;
    }

}
