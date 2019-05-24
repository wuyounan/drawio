package com.huigou.uasp.bmp.doc.attachment.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 附件配置明细
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_AttachmentConfigDetail")
public class AttachmentConfigurationDetail extends AbstractEntity {

    private static final long serialVersionUID = 4888270210933954803L;

    private String code;

    private String name;

    @Column(name = "allow_multiple")
    private Integer allowMultiple;

    @Column(name = "file_kind")
    private String fileKind;

    @Column(name = "col_span")
    private Integer colSpan;

    private Integer sequence;

    @Column(name = "is_required")
    private Integer isRequired;

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

    public Integer getAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(Integer allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public String getFileKind() {
        return fileKind;
    }

    public void setFileKind(String fileKind) {
        this.fileKind = fileKind;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

}
