package com.huigou.uasp.bmp.securitypolicy.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.ApplicationSystemDesc;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.securitypolicy.application.ApplicationSystemApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.query.ApplicationSystemQueryRequest;
import com.huigou.uasp.bmp.securitypolicy.repository.ApplicationSystemRepository;
import com.huigou.uasp.log.domain.model.ApplicationSystem;
import com.huigou.util.StringUtil;

@Service("applicationSystemApplication")
public class ApplicationSystemApplicationImpl extends BaseApplication implements ApplicationSystemApplication {

    @Autowired
    private ApplicationSystemRepository applicationSystemRepository;

    @Override
    @Transactional
    public String insertApplicationSystem(ApplicationSystem applicationSystem) {
        Assert.notNull(applicationSystem, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        if (this.applicationSystemRepository.countByCode(applicationSystem.getCode()) > 0L) {
            throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
        }
        if (this.applicationSystemRepository.countByName(applicationSystem.getName()) > 0L) {
            throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
        if (this.applicationSystemRepository.countByClassPrefix(applicationSystem.getClassPrefix()) > 0L) {
            throw new ApplicationException("类名前缀不能重复");
        }
        applicationSystem = this.applicationSystemRepository.save(applicationSystem);
        return applicationSystem.getId();
    }

    @Override
    @Transactional
    public ApplicationSystem updateApplicationSystem(ApplicationSystem applicationSystem) {
        Assert.notNull(applicationSystem, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        applicationSystem = this.commonDomainService.loadAndFillinProperties(applicationSystem, ApplicationSystem.class);
        if (this.applicationSystemRepository.countByCodeAndId(applicationSystem.getCode(), applicationSystem.getId()) > 0L) {
            throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
        }
        if (this.applicationSystemRepository.countByNameAndId(applicationSystem.getName(), applicationSystem.getId()) > 0L) {
            throw new ApplicationException(MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
        if (this.applicationSystemRepository.countByClassPrefixAndId(applicationSystem.getClassPrefix(), applicationSystem.getId()) > 0L) {
            throw new ApplicationException("类名前缀不能重复。");
        }
        return this.applicationSystemRepository.save(applicationSystem);
    }

    @Override
    public ApplicationSystem loadApplicationSystem(String id) {
        Assert.notNull(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.applicationSystemRepository.findOne(id);
    }

    @Override
    public void updateApplicationSystemsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(ApplicationSystem.class, params);
    }

    @Override
    @Transactional
    public Integer getApplicationSystemNextSequence() {
        return this.commonDomainService.getNextSequence(ApplicationSystem.class);
    }

    @Override
    @Transactional
    public void deleteApplicationSystems(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        List<ApplicationSystem> list = this.applicationSystemRepository.findAll(ids);
        this.applicationSystemRepository.delete(list);
    }

    @Override
    public ApplicationSystem queryApplicationSystem(String code, String classPrefix) {
        Assert.isTrue(!(StringUtil.isBlank(code) && StringUtil.isBlank(classPrefix)), "参数code和classPrefix不能同为空。");
        ApplicationSystem applicationSystem;
        if (StringUtil.isNotBlank(code)) {
            applicationSystem = this.applicationSystemRepository.findByCode(code);
        } else {
            applicationSystem = this.applicationSystemRepository.findByClassPrefix(classPrefix);
        }
        return applicationSystem;
    }

    @Override
    public Map<String, Object> sliceQueryApplicationSystems(ApplicationSystemQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "applicationSystem");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public List<ApplicationSystemDesc> queryAllDesc() {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "applicationSystemDesc");
        @SuppressWarnings("unchecked")
        List<ApplicationSystemDesc> result = this.generalRepository.query(queryDescriptor.getSql());
        return result;
    }

    @Override
    public List<ApplicationSystem> queryAll() {
        return applicationSystemRepository.findAll();
    }

}
