package com.huigou.data.query.model;

import com.huigou.data.query.XMLParseUtil;
import com.huigou.exception.NotFoundException;
import com.huigou.uasp.bmp.query.QueryDocument.Query;
import com.huigou.uasp.bmp.query.SqlDocument.Sql;

/**
 * 查询模型描述符
 * 
 * @author xx
 */

public class QueryDescriptor {

    private Query query;

    public QueryDescriptor(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getName() {
        return query.getName();
    }

    public String getLabel() {
        return query.getLabel();
    }

    public String getSql() {
        return query.getSqlQuery();
    }

    public String getSqlByName(String sqlName) {
        for (Sql sql : query.getSqlArray()) {
            if (sql.getName().equals(sqlName)) {
                return XMLParseUtil.getNodeTextValue(sql);
            }
        }
        throw new NotFoundException(String.format("实体%s中没有SQL(%s)的配置!", query.getName(), sqlName));
    }
}
