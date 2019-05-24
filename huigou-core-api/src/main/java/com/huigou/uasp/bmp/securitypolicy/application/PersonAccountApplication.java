package com.huigou.uasp.bmp.securitypolicy.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.FullIdQueryRequest;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;

/**
 * 人员账号管理
 * 
 * @author yuanwf
 */
public interface PersonAccountApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/securitypolicy.xml";

    /**
     * 保存账号管理
     *
     * @param personAccount
     *            账号管理实体
     */
    void savePersonAccount(PersonAccount personAccount);

    /**
     * 根据登录名查找人员账号
     *
     * @param loginName
     *            登录名
     * @return
     */
    PersonAccount loadPersonAccountByLoginName(String loginName);

    /**
     * 根据登录名查找人员和初始化账号
     *
     * @param loginName
     *            登录名
     * @param fullId
     *            全路径
     * @return
     */
    PersonAccount loadAndInitPersonAccountByLoginName(String loginName, String fullId);

    /**
     * 是否锁定
     * 
     * @param ids
     *            人员账号ID列表
     * @param status
     *            状态
     */
    void updatePersonAccountsStatus(List<String> ids, Integer status);

    /**
     * 分页查询人员账号管理
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> sliceQueryPersonAccounts(FullIdQueryRequest queryRequest);
}
