package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.application.ManagementQueryApplication;
import com.huigou.uasp.bmp.opm.domain.model.management.BaseManagementType;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;
import com.huigou.uasp.bmp.opm.domain.query.BizManagementTypesQueryRequest;
import com.huigou.uasp.bmp.opm.proxy.ManagementApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

@Controller
@ControllerMapping("management")
public class ManagementController extends CommonController {

    private static final String BASE_MANAGEMENT_TYPE_PAGE = "BaseManagementType";

    private static final String BASE_MANAGEMENT_TYPE_DETAIL_PAGE = "BaseManagementTypeDetail";

    private static final String BIZ_MANAGEMENT_TYPE_PAGE = "BizManagementType";

    private static final String BIZ_MANAGEMENT_TYPE_DETAIL_PAGE = "BizManagementTypeDetail";

    private static final String BIZ_MANAGEMENT_PAGE = "BizManagement";

    private static final String DELEGATION_BIZ_MANAGEMENT_PAGE = "DelegationBizManagement";

    private static final String SELECT_BIZ_MANAGEMENT_TYPE_DIALOG = "SelectBizManagementType";

    private static final String BIZ_MANAGEMENT_QUERY_PAGE = "BizManagementQuery";

    @Autowired
    private ManagementApplicationProxy managementApplication;

    @Autowired
    private ManagementQueryApplication managementQueryApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/management/";
    }

    @RequiresPermissions("BaseManagementType:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到基础管理权限类型列表页面")
    public String forwardBaseManagementType() {
        return forward(BASE_MANAGEMENT_TYPE_PAGE);
    }

    @RequiresPermissions("BaseManagementType:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加基础管理权限类型明细页面")
    public String showBaseManagementTypeDetail() {
        SDO params = this.getSDO();
        String folderId = params.getString("folderId");
        Integer sequence = this.managementApplication.getBaseManagementTypeNextSequence(folderId);
        params.putProperty("sequence", sequence);
        return forward(BASE_MANAGEMENT_TYPE_DETAIL_PAGE, params);
    }

    @RequiresPermissions("BaseManagementType:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改基础管理权限类型明细页面")
    public String loadBaseManagementType() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        BaseManagementType baseManagementType = this.managementApplication.loadBaseManagementType(id);
        return forward(BASE_MANAGEMENT_TYPE_DETAIL_PAGE, baseManagementType);
    }

    @RequiresPermissions("BaseManagementType:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加基础管理权限类型")
    public String insertBaseManagementType() {
        SDO params = this.getSDO();
        BaseManagementType baseManagementType = params.toObject(BaseManagementType.class);
        String bizManagementTypeId = params.getString("bizManagementTypeId");
        String id = managementApplication.saveBaseManagementType(baseManagementType, bizManagementTypeId);
        return success(id);
    }

    @RequiresPermissions("BaseManagementType:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改基础管理权限类型")
    public String updateBaseManagementType() {
        SDO params = this.getSDO();
        BaseManagementType baseManagementType = params.toObject(BaseManagementType.class);
        String bizManagementTypeId = params.getString("bizManagementTypeId");
        managementApplication.saveBaseManagementType(baseManagementType, bizManagementTypeId);
        return success();
    }

    @RequiresPermissions("BaseManagementType:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除基础管理权限类型")
    public String deleteBaseManagementType() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        managementApplication.deleteBaseManagementTypes(ids);
        return success();
    }

    @RequiresPermissions("BaseManagementType:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动基础管理权限类型")
    public String updateBaseManagementTypeFolderId() {
        SDO params = this.getSDO();
        String folderId = params.getString("folderId");
        List<String> ids = params.getStringList("ids");
        managementApplication.moveBaseManagementTypes(ids, folderId);
        return success();
    }

    @RequiresPermissions("BaseManagementType:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改基础管理权限类型排序号")
    public String updateBaseManagementTypeSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> baseManagementTypes = params.getStringMap("data");
        managementApplication.updateBaseManagementTypeSequence(baseManagementTypes);
        return success();
    }

    @RequiresPermissions("BaseManagementType:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询基础管理权限类型")
    public String slicedQueryBaseManagementTypes() {
        SDO params = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryBaseManagementTypes(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("ManagementType:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到业务管理权限类别列表页面")
    public String forwardBizManagementType() {
        return this.forward(BIZ_MANAGEMENT_TYPE_PAGE);
    }

    @RequiresPermissions("ManagementType:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加业务管理权限类别明细页面")
    public String showBizManagementTypeDetail() {
        SDO params = this.getSDO();

        String parentId = params.getString("parentId");
        Integer sequence = this.managementApplication.getBizManagementTypeNextSequence(parentId);
        params.putProperty("sequence", sequence);

        return this.forward(BIZ_MANAGEMENT_TYPE_DETAIL_PAGE, params);
    }

    // @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到业务管理权限类别页面")
    public String showSelectBizManagementTypeDialog() {
        return this.forward(SELECT_BIZ_MANAGEMENT_TYPE_DIALOG, this.getSDO().getProperties());
    }

    @RequiresPermissions("ManagementType:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加业务管理权限类别")
    public String insertBizManagementType() {
        SDO params = this.getSDO();
        BizManagementType bizManagementType = params.toObject(BizManagementType.class);
        String id = this.managementApplication.saveBizManagementType(bizManagementType);
        return success(id);
    }

    @RequiresPermissions("ManagementType:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改业务管理权限类别")
    public String updateBizManagementType() {
        SDO params = this.getSDO();
        BizManagementType bizManagementType = params.toObject(BizManagementType.class);
        managementApplication.saveBizManagementType(bizManagementType);
        return success();
    }

    @RequiresPermissions("ManagementType:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改业务管理权限类别明细页面")
    public String loadBizManagementType() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        BizManagementType bizManagementType = this.managementApplication.loadBizManagementType(id);
        return this.forward(BIZ_MANAGEMENT_TYPE_DETAIL_PAGE, bizManagementType);
    }

    @RequiresPermissions("ManagementType:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除业务管理权限类别")
    public String deleteBizManagementType() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        managementApplication.deleteBizManagementTypes(ids);
        return success();
    }

    @RequiresPermissions("ManagementType:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改业务管理权限类别排序号")
    public String updateBizManagementTypeSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap("data");
        managementApplication.updateBizManagementTypeSequence(data);
        return success();
    }

    @RequiresPermissions("ManagementType:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动业务管理权限类别")
    public String moveBizManagementType() {
        SDO params = this.getSDO();
        String parentId = params.getString("parentId");
        List<String> ids = params.getStringList("ids");
        managementApplication.moveBizManagementTypes(ids, parentId);
        return success();
    }

    @RequiresPermissions(value = { "ManagementType:query", "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询业务管理权限类别")
    public String queryBizManagementTypes() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        List<Map<String, Object>> data = managementApplication.queryBizManagementTypes(parentId);
        return this.toResult(data);
    }

    @RequiresPermissions(value = { "ManagementType:query", "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询业务管理权限类别")
    public String slicedQueryBizManagementTypes() {
        SDO params = this.getSDO();
        ParentAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(ParentAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryBizManagementTypes(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions(value = { "ManagementType:query", "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询业务权限类别")
    public String slicedQueryBizManagementTypesByKeyWord() {
        SDO params = this.getSDO();
        BizManagementTypesQueryRequest queryRequest = params.toQueryRequest(BizManagementTypesQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryBizManagementTypes(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions(value = { "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到业务管理权限授权列表页面")
    public String forwardBizManagement() {
        SDO params = this.getSDO();
        return forward(BIZ_MANAGEMENT_PAGE, params);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到分级授权业务管理权限页面")
    public String forwardDelegationBizManagement() {
        return forward(DELEGATION_BIZ_MANAGEMENT_PAGE);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到业务管理权限查询页面")
    public String forwardBizManagementQuery() {
        return forward(BIZ_MANAGEMENT_QUERY_PAGE);
    }

    @RequiresPermissions(value = { "AllocateSubordination:create", "AllocateManager:create" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "分配管理者")
    public String allocateManagers() {
        SDO params = this.getSDO();
        List<String> managerIds = params.getStringList("managerIds");
        String manageTypeId = params.getString("manageTypeId");
        String subordinationId = params.getString("subordinationId");

        this.managementApplication.allocateManagers(managerIds, manageTypeId, subordinationId);
        return success();
    }

    @RequiresPermissions(value = { "AllocateSubordination:create", "AllocateManager:create" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "分配下属")
    public String allocateSubordinations() {
        SDO params = this.getSDO();
        String managerId = params.getString("managerId");
        String manageTypeId = params.getString("manageTypeId");
        List<String> subordinationIds = params.getStringList("subordinationIds");
        managementApplication.allocateSubordinations(managerId, manageTypeId, subordinationIds);
        return success();
    }

    @RequiresPermissions(value = { "AllocateSubordination:delete", "AllocateManager:delete" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除业务管理权限")
    public String deleteBizManagement() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.managementApplication.deleteBizManagements(ids);
        return success();
    }

    @RequiresPermissions(value = { "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询业务管理权限")
    public String slicedQueryBizManagements() {
        SDO params = this.getSDO();
        String managerId = params.getString("managerId");
        String manageTypeId = params.getString("manageTypeId");
        String subordinationId = params.getString("subordinationId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data;
        if (StringUtil.isBlank(managerId)) {
            // 查找下属的管理者
            data = managementApplication.slicedQueryBizManagementsBySubordinationId(subordinationId, manageTypeId, queryRequest);
        } else {
            // 查找管理的下属
            data = managementApplication.slicedQueryBizManagementsByManagerId(managerId, manageTypeId, queryRequest);
        }

        return toResult(data);
    }

    @RequiresPermissions("AllocateManager:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组织已分配的业务管理权限类别")
    public String slicedQueryOrgAllocatedBizManagementTypeForManager() {
        SDO params = this.getSDO();
        String orgFullId = params.getString("selectedFullId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryOrgAllocatedBizManagementTypeForManager(orgFullId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("AllocateSubordination:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组已织分配的业务管理权限类别")
    public String slicedQueryOrgAllocatedBizManagementTypeForSubordination() {
        SDO params = this.getSDO();
        String orgFullId = params.getString("selectedFullId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryOrgAllocatedBizManagementTypeForSubordination(orgFullId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("AllocateManager:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询业务管理权限")
    public String slicedQueryBizManagementForManager() {
        SDO params = this.getSDO();
        String orgFullId = params.getString("selectedFullId");
        String manageTypeId = params.getString("manageTypeId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryBizManagementForManager(orgFullId, manageTypeId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("AllocateSubordination:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询业务管理权限")
    public String slicedQueryBizManagementForSubordination() {
        SDO params = this.getSDO();
        String orgFullId = params.getString("selectedFullId");
        String manageTypeId = params.getString("manageTypeId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = managementApplication.slicedQueryBizManagementForSubordination(orgFullId, manageTypeId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions(value = { "AllocateSubordination:cache", "AllocateManager:cache" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.CACHE, description = "删除权限缓存")
    public String removePermissionCache() {
        managementApplication.removePermissionCache();
        return success();
    }

    @RequiresPermissions("ManagementType:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到通过业务管理权限类别ID查询业务管理权限授权列表页面")
    public String forwardQueryManagementByTypeId() {
        return forward("BizManagementByTypeId", this.getSDO());
    }

    @RequiresPermissions("ManagementType:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "通过业务管理权限类别ID查询业务管理权限授权")
    public String slicedQueryManagementByTypeId() {
        SDO params = this.getSDO();
        BizManagementTypesQueryRequest queryRequest = params.toQueryRequest(BizManagementTypesQueryRequest.class);
        Map<String, Object> data = managementQueryApplication.slicedQueryManagementByTypeId(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions(value = { "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到查询业务管理权限授权列表页面")
    public String forwardPermissionBizManagementQuery() {
        return forward("PermissionBizManagementQuery", this.getSDO());
    }

    @RequiresPermissions(value = { "AllocateSubordination:query", "AllocateManager:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询业务管理权限授权")
    public String slicedPermissionBizManagementQuery() {
        SDO params = this.getSDO();
        BizManagementTypesQueryRequest queryRequest = params.toQueryRequest(BizManagementTypesQueryRequest.class);
        Map<String, Object> data = managementQueryApplication.slicedPermissionBizManagementQuery(queryRequest);
        return toResult(data);
    }

}
