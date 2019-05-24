package com.huigou.uasp.bmp.securitypolicy.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.FullIdQueryRequest;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonLoginLimit;
import com.huigou.uasp.bmp.securitypolicy.domain.query.PersonLoginLimitDesc;

/**
 * 人员登录限制
 * 
 * @author yuanwf
 */
public interface PersonLoginLimitApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/securitypolicy.xml";

    /**
     * 添加人员登录限制
     * 
     * @param personId
     *            人员ID
     * @param machines
     *            机器密级集合
     * @return
     */
    void insertPersonLoginLimit(String personId, String fullId, List<Machine> machines);

    /**
     * 加载人员登录限制
     * 
     * @param id
     *            ID
     * @return
     */
    PersonLoginLimit loadPersonLoginLimit(String id);

    /**
     * 删除人员登录限制
     * 
     * @param ids
     *            ID集合
     */
    void deletePersonLoginLimits(List<String> ids);

    /**
     * 修改人员登录限制排序号
     * 
     * @param params
     */
    void updatePersonLoginLimitsSequence(Map<String, Integer> params);

    /**
     * 分页查询
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> sliceQueryPersonLoginLimits(FullIdQueryRequest queryRequest);

    /**
     * 获取下一排序号
     * 
     * @param personId
     *            人员ID
     * @return
     */
    int getPersonLoginLimitNextSequence(String personId);

    /**
     * 查询登录限制机器
     * 
     * @param loginName
     * @return
     */
    List<PersonLoginLimitDesc> queryPersonLoginLimitsByLoginName(String loginName);
}
