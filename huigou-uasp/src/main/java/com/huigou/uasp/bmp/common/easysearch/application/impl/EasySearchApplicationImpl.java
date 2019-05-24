package com.huigou.uasp.bmp.common.easysearch.application.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.huigou.cache.SystemCache;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.dialect.Dialect;
import com.huigou.data.dialect.DialectUtils;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.data.query.SQLExecutor;
import com.huigou.data.query.XMLParseUtil;
import com.huigou.data.query.model.QueryModel;
import com.huigou.data.query.parser.model.ConditionModel;
import com.huigou.data.query.parser.model.PermissionGroup;
import com.huigou.data.query.parser.model.PermissionModel;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.easysearch.EasySearchParseInterface;
import com.huigou.uasp.bmp.common.easysearch.application.EasySearchApplication;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchParse;
import com.huigou.uasp.bmp.common.easysearch.domain.model.QuerySchemeField;
import com.huigou.uasp.bmp.easysearch.ConditionDocument.Condition;
import com.huigou.uasp.bmp.easysearch.EasySearchDocument.EasySearch;
import com.huigou.uasp.bmp.easysearch.PermissionDocument.Permission;
import com.huigou.uasp.bmp.easysearch.PermissionsDocument.Permissions;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.DateUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 快捷查询应用
 * 
 * @author xx
 */

public class EasySearchApplicationImpl implements EasySearchApplication {

    private SQLQuery sqlQuery;

    private QueryPermissionBuilder permissionBuilder;

    public void setSqlQuery(SQLQuery sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public void setPermissionBuilder(QueryPermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    /**
     * 解析查询条件
     * 
     * @param query
     * @param querySql
     * @param sdo
     * @return
     */
    private SQLExecutor buildSqlCondition(EasySearch query, String querySql, SDO sdo) {
        SQLExecutor executor = new SQLExecutor();
        executor.setPermissionBuilder(permissionBuilder);
        Map<String, Object> params = sdo.getProperties();
        executor.putAll(params);
        executor.addSqlList(querySql);
        String manageType = sdo.getProperty(Constants.MANAGE_TYPE, String.class);
        executor.setManageType(manageType);
        for (Condition obj : query.getConditionArray()) {// 根据查询条件定义组合查询语句
            ConditionModel condition = ConditionModel.newInstance(obj);
            condition.setFormula(XMLParseUtil.getNodeTextValue(obj));
            executor.buildSqlCondition(condition);
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

    public void search(final EasySearchParse dto, final SDO sdo) throws Exception {
        SQLExecutor executor = this.buildSqlCondition(dto.getEasySearch(), dto.getSql(), sdo);
        dto.putAllParam(executor.parseParamMap());
        dto.setSql(executor.getSql());
        // 调用外部接口
        this.easySearchParse(dto);
        // 重新构建查询条件
        String sql = dto.buildSql();
        Map<String, Object> param = dto.getQueryParams();
        param.put("currentPersonId", sdo.getOperator().getUserId());
        param.put("currentOrgId", sdo.getOperator().getOrgId());
        param.put("currentPositionId", sdo.getOperator().getPositionId());
        param.put("currentPersonMemberId", sdo.getOperator().getPersonMemberId());
        param.put("currentDeptId", sdo.getOperator().getDeptId());
        param.put("currentFullId", sdo.getOperator().getFullId());
        param.put("currentTenantId", sdo.getOperator().getTenantId());
        // 查询数据总数
        Integer total = sqlQuery.getJDBCDao().queryToObjectByMapParam(Dialect.getTotalSql(sql), Integer.class, param);
        sql = dto.appendOrderbySql(sql);
        Dialect d = DialectUtils.guessDialect(sqlQuery.getJDBCDao().getDataSource());
        String pageSql = d.paginate(dto.getIntPage(), dto.getPageSize(), sql);
        ThreadLocalUtil.putVariable(Constants.DICTIONARY_MAP, dto.getDictionaryMap());
        List<Map<String, Object>> list = (List<Map<String, Object>>) sqlQuery.getJDBCDao().queryToMapListByMapParam(pageSql, param);
        dto.setComputeCount(total);
        dto.setData(reBuilderData(dto, list));
    }

    /**
     * 判断快捷查询执行前是否需要调用外部接口
     * 
     * @param dto
     */
    private void easySearchParse(EasySearchParse dto) {
        String beanName = dto.getSqlBeanName();
        if (StringUtil.isBlank(beanName)) {
            return;
        }
        try {
            EasySearchParseInterface easySearchParseInterface = ApplicationContextWrapper.getBean(beanName, EasySearchParseInterface.class);
            easySearchParseInterface.easySearchParse(dto);
        } catch (Exception e) {
            throw new ApplicationException("调用接口错误“" + beanName + "”:" + e.getMessage());
        }
    }

    /**
     * 格式华显示数据
     * 
     * @param l
     * @return
     */
    private static List<Map<String, Object>> reBuilderData(EasySearchParse dto, List<Map<String, Object>> l) {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        String type = null, mask = null;
        for (Map<String, Object> map : l) {
            Map<String, Object> data = new HashMap<String, Object>();
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next();
                QuerySchemeField obj = dto.getField(key.toLowerCase());
                if (obj != null) {
                    type = obj.getType();
                    mask = StringUtil.isNotBlank(obj.getMask()) ? obj.getMask() : "";
                    String value = map.get(key) != null ? map.get(key).toString() : "";
                    if (!StringUtil.isBlank(value)) {
                        if (!StringUtil.isBlank(obj.getDictionary())) {
                            String textView = SystemCache.getDictionaryDetailText(obj.getDictionary(), value);
                            if (textView != null) {
                                data.put(obj.getCode() + "TextView", textView);
                            }
                        }
                        if (type.equals("number")) {
                            value = StringUtil.keepDigit(value, ClassHelper.convert(obj.getMask(), Integer.class), true);
                        }
                        // 这里改变了原有的数据，在后期处理数据会存在问题
                        /*
                         * if (obj.getType().equals("money")) {
                         * value = StringUtil.formatToCurrency(value);
                         * }
                         */
                        if (type.equals("date") || mask.equals("date")) {
                            value = DateUtil.getDateFormat(ClassHelper.convert(map.get(key), Date.class));
                        }
                        if (type.toLowerCase().equals("datetime") || mask.toLowerCase().equals("date")) {
                            value = DateUtil.getDateFormat(3, ClassHelper.convert(map.get(key), Date.class));
                        }
                    }
                    data.put(obj.getCode(), value);
                }
            }
            datas.add(data);
        }
        return datas;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> comboGridSearch(final EasySearchParse dto, SDO sdo) {
        SQLExecutor executor = this.buildSqlCondition(dto.getEasySearch(), dto.getSql(), sdo);
        dto.putAllParam(executor.parseParamMap());
        dto.setSql(executor.getSql());
        // 调用外部接口
        this.easySearchParse(dto);
        // 重新构建查询条件
        String sql = dto.buildSql();
        Map<String, Object> param = dto.getQueryParams();
        param.put("currentPersonId", sdo.getOperator().getUserId());
        param.put("currentOrgId", sdo.getOperator().getOrgId());
        param.put("currentPositionId", sdo.getOperator().getPositionId());
        param.put("currentPersonMemberId", sdo.getOperator().getPersonMemberId());
        param.put("currentDeptId", sdo.getOperator().getDeptId());
        param.put("currentFullId", sdo.getOperator().getFullId());
        param.put("currentTenantId", sdo.getOperator().getTenantId());
        QueryModel query = new QueryModel();
        query.initPageInfo(sdo.getProperties());
        query.setQueryParams(param);
        query.setSql(sql);
        query.setDefaultOrderBy(dto.getOrderby());
        query.setDictionaryMap(dto.getDictionaryMap());
        Map<String, Object> map = sqlQuery.executeSlicedQuery(query);
        List<Map<String, Object>> data = (List<Map<String, Object>>) map.get(Constants.ROWS);
        map.put(Constants.ROWS, reBuilderData(dto, data));
        return map;
    }
}
