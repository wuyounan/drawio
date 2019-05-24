package com.huigou.data.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.util.ApplicationContextWrapper;

/**
 * 流程单据抽象实体
 * 
 * @author gongmm
 */
@MappedSuperclass
public abstract class FlowBillAbstractEntity extends AbstractEntity {

    private static final long serialVersionUID = 3583187553508438608L;

    /**
     * 填单日期
     */
    @Column(name = "fillin_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fillinDate;

    /**
     * 单据编号
     */
    @Column(name = "bill_code")
    private String billCode;

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer statusId;

    /**
     * ID全路径
     */
    @Column(name = "full_Id")
    private String fullId;

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

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
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

    public Date getFillinDate() {
        return fillinDate;
    }

    public void setFillinDate(Date fillinDate) {
        this.fillinDate = fillinDate;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    protected abstract String getCodeRuleId();

    private void internalSetDefaultValues(OrgUnit orgUnit) {
        Assert.notNull(orgUnit, "参数orgUnit不能为空。");

        String codeRuleId = getCodeRuleId();
        Assert.hasText(codeRuleId, "没有设置单据编码规则。");

        Object codeGenerator = ApplicationContextWrapper.getBean("codeGenerator");
        try {
            Method method = codeGenerator.getClass().getMethod("getNextCode", String.class);
            String code = (String) method.invoke(codeGenerator, codeRuleId);

            this.setBillCode(code);
        } catch (NoSuchMethodException | SecurityException e1) {
            throw new ApplicationException(e1.getMessage());
        } catch (IllegalAccessException e) {
            throw new ApplicationException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ApplicationException(e.getTargetException().getMessage());
        }

        this.setFullId(orgUnit.getFullId());

        this.setOrganId(orgUnit.getOrgId());
        this.setOrganName(orgUnit.getOrgName());

        this.setDeptId(orgUnit.getDeptId());
        this.setDeptName(orgUnit.getDeptName());

        this.setPositionId(orgUnit.getPositionId());
        this.setPositionName(orgUnit.getPositionName());

        this.setPersonMemberId(orgUnit.getPersonMemberId());
        this.setPersonMemberName(orgUnit.getPersonMemberName());
        
        this.setFillinDate(new Timestamp(new Date().getTime()));
    }

    public void setDefaultValues(OrgUnit orgUnit) {
        internalSetDefaultValues(orgUnit);
    }

    public void setDefaultValues() {
        Operator operator = ThreadLocalUtil.getOperator();
        Assert.notNull(operator, "操作员环境参数不能为空。");
        OrgUnit orgUnit = new OrgUnit(operator.getFullId(), operator.getFullName());
        setDefaultValues(orgUnit);
    }

}
