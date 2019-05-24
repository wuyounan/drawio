package com.huigou.uasp.tool.remind.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.tool.remind.RemindOpenKind;
import com.huigou.uasp.tool.remind.RemindReplaceKind;
import com.huigou.uasp.tool.remind.application.MessageRemindApplication;
import com.huigou.uasp.tool.remind.domain.model.MessageRemind;
import com.huigou.uasp.tool.remind.domain.query.MessageRemindQueryRequest;
import com.huigou.util.SDO;

/**
 * 消息提醒配置后台
 * 
 * @ClassName: MessageRemindController
 * @author xx
 * @date 2017-02-15 14:39
 * @version V1.0
 */
@Controller
@ControllerMapping("messageRemind")
public class MessageRemindController extends CommonController {

    @Autowired
    private MessageRemindApplication messageRemindApplication;

    protected String getPagePath() {
        return "/system/remind/";
    }

    public String forwardList() {
        return forward("MessageRemindList");
    }

    public String slicedQuery() {
        SDO params = this.getSDO();
        MessageRemindQueryRequest queryRequest = params.toQueryRequest(MessageRemindQueryRequest.class);
        Map<String, Object> data = messageRemindApplication.slicedQuery(queryRequest);
        return this.toResult(data);
    }

    public String showInsert() {
        this.putAttribute("remindOpenKinds", RemindOpenKind.getData());
        this.putAttribute("remindReplaceKinds", RemindReplaceKind.getData());
        return forward("MessageRemindDetail");
    }

    public String save() {
        SDO params = this.getSDO();
        MessageRemind messageRemind = params.toObject(MessageRemind.class);
        messageRemind.setRemindTitle(this.getFilterInputParameter("remindTitle"));
        String id = messageRemindApplication.save(messageRemind);
        return success(id);
    }

    /**
     * 加载 系统消息提醒配置表
     * 
     * @author xx
     * @return String
     */
    public String showLoad() {
        SDO sdo = this.getSDO();
        try {
            String id = sdo.getString("id");
            MessageRemind messageRemind = messageRemindApplication.load(id);
            this.putAttribute("remindOpenKinds", RemindOpenKind.getData());
            this.putAttribute("remindReplaceKinds", RemindReplaceKind.getData());
            return forward("MessageRemindDetail", messageRemind);
        } catch (Exception e) {
            return errorPage(e);
        }
    }

    /**
     * 删除 系统消息提醒配置表
     * 
     * @author xx
     * @return String
     */
    public String delete() {
        SDO sdo = this.getSDO();
        try {
            String[] ids = sdo.getStringArray("ids");
            messageRemindApplication.delete(ids);
        } catch (Exception e) {
            return error(e);
        }
        return success();
    }

    public String updateFolderId() {
        SDO params = this.getSDO();
        try {
            List<String> ids = params.getStringList("ids");
            String folderId = params.getString("folderId");
            messageRemindApplication.updateFolderId(ids, folderId);
            return success();
        } catch (Exception e) {
            return error(e);
        }
    }

    public String updateStatus() {
        SDO params = this.getSDO();
        try {
            List<String> ids = params.getStringList("ids");
            Integer status = params.getInteger("status");
            messageRemindApplication.updateStatus(ids, status);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success();
    }

    public String updateSequence() {
        SDO sdo = this.getSDO();
        try {
            Map<String, Integer> map = sdo.getStringMap("data");
            this.messageRemindApplication.updateSequence(map);
            return success();
        } catch (Exception e) {
            return error(e);
        }
    }

    /**
     * 测试提醒执行结果
     * 
     * @author
     * @return String
     */
    public String testParseRemindFun() {
        SDO sdo = this.getSDO();
        try {
            String id = sdo.getString("id");
            List<Object> list = this.messageRemindApplication.testParseRemindFun(id);
            return toResult(list);
        } catch (Exception e) {
            return error(e);
        }
    }

    public String loadRemindByPerson() {
        try {
            String personId = this.getOperator().getUserId();
            List<?> list = messageRemindApplication.queryRemindByPersonId(personId);
            return this.toResult(list);
        } catch (Exception e) {
            return error(e);
        }
    }

}
