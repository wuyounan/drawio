package com.huigou.data.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;

/**
 * 修改人员
 * 
 * @author gongmm
 */
@Embeddable
public class Modifier {

    @Column(name = "last_modified_by_id")
    private String lastModifiedById;

    @Column(name = "last_modified_by_name")
    private String lastModifiedByName;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

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

    public void buildLastModifiedInfo() {
        Operator operator = ThreadLocalUtil.getOperator();

        setLastModifiedById(operator.getPersonMemberId());
        setLastModifiedByName(operator.getPersonMemberName());

        setLastModifiedDate(new Date());
    }

    public static Modifier newInstance(){
        Modifier result = new Modifier();
        result.buildLastModifiedInfo();
        return result;
    }

}
