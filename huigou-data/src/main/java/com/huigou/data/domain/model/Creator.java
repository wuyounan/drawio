package com.huigou.data.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;

/**
 * 创建人员
 * 
 * @author gongmm
 */
@Embeddable
public class Creator {

    @Column(name = "created_by_id")
    private String createdById;

    @Column(name = "created_by_name")
    private String createdByName;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

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

    public void buildCreatedInfo() {
        Operator operator = ThreadLocalUtil.getOperator();

        setCreatedById(operator.getPersonMemberId());
        setCreatedByName(operator.getPersonMemberName());

        setCreatedDate(new Date());
    }

    public static Creator newInstance() {
        Creator result = new Creator();
        result.buildCreatedInfo();
        return result;
    }

}
