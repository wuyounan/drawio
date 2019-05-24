package com.huigou.uasp.bmp.doc.attachment.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.annotation.SkipAuth;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentApplication;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.doc.attachment.domain.model.AttachmentConfiguration;
import com.huigou.uasp.bmp.doc.attachment.domain.model.AttachmentConfigurationDetail;
import com.huigou.uasp.bmp.doc.attachment.domain.model.UploadParameter;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.exception.PageForwardException;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.Constants;
import com.huigou.util.ContentTypeHelper;
import com.huigou.util.FileHelper;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 附件管理
 *
 * @author gongmm
 */

@Controller
@ControllerMapping("attachment")
public class AttachmentController extends CommonController {

    private static final String ATTACHMENT_CONFIGURATION_ID_KEY_NAME = "attachmentConfigurationId";

    private static final String LIST_PAGE = "AttachmentConfiguration";

    private static final String DETAIL_PAGE = "AttachmentConfigurationDetail";

    @Autowired
    private AttachmentApplication application;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    @RequiresPermissions("AttachmentConfiguration:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到附件配置列表页面")
    public String forward() {
        return forward(LIST_PAGE);
    }

    @RequiresPermissions("AttachmentConfiguration:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加附件配置明细页面")
    public String showInsert() {
        return forward(DETAIL_PAGE);
    }

    @RequiresPermissions("AttachmentConfiguration:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加附件配置")
    public String insert() {
        SDO params = this.getSDO();
        AttachmentConfiguration attachmentConfiguration = params.toObject(AttachmentConfiguration.class);
        List<AttachmentConfigurationDetail> inputDetails = params.getList("detailData", AttachmentConfigurationDetail.class);
        attachmentConfiguration.setInputDetails_(inputDetails);
        attachmentConfiguration.addUpdateFields_(AbstractEntity.INPUT_DETAILS_FIELD_NAME);
        attachmentConfiguration.setStatus(BaseInfoStatus.ENABLED.getId());
        String id = application.saveAttachmentConfiguration(attachmentConfiguration);
        return success(id);
    }

    @RequiresPermissions("AttachmentConfiguration:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改附件配置明细页面")
    public String showUpdate() {
        SDO sdo = this.getSDO();
        String id = sdo.getString(ID_KEY_NAME);
        AttachmentConfiguration attachmentConfiguration = application.loadAttachmentConfiguration(id);
        return forward(DETAIL_PAGE, attachmentConfiguration);
    }

    @RequiresPermissions("AttachmentConfiguration:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改附件配置")
    public String update() {
        SDO sdo = this.getSDO();

        AttachmentConfiguration attachmentConfiguration = sdo.toObject(AttachmentConfiguration.class);
        List<AttachmentConfigurationDetail> inputDetails = sdo.getList("detailData", AttachmentConfigurationDetail.class);
        attachmentConfiguration.setInputDetails_(inputDetails);
        attachmentConfiguration.addUpdateFields_(AbstractEntity.INPUT_DETAILS_FIELD_NAME);
        application.saveAttachmentConfiguration(attachmentConfiguration);
        return success();
    }

    @RequiresPermissions("AttachmentConfiguration:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "移动附件配置")
    public String move() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getString(FOLDER_ID_KEY_NAME);
        application.moveAttachmentConfigurations(ids, folderId);
        return success();
    }

    @RequiresPermissions("AttachmentConfiguration:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除附件配置")
    public String delete() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        application.deleteAttachmentConfigurations(ids);
        return success();
    }

    @RequiresPermissions("AttachmentConfiguration:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除附件配置明细")
    public String deleteDetails() {
        SDO sdo = this.getSDO();
        String attachmentConfigurationId = sdo.getString(ATTACHMENT_CONFIGURATION_ID_KEY_NAME);
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        application.deleteAttachmentConfigurationDetails(attachmentConfigurationId, ids);
        return success();
    }

    @RequiresPermissions("AttachmentConfiguration:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询附件配置")
    public String slicedQuery() {
        SDO params = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = application.slicedQueryAttachmentConfigurations(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("AttachmentConfiguration:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询附件配置明细")
    public String slicedQueryDetails() {
        SDO params = this.getSDO();
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        queryRequest.setParentId(params.getString(ATTACHMENT_CONFIGURATION_ID_KEY_NAME));
        Map<String, Object> data = application.slicedQueryAttachmentConfigurationDetails(queryRequest);
        return toResult(data);
    }

    /**
     * 检查上传文件保存路径
     * 
     * @param uploadParameter
     * @return
     */
    private Map<String, Object> checkUploadRootDir(UploadParameter uploadParameter) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("status", true);
        String fileSavePath = FileHelper.getFileSavePath();
        File uploadDir = new File(fileSavePath);
        if (!uploadDir.exists() || !uploadDir.isDirectory()) {
            LogHome.getLog(this).error("上传文件夹不存在：" + fileSavePath);
            result.put("status", false);
            result.put("message", "上传文件夹" + fileSavePath + "不存在，请检查。");
        }
        // 检查目录写权限
        if (!uploadDir.canWrite()) {
            result.put("status", false);
            result.put("message", "上传目录没有写权限。");
        }
        return result;
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPLOAD, description = "上传文件")
    public String upload() {
        SDO sdo = null;
        try {
            sdo = this.getUploadSDO();
        } catch (Exception e) {
            return error(e.getMessage());
        }
        UploadParameter uploadParameter = sdo.toObject(UploadParameter.class);
        if (uploadParameter.getUploadFileItem() == null) {
            throw new ApplicationException("上传文件错误:未找到文件。");
        }
        if (!uploadParameter.isAllowedFileType()) {
            // 文件类型不正确，只能上传{0}类型文件!
            return error(MessageSourceContext.getMessage("common.attachment.check.type", SystemCache.getParameter(SystemCache.UPLOAD_FILE_TYPE, String.class)));
        }
        if (!uploadParameter.isFileSizeInLimit()) {
            // 选择的文件太大({0})无法上传!
            return error(MessageSourceContext.getMessage("common.attachment.filesize.error",
                                                         FileHelper.formetFileSize(uploadParameter.getFileMaxSizeParameter())));
        }

        Map<String, Object> checkResult = checkUploadRootDir(uploadParameter);
        if (checkResult.get("status").equals(false)) {
            return error(checkResult.get("message").toString());
        }

        File parentDir = new File(uploadParameter.getAbsoluteFilePath());
        if (!parentDir.exists() || !parentDir.isDirectory()) {
            parentDir.mkdirs();
        }

        File uploadFile = new File(uploadParameter.getAbsoluteFilePath(), uploadParameter.getNewFileName());
        try {
            uploadParameter.getUploadFileItem().write(uploadFile);
        } catch (Exception e) {
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
            LogHome.getLog(this).error("上传文件错误：", e);
            return error("上传文件失败。");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("bizCode", uploadParameter.getBizCode());
        result.put("attachmentCode", uploadParameter.getAttachmentCode());
        result.put("bizId", uploadParameter.getBizId());
        result.put("fileName", uploadParameter.getUploadFileName());
        result.put("fileKind", uploadParameter.getFileExt());
        if (uploadParameter.returnPath()) {
            result.put("path", uploadParameter.getRelativeFileFullName());
        }
        if (StringUtil.isNotBlank(uploadParameter.getBackurl())) {
            if (uploadParameter.getBackurl().equals(UploadParameter.DO_SAVE)) {
                try {
                    if (uploadParameter.deleteOld()) {
                        this.application.deleteAttachmentsByBizId(uploadParameter.getBizCode(), uploadParameter.getBizId(), false);
                    }

                    Attachment attachment = new Attachment();
                    attachment.fromUploadParameter(uploadParameter);
                    attachment.setFileSize(FileHelper.formetFileSize(uploadFile.length()));

                    String id = this.application.saveAttachment(attachment);
                    result.putAll(attachment.toMap());
                    result.put("error", 0);
                    result.put("id", id);
                    return toResult(result);
                } catch (Exception e) {
                    LogHome.getLog(this).error("上传文件保存错误:", e);
                    FileHelper.deleteFile(uploadParameter.getAbsoluteFileFullName());
                    return error("上传文件保存错误:" + e.getMessage());
                }
            } else {// 跳转到其他地址
                sdo.putProperty("uploadFileItem", null);
                result.put(Constants.SDO, sdo);
                this.setSessionAttribute("AttachmentInfo", result);
                try {
                    this.getRequest().getRequestDispatcher(uploadParameter.getBackurl()).forward(this.getRequest(), this.getResponse());
                } catch (ServletException e) {
                    throw new PageForwardException(e);
                } catch (IOException e) {
                    throw new PageForwardException(e);
                }
                return NONE;
            }
        } else {
            return toResult(result);
        }
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询附件")
    public String doQuery() {
        String bizKindId = this.getParameter(BIZ_CODE_KEY_NAME);
        String bizId = this.getParameter(BIZ_ID_KEY_NAME);
        List<Attachment> attachments = this.application.queryAttachments(bizKindId, bizId);
        return success(attachments);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除附件")
    public String doDelete() {
        String isCheck = this.getParameter("isCheck");
        String id = this.getParameter(ID_KEY_NAME);
        this.application.deleteAttachment(id, Boolean.valueOf(isCheck));
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "更新附件排序号")
    public String updateAttachmentsSequence() {
        Map<String, Integer> params = this.getSDO().getStringMap("data");
        this.application.updateAttachmentsSequence(params);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除业务单据所有附件")
    public String doDeleteAll() {
        String isCheck = this.getParameter("isCheck");
        String bizCode = this.getParameter(BIZ_CODE_KEY_NAME);
        String bizId = this.getParameter(BIZ_ID_KEY_NAME);
        this.application.deleteAttachmentsByBizId(bizCode, bizId, Boolean.valueOf(isCheck));
        return success();
    }

    @ControllerMethodMapping("/attachmentDownFile")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DOWNLOAD, description = "下载附件")
    public String downFile() {
        File file = null;
        String id = this.getParameter(ID_KEY_NAME);
        try {
            Attachment attachment = this.application.loadAttachment(id);
            file = FileHelper.getFile(attachment.getPath());
            if (file != null && file.exists()) {
                String contentType = ContentTypeHelper.getContentType(FileHelper.getFileExtName(file.getName()));
                this.downloadByBreakpoint(file, contentType, attachment.getFileName());
            } else {
                throw new ApplicationException("下载的文件不存在，可能被其他用户删除或修改。");
            }
        } catch (ApplicationException ea) {
            return alert(ea.getMessage());
        } catch (Exception e) {
            LogHome.getLog(this).error("下载文件时出错", e);
            return alert("下载文件时出错，请与管理员联系。");
        }
        return null;
    }

    @ControllerMethodMapping("/outputFile")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DOWNLOAD, description = "下载附件")
    public String outputFile() {
        File file = null;
        String id = this.getParameter(ID_KEY_NAME);
        try {
            Attachment attachment = this.application.loadAttachment(id);
            file = FileHelper.getFile(attachment.getPath());
            if (file != null && file.exists()) {
                this.outputFile(file, FileHelper.getFileExtName(file.getName()));
            } else {
                throw new ApplicationException("下载的文件不存在，可能被其他用户删除或修改。");
            }
        } catch (ApplicationException ea) {
            return alert(ea.getMessage());
        } catch (Exception e) {
            LogHome.getLog(this).error("下载文件时出错", e);
            return alert("下载文件时出错，请与管理员联系。");
        }
        return null;
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DOWNLOAD, description = "下载附件")
    public String downFileByTmpdir() {
        SDO sdo = this.getSDO();
        String temp = sdo.getString("file");
        String fileName = sdo.getString("fileName");
        if (StringUtil.isBlank(temp)) {
            return alert("下载文件名为空，请与管理员联系。");
        }
        File file = null;
        String fileExt = "";
        try {
            file = new File(FileHelper.getTmpdir(), temp);
            if (file != null && file.exists()) {
                fileExt = FileHelper.getFileExtName(file.getName());
                fileName = (fileName != null && !fileName.equals("")) ? fileName : System.currentTimeMillis() + "";
                fileName += "." + fileExt;
                String contentType = ContentTypeHelper.getContentType(fileExt);
                this.downloadByBreakpoint(file, contentType, fileName);
            } else {
                throw new ApplicationException("下载的文件不存在，可能被其他用户删除或修改。");
            }
        } catch (ApplicationException ea) {
            LogHome.getLog(this).error(ea);
            return alert(ea.getMessage());
        } catch (Exception e) {
            LogHome.getLog(this).error(e);
            return alert("下载文件时出错，请与管理员联系。");
        }
        return NONE;
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DOWNLOAD, description = "下载附件")
    public String downFileBySavePath() throws Exception {
        SDO sdo = this.getSDO();
        String temp = sdo.getString("file");
        String fileName = sdo.getString("fileName");
        if (StringUtil.isBlank(temp)) {
            return alert("下载文件名为空，请与管理员联系。");
        }
        File file = null;
        String fileExt = "";
        try {
            file = FileHelper.getFile(temp);
            if (file != null && file.exists()) {
                fileExt = FileHelper.getFileExtName(file.getName());
                fileName = (fileName != null && !fileName.equals("")) ? fileName : System.currentTimeMillis() + "";
                fileName += "." + fileExt;
                String contentType = ContentTypeHelper.getContentType(fileExt);
                this.downloadByBreakpoint(file, contentType, fileName);
            } else {
                throw new ApplicationException("下载的文件不存在，可能被其他用户删除或修改。");
            }
        } catch (ApplicationException ea) {
            LogHome.getLog(this).error(ea);
            return alert(ea.getMessage());
        } catch (Exception e) {
            LogHome.getLog(this).error(e);
            return alert("下载文件时出错，请与管理员联系。");
        }
        return NONE;
    }

    @SkipAuth
    @ControllerMethodMapping("/attachmentPreview")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.VIEW, description = "查看附件")
    public String forwardConvertView() {
        String bizId = this.getParameter(BIZ_ID_KEY_NAME);
        String bizCode = this.getParameter(BIZ_CODE_KEY_NAME);
        String id = this.getParameter(ID_KEY_NAME);
        File file = null;
        Attachment attachment = this.application.loadAttachment(id);
        file = FileHelper.getFile(attachment.getPath());
        if (file != null && file.exists()) {
            if (!StringUtil.isBlank(bizCode) && bizId != null) {
                List<Attachment> attachments = this.application.queryAttachments(bizCode, bizId);
                this.putAttribute("attachmentList", attachments);
            }
            this.putAttribute("attachmentId", id);
            this.putAttribute("fileName", attachment.getFileName());
            this.putAttribute("fileSize", attachment.getFileSize());
            this.putAttribute("attachmentKind", attachment.getFileKind());
            String convertUrl = SystemCache.getParameter("SYS.Convert.URL", String.class);
            this.putAttribute("convertUrl", convertUrl);
        } else {
            throw new ApplicationException("文件不存在，可能被其他用户删除或修改。");
        }

        return forward("/common/attachmentConvertView.jsp");
    }

}
