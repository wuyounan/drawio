package com.huigou.uasp.bmp.common.easysearch.domain.model;

import com.huigou.uasp.bmp.easysearch.EasySearchDocument.EasySearch;
import com.huigou.uasp.bmp.easysearch.EasySearchMappingsDocument.EasySearchMappings;
import com.huigou.util.ConfigFileVersion;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 快捷查询配置映射模型
 *
 * @author Gerald
 */
public class EasySearchMappingModel implements Serializable, ConfigFileVersion {

    private static final long serialVersionUID = 173704882069216812L;

    /**
     * 包含查询配置文件
     */
    private final List<Map<String, QuerySchemeModel>> querySchemes;

    private Long versions;

    private List<String> configFilePaths;

    /**
     * 正则表达式去除空格，制表符及换行符
     */
    private static final Pattern pattern = Pattern.compile("\t|\r|\n");

    public EasySearchMappingModel(EasySearchMappings mappings) {
        this(Arrays.asList(mappings));
    }

    /**
     * @since 1.1.3
     */
    public EasySearchMappingModel(List<EasySearchMappings> mappings) {
        querySchemes = mappings.stream()
                .filter(Objects::nonNull)
                .map(m -> Arrays.stream(m.getEasySearchArray()).collect(Collectors.toMap(EasySearch::getName, this::parseQueryScheme)))
                .collect(Collectors.toList());
    }

    /**
     * 解析每个查询配置
     *
     * @param easySearch
     * @return QuerySchemeModel
     * @Title: parseQueryScheme
     * @author
     */
    private QuerySchemeModel parseQueryScheme(EasySearch easySearch) {
        QuerySchemeModel model = new QuerySchemeModel(easySearch.getFieldArray().length);
        model.setName(easySearch.getName());
        model.setDesc(easySearch.getDesc());
        Matcher matcher = pattern.matcher(easySearch.getSql());
        model.setSql(matcher.replaceAll(""));
        model.setFolderIdName(easySearch.getFolderIdName());
        model.setFolderKindId(easySearch.getFolderKindId());
        model.setSqlBeanName(easySearch.getSqlBeanName());
        model.setServiceName(easySearch.getServiceName());
        int length = easySearch.getFieldArray().length;
        for (int i = 0; i < length; i++) {// 解析查询字段
            QuerySchemeField field = QuerySchemeField.newInstance(easySearch.getFieldArray()[i]);
            model.addField(field);
        }
        model.setOrderby(easySearch.getOrderby());
        model.setEasySearch(easySearch);
        return model;
    }

    /**
     * 获取查询配置
     */
    public QuerySchemeModel getQuerySchemeModel(String name) {
        return querySchemes.stream()
                .map(queryScheme -> queryScheme.get(name))
                .filter(Objects::nonNull)
                .findFirst()
                .get();
    }

    @Override
    public Long getVersion() {
        return versions;
    }

    public void setVersions(Long versions) {
        this.versions = versions;
    }

    /**
     * @deprecated 已被 {@link #setConfigFilePaths(List)} 替代。
     */
    @Deprecated
    public void setConfigFilePath(String configFilePath) {
        this.configFilePaths = Collections.singletonList(configFilePath);
    }

    @Override
    public String getFilePath() {
        return configFilePaths.get(0);
    }

    /**
     * @since 1.1.3
     */
    public void setConfigFilePaths(List<String> configFilePaths) {
        this.configFilePaths = configFilePaths;
    }

    @Override
    public List<String> getFilePaths() {
        return configFilePaths;
    }
}
