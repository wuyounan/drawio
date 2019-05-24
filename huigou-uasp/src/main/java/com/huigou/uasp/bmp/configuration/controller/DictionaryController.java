package com.huigou.uasp.bmp.configuration.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.configuration.application.DictionaryApplication;
import com.huigou.uasp.bmp.configuration.domain.model.SysDictionary;
import com.huigou.uasp.bmp.configuration.domain.model.SysDictionaryDetail;
import com.huigou.uasp.bmp.configuration.domain.query.SysDictionariesQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 系统参数
 *
 * @author gongmm
 */
@Controller
@ControllerMapping("dictionary")
public class DictionaryController extends CommonController {

    private static final String DICTIONARY_ID_KEY_NAME = "dictionaryId";

    private static final String LIST_PAGE = "Dictionary";

    private static final String DETAIL_PAGE = "DictionaryDetail";

    @Autowired
    private DictionaryApplication application;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    @RequiresPermissions("SysDictionary:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到系统字典列表明细页面")
    public String forward() {
        return forward(LIST_PAGE);
    }

    @RequiresPermissions("SysDictionary:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加系统字典明细页面")
    public String showInsertSysDictionary() {
        return forward(DETAIL_PAGE);
    }

    @RequiresPermissions("SysDictionary:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加系统字典")
    public String insertSysDictionary() {
        SDO params = this.getSDO();
        SysDictionary sysDictionary = params.toObject(SysDictionary.class);
        List<SysDictionaryDetail> inputDetails = params.getList("detailData", SysDictionaryDetail.class);
        sysDictionary.setInputDetails_(inputDetails);
        sysDictionary.addUpdateFields_(AbstractEntity.INPUT_DETAILS_FIELD_NAME);

        String id = application.saveSysDictionary(sysDictionary);

        return success(id);
    }

    @RequiresPermissions("SysDictionary:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改系统字典页面")
    public String showUpdateSysDictionary() {
        SDO sdo = this.getSDO();
        String id = sdo.getString(ID_KEY_NAME);
        SysDictionary sysDictionary = application.loadSysDictionary(id);
        return forward("DictionaryDetail", sysDictionary);

    }

    @RequiresPermissions("SysDictionary:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改系统字典")
    public String updateSysDictionary() {
        SDO params = this.getSDO();
        SysDictionary sysDictionary =  params.toObject(SysDictionary.class);
        List<SysDictionaryDetail> inputDetails = params.getList("detailData", SysDictionaryDetail.class);
        sysDictionary.setInputDetails_(inputDetails);
        sysDictionary.addUpdateFields_(AbstractEntity.INPUT_DETAILS_FIELD_NAME);
        
        application.saveSysDictionary(sysDictionary);
        return success();
    }

    @RequiresPermissions("SysDictionary:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除系统字典")
    public String deleteSysDictionaries() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        application.deleteSysDictionaries(ids);
        return success();
    }

    @RequiresPermissions("SysDictionary:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除系统字典明细")
    public String deleteSysDictionaryDetails() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        String dictionaryId = sdo.getString(DICTIONARY_ID_KEY_NAME);
        application.deleteSysDictionaryDetails(dictionaryId, ids);
        return success();
    }

    @RequiresPermissions("SysDictionary:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改系统字典")
    public String updateSysDictionariesStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        Integer status = sdo.getInteger(STATUS_KEY_NAME);
        application.updateSysDictionariesStatus(ids, status);
        return success();
    }

    @RequiresPermissions("SysDictionary:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改系统字典")
    public String updateSysDictionaryDetailsStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        Integer status = sdo.getInteger(STATUS_KEY_NAME);
        application.updateSysDictionaryDetailsStatus(ids, status);
        return success();
    }

    @RequiresPermissions("SysDictionary:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询系统字典")
    public String slicedQuerySysDictionaries() {
        SDO params = this.getSDO();
        SysDictionariesQueryRequest queryRequest = params.toQueryRequest(SysDictionariesQueryRequest.class);
        Map<String, Object> data = application.slicedQuerySysDictionaries(queryRequest);
        return toResult(data);
    }

    // @RequiresPermissions("SysDictionary:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询系统字典明细")
    public String querySysDictionaryDetailsByCode() {
        SDO params = this.getSDO();
        String code = params.getString(CODE_KEY_NAME);
        Map<String, Object> data = application.querySysDictionaryDetailsByCode(code);
        return toResult(data);
    }

    @RequiresPermissions("SysDictionary:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询系统字典明细")
    public String slicedQuerySysDictionaryDetails() {
        SDO params = this.getSDO();
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        queryRequest.setParentId(params.getString(DICTIONARY_ID_KEY_NAME));
        Map<String, Object> data = application.slicedQuerySysDictionaryDetails(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("SysDictionary:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动系统字典")
    public String moveSysDictionaries() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getProperty(FOLDER_ID_KEY_NAME, String.class);
        application.moveSysDictionaries(ids, folderId);
        return success();
    }

    @RequiresPermissions("SysDictionary:syn")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.CACHE, description = "同步系统字典")
    public String syncCache() {
        this.application.syncCache();
        return success("同步缓存已完成。");
    }

}
