package com.huigou.uasp.bmp.dataManage.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.datamanagement.DataFieldSymbolKind;
import com.huigou.data.datamanagement.DataManageNodeKind;
import com.huigou.data.datamanagement.DataTypeKind;
import com.huigou.data.query.PermissionKind;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.dataManage.application.DataManageBusinessApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagebusiness;
import com.huigou.uasp.bmp.dataManage.domain.model.OpdatamanagebusinessField;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagebusinessFieldQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagebusinessQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 数据管理权限业务类型
 * 
 * @ClassName: DataManageBusinessController
 * @author xx
 * @date 2018-09-27 12:04
 * @version V1.0
 */
@Controller
@ControllerMapping("dataManageBusiness")
public class DataManageBusinessController extends CommonController {

    @Autowired
    private DataManageBusinessApplication dataManageBusinessApplication;

    protected String getPagePath() {
        return "/system/datamanage/managebusiness/";
    }

    @RequiresPermissions("DataManageBusiness:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到数据管理权限业务类型列表页面")
    public String forwardListOpdatamanagebusiness() {
        return forward("opdatamanagebusinessList");
    }

    @RequiresPermissions("DataManageBusiness:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询数据管理权限业务类型")
    public String slicedQueryOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        OpdatamanagebusinessQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagebusinessQueryRequest.class);
        Map<String, Object> data = dataManageBusinessApplication.slicedQueryOpdatamanagebusiness(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("DataManageBusiness:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "层级查询数据管理权限类型")
    public String queryDatamanagebusiness() {
        SDO sdo = this.getSDO();
        OpdatamanagebusinessQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagebusinessQueryRequest.class);
        List<Map<String, Object>> datas = dataManageBusinessApplication.queryDatamanagebusiness(queryRequest);
        return toResult(datas);
    }

    @RequiresPermissions("DataManageBusiness:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加数据管理权限业务类型明细页面")
    public String showInsertOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        this.putAttribute("DataManageNodeKinds", DataManageNodeKind.getData());
        return forward("opdatamanagebusinessDetail", sdo);
    }

    @RequiresPermissions("DataManageBusiness:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "添加保存数据管理权限业务类型")
    public String insertOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        Opdatamanagebusiness opdatamanagebusiness = sdo.toObject(Opdatamanagebusiness.class);
        String id = dataManageBusinessApplication.insertOpdatamanagebusiness(opdatamanagebusiness);
        return success(id);
    }

    @RequiresPermissions("DataManageBusiness:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改数据管理权限业务类型明细页面")
    public String showLoadOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        Opdatamanagebusiness opdatamanagebusiness = dataManageBusinessApplication.loadOpdatamanagebusiness(id);
        this.putAttribute("DataManageNodeKinds", DataManageNodeKind.getData());
        return forward("opdatamanagebusinessDetail", opdatamanagebusiness);
    }

    @RequiresPermissions("DataManageBusiness:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "编辑修改数据管理权限业务类型")
    public String updateOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        Opdatamanagebusiness opdatamanagebusiness = sdo.toObject(Opdatamanagebusiness.class);
        dataManageBusinessApplication.updateOpdatamanagebusiness(opdatamanagebusiness);
        return success();
    }

    @RequiresPermissions("DataManageBusiness:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据管理权限业务类型")
    public String deleteOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManageBusinessApplication.deleteOpdatamanagebusiness(ids);
        return success();
    }

    @RequiresPermissions("DataManageBusiness:update")
    public String updateOpdatamanagebusinessSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        dataManageBusinessApplication.updateOpdatamanagebusinessSequence(map);
        return success();
    }

    @RequiresPermissions("DataManageBusiness:update")
    public String moveOpdatamanagebusiness() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getParentId();
        List<String> ids = sdo.getIds();
        dataManageBusinessApplication.moveOpdatamanagebusiness(parentId, ids);
        return success();
    }

    @RequiresPermissions("DataManageBusiness:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询数据管理权限字段定义")
    public String queryOpdatamanagebusinessField() {
        SDO sdo = this.getSDO();
        OpdatamanagebusinessFieldQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagebusinessFieldQueryRequest.class);
        Map<String, Object> datas = dataManageBusinessApplication.queryOpdatamanagebusinessField(queryRequest);
        return toResult(datas);
    }

    @RequiresPermissions("DataManageBusiness:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据权限字段定义")
    public String deleteOpdatamanagebusinessField() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManageBusinessApplication.deleteOpdatamanagebusinessField(ids);
        return success();
    }

    @RequiresPermissions("DataManageBusiness:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加数据管理权限字段定义页面")
    public String showInsertOpdatamanagebusinessField() {
        SDO sdo = this.getSDO();
        Integer isOrgCondition = sdo.getInteger("isOrgCondition");
        isOrgCondition = isOrgCondition == null ? 0 : isOrgCondition;
        this.putAttribute("DataTypeKinds", DataTypeKind.getData());
        this.putAttribute("DataFieldSymbolKinds", DataFieldSymbolKind.getData());
        this.putAttribute("OrgPermissionKinds", PermissionKind.getChooseData());
        this.putAttribute("columnDataType", DataTypeKind.STRING.getId());
        return forward(isOrgCondition == 0 ? "opdatamanageFieldDetail" : "opdatamanageFieldOrgDetail", sdo);
    }

    @RequiresPermissions("DataManageBusiness:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到编辑数据管理权限字段定义页面")
    public String showLoadOpdatamanagebusinessField() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        OpdatamanagebusinessField odatamanagebusinessField = dataManageBusinessApplication.loadOpdatamanagebusinessField(id);
        this.putAttribute("DataTypeKinds", DataTypeKind.getData());
        this.putAttribute("DataFieldSymbolKinds", DataFieldSymbolKind.getData());
        this.putAttribute("OrgPermissionKinds", PermissionKind.getChooseData());
        Integer isOrgCondition = odatamanagebusinessField.getIsOrgCondition();
        isOrgCondition = isOrgCondition == null ? 0 : isOrgCondition;
        return forward(isOrgCondition == 0 ? "opdatamanageFieldDetail" : "opdatamanageFieldOrgDetail", odatamanagebusinessField);
    }

    @RequiresPermissions("DataManageBusiness:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "修改数据管理权限字段定义")
    public String saveOpdatamanagebusinessField() {
        SDO sdo = this.getSDO();
        OpdatamanagebusinessField field = sdo.toObject(OpdatamanagebusinessField.class);
        dataManageBusinessApplication.saveOpdatamanagebusinessField(field);
        return success();
    }
}
