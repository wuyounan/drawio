package com.huigou.uasp.bmp.configuration.application.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.CodeRuleIsTryAgain;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.configuration.application.CodeBuildRuleApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CodeBuildRule;
import com.huigou.uasp.bmp.configuration.repository.CodeBuildRuleRepository;
import com.huigou.util.DateUtil;

/**
 *  编码规则管理
 * 
 * @author gongmm
 */
@Service("codeBuildRuleApplication")
public class CodeBuildRuleApplicationImpl extends BaseApplication implements CodeBuildRuleApplication {

    @Autowired
    private CodeBuildRuleRepository repository;

    @Override
    @Transactional
    public String saveCodeBuildRule(CodeBuildRule codeBuildRule) {
        Assert.notNull(codeBuildRule, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "codeBuildRule"));
        codeBuildRule = (CodeBuildRule) this.commonDomainService.loadAndFillinProperties(codeBuildRule);
        codeBuildRule = (CodeBuildRule) this.commonDomainService.saveBaseInfoWithFolderEntity(codeBuildRule, repository);
        return codeBuildRule.getId();
    }

    @Override
    public CodeBuildRule loadCodeBuildRule(String id) {
        this.checkIdNotBlank(id);
        return this.repository.findOne(id);
    }

    /**
     * @Transactional(propagation=Propagation.REQUIRES_NEW) ：不管是否存在事务,都创建一个新的事务,原来的挂起,新的执行完毕,继续执行老的事务
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @CodeRuleIsTryAgain
    public CodeBuildRule getRuleValueAsStep(String code, Integer step) {
        Assert.hasText(code, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
        String id = this.sqlExecutorDao.queryToString("select t.id from sa_codebuildrule t where t.code = ?", code);
        Assert.hasText(id, MessageSourceContext.getMessage("object.not.found.by.code", code, "规则"));
        String sql = "select t.* from sa_codebuildrule t where t.id = ?  for update";
        CodeBuildRule codeBuildRule = this.sqlExecutorDao.queryToObject(sql, CodeBuildRule.class, id);
        // 步长最小为1
        if (step == null || step < 1) {
            step = 1;
        }
        Integer currentValue = codeBuildRule.getCurrentValue();
        if (currentValue == null) {
            currentValue = 0;
        }
        Pattern p = Pattern.compile(RULE_REG, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(codeBuildRule.getRule());
        if (m.find()) {
            String dateFormat = m.group(1);
            try {
                // 判断是否已到循环周期
                if (isPeriodEnd(dateFormat, codeBuildRule.getLastModifiedDate())) {
                    currentValue = 0;
                }
            } catch (ParseException e) {
                throw new ApplicationException(String.format("解析单据编号规则“%s出错”。", dateFormat));
            }
        } else {
            throw new ApplicationException(String.format("解析单据编号规则“%s出错”。", codeBuildRule.getRule()));
        }
        codeBuildRule.setCurrentValue(currentValue + step);// 流水号+步长
        codeBuildRule.setLastModifiedDate(DateUtil.getDateTime());
        // 使用version校验乐观锁
        Long version = this.sqlExecutorDao.queryToLong("select t.version from sa_codebuildrule t where t.id = ?", id);
        Assert.isTrue(version.equals(codeBuildRule.getVersion()), String.format("单据取号重复，请稍后重试", codeBuildRule.getRule()));
        codeBuildRule = repository.save(codeBuildRule);
        codeBuildRule.setLastValue(currentValue);
        return codeBuildRule;
    }

    /**
     * 判断是否已到循环周期
     * 
     * @param dateFormat
     *            日期格式
     * @param lastupdate
     *            最后更新日期
     * @return
     * @throws ParseException
     */
    private boolean isPeriodEnd(String dateFormat, Date lastupdate) throws ParseException {
        lastupdate = lastupdate != null ? lastupdate : DateUtil.getDateTime();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        Date now = DateUtil.getDateTime();
        Date formattedCurrentDate = df.parse(df.format(now));
        Date formattedLastUpdateDate = df.parse(df.format(lastupdate));
        return formattedCurrentDate.compareTo(formattedLastUpdateDate) > 0;
    }

    @Override
    @Transactional
    public void deleteCodeBuildRules(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        List<CodeBuildRule> codeBuildRules = this.repository.findAll(ids);
        Assert.isTrue(ids.size() == codeBuildRules.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "单据编码规则"));
        this.repository.delete(codeBuildRules);
    }

    @Override
    @Transactional
    public void moveCodeBuildRules(List<String> ids, String folderId) {
        this.checkIdsNotEmpty(ids);
        this.checkFolderIdNotBlank(folderId);
        this.commonDomainService.moveForFolder(CodeBuildRule.class, ids, folderId);
    }

    @Override
    public Map<String, Object> slicedQueryCodeBuildRules(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor QueryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "codeBuildRule");
        return this.sqlExecutorDao.executeSlicedQuery(QueryDescriptor, queryRequest);
    }

    @Override
    public List<CodeBuildRule> queryAll() {
        return this.repository.findAll();
    }

}
