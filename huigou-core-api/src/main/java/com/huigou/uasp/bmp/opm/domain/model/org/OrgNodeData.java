package com.huigou.uasp.bmp.opm.domain.model.org;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.OrgNode;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;

/**
 * 组织节点数据
 * <p>
 * 业务数据的组织冗余数据
 * 
 * @author gongmm
 */
@Embeddable
public class OrgNodeData {

    /**
     * ID全路径
     */
    @Column(name = "full_Id")
    private String fullId;

    /**
     * 名称全路径
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 公司ID
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 公司编码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 公司名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private String deptId;

    /**
     * 部门编码
     */
    @Column(name = "dept_code")
    private String deptCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 岗位ID
     */
    @Column(name = "position_id")
    private String positionId;

    /**
     * 岗位编码
     */
    @Column(name = "position_code")
    private String positionCode;

    /**
     * 岗位名称
     */
    @Column(name = "position_name")
    private String positionName;

    @Transient
    private String personMemberId;

    @Transient
    private String personMemberCode;

    @Transient
    private String personMemberName;

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

    public static OrgNodeData buildOrgNodeData(Org org) {
        Assert.notNull(org, "参数org不能为空。");

        // 组织机构路径： /.ogn/.ctr/.ctr/.dpt/.pos/.psm
        // 项目组织路径：/.ogn/.fld/.fld/.prj/.grp/.psm/.fun
        String[] orgIds = org.getFullId().substring(1).split("/");
        String[] orgCodes = org.getFullCode().substring(1).split("/");
        String[] orgNames = org.getFullName().substring(1).split("/");
        String[] orgKindIds = org.getFullOrgKindId().substring(1).split("/");
        Assert.isTrue(orgIds.length == orgCodes.length && orgIds.length == orgNames.length && orgIds.length == orgKindIds.length, "构建组织机构数据出错，路径长度不等。");
        int deptLevel = 0;
        int orgLevel = 0;

        OrgNodeData orgNodeData = new OrgNodeData();

        orgNodeData.fullId = org.getFullId();
        orgNodeData.fullName = org.getFullName();

        for (int i = orgIds.length - 1; i >= 0; i--) {
            if (orgIds[i].endsWith(OrgNode.ORGAN)) {
                orgLevel++;
                if (orgLevel == 1) {
                    orgNodeData.setOrgId(orgIds[i].replace("." + OrgNode.ORGAN, ""));
                    orgNodeData.setOrgCode(orgCodes[i]);
                    orgNodeData.setOrgName(orgNames[i]);
                }
            } else if (orgKindIds[i].equalsIgnoreCase(OrgNode.DEPT)) {
                deptLevel++;
                if (deptLevel == 1) {
                    orgNodeData.setDeptId(orgIds[i].replace("." + OrgNode.DEPT, ""));
                    orgNodeData.setDeptCode(orgCodes[i]);
                    orgNodeData.setDeptName(orgNames[i]);
                }
            } else if (orgIds[i].endsWith(OrgNode.POSITION)) {
                orgNodeData.setPositionId(orgIds[i].replace("." + OrgNode.POSITION, ""));
                orgNodeData.setPositionCode(orgCodes[i]);
                orgNodeData.setPositionName(orgNames[i]);
            } else if (orgIds[i].endsWith(OrgNode.PERSONMEMBER)) {
                orgNodeData.setPersonMemberId(orgIds[i].replace("." + OrgNode.PERSONMEMBER, ""));
                orgNodeData.setPersonMemberCode(orgCodes[i]);
                orgNodeData.setPersonMemberName(orgNames[i]);
            }
        }
        return orgNodeData;
    }

    public static OrgNodeData buildOrgNodeData(OrgUnit orgUnit) {
        Assert.notNull(orgUnit, "参数orgUnit不能为空。");

        OrgNodeData orgNodeData = new OrgNodeData();

        orgNodeData.setFullId(orgUnit.getFullId());
        orgNodeData.setFullName(orgUnit.getFullName());

        orgNodeData.setOrgId(orgUnit.getAttributeValue("orgId"));
        orgNodeData.setOrgName(orgUnit.getAttributeValue("orgName"));

        orgNodeData.setDeptId(orgUnit.getAttributeValue("deptId"));
        orgNodeData.setDeptName(orgUnit.getAttributeValue("deptName"));

        orgNodeData.setPositionId(orgUnit.getAttributeValue("posId"));
        orgNodeData.setPositionName(orgUnit.getAttributeValue("posName"));

        orgNodeData.setPersonMemberId(orgUnit.getAttributeValue("psmId"));
        orgNodeData.setPersonMemberName(orgUnit.getAttributeValue("psmName"));

        return orgNodeData;
    }

    public static OrgNodeData buildOperatorOrgNodeData() {
        Operator operator = ThreadLocalUtil.getOperator();
        Assert.notNull(operator, "操作员环境参数不能为空。");

        OrgNodeData orgNodeData = new OrgNodeData();

        orgNodeData.setFullId(operator.getFullId());
        orgNodeData.setFullName(operator.getFullName());

        orgNodeData.setOrgId(operator.getOrgId());
        orgNodeData.setOrgName(operator.getOrgName());

        orgNodeData.setDeptId(operator.getDeptId());
        orgNodeData.setDeptName(operator.getDeptName());

        orgNodeData.setPositionId(operator.getPositionId());
        orgNodeData.setPositionName(operator.getPositionName());

        orgNodeData.setPersonMemberId(operator.getPersonMemberId());
        orgNodeData.setPersonMemberName(operator.getPersonMemberName());

        return orgNodeData;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPersonMemberId() {
        return personMemberId;
    }

    public void setPersonMemberId(String personMemberId) {
        this.personMemberId = personMemberId;
    }

    public String getPersonMemberCode() {
        return personMemberCode;
    }

    public void setPersonMemberCode(String personMemberCode) {
        this.personMemberCode = personMemberCode;
    }

    public String getPersonMemberName() {
        return personMemberName;
    }

    public void setPersonMemberName(String personMemberName) {
        this.personMemberName = personMemberName;
    }

}
