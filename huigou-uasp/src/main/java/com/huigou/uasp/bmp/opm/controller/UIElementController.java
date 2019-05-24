package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.domain.model.resource.UIElement;
import com.huigou.uasp.bmp.opm.domain.query.UIElementsQueryRequest;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.UIElementApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("uiElement")
public class UIElementController extends CommonController {

    private static final String UI_ELEMENT_PAGE = "UIElement";

    private static final String UI_ELEMENT_DETAIL_PAGE = "UIElementDetail";

    @Autowired
    private UIElementApplicationProxy uiElementApplication;

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Override
    protected String getPagePath() {
        return "/system/opm/uielement/";
    }

    @RequiresPermissions("UIElement:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到界面元素列表页面")
    public String forwardUIElement() {
        Map<String, Object> operations = accessApplication.queryUIElementOperations();
        this.putAttribute("uiElementOperations", operations);
        return this.forward(UI_ELEMENT_PAGE);
    }

    @RequiresPermissions("UIElement:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加界面元素明细页面")
    public String showUIElementDetail() {
        SDO params = this.getSDO();
        String folderId = params.getString("folderId");
        Integer sequence = this.uiElementApplication.getUIElementNextSequence(folderId);
        params.putProperty("sequence", sequence);
        return forward(UI_ELEMENT_DETAIL_PAGE, params);
    }

    @RequiresPermissions("UIElement:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "跳转到修改界面元素明细页面")
    public String loadUIElement() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        UIElement uiElement = this.uiElementApplication.loadUIElement(id);
        return forward(UI_ELEMENT_DETAIL_PAGE, uiElement);
    }

    @RequiresPermissions("UIElement:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加界面元素")
    public String insertUIElement() {
        SDO params = this.getSDO();
        UIElement uiElement = params.toObject(UIElement.class);
        String id = this.uiElementApplication.saveUIElement(uiElement);
        return success(id);
    }

    @RequiresPermissions("UIElement:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改界面元素")
    public String updateUIElement() {
        SDO params = this.getSDO();        
        UIElement uiElement = params.toObject(UIElement.class);
        this.uiElementApplication.saveUIElement(uiElement);
        return success();
    }

    @RequiresPermissions("UIElement:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除界面元素")
    public String deleteUIElements() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.uiElementApplication.deleteUIElements(ids);
        return success();
    }

    @RequiresPermissions("UIElement:move")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.MOVE, description = "移动界面元素")
    public String moveUIElements() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        String folderId = params.getString("folderId");
        this.uiElementApplication.moveUIElements(ids, folderId);
        return success();
    }

    @RequiresPermissions("UIElement:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改界面元素状态")
    public String updateUIElementsStatus() {
        SDO params = this.getSDO();

        List<String> ids = params.getStringList("ids");
        Integer status = params.getInteger("status");

        this.uiElementApplication.updateUIElementsStatus(ids, status);
        return success();
    }

    @RequiresPermissions("UIElement:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改界面元素排序号")
    public String updateUIElementsSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap("data");
        this.uiElementApplication.updateUIElementsSequence(data);
        return success();
    }

    @RequiresPermissions("UIElement:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询界面元素")
    public String slicedQueryUIElements() {
        SDO params = this.getSDO();
        UIElementsQueryRequest queryRequest = params.toQueryRequest(UIElementsQueryRequest.class);
        Map<String, Object> data = this.uiElementApplication.slicedQueryUIElements(queryRequest);
        return this.toResult(data);
    }

}
