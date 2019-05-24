package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import com.huigou.util.SDO;

public interface TMAuthorizeApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 批量保存三员管理授权对象
     *
     * @param tmAuthorizes
     *            三员管理授权对象列表
     * @param subordinationId
     *            下属ID
     * @param systemId
     *            系统ID
     * @param roleKindId
     *            角色类别ID
     */
    void saveTMAuthorizes(List<TMAuthorize> tmAuthorizes, String subordinationId, String systemId, String roleKindId);

    /**
     * 查询三员管理授权对象列表
     *
     * @param subordinationId
     *            下属ID
     * @param managerId
     *            管理者ID
     * @return
     */
    Map<String, Object> queryTMAuthorizes(String subordinationId, String managerId);

    /**
     * TODO
     * 获取分级授权组织
     *
     * @param params
     *            请求参数
     * @param operator
     *            操作员对象
     * @return
     */
    Map<String, Object> queryDelegationOrgs(SDO params);
}
