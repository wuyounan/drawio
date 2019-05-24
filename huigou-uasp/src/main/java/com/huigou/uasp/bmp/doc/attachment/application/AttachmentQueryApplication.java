package com.huigou.uasp.bmp.doc.attachment.application;

import java.util.List;

import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.doc.attachment.domain.query.AttachmentConfigurationDesc;

public abstract interface AttachmentQueryApplication {

    /**
     * 查询附件
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizId
     *            业务ID
     * @return
     */
    List<Attachment> queryAttachments(String bizKindId, String bizId);

    /**
     * 查询分组的附件
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizId
     *            业务ID
     * @return
     */
    List<AttachmentConfigurationDesc> queryGroupedAttachments(String bizKindId, String bizId);
}