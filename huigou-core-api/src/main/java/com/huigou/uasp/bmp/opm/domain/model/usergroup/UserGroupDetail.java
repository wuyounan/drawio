package com.huigou.uasp.bmp.opm.domain.model.usergroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "SA_UserGroupDetail")
public class UserGroupDetail extends AbstractEntity {

    private static final long serialVersionUID = -4234312642263524782L;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "full_id")
    private String fullId;

    @Column(name = "group_id")
    private String groupId;

    private Integer sequence;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
