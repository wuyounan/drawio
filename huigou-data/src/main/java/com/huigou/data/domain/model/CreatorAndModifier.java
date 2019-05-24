package com.huigou.data.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;

/**
 * 创建者和修改者
 * 
 * @author gongmm
 */
@Embeddable
@Deprecated
public class CreatorAndModifier {

    @Column(name = "created_by_id")
    private String createdById;

    @Column(name = "created_by_name")
    private String createdByName;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "last_modified_by_id")
    private String lastModifiedById;

    @Column(name = "last_modified_by_name")
    private String lastModifiedByName;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(String lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public String getLastModifiedByName() {
        return lastModifiedByName;
    }

    public void setLastModifiedByName(String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    private void buildCreatedInfo() {
        Operator operator = ThreadLocalUtil.getOperator();

        setCreatedById(operator.getPersonMemberId());
        setCreatedByName(operator.getPersonMemberName());

        setCreatedDate(new Date());
    }

    public void buildLastModifiedInfo() {
        Operator operator = ThreadLocalUtil.getOperator();

        setLastModifiedById(operator.getPersonMemberId());
        setLastModifiedByName(operator.getPersonMemberName());

        setLastModifiedDate(new Date());
    }

    public void buildAuditableInfo() {
        buildCreatedInfo();
        buildLastModifiedInfo();
    }
    
    public static CreatorAndModifier newInstance(){
        CreatorAndModifier result = new CreatorAndModifier();
        result.buildAuditableInfo();
        return result;
    }

}
