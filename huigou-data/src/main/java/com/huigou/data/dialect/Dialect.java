package com.huigou.data.dialect;

import java.sql.Connection;

import javax.sql.DataSource;

import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.dialect.pagination.SQLServer2005LimitHandler;
import org.hibernate.engine.spi.RowSelection;

import com.huigou.util.StringUtil;

/**
 * this is a small Java tool collect all databases' dialect, most data are
 * extracted from Hibernate, usually jDialects is used for build pagination SQL
 * and DDL SQL for cross-databases developing. Currently jDialects support ~70
 * database dialects. It has no any 3rd party dependency, run on JDK1.6 or
 * above.
 * 
 * @author xx
 * @version 1.0.0
 */
public enum Dialect {
    // below found on Internet
    SQLiteDialect,
    AccessDialect,
    ExcelDialect,
    TextDialect,
    ParadoxDialect,
    CobolDialect,
    XMLDialect,
    DbfDialect, // NOSONAR
    // below are from Hibernate
    @Deprecated
    DerbyDialect, // Use other Derby version instead
    @Deprecated
    OracleDialect, // Use Oracle8iDialect instead
    @Deprecated
    Oracle9Dialect, // Use Oracle9i instead
    Cache71Dialect,
    CUBRIDDialect,
    DerbyTenFiveDialect,
    DataDirectOracle9Dialect,
    DB2Dialect,
    DB2390Dialect,
    DB2400Dialect,
    DerbyTenSevenDialect,
    DerbyTenSixDialect,
    FirebirdDialect,
    FrontBaseDialect,
    H2Dialect,
    HANAColumnStoreDialect,
    HANARowStoreDialect,
    HSQLDialect,
    InformixDialect,
    Informix10Dialect,
    IngresDialect,
    Ingres10Dialect,
    Ingres9Dialect,
    InterbaseDialect,
    JDataStoreDialect,
    MariaDBDialect,
    MariaDB53Dialect,
    MckoiDialect,
    MimerSQLDialect,
    MySQLDialect,
    MySQL5Dialect,
    MySQL55Dialect,
    MySQL57Dialect,
    MySQL57InnoDBDialect,
    MySQL5InnoDBDialect,
    MySQLInnoDBDialect,
    MySQLMyISAMDialect,
    Oracle8iDialect,
    Oracle9iDialect,
    Oracle10gDialect,
    Oracle12cDialect,
    PointbaseDialect,
    PostgresPlusDialect,
    PostgreSQLDialect,
    PostgreSQL81Dialect,
    PostgreSQL82Dialect,
    PostgreSQL9Dialect,
    PostgreSQL91Dialect,
    PostgreSQL92Dialect,
    PostgreSQL93Dialect,
    PostgreSQL94Dialect,
    PostgreSQL95Dialect,
    ProgressDialect,
    RDMSOS2200Dialect,
    SAPDBDialect,
    SQLServerDialect,
    SQLServer2005Dialect,
    SQLServer2008Dialect,
    SQLServer2012Dialect,
    SybaseDialect,
    Sybase11Dialect,
    SybaseAnywhereDialect,
    SybaseASE15Dialect,
    SybaseASE157Dialect,
    TeradataDialect,
    Teradata14Dialect,
    TimesTenDialect;// NOSONAR

    private static final String SKIP_ROWS = "$SKIP_ROWS";

    private static final String PAGESIZE = "$PAGESIZE";

    private static final String TOTAL_ROWS = "$TOTAL_ROWS";

    private static final String SKIP_ROWS_PLUS1 = "$SKIP_ROWS_PLUS1";

    private static final String TOTAL_ROWS_PLUS1 = "$TOTAL_ROWS_PLUS1";

    private static final String DISTINCT_TAG = "($DISTINCT)";

    public static final String NOT_SUPPORT = "NOT_SUPPORT";

    private String sqlTemplate = null;

    private String topLimitTemplate = null;

    static {
        for (Dialect d : Dialect.values()) {
            d.sqlTemplate = DialectPaginationTemplate.initializePaginSQLTemplate(d);
            d.topLimitTemplate = DialectPaginationTemplate.initializeTopLimitSqlTemplate(d);
        }
    }

    /**
     * Guess Dialect by given databaseName, major & minor version if have
     * 
     * @param databaseName
     * @param majorVersionMinorVersion
     * @return Dialect
     */
    public static Dialect guessDialect(String databaseName, Object... majorVersionMinorVersion) {
        return DialectUtils.guessDialect(databaseName, majorVersionMinorVersion);
    }

    /**
     * Guess Dialect by given connection, note:this method does not close
     * connection
     * 
     * @param con
     *            The JDBC Connection
     * @return Dialect The Dialect intance, if can not guess out, return null
     */
    public static Dialect guessDialect(Connection connection) {
        return DialectUtils.guessDialect(connection);
    }

    /**
     * Guess Dialect by given data source
     * 
     * @param datasource
     * @return Dialect
     */
    public static Dialect guessDialect(DataSource datasource) {
        return DialectUtils.guessDialect(datasource);
    }

    /**
     * SQLServer is complex, don't want re-invent wheel, copy Hibernate's source
     * code in this project to do the dirty job, that's why this project use
     * LGPL license
     */
    private static String processSQLServer(Dialect dialect, int pageNumber, int pageSize, String sql) {
        int skipRows = (pageNumber - 1) * pageSize;
        int totalRows = pageNumber * pageSize;

        RowSelection selection = new RowSelection();
        selection.setFirstRow(skipRows);
        selection.setMaxRows(totalRows);
        String result = null;
        switch (dialect) {
        case SQLServer2005Dialect:
        case SQLServer2008Dialect:
            result = new SQLServer2005LimitHandler().processSql(sql, selection);
            break;
        case SQLServer2012Dialect:
            result = new SQLServer2005LimitHandler().processSql(sql, selection);
            break;
        default:
        }
        result = StringHelper.replace(result, "__hibernate_row_nr__", "_ROW_NUM_");
        // Replace a special top tag
        result = StringHelper.replaceOnce(result, " $Top_Tag(?) ", " TOP(" + totalRows + ") ");
        result = StringHelper.replaceOnce(result, "_ROW_NUM_ >= ? AND _ROW_NUM_ < ?", "_ROW_NUM_ >= " + (skipRows + 1) + " AND _ROW_NUM_ < " + (totalRows + 1));
        result = StringHelper.replaceOnce(result, "offset ? rows fetch next ? rows only", "offset " + skipRows + " rows fetch next " + pageSize + " rows only");
        result = StringHelper.replaceOnce(result, "offset 0 rows fetch next ? rows only", "offset 0 rows fetch next " + pageSize + " rows only");

        if (StringUtil.isBlank(result)) DialectException.throwEX("Unexpected error, please report this bug");
        return result;
    }

    /**
     * Create a pagination SQL by given pageNumber, pageSize and SQL<br/>
     * 
     * @param pageNumber
     *            The page number, start from 1
     * @param pageSize
     *            The page item size
     * @param sql
     *            The original SQL
     * @return The paginated SQL
     */
    public String paginate(int pageNumber, int pageSize, String sql) {// NOSONAR
        DialectException.assureNotNull(sql, "sql string can not be null");
        String trimedSql = sql.trim();
        DialectException.assureNotEmpty(trimedSql, "sql string can not be empty");
        switch (this) {
        case SQLServer2005Dialect:
        case SQLServer2008Dialect:
        case SQLServer2012Dialect:
            return processSQLServer(this, pageNumber, pageSize, trimedSql);
        default:
        }

        if (!StringUtil.startsWithIgnoreCase(trimedSql, "select ")) return (String) DialectException.throwEX("SQL should start with \"select \".");
        String body = trimedSql.substring(7).trim();
        DialectException.assureNotEmpty(body, "SQL body can not be empty");

        int skipRows = (pageNumber - 1) * pageSize;
        int skipRowsPlus1 = skipRows + 1;
        int totalRows = pageNumber * pageSize;
        int totalRowsPlus1 = totalRows + 1;
        String useTemplate = this.sqlTemplate;

        // use simple limit ? template if offset is 0
        if (skipRows == 0) useTemplate = this.topLimitTemplate;

        if (Dialect.NOT_SUPPORT.equals(useTemplate)) {
            if (!Dialect.NOT_SUPPORT.equals(this.topLimitTemplate)) return (String) DialectException.throwEX("Dialect \"" + this
                                                                                                             + "\" only support top limit SQL \"" + "\"");
            return (String) DialectException.throwEX("Dialect \"" + this + "\" does not support physical pagination");
        }

        if (useTemplate.contains(DISTINCT_TAG)) {
            // if distinct template use non-distinct sql, delete distinct tag
            if (!StringUtil.startsWithIgnoreCase(body, "distinct ")) useTemplate = StringUtil.replace(useTemplate, DISTINCT_TAG, "");
            else {
                // if distinct template use distinct sql, use it
                useTemplate = StringUtil.replace(useTemplate, DISTINCT_TAG, "distinct");
                body = body.substring(9);
            }
        }

        // if have $XXX tag, replaced by real values
        String result = StringUtil.replaceIgnoreCase(useTemplate, SKIP_ROWS, String.valueOf(skipRows));
        result = StringUtil.replaceIgnoreCase(result, PAGESIZE, String.valueOf(pageSize));
        result = StringUtil.replaceIgnoreCase(result, TOTAL_ROWS, String.valueOf(totalRows));
        result = StringUtil.replaceIgnoreCase(result, SKIP_ROWS_PLUS1, String.valueOf(skipRowsPlus1));
        result = StringUtil.replaceIgnoreCase(result, TOTAL_ROWS_PLUS1, String.valueOf(totalRowsPlus1));

        // now insert the customer's real full SQL here
        result = StringUtil.replace(result, "$SQL", trimedSql);

        // or only insert the body without "select "
        result = StringUtil.replace(result, "$BODY", body);
        return result;
    }

    /**
     * 获取一条数据sql
     * 
     * @param sql
     * @return
     */
    public String getQueryFirstSql(String sql) {
        return this.paginate(1, 1, sql);
    }

    /**
     * 组合查询合计SQL
     * 
     * @Title: getTotalSql
     * @author
     * @param sql
     * @return String
     */
    public static String getTotalSql(String sql) {
        return "select count(0) from (" + sql + ") __t__";
    }

    /**
     * 组合查询合计SQL
     * 
     * @Title: getTotleFieldsSql
     * @author
     * @param @param sql
     * @param @param totleFields
     * @param @return
     * @return String
     * @throws
     */
    public static String getTotleFieldsSql(String sql, String totleFields) {
        String[] fields = totleFields.split(",");
        String fieldName = "";
        StringBuffer sb = new StringBuffer("select ");
        for (int i = 0; i < fields.length; i++) {
            fieldName = StringUtil.getUnderscoreName(fields[i]);
            sb.append("sum(").append(fieldName).append(") as ").append(fieldName).append(",");
        }
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append(" from (").append(sql).append(")");
        return sb.toString();
    }

    /**
     * @return true if is MySql family
     */
    public boolean isMySqlFamily() {
        return this.toString().startsWith("MySQL");
    }

    /**
     * @return true if is Infomix family
     */
    public boolean isInfomixFamily() {
        return this.toString().startsWith("Infomix");
    }

    /**
     * @return true if is Oracle family
     */
    public boolean isOracleFamily() {
        return this.toString().startsWith("Oracle");
    }

    /**
     * @return true if is SQL Server family
     */
    public boolean isSQLServerFamily() {
        return this.toString().startsWith("SQLServer");
    }

    /**
     * @return true if is H2 family
     */
    public boolean isH2Family() {
        return H2Dialect.equals(this);
    }

    /**
     * @return true if is Postgres family
     */
    public boolean isPostgresFamily() {
        return this.toString().startsWith("Postgres");
    }

    /**
     * @return true if is Sybase family
     */
    public boolean isSybaseFamily() {
        return this.toString().startsWith("Sybase");
    }

    /**
     * @return true if is DB2 family
     */
    public boolean isDB2Family() {
        return this.toString().startsWith("DB2");
    }

    /**
     * @return true if is Derby family
     */
    public boolean isDerbyFamily() {
        return this.toString().startsWith("Derby");
    }

}
