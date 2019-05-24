package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.CheckBaseInfoDuplicateParameter;
import com.huigou.data.i18n.model.I18nDataModel;
import com.huigou.data.i18n.model.I18nInitResourceInterface;
import com.huigou.data.jdbc.util.BatchSqlUpdateDetail;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.I18npropertiesApplication;
import com.huigou.uasp.bmp.configuration.domain.model.I18nproperties;
import com.huigou.uasp.bmp.configuration.domain.query.I18npropertiesQueryRequest;
import com.huigou.uasp.bmp.configuration.repository.I18npropertiesRepository;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;

/**
 * 数据库国际化资源读取
 * 
 * @ClassName: I18npropertiesApplicationImpl
 * @author xx
 * @date 2017-09-29 10:23
 * @version V1.0
 */
@Service("i18npropertiesApplication")
public class I18npropertiesApplicationImpl extends BaseApplication implements I18npropertiesApplication {
    @Autowired
    private I18npropertiesRepository i18npropertiesRepository;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public String saveI18nproperties(I18nproperties i18nproperties) {
        Assert.notNull(i18nproperties, CommonDomainConstants.OBJECT_NOT_NULL);
        i18nproperties = (I18nproperties) this.commonDomainService.loadAndFillinProperties(i18nproperties);
        CheckBaseInfoDuplicateParameter checkParameter = new CheckBaseInfoDuplicateParameter();
        checkParameter.setCheckCode(i18nproperties.getId(), i18nproperties.getCode());
        checkParameter.checkConstraints();
        List<I18nproperties> duplicateEntities = (List<I18nproperties>) this.commonDomainService.findDuplicateEntities(I18nproperties.class, checkParameter);
        I18nproperties other = null;
        if (duplicateEntities.size() > 0) {
            other = duplicateEntities.get(0);
        }
        i18nproperties.checkConstraints(other);
        i18nproperties = i18npropertiesRepository.save(i18nproperties);
        return i18nproperties.getId();
    }

    @Override
    public I18nproperties loadI18nproperties(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return i18npropertiesRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteI18nproperties(List<String> ids) {
        List<I18nproperties> objs = i18npropertiesRepository.findAll(ids);
        i18npropertiesRepository.delete(objs);
    }

    @Override
    public Map<String, Object> slicedQueryI18nproperties(I18npropertiesQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "i18nproperties");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void moveI18nProperties(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);
        this.commonDomainService.moveForFolder(I18nproperties.class, ids, folderId);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void initI18nproperties(String folderId, String resourcekind) {
        Assert.hasText(folderId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "folderId"));
        Assert.hasText(resourcekind, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "resourcekind"));
        I18nInitResourceInterface resourceInterface = ApplicationContextWrapper.getBean(resourcekind, I18nInitResourceInterface.class);
        Assert.notNull(resourceInterface, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, resourcekind));
        List<I18nDataModel> list = (List<I18nDataModel>) resourceInterface.loadI18nInitResources();
        if (list == null || list.size() == 0) {
            return;
        }
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "i18nproperties");
        String insertSql = queryDescriptor.getSqlByName("insertSql");
        String updataSql = queryDescriptor.getSqlByName("updataSql");
        BatchSqlUpdateDetail batchInsertDetail = BatchSqlUpdateDetail.newInstance(this.sqlExecutorDao.getDataSource(), insertSql, null);
        BatchSqlUpdateDetail batchUpdateDetail = BatchSqlUpdateDetail.newInstance(this.sqlExecutorDao.getDataSource(), updataSql, null);
        boolean insertFlag = false;
        boolean updateFlag = false;
        int count = 0;
        for (I18nDataModel model : list) {
            I18nproperties obj = new I18nproperties();
            obj.setId(CommonUtil.createGUID());
            obj.setCode(model.getCode());
            obj.setValue1(model.getValue());
            obj.setFolderId(folderId);
            obj.setResourceKind(resourcekind);
            // 判断编码不能重复
            count = i18npropertiesRepository.countByCode(model.getCode());
            if (count == 0) {
                batchInsertDetail.setRows(obj);
                insertFlag = true;
            } else {
                batchUpdateDetail.setRows(obj);
                updateFlag = true;
            }
        }
        if (insertFlag) {
            batchInsertDetail.flush();
        }
        if (updateFlag) {
            batchUpdateDetail.flush();
        }
    }

    @Override
    public void syncCache() {
        // 数据字典中定义的国际化类别
        List<Map<String, Object>> i18nLanguageList = DictUtil.getDictionaryList("i18nLanguage");
        if (i18nLanguageList == null || i18nLanguageList.size() == 0) {
            i18nLanguageList = new ArrayList<>(1);
            Map<String, Object> m = new HashMap<String, Object>(2);
            Locale locale = MessageSourceContext.getLocale();
            // {code=i18nLanguage, i18NKey=dictionary.i18nLanguage.zh_CN, name=中文, typeId=value1, value=zh_CN}
            m.put("typeId", "value1");
            m.put("value", String.format("%s_%s", locale.getLanguage(), locale.getCountry()));
            i18nLanguageList.add(m);
        }
        Map<String, String> values = new HashMap<String, String>(i18nLanguageList.size());
        Map<String, Map<String, String>> languages = new HashMap<String, Map<String, String>>(i18nLanguageList.size());
        for (Map<String, Object> m : i18nLanguageList) {
            languages.put(m.get("value").toString(), new HashMap<String, String>());
            values.put(m.get("value").toString(), m.get("typeId").toString());
        }
        // 查询全部国际化资源定义
        List<I18nproperties> objs = i18npropertiesRepository.findAll();
        if (objs == null || objs.size() == 0) {
            return;
        }
        Map<String, String> language = null;
        for (I18nproperties obj : objs) {
            for (String value : values.keySet()) {
                language = languages.get(value);
                // 通过反射读取对象中的数据
                language.put(obj.getCode(), ClassHelper.getProperty(obj, values.get(value)));
            }
        }
        if (values.size() > 0) {
            // 加入缓存
            SystemCache.removeI18nProperties();
            for (String value : values.keySet()) {
                SystemCache.setI18nProperties(value, (HashMap<String, String>) languages.get(value));
            }
        }
    }
}
