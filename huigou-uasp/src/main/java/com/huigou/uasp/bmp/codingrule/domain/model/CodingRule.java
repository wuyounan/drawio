package com.huigou.uasp.bmp.codingrule.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

@Entity
@Table(name = "SA_CodingRule")
public class CodingRule extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 6815157868175032956L;

    /**
     * 段间分隔符
     **/
    @Column(name = "delimiter", length = 1)
    private String delimiter;

    /**
     * 新增不允许断号
     **/
    @Column(name = "is_add_no_break", length = 22)
    private Integer isAddNoBreak;

    /**
     * 新增显示
     **/
    @Column(name = "is_add_show", length = 22)
    private Integer isAddShow;

    /**
     * 支持修改
     **/
    @Column(name = "is_modifiable", length = 22)
    private Integer isModifiable;

    /**
     * 支持断号
     **/
    @Column(name = "is_break_code", length = 22)
    private Integer isBreakCode;

    /**
     * 断号用户选择
     **/
    @Column(name = "is_select_break_code", length = 22)
    private Integer isSelectBreakCode;

    @Column(name = "remark", length = 512)
    private String remark;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "codingRule_id")
    @OrderBy("sequence")
    private List<CodingRuleDetail> details;

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Integer getIsAddNoBreak() {
        return this.isAddNoBreak;
    }

    public void setIsAddNoBreak(Integer isAddNoBreak) {
        this.isAddNoBreak = isAddNoBreak;
    }

    public Integer getIsAddShow() {
        return this.isAddShow;
    }

    public void setIsAddShow(Integer isAddShow) {
        this.isAddShow = isAddShow;
    }

    public Integer getIsModifiable() {
        return this.isModifiable;
    }

    public void setIsModifiable(Integer isModifiable) {
        this.isModifiable = isModifiable;
    }

    public Integer getIsBreakCode() {
        return this.isBreakCode;
    }

    public void setIsBreakCode(Integer isBreakCode) {
        this.isBreakCode = isBreakCode;
    }

    public Integer getIsSelectBreakCode() {
        return this.isSelectBreakCode;
    }

    public void setIsSelectBreakCode(Integer isSelectBreakCode) {
        this.isSelectBreakCode = isSelectBreakCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    @JsonIgnore()
    public List<CodingRuleDetail> getDetails() {
        return details;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setDetails(List<? extends AbstractEntity> details) {
        this.details = (List<CodingRuleDetail>) details;
    }

}
