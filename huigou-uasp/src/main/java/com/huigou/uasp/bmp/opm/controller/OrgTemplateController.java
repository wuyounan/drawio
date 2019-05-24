package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bmp.opm.proxy.OrgTemplateApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("orgTemplate")
public class OrgTemplateController extends CommonController {

    @Autowired
    private OrgTemplateApplicationProxy orgTemplateApplication;

    private static final String ORG_TEMPLATE_PAGE = "OrgTemplate";

    private static final String SELECT_ORG_TEMPLATE_DIALOG_PAGE = "SelectOrgTemplateDialog";

    @Override
    protected String getPagePath() {
        return "/system/opm/organization/";
    }

    @ControllerMethodMapping("orgTemplate")
    @RequiresPermissions("OrgTemplate:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到机构模板列表页面")
    public String execute() throws Exception {
        return forward(ORG_TEMPLATE_PAGE);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到选择机构模板页面")
    public String showSelectOrgTemplateDialog() {
        return forward(SELECT_ORG_TEMPLATE_DIALOG_PAGE);
    }

    @RequiresPermissions("OrgTemplate:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加机构模板")
    public String insertOrgTemplates() {
        SDO params = this.getSDO();
        String parentId = params.getString("parentId");
        List<String> orgTypeIds = params.getStringList("orgTypeIds");
        orgTemplateApplication.insertOrgTemplates(parentId, orgTypeIds);
        return success();
    }

    @RequiresPermissions("OrgTemplate:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除机构模板")
    public String deleteOrgTemplates() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        orgTemplateApplication.deleteOrgTemplates(ids);
        return success();
    }

    @RequiresPermissions("OrgTemplate:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改机构模板排序号")
    public String updateOrgTemplateSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap("data");
        orgTemplateApplication.updateOrgTemplateSequence(data);
        return success();
    }

    @RequiresPermissions("OrgTemplate:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询机构模板")
    public String queryOrgTemplates() {
        SDO params = this.getSDO();
        ParentAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(ParentAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = this.orgTemplateApplication.queryOrgTemplates(queryRequest);
        return this.toResult(data);
    }

}
