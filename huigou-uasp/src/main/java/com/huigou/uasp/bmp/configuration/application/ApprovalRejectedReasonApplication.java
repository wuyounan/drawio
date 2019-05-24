package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.configuration.domain.model.ApprovalRejectedReason;
import com.huigou.uasp.bmp.configuration.domain.query.ApprovalRejectedReasonQueryRequest;

/**
 * 审批驳回理由
 * 
 * @author xx
 * @date 2017-09-11 11:16
 */
public interface ApprovalRejectedReasonApplication {

    /**
     * 保存 审批驳回理由维护
     * 
     * @author xx
     * @param params
     */
    public String saveApprovalRejectedReason(ApprovalRejectedReason approvalRejectedReason);

    /**
     * 加载 审批驳回理由维护
     * 
     * @author xx
     * @return SDO
     */
    public ApprovalRejectedReason loadApprovalRejectedReason(String id);

    /**
     * 删除 审批驳回理由维护
     * 
     * @author xx
     */
    public void deleteApprovalRejectedReason(List<String> ids);

    /**
     * 查询 审批驳回理由维护
     * 
     * @author xx
     * @return SDO
     */
    public Map<String, Object> slicedQueryApprovalRejectedReason(ApprovalRejectedReasonQueryRequest queryRequest);

    /**
     * 显示序号
     * 
     * @param params
     */
    public void updateApprovalRejectedReasonSequence(Map<String, Integer> params);

    /**
     * 移动 审批驳回理由维护
     * 
     * @author xx
     */
    public void moveApprovalRejectedReason(List<String> ids, String folderId);
}
