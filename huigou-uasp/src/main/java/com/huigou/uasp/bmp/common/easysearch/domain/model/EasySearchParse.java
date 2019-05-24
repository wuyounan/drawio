package com.huigou.uasp.bmp.common.easysearch.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huigou.cache.DictUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 快捷查询数据传输对象
 * 
 * @author Gerald
 */
public class EasySearchParse extends QuerySchemeModel {

    private static final long serialVersionUID = -4425541686939965398L;

    private final static String CASCADE_FIELD = "cascadeField";

    private final static String COMMON_FIELD = "commonField";

    private final static String COMMON_PARAM = "cascadeParam";

    private final static String COMMON_PARAM_VALUE = "commonParamValue";

    private int count;

    private Integer intPage;

    private int sumPage;

    private Integer pageSize;

    private String paramValue;

    private String folderId;

    private Map<String, Object> queryParams = new HashMap<String, Object>(4);

    private List<Map<String, Object>> data;

    private Map<String, Object> dictionaryMap;

    private SDO sdo;

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public EasySearchParse() {
        count = 0;
        intPage = 0;
        sumPage = 0;
        pageSize = 0;
        paramValue = null;
        dictionaryMap = new HashMap<String, Object>(4);
    }

    @Override
    public void setFields(List<QuerySchemeField> fields) {
        super.setFields(fields);
        Set<QuerySchemeField> s = super.getHeardComparator();
        fields = new ArrayList<QuerySchemeField>(s.size());
        fields.addAll(s);
        for (QuerySchemeField f : this.getFields()) {
            if (f.getType().equals("hidden") || f.getType().equals("dictionary")) {
                f.setAutoCondition("false");
            }
            if (StringUtil.isNotBlank(f.getDictionary())) {
                String code = f.getCode().replaceAll("TextView", "");
                f.setAutoCondition("false");
                this.putDictionary(code, DictUtil.getDictionary(f.getDictionary()));
            }
        }
    }

    public int getHeadLength() {
        int headlength = 0;
        for (int i = 0; i < this.getFields().size(); i++) {
            QuerySchemeField o = this.getFields().get(i);
            if (!o.getType().equals("hidden")) headlength++;
        }
        return headlength;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIntPage() {
        return intPage;
    }

    public void setIntPage(Integer intPage) {
        this.intPage = intPage != null ? intPage : 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize != null ? pageSize : 15;
    }

    public int getSumPage() {
        return sumPage;
    }

    public void setSumPage(int sumPage) {
        this.sumPage = sumPage;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
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

    public void putAllParam(Map<String, Object> param) {
        queryParams.putAll(param);
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

    /**
     * 根据查询条件构建查询SQL
     * 
     * @Title: reBuildSql
     * @author
     * @return String
     */
    public String buildSql() {
        StringBuffer wheresql = new StringBuffer("1=1");
        StringBuffer tmpSql = new StringBuffer();
        String sql = this.getSql();
        tmpSql.append("select rebuild.* from (").append(sql).append(") rebuild where (");
        if (!StringUtil.isBlank(paramValue)) {
            if (sql.indexOf(CASCADE_FIELD) != -1) {// 多级查询cascadeField,是在配置文件中定义拼接为 and 并且关系
                String[] flag = paramValue.toUpperCase().split(" ");
                for (int i = 0; i < flag.length; i++) {
                    if (flag[i] != null && !flag[i].equals("")) {
                        wheresql.append(" and  upper(rebuild.").append(CASCADE_FIELD).append(") like :").append(CASCADE_FIELD + i);
                        putLikeParam(CASCADE_FIELD + i, flag[i]);
                    }
                }
            } else if (sql.indexOf(COMMON_FIELD) != -1) {// 批量查询commonField,是在配置文件中定义 拼接为or 或则关系
                String[] flag = paramValue.toUpperCase().split(" ");
                int length = flag.length;
                if (length > 0) {
                    wheresql.append(" and (");
                    for (int i = 0; i < length; i++) {
                        if (flag[i] != null && !flag[i].equals("")) {
                            wheresql.append(" upper(rebuild.").append(COMMON_FIELD).append(") like :").append(COMMON_PARAM + i);
                            wheresql.append("or");
                            putLikeParam(COMMON_PARAM + i, flag[i]);
                        }
                    }
                    if (wheresql.lastIndexOf("or") == (wheresql.length() - 2)) {
                        wheresql.replace(wheresql.length() - 2, wheresql.length(), "");
                    }
                    wheresql.append(")");
                }
            } else {
                for (QuerySchemeField field : this.getFields()) {// 将显示字段都做LIKE查询
                    if (field.isCondition()) {
                        if (wheresql.length() == 3) {
                            wheresql.delete(0, wheresql.length());
                        }
                        wheresql.append(" upper(rebuild.").append(StringUtil.getUnderscoreName(field.getCode())).append(") like :").append(COMMON_PARAM_VALUE)
                                .append(" or");
                    }
                }
                putLikeParam(COMMON_PARAM_VALUE, paramValue.toUpperCase());
                if (wheresql.lastIndexOf("or") == (wheresql.length() - 2)) {
                    wheresql.replace(wheresql.length() - 2, wheresql.length(), "");
                }
            }
        }
        tmpSql.append(wheresql).append(")");
        if (!StringUtil.isBlank(this.folderId)) {
            if (!StringUtil.isBlank(this.getFolderKindId())) { // 存在文件夹配置
                if (this.getFolderKindId().equals("org")) {
                    tmpSql.append(" and  rebuild.full_id like :sysfolderFullId");
                    putParam("sysfolderFullId", folderId + "%");
                } else if (!StringUtil.isBlank(this.getFolderIdName())) {
                    tmpSql.append(" and  rebuild.").append(this.getFolderIdName()).append(" = :").append(this.getFolderIdName());
                    putParam(this.getFolderIdName(), folderId);
                }
            }
        }
        return tmpSql.toString();
    }

    public String appendOrderbySql(String sql) {
        StringBuffer tmpSql = new StringBuffer();
        tmpSql.append(sql);
        // 定义的排序
        if (!StringUtil.isBlank(this.getOrderby())) {
            tmpSql.append(" order by ").append(this.getOrderby());
        }
        return tmpSql.toString();
    }

    public void setComputeCount(int count) {
        int s = ((count + ((pageSize != 0) ? pageSize : 1)) - 1) / ((pageSize != 0) ? pageSize : 1);
        if (intPage > s) {
            intPage = s;
        }
        if (intPage < 1) {
            intPage = 1;
        }
        this.sumPage = s;
        this.count = count;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public SDO getSdo() {
        return sdo;
    }

    public void setSdo(SDO sdo) {
        this.sdo = sdo;
    }

}
