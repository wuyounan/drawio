package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.configuration.domain.model.I18nproperties;
import com.huigou.uasp.bmp.configuration.domain.query.I18npropertiesQueryRequest;

/**
 * 数据库国际化资源读取
 * 
 * @author xx
 * @date 2017-09-29 10:23
 */
public interface I18npropertiesApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";

    /**
     * 保存 数据库数据国际化支持
     * 
     * @author xx
     * @param params
     */
    String saveI18nproperties(I18nproperties i18nproperties);

    /**
     * 加载 数据库数据国际化支持
     * 
     * @author xx
     * @return SDO
     */
    I18nproperties loadI18nproperties(String id);

    /**
     * 删除 数据库数据国际化支持
     * 
     * @author xx
     */

    void deleteI18nproperties(List<String> ids);

    /**
     * 查询 数据库数据国际化支持
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> slicedQueryI18nproperties(I18npropertiesQueryRequest queryRequest);

    /**
     * 移动
     * 
     * @param ids
     * @param folderId
     */
    void moveI18nProperties(List<String> ids, String folderId);

    /**
     * 资源初始化
     * 
     * @param folderId
     * @param resourcekind
     */
    void initI18nproperties(String folderId, String resourcekind);

    /**
     * 修改后同步缓存
     */
    void syncCache();

}
