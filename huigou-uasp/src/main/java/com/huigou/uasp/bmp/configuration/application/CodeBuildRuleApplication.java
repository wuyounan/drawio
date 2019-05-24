package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.configuration.domain.model.CodeBuildRule;

/**
 * 系统参数服务
 * 
 * @author gongmm
 */
public interface CodeBuildRuleApplication {
    public final String RULE_REG = "\\{(.[^\\{\\}]*)\\}(.*)\\{([0-9])\\}";

    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";

    /**
     * 保存单据编码规则
     * 
     * @param codeBuildRule
     *            单据编码规则
     * @return
     *         单据编码规则ID
     */
    String saveCodeBuildRule(CodeBuildRule codeBuildRule);

    /**
     * 加载单据编码规则
     * 
     * @param id
     *            单据编码规则ID
     * @return 单据编码规则
     */
    CodeBuildRule loadCodeBuildRule(String id);

    /**
     * 根据规则取值
     * 
     * @param code
     * @param step
     */
    CodeBuildRule getRuleValueAsStep(String code, Integer step);

    /**
     * 删除单据编码规则
     * 
     * @param ids
     *            单据编码规则ID列表
     */
    void deleteCodeBuildRules(List<String> ids);

    /**
     * 移动单据编码规则
     * 
     * @param ids
     *            单据编码规则ID数组
     * @param folderId
     *            文件夹ID
     */
    void moveCodeBuildRules(List<String> ids, String folderId);

    /**
     * 分页查询单据编码规则
     * 
     * @param folderId
     *            文件夹ID
     * @param code
     *            编码
     * @param name
     *            名称
     * @param queryModel
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryCodeBuildRules(FolderAndCodeAndNameQueryRequest queryRequest);

    /**
     * 查询全部单据编码规则
     * 
     * @return
     */
    List<CodeBuildRule> queryAll();
}
