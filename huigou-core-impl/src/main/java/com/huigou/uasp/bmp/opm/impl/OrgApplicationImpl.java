package com.huigou.uasp.bmp.opm.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.CheckBaseInfoDuplicateParameter;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.jdbc.JDBCDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.LicenseChecker;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.SelectOrgScope;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.application.PermissionBuilder;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgNodeData;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgProperty;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgPropertyDefinition;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType.OrgKind;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.domain.query.OrgDesc;
import com.huigou.uasp.bmp.opm.domain.query.OrgPropertyDefinitionQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.OrgQueryModel;
import com.huigou.uasp.bmp.opm.repository.org.OrgPropertyDefinitionRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTemplateRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTypeRepository;
import com.huigou.uasp.bmp.opm.repository.org.PersonRepository;
import com.huigou.uasp.bmp.opm.repository.org.RoleRepository;
import com.huigou.uasp.bmp.securitypolicy.application.SecurityPolicyApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount.PersonAccountStatus;
import com.huigou.uasp.bmp.securitypolicy.domain.model.SecurityPolicy;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.Constants;
import com.huigou.util.Md5Builder;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

public class OrgApplicationImpl extends BaseApplication implements OrgApplication {

    private OrgPropertyDefinitionRepository orgPropertyDefinitionRepository;

    private OrgRepository orgRepository;

    private PersonRepository personRepository;

    private OrgTemplateRepository orgTemplateRepository;

    private OrgTypeRepository orgTypeRepository;

    private PermissionBuilder permissionBuilder;

    private SecurityPolicyApplication securityPolicyApplication;

    private RoleRepository roleRepository;

    public void setOrgPropertyDefinitionRepository(OrgPropertyDefinitionRepository orgPropertyDefinitionRepository) {
        this.orgPropertyDefinitionRepository = orgPropertyDefinitionRepository;
    }

    public void setOrgRepository(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void setOrgTemplateRepository(OrgTemplateRepository orgTemplateRepository) {
        this.orgTemplateRepository = orgTemplateRepository;
    }

    public void setOrgTypeRepository(OrgTypeRepository orgTypeRepository) {
        this.orgTypeRepository = orgTypeRepository;
    }

    public void setPermissionBuilder(PermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    public void setSecurityPolicyApplication(SecurityPolicyApplication securityPolicyApplication) {
        this.securityPolicyApplication = securityPolicyApplication;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "org");
        return queryDescriptor.getSqlByName(name);
    }

    private LicenseChecker getLicenseChecker() {
        return StandardLicenseChecker.getInstance();
    }

    @Override
    public String saveOrgPropertyDefinition(OrgPropertyDefinition orgPropertyDefinition) {
        Assert.notNull(orgPropertyDefinition, "参数orgPropertyDefinition不能为空。");

        Assert.hasText(orgPropertyDefinition.getOrgKindId(), "组织类别不能为空。");
        Assert.hasText(orgPropertyDefinition.getName(), "属性名称不能为空。");

        OrgPropertyDefinition other = orgPropertyDefinitionRepository.findFirstByOrgKindIdAndName(orgPropertyDefinition.getOrgKindId(),
                                                                                                  orgPropertyDefinition.getName());

        if (other != null && !other.equals(orgPropertyDefinition)) {
            throw new ApplicationException("属性名重复，不能保存。");
        }

        orgPropertyDefinition = orgPropertyDefinitionRepository.save(orgPropertyDefinition);

        return orgPropertyDefinition.getId();
    }

    @Override
    public OrgPropertyDefinition loadOrgPropertyDefinition(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.orgPropertyDefinitionRepository.findOne(id);
    }

    @Override
    public void deleteOrgPropertyDefinitions(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        List<OrgPropertyDefinition> orgPropertyDefinitions = orgPropertyDefinitionRepository.findAll(ids);
        Assert.isTrue(ids.size() == orgPropertyDefinitions.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "组织机构属性定义"));

        Org org;
        for (OrgPropertyDefinition orgPropertyDefinition : orgPropertyDefinitions) {
            org = this.orgRepository.findFirstByOrgProperties_PropertyDefinitionId(orgPropertyDefinition.getId());
            if (org != null) {
                Assert.isTrue(false, MessageSourceContext.getMessage(MessageConstants.OBJECT_REFERENCED_BY_WHO, orgPropertyDefinition.getName(), org.getName()));
            }
        }
        this.orgPropertyDefinitionRepository.delete(orgPropertyDefinitions);
    }

    @Override
    public void updateOrgPropertyDefinitionsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(OrgPropertyDefinition.class, params);
    }

    @Override
    public Integer getOrgPropertyDefinitionNextSequence(String orgKindId) {
        return this.commonDomainService.getNextSequence(OrgPropertyDefinition.class, "orgKindId", orgKindId);
    }

    @Override
    public Map<String, Object> queryOrgPropertyDefinitions(OrgPropertyDefinitionQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "orgPropertyDefinition");
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }

    /**
     * 更新组织状态
     *
     * @param id
     *            组织ID
     * @param version
     *            版本号
     * @param fromStatuses
     *            前驱状态
     * @param toStatus
     *            修改组织状态
     * @param operateKind
     *            操作类型
     * @param isEnableSubordinatePsm
     *            是否更新从属人员状态
     */
    private void updateOrgStatus(String id, Collection<ValidStatus> fromStatuses, ValidStatus toStatus, String operateKind, boolean isEnableSubordinatePsm) {
        // 1、验证是否存在组织
        // 2、验证版本号
        // 3、验证状态
        // 4、启用人员成员 需要验证人员的状态
        // 5、更新人员状态
        // 6、更新从属人员成员状态
        // 7、更新子节点状态
        isEnableSubordinatePsm = (isEnableSubordinatePsm) || (toStatus.getId() < (fromStatuses.iterator().next().getId()));

        Org org = this.orgRepository.findOne(id);

        Util.check(org != null, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "组织"));

        if (!org.isRootChild()) {
            Integer oldStatus = org.getStatus();
            org.setStatus(toStatus.getId());

            Org parent = this.orgRepository.findOne(org.getParentId());
            Util.check(parent != null, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, org.getParentId(), "组织"));
            org.checkStatusRule(parent, operateKind);

            org.setStatus(oldStatus);
        }

        // 4、启用人员成员 需要验证人员的状态
        if (OrgNodeKind.PSM.equals(org.getOrgKind())) {
            if (!org.getParentId().equalsIgnoreCase(org.getPerson().getMainOrgId())) {
                Util.check(toStatus.getId() <= org.getPerson().getValidStatus().getId(), "%s失败，人员“%s”的状态是“%s”。", new Object[] {
                                                                                                                               operateKind,
                                                                                                                               org.getPerson().getName(),
                                                                                                                               org.getPerson().getValidStatus()
                                                                                                                                  .getDisplayName() });
            }
        }

        if (fromStatuses.contains(org.getValidStatus())) {
            updateMainOrgPersonStatus(org.getFullId(), fromStatuses, toStatus);
            if (isEnableSubordinatePsm) {
                updateSubordinatePsmStatus(org.getFullId(), fromStatuses, toStatus);
            }
            updateChildrenStatus(org.getFullId(), fromStatuses, toStatus);
        } else {
            Util.check(org.getValidStatus().equals(toStatus), "%s失败，“%s”当前的状态是“%s”。", new Object[] { operateKind, org.getName(),
                                                                                                    org.getValidStatus().getDisplayName() });
        }
    }

    private void updateChildrenStatus(String orgFullId, Collection<ValidStatus> fromStatuses, ValidStatus toStatus) {
        String sql = this.getQuerySqlByName("updateChildrenStatus");

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("newStatus", toStatus.getId());
        params.put("oldStatus", ValidStatus.toList(fromStatuses));
        params.put("fullId", String.format("%s%%", orgFullId));

        this.generalRepository.updateByNativeSql(sql, params);
    }

    private void updateSubordinatePsmStatus(String orgFullId, Collection<ValidStatus> fromStatuses, ValidStatus toStatus) {
        String jpql = this.getQuerySqlByName("updateSubordinatePsmStatus");

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("newStatus", toStatus.getId());
        params.put("oldStatus", ValidStatus.toList(fromStatuses));
        params.put("fullId", String.format("%s%%", orgFullId));

        this.generalRepository.updateByNativeSql(jpql, params);
    }

    private void updateMainOrgPersonStatus(String orgFullId, Collection<ValidStatus> fromStatuses, ValidStatus toStatus) {
        String jpql = this.getQuerySqlByName("updateMainOrgPersonStatus");

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("newStatus", toStatus.getId());
        params.put("oldStatus", ValidStatus.toList(fromStatuses));
        params.put("fullId", String.format("%s%%", orgFullId));

        this.generalRepository.updateByNativeSql(jpql, params);
    }

    private void setOrgTenantId(Org org) {
        Operator operator = ThreadLocalUtil.getOperator();
        org.setTenantId(operator.getTenantId());
    }

    private void internalInsertOrgByTemplateId(String orgParentId, String templateParentId) {
        String parentId;

        List<OrgTemplate> orgTemplates = this.orgTemplateRepository.findByParentId(templateParentId);
        for (OrgTemplate item : orgTemplates) {
            Org org = new Org();

            org.setParentId(orgParentId);
            org.setOrgKindId(item.getOrgType().getOrgKindId());
            org.setCode(item.getOrgType().getCode());
            org.setName(item.getOrgType().getName());
            org.setSequence(item.getSequence());
            org.setStatus(ValidStatus.ENABLED.getId());

            parentId = insertOrg(org, item.getOrgType());

            internalInsertOrgByTemplateId(parentId, item.getId());
        }
    }

    private void internalBuildChildrenOrg(Org sourceParentOrg, Org targetParentOrg, Tenant tenant) {
        List<Org> children = this.orgRepository.findOrgStructureByParentId(sourceParentOrg.getId());
        // Org currentOrg;
        int index = 1;
        for (Org child : children) {
            Org org = new Org();
            ClassHelper.copyProperties(child, org);
            org.setParentId(targetParentOrg.getId());
            org.setSequence(index++);
            org.setTenantId(tenant.getId());
            org = this.internalInsertOrg(org, child.getOrgType());
            internalBuildChildrenOrg(child, org, tenant);
        }
    }

    private Org getTenantRootOrg(boolean isIndustry) {
        String cacheKey = isIndustry ? TENANT_INDUSTRY_ROOT_ID_CACHE_KEY : TENANT_ENTITY_ROOT_ID_CACHE_KEY;
        String tenantRootId = SystemCache.getParameter(cacheKey, String.class);
        if (StringUtil.isBlank(tenantRootId)) {
            throw new ApplicationException(String.format("参数“%s”未设置。", cacheKey));
        }
        return this.loadEabledOrg(tenantRootId);
    }

    @Override
    public Org buildOrgStructureByOrgId(String orgId, Tenant tenant) {
        Assert.hasText(orgId, "参数orgId不能为空。");

        Org sourceOrg = this.loadEabledOrg(orgId);
        Assert.isTrue(sourceOrg.getOrgKind() == OrgNodeKind.OGN, "模板组织必须为公司。");

        Org result = new Org();

        ClassHelper.copyProperties(sourceOrg, result);

        boolean isIndustry = tenant.getIsIndustry().equals(1);
        Org tenantRootOrg = getTenantRootOrg(isIndustry);
        result.setParentId(tenantRootOrg.getId());

        result.setCode(tenant.getCode());
        result.setName(tenant.getName());

        Integer sequence = this.commonDomainService.getNextSequence(Org.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, tenantRootOrg.getId());
        result.setSequence(sequence);

        result.setTenantId(tenant.getId());

        result = this.internalInsertOrg(result, sourceOrg.getOrgType());
        internalBuildChildrenOrg(sourceOrg, result, tenant);
        buildDefaultDeptStructure(result, tenant);

        return result;
    }

    private void buildDefaultDeptStructure(Org parent, Tenant tenant) {
        OrgType orgType;

        Org dept;
        dept = this.orgRepository.findByParentIdAndCode(parent.getId(), "INIT_DPT");
        if (dept == null) {
            dept = new Org();
            orgType = orgTypeRepository.findByOrgKindIdAndCode(OrgKind.dpt.name(), "INIT_DPT");
            dept.setOrgKindId(OrgKind.dpt.name());
            dept.setParentId(parent.getId());
            dept.setCode(orgType.getCode());
            dept.setName(orgType.getName());
            dept.setSequence(1);
            dept.setStatus(ValidStatus.ENABLED.getId());
            dept.setTenantId(tenant.getId());
            dept = this.internalInsertOrg(dept, orgType);
        }

        Org pos;
        pos = this.orgRepository.findByParentIdAndCode(dept.getId(), "INIT_POS");
        if (pos == null) {
            pos = new Org();
            orgType = orgTypeRepository.findByOrgKindIdAndCode(OrgKind.pos.name(), "INIT_POS");
            pos.setOrgKindId(OrgKind.pos.name());
            pos.setParentId(dept.getId());
            pos.setCode(orgType.getCode());
            pos.setName(orgType.getName());
            pos.setSequence(1);
            pos.setStatus(ValidStatus.ENABLED.getId());
            pos.setTenantId(tenant.getId());
            pos = this.internalInsertOrg(pos, orgType);
        }

        String roleId = SystemCache.getParameter("tenant.admin.roleId", String.class);
        Assert.isTrue(StringUtil.isNotBlank(roleId), "租户默认角色没有设置。");
        Role role = roleRepository.findOne(roleId);
        Assert.notNull(role, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, roleId, "角色"));
        List<Role> roles = new ArrayList<Role>(1);
        roles.add(role);

        pos = this.loadOrg(pos.getId());

        pos.buildRoles(roles);
        this.orgRepository.save(pos);

        Person admin = new Person();

        admin.setMainOrgId(pos.getId());
        admin.setCode(tenant.getContactNumber());
        admin.setName(tenant.getContacts());
        admin.setLoginName(tenant.getContactNumber());
        admin.setStatus(ValidStatus.ENABLED.getId());
        admin.setTenantId(tenant.getId());

        this.insertPerson(admin);
    }

    @Override
    public Org buildDefaultOrgStructure(Tenant tenant) {
        Org organ = new Org();

        boolean isIndustry = tenant.getIsIndustry().equals(1);
        Org tenantRootOrg = getTenantRootOrg(isIndustry);
        organ.setParentId(tenantRootOrg.getId());
        organ.setOrgKindId(OrgKind.ogn.name());
        organ.setCode(tenant.getCode());
        organ.setName(tenant.getName());
        organ.setStatus(ValidStatus.ENABLED.getId());
        OrgType orgType = orgTypeRepository.findByOrgKindIdAndCode(OrgKind.ogn.name(), "INIT_ORG");

        Integer sequence = this.commonDomainService.getNextSequence(Org.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, tenantRootOrg.getId());
        organ.setSequence(sequence);

        organ.setTenantId(tenant.getId());

        organ = this.internalInsertOrg(organ, orgType);
        buildDefaultDeptStructure(organ, tenant);
        return organ;
    }

    @Override
    public void insertOrgByTemplateId(Org org, String templateId) {
        Assert.notNull(org, "参数org不能为空。");
        Assert.hasText(templateId, "参数templateId不能为空。");

        OrgTemplate orgTemplate = this.orgTemplateRepository.findOne(templateId);
        insertOrg(org, orgTemplate.getOrgType());
        internalInsertOrgByTemplateId(org.getId(), orgTemplate.getId());
    }

    private Org internalInsertOrg(Org org, OrgType orgType) {
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));

        Assert.isTrue(!OrgNodeKind.PSM.equals(org.getOrgKind()), "新建组织失败，请使用“insertPersonMember”新建人员成员。");

        Org parent = null;
        if (!Org.ORG_ROOT_ID.equals(org.getParentId())) {
            parent = orgRepository.findOne(org.getParentId());
        }

        String id = CommonUtil.createGUID();
        org.setId(id);

        if (StringUtil.isBlank(org.getTenantId())) {
            setOrgTenantId(org);
        }

        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        checkParameter.setCheckParentIdAndCodeAndName(org.getParentId(), org.getId(), org.getCode(), org.getName());
        checkParameter.checkConstraints();

        @SuppressWarnings("unchecked")
        List<Org> duplicateEntities = (List<Org>) this.commonDomainService.findDuplicateEntities((Class<? extends IdentifiedEntity>) Org.class, checkParameter);
        Org other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }

        org.checkConstraints(parent, other, "新建组织");

        org.buildRedundantData(parent);
        org.reviseProperties();

        if (orgType != null) {
            org.setOrgType(orgType);
        }

        org = orgRepository.save(org);
        return org;
    }

    @Override
    public String insertOrg(Org org, OrgType orgType) {
        return internalInsertOrg(org, orgType).getId();
    }

    @Override
    public void updateOrg(Org org) {
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        Util.check(orgRepository.exists(org.getId()), MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, org.getId(), "组织"));

        Org parent = null;
        if (!org.isRootChild()) {
            parent = orgRepository.findOne(org.getParentId());
        }
        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        checkParameter.setCheckParentIdAndCodeAndName(org.getParentId(), org.getId(), org.getCode(), org.getName());
        checkParameter.checkConstraints();

        @SuppressWarnings("unchecked")
        List<Org> duplicateEntities = (List<Org>) this.commonDomainService.findDuplicateEntities((Class<? extends IdentifiedEntity>) Org.class, checkParameter);
        Org other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }

        org.checkConstraints(parent, other, "修改组织");

        String oldFullCode = org.getFullCode();
        String oldFullName = org.getFullName();
        String oldFullSequence = org.getFullSequence();
        String oldFullOrgKindId = org.getFullOrgKindId();

        org.buildRedundantData(parent);
        org.reviseProperties();

        // 更新排序号全路径
        if (!oldFullSequence.equals(org.getFullSequence())) {
            this.updateOrgChildrenFullSequence(org.getFullId(), oldFullSequence, org.getFullSequence());
        }
        if (!oldFullOrgKindId.equalsIgnoreCase(org.getFullOrgKindId())) {
            this.updateOrgChildrenFullOrgKindId(org.getFullId(), oldFullOrgKindId, org.getFullOrgKindId());
        }

        if ((!oldFullCode.equals(org.getFullCode())) || (!oldFullName.equals(org.getFullName())) || !oldFullOrgKindId.equalsIgnoreCase(org.getFullOrgKindId())) {
            // if (!oldFullName.equals(org.getFullName())) {
            // updateManagementOrgName(org.getFullId(), oldFullName,
            // org.getFullName());
            // updateAuthorizeOrgName(org.getFullId(), oldFullName,
            // org.getFullName());
            // updateAuthorizeOrgName(org.getFullId(), oldFullName,
            // org.getFullName());
            // }
            updateOrgChildrenFullCodeAndName(org.getFullId(), oldFullCode, org.getFullCode(), oldFullName, org.getFullName());
        }

        // org.setVersion(this.commonDomainService.getVersionNextId());

        this.orgRepository.save(org);
    }

    @SuppressWarnings("unchecked")
    private void updateOrgChildrenFullCodeAndName(String fullId, String parentOldFullCode, String parentNewFullCode, String parentOldFullName,
                                                  String parentNewFullName) {
        String fullIdCriteria = fullId + "/%";
        String jpql = this.getQuerySqlByName("updateOrgChildrenFullCodeAndName");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("likeFullId", fullIdCriteria);
        params.put("parentOldFullCode", parentOldFullCode);
        params.put("parentNewFullCode", parentNewFullCode);
        params.put("parentOldFullName", parentOldFullName);
        params.put("parentNewFullName", parentNewFullName);

        this.generalRepository.updateByNativeSql(jpql, params);

        List<Org> orgs = this.orgRepository.findByFullIdLike(fullIdCriteria);

        jpql = this.getQuerySqlByName("updateRedundantData");
        OrgNodeData orgNodeData;
        params.clear();
        for (Org org : orgs) {
            orgNodeData = OpmUtil.buildOrgNodeData(org);
            try {
                params = ClassHelper.describe(orgNodeData);
                params.remove("class");
                params.remove("fullId");
                params.remove("fullName");
                params.remove("personMemberId");
                params.remove("personMemberCode");
                params.remove("personMemberName");
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ApplicationException("转换数据出错。");
            }

            params.put("id", org.getId());
            this.generalRepository.updateByNativeSql(jpql, params);
        }
    }

    private void updateOrgChildrenFullOrgKindId(String fullId, String oldParentFullOrgKindId, String newParentFullOrgKindId) {
        String jpql = this.getQuerySqlByName("updateOrgChildrenFullOrgKindId");

        String fullIdCriteria = fullId + "/%";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("likeFullId", fullIdCriteria);
        params.put("parentOldOrgKindId", oldParentFullOrgKindId);
        params.put("parentNewFullOrgKindId", newParentFullOrgKindId);

        this.generalRepository.updateByNativeSql(jpql, params);
    }

    private void updateOrgChildrenFullSequence(String fullId, String oldParentFullSequence, String newParentFullSequence) {
        String jpql = this.getQuerySqlByName("updateOrgChildrenFullSequence");

        String fullIdCriteria = fullId + "/%";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fullId", fullIdCriteria);
        params.put("parentOldSequence", oldParentFullSequence);
        params.put("parentNewSequence", newParentFullSequence);

        this.generalRepository.updateByNativeSql(jpql, params);
    }

    @Override
    public void logicDeleteOrg(List<String> ids) {
        ArrayList<ValidStatus> fromStatuses = new ArrayList<ValidStatus>(2);
        fromStatuses.add(ValidStatus.ENABLED);
        fromStatuses.add(ValidStatus.DISABLED);
        for (String id : ids) {
            this.updateOrgStatus(id, fromStatuses, ValidStatus.LOGIC_DELETE, "删除组织", true);
        }
    }

    /*
     * 删除角色管理权限
     * @param fullId
     * ID全路径
     * @param isDeletePerson
     * 是否删除人员
     */
    @SuppressWarnings("unused")
    private void deleteRoleManagement(String fullId, boolean isDeletePerson) {
        // String deleteRoleManagementSql = String
        // .format("delete from SA_OPRoleManagement  where Org_Full_ID like '%s%%'",
        // new Object[] { orgFullId });
        //
        // serviceUtil.getEntityDao().executeUpdate(deleteRoleManagementSql);
        //
        // if (isDeletePerson) {
        // deleteRoleManagementSql = "delete from SA_OPRoleManagement\n"
        // + " where exists (select person.ID\n"
        // + "          from SA_OPPerson person\n"
        // +
        // "         where (SA_OPRoleManagement.Org_ID like person.ID || \"%\")\n"
        // + "           and exists (select org.ID\n"
        // + "                  from SA_OPOrg org\n"
        // + "                 where org.Org_Kind_ID = 'psm'\n"
        // + "                   and person.Main_Org_ID = org.Parent_ID\n"
        // + "                   and person.ID = org.Person_ID\n"
        // + "                   and org.Full_ID like '%s%%'))";
        // serviceUtil.getEntityDao().executeUpdate(deleteRoleManagementSql);
        // }
    }

    /**
     * 删除业务管理权限
     * 
     * @param orgFullId
     *            组织id全路径
     * @param isDeletePerson
     *            是否删除人员
     */
    @SuppressWarnings("unused")
    private void deleteBizManagement(String orgFullId, boolean isDeletePerson) {
        Assert.hasText(orgFullId, "参数orgFullId不能为空。");
        String orgFullIdCriteria = String.format("%s%%", orgFullId);
        // this sql statements were not writed in opm.xml
        StringBuilder sb = new StringBuilder();
        sb.append("delete from SA_OPBizManagement t");
        sb.append(" where t.manager_id in (select id from SA_OPOrg o where o.full_id like ?)");
        sb.append("    or t.subordination_id in");
        sb.append("       (select id from SA_OPOrg o where o.full_id like ?)");
        JDBCDao jdbcDao = this.sqlExecutorDao.getSqlQuery().getJDBCDao();
        jdbcDao.executeUpdate(sb.toString(), orgFullIdCriteria, orgFullIdCriteria);
        if (isDeletePerson) {
            // 删除人员，一人多岗
            sb.delete(0, sb.length());
            sb.append("delete from SA_OPBizManagement t");
            sb.append(" where exists");
            sb.append(" (select 1");
            sb.append("          from SA_OPPerson person, SA_OPOrg org");
            sb.append("         where org.Org_Kind_ID = 'psm'");
            sb.append("           and person.Main_Org_ID = org.Parent_ID");
            sb.append("           and person.ID = org.Person_ID");
            sb.append("           and org.full_id like ?");
            sb.append("           and (t.manager_id like concat(person.ID, '@%') or");
            sb.append("               t.subordination_id like concat(person.ID, '@%')))");
            jdbcDao.executeUpdate(sb.toString(), orgFullIdCriteria);
        }
    }

    /**
     * 删除授权
     * 
     * @param orgFullId
     *            组织ID全路径
     * @param isDeletePerson
     *            是否删除人员
     */
    @SuppressWarnings("unused")
    private void deleteAuthorize(String orgFullId, boolean isDeletePerson) {
        Assert.hasText(orgFullId, "参数orgFullId不能为空。");
        String orgFullIdCriteria = String.format("%s%%", orgFullId);
        JDBCDao jdbcDao = this.sqlExecutorDao.getSqlQuery().getJDBCDao();

        StringBuilder sb = new StringBuilder();
        sb.append("delete from SA_OPAuthorize t");
        sb.append(" where t.org_id in (select id from SA_OPOrg o where o.full_id like ?)");

        jdbcDao.executeUpdate(sb.toString(), orgFullIdCriteria);

        if (isDeletePerson) {
            sb.delete(0, sb.length());
            sb.append("delete from SA_OPAuthorize t");
            sb.append(" where exists (select 1");
            sb.append("          from SA_OPPerson person, SA_OPOrg org");
            sb.append("         where org.Org_Kind_ID = 'psm'");
            sb.append("           and person.Main_Org_ID = org.Parent_ID");
            sb.append("           and person.ID = org.Person_ID");
            sb.append("           and org.full_id like ?");
            sb.append("           and t.org_id like concat(person.ID, '@%'))");
            jdbcDao.executeUpdate(sb.toString(), orgFullIdCriteria);
        }
    }

    /**
     * 删除代理
     * 
     * @param orgFullId
     *            组织ID全路径
     * @param isDeletePerson
     *            是否删除人员
     */
    @SuppressWarnings("unused")
    private void deleteAgent(String orgFullId, boolean isDeletePerson) {
        String orgFullIdCriteria = String.format("%s%%", orgFullId);

        JDBCDao jdbcDao = this.sqlExecutorDao.getSqlQuery().getJDBCDao();
        StringBuilder sb = new StringBuilder();
        sb.append("delete from SA_OPAgent t");
        sb.append(" where t.client_id in (select id from SA_OPOrg o where o.full_id like ?)");
        sb.append("    or t.agent_id in (select id from SA_OPOrg o where o.full_id like ?)");

        jdbcDao.executeUpdate(sb.toString(), orgFullIdCriteria, orgFullIdCriteria);

        if (isDeletePerson) {
            sb.delete(0, sb.length());

            sb.append("delete from SA_OPAgent t");
            sb.append(" where exists (select 1");
            sb.append("          from SA_OPPerson person, SA_OPOrg org");
            sb.append("         where org.Org_Kind_ID = 'psm'");
            sb.append("           and person.Main_Org_ID = org.Parent_ID");
            sb.append("           and person.ID = org.Person_ID");
            sb.append("           and org.full_id like ?");
            sb.append("           and (t.client_id like concat(person.ID, '@%') or");
            sb.append("               t.agent_id like concat(person.ID, '@%')))");

            jdbcDao.executeUpdate(sb.toString(), orgFullIdCriteria, orgFullIdCriteria);
        }
    }

    private void checkBizManagementForDeleteOrg(Org org) {
        String orgFullIdCriteria = String.format("%s%%", org.getFullId());
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from SA_OPBizManagement t");
        sb.append(" where t.manager_id in (select id from SA_OPOrg o where o.full_id like ?)");
        sb.append("    or t.subordination_id in");
        sb.append("       (select id from SA_OPOrg o where o.full_id like ?)");
        int count = this.sqlExecutorDao.getSqlQuery().getJDBCDao().queryToInt(sb.toString(), orgFullIdCriteria, orgFullIdCriteria);

        Assert.isTrue(count == 0, String.format("组织“%s”或其子节点已分配业务管理权限，不能删除。", org.getName()));
    }

    private void checkAuthorizeForDeleteOrg(Org org) {
        String orgFullIdCriteria = String.format("%s%%", org.getFullId());
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from SA_OPAuthorize t");
        sb.append(" where t.org_id in (select id from SA_OPOrg o where o.full_id like ?)");
        int count = this.sqlExecutorDao.getSqlQuery().getJDBCDao().queryToInt(sb.toString(), orgFullIdCriteria);
        Assert.isTrue(count == 0, String.format("组织“%s”或其子节点已授权，不能删除。", org.getName()));
    }

    private void checkAgentForDeleteOrg(Org org) {
        String orgFullIdCriteria = String.format("%s%%", org.getFullId());
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from SA_OPAgent t");
        sb.append(" where t.client_id in (select id from SA_OPOrg o where o.full_id like ?)");
        sb.append("    or t.agent_id in (select id from SA_OPOrg o where o.full_id like ?)");

        int count = this.sqlExecutorDao.getSqlQuery().getJDBCDao().queryToInt(sb.toString(), orgFullIdCriteria, orgFullIdCriteria);
        Assert.isTrue(count == 0, String.format("组织“%s”或其子节点已设置了代理，不能删除。", org.getName()));
    }

    /**
     * 删除人员
     * 
     * @param orgFullId
     *            组织ID全路径
     */
    private void deletePerson(String orgFullId) {
        String orgFullIdCriteria = String.format("%s%%", orgFullId);
        StringBuilder sb = new StringBuilder();
        // 删除一人多岗的人员数据
        sb.append("delete from SA_OPOrg t");
        sb.append(" where Org_Kind_ID = 'psm'");
        sb.append("   and exists (select 1");
        sb.append("          from SA_OPPerson person, SA_OPOrg org");
        sb.append("         where person.ID = t.Person_ID");
        sb.append("           and person.Main_Org_ID <> t.Parent_ID");
        sb.append("           and org.Org_Kind_ID = 'psm'");
        sb.append("           and person.Main_Org_ID = org.Parent_ID");
        sb.append("           and person.ID = org.Person_ID");
        sb.append("           and org.Full_ID like ?)");
        // 删除人员
        this.sqlExecutorDao.executeUpdate(sb.toString(), orgFullIdCriteria);
        sb.delete(0, sb.length());
        sb.append("delete from SA_OPPerson person");
        sb.append(" where exists (select 1");
        sb.append("          from SA_OPOrg org");
        sb.append("         where org.Org_Kind_ID = 'psm'");
        sb.append("           and person.Main_Org_ID = org.Parent_ID");
        sb.append("           and person.ID = org.Person_ID");
        sb.append("           and Full_ID like ?)");
        this.sqlExecutorDao.executeUpdate(sb.toString(), orgFullIdCriteria);
    }

    @Override
    public void physicalDeleteOrg(String id, boolean isDeletePerson) {
        Org org = this.orgRepository.findOne(id);
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "组织"));
        Assert.isTrue(ValidStatus.LOGIC_DELETE == ValidStatus.fromId(org.getStatus()),
                      String.format("清除组织失败，“%s”当前的状态是“%s”。", org.getName(), ValidStatus.LOGIC_DELETE.getDisplayName()));

        checkBizManagementForDeleteOrg(org);
        checkAuthorizeForDeleteOrg(org);
        checkAgentForDeleteOrg(org);

        if (isDeletePerson) {
            deletePerson(org.getFullId());
        }

        deleteOrgChildren(org.getFullId());
        this.orgRepository.delete(id);
    }

    private void deleteOrgChildren(String fullId) {
        Util.check(!StringUtil.isBlank(fullId), "参数“fullId”不能为空。");
        String sql = this.getQuerySqlByName("deleteOrgChildren");
        this.sqlExecutorDao.executeUpdate(sql, fullId + "/%");
    }

    @Override
    public void restoreOrg(String id, boolean isEnableSubordinatePsm) {
        ArrayList<ValidStatus> fromStatuses = new ArrayList<ValidStatus>();
        fromStatuses.add(ValidStatus.LOGIC_DELETE);
        this.updateOrgStatus(id, fromStatuses, ValidStatus.ENABLED, "还原组织", isEnableSubordinatePsm);
    }

    @Override
    public void insertPersonMembers(List<String> personIds, String positionId, ValidStatus status, Boolean autoEnableOldPsm) {
        for (String personId : personIds) {
            Person person = this.personRepository.findOne(personId);
            Org position = this.orgRepository.findOne(positionId);
            insertPersonMember(person, position, status, autoEnableOldPsm);
        }
    }

    @Override
    public String insertPersonMember(Person person, Org position, ValidStatus psmStatus, boolean autoEnableOldPsm) {
        if (psmStatus == null) {
            psmStatus = ValidStatus.fromId(Math.min(person.getValidStatus().getId(), position.getValidStatus().getId()));
        }

        Util.check(psmStatus.getId() <= person.getStatus(), "新建人员成员失败，人员“%s”的状态是“%s”。", new Object[] { person.getName(),
                                                                                                      person.getValidStatus().getDisplayName() });

        String personMemberId = OpmUtil.formatPersonMemberId(person.getId(), position.getId());

        String sequence = this.getOrgNextSequence(position.getId());

        Org personMember = new Org();

        personMember.setId(personMemberId);
        if (StringUtil.isBlank(person.getTenantId())) {
            this.setOrgTenantId(personMember);
        } else {
            personMember.setTenantId(person.getTenantId());
        }
        personMember.setPerson(person);

        personMember.setParentId(position.getId());
        personMember.setCode(person.getCode());
        personMember.setName(person.getName());
        personMember.setLongName(person.getName());
        personMember.setStatus(psmStatus.getId());
        personMember.setOrgKindId(OrgNodeKind.PSM.toString().toLowerCase());

        personMember.setSequence(Integer.valueOf(sequence));

        personMember.buildRedundantData(position);

        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        checkParameter.setCheckParentIdAndCodeAndName(personMember.getParentId(), personMember.getId(), personMember.getCode(), personMember.getName());
        checkParameter.checkConstraints();

        @SuppressWarnings("unchecked")
        List<Org> duplicateEntities = (List<Org>) this.commonDomainService.findDuplicateEntities((Class<? extends IdentifiedEntity>) Org.class, checkParameter);
        Org other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }

        personMember.checkConstraints(position, other, "新建人员成员");

        personMember.buildRedundantData(position);
        personMember.reviseProperties();

        this.orgRepository.save(personMember);

        return personMemberId;
    }

    @Override
    public void updateOrgSequence(Map<String, String> params) {
        String newFullSequence, sequence;
        Org org;
        boolean isNum;

        Map<String, Object> updateParams = new HashMap<String, Object>(3);

        String jpql = this.getQuerySqlByName("updateOrgSequence");

        for (String key : params.keySet()) {
            isNum = params.get(key).matches("^\\d{1,3}$");
            if (!isNum) {
                throw new ApplicationException("排序号必须为1到3位数字。");
            }
        }

        for (String key : params.keySet()) {
            org = this.loadOrg(key);
            if (org != null) {
                sequence = CommonUtil.lpad(3, Integer.parseInt(params.get(key)));

                newFullSequence = OpmUtil.createFileFullName(CommonUtil.getPathOfFile(org.getFullSequence()), sequence, "");
                updateParams.put("fullSequence", newFullSequence);
                updateParams.put("sequence", sequence);
                updateParams.put("id", org.getId());

                this.generalRepository.updateByNativeSql(jpql, updateParams);

                this.updateOrgChildrenFullSequence(org.getFullId(), org.getFullSequence(), newFullSequence);
            }
        }

    }

    @Override
    public void enableOrg(String id, Boolean isEnableSubordinatePsm) {
        ArrayList<ValidStatus> fromStatuses = new ArrayList<ValidStatus>();
        fromStatuses.add(ValidStatus.DISABLED);
        this.updateOrgStatus(id, fromStatuses, ValidStatus.ENABLED, "启用组织", isEnableSubordinatePsm);
    }

    @Override
    public void enableSubordinatePsm(String orgId, String personId) {
        enablePerson(personId, false);
        enableOrg(orgId, false);
    }

    @Override
    public void disableOrg(String id) {
        ArrayList<ValidStatus> fromStatuses = new ArrayList<ValidStatus>(1);
        fromStatuses.add(ValidStatus.ENABLED);
        this.updateOrgStatus(id, fromStatuses, ValidStatus.DISABLED, "禁用组织", true);
    }

    @Override
    public void assignPerson(List<String> ids, String orgId) {
        // TODO Auto-generated method stub
    }

    @Override
    public Org loadOrg(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.orgRepository.findOne(id);
    }

    @Override
    public Org loadEabledOrg(String id) {
        /*
         * Calendar calendar = Calendar.getInstance();
         * calendar.set(Calendar.YEAR, 2018);
         * calendar.set(Calendar.MONTH, Calendar.JUNE);
         * calendar.set(Calendar.DAY_OF_MONTH, 1);
         * Date currentDate = new Date();
         * if (currentDate.after(calendar.getTime())){
         * throw new ApplicationException("查询人员出错，错误编码：0001。");
         * }
         */
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        Org org = this.loadOrg(id);
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, id, "组织"));
        Assert.isTrue(org.getValidStatus() == ValidStatus.ENABLED, String.format("组织“%s”已停用。", org.getFullName()));
        return org;
    }

    private void buildOrgManagementPermissionAndChildrenCount(List<Map<String, Object>> orgs, OrgQueryModel parameter) {
        Integer childrenCount;
        String manageType;
        parameter.setSql(this.getQuerySqlByName(parameter.getSqlName()));
        parameter.setCountSql(this.getQuerySqlByName("queryCount"));
        for (Map<String, Object> item : orgs) {
            // 添加权限
            manageType = parameter.getManageType();
            if (!StringUtil.isBlank(manageType)) {
                buildManagementPermissionFlag(manageType, item);
            }

            parameter.putParam("parentId", item.get("id"));
            childrenCount = this.sqlExecutorDao.queryToObjectByMapParam(parameter.getCountSql(), Integer.class, parameter.getQueryParams());
            item.put("hasChildren", childrenCount);
            item.put("isExpand", 0);
        }
    }

    private void buildManagementPermissionFlag(String manageType, Map<String, Object> map) {
        boolean flag = true;
        String fullId;
        if (!manageType.equals(Constants.NO_CONTROL_AUTHORITY)) {
            fullId = ClassHelper.convert(map.get("fullId"), String.class);
            if (!StringUtil.isBlank(fullId)) {
                flag = permissionBuilder.hasManagementPermission(manageType, fullId);
            }
        }
        map.put("managerPermissionFlag", flag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryOrgs(OrgQueryModel parameter) {
        boolean hasRootOrgPermission = ThreadLocalUtil.getOperator().hasRootOrgPermission();
        if (parameter.isQueryVirtualRoot() && (parameter.getSelectOrgScope() == SelectOrgScope.ALL || hasRootOrgPermission)) {
            List<OrgDesc> orgDescs = new ArrayList<OrgDesc>(1);
            orgDescs.add(OrgDesc.createRoot());
            Map<String, Object> result = new HashMap<String, Object>(1);
            result.put(Constants.ROWS, orgDescs);
            return result;
        }
        parameter.setSql(this.getQuerySqlByName(parameter.getSqlName()));
        parameter.setCountSql(this.getQuerySqlByName("queryCount"));
        parameter.setTreeQuery(true);
        parameter.parse();
        parameter.setDefaultOrderBy("Full_Sequence");
        Map<String, Object> data = this.sqlExecutorDao.executeQuery(parameter);
        buildOrgManagementPermissionAndChildrenCount((List<Map<String, Object>>) data.get(Constants.ROWS), parameter);
        return data;
    }

    @Override
    public Map<String, Object> slicedQueryOrgs(OrgQueryModel parameter) {
        /*
         * Calendar calendar = Calendar.getInstance();
         * calendar.set(Calendar.YEAR, 2018);
         * calendar.set(Calendar.MONTH, Calendar.JUNE);
         * calendar.set(Calendar.DAY_OF_MONTH, 1);
         * Date currentDate = new Date();
         * if (currentDate.after(calendar.getTime())){
         * throw new ApplicationException("查询人员出错，错误编码：0001。");
         * }
         */
        parameter.setSql(this.getQuerySqlByName(parameter.getSqlName()));
        parameter.parse();
        return this.sqlExecutorDao.executeSlicedQuery(parameter);
    }

    @Override
    public List<Org> queryAllPersonMembersByOrgId(String orgId) {
        Org org = this.orgRepository.findOne(orgId);
        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, orgId, "组织"));
        return this.orgRepository.findAllPersonMembersByOrgFullId(String.format("%s%%", org.getFullId()));
    }

    /**
     * 获取父节点的最大序列号
     *
     * @param parentId
     *            父节点ID
     * @param orgKind
     *            节点类型
     * @param isExcludeDiabled
     *            是否排除停用的组织
     * @return
     */
    private Integer getMaxSequence(String parentId, OrgKind orgKind, boolean isExcludeDiabled) {
        StringBuilder sb = new StringBuilder(this.getQuerySqlByName("getOrgNextSequence"));
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("parentId", parentId);

        if (orgKind != null) {
            sb.append(" and orgKindId = :orgKindId ");
            params.put("orgKindId", orgKind.toString());
        }
        if (isExcludeDiabled) {
            sb.append(" and status = :status ");
            params.put("status", ValidStatus.ENABLED.getId());
        }

        Integer maxSequence = (Integer) this.generalRepository.single(sb.toString(), params);

        if (maxSequence == null) {
            return 1;
        } else {
            return ++maxSequence;
        }
    }

    @Override
    public String getOrgNextSequence(String parentId) {
        Integer sequence = getMaxSequence(parentId, null, false);
        return String.format("%03d", sequence);
    }

    @Override
    public Org loadOrgByFullName(String fullName) {
        Assert.hasText(fullName, "参数fullName不能为空。");
        return this.orgRepository.findByFullName(fullName);
    }

    @Override
    public Org loadOrgByFullId(String fullId) {
        Assert.hasText(fullId, "参数fullId不能为空。");
        return this.orgRepository.findByFullId(fullId);
    }

    @Override
    public Org loadMainOrgByPersonMemberId(String personMemberId) {
        Assert.hasText(personMemberId, "参数personMemberId不能为空。");
        return this.orgRepository.findMainOrgByPersonMemberId(personMemberId);
    }

    @Override
    public Org loadMainOrgByPersonId(String personId) {
        Assert.hasText(personId, "参数personId不能为空。");
        return this.orgRepository.findMainOrgByPersonId(personId);
    }

    @Override
    public Org loadMainOrgByPersonName(String personName) {
        Assert.hasText(personName, "参数personName不能为空。");
        return this.orgRepository.findMainOrgByPersonName(personName);
    }

    @Override
    public Org loadMainOrgByLoginName(String loginName) {
        Assert.hasText(loginName, "参数loginName不能为空。");
        return this.orgRepository.findMainOrgByLoginName(loginName.toUpperCase());
    }

    @Override
    public List<Org> loadOrgListByLoginName(String loginName) {
        Assert.hasText(loginName, "参数loginName不能为空。");
        return this.orgRepository.findOrgByLoginName(loginName.toUpperCase());
    }

    @Override
    public void quoteAuthorizationAndBizManagement(String sourceOrgId, String destOrgId) {
        // this.authorizationService.quoteAuthorize(sourceOrgId, destOrgId);
        // this.managementService.quoteBizManagement(sourceOrgId, destOrgId);
    }

    @Override
    public void changePersonMainOrg(String id, String personMemberId, boolean isDisableOldMasterPsm) {
        // 1、判断设置后的人员成员是否存在
        Org newOrg = this.loadOrg(personMemberId);
        Assert.notNull(newOrg, String.format("设置主人员成员失败，无效的人员成员标识“%s”。", personMemberId));
        // 2、判断人员是否存在
        Org org = this.orgRepository.findMainOrgByPersonId(id);
        Assert.notNull(org, String.format("设置主人员成员失败，无效的人员标识“%s”。", personMemberId));
        // 3、验证人员的状态和人员成员的状态是否一致
        Util.check(org.getPerson().getValidStatus().equals(org.getValidStatus()), "设置“%s”主人员成员失败，人员成员的状态与人员状态不一致。", org.getName());
        // 4、 更新人员的主组织
        Person person = this.personRepository.findOne(id);
        person.setMainOrgId(newOrg.getParentId());
        person.setTenantId(newOrg.getTenantId());
        this.personRepository.save(person);

        if ((isDisableOldMasterPsm) && (!personMemberId.equals(org.getId()))) {
            disableOrg(org.getId());
        }
    }

    @Override
    public List<Org> queryPersonMembersByPersonId(String personId) {
        Assert.hasText(personId, "参数personId不能为空。");
        return this.orgRepository.findPersonMembersByPersonId(personId);
    }

    private void checkPersonDuplication(Person person, boolean isCheckLogicDeleted) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", person.isNew() ? StringPool.AT : person.getId());
        params.put("code", person.getCode().toUpperCase());

        String loginName = person.getCode().toUpperCase();
        if (Util.isNotEmptyString(person.getLoginName())) {
            loginName = person.getLoginName().toUpperCase();
        }
        params.put("loginName", loginName);

        String cardNoCriteria = "";
        if (Util.isNotEmptyString(person.getCertificateNo())) {
            cardNoCriteria = " or upper(certificateNo) = :certificateNo ";
            params.put("certificateNo", person.getCertificateNo().toUpperCase());
        }

        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "person");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(queryDescriptor.getSqlByName("checkDuplication"), cardNoCriteria));

        if (!isCheckLogicDeleted) {
            sb.append(" and status != :status");
            params.put("status", -1);
        }

        @SuppressWarnings("unchecked")
        List<Person> personList = this.generalRepository.query(sb.toString(), params);

        if (personList.size() > 0) {

            Person existPerson = personList.get(0);

            Util.check(!person.getCode().equalsIgnoreCase(existPerson.getCode()), "人员编码“%s”与“%s”的编码重复。",
                       new Object[] { person.getCode(), existPerson.getName() });
            Util.check(!loginName.equalsIgnoreCase(existPerson.getLoginName()), "人员登录名“%s”与“%s”的登录名或编码重复。", new Object[] { loginName, existPerson.getName() });
            if (Util.isNotEmptyString(person.getCertificateNo())) {
                Util.check(!person.getCertificateNo().equalsIgnoreCase(existPerson.getCertificateNo()), "人员证件号“%s”与“%s”的证件号重复。",
                           new Object[] { person.getCertificateNo(), existPerson.getName() });
            }
        }
    }

    @Override
    public String insertPerson(Person person) {
        int count = this.personRepository.countByStatus(ValidStatus.ENABLED.getId());
        boolean flag = true;
        try {
            // linux 环境下无法取得文件
            flag = this.getLicenseChecker().checkRegistUser(count);
        } catch (ExceptionInInitializerError e1) {
            flag = true;
        } catch (NoClassDefFoundError e1) {
            flag = true;
        } catch (Exception e1) {
            flag = true;
        }
        if (!flag) {
            throw new ApplicationException("用户数已超过注册用户数。");
        }
        /*
         * Calendar calendar = Calendar.getInstance();
         * calendar.set(Calendar.YEAR, 2018);
         * calendar.set(Calendar.MONTH, Calendar.JUNE);
         * calendar.set(Calendar.DAY_OF_MONTH, 1);
         * Date currentDate = new Date();
         * if (currentDate.after(calendar.getTime())){
         * throw new ApplicationException("新增人员出错，错误编码：0001。");
         * }
         */

        // Date lastDate = Date.parse("")

        Org position = this.loadOrg(person.getMainOrgId());
        Assert.notNull(position, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, person.getMainOrgId(), "组织"));

        Assert.notNull(person, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        Assert.hasText(person.getMainOrgId(), String.format("新建人员'%s'失败，没有对应的岗位。", person.getName()));

        if (StringUtil.isBlank(person.getTenantId())) {
            Operator operator = ThreadLocalUtil.getOperator();
            person.setTenantId(operator.getTenantId());
        }

        person.checkConstraints();
        checkPersonDuplication(person, true);

        if (person.getStatus() == null) {
            person.setStatus(position.getStatus());
        }

        person.setPassword(OpmUtil.getDefaultEncryptPassword());
        person.setIsOperator(false);

        if (person.getSecurityGrade() != null) {
            String personSecurityGradeId = person.getSecurityGrade().getId();
            SecurityPolicy securityPolicy = this.securityPolicyApplication.findSecurityGrade(personSecurityGradeId, ValidStatus.ENABLED.getId());
            Assert.state(securityPolicy != null, String.format("密级“%s”没有设置或启用安全策略。", person.getSecurityGrade().getDisplayName()));
            Assert.hasText(securityPolicy.getInitPassword(), String.format("安全策略“%s”，初始化密码不能为空。", person.getSecurityGrade().getDisplayName()));
            person.setPassword(Md5Builder.getMd5(securityPolicy.getInitPassword()));
        }

        person = this.personRepository.save(person);

        insertPersonMember(person, position, position.getValidStatus(), false);

        String personId = person.getId();

        return personId;
    }

    @Override
    public void updatePerson(Person person) {
        Assert.notNull(person, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));

        person = this.commonDomainService.loadAndFillinProperties(person, Person.class);

        person.checkConstraints();

        checkPersonDuplication(person, true);
        // 1、保存人员
        this.personRepository.save(person);
        // String personId = person.getId();
        // this.flexFieldApplication.saveFlexFieldStorages("Person", personId);
        // 2、更新组织
        List<Org> orgs = this.orgRepository.findPersonMembersByPersonId(person.getId());
        for (Org org : orgs) {
            org.setCode(person.getCode());
            org.setName(person.getName());
            this.updateOrg(org);
        }
    }

    @Override
    public void updatePersonSimple(Person person) {
        Assert.notNull(person, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        person = this.commonDomainService.loadAndFillinProperties(person, Person.class);
        person.checkConstraints();

        checkPersonDuplication(person, true);
        this.personRepository.save(person);

    }

    /**
     * 更新人员状态
     *
     * @param id
     *            人员唯一标识
     * @param version
     *            版本号
     * @param fromStatuses
     *            前置状态
     * @param toStatus
     *            更新状态
     * @param operateType
     *            操作类型
     * @param isEnableSubordinatePsm
     *            是否允许从属人员成员
     */
    private void updatePersonStatus(String id, Collection<ValidStatus> fromStatuses, ValidStatus toStatus, String operateType, boolean isEnableSubordinatePsm) {
        Org personMember = this.orgRepository.findMainOrgByPersonId(id);
        Assert.notNull(personMember, String.format("未找到人员ID“%s”对应的人员。", id));
        this.updateOrgStatus(personMember.getId(), fromStatuses, toStatus, operateType, isEnableSubordinatePsm);
    }

    @Override
    public void logicDeletePerson(String id) {
        List<ValidStatus> fromStatuses = new ArrayList<ValidStatus>();
        fromStatuses.add(ValidStatus.ENABLED);
        fromStatuses.add(ValidStatus.DISABLED);
        this.updatePersonStatus(id, fromStatuses, ValidStatus.LOGIC_DELETE, "删除人员", true);
    }

    @Override
    public void physicalDeletePerson(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        Person person = this.personRepository.findOne(id);
        Assert.notNull(person, String.format("未找到人员ID“%s”对应的人员。", id));

        Util.check(person.getValidStatus().equals(ValidStatus.LOGIC_DELETE), "%s失败，“%s”当前的状态是“%s”。", new Object[] { "清除人员", person.getName(),
                                                                                                                   person.getValidStatus().getDisplayName() });

        List<Org> personMembers = this.orgRepository.findPersonMembersByPersonId(id);
        this.orgRepository.delete(personMembers);

        this.personRepository.delete(person);
    }

    @Override
    public Person loadPerson(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.personRepository.findOne(id);
    }

    @Override
    public void checkPersonIsEnabled(String personId) {
        Person person = loadPerson(personId);
        Assert.notNull(person, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, personId, "人员"));
        Assert.isTrue(person.isEnabled(), String.format("人员“%s”状态为“%s”。", person.getName(), person.getValidStatus().getDisplayName()));
    }

    @Override
    public Person loadPersonByIdCard(String certificateNo) {
        Assert.hasText(certificateNo, "参数certificateNo不能为空。");
        return this.personRepository.findByCertificateNo(certificateNo);
    }

    @Override
    public Person loadPersonByLoginName(String loginName) {
        Assert.hasText(loginName, "参数loginName不能为空。");
        return this.personRepository.findByLoginName(loginName.toUpperCase());
    }

    @Override
    public Person loadPersonByCaNo(String caNo) {
        Assert.hasText(caNo, "参数caNo不能为空。");
        return this.personRepository.findByCaNo(caNo);
    }

    @Override
    public void enablePerson(String id, boolean isEnableSubordinatePsm) {
        ArrayList<ValidStatus> fromStatuses = new ArrayList<ValidStatus>();
        fromStatuses.add(ValidStatus.DISABLED);
        updatePersonStatus(id, fromStatuses, ValidStatus.ENABLED, "启用人员", isEnableSubordinatePsm);
    }

    @Override
    public void disablePerson(String id) {
        ArrayList<ValidStatus> fromStatuses = new ArrayList<ValidStatus>();
        fromStatuses.add(ValidStatus.ENABLED);
        updatePersonStatus(id, fromStatuses, ValidStatus.DISABLED, "禁用人员", true);
    }

    @Override
    public void initPassword(String personId) {
        Assert.hasText(personId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "personId"));
        Person person = this.loadPerson(personId);
        Util.check(person != null, "没有找到ID“%s”对应的人员。", new Object[] { personId });
        person.setPassword(OpmUtil.getDefaultEncryptPassword());
        this.personRepository.save(person);
    }

    private void internalUpdatePassword(String personId, String oldPassword, String newPassword) {
        Assert.hasText(personId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "personId"));
        Assert.isTrue(!StringUtil.isBlank(newPassword) && !StringUtil.isBlank(oldPassword), "密码不能为空。");
        Assert.isTrue(!oldPassword.equalsIgnoreCase(newPassword), "新密码与旧密码不能相同。");

        Person person = this.loadPerson(personId);
        Assert.state(person != null, String.format("没有找到ID“%s”对应的人员。", personId));

        String decodedOldPassword = new String(Base64.decodeBase64(oldPassword));
        if (!person.getPassword().equals(Md5Builder.getMd5(decodedOldPassword))) {
            throw new ApplicationException("旧密码错误。");
        }

        if (person.getSecurityGrade() != null) {
            String decodedNewPassword = new String(Base64.decodeBase64(newPassword));
            String personSecurityGradeId = person.getSecurityGrade().getId();
            SecurityPolicy securityPolicy = this.securityPolicyApplication.findSecurityGrade(personSecurityGradeId, ValidStatus.ENABLED.getId());
            Assert.state(securityPolicy != null, String.format("密级“%s”没有设置或启用安全策略。", person.getSecurityGrade().getDisplayName()));

            if (decodedNewPassword.length() < securityPolicy.getPasswordMinimumLength()) {
                throw new ApplicationException("新密码长度不能低于" + securityPolicy.getPasswordMinimumLength() + "位");
            }
            if (!decodedNewPassword.matches("^.*[0-9]{" + securityPolicy.getNumberCount() + ",}?.*$")) {
                throw new ApplicationException("新密码数字个数不能低于" + securityPolicy.getNumberCount() + "个");
            }
            if (!decodedNewPassword.matches("^.*[A-Z]{" + securityPolicy.getUppercaseCount() + ",}?.*$")) {
                throw new ApplicationException("新密码大写字母个数不能低于" + securityPolicy.getUppercaseCount() + "个");
            }
            if (!decodedNewPassword.matches("^.*[a-z]{" + securityPolicy.getLowercaseCount() + ",}?.*$")) {
                throw new ApplicationException("新密码小写字母不能低于" + securityPolicy.getLowercaseCount() + "个");
            }
            if (!decodedNewPassword.matches("^.*[/~/^/$/.//,;:`!@#%&/*/|///?/_/-/+/(/)/[/]/{/}]{" + securityPolicy.getSpecialCharacterCount() + ",}?.*$")) {
                throw new ApplicationException("新密码特殊字符不能低于" + securityPolicy.getSpecialCharacterCount() + "个");
            }
        }

        String decodedPassword = new String(Base64.decodeBase64(newPassword));
        person.setPassword(Md5Builder.getMd5(decodedPassword));
        this.personRepository.save(person);

        PersonAccount personAccount = this.securityPolicyApplication.loadPersonAccountByLoginName(person.getLoginName().toUpperCase());
        if (personAccount != null) {
            if (personAccount.isStatus(PersonAccountStatus.INIT)) {
                personAccount.setStatus(PersonAccountStatus.NORMAL.getId());
            }
            personAccount.setLastModifiedPasswordDate(new Date());
            securityPolicyApplication.savePersonAccount(personAccount);
        }
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        Operator operator = ThreadLocalUtil.getOperator();
        internalUpdatePassword(operator.getUserId(), oldPassword, newPassword);
    }

    @Override
    public Map<String, Object> slicedQueryPerson(String parentIdOrFullId, boolean showDisabled, boolean showAllChildren, CodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "person");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        if (!Org.ORG_ROOT_ID.equals(parentIdOrFullId)) {
            if (showAllChildren) {
                queryModel.addCriteria(" and o.full_id like :parentIdOrFullId");
            } else {
                queryModel.addCriteria(" and o.parent_id = :parentIdOrFullId");
            }
            queryModel.putParam("parentIdOrFullId", parentIdOrFullId + "%");
        }

        if (!showDisabled) {
            queryModel.addCriteria(" and p.status = :status");
            queryModel.putParam("status", ValidStatus.ENABLED.getId());
        }

        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    public String adjustPersonOrgStructure(String personMemberId, String positionId, boolean isDisableOldPsm, boolean isUpdateMainPosition) {
        Org personMember = this.loadOrg(personMemberId);
        Assert.notNull(personMember, String.format("调整人员组织失败，无效的人员成员标识“%s”。", personMemberId));

        Org position = this.loadOrg(positionId);
        Assert.notNull(position, String.format("调整人员组织失败，无效的岗位标识“%s”。", positionId));

        String newPersonMemberId = this.insertPersonMember(personMember.getPerson(), position, personMember.getValidStatus(), true);

        if (isUpdateMainPosition) {
            this.changePersonMainOrg(personMember.getPerson().getId(), newPersonMemberId, isDisableOldPsm);
        }

        return newPersonMemberId;
    }

    private void initOrgProperties(Org org) {
        Assert.notNull(org, "参数org不能为空。");
        List<OrgPropertyDefinition> orgPropertyDefinitions = this.orgPropertyDefinitionRepository.findByOrgKindIdOrderBySequence(org.getOrgKindId());
        OrgProperty orgProperty;
        List<OrgProperty> orgProperties = org.getOrgProperties();

        boolean changed = false, found;

        for (OrgPropertyDefinition item : orgPropertyDefinitions) {
            found = false;
            for (OrgProperty inner : orgProperties) {
                if (inner.getPropertyDefinitionId().equals(item.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                orgProperty = new OrgProperty();
                orgProperty.setPropertyDefinitionId(item.getId());
                // orgProperty.setVersion(this.commonDomainService.getVersionNextId());
                orgProperties.add(orgProperty);
                changed = true;
            }
        }

        if (changed) {
            org.setOrgProperties(orgProperties);
            this.orgRepository.saveAndFlush(org);
        }
    }

    @Override
    public Map<String, Object> queryOrgProperties(String orgId) {
        Org org = this.loadOrg(orgId);

        Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, orgId, "组织"));

        initOrgProperties(org);

        QueryModel queryModel = new QueryModel();
        queryModel.setSql(this.getQuerySqlByName("queryOrgProperties"));
        queryModel.putParam("orgId", orgId);

        return this.sqlExecutorDao.executeQuery(queryModel);
    }

}
