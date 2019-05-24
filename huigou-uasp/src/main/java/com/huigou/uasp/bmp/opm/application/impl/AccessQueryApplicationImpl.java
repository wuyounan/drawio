package com.huigou.uasp.bmp.opm.application.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.application.AccessQueryApplication;
import com.huigou.uasp.bmp.opm.domain.query.AuthorizedPermissionsByOrgFullIdQueryRequest;
import com.huigou.uasp.bmp.opm.domain.query.PermissionsQueryRequest;

@Service("accessQueryApplication")
public class AccessQueryApplicationImpl extends BaseApplication implements AccessQueryApplication {

    @Override
    public Map<String, Object> slicedQueryRolesByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryRolesByOrgFullId");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryAuthorizedPermissionsByOrgFullId(AuthorizedPermissionsByOrgFullIdQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        if (queryRequest.isPerson()) {
            QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryPersonAuthorizedPermissionsByOrgFullId");
            return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
        } else {
            QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryAuthorizedPermissionsByOrgFullId");
            return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
        }
    }

    @Override
    public Map<String, Object> slicedQueryRolesByPermission(PermissionsQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryRolesByPermissionId");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryRoleAuthorizeByPermission(PermissionsQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryAuthorizeByPermissionId");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryPersonsByPermission(PermissionsQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = null;
        if (queryRequest.isSingle()) {
            queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "querySinglePersonsByPermissionId");
        } else {
            queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryPersonsByPermissionId");
        }
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    public Map<String, Object> slicedQueryRoleByAuthorize(PermissionsQueryRequest queryRequest) {
        queryRequest.checkRoleConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryRoleByAuthorize");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryPersonAsRoleAuthorize(PermissionsQueryRequest queryRequest) {
        queryRequest.checkRoleConstraints();
        QueryDescriptor queryDescriptor = null;
        if (queryRequest.isSingle()) {
            queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "querySinglePersonAsRoleAuthorize");
        } else {
            queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryPersonAsRoleAuthorize");
        }
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

}
