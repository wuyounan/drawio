package com.huigou.uasp.bmp.doc.attachment.application.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.jdbc.util.BatchSqlUpdateDetail;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentApplication;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentQueryApplication;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.doc.attachment.domain.model.AttachmentConfiguration;
import com.huigou.uasp.bmp.doc.attachment.domain.query.AttachmentConfigurationDesc;
import com.huigou.uasp.bmp.doc.attachment.repository.AttachmentConfigurationRepository;
import com.huigou.uasp.bmp.doc.attachment.repository.AttachmentRepository;
import com.huigou.util.CommonUtil;
import com.huigou.util.DateUtil;
import com.huigou.util.FileHelper;
import com.huigou.util.StringUtil;

@Service("attachmentApplication")
public class AttachmentApplicationImpl extends BaseApplication implements AttachmentApplication, AttachmentQueryApplication {

    @Autowired
    private AttachmentConfigurationRepository configurationRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    @Transactional
    public String saveAttachmentConfiguration(AttachmentConfiguration attachmentConfiguration) {
        Assert.notNull(attachmentConfiguration, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "attachmentConfiguration"));
        attachmentConfiguration = (AttachmentConfiguration) this.commonDomainService.loadAndFillinProperties(attachmentConfiguration);
        attachmentConfiguration.buildDetails();
        attachmentConfiguration = (AttachmentConfiguration) this.commonDomainService.saveBaseInfoWithFolderEntity(attachmentConfiguration,
                                                                                                                  configurationRepository);

        return attachmentConfiguration.getId();
    }

    @Override
    public AttachmentConfiguration loadAttachmentConfiguration(String id) {
        checkIdNotBlank(id);
        return this.configurationRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteAttachmentConfigurations(List<String> ids) {
        this.checkIdsNotEmpty(ids);

        List<AttachmentConfiguration> attachmentConfigurations = this.configurationRepository.findAll(ids);
        Assert.isTrue(ids.size() == attachmentConfigurations.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "附件配置"));

        this.configurationRepository.delete(attachmentConfigurations);
    }

    @Override
    @Transactional
    public void moveAttachmentConfigurations(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(AttachmentConfiguration.class, ids, folderId);
    }

    @Override
    public Map<String, Object> slicedQueryAttachmentConfigurations(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachmentConfiguration");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public void deleteAttachmentConfigurationDetails(String attachmentConfigurationId, List<String> ids) {
        Assert.hasText(attachmentConfigurationId, "参数attachmentConfigurationId不能为空。");
        this.checkIdsNotEmpty(ids);

        AttachmentConfiguration attachmentConfiguration = this.configurationRepository.findOne(attachmentConfigurationId);
        attachmentConfiguration.removeDetails(ids);

        this.configurationRepository.save(attachmentConfiguration);
    }

    @Override
    public Map<String, Object> slicedQueryAttachmentConfigurationDetails(ParentIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachmentConfigurationDetails");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public String saveAttachment(Attachment attachment) {
        Assert.notNull(attachment, "参数attachment不能为空。");
        attachment.checkConstraints();
        // 判断是否允许多个文件
        this.checkNeedMoreFile(attachment);
        attachment = this.attachmentRepository.save(attachment);
        return attachment.getId();
    }

    /**
     * 判断是否允许多个文件
     * 
     * @param attachment
     */
    private void checkNeedMoreFile(Attachment attachment) {
        String bizCode = attachment.getBizKindId();
        String attachmentCode = attachment.getBizSubKindId();
        if (StringUtil.isBlank(bizCode) || StringUtil.isBlank(attachmentCode)) {
            // 如果编码为空则不进行判断
            return;
        }
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachmentConfigurationDetails");
        int isMoreFlag = 0;
        String isMore = attachment.getIsMore();
        if (StringUtil.isBlank(isMore)) {
            String sql = queryDescriptor.getSqlByName("checkNeedcMoreFile");
            isMoreFlag = this.sqlExecutorDao.queryToInt(sql, bizCode, attachmentCode);
        } else {
            try {
                isMoreFlag = Integer.parseInt(isMore);
            } catch (Exception e) {
                isMoreFlag = 0;
            }
        }
        if (isMoreFlag > 0) { // 允许多个文件则返回
            return;
        }
        this.deleteAttachmentsByBizIdAndSubKind(bizCode, attachment.getBizId(), attachmentCode, false);
    }

    @Override
    @Transactional
    public void deleteAttachment(String id, Boolean verifyCreator) {
        this.checkIdNotBlank(id);

        List<String> ids = new ArrayList<String>(1);
        ids.add(id);

        deleteAttachmentsByIds(ids, verifyCreator);
    }

    private void internalDeleteAttachments(List<Attachment> attachments, Boolean verifyCreator) {
        for (Attachment attachment : attachments) {
            if (verifyCreator) {
                Operator operator = ThreadLocalUtil.getOperator();
                if (!attachment.getCreator().getCreatedById().contains(operator.getUserId())) {
                    throw new ApplicationException("不能删除其他人上传的文件。");
                }
            }
            attachment.setStatus(Attachment.Status.DELETED.getId());
        }

        this.attachmentRepository.save(attachments);
    }

    @Override
    @Transactional
    public void deleteAttachmentsByIds(List<String> ids, Boolean verifyCreator) {
        this.checkIdsNotEmpty(ids);
        List<Attachment> attachments = this.attachmentRepository.findAll(ids);

        Assert.notNull(ids.size() == attachments.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "附件"));

        internalDeleteAttachments(attachments, verifyCreator);
    }

    @Override
    @Transactional
    public void deleteAttachmentsByBizId(String bizKindId, String bizId, boolean verifyCreator) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        List<Attachment> attachments = this.attachmentRepository.findValidAttachments(bizKindId, bizId);
        internalDeleteAttachments(attachments, verifyCreator);
    }

    private void deleteAttachmentsByBizIdAndSubKind(String bizKindId, String bizId, String bizSubKindId, boolean verifyCreator) {
        Assert.hasText(bizId, "参数bizId不能为空。");
        List<Attachment> attachments = this.attachmentRepository.findValidAttachments(bizKindId, bizId, bizSubKindId);
        internalDeleteAttachments(attachments, verifyCreator);
    }

    @Override
    public Attachment loadAttachment(String id) {
        this.checkIdNotBlank(id);
        return this.attachmentRepository.findOne(id);
    }

    @Override
    public void updateAttachmentsSequence(Map<String, Integer> params) {
        Assert.notEmpty(params, "参数params不能为空。");
        this.commonDomainService.updateSequence(Attachment.class, params);
    }

    @Override
    public List<Attachment> queryAttachments(String bizKindId, String bizId) {
        Assert.hasText(bizKindId, "参数bizKindId不能为空。");
        Assert.hasText(bizId, "参数bizId不能为空。");

        List<Attachment> attachments = this.attachmentRepository.findValidAttachments(bizKindId, bizId);
        return attachments;
    }

    private List<AttachmentConfigurationDesc> queryConfigurationDescsByCode(String code) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachmentConfigurationDetails");
        String sql = queryDescriptor.getSqlByName("queryByBizCode");
        List<AttachmentConfigurationDesc> result = this.sqlExecutorDao.queryToList(sql, AttachmentConfigurationDesc.class, code);
        return result;
    }

    private List<AttachmentConfigurationDesc> buildGroupedAttachment(String bizKindId, List<Attachment> attachments) {
        // 查询附件分类配置
        List<AttachmentConfigurationDesc> descs = queryConfigurationDescsByCode(bizKindId);
        if (descs == null) {
            return null;
        }
        if (attachments != null) {
            String bizSubKindId = "";
            for (Attachment attachment : attachments) {
                for (AttachmentConfigurationDesc desc : descs) {
                    bizSubKindId = attachment.getBizSubKindId();
                    if (bizSubKindId == null) {
                        bizSubKindId = "other";
                    }
                    if (bizSubKindId.equals(desc.getCode())) {
                        desc.getAttachments().add(attachment);
                    }
                }
            }
        }
        return descs;
    }

    @Override
    public List<AttachmentConfigurationDesc> queryGroupedAttachments(String bizKindId, String bizId) {
        List<Attachment> attachments = null;
        if (bizId != null) {
            attachments = queryAttachments(bizKindId, bizId);
        }
        return buildGroupedAttachment(bizKindId, attachments);
    }

    @Override
    public boolean attachmentExists(String bizKindId, String bizSubKindId, String bizId) {
        return attachmentRepository.countValidAttachmentsBySubKindId(bizKindId, bizSubKindId, bizId) > 0;
    }

    @Override
    public boolean attachmentExists(String bizKindId, String bizId) {
        return attachmentRepository.countValidAttachments(bizKindId, bizId) > 0;
    }

    /**
     * 创建数据批量插入对象
     * 
     * @return
     */
    private BatchSqlUpdateDetail getBatchInsertDetail() {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachment");
        String insertSql = queryDescriptor.getSqlByName("insertAttachment");
        Map<String, Integer> paramType = new HashMap<String, Integer>();
        paramType.put("sequence", Types.NUMERIC);
        paramType.put("status", Types.NUMERIC);
        paramType.put("clearCache", Types.NUMERIC);
        paramType.put("createdDate", Types.TIMESTAMP);
        return BatchSqlUpdateDetail.newInstance(this.sqlExecutorDao.getDataSource(), insertSql, paramType);
    }

    @Override
    @Transactional
    public void copyAttachmentById(String fileId, String toBizCode, List<String> toBizIds, boolean newOperator, boolean isCopyFile) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachment");
        String sql = queryDescriptor.getSqlByName("loadById");
        Map<String, Object> map = this.sqlExecutorDao.queryToMap(sql, fileId);
        Assert.notEmpty(map, "文件不存在,可能被其他用户删除或修改！");
        BatchSqlUpdateDetail batchInsertDetail = this.getBatchInsertDetail();
        String path = null;
        Operator op = ThreadLocalUtil.getVariable("operator", Operator.class);
        for (String toBizId : toBizIds) {
            Map<String, Object> m = new HashMap<>();
            m.putAll(map);
            m.put("id", CommonUtil.createGUID());
            m.put("bizKindId", toBizCode);
            m.put("bizId", toBizId);
            if (isCopyFile) {
                path = FileHelper.copyFileByCode(m.get("path").toString(), toBizCode);
                Assert.hasText(path, "复制文件失败!");
                m.put("path", path);
            }
            if (newOperator) {
                m.put("createdById", op.getPersonMemberId());
                m.put("createdByName", op.getPersonMemberName());
                m.put("createdDate", DateUtil.getTimestamp());
            }
            batchInsertDetail.setRows(m);
        }
        batchInsertDetail.flush();
    }

    @Override
    @Transactional
    public void copyAttachmentById(String fileId, String toBizCode, String toBizId) {
        List<String> ids = new ArrayList<String>();
        ids.add(toBizId);
        this.copyAttachmentById(fileId, toBizCode, ids, true, false);
    }

    @Override
    @Transactional
    public void copyAttachment(String fromBizCode, String formBizId, String toBizCode, String toBizId, boolean newOperator, boolean isCopyFile) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "attachment");
        String sql = queryDescriptor.getSqlByName("queryByBizId");
        List<Map<String, Object>> objs = this.sqlExecutorDao.queryToListMap(sql, fromBizCode, formBizId);
        if (objs == null || objs.size() == 0) {
            return;
        }
        BatchSqlUpdateDetail batchInsertDetail = this.getBatchInsertDetail();
        String path = null;
        Operator op = ThreadLocalUtil.getVariable("operator", Operator.class);
        for (Map<String, Object> m : objs) {
            m.put("id", CommonUtil.createGUID());
            m.put("bizKindId", toBizCode);
            m.put("bizId", toBizId);
            if (isCopyFile) {
                path = FileHelper.copyFileByCode(m.get("path").toString(), toBizCode);
                Assert.hasText(path, "复制文件失败!");
                m.put("path", path);
            }
            if (newOperator) {
                m.put("createdById", op.getPersonMemberId());
                m.put("createdByName", op.getPersonMemberName());
                m.put("createdDate", DateUtil.getTimestamp());
            }
            batchInsertDetail.setRows(m);
        }
        batchInsertDetail.flush();
    }

    @Override
    @Transactional
    public void copyAttachment(String fromBizCode, String formBizId, String toBizCode, String toBizId) {
        this.copyAttachment(fromBizCode, formBizId, toBizCode, toBizId, true, false);
    }
}
