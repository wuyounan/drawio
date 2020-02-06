package com.huigou.index.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.index.application.IndexClassificationDimApplication;
import com.huigou.index.domain.model.IndexClassification;
import com.huigou.index.domain.model.IndexClassificationDim;
import com.huigou.index.repository.IndexClassificationDimRepository;
import com.huigou.index.repository.IndexClassificationRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;

/**
 * 指标分类维度应用
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 * @version V1.0
 */
@Service("indexClassificationDimApplication")
public class IndexClassificationDimApplicationImpl extends BaseApplication implements IndexClassificationDimApplication {
    @Autowired
    private IndexClassificationDimRepository indexClassificationDimRepository;

    @Autowired
    private IndexClassificationRepository indexClassificationRepository;

    @Override
    @Transactional
    public String saveIndexClassificationDim(IndexClassificationDim indexClassificationDim) {
        Assert.notNull(indexClassificationDim, CommonDomainConstants.OBJECT_NOT_NULL);
        indexClassificationDim = (IndexClassificationDim) this.commonDomainService.loadAndFillinProperties(indexClassificationDim);
        indexClassificationDim = (IndexClassificationDim) this.commonDomainService.saveBaseInfoEntity(indexClassificationDim, indexClassificationDimRepository);
        return indexClassificationDim.getId();
    }

    @Override
    public IndexClassificationDim loadIndexClassificationDim(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return indexClassificationDimRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteIndexClassificationDims(List<String> ids) {
        Assert.notEmpty(ids, CommonDomainConstants.IDS_NOT_BLANK);
        List<IndexClassificationDim> objs = indexClassificationDimRepository.findAll(ids);
        IndexClassification indexClassification;

        for (IndexClassificationDim obj : objs) {
            indexClassification = indexClassificationRepository.findFirstByDimId(obj.getId());
            if (indexClassification != null) {
                Assert.state(false, String.format(CommonDomainConstants.OBJECT_REFERENCED_BY_WHO, obj.getName(), indexClassification.getName()));
            }
        }

        indexClassificationDimRepository.delete(objs);
    }

    @Override
    @Transactional
    public void updateIndexClassificationDimsStatus(List<String> ids, Integer status) {
        Assert.notEmpty(ids, CommonDomainConstants.IDS_NOT_BLANK);
        Assert.notNull(status, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "status"));
        this.commonDomainService.updateStatus(IndexClassificationDim.class, ids, status);
    }

    @Override
    @Transactional
    public Integer getIndexClassificationDimNextSequence() {
        return this.commonDomainService.getNextSequence(IndexClassificationDim.class);
    }

    @Override
    @Transactional
    public void updateIndexClassificationDimsSequence(Map<String, Integer> params) {
        Assert.notNull(params, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "params"));
        this.commonDomainService.updateSequence(IndexClassificationDim.class, params);
    }

    @Override
    public List<Map<String, Object>> queryEnabledAll() {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, ENTITY_NAME);
        String sql = queryDescriptor.getSqlByName("queryEnabledAll");
        return this.sqlExecutorDao.queryToListMap(sql);
    }

    @Override
    public Map<String, Object> slicedQueryIndexClassificationDims(CodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, ENTITY_NAME);
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

}
