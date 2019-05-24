package com.huigou.uasp.bmp.securitypolicy.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.securitypolicy.application.ApplicationSystemApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.query.ApplicationSystemQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.ApplicationSystem;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("applicationSystem")
public class ApplicationSystemController extends CommonController {

    @Autowired
    private ApplicationSystemApplication application;

    @Override
    protected String getPagePath() {
        return "/system/securitypolicy/";
    }

    @RequiresPermissions("ApplicationSystem:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到应用系统维护列表页面")
    public String forwardApplicationSystem() {
        return forward("applicationSystem");
    }

    @RequiresPermissions("ApplicationSystem:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加应用系统维护明细页面")
    public String showInsertApplicationSystem() {
        this.putAttribute("sequence", this.application.getApplicationSystemNextSequence());
        return forward("applicationSystemDetail");
    }

    @RequiresPermissions("ApplicationSystem:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到修改应用系统统维明细页面")
    public String loadApplicationSystem() {
        SDO sdo = this.getSDO();
        String id = sdo.getString("id");
        ApplicationSystem applicationSystem = this.application.loadApplicationSystem(id);
        return forward("applicationSystemDetail", applicationSystem);
    }

    @RequiresPermissions("ApplicationSystem:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加应用系统")
    public String insertApplicationSystem() {
        SDO params = this.getSDO();
        ApplicationSystem applicationSystem = params.toObject(ApplicationSystem.class);
        this.application.insertApplicationSystem(applicationSystem);
        return success();
    }

    @RequiresPermissions("ApplicationSystem:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改应用系统")
    public String updateApplicationSystem() {
        SDO sdo = this.getSDO();
        ApplicationSystem applicationSystem = sdo.toObject(ApplicationSystem.class);
        this.application.updateApplicationSystem(applicationSystem);
        return success();
    }

    @RequiresPermissions("ApplicationSystem:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改应用系统排序号")
    public String updateApplicationSystemsSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> data = sdo.getStringMap("data");
        this.application.updateApplicationSystemsSequence(data);
        return success();
    }

    @RequiresPermissions("ApplicationSystem:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除应用系统")
    public String deleteApplicationSystems() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList("ids");
        this.application.deleteApplicationSystems(ids);
        return success();
    }

    @RequiresPermissions("ApplicationSystem:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询应用系统")
    public String sliceQueryApplicationSystems() {
        SDO params = this.getSDO();
        ApplicationSystemQueryRequest queryRequest = params.toQueryRequest(ApplicationSystemQueryRequest.class);
        Map<String, Object> data = this.application.sliceQueryApplicationSystems(queryRequest);
        return toResult(data);
    }

}
