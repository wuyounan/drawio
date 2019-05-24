package com.huigou.uasp.bpm.engine.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;

@Entity
@Table(name = "ACT_HI_ProcInst_Extension")
public class HistoricProcInstExtension extends AbstractEntity {

    private static final long serialVersionUID = 7540803005015298833L;

    /**
     * 流程实例ID
     */
    @Column(name = "proc_inst_id")
    private String procInstId;

    /**
     * 业务ID
     */
    @Column(name = "business_key")
    private String businessKey;

    @Column(name = "applicant_person_member_id")
    private String applicantPersonMemberId;

    @Column(name = "applicant_person_member_name")
    private String applicantPersonMemberName;

    @Column(name = "applicant_pos_id")
    private String applicantPosId;

    @Column(name = "applicant_pos_name")
    private String applicantPosName;

    @Column(name = "applicant_dept_id")
    private String applicantDeptId;

    @Column(name = "applicant_dept_name")
    private String applicantDeptName;

    @Column(name = "applicant_org_id")
    private String applicantOrgId;

    @Column(name = "applicant_org_name")
    private String applicantOrgName;

    @Column(name = "applicant_full_id")
    private String applicantFullId;

    @Column(name = "applicant_full_name")
    private String applicantFullName;
    
    private String description;
    
    @Column(name = "status_id")
    private String statusId;
    
    @Column(name = "status_name")
    private String statusName;
        

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getApplicantPersonMemberId() {
        return applicantPersonMemberId;
    }

    public void setApplicantPersonMemberId(String applicantPersonMemberId) {
        this.applicantPersonMemberId = applicantPersonMemberId;
    }

    public String getApplicantPersonMemberName() {
        return applicantPersonMemberName;
    }

    public void setApplicantPersonMemberName(String applicantPersonMemberName) {
        this.applicantPersonMemberName = applicantPersonMemberName;
    }

    public String getApplicantPosId() {
        return applicantPosId;
    }

    public void setApplicantPosId(String applicantPosId) {
        this.applicantPosId = applicantPosId;
    }

    public String getApplicantPosName() {
        return applicantPosName;
    }

    public void setApplicantPosName(String applicantPosName) {
        this.applicantPosName = applicantPosName;
    }

    public String getApplicantDeptId() {
        return applicantDeptId;
    }

    public void setApplicantDeptId(String applicantDeptId) {
        this.applicantDeptId = applicantDeptId;
    }

    public String getApplicantDeptName() {
        return applicantDeptName;
    }

    public void setApplicantDeptName(String applicantDeptName) {
        this.applicantDeptName = applicantDeptName;
    }

    public String getApplicantOrgId() {
        return applicantOrgId;
    }

    public void setApplicantOrgId(String applicantOrgId) {
        this.applicantOrgId = applicantOrgId;
    }

    public String getApplicantOrgName() {
        return applicantOrgName;
    }

    public void setApplicantOrgName(String applicantOrgName) {
        this.applicantOrgName = applicantOrgName;
    }

    public String getApplicantFullId() {
        return applicantFullId;
    }

    public void setApplicantFullId(String applicantFullId) {
        this.applicantFullId = applicantFullId;
    }

    public String getApplicantFullName() {
        return applicantFullName;
    }

    public void setApplicantFullName(String applicantFullName) {
        this.applicantFullName = applicantFullName;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public void assignApplicant(Org personMember) {
        Assert.notNull(personMember,"参数personMember不能为空。");
        this.applicantPersonMemberId = personMember.getId();
        this.applicantPersonMemberName = personMember.getName();
        this.applicantPosId = personMember.getPositionId();
        this.applicantPosName = personMember.getPositionName();
        this.applicantDeptId = personMember.getDeptId();
        this.applicantDeptName = personMember.getDeptName();
        this.applicantOrgId = personMember.getOrgId();
        this.applicantOrgName = personMember.getOrgName();
        this.applicantFullId = personMember.getFullId();
        this.applicantFullName = personMember.getFullName();
    }
    
    public void assignApplicant(Operator operator) {
        Assert.notNull(operator,"参数operator不能为空。");
        this.applicantPersonMemberId = operator.getPersonMemberId();
        this.applicantPersonMemberName = operator.getName();
        this.applicantPosId = operator.getPositionId();
        this.applicantPosName = operator.getPositionName();
        this.applicantDeptId = operator.getDeptId();
        this.applicantDeptName = operator.getDeptName();
        this.applicantOrgId = operator.getOrgId();
        this.applicantOrgName = operator.getOrgName();
        this.applicantFullId = operator.getFullId();
        this.applicantFullName = operator.getFullName();
    }
}
