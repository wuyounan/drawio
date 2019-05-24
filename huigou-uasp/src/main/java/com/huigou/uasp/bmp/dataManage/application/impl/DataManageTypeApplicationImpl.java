package com.huigou.uasp.bmp.dataManage.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.datamanagement.DataManageNodeKind;
import com.huigou.data.datamanagement.DataResourceKind;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManageTypeApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManagementApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetype;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetypekind;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagetypeQueryRequest;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagetypeRepository;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagetypekindRepository;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 数据管理权限类别定义
 * 
 * @ClassName: DataManageTypeApplicationImpl
 * @author xx
 * @date 2018-09-04 11:58
 * @version V1.0
 */
@Service("dataManageTypeApplication")
public class DataManageTypeApplicationImpl extends BaseApplication implements DataManageTypeApplication {
    @Autowired
    private OpdatamanagetypeRepository opdatamanagetypeRepository;

    @Autowired
    private OpdatamanagetypekindRepository opdatamanagetypekindRepository;

    @Autowired
    private DataManagementApplication dataManagementApplication;

    @Override
    @Transactional
    public String insertOpdatamanagetype(Opdatamanagetype opdatamanagetype) {
        Assert.notNull(opdatamanagetype, CommonDomainConstants.OBJECT_NOT_NULL);
        opdatamanagetype.setStatus(BaseInfoStatus.ENABLED.getId());
        opdatamanagetype.setSequence(this.commonDomainService.getNextSequence(Opdatamanagetype.class, "parentId", opdatamanagetype.getParentId()));
        opdatamanagetype = (Opdatamanagetype) this.commonDomainService.saveTreeEntity(opdatamanagetype, opdatamanagetypeRepository, opdatamanagetype.getName(),
                                                                                      true);
        return opdatamanagetype.getId();
    }

    @Override
    @Transactional
    public String updateOpdatamanagetype(Opdatamanagetype newOpdatamanagetype) {
        Assert.notNull(newOpdatamanagetype, CommonDomainConstants.OBJECT_NOT_NULL);
        Opdatamanagetype opdatamanagetype = this.opdatamanagetypeRepository.findOne(newOpdatamanagetype.getId());
        Assert.notNull(opdatamanagetype, CommonDomainConstants.LOAD_OBJECT_IS_NULL);
        String oldName = opdatamanagetype.getName();
        opdatamanagetype.fromEntity(newOpdatamanagetype);
        this.commonDomainService.saveTreeEntity(opdatamanagetype, opdatamanagetypeRepository, oldName, true);
        return opdatamanagetype.getId();
    }

    @Override
    public Opdatamanagetype loadOpdatamanagetype(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        Opdatamanagetype obj = opdatamanagetypeRepository.findOne(id);
        obj.setHasChildren(opdatamanagetypeRepository.countByParentId(id));
        return obj;
    }

    @Override
    @Transactional
    public void deleteOpdatamanagetype(List<String> ids) {
        Assert.notEmpty(ids, CommonDomainConstants.ID_NOT_BLANK);
        Opdatamanagetype obj = null;
        Integer count = 0;
        for (String id : ids) {
            obj = opdatamanagetypeRepository.findOne(id);
            // 校验是否存在子级
            count = opdatamanagetypeRepository.countByParentId(id);
            Assert.isTrue(count == 0, String.format("%s-%s存在子级无法删除!", obj.getCode(), obj.getName()));
            // 校验是否允许删除
            count = dataManagementApplication.countDatamanagedetailByDataManageId(id);
            Assert.isTrue(count == 0, String.format("%s-%s已被使用无法删除!", obj.getCode(), obj.getName()));
            opdatamanagetypekindRepository.deleteByDataManageId(id);
            opdatamanagetypeRepository.delete(obj);
        }
    }

    @Override
    public Map<String, Object> slicedQueryOpdatamanagetype(OpdatamanagetypeQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagetype");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("nodeKindId", DataManageNodeKind.getData());
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryDatamanagetypekind(OpdatamanagetypeQueryRequest queryRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        String parentId = queryRequest.getParentId();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagetype");
        if (StringUtil.isBlank(parentId)) {
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("code", "root");
            root.put("name", "全部");
            root.put("fullId", "/");
            root.put("delay", false);
            root.put("isExpand", true);
            root.put("nodeKindId", DataManageNodeKind.LIMB.getId());
            root.put(CommonDomainConstants.ID_FIELD_NAME, CommonDomainConstants.DEFAULT_ROOT_PARENT_ID);
            root.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, "");
            root.put("hasChildren", opdatamanagetypeRepository.countByParentId(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID));
            list.add(root);
            parentId = CommonDomainConstants.DEFAULT_ROOT_PARENT_ID;
        }
        queryRequest.setParentId(parentId);
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryByParentId");
        model.setDefaultOrderBy("sequence asc");
        Map<String, Object> map = this.sqlExecutorDao.executeQuery(model);
        List<Map<String, Object>> datas = (List<Map<String, Object>>) map.get(Constants.ROWS);
        list.addAll(datas);
        return list;
    }

    @Override
    @Transactional
    public void updateOpdatamanagetypeSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(Opdatamanagetype.class, map);
    }

    @Override
    @Transactional
    public void moveOpdatamanagetype(String parentId, List<String> ids) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        this.commonDomainService.moveTree(Opdatamanagetype.class, ids, parentId);
    }

    @Override
    public Long countDatamanagetypekindById(String kindId) {
        return opdatamanagetypekindRepository.countByDataKindId(kindId);
    }

    @Override
    @Transactional
    public void saveDatamanagetypekinds(String id, List<String> kindIds) {
        Assert.hasText(id, "数据权限ID不能为空!");
        Assert.notEmpty(kindIds, "资源维度ID不能为空!");
        List<Opdatamanagetypekind> typeKinds = new ArrayList<>();
        Opdatamanagetype obj = opdatamanagetypeRepository.findOne(id);
        Integer count = 0;
        for (String kindId : kindIds) {
            count = opdatamanagetypekindRepository.countByDataManageIdAndDataKindId(id, kindId);
            if (count == 0) {// 重复校验
                Opdatamanagetypekind kind = new Opdatamanagetypekind();
                kind.setDataKindId(kindId);
                kind.setDataManageId(id);
                typeKinds.add(kind);
            }
        }
        if (typeKinds.size() > 0) {
            opdatamanagetypekindRepository.save(typeKinds);
        }
        obj.setNodeKindId(DataManageNodeKind.LEAF.getId());
        opdatamanagetypeRepository.save(obj);
    }

    @Override
    @Transactional
    public void deleteDatamanagetypekinds(List<String> ids) {
        Assert.notEmpty(ids, "资源维度ID不能为空!");
        for (String id : ids) {
            opdatamanagetypekindRepository.delete(id);
        }
    }

    @Override
    public Map<String, Object> queryOpdatamanagetypekind(ParentIdQueryRequest queryRequest) {
        queryRequest.checkParentId();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagetypekind");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("dataKind", DataResourceKind.getData());
        return this.sqlExecutorDao.executeQuery(model);
    }

    @Override
    public List<Map<String, Object>> queryOpdatamanagetypekindByTypeId(String id) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagetypekind");
        String sql = queryDescriptor.getSqlByName("queryByTypeId");
        return this.sqlExecutorDao.queryToListMap(sql, id);
    }

}
