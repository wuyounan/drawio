package com.huigou.uasp.bmp.doc.attachment.domain.query;

import java.util.ArrayList;
import java.util.List;

import com.huigou.context.MessageSourceContext;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;

public class AttachmentConfigurationDesc {
    private String bizKindId;

    private String id;

    private String detailId;

    private Integer allowDelete;

    private Integer allowMultiple;

    private String code;

    private String name;

    private String fileKind;

    private Integer colspan;

    private List<Attachment> attachments;

    public AttachmentConfigurationDesc() {
        this.fileKind = "";
        this.colspan = 1;
    }

    public AttachmentConfigurationDesc(String id, String detailId, Integer allowDelete, Integer allowMultiple, String code, String name, String fileKind,
                                       Integer colspan) {
        this.id = id;
        this.detailId = detailId;
        this.allowDelete = allowDelete;
        this.allowMultiple = allowMultiple;

        this.code = code;
        this.name = name;
        this.fileKind = fileKind == null ? "" : fileKind;
        this.colspan = colspan == null ? 1 : colspan;
    }

    public String getBizKindId() {
        return bizKindId;
    }

    public void setBizKindId(String bizKindId) {
        this.bizKindId = bizKindId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public Integer isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Integer allowDelete) {
        this.allowDelete = allowDelete;
    }

    public Integer getAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(Integer allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        /******** 加入获取国际化资源的方法 *********/
        // 组合出的key 如 attachment.document.circulateFile
        return MessageSourceContext.getMessageAsDefault(String.format("attachment.%s.%s", bizKindId, code), this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileKind() {
        if (fileKind == null) {
            return "";
        }
        return fileKind;
    }

    public void setFileKind(String fileKind) {
        this.fileKind = fileKind;
    }

    public Integer getColspan() {
        if (colspan == null || colspan <= 0) {
            return 1;
        }
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public List<Attachment> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<Attachment>();
        }
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
