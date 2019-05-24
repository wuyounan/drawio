package com.huigou.uasp.bmp.dataManage.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.datamanagement.DataManageChooseOrgKind;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.dataManage.application.DataManagementApplication;
import com.huigou.uasp.bmp.dataManage.application.OpDataKindApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetail;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetailresource;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagement;
import com.huigou.uasp.bmp.dataManage.domain.query.DataManagePermissionsQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagedetailQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagedetailresourceQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagementQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 数据权限授权
 * 
 * @ClassName: DataManagementController
 * @author xx
 * @date 2018-09-05 17:15
 * @version V1.0
 */
@Controller
@ControllerMapping("dataManagement")
public class DataManagementController extends CommonController {

    @Autowired
    private DataManagementApplication dataManagementApplication;

    @Autowired
    private OpDataKindApplication opDataKindApplication;

    protected String getPagePath() {
        return "/system/datamanage/datamanagedetail/";
    }

    @RequiresPermissions("Datamanagedetail:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到数据管理权限取值定义")
    public String forwardListOpdatamanagedetail() {
        return forward("opdatamanagedetailList");
    }

    @RequiresPermissions("Datamanagedetail:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询数据管理权限取值定义")
    public String slicedQueryOpdatamanagedetail() {
        SDO sdo = this.getSDO();
        OpdatamanagedetailQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagedetailQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.slicedQueryOpdatamanagedetail(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Datamanagedetail:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加数据管理权限取值定义")
    public String showInsertOpdatamanagedetail() {
        SDO sdo = this.getSDO();
        String dataManageId = sdo.getString("dataManageId");
        Map<String, String> resourcekinds = dataManagementApplication.queryDataManageResourcekindByTypeId(dataManageId);
        this.putAttribute("Resourcekinds", resourcekinds);
        return forward("opdatamanagedetail", sdo);
    }

    @RequiresPermissions("Datamanagedetail:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "添加保存数据管理权限取值定义")
    public String insertOpdatamanagedetail() {
        SDO sdo = this.getSDO();
        Opdatamanagedetail opdatamanagedetail = sdo.toObject(Opdatamanagedetail.class);
        String id = dataManagementApplication.saveOpdatamanagedetail(opdatamanagedetail);
        return success(id);
    }

    @RequiresPermissions("Datamanagedetail:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到编辑数据管理权限取值定义")
    public String showLoadOpdatamanagedetail() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        Opdatamanagedetail opdatamanagedetail = dataManagementApplication.loadOpdatamanagedetail(id);
        Map<String, String> resourcekinds = dataManagementApplication.queryDataManageResourcekindByTypeId(opdatamanagedetail.getDataManageId());
        this.putAttribute("Resourcekinds", resourcekinds);
        return forward("opdatamanagedetail", opdatamanagedetail);
    }

    @RequiresPermissions("Datamanagedetail:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "新增修改数据管理权限取值定义")
    public String updateOpdatamanagedetail() {
        SDO sdo = this.getSDO();
        Opdatamanagedetail opdatamanagedetail = sdo.toObject(Opdatamanagedetail.class);
        dataManagementApplication.saveOpdatamanagedetail(opdatamanagedetail);
        return success();
    }

    @RequiresPermissions("Datamanagedetail:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据管理权限取值定义")
    public String deleteOpdatamanagedetail() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManagementApplication.deleteOpdatamanagedetail(ids);
        return success();
    }

    @RequiresPermissions("Datamanagedetail:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询数据管理权限包含维度资源")
    public String queryOpdatamanagedetailresource() {
        SDO sdo = this.getSDO();
        OpdatamanagedetailresourceQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagedetailresourceQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.queryOpdatamanagedetailresource(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Datamanagedetail:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加数据管理权限包含维度资源")
    public String showInsertOpdatamanagedetailresource() {
        SDO sdo = this.getSDO();
        String dataKindId = sdo.getString("dataKindId");
        Map<String, Object> map = opDataKindApplication.findById(dataKindId);
        this.putAttribute("dataManageChooseOrgKinds", DataManageChooseOrgKind.getData());
        map.put("dataKindId", dataKindId);
        map.put("orgDataKind", DataManageChooseOrgKind.CHOOSE.getId());
        return forward("opdatamanageresourceDetail", map);
    }

    @RequiresPermissions("Datamanagedetail:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "添加保存数据管理权限包含维度资源")
    public String insertOpdatamanagedetailresource() {
        SDO sdo = this.getSDO();
        Opdatamanagedetailresource opdatamanagedetailresource = sdo.toObject(Opdatamanagedetailresource.class);
        dataManagementApplication.saveOpdatamanagedetailresource(opdatamanagedetailresource);
        return success();
    }

    @RequiresPermissions("Datamanagedetail:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据管理权限包含维度资源")
    public String deleteOpdatamanagedetailresource() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManagementApplication.deleteOpdatamanagedetailresource(ids);
        return success();
    }

    @RequiresPermissions("DataManagement:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到数据管理权限授权")
    public String forwardListOpdatamanagement() {
        return forward("/system/datamanage/management/opdatamanagementList.jsp");
    }

    @RequiresPermissions("DataManagement:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询数据管理权限授权")
    public String queryOpdatamanagement() {
        SDO sdo = this.getSDO();
        OpdatamanagementQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagementQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.queryOpdatamanagement(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("DataManagement:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "添加保存数据管理权限授权")
    public String insertOpdatamanagement() {
        SDO sdo = this.getSDO();
        String managerId = sdo.getString("managerId");
        List<Opdatamanagement> datas = sdo.getList("datas", Opdatamanagement.class);
        dataManagementApplication.saveOpdatamanagement(managerId, datas);
        return success();
    }

    @RequiresPermissions("DataManagement:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据管理权限授权")
    public String deleteOpdatamanagement() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManagementApplication.deleteOpdatamanagement(ids);
        return success();
    }

    @RequiresPermissions("DataManagement:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询数据管理权限继承数据")
    public String slicedQueryDataManagementByOrgFullId() {
        SDO sdo = this.getSDO();
        OpdatamanagementQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagementQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.slicedQueryDataManagementByOrgFullId(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("DataManagement:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询继承权限包含资源")
    public String slicedQueryDataManageResourceByOrgFullId() {
        SDO sdo = this.getSDO();
        OpdatamanagementQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagementQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.slicedQueryDataManageResourceByOrgFullId(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到数据管理权限授权查询")
    public String forwardDataManageDetailPermission() {
        SDO sdo = this.getSDO();
        sdo.putProperty("singlePerson", "1");
        return forward("/system/datamanage/management/opdatamanagementPermission.jsp", sdo);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "根据资源定义ID查询授权情况")
    public String slicedQueryDataManageByDetailId() {
        SDO sdo = this.getSDO();
        DataManagePermissionsQueryRequest queryRequest = sdo.toQueryRequest(DataManagePermissionsQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.slicedQueryDataManageByDetailId(queryRequest);
        return toResult(data);
    }

    @Deprecated
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "根据资源定义ID查询具有权限的用户")
    public String slicedQueryPersonAsDataManage() {
        SDO sdo = this.getSDO();
        DataManagePermissionsQueryRequest queryRequest = sdo.toQueryRequest(DataManagePermissionsQueryRequest.class);
        Map<String, Object> data = dataManagementApplication.slicedQueryPersonAsDataManage(queryRequest);
        return toResult(data);
    }
}
