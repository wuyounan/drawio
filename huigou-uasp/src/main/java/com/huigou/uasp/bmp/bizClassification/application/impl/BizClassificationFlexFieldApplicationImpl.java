package com.huigou.uasp.bmp.bizClassification.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationApplication;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationFlexFieldApplication;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassifyAbstractEntity;
import com.huigou.uasp.bmp.bizClassification.domain.query.BizClassifyFlexFieldQueryRequest;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.flexfield.application.FlexFieldApplication;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldStorage;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 业务参数数据管理
 * xx
 */
@Service("bizClassificationFlexFieldApplication")
public class BizClassificationFlexFieldApplicationImpl extends BaseApplication implements BizClassificationFlexFieldApplication {

    @Autowired
    private BizClassificationApplication bizClassificationApplication;

    @Autowired
    private FlexFieldApplication flexFieldApplication;

    private Class<? extends BizClassifyAbstractEntity> getBizClassifyEntityClass(String bizclassificationdetailId) {
        return bizClassificationApplication.getBizClassifyEntityClass(bizclassificationdetailId);
    }

    @Override
    public void insertData(String bizclassificationdetailId, String orgId, String bizCode) {
        Assert.hasText(bizclassificationdetailId, "调用时bizclassificationdetailId为空。");
        Assert.hasText(orgId, "调用时orgId为空。");
        Assert.hasText(bizCode, "调用时bizCode为空。");
        Class<? extends BizClassifyAbstractEntity> cls = this.getBizClassifyEntityClass(bizclassificationdetailId);
        BizClassifyAbstractEntity entity = null;
        try {
            entity = cls.newInstance();
            entity.setOrgId(orgId);
            entity.setBizCode(bizCode);
        } catch (Exception e) {
            Assert.isTrue(false, String.format("错误[%s]!", e.getMessage()));
        }
        // 设置对象属性
        this.setProperty(entity);
        entity = this.generalRepository.save(entity);
        flexFieldApplication.saveFlexFieldStorages(bizCode, entity.getId());
    }

    @Override
    public void updateData(String bizclassificationdetailId, String detailId) {
        Assert.hasText(bizclassificationdetailId, "调用时bizclassificationdetailId为空。");
        Assert.hasText(detailId, "调用时detailId为空。");
        Class<? extends BizClassifyAbstractEntity> cls = this.getBizClassifyEntityClass(bizclassificationdetailId);
        BizClassifyAbstractEntity entity = this.generalRepository.findOne(cls, detailId);
        // 设置对象属性
        this.setProperty(entity);
        entity = this.generalRepository.save(entity);
        flexFieldApplication.saveFlexFieldStorages(entity.getBizCode(), entity.getId());
    }

    /**
     * 设置业务扩展表属性
     * 
     * @param entity
     */
    private void setProperty(BizClassifyAbstractEntity entity) {
        SDO sdo = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        if (sdo != null) {
            List<FlexFieldStorage> flexFieldStorages = sdo.getList(Constants.FLEX_FIELD, FlexFieldStorage.class);
            if (flexFieldStorages != null && flexFieldStorages.size() > 0) {
                for (FlexFieldStorage field : flexFieldStorages) {
                    ClassHelper.setProperty(entity, field.getFieldName(), field.getFieldValue());
                }
            } else {
                Assert.isTrue(false, String.format("未找到[%s]扩展属性定义!", entity.getBizCode()));
            }
        }
    }

    @Override
    public void deleteDatas(String tableName, String bizCode, List<String> ids) {
        Assert.hasText(tableName, "调用时tableName为空。");
        Assert.hasText(bizCode, "调用时bizCode为空。");
        String sql = String.format("delete %s s where s.id=?", tableName);
        List<Object[]> dataSet = new ArrayList<Object[]>();
        for (String id : ids) {
            dataSet.add(new Object[] { id });
            flexFieldApplication.deleteFlexFieldStorages(bizCode, id);
        }
        this.sqlExecutorDao.batchUpdate(sql, dataSet);
    }

    @Override
    public void saveFlexFiledDatas(String orgId, String bizCode) {
        Assert.hasText(orgId, "调用时orgId为空。");
        Assert.hasText(bizCode, "调用时bizCode为空。");
        flexFieldApplication.saveFlexFieldStorages(bizCode, orgId);
    }

    @Override
    public Map<String, Object> sliceQueryDatas(BizClassifyFlexFieldQueryRequest queryRequest) {
        queryRequest.checkParams();
        String sql = "select * from " + queryRequest.getTableName() + " where 1=1 ";
        QueryModel queryModel = queryRequest.initQueryModel();
        queryModel.setSql(sql);
        queryModel.addCriteria(" and org_id = :orgId");
        queryModel.putParam("orgId", queryRequest.getOrgId());
        queryModel.addCriteria(" and biz_code = :bizCode");
        queryModel.putParam("bizCode", queryRequest.getBizCode());
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

}
