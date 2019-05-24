package com.huigou.uasp.bmp.portal.personown.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.Modifier;
import com.huigou.exception.ApplicationException;
import com.huigou.util.DateUtil;

/**
 * 日程安排
 * 
 * @author xx
 *         SA_PERSONCALENDAR
 * @date 2017-03-01 09:40
 */
@Entity
@Table(name = "SA_PERSONCALENDAR")
public class Personcalendar extends AbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 2744232498164834199L;

    /**
     * 标题
     **/
    @Column(name = "SUBJECT", length = 128)
    protected String subject;

    /**
     * 开始时间
     **/
    @Column(name = "START_TIME", length = 7)
    protected Date startTime;

    /**
     * 结束时间
     **/
    @Column(name = "END_TIME", length = 7)
    protected Date endTime;

    /**
     * 是否全天
     **/
    @Column(name = "IS_ALLDAYEVENT", length = 22)
    protected Integer isAlldayevent;

    /**
     * 用户ID
     **/
    @Column(name = "PERSON_ID", length = 32)
    protected String personId;

    @Embedded
    private Creator creator;

    @Embedded
    private Modifier modifier;

    /**
     * 业务单据ID
     **/
    @Column(name = "BUSINESS_ID", length = 32)
    protected String businessId;

    /**
     * 业务单据链接
     **/
    @Column(name = "LINK_BILL_URL", length = 128)
    protected String linkBillUrl;

    @Transient
    protected String dataDate;

    @Transient
    protected String dataTime;

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
        this.startTime = startTime;
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
        this.endTime = endTime;
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

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    /* VARCHAR2 32 CanNull Y 业务单据ID */
    public java.lang.String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /* VARCHAR2 128 CanNull Y 业务单据链接 */
    public java.lang.String getLinkBillUrl() {
        return this.linkBillUrl;
    }

    public void setLinkBillUrl(String linkBillUrl) {
        this.linkBillUrl = linkBillUrl;
    }

    @Transient
    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    @Transient
    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public void parseStartTime() {
        if (dataDate == null) {
            return;
        }
        String startTimeStr = dataDate.trim();
        Date startTime = null;
        try {
            if (isAlldayevent == 1) {// 全天
                startTime = DateUtil.getDateParse(1, startTimeStr);
            } else {// 存在时间
                startTimeStr += " " + dataTime.trim();
                startTime = DateUtil.getDateParse(9, startTimeStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("错误的时间格式[" + startTimeStr + "]");
        }
        this.setStartTime(startTime);
        this.setEndTime(startTime);
    }

}
