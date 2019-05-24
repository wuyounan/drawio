package com.huigou.uasp.bmp.common.easysearch.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huigou.uasp.bmp.easysearch.EasySearchDocument.EasySearch;
import com.huigou.uasp.bmp.easysearch.EasySearchMappingsDocument.EasySearchMappings;
import com.huigou.util.ConfigFileVersion;

/**
 * 快捷查询配置映射模型
 * 
 * @author Gerald
 */
public class EasySearchMappingModel implements Serializable, ConfigFileVersion {

    private static final long serialVersionUID = 173704882069216812L;

    private Map<String, QuerySchemeModel> querySchemes;// 包含查询配置文件

    private Long versions;

    private String configFilePath;

    /**
     * 正则表达式去除空格，制表符及换行符
     */
    private static final Pattern pattern = Pattern.compile("\t|\r|\n");

    public EasySearchMappingModel(EasySearchMappings mappings) {
        querySchemes = new HashMap<String, QuerySchemeModel>(mappings.getEasySearchArray().length);
        for (EasySearch easySearch : mappings.getEasySearchArray()) {
            querySchemes.put(easySearch.getName(), parseQueryScheme(easySearch));
        }
    }

    /**
     * 解析每个查询配置
     * 
     * @Title: parseQueryScheme
     * @author
     * @param easySearch
     * @return QuerySchemeModel
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
        return querySchemes.get(name);
    }

    @Override
    public Long getVersion() {
        return versions;
    }

    public void setVersions(Long versions) {
        this.versions = versions;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    @Override
    public String getFilePath() {
        return configFilePath;
    }

}
