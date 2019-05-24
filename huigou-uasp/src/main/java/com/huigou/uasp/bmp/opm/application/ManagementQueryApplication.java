package com.huigou.uasp.bmp.opm.application;

import java.util.Map;

import com.huigou.uasp.bmp.opm.domain.query.BizManagementTypesQueryRequest;

/**
 * 业务权限查询
 * 
 * @author xx
 */
public interface ManagementQueryApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    Map<String, Object> slicedQueryManagementByTypeId(BizManagementTypesQueryRequest queryRequest);

    Map<String, Object> slicedPermissionBizManagementQuery(BizManagementTypesQueryRequest queryRequest);

}
