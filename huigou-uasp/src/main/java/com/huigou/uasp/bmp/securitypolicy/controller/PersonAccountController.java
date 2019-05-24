package com.huigou.uasp.bmp.securitypolicy.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.FullIdQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.securitypolicy.application.PersonAccountApplication;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("personAccount")
public class PersonAccountController extends CommonController {
    @Autowired
    private PersonAccountApplication application;

    @Override
    protected String getPagePath() {
        return "/system/securitypolicy/";
    }

    @RequiresPermissions("PersonAccount:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到账号管理列表页面")
    public String forwardPersonAccount() {
        return forward("personAccount");
    }

    @RequiresPermissions("PersonAccount:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改账号状态")
    public String updatePersonAccountsStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList("ids");
        int status = sdo.getInteger("status");
        this.application.updatePersonAccountsStatus(ids, status);
        return success();
    }

    @RequiresPermissions("PersonAccount:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询账号")
    public String sliceQueryPersonAccounts() {
        SDO params = this.getSDO();
        FullIdQueryRequest queryRequest = params.toQueryRequest(FullIdQueryRequest.class);
        Map<String, Object> data = this.application.sliceQueryPersonAccounts(queryRequest);
        return toResult(data);
    }
}
