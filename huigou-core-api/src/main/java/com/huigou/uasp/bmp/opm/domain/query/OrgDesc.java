package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.domain.ValidStatus;



/**
 * 组织机构查询结果
 * 
 * @author gongmm
 */
public class OrgDesc {

    private String id;

    private String typeId;

    private String code;

    private String name;

    private String longName;

    private String parentId;

    private String fullId;

    private String fullCode;

    private String fullName;

    private String orgId;

    private String orgCode;

    private String orgName;

    private String deptId;

    private String deptCode;

    private String deptName;

    private String positionId;

    private String positionCode;

    private String positionName;

    private String orgKindId;

    private String personId;

    private String nodeKindId;

    private String description;

    private Integer status;

    private String fullSequence;

    private Integer sex;

    private String certificateNo;

    private Integer personStatus;

    private String fullOrgKindId;

    private String mainOrgId;

    private Integer isVirtual;

    private Integer sequence;

    private Long version;

    private Integer hasChildren;

    private Integer isexpand = 0;

    private String nodeUrl = "Folder";

    public OrgDesc() {

    }

    public OrgDesc(String id, String typeId, String code, String name, String longName, String parentId, String fullId, String fullCode, String fullName,
                   String orgId, String orgCode, String orgName, String deptId, String deptCode, String deptName, String positionId, String positionCode,
                   String positionName, String orgKindId, String personId, String description, Integer status, String fullSequence, Integer sex, String certificateNo,
                   Integer personStatus, String fullOrgKindId, String mainOrgId, Integer isVirtual, Integer sequence, Long version, Integer hasChildren) {
        super();
        this.id = id;
        this.typeId = typeId;
        this.code = code;
        this.name = name;
        this.longName = longName;
        this.parentId = parentId;
        this.fullId = fullId;
        this.fullCode = fullCode;
        this.fullName = fullName;
        this.orgId = orgId;
        this.orgCode = orgCode;
        this.orgName = orgName;
        this.deptId = deptId;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.positionId = positionId;
        this.positionCode = positionCode;
        this.positionName = positionName;
        this.orgKindId = orgKindId;
        this.personId = personId;
        this.description = description;
        this.status = status;
        this.fullSequence = fullSequence;
        this.sex = sex;
        this.certificateNo = certificateNo;
        this.personStatus = personStatus;
        this.fullOrgKindId = fullOrgKindId;
        this.mainOrgId = mainOrgId;
        this.version = version;
        this.isVirtual = isVirtual;
        this.sequence = sequence;
        this.hasChildren = hasChildren;
    }

    public static OrgDesc createRoot() {
        OrgDesc result = new OrgDesc();

        result.setParentId("");
        result.setId("orgRoot");
        result.setCode("root");
        result.setName("组织机构");
        result.setOrgKindId("root");
        result.setStatus(ValidStatus.ENABLED.getId());
        result.setHasChildren(1);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullSequence() {
        return fullSequence;
    }

    public void setFullSequence(String fullSequence) {
        this.fullSequence = fullSequence;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Integer getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(Integer personStatus) {
        this.personStatus = personStatus;
    }

    public String getFullOrgKindId() {
        return fullOrgKindId;
    }

    public void setFullOrgKindId(String fullOrgKindId) {
        this.fullOrgKindId = fullOrgKindId;
    }

    public String getMainOrgId() {
        return mainOrgId;
    }

    public void setMainOrgId(String mainOrgId) {
        this.mainOrgId = mainOrgId;
    }

    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getIsexpand() {
        return isexpand;
    }

    public void setIsexpand(Integer isexpand) {
        this.isexpand = isexpand;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

}
