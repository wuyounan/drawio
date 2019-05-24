package com.huigou.uasp.bmp.opm.impl;

import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.repository.GeneralRepository;

public class BaseApplication {

    protected CommonDomainService commonDomainService;

    protected SQLExecutorDao sqlExecutorDao;

    protected GeneralRepository generalRepository;
    

    public void setCommonDomainService(CommonDomainService commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    public void setSqlExecutorDao(SQLExecutorDao sqlExecutorDao) {
        this.sqlExecutorDao = sqlExecutorDao;
    }

    public void setGeneralRepository(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
    }
    
    
}
