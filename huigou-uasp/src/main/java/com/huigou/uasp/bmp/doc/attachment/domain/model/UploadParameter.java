package com.huigou.uasp.bmp.doc.attachment.domain.model;

import java.util.Date;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;

import com.huigou.cache.SystemCache;
import com.huigou.util.DateUtil;
import com.huigou.util.FileHelper;
import com.huigou.util.StringUtil;

/**
 * 附件上传参数
 * 
 * @author gongmm
 */
public class UploadParameter {

    public static final String DO_SAVE = "doSave";

    private FileItem uploadFileItem;

    private String uploadFileName;

    private String flag;

    private String bizCode;

    private String backurl;

    private String attachmentCode;

    private String bizId;

    private String isMore;

    private String deleteOld;

    private String returnPath;

    private boolean isBuilded;

    private String relativeFilePath;

    private String absoluteFilePath;

    private String newFileName;

    private String fileMaxSize;

    public FileItem getUploadFileItem() {
        return uploadFileItem;
    }

    public void setUploadFileItem(FileItem uploadFileItem) {
        this.uploadFileItem = uploadFileItem;
    }

    public long getUploadFileItemSize() {
        return uploadFileItem.getSize();
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getFileExt() {
        return FileHelper.getFileExtName(uploadFileName);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBackurl() {
        return backurl;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public String getAttachmentCode() {
        return attachmentCode;
    }

    public void setAttachmentCode(String attachmentCode) {
        this.attachmentCode = attachmentCode;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
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

    public String getReturnPath() {
        return returnPath;
    }

    public void setReturnPath(String returnPath) {
        this.returnPath = returnPath;
    }

    private boolean isAddDateDir() {
        String isAddDir = flag != null ? flag : "true";
        return "true".equals(isAddDir);
    }

    private void buildFilePathAndFileName() {
        if (!isBuilded) {
            isBuilded = true;
            StringBuffer sb = new StringBuffer();
            sb.append(FileHelper.FILE_SEPARATOR);
            sb.append(StringUtil.isBlank(bizCode) ? "temp" : bizCode);
            if (isAddDateDir()) {
                sb.append(FileHelper.FILE_SEPARATOR);
                sb.append(DateUtil.getDateFormat(new Date(), "yyyyMM"));
            }
            sb.append(FileHelper.FILE_SEPARATOR);
            this.relativeFilePath = sb.toString();
            this.absoluteFilePath = String.format("%s%s", FileHelper.getFileSavePath(), relativeFilePath);
            this.newFileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + "." + this.getFileExt();
        }

    }

    public String getRelativeFilePath() {
        buildFilePathAndFileName();
        return relativeFilePath;
    }

    public String getAbsoluteFilePath() {
        buildFilePathAndFileName();
        return absoluteFilePath;
    }

    public String getNewFileName() {
        buildFilePathAndFileName();
        return newFileName;
    }

    public String getRelativeFileFullName() {
        buildFilePathAndFileName();
        return relativeFilePath + newFileName;
    }

    public String getAbsoluteFileFullName() {
        buildFilePathAndFileName();
        return absoluteFilePath + newFileName;
    }

    public boolean deleteOld() {
        return "true".equals(deleteOld);
    }

    public boolean returnPath() {
        return "true".equals(returnPath);
    }

    public String getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(String fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public long getFileMaxSizeParameter() {
        String result;
        if (StringUtil.isBlank(fileMaxSize)) {
            result = SystemCache.getParameter("uploadFileSize", String.class);
            result = StringUtil.isNotBlank(result) ? result : "2";
        } else {
            result = fileMaxSize;
        }
        try {
            Integer size = Integer.parseInt(result);
            return size * 1024L * 1024L;
        } catch (NumberFormatException e) {
            return 1000000l;
        }
    }

    /**
     * 文件大小在限制范围内
     * 
     * @return
     */
    public boolean isFileSizeInLimit() {
        return getUploadFileItemSize() <= getFileMaxSizeParameter();
    }

    /**
     * 是否允许的附件类型
     * 
     * @return
     */
    public boolean isAllowedFileType() {
        String FileType = SystemCache.getParameter(SystemCache.UPLOAD_FILE_TYPE, String.class);
        if (FileType == null || FileType.equals("")) {
            return true;
        }

        String[] types = FileType.split(",");
        for (String type : types) {
            if (this.getFileExt().toLowerCase().endsWith(type.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
