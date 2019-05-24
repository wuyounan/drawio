package com.huigou.uasp.bmp.common.application;

import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

public class BaseApplication extends AbstractApplication {

    @Autowired
    protected CommonDomainService commonDomainService;

    @Autowired
    protected SQLExecutorDao sqlExecutorDao;

    @Autowired
    protected GeneralRepository generalRepository;

    protected String getTenantId() {
        return ThreadLocalUtil.getOperator().getTenantId();
    }

    protected Operator getOperator() {
        Operator opr = ThreadLocalUtil.getOperator();
        if (opr == null) {
            opr = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class).getOperator();
        }
        return opr;
    }

}
