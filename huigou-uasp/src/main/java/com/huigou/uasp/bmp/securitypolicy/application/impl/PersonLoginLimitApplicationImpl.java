package com.huigou.uasp.bmp.securitypolicy.application.impl;

import java.util.ArrayList;
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
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.repository.org.PersonRepository;
import com.huigou.uasp.bmp.securitypolicy.application.PersonLoginLimitApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonLoginLimit;
import com.huigou.uasp.bmp.securitypolicy.domain.query.PersonLoginLimitDesc;
import com.huigou.uasp.bmp.securitypolicy.repository.PersonLoginLimitRepository;

@Service("personLoginLimitApplication")
public class PersonLoginLimitApplicationImpl extends BaseApplication implements PersonLoginLimitApplication {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonLoginLimitRepository personLoginLimitRepsoitory;

    @Override
    @Transactional
    public void insertPersonLoginLimit(String personId, String fullId, List<Machine> machines) {
        Assert.notNull(personId, "参数personId不能为空");
        Assert.notNull(machines, "参数machines不能为空");
        Person person = personRepository.findOne(personId);
        List<PersonLoginLimit> personLoginLimits = new ArrayList<PersonLoginLimit>();
        int sequence = getPersonLoginLimitNextSequence(personId);
        for (int i = 0; i < machines.size(); i++) {
            Machine machine = machines.get(i);
            PersonLoginLimit personLoginLimit = new PersonLoginLimit();
            personLoginLimit.setMachineId(machine.getId());
            personLoginLimit.setPersonId(personId);
            personLoginLimit.setLoginName(person.getLoginName());
            personLoginLimit.setSequence(sequence + i);
            personLoginLimit.setFullId(fullId);
            personLoginLimits.add(personLoginLimit);
        }
        this.personLoginLimitRepsoitory.save(personLoginLimits);
    }

    @Override
    public PersonLoginLimit loadPersonLoginLimit(String id) {
        Assert.notNull(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.personLoginLimitRepsoitory.findOne(id);
    }

    @Override
    @Transactional
    public void deletePersonLoginLimits(List<String> ids) {
        Assert.notEmpty(ids, "参数ids不能为空。");
        List<PersonLoginLimit> PersonLoginLimits = this.personLoginLimitRepsoitory.findAll(ids);
        this.personLoginLimitRepsoitory.delete(PersonLoginLimits);
    }

    @Override
    @Transactional
    public void updatePersonLoginLimitsSequence(Map<String, Integer> params) {
        Assert.notEmpty(params, "参数params不能为空。");
        this.commonDomainService.updateSequence(PersonLoginLimit.class, params);
    }

    @Override
    public Map<String, Object> sliceQueryPersonLoginLimits(FullIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "personLoginLimit");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public int getPersonLoginLimitNextSequence(String personId) {
        Assert.notNull(personId, "参数personId不能为空。");
        return this.commonDomainService.getNextSequence(PersonLoginLimit.class, "personId", personId);
    }

    @Override
    public List<PersonLoginLimitDesc> queryPersonLoginLimitsByLoginName(String loginName) {
        Assert.hasText(loginName, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "loginName"));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "personLoginLimit");
        String sql = queryDescriptor.getSqlByName("queryByLoginName");
        return this.sqlExecutorDao.queryToList(sql, PersonLoginLimitDesc.class, loginName);
    }
}
