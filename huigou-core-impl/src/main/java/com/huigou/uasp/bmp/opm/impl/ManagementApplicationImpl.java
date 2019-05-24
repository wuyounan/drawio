package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.opm.application.ManagementApplication;
import com.huigou.uasp.bmp.opm.application.PermissionBuilder;
import com.huigou.uasp.bmp.opm.domain.model.management.BaseManagementType;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagement;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.query.BizManagementTypesQueryRequest;
import com.huigou.uasp.bmp.opm.repository.managment.BaseManagementTypeRepository;
import com.huigou.uasp.bmp.opm.repository.managment.BizManagementRepository;
import com.huigou.uasp.bmp.opm.repository.managment.BizManagementTypeRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.util.StringUtil;

public class ManagementApplicationImpl extends BaseApplication implements ManagementApplication {

    private BaseManagementTypeRepository baseManagementTypeRepository;

    private BizManagementTypeRepository bizManagementTypeRepository;

    private BizManagementRepository bizManagementRepository;

    private OrgRepository orgRepository;

    private PermissionBuilder permissionBuilder;

    public void setBaseManagementTypeRepository(BaseManagementTypeRepository baseManagementTypeRepository) {
        this.baseManagementTypeRepository = baseManagementTypeRepository;
    }

    public void setBizManagementTypeRepository(BizManagementTypeRepository bizManagementTypeRepository) {
        this.bizManagementTypeRepository = bizManagementTypeRepository;
    }

    public void setBizManagementRepository(BizManagementRepository bizManagementRepository) {
        this.bizManagementRepository = bizManagementRepository;
    }

    public void setOrgRepository(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public void setPermissionBuilder(PermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    @Override
    public String saveBaseManagementType(BaseManagementType baseManagementType, String bizManagementTypeId) {
        Assert.notNull(baseManagementType, "参数baseManagementType不能为空。");

        baseManagementType = this.commonDomainService.loadAndFillinProperties(baseManagementType, BaseManagementType.class);

        if (StringUtil.isNotBlank(bizManagementTypeId)) {
            BizManagementType bizManagementType = this.loadBizManagementType(bizManagementTypeId);
            Assert.notNull(bizManagementType, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, bizManagementTypeId, "业务管理权限类别"));
            baseManagementType.setBizManagementType(bizManagementType);
        }

        baseManagementType = (BaseManagementType) this.commonDomainService.saveBaseInfoWithFolderEntity(baseManagementType, baseManagementTypeRepository);
        return baseManagementType.getId();
    }

    @Override
    public void deleteBaseManagementTypes(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        List<BaseManagementType> baseManagementTypes = this.baseManagementTypeRepository.findAll(ids);
        Assert.isTrue(ids.size() == baseManagementTypes.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "基础管理权限类别"));

        this.baseManagementTypeRepository.delete(baseManagementTypes);
    }

    @Override
    public Integer getBaseManagementTypeNextSequence(String folderId) {
        return this.commonDomainService.getNextSequence(BaseManagementType.class, CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
    }

    @Override
    public void updateBaseManagementTypeSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(BaseManagementType.class, params);
    }

    @Override
    public void moveBaseManagementTypes(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(BaseManagementType.class, ids, folderId);
    }

    @Override
    public BaseManagementType loadBaseManagementType(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.baseManagementTypeRepository.findOne(id);
    }

    @Override
    public Map<String, Object> slicedQueryBaseManagementTypes(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "baseManagementType");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public String saveBizManagementType(BizManagementType bizManagementType) {
        BizManagementType dbBizManagementType;
        String oldName = null;
        if (bizManagementType.isNew()) {
            dbBizManagementType = bizManagementType;
        } else {
            dbBizManagementType = this.loadBizManagementType(bizManagementType.getId());
            Assert.notNull(dbBizManagementType, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, dbBizManagementType.getId(),
                                                                                dbBizManagementType.getClass().getName()));
            oldName = dbBizManagementType.getName();
            dbBizManagementType.fromEntity(bizManagementType);
        }

        dbBizManagementType = (BizManagementType) this.commonDomainService.saveTreeEntity(dbBizManagementType, bizManagementTypeRepository, oldName);
        return dbBizManagementType.getId();
    }

    @Override
    public void deleteBizManagementTypes(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        List<BizManagementType> bizManagementTypes = this.bizManagementTypeRepository.findAll(ids);
        Assert.isTrue(ids.size() == bizManagementTypes.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "业务管理权限类别"));

        BaseManagementType baseManagementType;
        long childrenCount;
        for (BizManagementType bizManagementType : bizManagementTypes) {
            childrenCount = this.bizManagementTypeRepository.countByParentId(bizManagementType.getId());
            Assert.isTrue(childrenCount == 0, MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, bizManagementType.getName()));

            baseManagementType = this.baseManagementTypeRepository.findFirstByBizManagementType(bizManagementType);
            if (baseManagementType != null) {
                Assert.isTrue(false,
                              MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, bizManagementType.getName(),
                                                              baseManagementType.getName()));
            }
        }

        this.bizManagementTypeRepository.delete(bizManagementTypes);
    }

    @Override
    public Integer getBizManagementTypeNextSequence(String parentId) {
        return this.commonDomainService.getNextSequence(BizManagementType.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    @Override
    public void updateBizManagementTypeSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(BizManagementType.class, params);

    }

    @Override
    public void moveBizManagementTypes(List<String> ids, String parentId) {
        this.commonDomainService.moveForTree(BizManagementType.class, ids, CommonDomainConstants.PARENT_ID_COLUMN_NAME, parentId);
    }

    @Override
    public BizManagementType loadBizManagementType(String id) {
        return this.bizManagementTypeRepository.findOne(id);
    }

    @Override
    public List<Map<String, Object>> queryBizManagementTypes(String parentId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizManagementType");
        return sqlExecutorDao.queryToListMap(queryDescriptor.getSqlByName("queryBizManagementType"), parentId);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementTypes(ParentAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizManagementType");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementTypes(BizManagementTypesQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizManagementType");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    private void internalAllocateBizManagement(List<String> managerIds, String manageTypeId, List<String> subordinationIds) {
        List<Org> managers = this.orgRepository.findAll(managerIds);
        Assert.isTrue(managers.size() == managerIds.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "管理者"));

        BizManagementType bizManagementType = this.bizManagementTypeRepository.findOne(manageTypeId);
        Assert.notNull(bizManagementType, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, manageTypeId, "业务管理权限类型"));

        List<Org> subordinations = this.orgRepository.findAll(subordinationIds);
        Assert.isTrue(subordinations.size() == subordinationIds.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "下属"));

        List<BizManagement> bizManagements = new ArrayList<BizManagement>(managerIds.size());

        List<BizManagement> allocatedBizManagements = this.bizManagementRepository.findByManagerInAndBizManagementTypeAndSubordinationIn(managers,
                                                                                                                                         bizManagementType,
                                                                                                                                         subordinations);

        BizManagement bizManagement;
        boolean allocated;
        for (Org manager : managers) {
            for (Org subordination : subordinations) {
                allocated = false;
                for (BizManagement allocatedBizManagement : allocatedBizManagements) {
                    if (allocatedBizManagement.isAllocated(manager, bizManagementType, subordination)) {
                        allocated = true;
                        break;
                    }
                }
                if (!allocated) {
                    bizManagement = new BizManagement();

                    bizManagement.setManager(manager);
                    bizManagement.setBizManagementType(bizManagementType);
                    bizManagement.setSubordination(subordination);

                    bizManagements.add(bizManagement);
                }
            }
        }

        if (bizManagements.size() > 0) {
            this.bizManagementRepository.save(bizManagements);
        }
    }

    @Override
    public void allocateManagers(List<String> managerIds, String manageTypeId, String subordinationId) {
        Assert.notEmpty(managerIds, "参数manageTypeId不能为空。");
        Assert.hasText(manageTypeId, "参数manageTypeId不能为空。");
        Assert.hasText(subordinationId, "参数subordinationId不能为空。");

        List<String> subordinationIds = new ArrayList<String>(1);
        subordinationIds.add(subordinationId);

        internalAllocateBizManagement(managerIds, manageTypeId, subordinationIds);
    }

    @Override
    public void allocateSubordinations(String managerId, String manageTypeId, List<String> subordinationIds) {
        Assert.hasText(managerId, "参数managerId不能为空。");
        Assert.hasText(manageTypeId, "参数manageTypeId不能为空。");
        Assert.notEmpty(subordinationIds, "参数subordinationIds不能为空。");

        List<String> managerIds = new ArrayList<String>(1);
        managerIds.add(managerId);

        internalAllocateBizManagement(managerIds, manageTypeId, subordinationIds);
    }

    @Override
    public void deleteBizManagements(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));

        List<BizManagement> bizManagements = this.bizManagementRepository.findAll(ids);
        Assert.isTrue(bizManagements.size() == ids.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "业务管理权限"));

        this.bizManagementRepository.delete(bizManagements);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementsByManagerId(String managerId, String manageTypeId, EmptyQueryRequest queryRequest) {
        Assert.hasText(managerId, "参数managerId不能为空。");
        Assert.hasText(manageTypeId, "参数manageTypeId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "management");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryByManagerAndManageTypeId");
        queryModel.putParam("managerId", managerId);
        queryModel.putParam("manageTypeId", manageTypeId);
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);

    }

    @Override
    public Map<String, Object> slicedQueryBizManagementsBySubordinationId(String subordinationId, String manageTypeId, EmptyQueryRequest queryRequest) {
        Assert.hasText(subordinationId, "下属ID不能为空。");
        Assert.hasText(manageTypeId, "业务管理权限ID不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "management");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryBySubordinationAndManageTypeId");
        queryModel.putParam("subordinationId", subordinationId);
        queryModel.putParam("manageTypeId", manageTypeId);
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public Map<String, Object> slicedQueryOrgAllocatedBizManagementTypeForManager(String orgFullId, EmptyQueryRequest queryRequest) {
        Assert.hasText(orgFullId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "orgFullId"));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "management");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "selectOrgAllocatedBizManagementTypeForManager");
        queryModel.putParam("selectedFullId", orgFullId);
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public Map<String, Object> slicedQueryOrgAllocatedBizManagementTypeForSubordination(String orgFullId, EmptyQueryRequest queryRequest) {
        Assert.hasText(orgFullId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "orgFullId"));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "management");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "selectOrgAllocatedBizManagementTypeForSubordination");
        queryModel.putParam("selectedFullId", orgFullId);
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    private Map<String, Object> internalSlicedQueryBizManagement(String queryName, String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest) {
        Assert.hasText(orgFullId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "orgFullId"));
        Assert.hasText(manageTypeId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "manageTypeId"));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "management");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, queryName);
        queryModel.putParam("selectedFullId", orgFullId);
        queryModel.putParam("manageTypeId", manageTypeId);
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementForManager(String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest) {
        return internalSlicedQueryBizManagement("selectBizManagementForManager", orgFullId, manageTypeId, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryBizManagementForSubordination(String orgFullId, String manageTypeId, EmptyQueryRequest queryRequest) {
        return internalSlicedQueryBizManagement("selectBizManagementForSubordination", orgFullId, manageTypeId, queryRequest);
    }

    @Override
    public void removePermissionCache() {
        permissionBuilder.removeCache();
    }

    @Override
    public void quoteBizManagement(String sourceOrgId, String destOrgId) {
    }

}
