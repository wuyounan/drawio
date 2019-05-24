package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictionaryDesc;
import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.CommonTreeApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;
import com.huigou.uasp.bmp.configuration.domain.model.CommonTree.NodeKind;
import com.huigou.uasp.bmp.configuration.domain.query.CommonTreeQueryRequest;
import com.huigou.uasp.bmp.configuration.repository.CommonTreeRepository;
import com.huigou.util.StringPool;

@Service("commonTreeApplication")
public class CommonTreeApplicationImpl extends BaseApplication implements CommonTreeApplication {

    private final static String DICT_COMMON_TREE_BIZ_ENTITY_MAPPING = "commonTreeKind";

    @Autowired
    private CommonTreeRepository commonTreeRepository;

    @Override
    @Transactional
    public String insert(CommonTree commonTree) {
        Assert.notNull(commonTree, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "commonTree"));

        if (commonTree.getNodeKind() == null) {
            commonTree.setNodeKind(NodeKind.FOLDER);
            commonTree.setCode(commonTree.getName());
        }

        commonTree = (CommonTree) this.commonDomainService.saveTreeEntity(commonTree, commonTreeRepository);
        return commonTree.getId();
    }

    @Override
    @Transactional
    public String update(CommonTree commonTree) {
        CommonTree dbcommonTree = this.load(commonTree.getId());
        Assert.notNull(dbcommonTree, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, commonTree.getId(), commonTree.getClass().getName()));

        String oldName = dbcommonTree.getName();
        dbcommonTree.fromEntity(commonTree);
        
        dbcommonTree = (CommonTree) this.commonDomainService.saveTreeEntity(dbcommonTree, commonTreeRepository, oldName);
        return dbcommonTree.getId();
    }

    @Override
    @Transactional
    public void delete(List<String> ids) {
        this.checkIdsNotEmpty(ids);

        Long childrenCount, bizCount;
        String countByParentIdSql = this.commonDomainService.getCountByParentIdSql();

        List<CommonTree> commonTrees = this.commonTreeRepository.findAll(ids);
        for (CommonTree item : commonTrees) {
            Map<String, DictionaryDesc> map = SystemCache.getDictionary(DICT_COMMON_TREE_BIZ_ENTITY_MAPPING);
            Assert.notNull(map, String.format("未找到“%s”对应的数据字典设置。", DICT_COMMON_TREE_BIZ_ENTITY_MAPPING));
            DictionaryDesc dd = map.get(String.valueOf(item.getKindId()));
            Assert.notNull(dd, String.format("未找到通用树“%s”对应的业务对象映射关系。", item.getKindId()));
            String[] tabInfo = dd.getName().split(StringPool.COMMA);
            Assert.isTrue(tabInfo.length == 2, "业务对象映射格式错误。");

            childrenCount = this.commonTreeRepository.countByParentId(item.getId());
            Assert.isTrue(childrenCount.equals(0L), MessageSourceContext.getMessage(MessageConstants.CAN_NOT_DELETE_HAS_CHILDREN, item.getName()));

            bizCount = (Long) this.generalRepository.single(String.format(countByParentIdSql, tabInfo[0], tabInfo[1]),
                                                            QueryParameter.buildParameters("parentId", item.getId()));
            Assert.isTrue(bizCount.equals(0L), String.format("“%s”下已存在业务数据，不能删除。 ", item.getName()));
        }

        this.commonTreeRepository.delete(commonTrees);
    }

    @Override
    public CommonTree load(String id) {
        this.checkIdNotBlank(id);
        return commonTreeRepository.findOne(id);
    }

    @Override
    @Transactional
    public void updateSequence(Map<String, Integer> params) {
        Assert.notEmpty(params, "参数params不能为空。");
        this.commonDomainService.updateSequence(CommonTree.class, params);
    }

    @Override
    public CommonTree findByKindIdAndCode(Integer kindId, String code) {
        Assert.notNull(kindId, "参数kindId不能为空。");
        Assert.hasText(code, "参数code不能为空。");
        return commonTreeRepository.findByKindIdAndCode(kindId, code);
    }

    @Override
    public Map<String, Object> query(CommonTreeQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "commonTree");
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }
}
