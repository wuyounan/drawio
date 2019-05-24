package com.huigou.data.query.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.QueryPageRequest;
import com.huigou.util.ClassHelper;
import com.huigou.util.ListUtil;
import com.huigou.util.StringUtil;

/**
 * 查询模型
 * 
 * @author xx
 */

public class QueryModel extends QueryPageRequest {

    /**
     * 查询SQL
     */
    private String sql;

    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;

    /**
     * 管理权限类别
     */
    private String manageType;

    /**
     * 需转换数据字典
     */
    Map<String, Object> dictionaryMap = null;

    /**
     * 到处时是否按照xml格式生成表头
     */
    private String xmlHeads;

    /**
     * 表头XML所在文件路径
     */
    private String xmlFilePath;

    private String defaultOrderBy;

    private boolean isTreeQuery = false;

    private boolean isNeedCount = true;

    private boolean isNeedPermission = true;

    public QueryModel() {
        queryParams = new HashMap<String, Object>(4);
        dictionaryMap = new HashMap<String, Object>(4);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void putParam(String key, Object obj) {
        queryParams.put(key, obj);
    }

    public void putLikeParam(String key, Object obj) {
        queryParams.put(key, "%" + obj + "%");
    }

    public void putStartWithParam(String key, String obj) {
        queryParams.put(key, obj + "%");
    }

    public void putAll(Map<String, Object> map) {
        if (map != null) queryParams.putAll(map);
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public Map<String, Object> getDictionaryMap() {
        return dictionaryMap;
    }

    public void setDictionaryMap(Map<String, Object> dictionaryMap) {
        this.dictionaryMap = dictionaryMap;
    }

    public void putDictionary(String code, Map<String, String> map) {
        dictionaryMap.put(code, map);
    }

    public String getXmlFilePath() {
        return xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public String getXmlHeads() {
        return xmlHeads;
    }

    public void setXmlHeads(String xmlHeads) {
        this.xmlHeads = xmlHeads;
    }

    public String getDefaultOrderBy() {
        return defaultOrderBy;
    }

    public void setDefaultOrderBy(String defaultOrderBy) {
        this.defaultOrderBy = defaultOrderBy;
    }

    public boolean isTreeQuery() {
        return isTreeQuery;
    }

    public void setTreeQuery(boolean isTreeQuery) {
        this.isTreeQuery = isTreeQuery;
    }

    public boolean isNeedCount() {
        return isNeedCount;
    }

    public void setNeedCount(boolean isNeedCount) {
        this.isNeedCount = isNeedCount;
    }

    public boolean isNeedPermission() {
        return isNeedPermission;
    }

    public void setNeedPermission(boolean isNeedPermission) {
        this.isNeedPermission = isNeedPermission;
    }

    public boolean isExportQuery() {
        return !StringUtil.isBlank(this.getExportType());
    }

    public void addCriteria(String criteria) {
        if (StringUtil.isNotBlank(criteria)) {
            this.sql += criteria;
        }
    }

    /**
     * 解析排序字段
     * 
     * @return
     */
    public String parseSortFields() {
        List<SortField> sortFields = this.getSortFieldList();
        if (StringUtil.isNotBlank(this.getDefaultOrderBy())) {
            sortFields.add(new SortField(this.getDefaultOrderBy().trim()));
        }
        if (sortFields.size() == 0) {
            return "";
        }
        // 排除重复字段
        sortFields = ListUtil.distinct(sortFields);
        // 组合SQL
        StringBuffer sb = new StringBuffer();
        sb.append(" order by ");
        int l = sortFields.size();
        for (int i = 0; i < l; i++) {
            sb.append(sortFields.get(i).toString());
            if (i < l - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static QueryModel newInstanceByPageRequest(QueryPageRequest pageRequest) {
        QueryModel querModel = new QueryModel();
        if (pageRequest != null) {
            ClassHelper.copyProperties(pageRequest, querModel);
        }
        return querModel;
    }

}
