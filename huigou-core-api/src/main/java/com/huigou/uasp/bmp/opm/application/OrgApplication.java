package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgPropertyDefinition;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.domain.query.OrgPropertyDefinitionQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.OrgQueryModel;

public interface OrgApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    static final String TENANT_ENTITY_ROOT_ID_CACHE_KEY = "tenant.entity.rootId";

    static final String TENANT_INDUSTRY_ROOT_ID_CACHE_KEY = "tenant.industry.rootId";

    /**
     * 保存组织属性定义
     * 
     * @param orgPropertyDefinition
     *            组织属性定义实体
     * @return
     */
    String saveOrgPropertyDefinition(OrgPropertyDefinition orgPropertyDefinition);

    /**
     * 加载组织属性定义
     * 
     * @param id
     * @return
     */
    OrgPropertyDefinition loadOrgPropertyDefinition(String id);

    /**
     * 删除组织属性定义
     * 
     * @param ids
     *            组织属性定义ID列表
     */
    void deleteOrgPropertyDefinitions(List<String> ids);

    /**
     * 保存组织属性定义排序号
     * 
     * @param params
     *            组织属性定义ID和排序号组成的Map
     */
    void updateOrgPropertyDefinitionsSequence(Map<String, Integer> params);

    /**
     * 得到组织属性定义下一个排序号
     * 
     * @param orgKindId
     *            组织类别ID
     * @return
     */
    Integer getOrgPropertyDefinitionNextSequence(String orgKindId);

    /**
     * 查询组织属性定义
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryOrgPropertyDefinitions(OrgPropertyDefinitionQueryRequest queryRequest);

    /**
     * 从模板中添加组织
     * 
     * @param org
     *            组织机构
     * @param templateId
     *            组织模板ID
     */
    void insertOrgByTemplateId(Org org, String templateId);

    /**
     * 根据组织ID生成组织
     * 
     * @param orgId
     *            组织ID
     * @param tenant
     *            租户
     * @return 根组织
     */
    Org buildOrgStructureByOrgId(String orgId, Tenant tenant);

    /**
     * 生成默认组织
     * 
     * @param tenant
     *            租户
     * @return 根组织
     */
    Org buildDefaultOrgStructure(Tenant tenant);

    /**
     * 添加除人员成员外的各级组织节点
     * 
     * @param org
     * @return
     */
    String insertOrg(Org org, OrgType orgType);

    /**
     * 修改组织
     * 
     * @param org
     */
    void updateOrg(Org org);

    /**
     * 逻辑删除组织及其所有下级组织（将Org.Stauts设置为-1）。如果下级组织中有人员的"主要人员成员"，则同时逻辑删除人员（将Person.
     * Stauts设置为-1）和人员的其他"从属人员成员"。
     *
     * @param ids
     *            组织唯一标识列表
     */
    void logicDeleteOrg(List<String> ids);

    /**
     * 从数据库中彻底删除多个组织
     *
     * @param id
     *            组织唯一标识
     * @param isDeletePerson
     *            删除组织的时候，是否同时删除组织下的直属人员
     */
    void physicalDeleteOrg(String id, boolean isDeletePerson);

    /**
     * 还原一个组织及其所有下级组织（将Org.Status设置为-1）。如果下级组织中有人员的"主要人员成员"，则同时还原人员（将Person.
     * Status设置为-1）；由参数isEnableSubordinatePsm指明是否还原人员的其他"从属人员成员"。
     *
     * @param id
     *            组织唯一标识
     * @param isEnableSubordinatePsm
     *            是否还原人员的"从属人员成员"
     */
    void restoreOrg(String id, boolean isEnableSubordinatePsm);

    /**
     * 添加人员成员
     *
     * @param personIds
     *            人员ID数组
     * @param orgId
     *            组织ID
     * @param status
     *            状态
     * @param autoEnableOldPsm
     *            自动启用old人员成员
     */
    void insertPersonMembers(List<String> personIds, String orgId, ValidStatus status, Boolean autoEnableOldPsm);

    /**
     * 添加人员成员
     * 
     * @param person
     *            人员
     * @param position
     *            岗位
     * @param autoEnableOldPsm
     *            自动启用old人员成员
     * @return
     */
    String insertPersonMember(Person person, Org position, ValidStatus psmStatus, boolean autoEnableOldPsm);

    /**
     * 更新组织排序号
     * 
     * @param params
     */
    void updateOrgSequence(Map<String, String> params);

    /**
     * 启用一个组织及其所有下级组织（将Org.status设置为1）。如果下级组织中有人员的"主要人员成员"，则同时启用人员（将Person.
     * status设置为1）；由参数isEnableSubordinatePsm指明是否启用人员的其他"从属人员成员"。
     *
     * @param id
     *            组织ID
     * @param isEnableSubordinatePsm
     */
    void enableOrg(String id, Boolean isEnableSubordinatePsm);

    /**
     * 启用附属人员成员
     * 
     * @param orgId
     * @param personId
     */
    void enableSubordinatePsm(String orgId, String personId);

    /**
     * 禁用一个组织及其所有下级组织（将Org.status设置为0）。如果下级组织中有人员的"主要人员成员"，则同时禁用人员（将Person.
     * status设置为0）和人员的其他"从属人员成员"。
     * 
     * @param id
     *            组织ID
     */
    void disableOrg(String id);

    /**
     * 分配人员
     *
     * @param ids
     *            人员ID数组
     * @param orgId
     *            组织ID（岗位id）
     */
    void assignPerson(List<String> ids, String orgId);

    /**
     * 从数据库读取一个组织数据
     *
     * @param id
     *            组织ID
     */
    Org loadOrg(String id);

    /**
     * 查找有效的组织
     * 
     * @param id
     *            组织ID
     * @return
     */
    Org loadEabledOrg(String id);

    /**
     * 从数据库读取多个组织数据
     *
     * @param params
     * @return
     */
    Map<String, Object> queryOrgs(OrgQueryModel parameter);

    /**
     * 查询组织节点下的所有人员成员
     * 
     * @param orgId
     *            组织ID
     * @return
     */
    List<Org> queryAllPersonMembersByOrgId(String orgId);

    /**
     * 从数据库分页读取多个组织数据
     *
     * @param params
     * @return
     */
    Map<String, Object> slicedQueryOrgs(OrgQueryModel parameter);

    /**
     * 得到组织下一个排序号
     *
     * @param parentId
     *            父节点ID
     * @return
     */
    String getOrgNextSequence(String parentId);

    /**
     * 根据fullName从数据库读取一个组织数据
     * 
     * @param fullName
     *            组织名称全路径
     * @return
     */
    Org loadOrgByFullName(String fullName);

    /**
     * 根据fullId从数据库读取一个组织数据
     *
     * @param fullId
     *            组织ID全路径
     * @return 组织对象
     */
    Org loadOrgByFullId(String fullId);

    /**
     * 通过人员成员ID查找主组织
     * 
     * @param personMemberId
     *            人员成员ID
     * @return
     */
    Org loadMainOrgByPersonMemberId(String personMemberId);

    /**
     * 通过人员ID查找主组织
     * 
     * @param personId
     * @return
     */
    Org loadMainOrgByPersonId(String personId);

    /**
     * 通过姓名查找主组织
     *
     * @param name
     *            名称
     * @return 组织对象
     */
    Org loadMainOrgByPersonName(String name);

    /**
     * 通过登录名查找主组织
     *
     * @param loginName
     *            登录名
     * @return 组织对象
     */
    Org loadMainOrgByLoginName(String loginName);

    /**
     * 通过登录名称查找组织
     *
     * @param loginName
     *            登录名
     * @return
     */
    List<Org> loadOrgListByLoginName(String loginName);

    /**
     * 设置指定的"人员成员"成为人员的"主要人员成员"
     *
     * @param id
     *            人员唯一标识
     * @param personMemberId
     *            "人员成员"的唯一标识
     * @param isDisableOldMasterPsm
     *            是否禁用原"主要人员成员"
     */
    void changePersonMainOrg(String id, String personMemberId, boolean isDisableOldMasterPsm);

    /**
     * 查询人员的人员成员
     *
     * @param personId
     *            人员ID
     * @return
     */
    List<Org> queryPersonMembersByPersonId(String personId);

    /**
     * 引用权限
     *
     * @param sourceOrgId
     *            源组织ID
     * @param destOrgId
     *            目标组织ID
     */
    void quoteAuthorizationAndBizManagement(String sourceOrgId, String destOrgId);

    /**
     * 添加人员
     * 
     * @param person
     * @return
     */
    String insertPerson(Person person);

    /**
     * 更新人员
     *
     * @param params
     */
    void updatePerson(Person person);

    /**
     * 人员简单信息修改
     * 
     * @param person
     */
    void updatePersonSimple(Person person);

    /**
     * 逻辑删除一个人员及其所有的人员成员
     *
     * @param id
     *            人员唯一标识
     */
    void logicDeletePerson(String id);

    /**
     * 从数据库彻底删除一个人员及其所有人员成员，并且删除相关的权限、业务管理、分级管理、代理等组织数据。
     *
     * @param id
     *            人员唯一标识
     */
    void physicalDeletePerson(String id);

    /**
     * 从数据库加载一个人员的数据
     *
     * @param id
     *            人员ID
     * @return
     */
    Person loadPerson(String id);

    /**
     * 检查人员是否启用状态
     * 
     * @param personId
     *            人员ID
     */
    void checkPersonIsEnabled(String personId);

    /**
     * 根据身份证号加载人员
     * 
     * @param certificateNo
     * @return
     */
    Person loadPersonByIdCard(String certificateNo);

    /**
     * 根据登录名加载人员
     * 
     * @param loginName
     *            登录名
     * @return
     */
    Person loadPersonByLoginName(String loginName);

    /**
     * 根CA编码加载人员
     * 
     * @param caNo
     *            CA编码
     * @return
     */
    Person loadPersonByCaNo(String caNo);

    /**
     * 启用一个人员
     *
     * @param id
     *            人员唯一标识
     * @param isEnableSubordinatePsm
     *            是否启用"禁用"的"从属人员成员"
     */
    void enablePerson(String id, boolean isEnableSubordinatePsm);

    /**
     * 禁用人员
     * <p>
     * 禁用人员及其所有的"启用"的人员成员
     *
     * @param id
     *            人员唯一标识
     */
    void disablePerson(String id);

    /**
     * 修改密码
     * 
     * @param oldPassword
     *            源密码
     * @param newPassword
     *            新密码
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 初始化密码
     * 
     * @param personId
     *            人员ID
     */
    void initPassword(String personId);

    /**
     * 分页查询人员
     * 
     * @param parentIdOrFullId
     *            父ID或父ID全路径
     * @param showDisabled
     *            显示禁用
     * @param showAllChildren
     *            显示所有下级
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryPerson(String parentIdOrFullId, boolean showDisabled, boolean showAllChildren, CodeAndNameQueryRequest queryRequest);

    /**
     * 调整人员组织架构
     *
     * @param personMemberId
     *            人员成员ID
     * @param positionId
     *            岗位ID
     * @param isDisableOldPsm
     *            是否禁用原人员成员
     * @param isUpdateMainPosition
     *            是否更新主岗位
     * @return 新的人员成员id
     */
    String adjustPersonOrgStructure(String personMemberId, String positionId, boolean isDisableOldPsm, boolean isUpdateMainPosition);

    /**
     * 查询组织属性
     * 
     * @param orgId
     *            组织ID
     * @return
     */
    Map<String, Object> queryOrgProperties(String orgId);
}
