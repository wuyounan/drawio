package com.huigou.uasp.bmp.doc.attachment.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.doc.attachment.application.WebUploaderService;
import com.huigou.uasp.bmp.doc.attachment.domain.model.FileInfo;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.Constants;
import com.huigou.util.FileHelper;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

@Controller
@ControllerMapping("webUpload")
public class WebUploadController extends CommonController {

    @Autowired
    private WebUploaderService webUploaderService;

    protected String getPagePath() {
        return "/lib/webUploader/";
    }

    private String getBackurl() {
        String backurl = getParameter("backurl");
        return backurl;
    }

    /**
     * 重请求中读取文件信息
     *
     * @return
     */
    private FileInfo getFileInfo(SDO sdo) {
        FileInfo info = new FileInfo();
        info.setSize(sdo.getProperty("size", String.class));
        info.setName(sdo.getProperty("name", String.class));
        info.setId(sdo.getProperty("id", String.class));
        info.setLastModifiedDate(sdo.getProperty("lastModifiedDate", String.class));
        info.setType(sdo.getProperty("type", String.class));
        info.setExt(FileHelper.getFileExtName(info.getName()));
        info.setBizId(sdo.getProperty("bizId", String.class));
        info.setBizCode(sdo.getProperty("bizCode", String.class));
        info.setAttachmentCode(sdo.getProperty("attachmentCode", String.class));
        info.setUniqueName(sdo.getProperty("uniqueName", String.class));
        info.setIsMore(sdo.getProperty("isMore", String.class));
        info.setDeleteOld(sdo.getProperty("deleteOld", String.class));
        try {
            String chunkIndex = sdo.getProperty("chunkIndex", String.class);
            info.setChunkIndex(Integer.parseInt(chunkIndex));
        } catch (Exception e) {
        }
        try {
            String chunks = sdo.getProperty("chunks", String.class);
            info.setChunks(Integer.parseInt(chunks));
        } catch (Exception e) {
        }
        try {
            String chunk = sdo.getProperty("chunk", String.class);
            info.setChunk(Integer.parseInt(chunk));
        } catch (Exception e) {
        }
        return info;
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPLOAD, description = "上传文件")
    public String ajaxUpload() {
        SDO sdo = this.getUploadSDO();
        FileItem uploadItem = (FileItem) sdo.getProperty("uploadFileItem");
        if (uploadItem != null) {
            FileInfo info = this.getFileInfo(sdo);
            try {
                File target = webUploaderService.getReadySpace(info); // 为上传的文件准备好对应的位置
                if (target == null) {
                    return error("上传文件错误。");
                }
                uploadItem.write(target);
                // 没有分片 直接存文件
                if (info.getChunks() <= 0) {
                    String backurl = this.getBackurl();
                    if (StringUtil.isBlank(backurl)) {
                        String attachmentId = webUploaderService.saveFileMap(info, target);
                        Map<String, Object> map = info.toMap();
                        map.put("id", attachmentId);
                        return toResult(map);
                    } else {
                        Map<String, Object> param = info.toMap();
                        // 跳转到其他地址
                        sdo.putProperty("file", null);
                        param.put(Constants.SDO, sdo);
                        this.setSessionAttribute("AttachmentInfo", param);
                        this.getRequest().getRequestDispatcher(backurl).forward(this.getRequest(), this.getResponse());
                        return NONE;
                    }
                }
                return toResult("ok");
            } catch (Exception ex) {
                return error(ex);
            }
        }
        return error("上传文件错误,未找到文件对象。");
    }

    /**
     * 允许上传文件类型判断
     *
     * @param fileExt
     * @return
     */
    private boolean filterType(String fileExt) {
        // allowTypes 为空默认不判断文件类型
        String allowTypes = SystemCache.getParameter("uploadFileType", String.class);
        if (allowTypes == null || allowTypes.equals("")) return true;
        String[] types = allowTypes.split(",");
        for (String type : types) {
            if (fileExt.toLowerCase().endsWith(type.toLowerCase())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 获取文件唯一标示
     *
     * @return
     */
    public String md5Check() {
        FileInfo info = this.getFileInfo(this.getSDO());
        String fileExt = info.getExt();
        if (!filterType(fileExt)) {
            // "您要上传的文件类型不正确。"
            return error(MessageSourceContext.getMessage("common.attachment.check.type", SystemCache.getParameter(SystemCache.UPLOAD_FILE_TYPE, String.class)));
        }
        String uniqueFileName = info.getUniqueFileName();
        return toResult(uniqueFileName);
    }

    /**
     * 检查目标分片是否存在且完整
     *
     * @return
     */
    public String chunkCheck() {
        FileInfo info = this.getFileInfo(this.getSDO());
        if (webUploaderService.chunkCheck(info)) {
            return blank("{\"ifExist\": 1}");
        } else {
            return blank("{\"ifExist\": 0}");
        }
    }

    /**
     * 文件合并请求
     *
     * @return
     */
    public String chunksMerge() {
        SDO sdo = this.getSDO();
        FileInfo info = this.getFileInfo(sdo);
        try {
            File target = webUploaderService.chunksMerge(info);
            if (target == null) {
                throw new ApplicationException("文件上传失败。");
            }
            String backurl = this.getBackurl();
            if (StringUtil.isBlank(backurl)) {
                String fileId = webUploaderService.saveFileMap(info, target);
                Map<String, Object> map = info.toMap();
                map.put("id", fileId);
                return toResult(map);
            } else {
                Map<String, Object> param = info.toMap();
                // 跳转到其他地址
                param.put(Constants.SDO, sdo);
                this.setSessionAttribute("AttachmentInfo", param);
                this.getRequest().getRequestDispatcher(backurl).forward(this.getRequest(), this.getResponse());
                return NONE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 打开批量上传对话框
     *
     * @return
     */
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "批量上传文件")
    public String forwardBatchUpload() {
        SDO sdo = this.getSDO();
        String allowTypes = SystemCache.getParameter("uploadFileType", String.class);
        sdo.putProperty("uploadFileType", allowTypes);
        return forward("/lib/webUploader/batchUpload/batchUpload.jsp", sdo);
    }
}