package com.huigou.uasp.bmp.bizconfig.function.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bmp.bizconfig.function.application.GroupFunctionApplication;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctions;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetails;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsGroup;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsDetailsQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsGroupQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;

/**
 * 业务功能分组管理
 * 
 * @ClassName: BizFunctionController
 * @author
 * @date 2018-03-28 11:16
 * @version V1.0
 */
@Controller
@ControllerMapping("groupFunction")
public class GroupFunctionController extends CommonController {

    @Autowired
    private GroupFunctionApplication groupFunctionApplication;

    protected String getPagePath() {
        return "/system/bizconfig/functions/";
    }

    @RequiresPermissions("GroupFunction:query")
    public String forwardListFunctions() {
        return forward("functionsList");
    }

    @RequiresPermissions("GroupFunction:query")
    public String slicedQueryFunctions() {
        SDO sdo = this.getSDO();
        FunctionsQueryRequest queryRequest = sdo.toQueryRequest(FunctionsQueryRequest.class);
        Map<String, Object> data = groupFunctionApplication.slicedQueryFunctions(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("GroupFunction:create")
    public String showInsertFunctions() {
        return forward("functionsAdd");
    }

    @RequiresPermissions("GroupFunction:create")
    public String insertFunctions() {
        SDO sdo = this.getSDO();
        BpmFunctions functions = sdo.toObject(BpmFunctions.class);
        String id = groupFunctionApplication.saveFunctions(functions);
        return success(id);
    }

    @RequiresPermissions("GroupFunction:update")
    public String showLoadFunctions() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        BpmFunctions functions = groupFunctionApplication.loadFunctions(id);
        return forward("functionsDetail", functions);
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctions() {
        SDO sdo = this.getSDO();
        BpmFunctions functions = sdo.toObject(BpmFunctions.class);
        groupFunctionApplication.saveFunctions(functions);
        return success();
    }

    @RequiresPermissions("GroupFunction:delete")
    public String deleteFunctions() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        groupFunctionApplication.deleteFunctions(id);
        return success();
    }

    @RequiresPermissions("GroupFunction:query")
    public String forwardListFunctionsGroup() {
        return forward("functionsGroupList");
    }

    @RequiresPermissions("GroupFunction:query")
    public String queryFunctionsGroup() {
        SDO sdo = this.getSDO();
        FunctionsGroupQueryRequest queryRequest = sdo.toQueryRequest(FunctionsGroupQueryRequest.class);
        Map<String, Object> data = groupFunctionApplication.queryFunctionsGroup(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("GroupFunction:create")
    public String showInsertFunctionsGroup() {
        this.putAttribute("groupColorList", groupFunctionApplication.getGroupColorMap());
        return forward("functionsGroupDetail");
    }

    @RequiresPermissions("GroupFunction:create")
    public String insertFunctionsGroup() {
        SDO sdo = this.getSDO();
        BpmFunctionsGroup functionsGroup = sdo.toObject(BpmFunctionsGroup.class);
        String id = groupFunctionApplication.saveFunctionsGroup(functionsGroup);
        return success(id);
    }

    @RequiresPermissions("GroupFunction:update")
    public String showLoadFunctionsGroup() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        this.putAttribute("groupColorList", groupFunctionApplication.getGroupColorMap());
        BpmFunctionsGroup functionsGroup = groupFunctionApplication.loadFunctionsGroup(id);
        return forward("functionsGroupDetail", functionsGroup);
    }

    public String showChooseFunctionsGroupColor() {
        this.putAttribute("groupColorList", groupFunctionApplication.getGroupColorMap());
        return forward("functionsGroupDetail");
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctionsGroup() {
        SDO sdo = this.getSDO();
        BpmFunctionsGroup functionsGroup = sdo.toObject(BpmFunctionsGroup.class);
        groupFunctionApplication.saveFunctionsGroup(functionsGroup);
        return success();
    }

    @RequiresPermissions("GroupFunction:delete")
    public String deleteFunctionsGroup() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        groupFunctionApplication.deleteFunctionsGroup(ids);
        return success();
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctionsGroupSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        groupFunctionApplication.updateFunctionsGroupSequence(map);
        return success();
    }

    @RequiresPermissions("GroupFunction:query")
    public String forwardListFunctionsDetails() {
        return forward("functionsDetailsList");
    }

    @RequiresPermissions("GroupFunction:query")
    public String queryFunctionsDetails() {
        SDO sdo = this.getSDO();
        FunctionsDetailsQueryRequest queryRequest = sdo.toQueryRequest(FunctionsDetailsQueryRequest.class);
        Map<String, Object> data = groupFunctionApplication.queryFunctionsDetails(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("GroupFunction:create")
    public String showInsertFunctionsDetails() {
        this.putAttribute("functionColorList", groupFunctionApplication.getFuncColorMap());
        return forward("functionsFunDetail");
    }

    @RequiresPermissions("GroupFunction:create")
    public String insertFunctionsDetails() {
        SDO sdo = this.getSDO();
        BpmFunctionsDetails functionsDetails = sdo.toObject(BpmFunctionsDetails.class);
        String id = groupFunctionApplication.saveFunctionsDetails(functionsDetails);
        return success(id);
    }

    @RequiresPermissions("GroupFunction:update")
    public String showLoadFunctionsDetails() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        BpmFunctionsDetails functionsDetails = groupFunctionApplication.loadFunctionsDetails(id);
        this.putAttribute("functionColorList", groupFunctionApplication.getFuncColorMap());
        return forward("functionsFunDetail", functionsDetails);
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctionsDetails() {
        SDO sdo = this.getSDO();
        BpmFunctionsDetails functionsDetails = sdo.toObject(BpmFunctionsDetails.class);
        groupFunctionApplication.saveFunctionsDetails(functionsDetails);
        return success();
    }

    public String showChooseFunctionsDetailColor() {
        this.putAttribute("functionColorList", groupFunctionApplication.getFuncColorMap());
        return forward("functionsFunDetailColor");
    }

    @RequiresPermissions("GroupFunction:delete")
    public String deleteFunctionsDetails() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        groupFunctionApplication.deleteFunctionsDetails(ids);
        return success();
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctionsDetailsSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        groupFunctionApplication.updateFunctionsDetailsSequence(map);
        return success();
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctionsDetailsGroup() {
        SDO sdo = this.getSDO();
        String groupId = sdo.getString("groupId");
        List<String> ids = sdo.getIds();
        groupFunctionApplication.updateFunctionsDetailsGroup(ids, groupId);
        return success();
    }

    @RequiresPermissions("GroupFunction:update")
    public String updateFunctionsDetailsColor() {
        SDO sdo = this.getSDO();
        String color = sdo.getString("color");
        List<String> ids = sdo.getIds();
        groupFunctionApplication.updateFunctionsDetailsColor(ids, color);
        return success();
    }

    public String showFunctionsGroup() {
        SDO sdo = this.getSDO();
        String code = sdo.getString("code");
        this.putAttribute("functionGroupList", groupFunctionApplication.queryFunctionsGroup(code));
        return forward("functionsGroup");
    }

    /**
     * RESTful 风格调用
     * 
     * @param code
     * @return
     */
    @ControllerMethodMapping(value = "/{code}/show", combine = true)
    public String show(@PathVariable("code") String code) {
        this.putAttribute("functionGroupList", groupFunctionApplication.queryFunctionsGroup(code));
        return forward("functionsGroup");
    }

}
