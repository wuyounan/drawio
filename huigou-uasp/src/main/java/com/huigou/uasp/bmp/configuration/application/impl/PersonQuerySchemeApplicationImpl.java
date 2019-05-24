package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.PersonQuerySchemeApplication;
import com.huigou.uasp.bmp.configuration.domain.model.PersonQueryScheme;
import com.huigou.uasp.bmp.configuration.repository.PersonQuerySchemeRepository;

@Service("personQuerySchemeApplication")
public class PersonQuerySchemeApplicationImpl extends BaseApplication implements PersonQuerySchemeApplication {

    @Autowired
    private PersonQuerySchemeRepository personQuerySchemeRepository;

    @Override
    public String savePersonQueryScheme(PersonQueryScheme personQueryScheme) {
        Assert.notNull(personQueryScheme, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "personQueryScheme"));
        personQueryScheme = (PersonQueryScheme) this.commonDomainService.loadAndFillinProperties(personQueryScheme);
        if (personQueryScheme.isNew()) {
            personQueryScheme.setPersonId(ThreadLocalUtil.getOperator().getUserId());
            Integer sequence = this.personQuerySchemeRepository.getMaxSequence(personQueryScheme.getPersonId(), personQueryScheme.getKindId());
            personQueryScheme.setSequence(++sequence);
        }
        personQueryScheme = personQuerySchemeRepository.save(personQueryScheme);
        return personQueryScheme.getId();
    }

    @Override
    public PersonQueryScheme loadPersonQueryScheme(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.personQuerySchemeRepository.findOne(id);
    }

    @Override
    public Map<String, Object> queryPersonQuerySchemes(String personId, String kindId) {
        Assert.hasText(personId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "personId"));
        Assert.hasText(kindId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "kindId"));
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "personQueryScheme");
        String sql = queryDescriptor.getSqlByName("query");
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(sql);
        queryModel.putParam("personId", personId);
        queryModel.putParam("kindId", kindId);
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public void deletePersonQueryScheme(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        this.personQuerySchemeRepository.delete(id);
    }

}
