package com.huigou.uasp.bpm;

import org.springframework.util.Assert;

/**
 * 流程定义工具类
 * 
 * @author gongmm
 */
public class ProcessDefinitionUtil {

    /**
     * 从流程定义ID得到流程Key
     * <p>
     * procDefId 格式 entryCheckApplyProc:3:42322(流程Key:版本:ID)
     * 
     * @param processDefinitionId
     *            流程定义ID
     * @return
     */
    public static String getProcessDefinitionKeyFromId(String processDefinitionId) {
        Assert.hasText(processDefinitionId, "参数processDefinitionId不能为空。");
        String[] splits = processDefinitionId.split(":");
        Assert.isTrue(splits.length == 3, "流程定义ID格式不正确。");
        return splits[0];
    }

}
