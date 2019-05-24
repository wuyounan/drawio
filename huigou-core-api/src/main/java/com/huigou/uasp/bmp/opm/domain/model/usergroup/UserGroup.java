package com.huigou.uasp.bmp.opm.domain.model.usergroup;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.Creator;

/**
 * 用户组
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_UserGroup")
@EntityListeners({ CreatorAndModifierListener.class })
public class UserGroup extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 8361695002370924362L;

    @Embedded
    private Creator creator;

    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(name = "kind_id")
    private UserGroupKind userGroupKind;

    private Integer sequence;

    @Transient
    private List<UserGroupDetail> groupdetails;

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UserGroupKind getUserGroupKind() {
        return userGroupKind;
    }

    public void setUserGroupKind(UserGroupKind userGroupKind) {
        this.userGroupKind = userGroupKind;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<UserGroupDetail> getGroupdetails() {
        return groupdetails;
    }

    public void setGroupdetails(List<UserGroupDetail> groupdetails) {
        this.groupdetails = groupdetails;
    }

    public enum UserGroupKind {
        SYSTEM, CUSTOM;
    }
}
