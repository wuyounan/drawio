package com.huigou.uasp.bmp.opm.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.ContextUtil;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroup;
import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroupDetail;
import com.huigou.uasp.bmp.opm.proxy.UserGroupApplicationProxy;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("userGroup")
public class UserGroupController extends CommonController {

    @Autowired
    private UserGroupApplicationProxy userGroupApplication;

    private static final String USER_GROUP_PAGE = "UserGroup";

    private static final String USER_GROUP_DETAIL_PAGE = "UserGroupDetail";

    @Override
    protected String getPagePath() {
        return "/system/opm/usergroup/";
    }

    @RequiresPermissions("UserGroupMge:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到用户分组列表页面")
    public String forwardUserGroup() {
        return forward(USER_GROUP_PAGE);
    }

    @RequiresPermissions("UserGroupMge:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加用户分组明细页面")
    public String showInsertUserGroup() {
        SDO params = this.getSDO();
        String folderId = params.getString("folderId");
        Integer sequence = userGroupApplication.getUserGroupNextSequence(folderId);
        this.putAttribute("sequence", sequence);
        return forward(USER_GROUP_DETAIL_PAGE);
    }

    @RequiresPermissions("UserGroupMge:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到修改用户分组明细页面")
    public String showUpdateUserGroup() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        UserGroup userGroup = userGroupApplication.loadUserGroup(id);
        return forward("UserGroupDetail", userGroup);
    }

    @RequiresPermissions("UserGroupMge:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加用户分组")
    public String insertUserGroup() {
        SDO params = this.getSDO();
        UserGroup userGroup = params.toObject(UserGroup.class);
        List<UserGroupDetail> details = params.getList("detailData", UserGroupDetail.class);
        userGroup.setGroupdetails(details);
        String id = userGroupApplication.saveUserGroup(userGroup);
        userGroupApplication.cleanUserGroupRepeatDetail(id);
        return success(id);
    }

    @RequiresPermissions("UserGroupMge:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改用户分组")
    public String updateUserGroup() {
        SDO params = this.getSDO();
        UserGroup userGroup = params.toObject(UserGroup.class);
        List<UserGroupDetail> inputDetails = params.getList("detailData", UserGroupDetail.class);
        userGroup.setGroupdetails(inputDetails);
        String id = userGroupApplication.saveUserGroup(userGroup);
        userGroupApplication.cleanUserGroupRepeatDetail(id);
        return success(id);
    }

    @RequiresPermissions("UserGroupMge:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除用户分组")
    public String deleteUserGroups() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        userGroupApplication.deleteUserGroups(ids);
        return success();
    }

    @RequiresPermissions("UserGroupMge:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改用户分组状态")
    public String updateUserGroupsStatus() {
        SDO params = this.getSDO();
        Integer status = params.getProperty("status", Integer.class);
        List<String> ids = params.getStringList("ids");
        userGroupApplication.updateUserGroupsStatus(ids, status);
        return success();
    }

    @RequiresPermissions("UserGroupMge:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改用户分组排序号")
    public String updateUserGroupsSequence() {
        Map<String, Integer> data = this.getSDO().getStringMap("data");
        userGroupApplication.updateUserGroupsSequence(data);
        return success();
    }

    @RequiresPermissions("UserGroupMge:move")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.MOVE, description = "移动用户分组")
    public String moveUserGroups() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        String folderId = params.getString("folderId");
        this.userGroupApplication.moveUserGroups(ids, folderId);
        return success();
    }

    @RequiresPermissions("UserGroupMge:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询用户分组")
    public String slicedQueryUserGroups() {
        SDO params = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> map = userGroupApplication.slicedQueryUserGroups(queryRequest);
        return toResult(map);
    }

    @RequiresPermissions("UserGroupMge:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询用户分组明细")
    public String slicedQueryUserGroupDetails() {
        SDO params = this.getSDO();
        String groupId = params.getString("groupId");
        CodeAndNameQueryRequest queryRequest = params.toQueryRequest(CodeAndNameQueryRequest.class);
        Map<String, Object> map = userGroupApplication.slicedQueryUserGroupDetails(groupId, queryRequest);
        return toResult(map);
    }

    @RequiresPermissions("UserGroupMge:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除用户分组明细")
    public String deleteUserGroupDetails() {
        SDO params = this.getSDO();
        String groupId = params.getString("groupId");
        List<String> ids = params.getStringList("ids");
        userGroupApplication.deleteUserGroupDetails(groupId, ids);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.QUERY, description = "查询可用的用户分组")
    public String queryAvailableUserGroups() {
        String personId = ContextUtil.getOperator().getUserId();
        List<Map<String, Object>> list = userGroupApplication.queryAvailableUserGroups(personId);
        return toResult(list);
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.QUERY, description = "查询用户自定义组明细")
    public String queryAvailableUserGroupDetails() {
        SDO params = this.getSDO();
        String groupId = params.getString("groupId");
        Map<String, Object> map = userGroupApplication.queryAvailableUserGroupDetails(groupId);
        return toResult(map);
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.QUERY, description = "查询用户自定义组")
    public String queryCustomUserGroups() {
        String personId = ContextUtil.getOperator().getUserId();
        List<Map<String, Object>> list = userGroupApplication.queryCustomUserGroups(personId);
        return toResult(list);
    }

}
