package com.huigou.index.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.index.application.IndexClassificationApplication;
import com.huigou.index.domain.model.IndexClassification;
import com.huigou.index.domain.query.IndexClassificationQueryRequest;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("indexClassification")
public class IndexClassificationController extends CommonController {

    private final static String INDEX_CLASSIFICATION_PAGE = "indexClassification";

    private final static String INDEX_CLASSIFICATION_DETAIL_PAGE = "indexClassificationDetail";

    @Autowired
    private IndexClassificationApplication indexClassificationApplication;

    protected String getPagePath() {
        return "/system/index/";
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexClassification() {
        return forward(INDEX_CLASSIFICATION_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String showInsertIndexClassification() {
        SDO sdo = this.getSDO();

        String parentId = sdo.getParentId();
        Integer sequence = this.indexClassificationApplication.getIndexClassificationNextSequence(parentId);
        this.putAttribute(CommonDomainConstants.SEQUENCE_FIELD_NAME, sequence);

        return forward(INDEX_CLASSIFICATION_DETAIL_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String insertIndexClassification() {
        SDO sdo = this.getSDO();
        IndexClassification indexClassification = sdo.toObject(IndexClassification.class);
        String id = indexClassificationApplication.saveIndexClassification(indexClassification);
        return success(id);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String showUpdateIndexClassification() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();

        IndexClassification indexClassification = indexClassificationApplication.loadIndexClassification(id);
        return forward(INDEX_CLASSIFICATION_DETAIL_PAGE, indexClassification);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexClassification() {
        SDO sdo = this.getSDO();
        IndexClassification indexClassification = sdo.toObject(IndexClassification.class);
        indexClassificationApplication.saveIndexClassification(indexClassification);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexClassificationsSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> params = sdo.getStringMap("data");
        indexClassificationApplication.updateIndexClassificationsSequence(params);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexClassificationsStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        Integer status = sdo.getInteger(CommonDomainConstants.STATUS_FIELD_NAME);
        indexClassificationApplication.updateIndexClassificationsStatus(ids, status);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexClassifications() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        indexClassificationApplication.deleteIndexClassifications(ids);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexClassifications() {
        SDO sdo = this.getSDO();
        IndexClassificationQueryRequest queryRequest = sdo.toQueryRequest(IndexClassificationQueryRequest.class);
        Map<String, Object> data = indexClassificationApplication.queryIndexClassifications(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String slicedQueryIndexClassifications() {
        SDO sdo = this.getSDO();
        ParentAndCodeAndNameQueryRequest queryRequest = sdo.toQueryRequest(ParentAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = indexClassificationApplication.slicedQueryIndexClassifications(queryRequest);
        return toResult(data);
    }
}
