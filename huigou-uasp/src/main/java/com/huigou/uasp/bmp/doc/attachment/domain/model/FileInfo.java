package com.huigou.uasp.bmp.doc.attachment.domain.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.FileHelper;

/**
 * webUploader附件文件对象
 * 
 * @author gongmm
 */
public class FileInfo {

    private int chunkIndex;

    private String size;

    private String name;

    private String id;

    private int chunks;

    private int chunk;

    private String lastModifiedDate;

    private String type;

    private String ext;

    private String bizId;

    private String bizCode;

    private String attachmentCode;

    private String isMore;

    private String deleteOld;

    private String savePath;

    private String uniqueName;

    public FileInfo() {
        bizCode = "";
        attachmentCode = "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getAttachmentCode() {
        return attachmentCode;
    }

    public void setAttachmentCode(String attachmentCode) {
        this.attachmentCode = attachmentCode;
    }

    public String getIsMore() {
        return isMore;
    }

    public void setIsMore(String isMore) {
        this.isMore = isMore;
    }

    public String getDeleteOld() {
        return deleteOld;
    }

    public void setDeleteOld(String deleteOld) {
        this.deleteOld = deleteOld;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public boolean deleteOld() {
        return "true".equals(this.deleteOld);
    }

    private String md5(String content) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));
            byte[] tmpFolder = md5.digest();
            for (int i = 0; i < tmpFolder.length; i++) {
                sb.append(Integer.toString((tmpFolder[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getUniqueFileName() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getBizId());
        sb.append(this.getBizCode());
        sb.append(this.getAttachmentCode());
        sb.append(this.getName());
        sb.append(this.getType());
        sb.append(this.getLastModifiedDate());
        sb.append(this.getSize());
        String s = this.md5(sb.toString());
        return s;
    }

    public Map<String, Object> toMap() {
        Long size = ClassHelper.convert(this.getSize(), Long.class);
        String fileExt = FileHelper.getFileExtName(this.getName());
        Operator operator = ThreadLocalUtil.getOperator();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("bizCode", this.getBizCode());
        param.put("bizId", this.getBizId());
        param.put("attachmentCode", this.getAttachmentCode());
        param.put("isMore", this.getIsMore());
        param.put("deleteOld", this.getDeleteOld());
        param.put("path", this.getSavePath());
        param.put("fileName", this.getName());
        param.put("fileSize", FileHelper.formetFileSize(size));
        param.put("fileKind", fileExt);
        param.put("fileLength", size);
        param.put("isFtp", 0);
        if (operator != null) {
            param.put("createdById", operator.getPersonMemberId());
            param.put("createdByName", operator.getPersonMemberName());
        }
        param.put("createdDate", new Timestamp(System.currentTimeMillis()));
        return param;
    }

    public String toString() {
        return "name=" + this.name + "; size=" + this.size + "; chunkIndex=" + this.chunkIndex + "; id=" + this.id + "; chunks=" + this.chunks + "; chunk="
               + this.chunk + "; lastModifiedDate=" + this.lastModifiedDate + "; type=" + this.type + "; ext=" + this.ext;
    }
}
