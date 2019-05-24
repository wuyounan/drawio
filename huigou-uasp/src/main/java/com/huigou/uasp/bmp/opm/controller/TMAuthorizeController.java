package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import com.huigou.uasp.bmp.opm.proxy.TMAuthorizeApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("tmAuthorize")
public class TMAuthorizeController extends CommonController {

    @Autowired
    private TMAuthorizeApplicationProxy tmAuthorizeApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/permission/";
    }

    @RequiresPermissions("TMAuthorize:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到三员授权列表页面")
    public String forwardTMAuthorize() {
        this.putAttribute("roleKindId", this.getOperator().getRoleKind().getId());
        return forward("tmAuthorize");
    }

    @RequiresPermissions(value = { "TMAuthorize:create", "TMAuthorize:update", "TMAuthorize:delete" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.SAVE, description = "保存三员授权")
    public String saveTMAuthorizes() {
        SDO sdo = this.getSDO();

        List<TMAuthorize> tmAuthorizes = sdo.getList("detailData", TMAuthorize.class);
        String subordinationId = sdo.getString("subordinationId");
        String systemId = sdo.getString("systemId");
        String roleKindId = sdo.getString("roleKindId");

        tmAuthorizeApplication.saveTMAuthorizes(tmAuthorizes, subordinationId, systemId, roleKindId);
        return success();
    }

    @RequiresPermissions("TMAuthorize:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询三员授权")
    public String queryTMAuthorizes() {
        SDO sdo = this.getSDO();
        String subordinationId = sdo.getString("subordinationId");
        String managedOrgId = sdo.getString("managedOrgId");

        Map<String, Object> result = this.tmAuthorizeApplication.queryTMAuthorizes(subordinationId, managedOrgId);
        return toResult(result);
    }

    //@RequiresPermissions("TMAuthorize:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询分级授权组织")
    public String queryDelegationOrgs() {
        SDO sdo = this.getSDO();
        Map<String, Object> result = this.tmAuthorizeApplication.queryDelegationOrgs(sdo);
        return toResult(result);
    }
}