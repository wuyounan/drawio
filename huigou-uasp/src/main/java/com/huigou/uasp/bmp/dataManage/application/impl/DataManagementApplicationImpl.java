package com.huigou.uasp.bmp.dataManage.application.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.datamanagement.DataResourceKind;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManageTypeApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManagementApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetail;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetailresource;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagement;
import com.huigou.uasp.bmp.dataManage.domain.query.DataManagePermissionsQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagedetailQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagedetailresourceQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagementQueryRequest;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagedetailRepository;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagedetailresourceRepository;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagementRepository;
import com.huigou.util.ClassHelper;

/**
 * 数据权限授权
 * 
 * @ClassName: DataManagementApplicationImpl
 * @author xx
 * @date 2018-09-05 17:15
 * @version V1.0
 */
@Service("dataManagementApplication")
public class DataManagementApplicationImpl extends BaseApplication implements DataManagementApplication {
    @Autowired
    private OpdatamanagedetailRepository opdatamanagedetailRepository;

    @Autowired
    private OpdatamanagedetailresourceRepository opdatamanagedetailresourceRepository;

    @Autowired
    private OpdatamanagementRepository opdatamanagementRepository;

    @Autowired
    private DataManageTypeApplication dataManageTypeApplication;

    @Override
    public Integer countDatamanagedetailByDataManageId(String dataManageId) {
        return opdatamanagedetailRepository.countByDataManageId(dataManageId);
    }

    @Override
    public Map<String, String> queryDataManageResourcekindByTypeId(String id) {
        List<Map<String, Object>> list = dataManageTypeApplication.queryOpdatamanagetypekindByTypeId(id);
        Map<String, String> map = new LinkedHashMap<>();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> m : list) {
                map.put(ClassHelper.convert(m.get("id"), String.class), ClassHelper.convert(m.get("name"), String.class));
            }
        }
        return map;
    }

    @Override
    @Transactional
    public String saveOpdatamanagedetail(Opdatamanagedetail opdatamanagedetail) {
        Assert.notNull(opdatamanagedetail, CommonDomainConstants.OBJECT_NOT_NULL);
        opdatamanagedetail = (Opdatamanagedetail) this.commonDomainService.loadAndFillinProperties(opdatamanagedetail);
        opdatamanagedetail = opdatamanagedetailRepository.save(opdatamanagedetail);
        return opdatamanagedetail.getId();
    }

    @Override
    public Opdatamanagedetail loadOpdatamanagedetail(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return opdatamanagedetailRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteOpdatamanagedetail(List<String> ids) {
        Assert.notEmpty(ids, CommonDomainConstants.ID_NOT_BLANK);
        Long count = 0l;
        for (String id : ids) {
            Opdatamanagedetail opdatamanagedetail = opdatamanagedetailRepository.findOne(id);
            count = opdatamanagementRepository.countByDataManagedetalId(id);
            Assert.isTrue(count.equals(0l), String.format("%s已被授权无法删除!", opdatamanagedetail.getName()));
            opdatamanagedetailRepository.delete(opdatamanagedetail);
            opdatamanagedetailresourceRepository.deleteByDataManagedetalId(id);
        }
    }

    @Override
    public Map<String, Object> slicedQueryOpdatamanagedetail(OpdatamanagedetailQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagedetail");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void saveOpdatamanagedetailresource(Opdatamanagedetailresource opdatamanagedetailresource) {
        Assert.notNull(opdatamanagedetailresource, CommonDomainConstants.OBJECT_NOT_NULL);
        opdatamanagedetailresource.checkConstraints();
        // 校验数据是否重复
        Integer count = opdatamanagedetailresourceRepository.countByDataManagedetalIdAndDataKindIdAndResourceKey(opdatamanagedetailresource.getDataManagedetalId(),
                                                                                                                 opdatamanagedetailresource.getDataKindId(),
                                                                                                                 opdatamanagedetailresource.getResourceKey());
        if (count == 0) {
            opdatamanagedetailresource = (Opdatamanagedetailresource) this.commonDomainService.loadAndFillinProperties(opdatamanagedetailresource);
            opdatamanagedetailresource = opdatamanagedetailresourceRepository.save(opdatamanagedetailresource);
        }
    }

    @Override
    @Transactional
    public void deleteOpdatamanagedetailresource(List<String> ids) {
        List<Opdatamanagedetailresource> objs = opdatamanagedetailresourceRepository.findAll(ids);
        opdatamanagedetailresourceRepository.delete(objs);
    }

    @Override
    public Map<String, Object> queryOpdatamanagedetailresource(OpdatamanagedetailresourceQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagedetailresource");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("dataKind", DataResourceKind.getData());
        model.setDefaultOrderBy("sequence,data_kind_id");
        return this.sqlExecutorDao.executeQuery(model);
    }

    @Override
    @Transactional
    public void saveOpdatamanagement(String managerId, List<Opdatamanagement> datas) {
        Assert.hasText(managerId, "管理者ID不能为空!");
        Assert.notEmpty(datas, "数据管理权限不能为空!");
        List<Opdatamanagement> list = new ArrayList<>(datas.size());
        Integer count = 0;
        for (Opdatamanagement obj : datas) {
            obj.checkConstraints();
            count = opdatamanagementRepository.countByManagerIdAndDataManagedetalId(managerId, obj.getDataManagedetalId());
            if (count == 0) {
                obj.setManagerId(managerId);
                list.add(obj);
            }
        }
        if (list.size() > 0) {
            opdatamanagementRepository.save(list);
        }
    }

    @Override
    @Transactional
    public void deleteOpdatamanagement(List<String> ids) {
        List<Opdatamanagement> objs = opdatamanagementRepository.findAll(ids);
        opdatamanagementRepository.delete(objs);
    }

    @Override
    public Map<String, Object> queryOpdatamanagement(OpdatamanagementQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagement");
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryDataManagementByOrgFullId(OpdatamanagementQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryDataManagementByOrgFullId");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryDataManageResourceByOrgFullId(OpdatamanagementQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryDataManageResourceByOrgFullId");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("dataKind", DataResourceKind.getData());
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    public Map<String, Object> slicedQueryDataManageByDetailId(DataManagePermissionsQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryDataManagementByDetailId");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    @Deprecated
    public Map<String, Object> slicedQueryPersonAsDataManage(DataManagePermissionsQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = null;
        if (queryRequest.isSingle()) {
            queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "querySinglePersonAsDataManagement");
        } else {
            queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryPersonAsDataManagement");
        }
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

}
