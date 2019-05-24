package com.huigou.uasp.bmp.operator;

import java.util.List;
import java.util.Map;

public interface OperatorUIElementPermissionBuilder {

    /**
     * 根据功能ID或编码查询当前用户的界面元素权限
     * 
     * @author
     * @param function
     * @param operator
     * @param isId
     *            void
     */
    List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId);

    /**
     * 查询当前处理环节的界面元素权限
     * 
     * @param procUnitHandlerId
     *            流程环节ID
     * @return
     */
    List<Map<String, Object>> queryUIElementPermissionsByProcUnitHandlerId(String procUnitHandlerId);

    /**
     * 查询当前处理环节的界面元素权限
     * 
     * @param taskId
     *            任务ID
     * @return
     */
    List<Map<String, Object>> queryUIElementPermissionsByTaskId(String taskId);

}
