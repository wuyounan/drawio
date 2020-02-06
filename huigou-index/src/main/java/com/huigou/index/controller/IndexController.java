package com.huigou.index.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.index.IndexConstant;
import com.huigou.index.application.IndexApplication;
import com.huigou.index.domain.model.Index;
import com.huigou.index.domain.model.IndexEntry;
import com.huigou.index.domain.model.IndexEntryFormulaParam;
import com.huigou.index.domain.model.IndexEntryTab;
import com.huigou.index.domain.model.IndexEntryUIParam;
import com.huigou.index.domain.query.IndexEntryEntryQueryRequest;
import com.huigou.index.domain.query.IndexQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 指标Controller
 * 
 * @author gongmm
 */
@Controller
@ControllerMapping("index")
public class IndexController extends CommonController {
    private static final String LIST_PAGE = "index";

    private static final String ENTRY_PAGE = "indexDetail";

    private static final String ENTRY_DETAIL_PAGE = "indexEntryDetail";

    @Autowired
    private IndexApplication indexApplication;

    protected String getPagePath() {
        return "/system/index/";
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardListIndex() {
        return forward(LIST_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String showInsertIndex() {
        putAttribute(CommonDomainConstants.SEQUENCE_FIELD_NAME, this.indexApplication.getIndexNextSequence());
        return forward(ENTRY_PAGE, this.getSDO());
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String insertIndex() {
        SDO sdo = this.getSDO();
        Index index = sdo.toObject(Index.class);
        String id = indexApplication.saveIndex(index);
        return success(id);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String showUpdateIndex() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        Index index = indexApplication.loadIndex(id);
        return forward(ENTRY_PAGE, index);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndex() {
        SDO sdo = this.getSDO();
        Index index = sdo.toObject(Index.class);
        indexApplication.saveIndex(index);
        return success(index.getId());
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexes() {
        List<String> ids = this.getSDO().getIds();
        indexApplication.deleteIndexes(ids);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexsStatus() {
        SDO sdo = getSDO();
        List<String> ids = sdo.getIds();
        Integer status = sdo.getInteger(CommonDomainConstants.STATUS_FIELD_NAME);
        this.indexApplication.updateIndexsStatus(ids, status);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexsSequence() {
        Map<String, Integer> params = getSDO().getStringMap("data");
        this.indexApplication.updateIndexsSequence(params);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String slicedQueryIndexes() {
        SDO sdo = this.getSDO();
        IndexQueryRequest queryRequest = sdo.toQueryRequest(IndexQueryRequest.class);
        Map<String, Object> data = indexApplication.slicedQueryIndexes(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.MOVE, description = "")
    public String moveIndexs() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        String classificationId = sdo.getString(IndexConstant.CLASSIFICATION_ID_FIELD_NAME);
        indexApplication.moveIndexs(ids, classificationId);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String showInsertIndexEntry() {
        SDO sdo = this.getSDO();
        Integer sequence = this.indexApplication.getIndexEntryNextSequence(sdo.getString(IndexConstant.INDEX_ID_FIELD_NAME));
        this.putAttribute(CommonDomainConstants.SEQUENCE_FIELD_NAME, sequence);
        return forward(ENTRY_DETAIL_PAGE, sdo);
    }

    private IndexEntry internalBuildIndexEntry() {
        SDO sdo = this.getSDO();
        IndexEntry indexEntry = sdo.toObject(IndexEntry.class);

        List<IndexEntryFormulaParam> inputIndexEntryFormulaParams = sdo.getList("indexEntryFormulaParamData", IndexEntryFormulaParam.class);
        List<IndexEntryUIParam> inputIndexEntryUIParams = sdo.getList("indexEntryUIParamData", IndexEntryUIParam.class);
        List<IndexEntryTab> inputIndexEntryTabs = sdo.getList("indexEntryTabData", IndexEntryTab.class);

        indexEntry.setInputIndexEntryFormulaParams_(inputIndexEntryFormulaParams);
        indexEntry.setInputIndexEntryUIParams_(inputIndexEntryUIParams);
        indexEntry.setInputIndexEntryTabs_(inputIndexEntryTabs);
        indexEntry.addUpdateFields_(IndexEntry.INDEX_ENTRY_UI_PARAM_FIELD_NAME, IndexEntry.INDEX_ENTRY_TAB_FIELD_NAME,
                                    IndexEntry.INDEX_ENTRY_FORMULA_PARAM_FIELD_NAME);

        return indexEntry;
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String insertIndexEntry() {
        IndexEntry indexEntry = internalBuildIndexEntry();
        String id = indexApplication.saveIndexEntry(indexEntry);
        return success(id);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String showUpdateIndexEntry() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        IndexEntry indexEntry = indexApplication.loadIndexEntry(id);
        return forward("indexEntryDetail", indexEntry);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexEntry() {
        IndexEntry indexEntry = internalBuildIndexEntry();
        indexApplication.saveIndexEntry(indexEntry);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexEntries() {
        List<String> ids = this.getSDO().getIds();
        indexApplication.deleteIndexEntries(ids);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexEntryFormulaParams() {
        SDO sdo = this.getSDO();
        String entryId = sdo.getString("entryId");
        List<String> ids = sdo.getIds();
        this.indexApplication.deleteIndexEntryFormulaParams(entryId, ids);
        return this.success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexEntryUIParams() {
        SDO sdo = this.getSDO();
        String entryId = sdo.getString("entryId");
        List<String> ids = sdo.getIds();
        this.indexApplication.deleteIndexEntryUIParams(entryId, ids);
        return this.success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexEntryTabs() {
        SDO sdo = this.getSDO();
        String entryId = sdo.getString("entryId");
        List<String> ids = sdo.getIds();
        this.indexApplication.deleteIndexEntryTabs(entryId, ids);
        return this.success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexEntriesSequence() {
        Map<String, Integer> params = getSDO().getStringMap("data");
        indexApplication.updateIndexEntriesSequence(params);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String slicedQueryIndexEntries() {
        SDO sdo = this.getSDO();
        IndexQueryRequest queryRequest = sdo.toQueryRequest(IndexQueryRequest.class);
        Map<String, Object> data = indexApplication.slicedQueryIndexEntries(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexEntryFormulaParams() {
        SDO sdo = this.getSDO();
        IndexEntryEntryQueryRequest queryRequest = sdo.toQueryRequest(IndexEntryEntryQueryRequest.class);
        Map<String, Object> data = indexApplication.queryIndexEntryFormulaParams(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexEntryUIParams() {
        SDO sdo = this.getSDO();
        IndexEntryEntryQueryRequest queryRequest = sdo.toQueryRequest(IndexEntryEntryQueryRequest.class);
        Map<String, Object> data = indexApplication.queryIndexEntryUIParams(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexEntryTabs() {
        SDO sdo = this.getSDO();
        IndexEntryEntryQueryRequest queryRequest = sdo.toQueryRequest(IndexEntryEntryQueryRequest.class);
        Map<String, Object> data = indexApplication.queryIndexEntryTabs(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexEntriesByIndexId() {
        String indexId = this.getSDO().getString("indexId");
        Map<String, Object> data = indexApplication.queryIndexEntriesByIndexId(indexId);
        return toResult(data);
    }

}
