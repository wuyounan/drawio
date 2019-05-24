package com.huigou.uasp.bmp.opm.domain.model.org;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;

/**
 * 创建人节点数据
 * 
 * @author gongmm
 */
@Embeddable
public class CreatorOrgNodeData {

    /**
     * ID全路径
     */
    @Column(name = "full_id")
    private String fullId;

    /**
     * 名称全路径
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 公司ID
     */
    @Column(name = "organ_id")
    private String organId;

    /**
     * 公司名称
     */
    @Column(name = "organ_name")
    private String organName;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private String deptId;

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
     * 岗位名称
     */
    @Column(name = "position_name")
    private String positionName;

    @Column(name = "person_member_id")
    private String personMemberId;

    @Column(name = "person_member_name")
    private String personMemberName;

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

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPersonMemberId() {
        return personMemberId;
    }

    public void setPersonMemberId(String personMemberId) {
        this.personMemberId = personMemberId;
    }

    public String getPersonMemberName() {
        return personMemberName;
    }

    public void setPersonMemberName(String personMemberName) {
        this.personMemberName = personMemberName;
    }

    public static CreatorOrgNodeData buildOperatorOrgNodeData() {
        Operator operator = ThreadLocalUtil.getOperator();
        Assert.notNull(operator, "操作员环境参数不能为空。");

        CreatorOrgNodeData orgNodeData = new CreatorOrgNodeData();

        orgNodeData.fullId = operator.getFullId();
        orgNodeData.fullName = operator.getFullName();

        orgNodeData.organId = operator.getOrgId();
        orgNodeData.organName = (operator.getOrgName());

        orgNodeData.deptId = operator.getDeptId();
        orgNodeData.deptName = operator.getDeptName();

        orgNodeData.positionId = operator.getPositionId();
        orgNodeData.positionName = operator.getPositionName();

        orgNodeData.personMemberId = operator.getPersonMemberId();
        orgNodeData.personMemberName = operator.getPersonMemberName();

        return orgNodeData;
    }
}
