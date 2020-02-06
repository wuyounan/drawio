package com.huigou.index.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.index.application.IndexApplication;
import com.huigou.index.domain.model.Index;
import com.huigou.index.domain.model.IndexAttachClassification;
import com.huigou.index.domain.model.IndexClassification;
import com.huigou.index.domain.model.IndexEntry;
import com.huigou.index.domain.model.IndexEntryFormulaParam;
import com.huigou.index.domain.query.IndexEntryEntryQueryRequest;
import com.huigou.index.domain.query.IndexQueryRequest;
import com.huigou.index.repository.IndexAttachClassificationRepository;
import com.huigou.index.repository.IndexClassificationRepository;
import com.huigou.index.repository.IndexEntryRepository;
import com.huigou.index.repository.IndexRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 指标接口实现
 * 
 * @ClassName: IndexApplicationImpl
 * @author
 * @date 2017-09-25 14:58
 * @version V1.0
 */
@Service("indexApplication")
public class IndexApplicationImpl extends BaseApplication implements IndexApplication {

    private static final String CAN_NOT_DELETE_INDEX_ENTRY_HAS_PARAM = "排序号为%s的指标明细存在指标参数，不能删除。";

    private static final String CAN_NOT_DELETE_INDEX_ENTRY_HAS_ALERT_LEVEL = "排序号为%s的指标明细存在预警级别，不能删除。";

    private static final String CAN_NOT_DELETE_INDEX_HAS_ENTRY = "指标“%s”存在指标明细，不能删除。";

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private IndexEntryRepository indexEntryRepository;


    @Autowired
    private IndexClassificationRepository indexClassificationRepository;
    	
    @Autowired
    private IndexAttachClassificationRepository indexAttachClassificationRepository;

  

    @Override
    @Transactional
    public String saveIndex(Index index) {
        Assert.notNull(index, CommonDomainConstants.OBJECT_NOT_NULL);
        IndexAttachClassification indexAttachClassification = null;
        if (index.isNew()) {
            indexAttachClassification = new IndexAttachClassification();
            indexAttachClassification.setClassificationId(index.getClassificationId());
        }
        index = (Index) this.commonDomainService.loadAndFillinProperties(index);
        index = (Index) this.commonDomainService.saveBaseInfoEntity(index, indexRepository);

        if (indexAttachClassification != null && StringUtil.isNotBlank(indexAttachClassification.getClassificationId())) {
            indexAttachClassification.setIndexId(index.getId());
            saveIndexAttachClassification(indexAttachClassification);
        }

      /*  HanaIndex hanaIndex = new HanaIndex();
        BeanUtils.copyProperties(index, hanaIndex);
        indexSynchronizer.saveIndex(hanaIndex);*/

        return index.getId();
    }

    @Override
    public Index loadIndex(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return indexRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteIndexes(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<Index> objs = indexRepository.findAll(ids);
        int count;
        for (Index obj : objs) {
            count = indexEntryRepository.countByIndexId(obj.getId());
            Assert.state(count == 0, String.format(CAN_NOT_DELETE_INDEX_HAS_ENTRY, obj.getName()));
        }
        List<IndexAttachClassification> attachs = indexAttachClassificationRepository.findByIndexIdIn(ids);
        List<String> indexAttachClassificationIDs = new ArrayList<String>(attachs.size());
        for (IndexAttachClassification attach : attachs) {
            indexAttachClassificationIDs.add(attach.getId());
        }


        indexAttachClassificationRepository.delete(attachs);
        indexRepository.delete(objs);
    }

    @Override
    public Map<String, Object> slicedQueryIndexes(IndexQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, INDEX_ENTITY_NAME);
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void updateIndexsSequence(Map<String, Integer> params) {
        Assert.isTrue(params.size() > 0, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "params"));
        this.commonDomainService.updateSequence(Index.class, params);
    }

    @Override
    @Transactional
    public void updateIndexsStatus(List<String> ids, Integer status) {
        checkIdsNotEmpty(ids);
        Assert.notNull(status, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, CommonDomainConstants.STATUS_FIELD_NAME));
        this.commonDomainService.updateStatus(Index.class, ids, status);
    }

    @Override
    @Transactional
    public Integer getIndexNextSequence() {
        return this.commonDomainService.getNextSequence(Index.class);
    }

    @Override
    @Transactional
    public String saveIndexAttachClassification(IndexAttachClassification indexAttachClassification) {
        Assert.notNull(indexAttachClassification, CommonDomainConstants.OBJECT_NOT_NULL);
        indexAttachClassification = (IndexAttachClassification) this.commonDomainService.loadAndFillinProperties(indexAttachClassification);
        indexAttachClassification = indexAttachClassificationRepository.save(indexAttachClassification);

        /*HanaIndexAttachClassification hanaAttach = new HanaIndexAttachClassification();
        BeanUtils.copyProperties(indexAttachClassification, hanaAttach);
        indexSynchronizer.saveIndexAttachClassification(hanaAttach);
        */
        return indexAttachClassification.getId();
    }

    @Override
    @Transactional
    public void moveIndexs(List<String> ids, String classificationId) {
        this.checkIdsNotEmpty(ids);
        List<IndexAttachClassification> attachs = indexAttachClassificationRepository.findByIndexIdIn(ids);

        List<String> indexIds = new ArrayList<String>(ids.size());

        for (IndexAttachClassification attach : attachs) {
            attach.setClassificationId(classificationId);

            indexIds.add(attach.getIndexId());

        }

        List<Index> indexes = this.indexRepository.findAll(indexIds);
        for (Index index : indexes) {
            index.setClassificationId(classificationId);

        }

        indexAttachClassificationRepository.save(attachs);
    }

    @Override
    @Transactional
    public String saveIndexEntry(IndexEntry indexEntry) {
        Assert.notNull(indexEntry, CommonDomainConstants.OBJECT_NOT_NULL);
        indexEntry = (IndexEntry) this.commonDomainService.loadAndFillinProperties(indexEntry);

        indexEntry.buildDetails();

        indexEntry = indexEntryRepository.save(indexEntry);

       /* HanaIndexEntry hanaIndexEntry = new HanaIndexEntry();
        BeanUtils.copyProperties(indexEntry, hanaIndexEntry);
        indexSynchronizer.saveIndexEntry(hanaIndexEntry);
       
        if (indexEntry.getIndexEntryFormulaParams() != null) {
            List<HanaIndexEntryParam> hanaIndexEntryParams = new ArrayList<HanaIndexEntryParam>(indexEntry.getIndexEntryFormulaParams().size());
            HanaIndexEntryParam hanaIndexEntryParam;
            for (IndexEntryFormulaParam item : indexEntry.getIndexEntryFormulaParams()) {
                hanaIndexEntryParam = new HanaIndexEntryParam();
                BeanUtils.copyProperties(item, hanaIndexEntryParam);
                hanaIndexEntryParam.setEntryId(indexEntry.getId());
                hanaIndexEntryParams.add(hanaIndexEntryParam);
            }

            if (hanaIndexEntryParams.size() > 0) {
                indexSynchronizer.saveIndexEntryParams(hanaIndexEntryParams);
            }
        }
        */
     
        return indexEntry.getId();
    }

    @Override
    public IndexEntry loadIndexEntry(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        IndexEntry findOne = indexEntryRepository.findOne(id);
        System.out.println(findOne);
        
        return indexEntryRepository.findOne(id);
    }
    @Override
    public IndexClassification loadIndexClassification(String id) {
    	Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
    	return indexClassificationRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteIndexEntries(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<IndexEntry> objs = indexEntryRepository.findAll(ids);
        // 验证指标参数、指标预警级别
        int countParam;
        for (IndexEntry obj : objs) {
            countParam = obj.getIndexEntryFormulaParams().size();
            Assert.state(countParam == 0, String.format(CAN_NOT_DELETE_INDEX_ENTRY_HAS_PARAM, obj.getSequence()));
        }

        indexEntryRepository.delete(objs);
    }

    private IndexEntry checkDeleteIndexEntryEntryParamsAndReturn(String entryId, List<String> ids) {
        Assert.hasText(entryId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "entryId"));
        this.checkIdsNotEmpty(ids);
        IndexEntry indexEntry = this.indexEntryRepository.findOne(entryId);
        Assert.state(indexEntry != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, entryId, "指标明细"));
        return indexEntry;
    }

    @Override
    @Transactional
    public void deleteIndexEntryFormulaParams(String entryId, List<String> ids) {
        IndexEntry indexEntry = checkDeleteIndexEntryEntryParamsAndReturn(entryId, ids);
        indexEntry.removeDetails(indexEntry.getIndexEntryFormulaParams(), ids);

    }

    @Override
    @Transactional
    public void deleteIndexEntryUIParams(String entryId, List<String> ids) {
        IndexEntry indexEntry = checkDeleteIndexEntryEntryParamsAndReturn(entryId, ids);
        indexEntry.removeDetails(indexEntry.getIndexEntryUIParams(), ids);
    }

    @Override
    @Transactional
    public void deleteIndexEntryTabs(String entryId, List<String> ids) {
        IndexEntry indexEntry = checkDeleteIndexEntryEntryParamsAndReturn(entryId, ids);
        indexEntry.removeDetails(indexEntry.getIndexEntryTabs(), ids);
    }

    @Override
    @Transactional
    public Integer getIndexEntryNextSequence(String indexId) {
        Assert.hasText(indexId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "indexId"));
        return this.commonDomainService.getNextSequence(IndexEntry.class, "indexId", indexId);
    }

    @Override
    @Transactional
    public void updateIndexEntriesSequence(Map<String, Integer> params) {
        Assert.isTrue(params.size() > 0, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "params"));
        this.commonDomainService.updateSequence(IndexEntry.class, params);
    }

    @Override
    public Map<String, Object> slicedQueryIndexEntries(IndexQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, INDEX_ENTRY_ENTITY_NAME);
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    private void checkIndexEntryEntryQueryRequest(IndexEntryEntryQueryRequest queryRequest) {
        Assert.notNull(queryRequest, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "queryRequest"));
        queryRequest.checkConstraints();
    }

    @Override
    public Map<String, Object> queryIndexEntryUIParams(IndexEntryEntryQueryRequest queryRequest) {
        checkIndexEntryEntryQueryRequest(queryRequest);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, INDEX_ENTRY_UI_PARAM_ENTITY_NAME);
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> queryIndexEntryTabs(IndexEntryEntryQueryRequest queryRequest) {
        checkIndexEntryEntryQueryRequest(queryRequest);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, INDEX_ENTRY_TAB_ENTITY_NAME);

        String sql = queryDescriptor.getSqlByName("selectTableColumnsJson");

        String tableColumnsJson;

        Map<String, Object> data = this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(Constants.ROWS);
        for (Map<String, Object> item : list) {
            tableColumnsJson = this.sqlExecutorDao.getSqlQuery().getJDBCDao().loadClob(sql, item.get("id"));
            item.put("tableColumnsJson", tableColumnsJson);
        }

        return data;
    }

    @Override
    public Map<String, Object> queryIndexEntryFormulaParams(IndexEntryEntryQueryRequest queryRequest) {
        checkIndexEntryEntryQueryRequest(queryRequest);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, INDEX_ENTRY_FORMULA_PARAM_ENTITY_NAME);
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("dataType", DictUtil.getDictionary("fieldTypeList"));
        return this.sqlExecutorDao.executeQuery(model);
    }

    @Override
    public Map<String, Object> queryIndexEntriesByIndexId(String indexId) {
        Assert.hasText(indexId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "indexId"));
        IndexQueryRequest queryRequest = new IndexQueryRequest();
        queryRequest.setIndexId(indexId);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, INDEX_ENTRY_ENTITY_NAME);
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }
}
