package com.huigou.uasp.bmp.opm.application.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.application.ManagementQueryApplication;
import com.huigou.uasp.bmp.opm.domain.query.BizManagementTypesQueryRequest;

@Service("managementQueryApplication")
public class ManagementQueryApplicationImpl extends BaseApplication implements ManagementQueryApplication {

    @Override
    public Map<String, Object> slicedQueryManagementByTypeId(BizManagementTypesQueryRequest queryRequest) {
        Assert.hasText(queryRequest.getParentId(), "管理权限ID不能为空!");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "managementQuery");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.setDefaultOrderBy("full_sequence asc");
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    public Map<String, Object> slicedPermissionBizManagementQuery(BizManagementTypesQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "managementQuery");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

}
