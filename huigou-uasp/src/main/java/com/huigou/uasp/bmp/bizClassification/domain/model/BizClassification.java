package com.huigou.uasp.bmp.bizClassification.domain.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.TreeEntity;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;

/**
 * 业务分类配置
 */
@Entity
@Table(name = "SA_Bizclassification")
public class BizClassification extends TreeEntity {

    private static final long serialVersionUID = 1L;

    public static final String ROOT_ID = "1";

    /**
     * 描述
     */
    private String description;

    @Transient
    private Integer hasChildren;

    @Transient
    private List<BizClassificationDetail> detail;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<BizClassificationDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<BizClassificationDetail> detail) {
        this.detail = detail;
    }

    @JsonIgnore
    @Transient
    public String getPermissionNodeKindId() {
        return PermissionNodeKind.BUSINESSCLASS.getId();
    }

}
