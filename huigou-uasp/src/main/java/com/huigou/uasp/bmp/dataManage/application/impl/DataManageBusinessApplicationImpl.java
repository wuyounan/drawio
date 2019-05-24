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
import com.huigou.data.datamanagement.DataFieldSymbolKind;
import com.huigou.data.datamanagement.DataManageNodeKind;
import com.huigou.data.datamanagement.DataResourceKind;
import com.huigou.data.datamanagement.DataTypeKind;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.PermissionKind;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManageBusinessApplication;
import com.huigou.uasp.bmp.dataManage.application.DataManageTypeApplication;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagebusiness;
import com.huigou.uasp.bmp.dataManage.domain.model.OpdatamanagebusinessField;
import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetype;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagebusinessFieldQueryRequest;
import com.huigou.uasp.bmp.dataManage.domain.query.OpdatamanagebusinessQueryRequest;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagebusinessFieldRepository;
import com.huigou.uasp.bmp.dataManage.repository.OpdatamanagebusinessRepository;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 数据管理权限业务类型
 * 
 * @ClassName: DataManageBusinessApplicationImpl
 * @author xx
 * @date 2018-09-27 12:04
 * @version V1.0
 */
@Service("dataManageBusinessApplication")
public class DataManageBusinessApplicationImpl extends BaseApplication implements DataManageBusinessApplication {
    @Autowired
    private OpdatamanagebusinessRepository opdatamanagebusinessRepository;

    @Autowired
    private OpdatamanagebusinessFieldRepository opdatamanagebusinessFieldRepository;

    @Autowired
    private DataManageTypeApplication dataManageTypeApplication;

    @Override
    @Transactional
    public String insertOpdatamanagebusiness(Opdatamanagebusiness opdatamanagebusiness) {
        Assert.notNull(opdatamanagebusiness, CommonDomainConstants.OBJECT_NOT_NULL);
        opdatamanagebusiness.setStatus(BaseInfoStatus.ENABLED.getId());
        opdatamanagebusiness.setSequence(this.commonDomainService.getNextSequence(Opdatamanagebusiness.class, "parentId", opdatamanagebusiness.getParentId()));
        opdatamanagebusiness = (Opdatamanagebusiness) this.commonDomainService.saveTreeEntity(opdatamanagebusiness, opdatamanagebusinessRepository,
                                                                                              opdatamanagebusiness.getName(), true);
        return opdatamanagebusiness.getId();
    }

    @Override
    @Transactional
    public String updateOpdatamanagebusiness(Opdatamanagebusiness newOpdatamanagebusiness) {
        Assert.notNull(newOpdatamanagebusiness, CommonDomainConstants.OBJECT_NOT_NULL);
        Opdatamanagebusiness opdatamanagebusiness = this.opdatamanagebusinessRepository.findOne(newOpdatamanagebusiness.getId());
        Assert.notNull(opdatamanagebusiness, CommonDomainConstants.LOAD_OBJECT_IS_NULL);
        String oldName = opdatamanagebusiness.getName();
        opdatamanagebusiness.fromEntity(newOpdatamanagebusiness);
        this.commonDomainService.saveTreeEntity(newOpdatamanagebusiness, opdatamanagebusinessRepository, oldName, true);
        return opdatamanagebusiness.getId();
    }

    @Override
    public Opdatamanagebusiness loadOpdatamanagebusiness(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        Opdatamanagebusiness opdatamanagebusiness = opdatamanagebusinessRepository.findOne(id);
        String dataManageId = opdatamanagebusiness.getDataManageId();
        if (StringUtil.isNotBlank(dataManageId)) {
            Opdatamanagetype manageType = dataManageTypeApplication.loadOpdatamanagetype(dataManageId);
            opdatamanagebusiness.setDataManageName(manageType.getName());
        }
        opdatamanagebusiness.setHasChildren(opdatamanagebusinessRepository.countByParentId(id));
        return opdatamanagebusiness;
    }

    @Override
    @Transactional
    public void deleteOpdatamanagebusiness(List<String> ids) {
        Assert.notEmpty(ids, CommonDomainConstants.ID_NOT_BLANK);
        Opdatamanagebusiness obj = null;
        Integer count = 0;
        for (String id : ids) {
            obj = opdatamanagebusinessRepository.findOne(id);
            // 校验是否存在子级
            count = opdatamanagebusinessRepository.countByParentId(id);
            Assert.isTrue(count == 0, String.format("%s-%s存在子级无法删除!", obj.getCode(), obj.getName()));
            opdatamanagebusinessFieldRepository.deleteByDatamanagebusinessId(id);
            opdatamanagebusinessRepository.delete(obj);
        }
    }

    @Override
    public Map<String, Object> slicedQueryOpdatamanagebusiness(OpdatamanagebusinessQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagebusiness");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("nodeKindId", DataManageNodeKind.getData());
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryDatamanagebusiness(OpdatamanagebusinessQueryRequest queryRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        String parentId = queryRequest.getParentId();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagebusiness");
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
            root.put("hasChildren", opdatamanagebusinessRepository.countByParentId(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID));
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
    public void updateOpdatamanagebusinessSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(Opdatamanagebusiness.class, map);
    }

    @Override
    @Transactional
    public void moveOpdatamanagebusiness(String parentId, List<String> ids) {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        this.commonDomainService.moveTree(Opdatamanagebusiness.class, ids, parentId);
    }

    @Override
    @Transactional
    public void saveOpdatamanagebusinessField(OpdatamanagebusinessField field) {
        Assert.notNull(field, "字段定义不能为空");
        field.checkConstraints();
        field = (OpdatamanagebusinessField) this.commonDomainService.loadAndFillinProperties(field);
        List<OpdatamanagebusinessField> others = opdatamanagebusinessFieldRepository.findByDatamanagebusinessIdAndTableColumnAndTableAliasAndIsOrgCondition(field.getDatamanagebusinessId(),
                                                                                                                                                            field.getTableColumn(),
                                                                                                                                                            field.getTableAlias(),
                                                                                                                                                            field.getIsOrgCondition());
        if (others != null && others.size() > 0) {
            for (OpdatamanagebusinessField other : others) {
                if (field.isNew()) {
                    Assert.isTrue(false, String.format("字段'%s.%s'已存在!", field.getTableAlias(), field.getTableColumn()));
                } else {
                    Assert.isTrue(other.getId().equals(field.getId()), String.format("字段'%s.%s'已存在!", field.getTableAlias(), field.getTableColumn()));
                }
            }
        }
        opdatamanagebusinessFieldRepository.save(field);
    }

    @Override
    public OpdatamanagebusinessField loadOpdatamanagebusinessField(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        OpdatamanagebusinessField obj = opdatamanagebusinessFieldRepository.findOne(id);
        return obj;
    }

    @Override
    public Map<String, Object> queryOpdatamanagebusinessField(OpdatamanagebusinessFieldQueryRequest queryRequest) {
        queryRequest.checkParentId();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "opdatamanagebusinessField");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("dataKindId", DataResourceKind.getData());
        model.putDictionary("columnDataType", DataTypeKind.getData());
        model.putDictionary("columnSymbol", DataFieldSymbolKind.getData());
        model.putDictionary("dataKind", PermissionKind.getChooseData());
        return this.sqlExecutorDao.executeQuery(model);
    }

    @Override
    @Transactional
    public void deleteOpdatamanagebusinessField(List<String> ids) {
        Assert.notEmpty(ids, "字段定义ID不能为空!");
        for (String id : ids) {
            opdatamanagebusinessFieldRepository.delete(id);
        }
    }

}
