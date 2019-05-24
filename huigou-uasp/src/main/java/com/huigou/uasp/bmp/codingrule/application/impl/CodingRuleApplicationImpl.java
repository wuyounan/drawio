package com.huigou.uasp.bmp.codingrule.application.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.codingrule.application.CodingGenerator;
import com.huigou.uasp.bmp.codingrule.application.CodingRuleApplication;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRule;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail;
import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleQueryRequest;
import com.huigou.uasp.bmp.codingrule.repository.CodingRuleRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;

@Service("codingRuleApplication")
public class CodingRuleApplicationImpl extends BaseApplication implements CodingRuleApplication {
    @Autowired
    private CodingRuleRepository codingRuleRepository;

    @Autowired
    private CodingGenerator codingGenerator;

    private void test() {
        codingGenerator.getNextCode("AbroadCustSupp", null);
        codingGenerator.getNextCode("ArmyCustSupp", null);
        codingGenerator.getNextCode("AssociationCustSupp", null);

        Map<String, Object> params = new HashMap<String, Object>(4);

        // params.put("materialClassificationId", "0EE06256FFCA459C8F56834745619A41");
        params.put("materialClassificationId", "0EE06256FFCA459C8F56834745619A41");
        codingGenerator.getNextCode("Material", params);

        params.put("productCode", "00516A");
        params.put("code1", "1");
        params.put("code2", "01");
        params.put("code3", "0");

       // codingGenerator.getNextCode("MilitaryProduct", params);
    }

    @Override
    @Transactional
    public String saveCodingRule(CodingRule codingRule) {
        if (true){
            test();
        }
        Assert.notNull(codingRule, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "codingRule"));
        codingRule = (CodingRule) this.commonDomainService.loadAndFillinProperties(codingRule);

        if (codingRule.isNew()) {
            codingRule.setId(null);
            codingRule.setStatus(BaseInfoStatus.ENABLED.getId());
        }
        codingRule.buildDetails();

        codingRule = (CodingRule) this.commonDomainService.saveBaseInfoWithFolderEntity(codingRule, codingRuleRepository);
        return codingRule.getId();
    }

    @Override
    public CodingRule loadCodingRule(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return codingRuleRepository.findOne(id);
    }

    @Override
    @Transactional
    public void moveCodingRules(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);

        this.commonDomainService.moveForFolder(CodingRule.class, ids, folderId);
    }

    @Override
    @Transactional
    public void updateCodingRulesStatus(List<String> ids, Integer status) {
        this.checkIdsNotEmpty(ids);
        Assert.notNull(status, "参数status不能为空。");
        this.commonDomainService.updateStatus(CodingRule.class, ids, status);
    }

    @Override
    @Transactional
    public void deleteCodingRules(Collection<String> ids) {
        Assert.notEmpty(ids, CommonDomainConstants.IDS_NOT_BLANK);
        List<CodingRule> codingRules = this.codingRuleRepository.findAll(ids);
        codingRuleRepository.delete(codingRules);
    }

    @Override
    public Map<String, Object> slicedQueryCodingRules(CodingRuleQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "codingRule");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void deleteCodingRuleDetails(String codingRuleId, List<String> ids) {
        Assert.hasText(codingRuleId, "参数codingRuleId不能为空。");
        this.checkIdsNotEmpty(ids);

        CodingRule codingRule = this.codingRuleRepository.findOne(codingRuleId);

        Assert.state(codingRule != null, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, codingRuleId, "编码规则"));

        codingRule.removeDetails(ids);

        this.codingRuleRepository.save(codingRule);
    }

    @Override
    public Map<String, Object> slicedQueryCodingRuleDetails(ParentIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "codingRuleDetail");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);

        model.putDictionary("attributeKind", CodingRuleDetail.AttributeKind.getData());
        model.putDictionary("attributeUseKind", CodingRuleDetail.AttributeUseKind.getData());
        model.putDictionary("fillSignDirection", CodingRuleDetail.DirectionKind.getData());
        model.putDictionary("interceptDirection", CodingRuleDetail.DirectionKind.getData());
        model.putDictionary("format", CodingRuleDetail.Format.getData());

        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

}
