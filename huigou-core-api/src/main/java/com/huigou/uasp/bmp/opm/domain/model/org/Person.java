package com.huigou.uasp.bmp.opm.domain.model.org;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.SecurityGrade;
import com.huigou.data.domain.model.BaseInfoWithTenantAbstractEntity;
import com.huigou.domain.ValidStatus;
import com.huigou.util.Util;

@Entity
@Table(name = "SA_OPPerson")
public class Person extends BaseInfoWithTenantAbstractEntity {

    private static final long serialVersionUID = 1117798307947141411L;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 英文名称
     */
    @Column(name = "english_name")
    private String englishName;

    /**
     * 描述
     */
    private String description;

    /**
     * 岗位
     */
    @Column(name = "main_org_id")
    private String mainOrgId;

    /**
     * 证件类型
     */
    @Column(name = "certificate_kind_id")
    private String certificateKindId;

    /**
     * 证件号码
     */
    @Column(name = "certificate_no")
    private String certificateNo;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    @Temporal(TemporalType.DATE)
    private Date birthday;

    /**
     * 参加工作日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "join_date")
    private Date joinDate;

    /**
     * 出生地
     */
    @Column(name = "home_place")
    private String homePlace;

    /**
     * 学历
     */
    private String degree;

    /**
     * 毕业院校
     */
    @Column(name = "graduate_school")
    private String graduateSchool;

    /**
     * 学年制
     */
    @Column(name = "school_length")
    private String schoolLength;

    /**
     * 专业
     */
    private String speciality;

    /**
     * 职称
     */
    private String title;

    /**
     * 婚姻状况
     */
    private String marriage;

    /**
     * 家庭住址
     */
    @Column(name = "family_address")
    private String familyAddress;

    /**
     * 邮编
     */
    private String zip;

    /**
     * email
     */
    private String email;

    /**
     * QQ
     */
    private String qq;

    /**
     * 微信号
     */
    private String weixin;

    /**
     * 移动电话
     */
    @Column(name = "mobile_phone")
    private String mobilePhone;

    /**
     * 家庭电话
     */
    @Column(name = "family_phone")
    private String familyPhone;

    /**
     * 办公室电话
     */
    @Column(name = "office_phone")
    private String officePhone;

    /**
     * 照片路径ID
     */
    @Column(name = "photo_file_id")
    private String photoFileId;

    /**
     * 是否操作员
     */
    @Column(name = "is_operator")
    private Boolean isOperator;

    /**
     * 密码
     */
    private String password;

    /**
     * CA号码
     */
    @Column(name = "ca_no")
    private String caNo;

    /**
     * CA状态
     */
    @Column(name = "ca_status")
    private Integer caStatus;

    @Column(name = "security_grade_id")
    @Enumerated(EnumType.STRING)
    private SecurityGrade securityGrade;

    @Column(name = "person_security_grade_id")
    @Enumerated(EnumType.STRING)
    private PersonSecurityGrade personSecurityGrade;

    private Integer sequence;

    @Column(name = "person_kind")
    private String personKind;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainOrgId() {
        return this.mainOrgId;
    }

    public void setMainOrgId(String mainOrgId) {
        this.mainOrgId = mainOrgId;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateKindId() {
        return this.certificateKindId;
    }

    public void setCertificateKindId(String certificateKindId) {
        this.certificateKindId = certificateKindId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getHomePlace() {
        return homePlace;
    }

    public void setHomePlace(String homePlace) {
        this.homePlace = homePlace;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getSchoolLength() {
        return schoolLength;
    }

    public void setSchoolLength(String schoolLength) {
        this.schoolLength = schoolLength;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getPhotoFileId() {
        return photoFileId;
    }

    public void setPhotoFileId(String photoFileId) {
        this.photoFileId = photoFileId;
    }

    public Boolean getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(Boolean isOperator) {
        this.isOperator = isOperator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public Integer getCaStatus() {
        return this.caStatus;
    }

    public void setCaStatus(Integer caStatus) {
        this.caStatus = caStatus;
    }

    public SecurityGrade getSecurityGrade() {
        return securityGrade;
    }

    public void setSecurityGrade(SecurityGrade securityGrade) {
        this.securityGrade = securityGrade;
    }

    public PersonSecurityGrade getPersonSecurityGrade() {
        return personSecurityGrade;
    }

    public void setPersonSecurityGrade(PersonSecurityGrade personSecurityGrade) {
        this.personSecurityGrade = personSecurityGrade;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getPersonKind() {
        return personKind;
    }

    public void setPersonKind(String personKind) {
        this.personKind = personKind;
    }

    @JsonIgnore
    @Transient
    public ValidStatus getValidStatus() {
        return ValidStatus.fromId(this.getStatus());
    }

    /**
     * 验证编码的合法性
     * 
     * @param code
     *            组织编码
     */
    private void checkCodeRule() {
        Util.check(Util.isNotEmptyString(this.getCode()), "编码“%s”不能为空。", new Object[] { this.getCode() });
        Util.check((this.getCode().indexOf('/') < 0) && (this.getCode().indexOf('%') < 0) && (this.getCode().indexOf(',') < 0),
                   "无效的编码“%s”，不能包含字符“/”、“%%”、“,”。", new Object[] { this.getCode() });
    }

    /**
     * 验证组织名称合法性
     * 
     * @param name
     *            组织名称
     */
    private void checkNameRule() {
        Util.check(Util.isNotEmptyString(this.getName()), "名称“%s”不能为空。", new Object[] { this.getName() });
        Util.check((this.getName().indexOf('/') < 0) && (this.getName().indexOf('%') < 0) && (this.getName().indexOf(',') < 0),
                   "无效的名称“%s”，不能包含字符“/”、“%%”、“,”。", new Object[] { this.getName() });
    }

    private void checkNamedValidity() {
        this.checkCodeRule();
        this.checkNameRule();
    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();
        checkNamedValidity();
    }

    /**
     * 验证是否启用状态
     */
    @JsonIgnore
    public boolean isEnabled() {
        return getValidStatus() == ValidStatus.ENABLED;
    }

    /**
     * COUNTRY VARCHAR2(64) Y 国家
     * PROVINCE VARCHAR2(64) Y 省
     * CITY VARCHAR2(64) Y 市
     * PAY_PASSWORD VARCHAR2(64) Y 薪资密码
     * IS_OPERATOR INTEGER Y 是否操作员
     * IS_HIDDEN INTEGER Y 是否隐藏
     * APP_CODE VARCHAR2(32) Y 微信
     */
}
