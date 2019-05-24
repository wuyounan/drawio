package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.OrgfunApplication;
import com.huigou.uasp.bmp.configuration.domain.model.Orgfun;
import com.huigou.uasp.bmp.configuration.domain.query.OrgfunQueryRequest;
import com.huigou.uasp.bmp.configuration.repository.OrgfunRepository;

/**
 * 组织机构函数记录维护
 * 
 * @ClassName: OrgfunApplicationImpl
 * @author xx
 * @date 2018-03-09 10:47
 * @version V1.0
 */
@Service("orgfunApplication")
public class OrgfunApplicationImpl extends BaseApplication implements OrgfunApplication {
    @Autowired
    private OrgfunRepository orgfunRepository;

    @Override
    @Transactional
    public String saveOrgfun(Orgfun orgfun) {
        Assert.notNull(orgfun, CommonDomainConstants.OBJECT_NOT_NULL);
        orgfun = (Orgfun) this.commonDomainService.loadAndFillinProperties(orgfun);
        if (orgfun.isNew()) {
            orgfun.setStatus(BaseInfoStatus.ENABLED.getId());
            orgfun.setSequence(this.commonDomainService.getNextSequence(Orgfun.class));
        }
        orgfun = (Orgfun) this.commonDomainService.saveBaseInfoEntity(orgfun, orgfunRepository);
        return orgfun.getId();
    }

    @Override
    public Orgfun loadOrgfun(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return orgfunRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteOrgfun(List<String> ids) {
        Assert.notEmpty(ids);
        Long count = 0l;
        for (String id : ids) {
            Orgfun orgfun = orgfunRepository.findOne(id);
            if (orgfun != null) {
                count = orgfunRepository.countByParentId(orgfun.getId());
                Assert.isTrue(count.compareTo(0l) == 0, String.format("%s存在子节点不能删除!", orgfun.getName()));
                orgfunRepository.delete(orgfun);
            }
        }

    }

    @Override
    public Map<String, Object> slicedQueryOrgfun(OrgfunQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "saOrgFun");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("isLast", DictUtil.getDictionary("yesorno"));
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    public List<Map<String, Object>> queryOrgfunByParentId(String parentId) {
        Assert.hasText(parentId, "parentId不能为空!");
        if (parentId.equals("root")) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("code", "root");
            root.put("name", "全部");
            root.put("isLast", "0");
            root.put("fullId", "/");
            root.put(CommonDomainConstants.ID_FIELD_NAME, CommonDomainConstants.DEFAULT_ROOT_PARENT_ID);
            root.put(CommonDomainConstants.PARENT_ID_FIELD_NAME, "");
            root.put("hasChildren", orgfunRepository.countByParentId(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID));
            list.add(root);
            return list;
        }
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "saOrgFun");
        String sql = queryDescriptor.getSqlByName("treeQuery");
        return this.sqlExecutorDao.queryToListMap(sql, parentId);
    }

    @Override
    @Transactional
    public void updateOrgfunSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(Orgfun.class, map);
    }

    @Override
    @Transactional
    public void updateOrgfunStatus(List<String> ids, Integer status) {
        this.checkIdsNotEmpty(ids);
        Assert.notNull(status, MessageSourceContext.getMessage("status.not.blank"));
        this.commonDomainService.updateStatus(Orgfun.class, ids, status);
    }

    @Override
    @Transactional
    public void moveOrgfun(List<String> ids, String parentId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(parentId);
        this.commonDomainService.move(Orgfun.class, ids, "parent_Id", parentId);
    }

}
