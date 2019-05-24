package com.huigou.uasp.bmp.opm.domain.query;

import java.util.Date;

public class BizManagementDesc {

    /**
     * ID
     */
    private String id;

    /**
     * 组织全路径
     */
    private String fullName;

    /**
     * 组织状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createdByName;

    /**
     * 创建日志
     */
    private Date createdDate;

    public BizManagementDesc(String id, String fullName, Integer status, String createdByName, Date createdDate) {
        this.id = id;
        this.fullName = fullName;
        this.status = status;
        this.createdByName = createdByName;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

}
