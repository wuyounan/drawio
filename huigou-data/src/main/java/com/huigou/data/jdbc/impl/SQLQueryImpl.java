package com.huigou.data.jdbc.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.huigou.cache.SystemCache;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.dialect.Dialect;
import com.huigou.data.dialect.DialectUtils;
import com.huigou.data.domain.query.QueryPageRequest;
import com.huigou.data.excel.exporter.ExportExcel;
import com.huigou.data.excel.exporter.XSSFExport;
import com.huigou.data.exception.ExportExcelException;
import com.huigou.data.exception.SQLParseException;
import com.huigou.data.jdbc.JDBCDao;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.data.query.model.QueryModel;
import com.huigou.data.query.model.SQLModel;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 公用查询组合显示数据
 */
public class SQLQueryImpl implements SQLQuery {

    private JDBCDao jdbcDao;

    private QueryPermissionBuilder permissionBuilder;

    public JDBCDao getJdbcDao() {
        return jdbcDao;
    }

    public void setJdbcDao(JDBCDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public QueryPermissionBuilder getPermissionBuilder() {
        return permissionBuilder;
    }

    public void setPermissionBuilder(QueryPermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    public JDBCDao getJDBCDao() {
        return jdbcDao;
    }

    @Override
    public Map<String, Object> executeSlicedQuery(QueryModel queryModel) {
        Map<String, Object> result = null;
        try {
            ThreadLocalUtil.putVariable(Constants.DICTIONARY_MAP, queryModel.getDictionaryMap());
            if (!queryModel.isExportQuery()) {
                result = internalExecuteSlicedQuery(queryModel);
                result.put(Constants.PAGE_PARAM_NAME, queryModel.getPageIndex());// 当前页数设置
            } else {// 出生成待导出文件
                parseExportHead(queryModel);
                result = this.exportExecuteQuery(queryModel);
            }
        } catch (ExportExcelException e) {
            throw new ExportExcelException(e);
        } catch (Exception e) {
            throw new SQLParseException(e);
        } finally {
            ThreadLocalUtil.removeVariable(Constants.DICTIONARY_MAP);
        }
        return result;
    }

    public Map<String, Object> executeQuery(QueryModel queryModel) {
        Map<String, Object> result = null;
        try {
            ThreadLocalUtil.putVariable(Constants.DICTIONARY_MAP, queryModel.getDictionaryMap());
            if (!queryModel.isExportQuery()) {
                result = internalExecuteQuery(queryModel);
            } else {// 出生成待导出文件
                parseExportHead(queryModel);
                result = this.exportExecuteQuery(queryModel);
            }
        } catch (ExportExcelException e) {
            throw new ExportExcelException(e);
        } catch (Exception e) {
            throw new SQLParseException(e);
        } finally {
            ThreadLocalUtil.removeVariable(Constants.DICTIONARY_MAP);
        }
        return result;
    }

    /**
     * 执行查询
     * 
     * @author
     * @param queryModel
     * @param orderby
     * @param order
     * @return Map<String,Object>
     */
    public Map<String, Object> executeQuery(QueryModel queryModel, String orderby, String order) {
        queryModel.setSortFieldName(orderby);
        queryModel.setSortOrder(order);
        Map<String, Object> result = null;
        try {
            ThreadLocalUtil.putVariable(Constants.DICTIONARY_MAP, queryModel.getDictionaryMap());
            result = internalExecuteQuery(queryModel);
        } catch (Exception e) {
            throw new SQLParseException(e);
        } finally {
            ThreadLocalUtil.removeVariable(Constants.DICTIONARY_MAP);
        }
        return result;
    }

    /**
     * 解析导出表头
     * 
     * @Title: parseExportHead
     * @author @param
     * @return void
     * @throws
     */
    private void parseExportHead(QueryModel queryModel) {
        // 如果存在自己定义的表头则不执行解析过程
        if (!StringUtil.isBlank(queryModel.getXmlFilePath())) return;
        if (!StringUtil.isBlank(queryModel.getXmlHeads())) return;
        String head = queryModel.getExportHead();
        if (!StringUtil.isBlank(head)) {
            queryModel.setXmlHeads(head);
        }
    }

    /**
     * 导出数据生成Excel表格
     * 
     * @param datas
     * @param obj
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private Map<String, Object> exportExcel(Map<String, Object> datas, QueryModel obj) throws Exception {
        return ExportExcel.doExport(datas, obj.getXmlHeads(), obj.getXmlFilePath(), obj.getExportExcelType());
    }

    /**
     * 组合排序 SQL
     * 
     * @param queryModel
     * @param sql
     * @return
     */
    private String combineOrderBySql(QueryModel queryModel, String sql) {
        StringBuffer sb = new StringBuffer();
        if (!StringUtil.isBlank(queryModel.getManageType())) {
            sb.append(sql);
        } else {
            sb.append("select * from (").append(sql).append(")");
            sb.append(" where 1=1 ");
        }
        sb.append(queryModel.parseSortFields());
        return sb.toString();
    }

    /**
     * 组合权限查询sql
     * 
     * @param queryModel
     * @return
     */
    private String getPermissionQuerySql(QueryModel queryModel) {
        StringBuffer sb = new StringBuffer();
        if (queryModel.isNeedPermission() && !StringUtil.isBlank(queryModel.getManageType())) {// 组合权限控制SQL
            SQLModel sqlModel = null;
            if (queryModel.isTreeQuery()) {
                sqlModel = permissionBuilder.applyManagementPermissionForTree(queryModel.getSql(), queryModel.getManageType());
            } else {
                sqlModel = permissionBuilder.applyManagementPermission(queryModel.getSql(), queryModel.getManageType());
            }
            sb.append(sqlModel.getSql());
            queryModel.putAll(sqlModel.getQueryParams());
        } else {
            sb.append(queryModel.getSql());
        }
        return sb.toString();
    }

    /**
     * 内部执行分页查询
     * 
     * @author xiexin
     * @param queryModel
     * @return
     * @throws
     */
    private Map<String, Object> internalExecuteSlicedQuery(QueryModel queryModel) {
        String sql = getPermissionQuerySql(queryModel);
        Map<String, Object> map = new HashMap<String, Object>();
        boolean isNeedCount = queryModel.isNeedCount();
        if (isNeedCount) {// 判断是否需要执行条数统计
            int count = getTotal(sql, queryModel.getQueryParams());
            map.put(Constants.RECORD, count);
        }
        String querySql = this.combineOrderBySql(queryModel, sql);
        List<Map<String, Object>> datas = queryListForPaging(querySql, queryModel.getQueryParams(), queryModel.getPageIndex(), queryModel.getPageSize());
        map.put(Constants.ROWS, datas);
        if (!isNeedCount) {
            int count = 0;
            if (datas != null && datas.size() > 0) {
                count = datas.size();
            }
            map.put(Constants.RECORD, count);
        }
        if (queryModel.getTotalFields() != null && !queryModel.getTotalFields().equals("")) {
            Map<String, Object> totals = queryTotalByFields(sql, queryModel.getTotalFields(), queryModel.getQueryParams());
            if (totals != null) {
                map.put(Constants.TOTAL_FIELDS, totals);
            }
        }
        return map;
    }

    /**
     * 内部执行查询
     * 
     * @param queryModel
     * @param cuser
     * @return
     */
    private Map<String, Object> internalExecuteQuery(QueryModel queryModel) {
        String sql = getPermissionQuerySql(queryModel);
        String querySql = this.combineOrderBySql(queryModel, sql);
        Map<String, Object> result = new HashMap<String, Object>(2);
        List<Map<String, Object>> data = jdbcDao.queryToMapListByMapParam(querySql, queryModel.getQueryParams());
        result.put(Constants.ROWS, data);
        result.put(Constants.RECORD, data.size());
        if (queryModel.getTotalFields() != null && !queryModel.getTotalFields().equals("")) {
            Map<String, Object> totals = queryTotalByFields(sql, queryModel.getTotalFields(), queryModel.getQueryParams());
            if (totals != null) {
                result.put(Constants.TOTAL_FIELDS, totals);
            }
        }
        return result;
    }

    /**
     * 导出查询
     * 
     * @author
     * @param queryModel
     * @return
     * @throws
     */
    public Map<String, Object> exportExecuteQuery(QueryModel queryModel) {
        String sql = getPermissionQuerySql(queryModel);
        int count = getTotal(sql, queryModel.getQueryParams());
        int maxExportCount = ClassHelper.convert(SystemCache.getParameter("exportExcelCount", String.class), Integer.class,
                                                 QueryPageRequest.DEFAULT_MAX_EXPORT_COUNT);
        if (count > maxExportCount) {
            throw new ExportExcelException("导出数据已超过设定上限，请调整查询条件。");
        }

        String querySql = this.combineOrderBySql(queryModel, sql);
        Map<String, Object> result = new HashMap<String, Object>(1);
        if (queryModel.getTotalFields() != null && !queryModel.getTotalFields().equals("")) {
            Map<String, Object> totals = queryTotalByFields(sql, queryModel.getTotalFields(), queryModel.getQueryParams());
            if (totals != null) {
                result.put(Constants.TOTAL_FIELDS, totals);
            }
        }
        Map<String, Object> m = new HashMap<String, Object>(1);
        try {
            XSSFExport exporter = new XSSFExport();
            Element headRoot = ExportExcel.readXml(queryModel.getXmlHeads(), queryModel.getXmlFilePath());
            exporter.setHeadRoot(headRoot);
            exporter.setDatas(result);
            String s = exporter.expExcel(querySql, queryModel.getQueryParams(), jdbcDao);
            File file = new File(s);
            if (file.exists()) {
                m.put("file", file.getName());
            } else {
                throw new ExportExcelException("文件生成失败。");
            }
        } catch (Exception e) {
            throw new ExportExcelException(e);
        }
        return m;
    }

    /**
     * 根据条件查询记录总数
     * 
     * @Title: getTotal
     * @author
     * @param sql
     * @param param
     * @return int
     */
    public int getTotal(final String sql, final Map<String, Object> param) {
        String totalSql = Dialect.getTotalSql(sql);
        return jdbcDao.queryToObjectByMapParam(totalSql, Integer.class, param);
    }

    /**
     * 组合分页SQL 执行查询
     * 
     * @Title: queryListForPaging
     * @author @param sql
     * @param param
     * @param firstResult
     * @param rows
     * @return List<Map<String,Object>>
     */
    public List<Map<String, Object>> queryListForPaging(final String sql, final Map<String, Object> param, final int pageIndex, final int pageSize) {
        Dialect d = DialectUtils.guessDialect(jdbcDao.getDataSource());
        String pageSql = d.paginate(pageIndex, pageSize, sql);
        return jdbcDao.queryToMapListByMapParam(pageSql, param);
    }

    /**
     * 组合查询合计SQL并执行查询
     * 
     * @Title: queryTotalByFields
     * @author
     * @param sql
     * @param totalFields
     * @param param
     * @return Map<String,Object>
     */
    public Map<String, Object> queryTotalByFields(final String sql, final String totalFields, final Map<String, Object> param) {
        String totalSql = Dialect.getTotleFieldsSql(sql, totalFields);
        if (StringUtil.isBlank(totalSql)) {
            return null;
        }
        return jdbcDao.queryToMapByMapParam(totalSql, param);
    }
}
