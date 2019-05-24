package com.huigou.data.domain.query;

import java.util.Date;

/**
 * 查询通用模型
 * 
 * @author Administrator
 */

public class DateStartAndEndQueryRequest extends QueryAbstractRequest {
    protected Date startDate;

    protected Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
