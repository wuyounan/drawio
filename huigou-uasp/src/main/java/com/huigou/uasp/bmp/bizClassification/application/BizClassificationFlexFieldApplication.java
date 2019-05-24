package com.huigou.uasp.bmp.bizClassification.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.bizClassification.domain.query.BizClassifyFlexFieldQueryRequest;

/**
 * 业务参数数据管理
 * xx
 */
public interface BizClassificationFlexFieldApplication {

    /**
     * 业务表添加数据
     * 
     * @param bizclassificationdetailId
     * @param orgId
     * @return
     */
    void insertData(String bizclassificationdetailId, String orgId, String bizCode);

    /**
     * 业务表修改数据
     * 
     * @param bizclassificationdetailId
     * @param detailId
     * @return
     */
    void updateData(String bizclassificationdetailId, String detailId);

    /**
     * 业务表删除数据
     * 
     * @param tableName
     * @param ids
     */
    void deleteDatas(String tableName, String bizCode, List<String> ids);

    /**
     * 业务表数据分页查询
     * 
     * @param tableName
     * @param orgId
     * @return
     */
    Map<String, Object> sliceQueryDatas(BizClassifyFlexFieldQueryRequest queryRequest);

    /**
     * 保存业务配置扩展数据
     * 
     * @param bizclassificationdetailId
     * @param orgId
     */
    void saveFlexFiledDatas(String orgId, String bizCode);

}
