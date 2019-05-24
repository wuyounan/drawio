package com.huigou.uasp.bmp.portal.personown.application;

import java.util.Date;
import java.util.Map;

import com.huigou.uasp.bmp.portal.personown.domain.model.Personcalendar;

/**
 * 用户相关私有功能
 * 
 * @author xx
 * @date 2017-03-01 09:40
 */
public interface PersonOwnApplication {

    /**
     * 保存 日程安排
     * 
     * @author xx
     * @param params
     */
    public String savePersoncalendar(Personcalendar personcalendar);

    /**
     * 加载 日程安排
     * 
     * @author xx
     * @return SDO
     */
    public Personcalendar loadPersoncalendar(String id);

    /**
     * 删除 日程安排
     * 
     * @author xx
     */
    public void deletePersoncalendar(String id);

    /**
     * 初始化日历
     * 
     * @param sdo
     * @return
     */
    public Map<String, Object> intPersonCalendar(String personId, String personName);

    /**
     * 按人员 时间查询日程
     * 
     * @param personId
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<String, Object> queryCalendarByPerson(String personId, Date startDate, Date endDate);

}
