package com.huigou.uasp.bmp.portal.personown.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentApplication;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bmp.portal.personown.application.PersonOwnApplication;
import com.huigou.uasp.bmp.portal.personown.domain.model.Personcalendar;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 用户相关私有功能
 * 
 * @ClassName: PersonOwnController
 * @author xx
 * @date 2017-03-01 09:50
 * @version V1.0
 */
@Controller
@ControllerMapping("personOwn")
public class PersonOwnController extends CommonController {

    @Autowired
    private PersonOwnApplication personOwnApplication;

    @Autowired
    private AttachmentApplication attachmentApplication;

    @Autowired
    private OrgApplicationProxy orgApplication;

    protected String getPagePath() {
        return "/system/userPanel/";
    }

    public String intPersonCalendar() throws Exception {
        SDO sdo = this.getSDO();
        String personId = sdo.getOperator().getUserId();
        String personName = sdo.getOperator().getName();
        Map<String, Object> map = personOwnApplication.intPersonCalendar(personId, personName);
        return this.toResult(map);
    }

    public String queryCalendarByPerson() throws Exception {
        SDO sdo = this.getSDO();
        String personId = sdo.getProperty("personId", String.class);
        Date startDate = sdo.getProperty("startDate", Date.class);
        Date endDate = sdo.getProperty("endDate", Date.class);
        Map<String, Object> map = personOwnApplication.queryCalendarByPerson(personId, startDate, endDate);
        return this.toResult(map);
    }

    public String insertPersoncalendar() {
        SDO params = this.getSDO();
        Personcalendar personcalendar = params.toObject(Personcalendar.class);
        String id = personOwnApplication.savePersoncalendar(personcalendar);
        return success(id);
    }

    public String updatePersoncalendar() {
        SDO params = this.getSDO();
        Personcalendar personcalendar = params.toObject(Personcalendar.class);
        String id = personOwnApplication.savePersoncalendar(personcalendar);
        return success(id);
    }

    public String deletePersoncalendar() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        personOwnApplication.deletePersoncalendar(id);
        return success();
    }

    /********** 控制面板用户个人信息 ***********/
    public String forwardUsercontrol() {
        SDO sdo = this.getSDO();
        String id = sdo.getOperator().getUserId();
        Person person = this.orgApplication.loadPerson(id);
        List<Attachment> attachments = attachmentApplication.queryAttachments(Constants.PERSON_PICTURE, id);
        if (attachments.size() > 0) {
            Attachment attachment = attachments.get(0);
            this.putAttribute("picturePath", StringUtil.encode(attachment.getPath()));
        }
        return this.forward("UsercontrolPanel", person);
    }

    /**
     * 获取用户照片
     * 
     * @return
     */
    public String loadPersonPicture() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        if (StringUtil.isBlank(id)) {
            id = sdo.getOperator().getUserId();
        }
        List<Attachment> attachments = attachmentApplication.queryAttachments(Constants.PERSON_PICTURE, id);
        if (attachments.size() > 0) {
            Attachment attachment = attachments.get(0);
            return toResult(attachment.getPath());
        }
        return toResult("");
    }

    /**
     * 获取用户照片
     * 
     * @return
     */
    public String loadPersonPictures() {
        SDO sdo = this.getSDO();
        String ids = sdo.getString("ids");
        if (StringUtil.isBlank(ids)) {
            return toResult("");
        }
        String[] personIds = ids.split(",");
        Map<String, String> map = new HashMap<String, String>(personIds.length);
        for (String personId : personIds) {
            List<Attachment> attachments = attachmentApplication.queryAttachments(Constants.PERSON_PICTURE, personId);
            if (attachments.size() > 0) {
                Attachment attachment = attachments.get(0);
                map.put(personId, attachment.getPath());
            }
        }
        return toResult(map);
    }

    /**
     * 用户用户修改
     * 
     * @author xiexin
     * @return
     * @throws
     */
    public String saveUsercontrolInfo() {
        SDO sdo = this.getSDO();
        Person person = sdo.toObject(Person.class);
        this.orgApplication.updatePersonSimple(person);
        return success();
    }
}
