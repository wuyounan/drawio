package com.huigou.uasp.bmp.portal.personown.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.portal.personown.application.PersonOwnApplication;
import com.huigou.uasp.bmp.portal.personown.domain.model.Personcalendar;
import com.huigou.uasp.bmp.portal.personown.repository.PersoncalendarRepository;
import com.huigou.util.CommonUtil;
import com.huigou.util.DateUtil;

/**
 * 用户相关私有功能
 * 
 * @ClassName: PersonOwnApplicationImpl
 * @author xx
 * @date 2017-03-01 09:40
 * @version V1.0
 */
@Service("personOwnApplication")
public class PersonOwnApplicationImpl extends BaseApplication implements PersonOwnApplication {
    @Autowired
    private PersoncalendarRepository personcalendarRepository;

    @Override
    @Transactional
    public String savePersoncalendar(Personcalendar personcalendar) {
        Assert.notNull(personcalendar, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        personcalendar = (Personcalendar) this.commonDomainService.loadAndFillinProperties(personcalendar);
        personcalendar.parseStartTime();
        personcalendar = personcalendarRepository.save(personcalendar);
        return personcalendar.getId();
    }

    @Override
    public Personcalendar loadPersoncalendar(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return personcalendarRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deletePersoncalendar(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        personcalendarRepository.delete(id);
    }

    @Override
    public Map<String, Object> intPersonCalendar(String personId, String personName) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> viewPersons = new ArrayList<Map<String, Object>>();
        Map<String, Object> person = new HashMap<String, Object>();
        person.put("personId", personId);
        person.put("personName", personName);
        viewPersons.add(person);
        // 查询允许查看的人员列表
        /*
         * String queryBySharePersonId = serviceUtil.getEntityDao().getSqlByName(getEntity(PERSON_SHARE_SETUP_ENTITY), "queryBySharePersonId");
         * List<Map<String, Object>> personList = serviceUtil.getEntityDao().queryToListMap(queryBySharePersonId, personId);
         * if (personList != null && personList.size() > 0) {
         * viewPersons.addAll(personList);
         * }
         */
        map.put("viewPersons", viewPersons);
        Date nowDate = DateUtil.getDate();
        map.put("customDay", DateUtil.getDateFormat(nowDate));
        Date startDate = CommonUtil.getFirstDateOfMonth(nowDate);
        Date endDate = CommonUtil.getLastDateOfMonth(nowDate);
        // 查询本月日程
        List<Map<String, Object>> datas = this.queryCalendar(personId, startDate, endDate);
        map.put("calendarDatas", datas);
        return map;
    }

    @Override
    public Map<String, Object> queryCalendarByPerson(String personId, Date startDate, Date endDate) {
        Assert.hasText(personId, "人员ID不能为空!");
        Assert.notNull(startDate, "开始时间不能为空!");
        Assert.notNull(endDate, "开始时间不能为空!");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> datas = this.queryCalendar(personId, startDate, endDate);
        map.put("calendarDatas", datas);
        return map;
    }

    /**
     * 查询人员日程
     * 
     * @param personId
     * @param startTime
     * @param endTime
     * @return
     */
    private List<Map<String, Object>> queryCalendar(String personId, Date startTime, Date endTime) {
        startTime = DateUtil.getDate(startTime);
        endTime = DateUtil.getDate(endTime);
        endTime = DateUtil.getStepDay(endTime, 1);
        List<Personcalendar> personcalendars = personcalendarRepository.findPersoncalendarByPersonIdAndDate(personId, startTime, endTime);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(personcalendars.size());
        for (Personcalendar obj : personcalendars) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", obj.getId());
            map.put("subject", obj.getSubject());
            map.put("isAlldayevent", obj.getIsAlldayevent());
            map.put("dataDate", DateUtil.getDateFormat(obj.getStartTime()));
            map.put("dataTime", DateUtil.getDateFormat(obj.getStartTime(), "HH:mm"));
            map.put("startTime", obj.getStartTime());
            map.put("linkBillUrl", obj.getLinkBillUrl());
            list.add(map);
        }
        return list;
    }
}
