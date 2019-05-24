package com.huigou.uasp.bmp.doc.attachment.domain.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;

/**
 * 附件配置
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_Attachment")
public class Attachment extends AbstractEntity {

    private static final long serialVersionUID = 8896755628851634664L;

    @Column(name = "biz_kind_id")
    private String bizKindId;

    @Column(name = "biz_sub_kind_id")
    private String bizSubKindId;

    @Column(name = "biz_id")
    private String bizId;

    @Column(name = "path")
    private String path;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "file_kind")
    private String fileKind;

    @Column(name = "clear_cache")
    private Integer clearCache;

    @Column(name = "upload_kind")
    private String uploadKind;

    private String remark;

    private Integer status;

    private Integer sequence;

    @Embedded
    private Creator creator;

    @Transient
    private String isMore;

    public String getBizKindId() {
        return bizKindId;
    }

    public void setBizKindId(String bizKindId) {
        this.bizKindId = bizKindId;
    }

    public String getBizSubKindId() {
        return bizSubKindId;
    }

    public void setBizSubKindId(String bizSubKindId) {
        this.bizSubKindId = bizSubKindId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileKind() {
        return fileKind == null ? "" : fileKind;
    }

    public void setFileKind(String fileKind) {
        this.fileKind = fileKind;
    }

    public Integer getClearCache() {
        return clearCache;
    }

    public void setClearCache(Integer clearCache) {
        this.clearCache = clearCache;
    }

    public String getUploadKind() {
        return uploadKind;
    }

    public void setUploadKind(String uploadKind) {
        this.uploadKind = uploadKind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getIsMore() {
        return isMore;
    }

    public void setIsMore(String isMore) {
        this.isMore = isMore;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public void checkConstraints() {
        super.checkConstraints();

        Assert.hasText(bizId, "业务ID不能为空。");
        Assert.hasText(bizKindId, "bizKindId不能为空。");

        Assert.hasText(path, "path不能为空。");
        Assert.hasText(fileName, "fileName不能为空。");
        // Assert.hasText(fileKind, "fileKind不能为空。");
        // 文件名超长判读
        int length = fileName.length();
        if (length > 120) {
            String cutFileName = String.format("%s.%s", fileName.substring(0, 100), this.getFileKind());
            this.setFileName(cutFileName);
        }

    }

    public void fromUploadParameter(UploadParameter uploadParameter) {
        this.bizKindId = uploadParameter.getBizCode();
        this.bizSubKindId = uploadParameter.getAttachmentCode();
        this.bizId = uploadParameter.getBizId();
        this.path = uploadParameter.getRelativeFileFullName();
        this.fileName = uploadParameter.getUploadFileName();
        this.fileKind = uploadParameter.getFileExt();
        this.isMore = uploadParameter.getIsMore();
        this.creator = Creator.newInstance();
        this.status = Attachment.Status.NORMAL.getId();
        this.uploadKind = Attachment.UploadKind.WEB.name();
    }

    public Map<String, Object> toMap() {
        Operator operator = ThreadLocalUtil.getOperator();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("bizCode", this.getBizKindId());
        param.put("bizId", this.getBizId());
        param.put("attachmentCode", this.getBizSubKindId());
        param.put("isMore", this.getIsMore());
        param.put("path", this.getPath());
        param.put("fileName", this.getFileName());
        param.put("fileSize", this.getFileSize());
        param.put("fileKind", this.getFileKind());
        param.put("isFtp", 0);
        if (operator != null) {
            param.put("createdById", operator.getPersonMemberId());
            param.put("createdByName", operator.getPersonMemberName());
        }
        param.put("createdDate", new Timestamp(System.currentTimeMillis()));
        return param;
    }

    public enum Status {

        NORMAL(1, "正常"), DELETED(0, "已删除");

        private final int id;

        private final String displayName;

        private Status(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String toString() {
            return String.valueOf(this.id);
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

    }

    public enum UploadKind {
        FTP, WEB
    }

}
