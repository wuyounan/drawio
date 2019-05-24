package com.huigou.data.query.parser.impl;

import java.util.Map;
import java.util.regex.Matcher;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.data.query.SQLExecutor;
import com.huigou.data.query.XMLParseUtil;
import com.huigou.data.query.parser.SQLBuilder;
import com.huigou.data.query.parser.model.ConditionModel;
import com.huigou.data.query.parser.model.DataFilterGroup;
import com.huigou.data.query.parser.model.PermissionGroup;
import com.huigou.data.query.parser.model.PermissionModel;
import com.huigou.exception.NotFoundException;
import com.huigou.uasp.bmp.query.ConditionDocument.Condition;
import com.huigou.uasp.bmp.query.PermissionDocument.Permission;
import com.huigou.uasp.bmp.query.PermissionsDocument.Permissions;
import com.huigou.uasp.bmp.query.QueryDocument.Query;
import com.huigou.uasp.bmp.query.SqlDocument.Sql;
import com.huigou.util.StringUtil;

/**
 * 根据查询对象组合SQL
 * 
 * @author xx
 * @date 2017-1-25 下午01:44:18
 * @version V1.0
 */

public class SQLBuilderImpl implements SQLBuilder {

    private QueryPermissionBuilder permissionBuilder;

    public void setPermissionBuilder(QueryPermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    @Override
    public SQLExecutor buildQuerySql(Query query, QueryAbstractRequest queryRequest) {
        StringBuffer querySql = new StringBuffer();
        String sql = query.getSqlQuery();
        Matcher matcher = XMLParseUtil.pattern.matcher(sql);
        querySql.append(matcher.replaceAll(""));
        if (sql.toUpperCase().indexOf("WHERE") < 0) {
            querySql.append(" WHERE 1=1 ");
        }
        return buildSqlCondition(query, querySql.toString(), queryRequest);
    }

    @Override
    public String getSqlByName(Query query, String sqlName) {
        for (Sql sql : query.getSqlArray()) {
            if (sql.getName().equals(sqlName)) {
                return XMLParseUtil.getNodeTextValue(sql);
            }
        }
        throw new NotFoundException(String.format("实体%s中没有SQL(%s)的配置!", query.getName(), sqlName));
    }

    @Override
    public SQLExecutor buildSqlByName(Query query, String sqlName, QueryAbstractRequest queryRequest) {
        String sql = getSqlByName(query, sqlName);
        if (StringUtil.isBlank(sql)) {
            throw new NotFoundException(String.format("实体%s中SQL(%s)的配置为空!", query.getName(), sqlName));
        }
        StringBuffer buildSql = new StringBuffer();
        buildSql.append(sql);
        if (sql.toUpperCase().indexOf("WHERE") < 0) {
            buildSql.append(" WHERE 1=1 ");
        }
        return buildSqlCondition(query, buildSql.toString(), queryRequest);
    }

    /**
     * 组合查询条件
     * 
     * @author
     * @param entity
     * @param querySql
     * @param params
     * @return SQLExecutor
     */
    private SQLExecutor buildSqlCondition(Query query, String querySql, QueryAbstractRequest queryRequest) {
        SQLExecutor executor = new SQLExecutor();
        executor.setPermissionBuilder(permissionBuilder);
        Map<String, Object> params = queryRequest.toParamMap();
        executor.putAll(params);
        executor.addSqlList(querySql);
        executor.setManageType(queryRequest.getSys_manage_type());
        if (params != null && params.size() > 0) {// 存在参数
            // 根据查询条件定义组合查询语句
            for (Condition obj : query.getConditionArray()) {
                ConditionModel condition = ConditionModel.newInstance(obj);
                condition.setFormula(XMLParseUtil.getNodeTextValue(obj));
                executor.buildSqlCondition(condition);
            }
        }
        // 数据查询条件生成 高级自定义查询
        DataFilterGroup group = queryRequest.getDataFilterGroup();
        if (group != null) {
            executor.buildSqlDataFilterGroup(group);
        }
        // 组合权限查询sql
        Permissions permissions = query.getPermissions();
        if (permissions != null) {
            executor.buildSqlPermissions(this.parsePermissions(permissions));
        }
        // 构建查询sql 及条件组合
        executor.buildSql();
        return executor;
    }

    /**
     * 解析权限组
     * 
     * @param permissions
     * @return
     */
    private PermissionGroup parsePermissions(Permissions permissions) {
        PermissionGroup permissionGroup = PermissionGroup.newInstance(permissions);
        Permission[] permissionArray = permissions.getPermissionArray();
        if (permissionArray != null && permissionArray.length > 0) {
            for (Permission xmlObj : permissionArray) {
                PermissionModel permission = PermissionModel.newInstance(xmlObj);
                permission.setFormula(XMLParseUtil.getNodeTextValue(xmlObj));
                permissionGroup.addPermissionModel(permission);
            }
        }
        Permissions[] permissionsArray = permissions.getPermissionsArray();
        if (permissionArray != null && permissionArray.length > 0) {
            for (Permissions xmlObj : permissionsArray) {
                permissionGroup.addPermissionGroup(this.parsePermissions(xmlObj));
            }
        }
        return permissionGroup;
    }

}
