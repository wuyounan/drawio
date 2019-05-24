package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.query.OrgTypeQueryRequest;
import com.huigou.uasp.bmp.opm.proxy.OrgTypeApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("orgType")
public class OrgTypeController extends CommonController {

    private static final String ORG_KIND_ID_KEY_NAME = "orgKindId";

    private static final String ORG_TYPE_PAGE = "OrgType";

    private static final String ORG_TYPE_DETAIL_PAGE = "OrgTypeDetail";

    private static final String SELECT_ORGTYPE_DIALOG = "SelectOrgTypeDialog";

    @Autowired
    private OrgTypeApplicationProxy orgTypeApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/organization/";
    }

    @RequiresPermissions(value = { "OrgType:query", "DeptType:query", "PosType:query" }, logical = Logical.OR)
    @ControllerMethodMapping("/orgType")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到组织类型列表页面")
    public String execute(){
        return forward(ORG_TYPE_PAGE);
    }

    //@LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到选择组织类型列表页面")
    public String showSelectOrgTypeDialog() {
        return forward(SELECT_ORGTYPE_DIALOG, this.getSDO().getProperties());
    }

    @RequiresPermissions(value = { "OrgType:create", "DeptType:create", "PosType:create" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到组织类型添加页面")
    public String showOrgTypeDetail() {
        SDO params = this.getSDO();
        String orgKindId = params.getString(ORG_KIND_ID_KEY_NAME);
        String folderId = params.getString(FOLDER_ID_KEY_NAME);

        Integer sequence = orgTypeApplication.getNextSequence(folderId);

        OrgType orgType = new OrgType(orgKindId, folderId, sequence);

        return forward(ORG_TYPE_DETAIL_PAGE, orgType);
    }

    @RequiresPermissions(value = { "OrgType:create", "DeptType:create", "PosType:create" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加组织类型")
    public String insertOrgType() {
        SDO params = this.getSDO();
        OrgType orgType = params.toObject(OrgType.class);
        String id = orgTypeApplication.saveOrgType(orgType);
        return success(id);
    }

    @RequiresPermissions(value = { "OrgType:update", "DeptType:update", "PosType:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到组织类型修改界面")
    public String loadOrgType() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        OrgType orgType = this.orgTypeApplication.loadOrgType(id);
        return forward(ORG_TYPE_DETAIL_PAGE, orgType);
    }

    @RequiresPermissions(value = { "OrgType:update", "DeptType:update", "PosType:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改组织类型")
    public String updateOrgType() {
        SDO params = this.getSDO();
        OrgType orgType = params.toObject(OrgType.class);
        orgTypeApplication.saveOrgType(orgType);
        return success();
    }

    @RequiresPermissions(value = { "OrgType:delete", "DeptType:delete", "PosType:delete" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除组织类型")
    public String deleteOrgTypes() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        orgTypeApplication.deleteOrgTypes(ids);
        return success();
    }

    @RequiresPermissions(value = { "OrgType:update", "DeptType:update", "PosType:update" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改组织类型排序号")
    public String updateOrgTypeSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> orgTypes = params.getStringMap(DATA_KEY_NAME);
        orgTypeApplication.updateOrgTypeSequence(orgTypes);
        return success();
    }

    @RequiresPermissions(value = { "OrgType:move", "DeptType:move", "PosType:move" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.MOVE, description = "移动组织类型")
    public String moveOrgType() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getString(FOLDER_ID_KEY_NAME);
        this.orgTypeApplication.moveOrgType(ids, folderId);
        return success();
    }

    @RequiresPermissions(value = { "OrgType:query", "DeptType:query", "PosType:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询组织类型")
    public String slicedQueryOrgTypes() {
        SDO params = this.getSDO();
        OrgTypeQueryRequest queryRequest = params.toQueryRequest(OrgTypeQueryRequest.class);
        Map<String, Object> data = orgTypeApplication.slicedQueryOrgTypes(queryRequest);
        return toResult(data);
    }
}
