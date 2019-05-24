package com.huigou.uasp.tool.dataimport.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.domain.query.QueryPageRequest;
import com.huigou.data.excel.exporter.ExportExcel;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.uasp.tool.dataimport.application.ExcelImportApplication;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportLog;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportStatus;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportTemplate;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportTemplateDetail;
import com.huigou.util.FileHelper;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 数据导入
 *
 * @author gongmm
 */
@Controller
@ControllerMapping("excelImport")
public class ExcelImportController extends CommonController {

    private static final String EXCEL_IMPORT_TEMPLATE = "ExcelImportTemplate";

    private static final String EXCEL_IMPORT_TEMPLATE_DETAIL = "ExcelImportTemplateDetail";

    private static final String EXCEL_IMPORT_LOG = "ExcelImportLog";

    @Autowired
    private ExcelImportApplication excelImportApplication;

    protected String getPagePath() {
        return "/system/excelimport/";
    }

    @RequiresPermissions("ExcelImportTemplate:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到导入模板设置列表页面")
    public String forwardList() {
        return forward(EXCEL_IMPORT_TEMPLATE);
    }

    @RequiresPermissions("ExcelImportTemplate:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加导入模板明细页面")
    public String showInsertExcelImportTemplate() {
        return forward(EXCEL_IMPORT_TEMPLATE_DETAIL);
    }

    @RequiresPermissions("ExcelImportTemplate:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加导入模板")
    public String insertExcelImportTemplate() {
        SDO params = this.getSDO();
        ExcelImportTemplate excelImportTemplate = params.toObject(ExcelImportTemplate.class);
        List<ExcelImportTemplateDetail> details = params.getList("detailData", ExcelImportTemplateDetail.class);

        excelImportTemplate.setStatus(BaseInfoStatus.ENABLED.getId());
        excelImportTemplate.setDetails(details);

        String id = excelImportApplication.saveExcelImportTemplate(excelImportTemplate);

        return success(id);
    }

    @RequiresPermissions("ExcelImportTemplate:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到修改导入模板明细页面")
    public String showUpdateExcelImportTemplate() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        ExcelImportTemplate excelImportTemplate = excelImportApplication.loadExcelImportTemplate(id);
        return forward(EXCEL_IMPORT_TEMPLATE_DETAIL, excelImportTemplate);
    }

    @RequiresPermissions("ExcelImportTemplate:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改导入模板")
    public String updateExcelImportTemplate() {
        SDO params = this.getSDO();
        ExcelImportTemplate excelImportTemplate = params.toObject(ExcelImportTemplate.class);// this.excelImportApplication.loadExcelImportTemplate(id);
        List<ExcelImportTemplateDetail> inputDetails = params.getList("detailData", ExcelImportTemplateDetail.class);
        excelImportTemplate.setInputDetails_(inputDetails);
        excelImportTemplate.addUpdateFields_(AbstractEntity.INPUT_DETAILS_FIELD_NAME);
        excelImportApplication.saveExcelImportTemplate(excelImportTemplate);
        return success();
    }

    @RequiresPermissions("ExcelImportTemplate:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除导入模板")
    public String deleteExcelImportTemplates() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        excelImportApplication.deleteExcelImportTemplates(ids);
        return success();
    }

    @RequiresPermissions("ExcelImportTemplate:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询导入模板")
    public String slicedQueryExcelImportTemplates() {
        SDO params = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> page = excelImportApplication.slicedQueryExcelImportTemplates(queryRequest);
        return toResult(page);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询导入日志")
    public String slicedQueryExcelImportLogs() {
        SDO params = this.getSDO();
        String templateId = params.getString("templateId");
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        queryRequest.setParentId(templateId);
        Map<String, Object> data = excelImportApplication.slicedQueryExcelImportLogs(queryRequest);
        return toResult(data);
    }

    private boolean filterType(String fileExt) {
        // allowTypes 为空默认不判断文件类型
        String allowTypes = "xls,xlsx";
        if (allowTypes == null || allowTypes.equals("")) return true;
        String[] types = allowTypes.split(",");
        for (String type : types) {
            if (fileExt.toLowerCase().endsWith(type.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.DOWNLOAD, description = "导入数据")
    public String upload() {
        SDO sdo = this.getUploadSDO();
        String templateId = sdo.getString("templateId");
        String templateCode = sdo.getString("templateCode");
        String batchNumber = sdo.getString("batchNumber");
        FileItem uploadItem = (FileItem) sdo.getProperty("uploadFileItem");
        if (uploadItem == null) {
            throw new ApplicationException("上传文件错误:未找到文件。");
        }
        String uploadFileName = sdo.getProperty("uploadFileName", String.class);
        String fileExt = FileHelper.getFileExtName(uploadFileName);
        if (!filterType(fileExt)) {
            return blank("您要上传的文件类型不正确。");
        }
        if (StringUtil.isBlank(templateId)) {
            if (StringUtil.isNotBlank(templateCode)) {
                ExcelImportTemplate template = excelImportApplication.loadExcelImportTemplateByCode(templateCode);
                Assert.notNull(template, String.format("未找到[%s]对应的导入模板!", templateCode));
                templateId = template.getId();
            }
        }
        Assert.hasText(templateId, "导入模板id不能为空!");
        /********* 保存文件到临时文件夹 ************/
        String newFileName = FileHelper.getTmpdirFilePath(fileExt);

        File uploadedFile = new File(newFileName);
        try {
            // 以服务器的文件保存地址和原文件名建立上传文件输出流
            uploadItem.write(uploadedFile);
        } catch (Exception e) {
            if (uploadedFile.exists()) uploadedFile.delete();
            LogHome.getLog(this).error("上传文件错误：", e);
            return blank("上传文件失败。");
        }
        ExcelImportLog excelImportLog = new ExcelImportLog();
        try {
            ThreadLocalUtil.putVariable("ExcelImportParam_", sdo.getProperties());
            ExcelImportTemplate template = this.excelImportApplication.loadExcelImportTemplate(templateId);
            excelImportLog.setExcelImportTemplate(template);
            excelImportLog.setFileName(newFileName);
            batchNumber = excelImportApplication.doImport(templateId, batchNumber, newFileName);
            excelImportLog.setBatchNumber(batchNumber);
            Long errorCount = excelImportApplication.countExcelImport(templateId, batchNumber, ExcelImportStatus.ERROR);
            Long successCount = excelImportApplication.countExcelImport(templateId, batchNumber, ExcelImportStatus.SUCCESS);

            excelImportLog.setErrorCount(errorCount.intValue());
            excelImportLog.setSuccessCount(successCount.intValue());
            // 保存日志
            excelImportApplication.saveExcelImportLog(excelImportLog);
            return success(batchNumber);
        } catch (Exception e) {
            excelImportLog.setErrorMessage("解析文件错误:" + e.getMessage());
            excelImportApplication.saveExcelImportLog(excelImportLog);
            LogHome.getLog(this).error("解析错误：", e);
            return blank(e.getMessage());
        } finally {
            uploadItem.delete();
            if (uploadedFile.exists()) {
                uploadedFile.delete();
            }
        }

    }

    @RequiresPermissions("ExcelImportTemplate:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询导入模板明细")
    public String queryExcelImportTemplateDetails() {
        SDO params = this.getSDO();
        String templateId = params.getString("templateId");
        EmptyQueryRequest queryRequest = params.toQueryRequest(EmptyQueryRequest.class);
        Map<String, Object> data = excelImportApplication.queryExcelImportTemplateDetails(templateId, queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("ExcelImportTemplate:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除导入模板明细")
    public String deleteExcelImportTemplateDetails() {
        SDO params = this.getSDO();
        String templateId = params.getString("templateId");
        List<String> ids = params.getStringList("ids");
        excelImportApplication.deleteExcelImportTemplateDetails(templateId, ids);
        return success();
    }

    @RequiresPermissions("ExcelImportTemplate:move")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.MOVE, description = "移动导入模板")
    public String moveExcelImportTemplates() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        String folderId = params.getString("folderId");
        excelImportApplication.moveExcelImportTemplates(ids, folderId);
        return success();
    }

    @RequiresPermissions("ExcelImportTemplate:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改导入模板状态")
    public String updateExcelImportTemplateStatus() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        Integer status = params.getInteger("status");
        excelImportApplication.updateExcelImportTemplateStatus(ids, status);
        return success();
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询导入结果头")
    public String queryExcelImportGridHead() {
        SDO params = this.getSDO();
        String templateId = params.getString("templateId");
        Map<String, Object> data = excelImportApplication.queryExcelImportGridHead(templateId);
        return toResult(data);
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DOWNLOAD, description = "导出导入模板")
    public String exportExcelTemplate() {
        SDO sdo = this.getSDO();
        String templateId = sdo.getString("id");
        String xml = excelImportApplication.buildExcelHeadByTemplateId(templateId);
        String s = ExportExcel.createExcel(xml);// 组合EXCEL
        File file = new File(s);
        if (file.exists()) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("file", file.getName());
            return toResult(m);
        }
        return null;
    }

    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询导入明细")
    public String slicedQueryExcelImportDetails() {
        SDO params = this.getSDO();
        String templateId = params.getString("templateId");
        String batchNumber = params.getString("batchNumber");
        Integer inputStatus = params.getInteger("status");
        ExcelImportStatus status = ExcelImportStatus.fromId(inputStatus);
        Map<String, Object> map = excelImportApplication.slicedQueryExcelImportDetails(templateId, batchNumber, status, QueryPageRequest.newInstance(params));
        return toResult(map);
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.LIST, description = "跳转到数据导入列表页面")
    public String forwardExcelImportLog() {
        SDO sdo = this.getSDO();
        String code = sdo.getString("code");
        if (StringUtil.isNotBlank(code)) {
            ExcelImportTemplate template = excelImportApplication.loadExcelImportTemplateByCode(code);
            if (template != null) {
                return forward(EXCEL_IMPORT_LOG, template);
            }
        }
        return forward(EXCEL_IMPORT_LOG);
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.DETALL, description = "跳转到Excel导入详细页面")
    public String forwardAssignCodeImpPage() {
        SDO sdo = this.getSDO();
        String code = sdo.getString("code");
        ExcelImportTemplate template = excelImportApplication.loadExcelImportTemplateByCode(code);
        Assert.notNull(template, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_CODE, code, "Excel导入模板"));
        this.putAttribute("statusList", ExcelImportStatus.getQueryViewMap());
        this.putAttribute("excelImpDataStatus", "-1");
        this.putAttribute("batchNumber", sdo.getProperty("batchNumber"));
        String templateRemark = template.getRemark();
        if (StringUtil.isNotBlank(templateRemark)) {
            this.putAttribute("hasRemark", true);
            this.putAttribute("templateRemark", templateRemark);
        } else {
            this.putAttribute("hasRemark", false);
        }
        return forward("AssignCodeImp", template);
    }

    @LogInfo(logType = LogType.BIZ, operaionType = OperationType.DELETE, description = "删除导入临时表数据")
    public String deleteTemporaryData() {
        SDO params = this.getSDO();
        String templateId = params.getString("templateId");
        String batchNumber = params.getString("batchNumber");
        excelImportApplication.deleteTemporaryData(templateId, batchNumber);
        return success();
    }
}
