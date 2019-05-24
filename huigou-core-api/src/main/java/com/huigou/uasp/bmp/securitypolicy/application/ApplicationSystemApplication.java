package com.huigou.uasp.bmp.securitypolicy.application;

import java.util.List;
import java.util.Map;

import com.huigou.cache.ApplicationSystemDesc;
import com.huigou.uasp.bmp.securitypolicy.domain.query.ApplicationSystemQueryRequest;
import com.huigou.uasp.log.domain.model.ApplicationSystem;

/**
 * 应用系统维护操作接口
 * 
 * @author yuanwf
 */
public interface ApplicationSystemApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/securitypolicy.xml";

    /**
     * 添加应用系统
     * 
     * @param applicationSystem
     *            应用系统实体
     * @return  应用系统实体ID
     */
    String insertApplicationSystem(ApplicationSystem applicationSystem);

    /**
     * 修改应用系统
     * 
     * @param applicationSystem
     *            应用系统实体
     * @return  应用系统实体
     */
    ApplicationSystem updateApplicationSystem(ApplicationSystem applicationSystem);

    /**
     * 加载应用系统
     * 
     * @param id
     *            应用系统实体ID
     * @return 应用系统实体
     */
    ApplicationSystem loadApplicationSystem(String id);

    /**
     * 删除应用系统
     * 
     * @param ids
     *            应用系统实体ID列表
     */
    void deleteApplicationSystems(List<String> ids);

    /**
     * 修改应用系统排序号
     * 
     * @param params
     *            应用系统实体ID和排序号的Map
     */
    void updateApplicationSystemsSequence(Map<String, Integer> params);

    /**
     * 获取应用系统下一排序号
     * 
     * @return
     */
    Integer getApplicationSystemNextSequence();

    /**
     * 查询应用系统
     * 
     * @param code
     *            编码
     * @param classPrefix
     *            类名前缀
     * @return
     */
    ApplicationSystem queryApplicationSystem(String code, String classPrefix);

    /**
     * 分页查询应用系统
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> sliceQueryApplicationSystems(ApplicationSystemQueryRequest queryRequest);
    
    /**
     * 查询所有数据
     * @return
     */
    List<ApplicationSystemDesc>  queryAllDesc();
    
    /**
     * 查询所有数据
     * @return
     */
    List<ApplicationSystem>  queryAll();

}
