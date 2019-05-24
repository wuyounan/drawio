package com.huigou.uasp.bmp.configuration.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.configuration.application.ApprovalRejectedReasonApplication;
import com.huigou.uasp.bmp.configuration.domain.model.ApprovalRejectedReason;
import com.huigou.uasp.bmp.configuration.domain.query.ApprovalRejectedReasonQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;

/**
 * 审批驳回理由维护
 * 
 * @ClassName: ApprovalRejectedReasonController
 * @author xx
 * @date 2017-09-11 11:16
 * @version V1.0
 */
@Controller
@ControllerMapping("approvalRejectedReason")
public class ApprovalRejectedReasonController extends CommonController {

    @Autowired
    private ApprovalRejectedReasonApplication approvalRejectedReasonApplication;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    public String forwardListApprovalRejectedReason() {
        SDO sdo = this.getSDO();
        sdo.putProperty("approvalRejectedReasonKind", sdo.getString("kind"));
        return forward("approvalRejectedReasonList", sdo);
    }

    public String slicedQueryApprovalRejectedReason() {
        SDO sdo = this.getSDO();
        ApprovalRejectedReasonQueryRequest queryRequest = sdo.toQueryRequest(ApprovalRejectedReasonQueryRequest.class);
        Map<String, Object> data = approvalRejectedReasonApplication.slicedQueryApprovalRejectedReason(queryRequest);
        return toResult(data);
    }

    public String showInsertApprovalRejectedReason() {
        return forward("approvalRejectedReasonDetail");
    }

    public String insertApprovalRejectedReason() {
        SDO sdo = this.getSDO();
        ApprovalRejectedReason approvalRejectedReason = sdo.toObject(ApprovalRejectedReason.class);
        String id = approvalRejectedReasonApplication.saveApprovalRejectedReason(approvalRejectedReason);
        return success(id);
    }

    public String showLoadApprovalRejectedReason() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        ApprovalRejectedReason approvalRejectedReason = approvalRejectedReasonApplication.loadApprovalRejectedReason(id);
        return forward("approvalRejectedReasonDetail", approvalRejectedReason);
    }

    public String updateApprovalRejectedReason() {
        SDO sdo = this.getSDO();
        ApprovalRejectedReason approvalRejectedReason = sdo.toObject(ApprovalRejectedReason.class);
        approvalRejectedReasonApplication.saveApprovalRejectedReason(approvalRejectedReason);
        return success();
    }

    public String deleteApprovalRejectedReason() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        approvalRejectedReasonApplication.deleteApprovalRejectedReason(ids);
        return success();
    }

    public String updateApprovalRejectedReasonSequence() {
        SDO params = this.getSDO();
        Map<String, Integer> data = params.getStringMap("data");
        this.approvalRejectedReasonApplication.updateApprovalRejectedReasonSequence(data);
        return success();

    }

    public String moveApprovalRejectedReason() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getProperty(FOLDER_ID_KEY_NAME, String.class);
        approvalRejectedReasonApplication.moveApprovalRejectedReason(ids, folderId);
        return success();
    }

}
