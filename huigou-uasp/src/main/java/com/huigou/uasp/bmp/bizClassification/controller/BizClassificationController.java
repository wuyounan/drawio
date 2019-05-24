package com.huigou.uasp.bmp.bizClassification.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationApplication;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassification;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassificationDetail;
import com.huigou.uasp.bmp.bizClassification.domain.query.BizClassificationQueryRequest;
import com.huigou.uasp.bmp.flexfield.application.FlexFieldApplication;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 业务分类配置Controller
 */
@Controller
@ControllerMapping("bizClassification")
public class BizClassificationController extends CommonController {

    @Autowired
    private BizClassificationApplication application;

    @Autowired
    private FlexFieldApplication flexFieldApplication;

    @Override
    protected String getPagePath() {
        return "/system/classification/";
    }

    @RequiresPermissions("BizClassificationConfiguration:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到业务分类配置列表页面")
    public String forwardBizClassification() {
        application.insertBizClassificationRoot();
        return forward("bizClassification");
    }

    @RequiresPermissions("BizClassificationConfiguration:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加业务分类配置明细页面")
    public String showInsertBizClassification() {
        SDO sdo = this.getSDO();
        String parentId = sdo.getString("parentId");
        this.putAttribute("parentId", parentId);
        this.putAttribute("sequence", application.getBizClassificationNextSequence(parentId));
        return forward("bizClassificationDetail");
    }

    @RequiresPermissions("BizClassificationConfiguration:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加业务分类配置")
    public String insertBizClassification() {
        SDO params = this.getSDO();
        BizClassification bizClassification = params.toObject(BizClassification.class);
        String id = application.insertBizClassification(bizClassification);
        return success(id);
    }

    @RequiresPermissions("BizClassificationConfiguration:buildPermission")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "生成权限")
    public String buildPermission() {
        SDO params = this.getSDO();
        String fullId = params.getString("fullId");
        application.buildPermission(fullId);
        return success();
    }

    @RequiresPermissions("BizClassificationConfiguration:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改业务分类配置")
    public String updateBizClassification() {
        SDO params = this.getSDO();
        BizClassification bizClassification = params.toObject(BizClassification.class);
        List<BizClassificationDetail> detail = params.getList("bizClassificationDetails", BizClassificationDetail.class);
        bizClassification.setDetail(detail);
        String id = application.updateBizClassification(bizClassification);
        return success(id);
    }

    @RequiresPermissions("BizClassificationConfiguration:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改业务分类配置明细页面")
    public String showUpdateBizClassification() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        BizClassification bizClassification = application.loadBizClassification(id);
        return forward("bizClassificationDetail", bizClassification);
    }

    @RequiresPermissions("BizClassificationConfiguration:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除业务分类配置")
    public String deleteBizClassification() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.application.deleteBizClassifications(ids);
        return success("您已成功删除业务分类配置。");
    }

    @RequiresPermissions("BizClassificationConfiguration:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动业务分类配置")
    public String moveBizClassifications() {
        SDO params = this.getSDO();
        String parentId = params.getString("parentId");
        List<String> ids = params.getStringList("ids");
        this.application.moveBizClassifications(parentId, ids);
        return success();
    }

    @RequiresPermissions("BizClassificationConfiguration:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改业务分类配置排序号")
    public String updateBizClassificationsSequence() {
        Map<String, Integer> params = this.getSDO().getStringMap("data");
        this.application.updateBizClassificationsSequence(params);
        return success("您已成功修改排序号。");
    }

    @RequiresPermissions("BizClassificationConfiguration:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改业务分类配置状态")
    public String updateBizClassificationsStatus() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        Integer status = params.getInteger("status");
        this.application.updateBizClassificationsStatus(ids, status);
        return success("您已成功修改状态。");
    }

    @RequiresPermissions(value = { "BizClassificationConfiguration:query", "BizDataConfiguration:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "查询业务分类配置状态")
    public String queryBizClassifications() {
        SDO params = this.getSDO();
        BizClassificationQueryRequest bizClassificationQueryRequest = params.toQueryRequest(BizClassificationQueryRequest.class);
        Map<String, Object> data = this.application.queryBizClassifications(bizClassificationQueryRequest);
        return toResult(data);
    }

    /**
     * 按权限查询业务分类配置
     * 
     * @return
     */
    public String queryBizClassificationsByPermission() {
        SDO params = this.getSDO();
        BizClassificationQueryRequest bizClassificationQueryRequest = params.toQueryRequest(BizClassificationQueryRequest.class);
        List<Map<String, Object>> data = this.application.queryBizClassificationsByPermission(bizClassificationQueryRequest);
        return toResult(data);
    }

    @RequiresPermissions("BizClassificationConfiguration:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "分页查询业务分类配置状态")
    public String sliceQueryBizClassifications() {
        SDO params = this.getSDO();
        BizClassificationQueryRequest bizClassificationQueryRequest = params.toQueryRequest(BizClassificationQueryRequest.class);
        Map<String, Object> data = this.application.sliceQueryBizClassifications(bizClassificationQueryRequest);
        return toResult(data);
    }

    @RequiresPermissions("BizClassificationConfiguration:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除业务分类配置明细")
    public String deleteBizclassificationdetails() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.application.deleteBizclassificationdetails(ids);
        return success("您已成功删除业务分类配置明细。");
    }

    /**
     * 批量添加业务分类配置明细表
     */
    public String batchInsertBizFieldSysGroup() {
        SDO params = this.getSDO();
        String bizClassificationId = params.getString("bizClassificationId");
        String ids = params.getString("ids");
        application.batchInsertBizFieldSysGroup(bizClassificationId, ids.split(","));
        return success("您已成功添加业务系统分组。");
    }

    /**
     * 保存业务分类配置明细排序号
     *
     * @return
     */
    public String updateBizClassificationDetailsSequence() {
        Map<String, Integer> params = this.getSDO().getStringMap("data");
        this.application.updateBizClassificationDetailsSequence(params);
        return success("您已成功修改排序号。");
    }

    /**
     * 保存业务分类配置明细状态
     */
    public String updateBizclassificationdetailsStatus() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        Integer status = params.getInteger("status");
        this.application.updateBizclassificationdetailsStatus(ids, status);
        return success("您已成功修改状态。");
    }

    /**
     * 查询业务分类配置明细
     */
    public String queryBizClassificationDetails() {
        SDO params = this.getSDO();
        BizClassificationQueryRequest bizClassificationQueryRequest = params.toQueryRequest(BizClassificationQueryRequest.class);
        Map<String, Object> data = this.application.queryBizClassificationDetails(bizClassificationQueryRequest);
        return toResult(data);
    }

    /**
     * 查询业务表可视字段
     * 
     * @return
     */
    public String queryClassificationVisibleFields() {
        SDO params = this.getSDO();
        String bizPropertyId = params.getString("bizPropertyId");
        List<Map<String, Object>> data = this.application.queryClassificationVisibleFields(bizPropertyId);
        return toResult(data);
    }

    /**
     * 查询分类下包含的分组明细
     * 
     * @return
     */
    public String queryDetailsByClassificationId() {
        SDO params = this.getSDO();
        String bizClassificationId = params.getString("bizClassificationId");
        List<Map<String, Object>> data = this.application.queryByClassificationId(bizClassificationId);
        return toResult(data);
    }

    /**
     * 跳转到业务分类列表页面
     */
    public String forwardBizClassificationDetail() {
        return forward("showBizClassificationDetail");
    }

    public String forwardTenantBizClassification() {
        this.putAttribute("orgId", this.getOperator().getTenantId());
        return forward("showTenantBizClassification");
    }

}
