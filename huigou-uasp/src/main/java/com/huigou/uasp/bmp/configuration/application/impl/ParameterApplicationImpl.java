package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.codingrule.application.CodingGenerator;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.ParameterApplication;
import com.huigou.uasp.bmp.configuration.domain.model.SysParameter;
import com.huigou.uasp.bmp.configuration.repository.SysParameterRepository;

/**
 * 系统参数
 * 
 * @author gongmm
 */
@Service("parameterApplication")
public class ParameterApplicationImpl extends BaseApplication implements ParameterApplication {
    
    @Autowired
    private CodingGenerator codingGenerator;

    @Autowired
    private SysParameterRepository sysParameterRepository;

    @Override
    @Transactional
    public String saveSysParameter(SysParameter sysParameter) {
        Assert.notNull(sysParameter, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "sysParameter"));
        sysParameter = (SysParameter) this.commonDomainService.loadAndFillinProperties(sysParameter);
        sysParameter = (SysParameter) this.commonDomainService.saveBaseInfoWithFolderEntity(sysParameter, sysParameterRepository);
        return sysParameter.getId();
    }

    @Override
    public SysParameter loadSysParameter(String id) {
        this.checkIdNotBlank(id);
        return this.sysParameterRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteSysParameters(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<SysParameter> sysParameters = this.sysParameterRepository.findAll(ids);
        this.sysParameterRepository.delete(sysParameters);
    }

    @Override
    @Transactional
    public void moveSysParameters(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);
        this.commonDomainService.moveForFolder(SysParameter.class, ids, folderId);
    }

    @Override
    public Map<String, Object> slicedQuerySysParameters(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "sysParameter");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public List<SysParameter> queryAll() {
        return this.sysParameterRepository.findAll();
    }

    @Override
    public void syncCache() {
        List<SysParameter> parameterList = queryAll();
        SystemCache.removeParameter();
        for (SysParameter sysParameter : parameterList) {
            SystemCache.setParameter(sysParameter.getCode(), sysParameter.getValue());
        }
    }

}
