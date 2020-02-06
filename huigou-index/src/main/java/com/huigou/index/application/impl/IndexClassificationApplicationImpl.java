package com.huigou.index.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.index.application.IndexClassificationApplication;
import com.huigou.index.domain.model.IndexClassification;
import com.huigou.index.domain.model.IndexClassificationDim;
import com.huigou.index.domain.query.IndexClassificationQueryRequest;
import com.huigou.index.repository.IndexClassificationDimRepository;
import com.huigou.index.repository.IndexClassificationRepository;
import com.huigou.index.repository.IndexRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.util.CommonUtil;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 指标分类应用
 * 
 * @author gongmm
 */
@Service("indexClassificationApplication")
public class IndexClassificationApplicationImpl extends BaseApplication implements IndexClassificationApplication {
    @Autowired
    private IndexClassificationRepository indexClassificationRepository;

    @Autowired
    private IndexClassificationDimRepository indexClassificationDimRepository;

   
    
    @Autowired
    private IndexRepository indexRepository;

    private void synchronizeAllIndexClassifications() {
        List<IndexClassification> list = this.indexClassificationRepository.findAll();
       
    }

    @Override
    @Transactional
    public String saveIndexClassification(IndexClassification indexClassification) {
        synchronizeAllIndexClassifications();
        Assert.notNull(indexClassification, CommonDomainConstants.OBJECT_NOT_NULL);
        String oldName = null, oldFullName = null;
        boolean updatedName = false;
        if (!indexClassification.isNew()) {
            IndexClassification oldIndexClassification = indexClassificationRepository.findOne(indexClassification.getId());
            Assert.state(oldIndexClassification != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, indexClassification.getId(), "指标分类"));
            oldName = oldIndexClassification.getName();
            oldFullName = oldIndexClassification.getFullName();

            updatedName = indexClassification.isUpdateName(oldName);
        }

        indexClassification = (IndexClassification) this.commonDomainService.loadAndFillinProperties(indexClassification);
        indexClassification = (IndexClassification) this.commonDomainService.saveTreeEntity(indexClassification, indexClassificationRepository, oldName);

        IndexClassification parent = this.indexClassificationRepository.findOne(indexClassification.getParentId());
        if (parent == null) {
            IndexClassificationDim classificationDim = indexClassificationDimRepository.findOne(indexClassification.getDimId());
            if (classificationDim != null) {
                String fullId = CommonUtil.createFileFullName(classificationDim.getId(), indexClassification.getId(), "");
                String fullName = CommonUtil.createFileFullName(classificationDim.getName(), indexClassification.getName(), "");
                indexClassification.setFullId(fullId);
                indexClassification.setFullName(fullName);
                indexClassification = indexClassificationRepository.save(indexClassification);
                if (updatedName) {
                    commonDomainService.updateChildenFullName(IndexClassification.class, indexClassification.getFullId(), oldFullName,
                                                              indexClassification.getFullName());
                }
            }
        }

       
        return indexClassification.getId();
    }

    @Override
    @Transactional
    public Integer getIndexClassificationNextSequence(String parentId) {
        return this.commonDomainService.getNextSequence(IndexClassification.class, CommonDomainConstants.PARENT_ID_FIELD_NAME, parentId);
    }

    @Override
    public IndexClassification loadIndexClassification(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return indexClassificationRepository.findOne(id);
    }

    @Override
    @Transactional
    public void updateIndexClassificationsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(IndexClassification.class, params);
    }

    @Override
    @Transactional
    public void updateIndexClassificationsStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(IndexClassification.class, ids, status);
    }

    public void deleteIndexClassifications(List<String> ids) {
        Assert.notEmpty(ids, CommonDomainConstants.IDS_NOT_BLANK);
        List<IndexClassification> objs = indexClassificationRepository.findAll(ids);

        int count;
        for (IndexClassification obj : objs) {
            count = indexClassificationRepository.countByParentId(obj.getId());
            Assert.state(count == 0, String.format(CommonDomainConstants.CAN_NOT_DELETE_HAS_CHILDREN, obj.getName()));
            // TODO 分类下已有指标不能删除
            count = indexRepository.countByclassificationId(obj.getId());
            Assert.state(count == 0, String.format(CommonDomainConstants.OBJECT_REFERENCED_BY_WHO, obj.getName(), "指标"));
        }

        indexClassificationRepository.delete(objs);
    }

    @Override
    public Map<String, Object> queryIndexClassifications(IndexClassificationQueryRequest queryRequest) {
        Assert.isTrue(queryRequest != null && StringUtil.isNotBlank(queryRequest.getParentId()), "查询模型的“parentId”不能为空。");

        if (IndexClassification.ROOT_PARENT_ID.equals(queryRequest.getParentId())) {
            Map<String, Object> result = new HashMap<String, Object>(1);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(1);

            Map<String, Object> root = new HashMap<String, Object>(5);
            root.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, IndexClassification.ROOT_PARENT_ID);
            root.put(CommonDomainConstants.ID_FIELD_NAME, IndexClassification.ROOT_ID);
            root.put(CommonDomainConstants.CODE_FIELD_NAME, "ZBFL");
            root.put(CommonDomainConstants.NAME_FIELD_NAME, "指标分类");
            root.put("hasChildren", 1);

            list.add(root);
            result.put(Constants.ROWS, list);
            return result;
        }
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, ENTITY_NAME);
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest, "queryForTree");
    }

    @Override
    public Map<String, Object> slicedQueryIndexClassifications(ParentAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, ENTITY_NAME);
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

}
