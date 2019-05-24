package com.huigou.uasp.bmp.securitypolicy.application.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FullIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.domain.ValidStatus;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.repository.org.PersonRepository;
import com.huigou.uasp.bmp.securitypolicy.application.PersonAccountApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;
import com.huigou.uasp.bmp.securitypolicy.repository.PersonAccountRepository;

@Service("personAccountApplication")
public class PersonAccountApplicationImpl extends BaseApplication implements PersonAccountApplication {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonAccountRepository personAccountRepository;

    @Override
    public PersonAccount loadPersonAccountByLoginName(String loginName) {
        Assert.hasText(loginName, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "loginName"));
        return personAccountRepository.findByLoginName(loginName.toUpperCase());
    }

    @Override
    @Transactional
    public void savePersonAccount(PersonAccount personAccount) {
        Assert.notNull(personAccount, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "personAccount"));
        personAccountRepository.save(personAccount);
    }

    @Override
    @Transactional
    public PersonAccount loadAndInitPersonAccountByLoginName(String loginName, String fullId) {
        PersonAccount personAccount = loadPersonAccountByLoginName(loginName);
        if (personAccount == null) {
            personAccount = new PersonAccount();
            personAccount.setLoginName(loginName.toUpperCase());
            personAccount.setLastLoginDate(new Date());
            personAccount.setLastModifiedPasswordDate(new Date());
            personAccount.setStatus(PersonAccount.PersonAccountStatus.INIT.getId());
            personAccount.setFullId(fullId);
            personAccountRepository.save(personAccount);
        }
        return personAccount;
    }

    @Override
    @Transactional
    public void updatePersonAccountsStatus(List<String> ids, Integer status) {
        Assert.notEmpty(ids, "参数ids不能为空。");
        Assert.notNull(status, "参数status不能为空。");

        List<PersonAccount> personAccounts = this.personAccountRepository.findAll(ids);
        for (PersonAccount personAccount : personAccounts) {
            if (status.equals(ValidStatus.DISABLED.getId())) {
                personAccount.setLockedDate(new Date());
            } else {
                personAccount.setLockedDate(null);
            }
            personAccount.setStatus(status);
        }
        this.personAccountRepository.save(personAccounts);
    }

    @Override
    public Map<String, Object> sliceQueryPersonAccounts(FullIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "personAccount");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

}
