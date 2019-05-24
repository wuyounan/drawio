package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction.NodeKind;
import com.huigou.uasp.bmp.opm.proxy.SysFunctionApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("sysFunction")
public class SysFunctionController extends CommonController {

    private final static String LIST_PAGE = "Function";

    private final static String DETAIL_PAGE = "FunctionDetail";

    private final static String NODE_KIND_LIST_KEY_NAME = "nodeKindList";

    @Autowired
    private SysFunctionApplicationProxy sysFunctionApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/permission/";
    }

    @RequiresPermissions("Func:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到功能列表页面")
    public String forwardFunction() {
        return forward(LIST_PAGE);
    }

    @RequiresPermissions("Func:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加功能页面")
    public String showFunctionDetail() {
        SDO params = this.getSDO();
        String parentId = params.getString(PARENT_ID_KEY_NAME);
        Integer sequence = this.sysFunctionApplication.getSysFunctionNextSequence(parentId);
        params.putProperty(NODE_KIND_LIST_KEY_NAME, NodeKind.getData());
        params.putProperty(SEQUENCE_KEY_NAME, sequence);
        return forward(DETAIL_PAGE, params);
    }

    @RequiresPermissions("Func:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到修改功能页面")
    public String loadFunction() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        SysFunction sysFunction = this.sysFunctionApplication.loadSysFunction(id);
        this.putAttribute(NODE_KIND_LIST_KEY_NAME, NodeKind.getData());
        return forward(DETAIL_PAGE, sysFunction);
    }

    @RequiresPermissions("Func:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加功能")
    public String insertFunction() {
        SDO params = this.getSDO();
        SysFunction sysFunction = params.toObject(SysFunction.class);
        this.sysFunctionApplication.insertSysFunction(sysFunction);
        return success();
    }

    @RequiresPermissions("Func:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改功能")
    public String updateFunction() {
        SDO params = this.getSDO();
        SysFunction sysFunction = params.toObject(SysFunction.class);
        this.sysFunctionApplication.updateSysFunction(sysFunction);
        return success();
    }

    @RequiresPermissions("Func:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除功能")
    public String deleteFunctions() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        this.sysFunctionApplication.deleteSysFunctions(ids);
        return success();
    }

    @RequiresPermissions("Func:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改功能状态")
    public String updateSysFunctionsStatus() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        Integer status = params.getInteger(STATUS_KEY_NAME);

        this.sysFunctionApplication.updateSysFunctionsStatus(ids, status);
        return success();
    }

    @RequiresPermissions("Func:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改功能排序号")
    public String updateFunctionsSequence() {
        SDO params = this.getSDO();
        this.sysFunctionApplication.updateSysFunctionsSequence(params.getStringMap("data"));
        return success();
    }

    @RequiresPermissions("Func:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询功能")
    public String queryFunctions() {
        SDO params = this.getSDO();
        ParentAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(ParentAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = this.sysFunctionApplication.querySysFunctions(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("Func:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "构建权限")
    public String buildPermission() {
        SDO params = this.getSDO();
        String fullId = params.getString("fullId");
        this.sysFunctionApplication.buildPermission(fullId);
        return this.success();
    }

    @RequiresPermissions("Func:move")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.MOVE, description = "移动功能")
    public String moveFunctions() {
        SDO params = this.getSDO();
        String parentId = params.getString("parentId");
        List<String> ids = params.getStringList("ids");
        this.sysFunctionApplication.moveFunctions(parentId, ids);
        return success();
    }

}