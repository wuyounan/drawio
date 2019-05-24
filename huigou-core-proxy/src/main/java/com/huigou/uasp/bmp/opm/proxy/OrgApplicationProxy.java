package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgPropertyDefinition;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.domain.query.OrgPropertyDefinitionQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.OrgQueryModel;
import com.huigou.uasp.bmp.opm.impl.OrgApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.OrgPropertyDefinitionRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTemplateRepository;
import com.huigou.uasp.bmp.opm.repository.org.OrgTypeRepository;
import com.huigou.uasp.bmp.opm.repository.org.PersonRepository;
import com.huigou.uasp.bmp.opm.repository.org.RoleRepository;
import com.huigou.uasp.bmp.securitypolicy.application.SecurityPolicyApplication;

@Service("orgApplicationProxy")
public class OrgApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private OrgPropertyDefinitionRepository orgPropertyDefinitionRepository;

    @Autowired
    private OrgTemplateRepository orgTemplateRepository;

    @Autowired
    private OrgTypeRepository orgTypeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SecurityPolicyApplication securityPolicyApplication;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private OrgApplication orgApplication;

    void initProperties(OrgApplicationImpl orgApplicationImpl) {
        orgApplicationImpl.setCommonDomainService(commonDomainService);
        orgApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        orgApplicationImpl.setGeneralRepository(generalRepository);

        orgApplicationImpl.setOrgRepository(orgRepository);
        orgApplicationImpl.setOrgPropertyDefinitionRepository(orgPropertyDefinitionRepository);
        orgApplicationImpl.setOrgTemplateRepository(orgTemplateRepository);
        orgApplicationImpl.setOrgTypeRepository(orgTypeRepository);
        orgApplicationImpl.setPersonRepository(personRepository);
        orgApplicationImpl.setSecurityPolicyApplication(this.securityPolicyApplication);
        orgApplicationImpl.setRoleRepository(roleRepository);
    }

    public OrgApplication getOrgApplication() {
        if (orgApplication == null) {
            if (orgApplication == null) {
                OrgApplicationImpl orgApplicationImpl = coreApplicationFactory.getOrgApplication();
                orgApplication = orgApplicationImpl;
            }
        }
        return orgApplication;
    }

    @Transactional
    public String saveOrgPropertyDefinition(OrgPropertyDefinition orgPropertyDefinition) {
        return getOrgApplication().saveOrgPropertyDefinition(orgPropertyDefinition);
    }

    public OrgPropertyDefinition loadOrgPropertyDefinition(String id) {
        return getOrgApplication().loadOrgPropertyDefinition(id);
    }

    @Transactional
    public void deleteOrgPropertyDefinitions(List<String> ids) {
        getOrgApplication().deleteOrgPropertyDefinitions(ids);
    }

    @Transactional
    public void updateOrgPropertyDefinitionsSequence(Map<String, Integer> params) {
        getOrgApplication().updateOrgPropertyDefinitionsSequence(params);
    }

    @Transactional
    public Integer getOrgPropertyDefinitionNextSequence(String orgKindId) {
        return getOrgApplication().getOrgPropertyDefinitionNextSequence(orgKindId);
    }

    public Map<String, Object> queryOrgPropertyDefinitions(OrgPropertyDefinitionQueryRequest queryRequest) {
        return getOrgApplication().queryOrgPropertyDefinitions(queryRequest);
    }

    @Transactional
    public void insertOrgByTemplateId(Org org, String templateId) {
        getOrgApplication().insertOrgByTemplateId(org, templateId);
    }

    @Transactional
    public String insertOrg(Org org, OrgType orgType) {
        return getOrgApplication().insertOrg(org, orgType);
    }

    @Transactional
    public void updateOrg(Org org) {
        getOrgApplication().updateOrg(org);
    }

    @Transactional
    public void logicDeleteOrg(List<String> ids) {
        getOrgApplication().logicDeleteOrg(ids);
    }

    @Transactional
    public void physicalDeleteOrg(String id, boolean isDeletePerson) {
        getOrgApplication().physicalDeleteOrg(id, isDeletePerson);
    }

    @Transactional
    public void restoreOrg(String id, boolean isEnableSubordinatePsm) {
        getOrgApplication().restoreOrg(id, isEnableSubordinatePsm);
    }

    @Transactional
    public void insertPersonMembers(List<String> personIds, String orgId, ValidStatus status, Boolean autoEnableOldPsm) {
        getOrgApplication().insertPersonMembers(personIds, orgId, status, autoEnableOldPsm);
    }

    @Transactional
    public String insertPersonMember(Person person, Org position, ValidStatus psmStatus, boolean autoEnableOldPsm) {
        return getOrgApplication().insertPersonMember(person, position, psmStatus, autoEnableOldPsm);
    }

    @Transactional
    public void updateOrgSequence(Map<String, String> params) {
        getOrgApplication().updateOrgSequence(params);
    }

    @Transactional
    public void enableOrg(String id, Boolean isEnableSubordinatePsm) {
        getOrgApplication().enableOrg(id, isEnableSubordinatePsm);
    }

    @Transactional
    public void enableSubordinatePsm(String orgId, String personId) {
        getOrgApplication().enableSubordinatePsm(orgId, personId);
    }

    @Transactional
    public void disableOrg(String id) {
        getOrgApplication().disableOrg(id);
    }

    @Transactional
    public void assignPerson(List<String> ids, String orgId) {
        getOrgApplication().assignPerson(ids, orgId);
    }

    public Org loadOrg(String id) {
        return getOrgApplication().loadOrg(id);
    }

    public Org loadEabledOrg(String id) {
        return getOrgApplication().loadEabledOrg(id);
    }

    public Map<String, Object> queryOrgs(OrgQueryModel parameter) {
        return getOrgApplication().queryOrgs(parameter);
    }

    public List<Org> queryAllPersonMembersByOrgId(String orgId) {
        return getOrgApplication().queryAllPersonMembersByOrgId(orgId);
    }

    public Map<String, Object> slicedQueryOrgs(OrgQueryModel parameter) {
        return getOrgApplication().slicedQueryOrgs(parameter);
    }

    @Transactional
    public String getOrgNextSequence(String parentId) {
        return getOrgApplication().getOrgNextSequence(parentId);
    }

    public Org loadOrgByFullName(String fullName) {
        return getOrgApplication().loadOrgByFullName(fullName);
    }

    public Org loadOrgByFullId(String fullId) {
        return getOrgApplication().loadOrgByFullId(fullId);
    }

    public Org loadMainOrgByPersonMemberId(String personMemberId) {
        return getOrgApplication().loadMainOrgByPersonMemberId(personMemberId);
    }

    public Org loadMainOrgByPersonId(String personId) {
        return getOrgApplication().loadMainOrgByPersonId(personId);
    }

    public Org loadMainOrgByPersonName(String name) {
        return getOrgApplication().loadMainOrgByPersonName(name);
    }

    public Org loadMainOrgByLoginName(String loginName) {
        return getOrgApplication().loadMainOrgByLoginName(loginName);
    }

    public List<Org> loadOrgListByLoginName(String loginName) {
        return getOrgApplication().loadOrgListByLoginName(loginName);
    }

    @Transactional
    public void changePersonMainOrg(String id, String personMemberId, boolean isDisableOldMasterPsm) {
        getOrgApplication().changePersonMainOrg(id, personMemberId, isDisableOldMasterPsm);
    }

    public List<Org> queryPersonMembersByPersonId(String personId) {
        return getOrgApplication().queryPersonMembersByPersonId(personId);
    }

    @Transactional
    public void quoteAuthorizationAndBizManagement(String sourceOrgId, String destOrgId) {
        getOrgApplication().quoteAuthorizationAndBizManagement(sourceOrgId, destOrgId);
    }

    @Transactional
    public String insertPerson(Person person) {
        return getOrgApplication().insertPerson(person);
    }

    @Transactional
    public void updatePerson(Person person) {
        getOrgApplication().updatePerson(person);
    }

    @Transactional
    public void updatePersonSimple(Person person) {
        getOrgApplication().updatePersonSimple(person);
    }

    @Transactional
    public void logicDeletePerson(String id) {
        getOrgApplication().logicDeletePerson(id);
    }

    @Transactional
    public void physicalDeletePerson(String id) {
        getOrgApplication().physicalDeletePerson(id);
    }

    public Person loadPerson(String id) {
        return getOrgApplication().loadPerson(id);
    }

    public void checkPersonIsEnabled(String personId) {
        getOrgApplication().checkPersonIsEnabled(personId);
    }

    public Person loadPersonByIdCard(String certificateNo) {
        return getOrgApplication().loadPersonByIdCard(certificateNo);
    }

    public Person loadPersonByLoginName(String loginName) {
        return getOrgApplication().loadPersonByLoginName(loginName);
    }
    
    public Person loadPersonByCaNo(String caNo) {
        return getOrgApplication().loadPersonByCaNo(caNo);
    }

    @Transactional
    public void enablePerson(String id, boolean isEnableSubordinatePsm) {
        getOrgApplication().enablePerson(id, isEnableSubordinatePsm);
    }

    @Transactional
    public void disablePerson(String id) {
        getOrgApplication().disablePerson(id);
    }

    @Transactional
    public void updatePassword(String oldPassword, String newPassword) {
        getOrgApplication().updatePassword(oldPassword, newPassword);
    }
    
    @Transactional
    public void initPassword(String personId) {
        getOrgApplication().initPassword(personId);
    }

    public Map<String, Object> slicedQueryPerson(String parentIdOrFullId, boolean showDisabled, boolean showAllChildren, CodeAndNameQueryRequest queryRequest) {
        return getOrgApplication().slicedQueryPerson(parentIdOrFullId, showDisabled, showAllChildren, queryRequest);
    }

    @Transactional
    public String adjustPersonOrgStructure(String personMemberId, String positionId, boolean isDisableOldPsm, boolean isUpdateMainPosition) {
        return getOrgApplication().adjustPersonOrgStructure(personMemberId, positionId, isDisableOldPsm, isUpdateMainPosition);
    }

    public Map<String, Object> queryOrgProperties(String orgId) {
        return getOrgApplication().queryOrgProperties(orgId);
    }

}
