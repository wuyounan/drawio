package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;
import com.huigou.uasp.bmp.opm.domain.query.OrgTypeQueryRequest;

/**
 * 组织类型服务
 * 
 * @author gongmm
 */
public interface OrgTypeApplication {
    /**
     * 查询文件配置地址
     */
    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/opm.xml";

    /**
     * 保存组构类型
     * 
     * @param orgType
     *            组织机构类型
     * @return
     */
    String saveOrgType(OrgType orgType);

    /**
     * 删除组织类型
     * 
     * @param ids
     *            组织类型ID列表
     */
    void deleteOrgTypes(List<String> ids);

    /**
     * 更新排序号
     * 
     * @param orgTypes
     *            组织类型ID和排序号的Map
     */
    void updateOrgTypeSequence(Map<String, Integer> orgTypes);

    /**
     * 移动组织类型
     * 
     * @param ids
     *            组织类型ID列表
     * @param folderId
     *            文件夹ID
     */
    void moveOrgType(List<String> ids, String folderId);

    /**
     * 从数据库读取一个组织类型数据
     * 
     * @param id
     *            组织类型ID
     */
    OrgType loadOrgType(String id);

    /**
     * 分页查询组织类型
     * 
     * @param OrgTypeQueryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryOrgTypes(OrgTypeQueryRequest queryRequest);

    /**
     * 得到下一个排序号
     * 
     * @return
     */
    Integer getNextSequence(String folderId);
}
