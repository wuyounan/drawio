
package com.huigou.index.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.domain.ValidStatus;
import com.huigou.index.application.IndexClassificationDimApplication;
import com.huigou.index.domain.model.IndexClassificationDim;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("indexClassificationDim")
public class IndexClassificationDimController extends CommonController {

    private static final String INDEX_CLASSIFICATION_DIM_PAGE = "indexClassificationDim";

    private static final String INDEX_CLASSIFICATION_DIM_DETAIL_PAGE = "indexClassificationDimDetail";

    @Autowired
    private IndexClassificationDimApplication indexClassificationDimApplication;

    protected String getPagePath() {
        return "/system/index/";
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexClassificationDim() {
        return forward(INDEX_CLASSIFICATION_DIM_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String showInsertIndexClassificationDim() {
        Integer sequence = this.indexClassificationDimApplication.getIndexClassificationDimNextSequence();
        this.putAttribute(IndexClassificationDim.IS_DEFAULT_FIELD_NAME, IndexClassificationDim.DEFAULT_NO_VALUE);
        this.putAttribute(CommonDomainConstants.STATUS_FIELD_NAME, ValidStatus.ENABLED.getId());
        this.putAttribute(CommonDomainConstants.SEQUENCE_FIELD_NAME, sequence);
        return forward(INDEX_CLASSIFICATION_DIM_DETAIL_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.ADD, description = "")
    public String insertIndexClassificationDim() {
        SDO sdo = this.getSDO();
        IndexClassificationDim indexClassificationDim = sdo.toObject(IndexClassificationDim.class);
        String id = indexClassificationDimApplication.saveIndexClassificationDim(indexClassificationDim);
        return success(id);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String showUpdateIndexClassificationDim() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        IndexClassificationDim indexClassificationDim = indexClassificationDimApplication.loadIndexClassificationDim(id);
        return forward("indexClassificationDimDetail", indexClassificationDim);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexClassificationDim() {
        SDO sdo = this.getSDO();
        IndexClassificationDim indexClassificationDim = sdo.toObject(IndexClassificationDim.class);
        indexClassificationDimApplication.saveIndexClassificationDim(indexClassificationDim);
        return success();
    }
    
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexClassificationDimsSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> params = sdo.getStringMap("data");
        indexClassificationDimApplication.updateIndexClassificationDimsSequence(params);
        return success();
    }

    
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.UPDATE, description = "")
    public String updateIndexClassificationDimsStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        Integer status = sdo.getInteger(CommonDomainConstants.STATUS_FIELD_NAME);
        indexClassificationDimApplication.updateIndexClassificationDimsStatus(ids, status);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteIndexClassificationDims() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        indexClassificationDimApplication.deleteIndexClassificationDims(ids);
        return success();
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String slicedQueryIndexClassificationDims() {
        SDO sdo = this.getSDO();
        CodeAndNameQueryRequest queryRequest = sdo.toQueryRequest(CodeAndNameQueryRequest.class);
        Map<String, Object> data = indexClassificationDimApplication.slicedQueryIndexClassificationDims(queryRequest);
        return toResult(data);
    }

}
