package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroup;

/**
 * 用户分组应用
 * 
 * @author gongmm
 */
public interface UserGroupApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 保存用户分组
     * 
     * @param userGroup
     *            用户分组
     * @return
     */
    String saveUserGroup(UserGroup userGroup);

    /**
     * 加载用户分组
     * 
     * @param id
     *            用户分组ID
     * @return
     */
    UserGroup loadUserGroup(String id);

    /**
     * 删除用户分组
     * 
     * @param ids
     *            用户分组ID列表
     */
    void deleteUserGroups(List<String> ids);

    /**
     * 更新用户分组状态
     * 
     * @param ids
     *            用户分组ID列表
     * @param status
     *            状态
     */
    void updateUserGroupsStatus(List<String> ids, Integer status);

    /**
     * 移动用户分组状态
     * 
     * @param ids
     *            用户分组ID列表
     * @param folderId
     *            文件ID
     */
    void moveUserGroups(List<String> ids, String folderId);

    /**
     * 更新用户分组排序号
     * 
     * @param params
     *            用户分组ID和排序号的Map
     */
    void updateUserGroupsSequence(Map<String, Integer> params);

    /**
     * 得到用户分组下一个排序ID
     * 
     * @param folderId
     *            文件夹ID
     * @return
     */
    Integer getUserGroupNextSequence(String folderId);

    /**
     * 分页查询用户分组
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryUserGroups(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 删除用户分组明细
     * 
     * @param groupId
     *            用户分组ID
     * @param ids
     *            用户分组明细ID列表
     */
    public void deleteUserGroupDetails(String groupId, List<String> ids);

    /**
     * 分页查询用户分组明细
     * 
     * @param groupId
     *            分组ID
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryUserGroupDetails(String groupId, CodeAndNameQueryRequest queryRequest);

    /**
     * @return
     */
    /**
     * 查询可用的用户分组
     * 
     * @param personId
     *            人员ID
     * @return
     */
    List<Map<String, Object>> queryAvailableUserGroups(String personId);

    /**
     * 查询用户自定义组明细
     * 
     * @param groupId
     *            分组ID
     * @return
     */
    Map<String, Object> queryAvailableUserGroupDetails(String groupId);

    /**
     * @param personId
     * @return
     */
    List<Map<String, Object>> queryCustomUserGroups(String personId);

}
