package com.huigou.uasp.bmp.opm.domain.model.management;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.Modifier;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;

/**
 * 业务管理权限
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPBizManagement")
@EntityListeners({ CreatorAndModifierListener.class })
public class BizManagement extends AbstractEntity {

    private static final long serialVersionUID = -5682982361543414322L;

    /**
     * 管理者
     */
    @ManyToOne()
    @JoinColumn(name = "manager_id")
    private Org manager;

    /**
     * 业务管理权限类别
     */
    @ManyToOne()
    @JoinColumn(name = "manage_type_id")
    private BizManagementType bizManagementType;

    /**
     * 下属
     */
    @ManyToOne()
    @JoinColumn(name = "subordination_id")
    private Org subordination;

    /**
     * 创建者和修改者
     */
    @Embedded
    private Creator creator;

    @Embedded
    private Modifier modifier;

    public Org getManager() {
        return manager;
    }

    public void setManager(Org manager) {
        this.manager = manager;
    }

    public BizManagementType getBizManagementType() {
        return bizManagementType;
    }

    public void setBizManagementType(BizManagementType bizManagementType) {
        this.bizManagementType = bizManagementType;
    }

    public Org getSubordination() {
        return subordination;
    }

    public void setSubordination(Org subordination) {
        this.subordination = subordination;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public boolean isAllocated(Org manager, BizManagementType bizManagementType, Org subordination) {
        return this.manager.equals(manager) && this.bizManagementType.equals(bizManagementType) && this.subordination.equals(subordination);
    }
}
