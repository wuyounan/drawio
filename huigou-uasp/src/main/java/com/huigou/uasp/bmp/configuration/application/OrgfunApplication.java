package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.configuration.domain.model.Orgfun;
import com.huigou.uasp.bmp.configuration.domain.query.OrgfunQueryRequest;

/**
 * 组织机构函数记录维护
 * 
 * @author xx
 * @date 2018-03-09 10:47
 */
public interface OrgfunApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";

    /**
     * 保存 系统可用组织机构函数
     * 
     * @author xx
     * @param params
     */
    String saveOrgfun(Orgfun orgfun);

    /**
     * 加载 系统可用组织机构函数
     * 
     * @author xx
     * @return SDO
     */
    Orgfun loadOrgfun(String id);

    /**
     * 删除 系统可用组织机构函数
     * 
     * @author xx
     */
    void deleteOrgfun(List<String> ids);

    /**
     * 查询 系统可用组织机构函数
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> slicedQueryOrgfun(OrgfunQueryRequest queryRequest);

    List<Map<String, Object>> queryOrgfunByParentId(String parentId);

    void updateOrgfunSequence(Map<String, Integer> map);

    void updateOrgfunStatus(List<String> ids, Integer status);

    void moveOrgfun(List<String> ids, String parentId);
}
