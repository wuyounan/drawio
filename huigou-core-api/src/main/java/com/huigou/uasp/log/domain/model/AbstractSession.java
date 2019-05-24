package com.huigou.uasp.log.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.util.NetworkUtil;

/**
 * 抽象会话
 * 
 * @author gongmm
 */
@MappedSuperclass
public class AbstractSession implements Serializable {

    private static final long serialVersionUID = 5556440621188246716L;

    /**
     * ID
     */
    
    //@GeneratedValue(generator = "sessionGenerator")
    //@GenericGenerator(name = "sessionGenerator", strategy = "uuid")
    @Id
    private String id;

//    @Column(name = "session_id")
//    private String sessionId;

    @Column(name = "full_id")
    private String fullId;

    @Column(name = "full_name")
    private String fullName;

    /**
     * 机构ID
     */
    @Column(name = "organ_id")
    private String organId;

    /**
     * 机构名称
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
     * 人员ID
     */
    @Column(name = "person_member_id")
    private String personMemberId;

    /**
     * 人员名称
     */
    @Column(name = "person_member_name")
    private String personMemberName;

    @Column(name = "login_name", length = 32)
    private String loginName;

    /**
     * 登录时间
     */
    @Column(name = "login_date")
    private Date loginDate;

    /**
     * 客户IP
     */
    @Column(name = "client_ip")
    private String clientIp;

    /**
     * 服务器IP
     */
    @Column(name = "server_ip")
    private String ServerIp;

    private Long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(String sessionId) {
//        this.sessionId = sessionId;
//    }

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return ServerIp;
    }

    public void setServerIp(String serverIp) {
        ServerIp = serverIp;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void fillDefaultValues(Operator operator) {
        this.setClientIp(ContextUtil.getRequestIP());

        String[] serverIps = NetworkUtil.getIps();
        if (serverIps.length > 0) {
            this.setServerIp(serverIps[0]);
        }

        this.setLoginDate(new Date());
        if (operator != null){
            this.fullId = operator.getFullId();
            this.fullName = operator.getFullName();
            
            this.organId = operator.getOrgId();
            this.organName = operator.getOrgName();
            this.deptId = operator.getDeptId();
            this.deptName = operator.getDeptName();
            this.personMemberId = operator.getPersonMemberId();
            this.personMemberName = operator.getPersonMemberName();
            
            this.loginName = operator.getLoginName();
        }

    }

}
