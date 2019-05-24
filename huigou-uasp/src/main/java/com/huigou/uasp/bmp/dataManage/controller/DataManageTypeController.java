package com.huigou.uasp.bmp.dataManage.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.datamanagement.DataManageNodeKind;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.dataManage.application.DataManageTypeApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetype;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagetypeQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 数据管理权限类别定义
 * 
 * @ClassName: DataManageTypeController
 * @author xx
 * @date 2018-09-04 11:58
 * @version V1.0
 */
@Controller
@ControllerMapping("dataManageType")
public class DataManageTypeController extends CommonController {

    @Autowired
    private DataManageTypeApplication dataManageTypeApplication;

    protected String getPagePath() {
        return "/system/datamanage/datamanagetype/";
    }

    @RequiresPermissions("DataManageType:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到数据管理权限类型")
    public String forwardListOpdatamanagetype() {
        return forward("opdatamanagetypeList");
    }

    @RequiresPermissions("DataManageType:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "层级查询数据管理权限类型")
    public String queryDatamanagetypekind() {
        SDO sdo = this.getSDO();
        OpdatamanagetypeQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagetypeQueryRequest.class);
        List<Map<String, Object>> datas = dataManageTypeApplication.queryDatamanagetypekind(queryRequest);
        return toResult(datas);
    }

    @RequiresPermissions("DataManageType:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询数据管理权限类型")
    public String slicedQueryOpdatamanagetype() {
        SDO sdo = this.getSDO();
        OpdatamanagetypeQueryRequest queryRequest = sdo.toQueryRequest(OpdatamanagetypeQueryRequest.class);
        Map<String, Object> data = dataManageTypeApplication.slicedQueryOpdatamanagetype(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("DataManageType:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加数据管理权限类型")
    public String showInsertOpdatamanagetype() {
        SDO sdo = this.getSDO();
        this.putAttribute("DataManageNodeKinds", DataManageNodeKind.getData());
        return forward("opdatamanagetypeDetail", sdo);
    }

    @RequiresPermissions("DataManageType:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "添加保存数据管理权限类型")
    public String insertOpdatamanagetype() {
        SDO sdo = this.getSDO();
        Opdatamanagetype opdatamanagetype = sdo.toObject(Opdatamanagetype.class);
        String id = dataManageTypeApplication.insertOpdatamanagetype(opdatamanagetype);
        return success(id);
    }

    @RequiresPermissions("DataManageType:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到编辑数据管理权限类型")
    public String showLoadOpdatamanagetype() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        Opdatamanagetype opdatamanagetype = dataManageTypeApplication.loadOpdatamanagetype(id);
        this.putAttribute("DataManageNodeKinds", DataManageNodeKind.getData());
        return forward("opdatamanagetypeDetail", opdatamanagetype);
    }

    @RequiresPermissions("DataManageType:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "添加修改数据管理权限类型")
    public String updateOpdatamanagetype() {
        SDO sdo = this.getSDO();
        Opdatamanagetype opdatamanagetype = sdo.toObject(Opdatamanagetype.class);
        dataManageTypeApplication.updateOpdatamanagetype(opdatamanagetype);
        return success();
    }

    @RequiresPermissions("DataManageType:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据管理权限类型")
    public String deleteOpdatamanagetype() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManageTypeApplication.deleteOpdatamanagetype(ids);
        return success();
    }

    @RequiresPermissions("DataManageType:update")
    public String updateOpdatamanagetypeSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        dataManageTypeApplication.updateOpdatamanagetypeSequence(map);
        return success();
    }

    @RequiresPermissions("DataManageType:update")
    public String moveOpdatamanagetype() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getParentId();
        List<String> ids = sdo.getIds();
        dataManageTypeApplication.moveOpdatamanagetype(parentId, ids);
        return success();
    }

    @RequiresPermissions("DataManageType:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "添加保存数据资源类型")
    public String saveDatamanagetypekinds() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        List<String> kindIds = sdo.getStringList("kindIds");
        dataManageTypeApplication.saveDatamanagetypekinds(id, kindIds);
        return success();
    }

    @RequiresPermissions("DataManageType:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "删除数据资源类型")
    public String deleteDatamanagetypekinds() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        dataManageTypeApplication.deleteDatamanagetypekinds(ids);
        return success();
    }

    @RequiresPermissions("DataManageType:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "查询已选择的资源类别")
    public String queryOpdatamanagetypekind() {
        SDO sdo = this.getSDO();
        ParentIdQueryRequest queryRequest = sdo.toQueryRequest(ParentIdQueryRequest.class);
        Map<String, Object> data = dataManageTypeApplication.queryOpdatamanagetypekind(queryRequest);
        return toResult(data);
    }

}
