package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;

public interface OrgTemplateApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 添加组织模板
     * 
     * @param parentId
     *            父节点ID
     * @param orgTypeIds
     *            组织机构类型ID列表
     */
    void insertOrgTemplates(String parentId, List<String> orgTypeIds);

    /**
     * 加载组织机构模板
     * 
     * @param id
     *            组织机构模板ID
     * @return
     */
    OrgTemplate loadOrgTemplate(String id);

    /**
     * 删除组织模板
     * 
     * @param ids
     *            组织模板ID列表
     */
    void deleteOrgTemplates(List<String> ids);

    /**
     * 更新排序号
     * 
     * @param params
     *            组织模板ID和排序号Map
     */
    void updateOrgTemplateSequence(Map<String, Integer> params);

    /**
     * 查询组织机构模板
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryOrgTemplates(ParentAndCodeAndNameQueryRequest queryRequest);

}
