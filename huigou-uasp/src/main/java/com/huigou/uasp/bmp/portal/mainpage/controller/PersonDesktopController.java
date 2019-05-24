package com.huigou.uasp.bmp.portal.mainpage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.portal.mainpage.application.PersonDesktopApplication;
import com.huigou.uasp.bmp.portal.mainpage.domain.model.PersonDesktopScreen;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 用户桌面
 * 
 * @author
 */
@Controller
@ControllerMapping("personDesktop")
public class PersonDesktopController extends CommonController {

    @Autowired
    private PersonDesktopApplication personDesktopApplication;

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.SAVE, description = "保存用户桌面屏幕")
    public String savePersonDesktopScreen() {
        String id = this.personDesktopApplication.savePersonDesktopScreen();
        return success(id);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.SAVE, description = "保存用户桌面屏幕功能")
    public String savePersonDesktopScreenFunctions() {
        SDO params = this.getSDO();
        String screenId = params.getString("id");
        List<String> functionIds = params.getStringList("functionIds");
        personDesktopApplication.savePersonDesktopScreenFunctions(screenId, functionIds);
        return success();
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除用户桌面屏幕")
    public String deletePersonDesktopScreen() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        personDesktopApplication.deletePersonDesktopScreen(id);
        return success();
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询用户桌面屏幕")
    public String queryPersonDesktopScreens() {
        List<PersonDesktopScreen> result = personDesktopApplication.queryPersonDesktopScreens();
        if (result.size() == 0) {
            String id = personDesktopApplication.savePersonDesktopScreen();
            PersonDesktopScreen personDesktopScreen = new PersonDesktopScreen();
            personDesktopScreen.setId(id);
            result.add(personDesktopScreen);
        }
        return toResult(result);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到桌面页面")
    public String showDesktop() {
        List<PersonDesktopScreen> result = personDesktopApplication.queryPersonDesktopScreens();
        if (result.size() == 0) {
            String id = personDesktopApplication.savePersonDesktopScreen();
            PersonDesktopScreen personDesktopScreen = new PersonDesktopScreen();
            personDesktopScreen.setId(id);
            result.add(personDesktopScreen);
        }
        this.putAttribute("screens", result);
        return forward("/desktop/desktop.jsp");
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询用户功能")
    public String queryPersonFunctions() {
        SDO params = this.getSDO();
        String parentId = params.getString("parentId");
        List<Map<String, Object>> list = personDesktopApplication.queryPersonFunctions(parentId);
        return toResult(list);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询用户功能")
    public String queryPersonDesktopFunctions() {
        SDO params = this.getSDO();
        String personId = params.getOperator().getUserId();
        List<Map<String, Object>> list = personDesktopApplication.queryPersonDesktopFunctions(personId);
        return toResult(list);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.SAVE, description = "保存用户桌面屏幕功能")
    public String savePersonDesktopScreenAndFunctions() {
        SDO params = this.getSDO();
        String screenId = params.getString("screenId");
        List<String> functionIds = params.getStringList("functionIds");
        String id = personDesktopApplication.savePersonDesktopScreenAndFunctions(screenId, functionIds);
        return success(id);
    }

    public String queryJobFunctions() {
        return null;
    }

    public String queryOftenUseFunctions() {
        return null;
    }
}
