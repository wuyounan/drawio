package com.huigou.uasp.bmp.opm.application;

import java.util.Map;

import com.huigou.context.Operator;


/**
 * 认证服务
 * 
 * @author gongmm
 */
public interface AuthenticationApplication {
    
    /**
     * 设置操作员组织信息
     * 
     * @param operator
     *            操作员
     * @param personMemberId
     *            人员成员ID
     */
    void setOperatorContext(Operator operator, String personMemberId);

    /**
     * 登录
     * 
     * @param loginName
     *            用户名
     * @param password
     *            密码
     */
    Map<String, Object> login(String loginName, String password);

    /**
     * 根据人员成员ID创建操作员
     * 
     * @param personMemberId
     *            人员成员ID
     * @return
     */
    Operator createOperatorByPersonMemberId(String personMemberId);

    /**
     * 根据登录名创建操作员
     * 
     * @param loginName
     *            登录名
     * @return
     */
    Operator createOperatorByLoginName(String loginName);

    /**
     * 本地数据认证登录
     * 
     * @param loginName
     *            用户名
     * @param password
     *            密码
     * @return
     */
    Map<String, Object> loginFromErp(String loginName, String password);

    /**
     * 本地数据认证登录
     * 
     * @param loginName
     *            用户名
     * @return
     */

    Map<String, Object> loginFromErp(String loginName);

}
