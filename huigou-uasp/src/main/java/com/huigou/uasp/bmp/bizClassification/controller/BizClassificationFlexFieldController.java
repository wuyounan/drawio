package com.huigou.uasp.bmp.bizClassification.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationFlexFieldApplication;
import com.huigou.uasp.bmp.bizClassification.domain.query.BizClassifyFlexFieldQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;

/**
 * 业务参数数据管理
 * xx
 */
@Controller
@ControllerMapping("bizClassificationFlexField")
public class BizClassificationFlexFieldController extends CommonController {

    @Autowired
    private BizClassificationFlexFieldApplication application;

    /**
     * 添加扩展表数据
     */
    public String insertData() {
        SDO params = this.getSDO();
        String bizclassificationdetailId = params.getString("bizclassificationdetailId");
        String orgId = params.getString("orgId");
        String bizCode = params.getString("bizCode");
        application.insertData(bizclassificationdetailId, orgId, bizCode);
        return success();
    }

    /**
     * 修改扩展表数据
     */
    public String updateData() {
        SDO params = this.getSDO();
        String bizclassificationdetailId = params.getString("bizclassificationdetailId");
        String detailId = params.getString("detailId");
        application.updateData(bizclassificationdetailId, detailId);
        return success();
    }

    /**
     * 删除扩展表数据
     */
    public String deleteDatas() {
        SDO params = this.getSDO();
        String tableName = params.getString("tableName");
        List<String> ids = params.getStringList("ids");
        String bizCode = params.getString("bizCode");
        this.application.deleteDatas(tableName, bizCode, ids);
        return success();
    }

    /**
     * 分页查询业务表数据
     * 
     * @return
     */
    public String sliceQueryBizTables() {
        SDO params = this.getSDO();
        BizClassifyFlexFieldQueryRequest queryRequest = params.toQueryRequest(BizClassifyFlexFieldQueryRequest.class);
        Map<String, Object> data = this.application.sliceQueryDatas(queryRequest);
        return toResult(data);
    }

    /**
     * 保存业务定义扩展属性
     * 
     * @return
     */
    public String saveFlexFiledDatas() {
        SDO params = this.getSDO();
        String orgId = params.getString("orgId");
        String bizCode = params.getString("bizCode");
        this.application.saveFlexFiledDatas(orgId, bizCode);
        return success();
    }

}
