package com.huigou.uasp.bmp.securitypolicy.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.query.FullIdQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.securitypolicy.application.PersonLoginLimitApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("personLoginLimit")
public class PersonLoginLimitController extends CommonController {
    @Autowired
    private PersonLoginLimitApplication application;

    @Override
    protected String getPagePath() {
        return "/system/securitypolicy/";
    }

    @RequiresPermissions("PersonLoginLimit:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到登录限制列表页面")
    public String forwardPersonLoginLimit() {
        return forward("personLoginLimit");
    }

    @RequiresPermissions("PersonLoginLimit:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加登录限制")
    public String insertPersonLoginLimit() {
        SDO sdo = this.getSDO();
        List<Machine> machines = sdo.getList("data", Machine.class);
        String personId = sdo.getString("personId");
        String fullId = sdo.getString("fullId");
        this.application.insertPersonLoginLimit(personId, fullId, machines);
        return success();
    }

    @RequiresPermissions("PersonLoginLimit:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改登录限制排序号")
    public String updatePersonLoginLimitsSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        this.application.updatePersonLoginLimitsSequence(map);
        ThreadLocalUtil.putVariable("description", "修改排序号");
        return success();
    }

    @RequiresPermissions("PersonLoginLimit:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除登录限制")
    public String deletePersonLoginLimits() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList("ids");
        this.application.deletePersonLoginLimits(ids);
        ThreadLocalUtil.putVariable("description", "删除登录限制");
        return success();
    }

    @RequiresPermissions("PersonLoginLimit:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询登录限制")
    public String sliceQueryPersonLoginLimits() {
        SDO params = this.getSDO();
        FullIdQueryRequest queryRequest = params.toQueryRequest(FullIdQueryRequest.class);
        Map<String, Object> data = this.application.sliceQueryPersonLoginLimits(queryRequest);
        return toResult(data);
    }
}
