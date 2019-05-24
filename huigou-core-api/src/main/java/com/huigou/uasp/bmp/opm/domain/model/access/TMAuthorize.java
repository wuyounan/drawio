package com.huigou.uasp.bmp.opm.domain.model.access;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;

import javax.persistence.*;

@Entity
@Table(name = "SA_TMAuthorize")
@EntityListeners({CreatorAndModifierListener.class})
public class TMAuthorize extends AbstractEntity {

	private static final long serialVersionUID = 2434474561227361739L;

	/**
     * 下属ID
     */
    @Column(name = "subordination_id")
    private String subordinationId;

    /**
     * 下属全路径ID
     */
    @Column(name = "subordination_full_id")
    private String subordinationFullId;
    /**
     * 系统ID
     */
    @Column(name = "system_id")
    private String systemId;
    /**
     * 角色ID
     */
    @Column(name = "role_kind_id")
    private String roleKindId;
    /**
     * 管理人ID
     */
    @Column(name = "manager_id")
    private String managerId;

    /**
     * 创建人信息
     */
    @Embedded
    private Creator creator;

    public String getSubordinationId() {
        return subordinationId;
    }

    public void setSubordinationId(String subordinationId) {
        this.subordinationId = subordinationId;
    }

    public String getSubordinationFullId() {
        return subordinationFullId;
    }

    public void setSubordinationFullId(String subordinationFullId) {
        this.subordinationFullId = subordinationFullId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getRoleKindId() {
        return roleKindId;
    }

    public void setRoleKindId(String roleKindId) {
        this.roleKindId = roleKindId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
