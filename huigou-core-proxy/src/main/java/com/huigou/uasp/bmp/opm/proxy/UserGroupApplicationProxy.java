package com.huigou.uasp.bmp.opm.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.configuration.application.CommonTreeApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTreeKind;
import com.huigou.uasp.bmp.opm.application.UserGroupApplication;
import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroup;
import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroupDetail;
import com.huigou.uasp.bmp.opm.impl.UserGroupApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.usergroup.UserGroupDetailRepository;
import com.huigou.uasp.bmp.opm.repository.usergroup.UserGroupRepository;
import com.huigou.util.StringUtil;

@Service("userGroupApplicationProxy")
public class UserGroupApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private CommonTreeApplication commonTreeApplication;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserGroupDetailRepository userGroupDetailRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private UserGroupApplication userGroupApplication;

    void initProperties(UserGroupApplicationImpl userGroupApplicationImpl) {
        userGroupApplicationImpl.setCommonDomainService(commonDomainService);
        userGroupApplicationImpl.setCommonTreeApplication(commonTreeApplication);
        userGroupApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        userGroupApplicationImpl.setUserGroupRepository(userGroupRepository);
    }

    private UserGroupApplication getUserGroupApplication() {
        if (userGroupApplication == null) {
            // synchronized (UserGroupApplicationProxy.class) {
            if (userGroupApplication == null) {
                UserGroupApplicationImpl userGroupApplicationImpl = coreApplicationFactory.getUserGroupApplication();
                userGroupApplication = userGroupApplicationImpl;
            }
            // }
        }
        return userGroupApplication;
    }

    @Transactional
    public String saveUserGroup(UserGroup userGroup) {
        Assert.notNull(userGroup, "参数userGroup不能为空。");
        List<UserGroupDetail> details = userGroup.getGroupdetails();
        userGroup = this.commonDomainService.loadAndFillinProperties(userGroup, UserGroup.class);
        if (StringUtil.isBlank(userGroup.getFolderId())) {
            CommonTree commonTree = commonTreeApplication.findByKindIdAndCode(CommonTreeKind.CUSTOM_GROUP, UserGroup.UserGroupKind.CUSTOM.name());
            userGroup.setFolderId(commonTree.getId());
        }
        userGroup = (UserGroup) this.commonDomainService.saveBaseInfoWithFolderEntity(userGroup, userGroupRepository);
        // 保存明细
        this.saveUserGroupDetail(userGroup.getId(), details);
        return userGroup.getId();
    }

    private void saveUserGroupDetail(String id, List<UserGroupDetail> details) {
        if (details != null && details.size() > 0) {
            List<UserGroupDetail> datas = new ArrayList<>();
            for (UserGroupDetail d : details) {
                if (d.isNew()) {
                    d.setGroupId(id);
                } else {
                    d = this.commonDomainService.loadAndFillinProperties(d, UserGroupDetail.class);
                }
                datas.add(d);
            }
            this.userGroupDetailRepository.save(datas);
        }
    }

    @Transactional
    public void cleanUserGroupRepeatDetail(String groupId) {
        Assert.hasText(groupId, "参数groupId不能为空。");
        UserGroup userGroup = this.userGroupRepository.findOne(groupId);
        Assert.notNull(userGroup, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, groupId, "用户分组"));
        List<UserGroupDetail> list = this.userGroupDetailRepository.findByGroupId(groupId);
        if (list != null && list.size() > 0) {
            Map<String, String> map = new HashMap<>();
            List<UserGroupDetail> details = new ArrayList<>();
            for (UserGroupDetail d : list) {
                if (map.containsKey(d.getOrgId())) {
                    details.add(d);
                } else {
                    map.put(d.getOrgId(), "1");
                }
            }
            if (details.size() > 0) {
                this.userGroupDetailRepository.delete(details);
            }
        }
    }

    public UserGroup loadUserGroup(String id) {
        return getUserGroupApplication().loadUserGroup(id);
    }

    @Transactional
    public void deleteUserGroups(List<String> ids) {
        for (String id : ids) {
            this.userGroupDetailRepository.deleteByGroupId(id);
        }
        getUserGroupApplication().deleteUserGroups(ids);
    }

    @Transactional
    public void updateUserGroupsStatus(List<String> ids, Integer status) {
        getUserGroupApplication().updateUserGroupsStatus(ids, status);

    }

    @Transactional
    public void moveUserGroups(List<String> ids, String folderId) {
        getUserGroupApplication().moveUserGroups(ids, folderId);

    }

    @Transactional
    public void updateUserGroupsSequence(Map<String, Integer> params) {
        getUserGroupApplication().updateUserGroupsSequence(params);

    }

    @Transactional
    public Integer getUserGroupNextSequence(String folderId) {
        return getUserGroupApplication().getUserGroupNextSequence(folderId);
    }

    public Map<String, Object> slicedQueryUserGroups(FolderAndCodeAndNameQueryRequest queryRequest) {
        return getUserGroupApplication().slicedQueryUserGroups(queryRequest);
    }

    @Transactional
    public void deleteUserGroupDetails(String groupId, List<String> ids) {
        List<UserGroupDetail> userGroupDetails = this.userGroupDetailRepository.findAll(ids);
        this.userGroupDetailRepository.delete(userGroupDetails);
    }

    public Map<String, Object> slicedQueryUserGroupDetails(String groupId, CodeAndNameQueryRequest queryRequest) {
        return getUserGroupApplication().slicedQueryUserGroupDetails(groupId, queryRequest);
    }

    public List<Map<String, Object>> queryAvailableUserGroups(String personId) {
        return getUserGroupApplication().queryAvailableUserGroups(personId);
    }

    public Map<String, Object> queryAvailableUserGroupDetails(String groupId) {
        return getUserGroupApplication().queryAvailableUserGroupDetails(groupId);
    }

    public List<Map<String, Object>> queryCustomUserGroups(String personId) {
        return getUserGroupApplication().queryCustomUserGroups(personId);
    }

}
