package com.huigou.uasp.bmp.securitypolicy.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.SecurityGrade;
import com.huigou.data.domain.EntityUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.securitypolicy.application.PersonAccountApplication;
import com.huigou.uasp.bmp.securitypolicy.application.PersonLoginLimitApplication;
import com.huigou.uasp.bmp.securitypolicy.application.SecurityPolicyApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;
import com.huigou.uasp.bmp.securitypolicy.domain.model.SecurityPolicy;
import com.huigou.uasp.bmp.securitypolicy.domain.query.PersonLoginLimitDesc;
import com.huigou.uasp.bmp.securitypolicy.domain.query.SecurityPoliciesQueryRequest;
import com.huigou.uasp.bmp.securitypolicy.repository.SecurityPolicyRepository;

@Service("securityPolicyApplication")
public class SecurityPolicyApplicationImpl extends BaseApplication implements SecurityPolicyApplication {

    @Autowired
    private PersonAccountApplication personAccountApplication;

    @Autowired
    private SecurityPolicyRepository securityPolicyRepository;

    @Autowired
    private PersonLoginLimitApplication personLoginLimitApplication;

    @Override
    @Transactional
    public void saveSecurityPolicy(SecurityPolicy securityPolicy) {
        Assert.notNull(securityPolicy, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        securityPolicyRepository.save(securityPolicy);
    }

    @Override
    @Transactional
    public void updateSecurityPoliciesStatus(List<String> ids, Integer status) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.STATUS_NOT_BLANK));
        if (status.equals(ValidStatus.ENABLED.getId())) {
            List<SecurityPolicy> securityPolicies = this.securityPolicyRepository.findAll(ids);
            int count;
            for (SecurityPolicy securityPolicy : securityPolicies) {
                count = this.securityPolicyRepository.countDuplicate(securityPolicy.getId(), securityPolicy.getSecurityGrade(), status);
                SecurityGrade securityGrade = SecurityGrade.fromId(securityPolicy.getSecurityGrade());
                EntityUtil.isNotDuplicate(count == 0, String.format("密级“%s”的安全策略，已启用，不能重复启用。", securityGrade.getDisplayName()));
            }
        }

        this.commonDomainService.updateStatus(SecurityPolicy.class, ids, status);
    }

    @Override
    @Transactional
    public void deleteSecurityPolicies(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.IDS_NOT_BLANK));
        List<SecurityPolicy> securityPolicies = securityPolicyRepository.findAll(ids);
        securityPolicyRepository.delete(securityPolicies);
    }

    @Override
    public SecurityPolicy loadSecurityPolicy(String id) {
        Assert.notNull(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        return this.securityPolicyRepository.findOne(id);
    }

    @Override
    public Map<String, Object> sliceQuerySecurityPolicies(SecurityPoliciesQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "securitypolicy");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void savePersonAccount(PersonAccount personAccount) {
        personAccountApplication.savePersonAccount(personAccount);
    }

    @Override
    public PersonAccount loadPersonAccountByLoginName(String loginName) {
        return personAccountApplication.loadPersonAccountByLoginName(loginName);
    }

    @Override
    @Transactional
    public PersonAccount loadAndInitPersonAccountByLoginName(String loginName, String fullId) {
        return personAccountApplication.loadAndInitPersonAccountByLoginName(loginName, fullId);
    }

    @Override
    public List<PersonLoginLimitDesc> queryPersonLoginLimitsByLoginName(String loginName) {
        return personLoginLimitApplication.queryPersonLoginLimitsByLoginName(loginName);
    }

    @Override
    public SecurityPolicy findSecurityGrade(String securityGrade, Integer status) {
        Assert.hasText(securityGrade, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "securityGrade"));
        Assert.notNull(status, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "status"));
        List<SecurityPolicy> list = this.securityPolicyRepository.findBySecurityGradeAndStatus(securityGrade, status);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
