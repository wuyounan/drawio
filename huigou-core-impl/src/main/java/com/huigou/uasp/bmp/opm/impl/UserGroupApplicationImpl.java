package com.huigou.uasp.bmp.opm.impl;

import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.configuration.application.CommonTreeApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTreeKind;
import com.huigou.uasp.bmp.opm.application.UserGroupApplication;
import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroup;
import com.huigou.uasp.bmp.opm.repository.usergroup.UserGroupRepository;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

public class UserGroupApplicationImpl extends BaseApplication implements UserGroupApplication {

    private UserGroupRepository userGroupRepository;

    private CommonTreeApplication commonTreeApplication;

    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public void setCommonTreeApplication(CommonTreeApplication commonTreeApplication) {
        this.commonTreeApplication = commonTreeApplication;
    }

    @Override
    public String saveUserGroup(UserGroup userGroup) {
        Assert.notNull(userGroup, "参数userGroup不能为空。");
        userGroup = this.commonDomainService.loadAndFillinProperties(userGroup, UserGroup.class);
        if (StringUtil.isBlank(userGroup.getFolderId())) {
            CommonTree commonTree = commonTreeApplication.findByKindIdAndCode(CommonTreeKind.CUSTOM_GROUP, UserGroup.UserGroupKind.CUSTOM.name());
            userGroup.setFolderId(commonTree.getId());
        }
        userGroup.buildDetails();
        userGroup = (UserGroup) this.commonDomainService.saveBaseInfoWithFolderEntity(userGroup, userGroupRepository);
        return userGroup.getId();
    }

    @Override
    public UserGroup loadUserGroup(String id) {
        Assert.notNull(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.userGroupRepository.findOne(id);
    }

    @Override
    public void deleteUserGroups(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        List<UserGroup> userGroups = this.userGroupRepository.findAll(ids);
        Assert.isTrue(userGroups.size() == ids.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "用户分组"));

        this.userGroupRepository.delete(userGroups);
    }

    @Override
    public void updateUserGroupsStatus(List<String> ids, Integer status) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        Assert.notNull(status, "参数status不能为空。");
        this.commonDomainService.updateStatus(UserGroup.class, ids, status);
    }

    @Override
    public void moveUserGroups(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(UserGroup.class, ids, folderId);
    }

    @Override
    public void updateUserGroupsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(UserGroup.class, params);
    }

    @Override
    public Integer getUserGroupNextSequence(String folderId) {
        return this.commonDomainService.getNextSequence(UserGroup.class, CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
    }

    @Override
    public Map<String, Object> slicedQueryUserGroups(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "userGroup");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public void deleteUserGroupDetails(String groupId, List<String> ids) {
        Assert.hasText(groupId, "参数groupId不能为空。");
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        UserGroup userGroup = this.userGroupRepository.findOne(groupId);
        Assert.notNull(userGroup, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, groupId, "用户分组"));

        userGroup.removeDetails(ids);

        this.userGroupRepository.save(userGroup);
    }

    @Override
    public Map<String, Object> slicedQueryUserGroupDetails(String groupId, CodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "systemGroupDetail");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        queryModel.putParam("groupId", groupId);
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public List<Map<String, Object>> queryAvailableUserGroups(String personId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "userGroup");
        String sql = queryDescriptor.getSqlByName("queryAvailableUserGroup");
        return this.sqlExecutorDao.queryToListMap(sql, personId + "%");
    }

    @Override
    public Map<String, Object> queryAvailableUserGroupDetails(String groupId) {
        Assert.hasText(groupId, "参数groupId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "userGroup");
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(queryDescriptor.getSqlByName("queryAvailableUserGroupDetail"));
        queryModel.putParam("groupId", groupId);
        queryModel.setOrderBy("sequence");
        return this.sqlExecutorDao.executeQuery(queryModel);

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryCustomUserGroups(String personId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "userGroup");
        String sql = queryDescriptor.getSqlByName("queryUserCustomGroup");
        List<Map<String, Object>> groups = this.sqlExecutorDao.queryToListMap(sql, personId + "@%");
        for (Map<String, Object> m : groups) {
            String groupId = ClassHelper.convert(m.get("id"), String.class);
            Map<String, Object> data = queryAvailableUserGroupDetails(groupId);
            m.put("details", (List<Map<String, Object>>) data.get(Constants.ROWS));
        }
        return groups;
    }

}
