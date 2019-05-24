package com.huigou.uasp.bmp.securitypolicy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.BaseInfoAbstractEntity;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.log.annotation.LogPorperty;

/**
 * 设置计算机的密级，登录时验证人员密级与计算机密级是否匹配，匹配规则,设置计算机的密级
 * <blockquote>
 * TODO 匹配规则：三种情况
 * <p>
 * 1、登录
 * </p>
 * <ul>
 * <li>人员密级与计算机密级一致</li>
 * <li>人员密级高于计算机密级</li>
 * <li>人员密级低于计算机密级</li>
 * </ul>
 * <p>
 * 2、查询、全文检索
 * </P>
 * </blockquote>
 * 
 * @author gongmm
 */
@Entity
@LogPorperty
@Table(name = "SA_Machine")
public class Machine extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
	  * 
	  */
    private String ip;

    /**
	  * 
	  */
    private String mac;

    /**
	  * 
	  */
    @Column(name = "security_grade")
    private String securityGrade;

    /**
	  * 
	  */
    @LogPorperty
    private String remark;

    public void setIp(String ip) {
        this.ip = ip;
    }

    @LogPorperty
    public String getIp() {
        return this.ip;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @LogPorperty
    public String getMac() {
        return this.mac;
    }

    public void setSecurityGrade(String securityGrade) {
        this.securityGrade = securityGrade;
    }

    @LogPorperty
    public String getSecurityGrade() {
        return this.securityGrade;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @LogPorperty
    public String getRemark() {
        return this.remark;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return ValidStatus.ENABLED.getId().equals(this.getStatus());
    }

    @JsonIgnore
    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();
        Assert.hasText(ip, "ip地址不能为空。");
        Assert.hasText(mac, "mac地址不能为空。");
    }
}