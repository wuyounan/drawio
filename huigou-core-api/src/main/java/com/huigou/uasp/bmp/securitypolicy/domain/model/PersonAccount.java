package com.huigou.uasp.bmp.securitypolicy.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 人员账号管理
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_PersonAccountManagement")
public class PersonAccount extends AbstractEntity {

    private static final long serialVersionUID = -3386606872316212559L;

    /**
     * 登录名
     */
    @Column(name = "Login_Name")
    private String loginName;

    /**
     * 组织机构全路径ID
     */
    @Column(name = "Full_Id")
    private String fullId;

    /**
     * 是否锁定
     */
    private Integer status;

    /**
     * 锁定时间
     */
    @Column(name = "Locked_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockedDate;

    /**
     * 最后登录时间
     */
    @Column(name = "Last_Login_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    /**
     * 最近修改密码时间
     */
    @Column(name = "Last_Modified_Password_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedPasswordDate;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Date getLastModifiedPasswordDate() {
        return lastModifiedPasswordDate;
    }

    public void setLastModifiedPasswordDate(Date lastModifiedPasswordDate) {
        this.lastModifiedPasswordDate = lastModifiedPasswordDate;
    }

    public boolean isStatus(PersonAccountStatus status) {
        if (this.status == null){
            return false;
        }
        return status.id == this.status;
    }

    public enum PersonAccountStatus {
        INIT(-1, "初始化"), LOCKED(0, "已锁定"), NORMAL(1, "正常");
        private final int id;

        private final String displayName;

        private PersonAccountStatus(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public Integer getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static PersonAccountStatus fromId(int id) {
            for (PersonAccountStatus item : PersonAccountStatus.values()) {
                if (item.id == id) {
                    return item;
                }
            }
            throw new IllegalArgumentException(String.format("无效的人员状态“%s”！", new Object[] { Integer.valueOf(id) }));
        }
    }

}
