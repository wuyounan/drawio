package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.ApprovalRejectedReasonApplication;
import com.huigou.uasp.bmp.configuration.domain.model.ApprovalRejectedReason;
import com.huigou.uasp.bmp.configuration.domain.query.ApprovalRejectedReasonQueryRequest;
import com.huigou.uasp.bmp.configuration.repository.ApprovalRejectedReasonRepository;

/**
 * 驳回理由维护
 * 
 * @ClassName: ApprovalRejectedReasonApplicationImpl
 * @author xx
 * @date 2017-09-11 11:16
 * @version V1.0
 */
@Service("approvalRejectedReasonApplication")
public class ApprovalRejectedReasonApplicationImpl extends BaseApplication implements ApprovalRejectedReasonApplication {
    @Autowired
    private ApprovalRejectedReasonRepository approvalRejectedReasonRepository;

    @Override
    @Transactional
    public String saveApprovalRejectedReason(ApprovalRejectedReason approvalRejectedReason) {
        Assert.notNull(approvalRejectedReason, CommonDomainConstants.OBJECT_NOT_NULL);
        approvalRejectedReason = (ApprovalRejectedReason) this.commonDomainService.loadAndFillinProperties(approvalRejectedReason);
        if (approvalRejectedReason.isNew()) {
            approvalRejectedReason.setSequence(this.commonDomainService.getNextSequence(ApprovalRejectedReason.class));
        }
        approvalRejectedReason = approvalRejectedReasonRepository.save(approvalRejectedReason);
        return approvalRejectedReason.getId();
    }

    @Override
    public ApprovalRejectedReason loadApprovalRejectedReason(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return approvalRejectedReasonRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteApprovalRejectedReason(List<String> ids) {
        List<ApprovalRejectedReason> objs = approvalRejectedReasonRepository.findAll(ids);
        approvalRejectedReasonRepository.delete(objs);
    }

    @Override
    public Map<String, Object> slicedQueryApprovalRejectedReason(ApprovalRejectedReasonQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/configuration.xml", "approvalRejectedReason");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void updateApprovalRejectedReasonSequence(Map<String, Integer> params) {
        Assert.isTrue(params.size() > 0, "参数params不能为空。");
        this.commonDomainService.updateSequence(ApprovalRejectedReason.class, params);
    }

    @Override
    @Transactional
    public void moveApprovalRejectedReason(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);
        this.commonDomainService.moveForFolder(ApprovalRejectedReason.class, ids, folderId);
    }

}
