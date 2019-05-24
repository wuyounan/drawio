package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.management.BaseManagementType;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;
import com.huigou.uasp.bmp.opm.domain.query.BizManagementTypesQueryRequest;

/**
 * 业务管理权限
 * 
 * @author gongmm
 */
public interface ManagementApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 保存基础管理权限类别
     * 
     * @param baseManagementType
     *            基础管理权限类别
     * @param bizManagementTypeId
     *            业务管理权限 类别ID
     * @return 基础管理权限类别ID
     */
    String saveBaseManagementType(BaseManagementType baseManagementType, String bizManagementTypeId);

    /**
     * 删除基础管理权限类别
     * 
     * @param ids
     *            基础管理权限类别ID列表
     */
    void deleteBaseManagementTypes(List<String> ids);

    /**
     * 得到基础管理权限类别下一个排序号
     * 
     * @param folderId
     *            文件ID
     * @return
     */
    Integer getBaseManagementTypeNextSequence(String folderId);

    /**
     * 更新基础管理权限排序号
     * 
     * @param params
     *            包括基础权限ID和排序号的Map
     */
    void updateBaseManagementTypeSequence(Map<String, Integer> params);

    /**
     * 移动基础管理权限类别
     * 
     * @param ids
     *            基础管理权限类别ID列表
     * @param folderId
     *            文件夹ID
     */
    void moveBaseManagementTypes(List<String> ids, String folderId);

    /**
     * 从数据库中读取一条基础管理权限类别数据
     * 
     * @param id
     *            基础管理权限类别ID
     * @return 基础管理权限类
     */
    BaseManagementType loadBaseManagementType(String id);

    /**
     * 分页查询基础管理权限类别
     * 
     * @param queryRequest
     *            查询 对象
     * @return
     */
    Map<String, Object> slicedQueryBaseManagementTypes(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 保存业务管理权限类别
     * 
     * @param bizManagementType
     *            业务管理权限类别
     * @return
     */
    String saveBizManagementType(BizManagementType bizManagementType);

    /**
     * 删除业务管理权限类别
     * 
     * @param ids
     *            业务管理权限类别ID列表
     */
    void deleteBizManagementTypes(List<String> ids);

    /**
     * 得到业务管理权限下一个排序号
     * 
     * @return
     */
    Integer getBizManagementTypeNextSequence(String parentId);

    /**
     * 更新业务管理权限排序号
     * 
     * @param params
     *            业务管理权限ID和排序号MAP
     */
    void updateBizManagementTypeSequence(Map<String, Integer> params);

    /**
     * 移动业务管理权限类别
     * 
     * @param ids
     *            业务管理权限类别ID列表
     * @param parentId
     *            父ID
     */
    public void moveBizManagementTypes(List<String> ids, String parentId);

    /**
     * 从数据库中读取一条业务管理权限类别数据
     * 
     * @param id
     * @return 业务管理权限类别
     */
    BizManagementType loadBizManagementType(String id);

    /**
     * 查询业务权限类别
     * 
     * @param parentId
     *            父ID
     * @return
     */
    List<Map<String, Object>> queryBizManagementTypes(String parentId);

    /**
     * 分页查询业务权限类别
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryBizManagementTypes(ParentAndCodeAndNameQueryRequest queryRequest);

    /**
     * 分页查询业务权限类别
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryBizManagementTypes(BizManagementTypesQueryRequest queryRequest);

    /**
     * 分配管理者
     * 
     * @param managerIds
     *            管理者ID列表
     * @param manageTypeId
     *            业务权限类别ID
     * @param subordinationOrgId
     *            被管理组织ID
     * @return
     */
    void allocateManagers(List<String> managerIds, String manageTypeId, String subordinationId);

    /**
     * 分配下属
     * 
     * @param managerId
     *            管理者ID
     * @param manageTypeId
     *            业务管理权限类别ID
     * @param subordinationIds
     *            下属ID列表
     */
    void allocateSubordinations(String managerId, String manageTypeId, List<String> subordinationIds);

    /**
     * 删除业务管理权限
     * 
     * @param ids
     */
    void deleteBizManagements(List<String> ids);

    /**
     * 根据管理者ID分页查询业务管理权限
     * 
     * @param managerId
     *            管理者ID
     * @param manageTypeId
     *            业务管理权限ID
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryBizManagementsByManagerId(String managerId, String manageTypeId, EmptyQueryRequest queryRequest);

    /**
     * 根据下属ID分页查询业务管理权限
     * 
     * @param subordinationId
     *            下属ID
     * @param manageType
     *            业务管理权限ID
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryBizManagementsBySubordinationId(String subordinationId, String manageTypeId, EmptyQueryRequest queryRequest);

    /**
     * 根据组织ID分页查询业务管理权限
     * 
     * @param params
     * @return
     */
    // Page<BizManagement> slicedQueryBizManagementsByManageOrgId(String managerOrgId, Pageable pageable);

    /**
     * 分页查询管理者已分配的业务管理权限类别
     * 
     * @param orgFullId
     *            管理者ID全路径
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryOrgAllocatedBizManagementTypeForManager(String orgFullId, EmptyQueryRequest queryRequest);

    /**
     * 分页查询下属已分配的业务管理权限类别
     * 
     * @param orgFullId
     *            管理者ID全路径
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryOrgAllocatedBizManagementTypeForSubordination(String orgFullId, EmptyQueryRequest queryRequest);

    /**
     * 分页查询业务管理权限的下属
     * 
     * @param orgFullId
     *            管理者ID全路径
     * @param manageTypeId
     *            业务权限类别
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryBizManagementForManager(String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest);

    /**
     * 分页查询业务管理权限的 管理者
     * 
     * @param orgFullId
     *            下属ID全路径
     * @param manageTypeId
     *            业务权限类别
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryBizManagementForSubordination(String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest);

    /**
     * 清除权限缓存
     */
    void removePermissionCache();

    /**
     * 引用业务权限
     * 
     * @param sourceOrgId
     *            源组织ID
     * @param destOrgId
     *            目标组织ID
     */
    void quoteBizManagement(String sourceOrgId, String destOrgId);
}
