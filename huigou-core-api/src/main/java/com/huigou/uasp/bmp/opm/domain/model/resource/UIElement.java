package com.huigou.uasp.bmp.opm.domain.model.resource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

/**
 * 界面元素
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPUIElement")
public class UIElement extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 6010855196682354586L;

    /**
     * 类别ID 主集 子集 按钮
     */
    @Column(name = "kind_id")
    private String kindId;

    /**
     * 默认权限
     */
    @Column(name = "default_operation_id")
    private String defaultOperationId;
    
    private Integer sequence;

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getDefaultOperationId() {
        return defaultOperationId;
    }

    public void setDefaultOperationId(String defaultOperationId) {
        this.defaultOperationId = defaultOperationId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
