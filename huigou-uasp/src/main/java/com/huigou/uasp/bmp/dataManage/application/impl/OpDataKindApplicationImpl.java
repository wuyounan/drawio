package com.huigou.uasp.bmp.dataManage.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.datamanagement.DataResourceKind;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManageTypeApplication;
import com.huigou.uasp.bmp.dataManage.application.OpDataKindApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatakind;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatakindQueryRequest;
import com.huigou.uasp.bmp.dataManage.repository.OpdatakindRepository;
import com.huigou.util.ClassHelper;

/**
 * 数据管理权限维度定义
 * 
 * @ClassName: OpDataKindApplicationImpl
 * @author xx
 * @date 2018-09-04 10:52
 * @version V1.0
 */
@Service("opDataKindApplication")
public class OpDataKindApplicationImpl extends BaseApplication implements OpDataKindApplication {
    @Autowired
    private OpdatakindRepository opdatakindRepository;

    @Autowired
    private DataManageTypeApplication dataManageTypeApplication;

    @Override
    @Transactional
    public String saveOpdatakind(Opdatakind opdatakind) {
        Assert.notNull(opdatakind, CommonDomainConstants.OBJECT_NOT_NULL);
        if (opdatakind.isNew()) {
            opdatakind.setSequence(this.commonDomainService.getNextSequence(Opdatakind.class));
        }
        opdatakind = (Opdatakind) this.commonDomainService.loadAndFillinProperties(opdatakind);
        opdatakind = (Opdatakind) this.commonDomainService.saveBaseInfoEntity(opdatakind, opdatakindRepository);
        return opdatakind.getId();
    }

    @Override
    public Opdatakind loadOpdatakind(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return opdatakindRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteOpdatakind(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        Long count = dataManageTypeApplication.countDatamanagetypekindById(id);
        Assert.isTrue(count.equals(0l), "数据管理权限维度已被使用，无法删除!");
        opdatakindRepository.delete(id);
    }

    @Override
    public Map<String, Object> slicedQueryOpdatakind(OpdatakindQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatakind");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("dataKind", DataResourceKind.getData());
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    @Transactional
    public void updateOpdatakindSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(Opdatakind.class, map);
    }

    @Override
    public List<Opdatakind> findAll() {
        return opdatakindRepository.findAllByOrderBySequenceAsc();
    }

    @Override
    public Map<String, Object> findById(String id) {
        Opdatakind kind = opdatakindRepository.findOne(id);
        Assert.notNull(kind, "未找到对应的资源维度定义!");
        return ClassHelper.toMap(kind);
    }

}
