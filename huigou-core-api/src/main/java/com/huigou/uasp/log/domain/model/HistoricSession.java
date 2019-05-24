package com.huigou.uasp.log.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;

/**
 * 历史会话
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_HistoricSession")
public class HistoricSession extends AbstractSession {

    private static final long serialVersionUID = -3367154580309939272L;

    /**
     * 退出系统时间
     */
    @Column(name = "logout_date")
    private Date logoutDate;

    /**
     * 退出系统方式ID
     */
    @Column(name = "logout_kind_id")
    private String logoutKindId;

    /**
     * 退出人员成员ID
     */
    @Column(name = "logout_person_memeber_id")
    private String logoutPersonMemeberId;

    /**
     * 退出人员成员
     */
    @Column(name = "logout_person_memeber_name")
    private String logoutPersonMemeberName;

    private Integer status;

    @Column(name = "error_message", length = 512)
    private String errorMessage;

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getLogoutKindId() {
        return logoutKindId;
    }

    public void setLogoutKindId(String logoutKindId) {
        this.logoutKindId = logoutKindId;
    }

    public String getLogoutPersonMemeberId() {
        return logoutPersonMemeberId;
    }

    public void setLogoutPersonMemeberId(String logoutPersonMemeberId) {
        this.logoutPersonMemeberId = logoutPersonMemeberId;
    }

    public String getLogoutPersonMemeberName() {
        return logoutPersonMemeberName;
    }

    public void setLogoutPersonMemeberName(String logoutPersonMemeberName) {
        this.logoutPersonMemeberName = logoutPersonMemeberName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static HistoricSession newInstance(Operator operator) {
        HistoricSession result = new HistoricSession();

        result.setId(ContextUtil.getSession().getId().toString());

        result.fillDefaultValues(operator);

        return result;
    }

    public static HistoricSession newInstance(Org mainPersonMember) {
        HistoricSession result = new HistoricSession();

        result.setFullId(mainPersonMember.getFullId());
        result.setFullName(mainPersonMember.getFullName());

        result.setOrganId(mainPersonMember.getOrgId());
        result.setOrganName(mainPersonMember.getOrgName());
        result.setDeptId(mainPersonMember.getDeptId());
        result.setDeptName(mainPersonMember.getDeptName());
        result.setPersonMemberId(mainPersonMember.getId());
        result.setPersonMemberName(mainPersonMember.getName());

        return result;
    }

    public void fillLogoutPersonMember(Operator operator) {
        if (operator != null) {
            this.setLogoutPersonMemeberId(operator.getPersonMemberId());
            this.setLogoutPersonMemeberName(operator.getPersonMemberName());
        }
    }

}
