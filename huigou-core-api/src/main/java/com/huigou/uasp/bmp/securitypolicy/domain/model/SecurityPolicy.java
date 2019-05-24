package com.huigou.uasp.bmp.securitypolicy.domain.model;

import com.huigou.data.domain.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 安全策略
 *
 * @author gongmm
 */
@Entity
@Table(name = "SA_SecurityPolicy")
public class SecurityPolicy extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 允许外网登录
     */
    @Column(name = "Enable_Internet_Login")
    private Integer enableInternetLogin;

    /**
     * 内网网段
     */
    @Column(name = "Intranet_segment")
    private String intranetSegment;

    /**
     * 密码有效天数
     */
    @Column(name = "Password_Validity_Interval")
    private Integer passwordValidityInterval;

    /**
     * 密码过期提前提醒天数
     */
    @Column(name = "Password_Expire_Give_Days")
    private Integer passwordExpireGiveDays;

    /**
     * 密码最小长度
     */
    @Column(name = "Password_Minimum_Length")
    private Integer passwordMinimumLength;

    /**
     * 密码错误锁定用户次数
     */
    @Column(name = "Lock_User_Password_Error_Time")
    private Integer lockUserPasswordErrorTime;

    /**
     * 自动解锁时间
     */
    @Column(name = "Auto_Unlock_Time")
    private Integer autoUnlockTime;

    /**
     * 密级级别
     */
    @Column(name = "security_grade")
    private String securityGrade;

    /**
     * 最小数字个数
     */
    @Column(name = "number_count")
    private Long numberCount;
    /**
     * 最小大写字母个数
     */
    @Column(name = "uppercase_count")
    private Long uppercaseCount;
    /**
     * 最小小写字母个数
     */
    @Column(name = "lowercase_count")
    private Long lowercaseCount;
    /**
     * 特殊字符个数
     */
    @Column(name = "special_character_count")
    private Long specialCharacterCount;

    /**
     * 初始化密码
     */
    @Column(name = "init_password")
    private String initPassword;
    /**
     * 状态
     */
    private Integer status;

    public Integer getEnableInternetLogin() {
        return enableInternetLogin;
    }

    public void setEnableInternetLogin(Integer enableInternetLogin) {
        this.enableInternetLogin = enableInternetLogin;
    }

    public String getIntranetSegment() {
        return intranetSegment;
    }

    public void setIntranetSegment(String intranetSegment) {
        this.intranetSegment = intranetSegment;
    }

    public Integer getPasswordValidityInterval() {
        return passwordValidityInterval;
    }

    public void setPasswordValidityInterval(Integer passwordValidityInterval) {
        this.passwordValidityInterval = passwordValidityInterval;
    }

    public Integer getPasswordExpireGiveDays() {
        return passwordExpireGiveDays;
    }

    public void setPasswordExpireGiveDays(Integer passwordExpireGiveDays) {
        this.passwordExpireGiveDays = passwordExpireGiveDays;
    }

    public Integer getPasswordMinimumLength() {
        return passwordMinimumLength;
    }

    public void setPasswordMinimumLength(Integer passwordMinimumLength) {
        this.passwordMinimumLength = passwordMinimumLength;
    }

    public Integer getLockUserPasswordErrorTime() {
        return lockUserPasswordErrorTime;
    }

    public void setLockUserPasswordErrorTime(Integer lockUserPasswordErrorTime) {
        this.lockUserPasswordErrorTime = lockUserPasswordErrorTime;
    }

    public Integer getAutoUnlockTime() {
        return autoUnlockTime;
    }

    public void setAutoUnlockTime(Integer autoUnlockTime) {
        this.autoUnlockTime = autoUnlockTime;
    }

    public String getSecurityGrade() {
        return securityGrade;
    }

    public void setSecurityGrade(String securityGrade) {
        this.securityGrade = securityGrade;
    }

    public Long getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(Long numberCount) {
        this.numberCount = numberCount;
    }

    public Long getUppercaseCount() {
        return uppercaseCount;
    }

    public void setUppercaseCount(Long uppercaseCount) {
        this.uppercaseCount = uppercaseCount;
    }

    public Long getLowercaseCount() {
        return lowercaseCount;
    }

    public void setLowercaseCount(Long lowercaseCount) {
        this.lowercaseCount = lowercaseCount;
    }

    public Long getSpecialCharacterCount() {
        return specialCharacterCount;
    }

    public void setSpecialCharacterCount(Long specialCharacterCount) {
        this.specialCharacterCount = specialCharacterCount;
    }

    public String getInitPassword() {
        return initPassword;
    }

    public void setInitPassword(String initPassword) {
        this.initPassword = initPassword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
