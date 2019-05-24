package com.huigou.uasp.bmp.portal.personown.domain.query;

import java.util.Date;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 日程安排
 * 
 * @author xx
 *         SA_PERSONCALENDAR
 * @date 2017-03-01 09:40
 */
public class PersoncalendarQueryRequest extends QueryAbstractRequest {

    /**
     * 标题
     **/
    protected String subject;

    /**
     * 开始时间
     **/
    protected Date startTime;

    /**
     * 结束时间
     **/
    protected Date endTime;

    /**
     * 是否全天
     **/
    protected Integer isAlldayevent;

    /**
     * 用户ID
     **/
    protected String personId;

    /* VARCHAR2 128 CanNull Y 标题 */
    public java.lang.String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /* DATE 7 CanNull Y 开始时间 */
    public java.util.Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        if (null == startTime) {
            this.startTime = null;
            return;
        }
        this.startTime = new java.sql.Date(startTime.getTime());
    }

    /* DATE 7 CanNull Y 结束时间 */
    public java.util.Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        if (null == endTime) {
            this.endTime = null;
            return;
        }
        this.endTime = new java.sql.Date(endTime.getTime());
    }

    /* NUMBER 22 CanNull Y 是否全天 */
    public java.lang.Integer getIsAlldayevent() {
        return this.isAlldayevent;
    }

    public void setIsAlldayevent(Integer isAlldayevent) {
        this.isAlldayevent = isAlldayevent;
    }

    /* VARCHAR2 32 CanNull Y 用户ID */
    public java.lang.String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

}
