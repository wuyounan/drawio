package com.huigou.uasp.bmp.operator;

import com.huigou.context.Operator;

public interface OperatorApplication {
    /**
     * 根据登录账号创建 Operator
     * 
     * @param loginName
     * @return
     */
    Operator createOperatorByLoginName(String loginName);

    /**
     * 根据组织成员 id创建 Operator
     * 
     * @param personMemberId
     * @return
     */
    Operator createOperatorByPersonMemberId(String personMemberId);

    /**
     * 根据人员id创建 Operator
     * 
     * @param personId
     * @return
     */
    Operator createOperatorByPersonId(String personId);
}
