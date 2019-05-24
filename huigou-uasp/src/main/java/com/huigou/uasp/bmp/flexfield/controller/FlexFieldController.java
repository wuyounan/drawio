package com.huigou.uasp.bmp.flexfield.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.flexfield.application.FlexFieldApplication;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroup;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroupField;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldDefinition;
import com.huigou.uasp.bmp.flexfield.domain.query.FlexFieldBizGroupsQueryRequest;
import com.huigou.uasp.bmp.flexfield.domain.query.FlexFieldDefinitionsQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 弹性域
 *
 * @author gongmm
 */
@Controller
@ControllerMapping("flexField")
public class FlexFieldController extends CommonController {

    private static final String FLEX_FIELD_DEFINE_LIST_PAGE = "flexFieldDefineList";

    private static final String FLEX_FIELD_DEFINE_DETAIL_PAGE = "flexFieldDefineEdit";

    private static final String FLEX_FIELD_GROUP_LIST_PAGE = "flexFieldGroupList";

    private static final String FLEX_FIELD_GROUP_DETAIL_PAGE = "flexFieldGroupEdit";

    @Autowired
    private FlexFieldApplication flexFieldApplication;

    protected String getPagePath() {
        return "/system/flexfield/";
    }

    @RequiresPermissions("FlexFieldDefine:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到弹性域定义列表页面")
    public String executeFlexFieldDefine() {
        return forward(FLEX_FIELD_DEFINE_LIST_PAGE);
    }

    @RequiresPermissions("FlexFieldDefine:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加弹性域定义明细页面")
    public String showInsertFlexFieldDefinition() {
        return forward(FLEX_FIELD_DEFINE_DETAIL_PAGE);
    }

    @RequiresPermissions("FlexFieldDefine:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加弹性域定义")
    public String insertFlexFieldDefinition() {
        SDO params = this.getSDO();
        FlexFieldDefinition flexFieldDefinition = params.toObject(FlexFieldDefinition.class);
        String id = this.flexFieldApplication.saveFlexFieldDefinition(flexFieldDefinition);
        return success(id);
    }

    @RequiresPermissions("FlexFieldDefine:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改弹性域定义明细页面")
    public String loadFlexFieldDefinition() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        FlexFieldDefinition flexFieldDefinition = this.flexFieldApplication.loadFlexFieldDefinition(id);
        return forward("flexFieldDefineEdit", flexFieldDefinition);
    }

    @RequiresPermissions("FlexFieldDefine:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改弹性域定义")
    public String updateFlexFieldDefinition() {
        SDO params = this.getSDO();
        FlexFieldDefinition flexFieldDefinition = params.toObject(FlexFieldDefinition.class);
        this.flexFieldApplication.saveFlexFieldDefinition(flexFieldDefinition);

        return success();
    }

    @RequiresPermissions("FlexFieldDefine:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除弹性域定义")
    public String deleteFlexFieldDefinitions() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.flexFieldApplication.deleteFlexFieldDefinitions(ids);

        return success();
    }

    @RequiresPermissions("FlexFieldDefine:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动弹性域定义")
    public String moveFlexFieldDefinitions() {
        SDO params = this.getSDO();

        String folderId = params.getString("folderId");
        List<String> ids = params.getStringList("ids");
        flexFieldApplication.moveFlexFieldDefinitions(ids, folderId);
        return success();

    }

    @RequiresPermissions("FlexFieldDefine:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询弹性域定义")
    public String slicedQueryFlexFieldDefinitions() {
        SDO params = this.getSDO();
        FlexFieldDefinitionsQueryRequest queryRequest = params.toQueryRequest(FlexFieldDefinitionsQueryRequest.class);
        Map<String, Object> data = this.flexFieldApplication.slicedQueryFlexFieldDefinitions(queryRequest);
        return toResult(data);

    }

    @RequiresPermissions("FlexFieldGroup:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到弹性域业务分组列表页面")
    public String executeFlexFieldGroup() {
        return forward(FLEX_FIELD_GROUP_LIST_PAGE);
    }

    @RequiresPermissions("FlexFieldGroup:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加弹性域业务分组明细页面")
    public String showInsertFlexFieldBizGroup() {
        return forward(FLEX_FIELD_GROUP_DETAIL_PAGE, this.getSDO());
    }

    @RequiresPermissions("FlexFieldGroup:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加弹性域业务分组")
    public String insertFlexFieldBizGroup() {
        SDO params = this.getSDO();
        FlexFieldBizGroup flexFieldBizGroup = params.toObject(FlexFieldBizGroup.class);
        String id = this.flexFieldApplication.saveFlexFieldBizGroup(flexFieldBizGroup);
        return success(id);

    }

    @RequiresPermissions("FlexFieldGroup:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改弹性域业务分组明细页面")
    public String loadFlexFieldBizGroup() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        FlexFieldBizGroup flexFieldBizGroup = this.flexFieldApplication.loadFlexFieldBizGroup(id);
        return forward("flexFieldGroupEdit", flexFieldBizGroup);

    }

    @RequiresPermissions("FlexFieldGroup:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改弹性域业务分组")
    public String updateFlexFieldBizGroup() {
        SDO params = this.getSDO();
        FlexFieldBizGroup flexFieldBizGroup = params.toObject(FlexFieldBizGroup.class);
        List<FlexFieldBizGroupField> detailData = params.getList("details", FlexFieldBizGroupField.class);
        flexFieldBizGroup.setDetailData(detailData);
        this.flexFieldApplication.saveFlexFieldBizGroup(flexFieldBizGroup);

        return success();
    }

    @RequiresPermissions("FlexFieldGroup:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除弹性域业务分组")
    public String deleteFlexFieldBizGroups() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.flexFieldApplication.deleteFlexFieldBizGroups(ids);
        return success();

    }

    @RequiresPermissions("FlexFieldGroup:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动弹性域业务分组")
    public String moveFlexFieldBizGroups() {
        SDO params = this.getSDO();
        String folderId = params.getString("folderId");
        List<String> ids = params.getStringList("ids");
        this.flexFieldApplication.moveFlexFieldBizGroups(ids, folderId);
        return success();

    }

    @RequiresPermissions("FlexFieldGroup:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改弹性域业务分组排序号")
    public String updateFlexFieldBizGroupSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap("data");
        this.flexFieldApplication.updateFlexFieldBizGroupSequence(data);
        return success();

    }

    @RequiresPermissions("FlexFieldGroup:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "复制弹性域业务分组")
    public String copyFlexFieldBizGroups() {
        SDO params = this.getSDO();
        String id = params.getId();
        String copyId = this.flexFieldApplication.copyFlexFieldBizGroups(id);
        return success(copyId);

    }

    @RequiresPermissions("FlexFieldGroup:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "分页查询弹性域业务分组")
    public String slicedQueryFlexFieldBizGroups() {
        SDO params = this.getSDO();
        FlexFieldBizGroupsQueryRequest queryRequest = params.toQueryRequest(FlexFieldBizGroupsQueryRequest.class);
        Map<String, Object> data = this.flexFieldApplication.slicedQueryFlexFieldBizGroups(queryRequest);
        return toResult(data);

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存弹性域业务分组字段")
    public String saveFlexFieldBizGroupFields() {
        SDO params = this.getSDO();
        String flexFieldBizGroupId = params.getId();
        List<String> flexFieldDefinitionIds = params.getStringList("defineIds");
        this.flexFieldApplication.saveFlexFieldBizGroupFields(flexFieldBizGroupId, flexFieldDefinitionIds);
        return success();

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除弹性域业务分组字段")
    public String deleteFlexFieldBizGroupFields() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        String flexFieldBizGroupId = params.getString("id");
        this.flexFieldApplication.deleteFlexFieldBizGroupFields(flexFieldBizGroupId, ids);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询弹性域业务分组字段")
    public String slicedQueryFlexFieldBizGroupFields() {
        SDO params = this.getSDO();
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        Map<String, Object> data = this.flexFieldApplication.slicedQueryFlexFieldBizGroupFields(queryRequest);
        return toResult(data);

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询弹性域业务分组字段")
    public String queryFlexFieldBizGroupFieldStorage() {
        SDO params = this.getSDO();
        String bizCode = params.getString("bizCode");
        String bizId = params.getString("bizId");
        List<Map<String, Object>> list = flexFieldApplication.queryFlexFieldBizGroupFieldStorage(bizCode, bizId);
        return toResult(list);
    }

}
