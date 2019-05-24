package com.huigou.uasp.bmp.opm.domain.model.resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 资源操作
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPOperation")
public class ResourceOperation extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = -7369775746498363695L;
    
    public static Integer COMMON_OPERATION = 1;

    /**
     * 资源类别ID
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_kind_id")
    private ResourceKind resourceKind;

    /**
     * 排序号
     */
    private Integer sequence;

    /**
     * 是否公共操作
     */
    @Column(name = "is_common")
    private Integer isCommon;

    public ResourceKind getResourceKind() {
        return resourceKind;
    }

    public void setResourceKind(ResourceKind resourceKind) {
        this.resourceKind = resourceKind;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Integer isCommon) {
        this.isCommon = isCommon;
    }
}
