package com.huigou.uasp.bmp.bizClassification.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.EntityUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.model.TreeEntity;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationApplication;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassification;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassificationDetail;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassifyAbstractEntity;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizDlassificationType;
import com.huigou.uasp.bmp.bizClassification.domain.query.BizClassificationQueryRequest;
import com.huigou.uasp.bmp.bizClassification.repository.BizClassificationDetailRepository;
import com.huigou.uasp.bmp.bizClassification.repository.BizClassificationRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.flexfield.application.FlexFieldApplication;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroup;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.PermissionResourceKind;
import com.huigou.uasp.bmp.opm.proxy.PermissionApplicationProxy;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 业务分类配置接口实现
 * xx
 */
@Service("bizclassificationApplication")
public class BizClassificationApplicationImpl extends BaseApplication implements BizClassificationApplication {

    @Autowired
    private BizClassificationRepository bizClassificationRepository;

    @Autowired
    private BizClassificationDetailRepository bizClassificationDetailRepository;

    @Autowired
    private FlexFieldApplication flexFieldApplication;

    @Autowired
    private PermissionApplicationProxy permissionApplication;

    @Override
    @Transactional
    public void insertBizClassificationRoot() {
        BizClassification obj = this.bizClassificationRepository.findOne("1");
        if (obj == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("insert into SA_Bizclassification");
            sb.append("  (id, code, name, status, full_id, full_name, parent_id)");
            sb.append("values");
            sb.append("  (?, ?, ?, ?, ?, ?, ?)");
            this.sqlExecutorDao.executeUpdate(sb.toString(), "1", "root", "业务分类配置", 1, "/1", "/业务分类配置", "0");
        }
    }

    @Override
    @Transactional
    public String insertBizClassification(BizClassification bizClassification) {
        Assert.notNull(bizClassification, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        TreeEntity treeEntity = this.commonDomainService.saveTreeEntity(bizClassification, bizClassificationRepository);
        this.buildPermission(treeEntity.getFullId());
        return treeEntity.getId();
    }

    @Override
    @Transactional
    public void buildPermission(String fullId) {
        Assert.hasText(fullId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "fullId"));
        List<BizClassification> bizClassifications = this.bizClassificationRepository.findByFullIdLikeOrderByFullId(fullId + "%");
        this.permissionApplication.buildPermission(bizClassifications, PermissionResourceKind.BUSINESSCLASS);
    }

    @Override
    @Transactional
    public String updateBizClassification(BizClassification bizClassification) {
        Assert.notNull(bizClassification, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        BizClassification baseBizClassification = bizClassificationRepository.findOne(bizClassification.getId());
        List<BizClassificationDetail> detail = bizClassification.getDetail();
        String oldName = baseBizClassification.getName();
        String oldFullName = baseBizClassification.getFullName();
        baseBizClassification.fromEntity(bizClassification);
        // 保存数据
        TreeEntity treeEntity = this.commonDomainService.saveTreeEntity(baseBizClassification, bizClassificationRepository, oldName);
        // 修改权限定义
        this.permissionApplication.updatePermission(treeEntity, oldName, oldFullName, PermissionResourceKind.BUSINESSCLASS);
        // 保存分类明细
        this.batchUpdateBizFieldSysGroup(detail);
        return treeEntity.getId();
    }

    @Override
    public BizClassification loadBizClassification(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.bizClassificationRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteBizClassifications(List<String> ids) {
        List<BizClassification> bizClassifications = this.bizClassificationRepository.findAll(ids);
        for (BizClassification item : bizClassifications) {
            Integer count = this.bizClassificationRepository.countByParentId(item.getId());
            Assert.isTrue(count.equals(0), MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, item.getName()));
            count = this.permissionApplication.countByResourceId(item.getId());
            Assert.isTrue(count.equals(0), String.format("“%s” 已生成权限，不能删除。", item.getName()));
            count = this.bizClassificationDetailRepository.countByBizClassificationId(item.getId());
            Assert.isTrue(count.equals(0), item.getName() + "已设置业务分类配置数据,不能删除");
        }
        this.bizClassificationRepository.delete(bizClassifications);
    }

    @Override
    @Transactional
    public void moveBizClassifications(String parentId, List<String> ids) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        this.commonDomainService.moveTree(BizClassification.class, ids, parentId);
        this.permissionApplication.movePermission(parentId, ids, PermissionResourceKind.BUSINESSCLASS);
    }

    @Override
    public Integer getBizClassificationNextSequence(String parentId) {
        return this.commonDomainService.getNextSequence(BizClassification.class, "parentId", parentId);
    }

    @Override
    @Transactional
    public void updateBizClassificationsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(BizClassification.class, params);
    }

    @Override
    public void updateBizClassificationsStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(BizClassification.class, ids, status);
    }

    @Override
    public Map<String, Object> queryBizClassifications(BizClassificationQueryRequest bizClassificationQueryRequest) {
        QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizclassification");
        return this.sqlExecutorDao.executeQuery(query, bizClassificationQueryRequest, "queryTree");
    }

    @Override
    public Map<String, Object> sliceQueryBizClassifications(BizClassificationQueryRequest bizClassificationQueryRequest) {
        QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizclassification");
        return this.sqlExecutorDao.executeSlicedQuery(query, bizClassificationQueryRequest);
    }

    @Override
    @Transactional
    public void updateBizClassificationDetailsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(BizClassificationDetail.class, params);
    }

    @Override
    @Transactional
    public void updateBizclassificationdetailsStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(BizClassificationDetail.class, ids, status);
    }

    @Override
    @Transactional
    public void deleteBizclassificationdetails(List<String> ids) {
        List<BizClassificationDetail> bizclassificationdetails = this.bizClassificationDetailRepository.findAll(ids);
        this.bizClassificationDetailRepository.delete(bizclassificationdetails);

    }

    @Override
    @Transactional
    public void batchInsertBizFieldSysGroup(String bizClassificationId, String[] bizPropertyIds) {
        if (bizPropertyIds != null && bizPropertyIds.length > 0) {
            List<BizClassificationDetail> detail = new ArrayList<BizClassificationDetail>();
            Integer squence = this.commonDomainService.getNextSequence(BizClassificationDetail.class, "bizClassificationId", bizClassificationId);
            QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizclassificationdetail");
            String sql = query.getSqlByName("checkByBizCode");
            for (String bizPropertyId : bizPropertyIds) {
                Integer count = bizClassificationDetailRepository.countByBizClassificationIdAndBizPropertyId(bizClassificationId, bizPropertyId);
                if (count.equals(0)) {
                    FlexFieldBizGroup group = flexFieldApplication.loadFlexFieldBizGroup(bizPropertyId);
                    String bizCode = group.getBizCode();
                    // 校验权限编码是否已被使用
                    String groupName = this.sqlExecutorDao.queryToString(sql, bizCode);
                    Assert.isTrue(StringUtil.isBlank(groupName), String.format("“%s”已被“%s”引用，不能添加。", bizCode, groupName));
                    // 创建新对象
                    BizClassificationDetail bizClassificationDetail = new BizClassificationDetail();
                    bizClassificationDetail.setBizPropertyId(bizPropertyId);
                    bizClassificationDetail.setBizClassificationId(bizClassificationId);
                    bizClassificationDetail.setBizType(BizDlassificationType.BIZ_FIELD_GROUP.getId());
                    bizClassificationDetail.setSequence(squence++);
                    bizClassificationDetail.setBizName(group.getName());
                    detail.add(bizClassificationDetail);
                }
            }
            if (detail.size() > 0) {
                this.bizClassificationDetailRepository.save(detail);
            }
        }
    }

    @Transactional
    private void batchUpdateBizFieldSysGroup(List<BizClassificationDetail> details) {
        if (details != null && details.size() > 0) {
            String className = null;
            for (BizClassificationDetail bizClassificationDetail : details) {
                className = bizClassificationDetail.getEntityClassName();
                if (StringUtil.isBlank(className)) {
                    bizClassificationDetail.setBizType(BizDlassificationType.BIZ_FIELD_GROUP.getId());
                } else {
                    bizClassificationDetail.setBizType(BizDlassificationType.BIZ_TABLE.getId());
                    // 校验类名是否有效
                    try {
                        Class<?> cls = Class.forName(className);
                        Assert.isTrue(ClassHelper.isSubClass(cls, BizClassifyAbstractEntity.class),
                                      String.format("[%s]不是BizClassifyAbstractEntity.class的子类!", className));
                    } catch (ClassNotFoundException e) {
                        Assert.isTrue(false, String.format("[%s]类未找到!", className));
                    }
                }
            }
            this.bizClassificationDetailRepository.save(details);
        }
    }

    @Override
    public List<Map<String, Object>> queryBizClassificationsByPermission(BizClassificationQueryRequest bizClassificationQueryRequest) {
        Assert.notNull(bizClassificationQueryRequest.getParentId(), MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        List<Map<String, Object>> list = null;
        if (bizClassificationQueryRequest.getParentId().equals(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID)) {
            list = new ArrayList<Map<String, Object>>();
            Map<String, Object> rootMap = new HashMap<String, Object>();
            rootMap.put("id", "1");
            rootMap.put("code", "root");
            rootMap.put("name", "业务分类配置");
            rootMap.put("fullId", "/root");
            rootMap.put("fullName", "/业务分类配置");
            rootMap.put("hasChildren", "1");
            list.add(rootMap);
        } else {
            String personId = bizClassificationQueryRequest.getOperator().getUserId();
            QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizclassification");
            String sql = query.getSqlByName("queryTreeByPermission");
            list = this.sqlExecutorDao.queryToListMap(sql, personId, PermissionResourceKind.BUSINESSCLASS.getId(), PermissionNodeKind.BUSINESSCLASS.getId(),
                                                      bizClassificationQueryRequest.getParentId());
        }
        return list;
    }

    @Override
    public Map<String, Object> queryBizClassificationDetails(BizClassificationQueryRequest bizClassificationQueryRequest) {
        bizClassificationQueryRequest.checkBizClassificationId();
        QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizclassificationdetail");
        return this.sqlExecutorDao.executeQuery(query, bizClassificationQueryRequest);
    }

    @Override
    public List<Map<String, Object>> queryByClassificationId(String bizClassificationId) {
        QueryDescriptor query = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "bizclassificationdetail");
        String sql = query.getSqlByName("queryByClassificationId");
        List<Map<String, Object>> list = this.sqlExecutorDao.queryToListMap(sql, bizClassificationId);
        for (Map<String, Object> map : list) {
            String bizType = ClassHelper.convert(map.get("bizType"), String.class, "");
            // 获取业务表名
            if (bizType.equals(BizDlassificationType.BIZ_TABLE.getId())) {
                String className = ClassHelper.convert(map.get("entityClassName"), String.class, "");
                Class<? extends BizClassifyAbstractEntity> cls = getBizClassifyEntityClassByName(className);
                map.put("tableName", EntityUtil.getTableName(cls));
            } else {
                map.put("tableName", "");
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> queryClassificationVisibleFields(String bizPropertyId) {
        return this.flexFieldApplication.queryVisibleFields(bizPropertyId);
    }

    @Override
    public Class<? extends BizClassifyAbstractEntity> getBizClassifyEntityClass(String bizclassificationdetailId) {
        BizClassificationDetail bizClassificationDetail = bizClassificationDetailRepository.findOne(bizclassificationdetailId);
        String className = bizClassificationDetail.getEntityClassName();
        return getBizClassifyEntityClassByName(className);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends BizClassifyAbstractEntity> getBizClassifyEntityClassByName(String className) {
        Assert.hasText(className, "调用getEntityClassName为空。");
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
            Assert.isTrue(ClassHelper.isSubClass(cls, BizClassifyAbstractEntity.class), String.format("[%s]不是BizClassifyAbstractEntity.class的子类!", className));
        } catch (ClassNotFoundException e) {
            Assert.isTrue(false, String.format("[%s]类未找到!", className));
        }
        return (Class<? extends BizClassifyAbstractEntity>) cls;
    }
}
