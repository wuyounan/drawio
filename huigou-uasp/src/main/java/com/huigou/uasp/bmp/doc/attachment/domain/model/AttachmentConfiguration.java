package com.huigou.uasp.bmp.doc.attachment.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

/**
 * 附件配置
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_AttachmentConfig")
public class AttachmentConfiguration extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 3182070074992260521L;

    @Column(name = "allow_delete")
    private Integer allowDelete;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attachmentconfig_id")
    private List<AttachmentConfigurationDetail> details;

    private String remark;

    public Integer getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Integer allowDelete) {
        this.allowDelete = allowDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    @JsonIgnore
    public List<AttachmentConfigurationDetail> getDetails() {
        return details;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    @Override
    public void setDetails(List<? extends AbstractEntity> details) {
        this.details = (List<AttachmentConfigurationDetail>) details;
    }

}
