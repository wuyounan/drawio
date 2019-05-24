package com.huigou.uasp.bmp.configuration.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.configuration.application.OrgfunApplication;
import com.huigou.uasp.bmp.configuration.domain.model.Orgfun;
import com.huigou.uasp.bmp.configuration.domain.query.OrgfunQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 组织机构函数记录维护
 * 
 * @ClassName: OrgfunController
 * @author xx
 * @date 2018-03-09 10:47
 * @version V1.0
 */
@Controller
@ControllerMapping("orgfun")
public class OrgfunController extends CommonController {

    @Autowired
    private OrgfunApplication orgfunApplication;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    @RequiresPermissions("Orgfun:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到组织机构函数定义列表页面")
    public String forwardListOrgfun() {
        return forward("orgfunList");
    }

    @RequiresPermissions("Orgfun:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询组织机构函数定义")
    public String slicedQueryOrgfun() {
        SDO sdo = this.getSDO();
        OrgfunQueryRequest queryRequest = sdo.toQueryRequest(OrgfunQueryRequest.class);
        Map<String, Object> data = orgfunApplication.slicedQueryOrgfun(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("Orgfun:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询组织机构函数定义")
    public String queryOrgfunByParentId() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getParentId();
        List<Map<String, Object>> list = orgfunApplication.queryOrgfunByParentId(parentId);
        return toResult(list);
    }

    @RequiresPermissions("Orgfun:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加组织机构函数定义明细页面")
    public String showInsertOrgfun() {
        return forward("orgfunDetail");
    }

    @RequiresPermissions("Orgfun:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加组织机构函数定义")
    public String insertOrgfun() {
        SDO sdo = this.getSDO();
        Orgfun orgfun = sdo.toObject(Orgfun.class);
        String id = orgfunApplication.saveOrgfun(orgfun);
        return success(id);
    }

    @RequiresPermissions("Orgfun:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改组织机构函数定义页面")
    public String showLoadOrgfun() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        Orgfun orgfun = orgfunApplication.loadOrgfun(id);
        return forward("orgfunDetail", orgfun);
    }

    @RequiresPermissions("Orgfun:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改组织机构函数明细定义")
    public String updateOrgfun() {
        SDO sdo = this.getSDO();
        Orgfun orgfun = sdo.toObject(Orgfun.class);
        orgfunApplication.saveOrgfun(orgfun);
        return success();
    }

    @RequiresPermissions("Orgfun:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除组织机构函数定义")
    public String deleteOrgfun() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        orgfunApplication.deleteOrgfun(ids);
        return success();
    }

    @RequiresPermissions("Orgfun:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改组织机构函数定义排序号")
    public String updateOrgfunSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        orgfunApplication.updateOrgfunSequence(map);
        return success();
    }

    @RequiresPermissions("Orgfun:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改组织机构函数定义状态")
    public String updateOrgfunStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        Integer status = sdo.getInteger(STATUS_KEY_NAME);
        orgfunApplication.updateOrgfunStatus(ids, status);
        return success();
    }
    @RequiresPermissions("Orgfun:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动组织机构函数定义")
    public String moveOrgfun() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String parentId = params.getParentId();
        orgfunApplication.moveOrgfun(ids, parentId);
        return success();
    }

}
