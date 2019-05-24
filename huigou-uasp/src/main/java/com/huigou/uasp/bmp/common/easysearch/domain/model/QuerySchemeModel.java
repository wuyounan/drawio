package com.huigou.uasp.bmp.common.easysearch.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.huigou.uasp.bmp.easysearch.EasySearchDocument.EasySearch;

/**
 * 快捷查询对象模型
 * 
 * @author Gerald
 */
public class QuerySchemeModel implements Serializable {

    private static final long serialVersionUID = 3946776919057292727L;

    private String sql;

    private List<QuerySchemeField> fields;

    private Map<String, QuerySchemeField> fieldMap;

    private String name;

    private String desc;

    private Long width;

    private String folderIdName;

    private String folderKindId;

    private String sqlBeanName;

    private String serviceName;

    private String orderby;

    private EasySearch easySearch;

    public QuerySchemeModel() {
        name = null;
        sql = null;
        desc = null;
        width = new Long(0);
        fields = new ArrayList<QuerySchemeField>();
        fieldMap = new HashMap<String, QuerySchemeField>();
    }

    public QuerySchemeModel(int length) {
        name = null;
        sql = null;
        desc = null;
        width = new Long(0);
        fields = new ArrayList<QuerySchemeField>(length);
        fieldMap = new HashMap<String, QuerySchemeField>(length);
    }

    /**
     * 返回排序后的字段
     * 
     * @Title: getHeardComparator
     * @author
     * @return Set<QuerySchemeField>
     */
    public Set<QuerySchemeField> getHeardComparator() {
        Set<QuerySchemeField> temp = new TreeSet<QuerySchemeField>(new Comparator<QuerySchemeField>() {
            public int compare(QuerySchemeField o1, QuerySchemeField o2) {
                QuerySchemeField obj1 = (QuerySchemeField) o1;
                QuerySchemeField obj2 = (QuerySchemeField) o2;
                if (obj1.getSequence() > obj2.getSequence()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        temp.addAll(fields);
        return temp;
    }

    public void addField(QuerySchemeField field) {
        this.fields.add(field);
        this.fieldMap.put(field.getCode().toLowerCase(), field);
        if (!field.getType().endsWith("hidden")) {
            width += field.getWidth();
        }
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<QuerySchemeField> getFields() {
        return fields;
    }

    public void setFields(List<QuerySchemeField> fields) {
        this.fields = fields;
    }

    public QuerySchemeField getField(String key) {
        return fieldMap.get(key);
    }

    public Map<String, QuerySchemeField> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, QuerySchemeField> heardMap) {
        this.fieldMap = heardMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public String getFolderIdName() {
        return folderIdName;
    }

    public void setFolderIdName(String folderIdName) {
        this.folderIdName = folderIdName;
    }

    public String getFolderKindId() {
        return folderKindId;
    }

    public void setFolderKindId(String folderKindId) {
        this.folderKindId = folderKindId;
    }

    public String getSqlBeanName() {
        return sqlBeanName;
    }

    public void setSqlBeanName(String sqlBeanName) {
        this.sqlBeanName = sqlBeanName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public EasySearch getEasySearch() {
        return easySearch;
    }

    public void setEasySearch(EasySearch easySearch) {
        this.easySearch = easySearch;
    }

}
