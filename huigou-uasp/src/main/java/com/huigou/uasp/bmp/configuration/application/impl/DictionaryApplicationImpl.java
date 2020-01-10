package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictionaryDesc;
import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.DictionaryApplication;
import com.huigou.uasp.bmp.configuration.domain.model.SysDictionary;
import com.huigou.uasp.bmp.configuration.domain.model.SysDictionaryDetail;
import com.huigou.uasp.bmp.configuration.domain.query.SysDictionariesQueryRequest;
import com.huigou.uasp.bmp.configuration.repository.SysDictionaryRepository;

@Service("dictionaryApplication")
public class DictionaryApplicationImpl extends BaseApplication implements DictionaryApplication {

    @Autowired
    private SysDictionaryRepository dictionaryRepository;

    @Override
    @Transactional
    public String saveSysDictionary(SysDictionary sysDictionary) {
        Assert.notNull(sysDictionary,MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT,"sysDictionary"));
        sysDictionary = (SysDictionary) this.commonDomainService.loadAndFillinProperties(sysDictionary);

        if (sysDictionary.isNew()) {
            sysDictionary.setId(null);
            sysDictionary.setStatus(BaseInfoStatus.ENABLED.getId());
        }
        sysDictionary.buildDetails();

        sysDictionary = (SysDictionary) this.commonDomainService.saveBaseInfoWithFolderEntity(sysDictionary, dictionaryRepository);
        return sysDictionary.getId();
    }

    @Override
    public SysDictionary loadSysDictionary(String id) {
        this.checkIdNotBlank(id);
        return this.dictionaryRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteSysDictionaries(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        List<SysDictionary> sysDictionaries = this.dictionaryRepository.findAll(ids);

        for (SysDictionary item : sysDictionaries) {
            Assert.isTrue(item.getStatus() == -1, String.format("%s状态不为“草稿”状态,不能删除。", item.getName()));
        }

        this.dictionaryRepository.delete(sysDictionaries);
    }

    @Override
    @Transactional
    public void moveSysDictionaries(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);

        this.commonDomainService.moveForFolder(SysDictionary.class, ids, folderId);
    }

    @Override
    @Transactional
    public void updateSysDictionariesStatus(List<String> ids, Integer status) {
        this.checkIdsNotEmpty(ids);
        Assert.notNull(status, "参数status不能为空。");
        this.commonDomainService.updateStatus(SysDictionary.class, ids, status);
    }

    @Override
    public Map<String, Object> slicedQuerySysDictionaries(SysDictionariesQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "sysDictionary");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void deleteSysDictionaryDetails(String dictionaryId, List<String> ids) {
        Assert.hasText(dictionaryId, "参数dictionaryId不能为空。");
        this.checkIdsNotEmpty(ids);

        SysDictionary sysDictionary = this.dictionaryRepository.findOne(dictionaryId);

        Assert.notNull(sysDictionary, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, dictionaryId, "系统字典"));

        sysDictionary.removeDetails(ids);

        this.dictionaryRepository.save(sysDictionary);
    }

    @Override
    @Transactional
    public void updateSysDictionaryDetailsStatus(List<String> ids, Integer status) {
        this.checkIdsNotEmpty(ids);
        Assert.notNull(status, "参数status不能为空。");

        this.commonDomainService.updateStatus(SysDictionaryDetail.class, ids, status);
    }

    @Override
    public Map<String, Object> slicedQuerySysDictionaryDetails(ParentIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "sysDictionaryDetails");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);

    }

    @Override
    public Map<String, Object> querySysDictionaryDetailsByCode(String code) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "sysDictionaryDetails");
        String sql = queryDescriptor.getSqlByName("queryDetailsByCode");
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(sql);
        queryModel.putParam("code", code);
        queryModel.setDefaultOrderBy("sequence");
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public void syncCache() {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "sysDictionary");
        String sql = queryDescriptor.getSqlByName("queryAllEnabled");
        List<DictionaryDesc> dictionaryDesces = this.sqlExecutorDao.queryToList(sql, DictionaryDesc.class);
        SystemCache.removeDictionary();
        HashMap<String, DictionaryDesc> dictionary;
        if (dictionaryDesces != null && dictionaryDesces.size() > 0) {
            String code = dictionaryDesces.get(0).getCode();
            dictionary = new LinkedHashMap<String, DictionaryDesc>();
            for (DictionaryDesc item : dictionaryDesces) {
                if (!code.equals(item.getCode())) {
                    SystemCache.setDictionary(code, dictionary);
                    code = item.getCode();
                    dictionary = new LinkedHashMap<String, DictionaryDesc>();
                }
                dictionary.put(item.getValue(), item);
            }
            SystemCache.setDictionary(code, dictionary);
        }
    }

}
