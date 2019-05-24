package com.huigou.uasp.bmp.opm.domain.model.management;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

/**
 * 基础管理权限类型
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPBaseManagementType")
public class BaseManagementType extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = -8253749730705606754L;

    /**
     * 业务管理权限
     */
    @ManyToOne()
    @JoinColumn(name = "biz_management_type_id")
    private BizManagementType bizManagementType;
    
    private Integer sequence;

    /**
     * 备注
     */
    private String remark;

    @JsonIgnore
    public BizManagementType getBizManagementType() {
        return bizManagementType;
    }
    
    @JsonIgnore
    public void setBizManagementType(BizManagementType bizManagementType) {
        this.bizManagementType = bizManagementType;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getBizManagementTypeId(){
        if (getBizManagementType() != null){
            return getBizManagementType().getId();
        }
        return "";
    }

    public String getBizManagementTypeName(){
        if (getBizManagementType() != null){
            return getBizManagementType().getName();
        }
        return "";
    }
}
