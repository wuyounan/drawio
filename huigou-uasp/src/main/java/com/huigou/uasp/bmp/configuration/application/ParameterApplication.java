package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.configuration.domain.model.SysParameter;

/**
 * 系统参数服务
 * 
 * @author gongmm
 */
public interface ParameterApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";

    /**
     * 保存系统参数
     * 
     * @param sysParameter
     *            系统参数
     * @return
     */
    String saveSysParameter(SysParameter sysParameter);

    /**
     * 加载系统参数
     * 
     * @param id
     *            系统参数ID
     * @return
     */
    SysParameter loadSysParameter(String id);

    /**
     * 删除系统参数
     * 
     * @param ids
     *            系统参数ID列表
     */
    void deleteSysParameters(List<String> ids);

    /**
     * 移动系统参数
     * 
     * @param ids
     *            系统参数ID列表
     * @param foldeId
     *            文件夹ID
     */
    void moveSysParameters(List<String> ids, String foldeId);

    /**
     * 分页查询系统参数
     * 
     * @param folderId
     *            文件夹ID
     * @param code
     *            编码
     * @param name
     *            名称
     * @param queryModel
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQuerySysParameters(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 同步系统参数缓存
     */
    void syncCache();

    /**
     * 查询全部系统参数
     * 
     * @return
     */
    List<SysParameter> queryAll();

}
