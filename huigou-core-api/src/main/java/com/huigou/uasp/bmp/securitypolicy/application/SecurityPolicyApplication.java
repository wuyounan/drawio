package com.huigou.uasp.bmp.securitypolicy.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;
import com.huigou.uasp.bmp.securitypolicy.domain.model.SecurityPolicy;
import com.huigou.uasp.bmp.securitypolicy.domain.query.PersonLoginLimitDesc;
import com.huigou.uasp.bmp.securitypolicy.domain.query.SecurityPoliciesQueryRequest;

/**
 * 安全策略验证器
 *
 * @author gongmm
 */
public interface SecurityPolicyApplication {

    String LOGIN_TIME_ATTRIBUTE = "_loginTime_";

    String PASSWORD_EXPIRED_ATTRIBUTE = "_passwordExpired_";

    String FIRST_LOGIN_UPDATE_PASSWORD_ATTRIBUTE = "_firstLoginUpdatePassword_";

    String UPDATE_PASSWORD_PROMPT_ATTRIBUTE = "_updatePasswordPrompt_";

    String PASSWORD_REMAINDER_DAYS_ATTRIBUTE = "_passwordRemainderDays_";

    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/securitypolicy.xml";

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
     * 根据登录名查询人员登录限制
     *
     * @param loginName
     *            登录名
     * @return
     */
    List<PersonLoginLimitDesc> queryPersonLoginLimitsByLoginName(String loginName);

    /**
     * 保存安全策略
     *
     * @param securityPolicy
     *            安全策略对象
     */
    void saveSecurityPolicy(SecurityPolicy securityPolicy);

    /**
     * 修改安全策略状态
     *
     * @param ids
     *            安全策略ID列表
     * @param status
     *            状态
     */
    void updateSecurityPoliciesStatus(List<String> ids, Integer status);

    /**
     * 删除安全策略
     *
     * @param ids
     *            安全策略ID列表
     */
    void deleteSecurityPolicies(List<String> ids);

    /**
     * 加载安全策略
     *
     * @param id
     *            安全策略ID
     * @return
     */
    SecurityPolicy loadSecurityPolicy(String id);

    /**
     * 查找安全策略
     *
     * @param securityGrade
     *            密级ID
     * @param status
     *            状态
     * @return
     */
    SecurityPolicy findSecurityGrade(String securityGrade, Integer status);

    /**
     * 分页查询安全策略
     *
     * @param queryRequest
     *            查询对象
     * @return 安全策略列表
     */
    Map<String, Object> sliceQuerySecurityPolicies(SecurityPoliciesQueryRequest queryRequest);
}
