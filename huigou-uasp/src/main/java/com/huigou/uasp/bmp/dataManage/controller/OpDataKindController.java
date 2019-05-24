package com.huigou.uasp.bmp.dataManage.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.data.datamanagement.DataResourceKind;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.dataManage.application.OpDataKindApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatakind;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatakindQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 数据管理权限维度定义
 * 
 * @ClassName: OpDataKindController
 * @author xx
 * @date 2018-09-04 10:52
 * @version V1.0
 */
@Controller
@ControllerMapping("opDataKind")
public class OpDataKindController extends CommonController {

    @Autowired
    private OpDataKindApplication opDataKindApplication;

    protected String getPagePath() {
        return "/system/datamanage/datakind/";
    }

    @RequiresPermissions("OpDataKind:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到数据管理权限维度定义")
    public String forwardListOpdatakind() {
        this.putAttribute("DataResourceKinds", DataResourceKind.getData());
        return forward("opdatakindList");
    }

    @RequiresPermissions("OpDataKind:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "分页查询数据管理权限维度定义")
    public String slicedQueryOpdatakind() {
        SDO sdo = this.getSDO();
        OpdatakindQueryRequest queryRequest = sdo.toQueryRequest(OpdatakindQueryRequest.class);
        Map<String, Object> data = opDataKindApplication.slicedQueryOpdatakind(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("OpDataKind:create")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加数据管理权限维度定义")
    public String showInsertOpdatakind() {
        this.putAttribute("DataResourceKinds", DataResourceKind.getData());
        return forward("opdatakindDetail");
    }

    @RequiresPermissions("OpDataKind:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "跳转到编辑数据管理权限维度定义")
    public String showLoadOpdatakind() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        Opdatakind opdatakind = opDataKindApplication.loadOpdatakind(id);
        this.putAttribute("DataResourceKinds", DataResourceKind.getData());
        return forward("opdatakindDetail", opdatakind);
    }

    @RequiresPermissions("OpDataKind:update")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "添加修改数据管理权限维度定义")
    public String saveOpdatakind() {
        SDO sdo = this.getSDO();
        Opdatakind opdatakind = sdo.toObject(Opdatakind.class);
        opDataKindApplication.saveOpdatakind(opdatakind);
        return success();
    }

    @RequiresPermissions("OpDataKind:delete")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "删除数据管理权限维度定义")
    public String deleteOpdatakind() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        Assert.notEmpty(ids, "请选择需要删除的数据!");
        opDataKindApplication.deleteOpdatakind(ids.get(0));
        return success();
    }

    @RequiresPermissions("OpDataKind:update")
    public String updateOpdatakindSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        opDataKindApplication.updateOpdatakindSequence(map);
        return success();
    }

}
