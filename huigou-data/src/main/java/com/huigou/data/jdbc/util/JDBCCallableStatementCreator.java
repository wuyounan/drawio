package com.huigou.data.jdbc.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;

/**
 * 实现 spring CallableStatementCreator接口 生成CallableStatement
 * 
 * @author
 * @version V1.0
 */
public class JDBCCallableStatementCreator implements CallableStatementCreator {
    private Object[] params;

    private String sql;

    @SuppressWarnings("rawtypes")
    private List declaredParameters;

    @SuppressWarnings("rawtypes")
    public JDBCCallableStatementCreator(final String sql, final Object[] params, List declaredParameters) {
        this.params = params;
        this.sql = sql;
        this.declaredParameters = declaredParameters;
    }

    public CallableStatement createCallableStatement(Connection con) throws SQLException {
        CallableStatement cs = con.prepareCall(sql);
        int i = 1;
        if (null != params && params.length > 0) {
            RowSetUtil.setStatementParams(cs, params);
        }
        if ((null == declaredParameters) || (0 == declaredParameters.size())) {
            return cs;
        } else {
            for (; i <= declaredParameters.size(); i++) {
                if (declaredParameters.get(i - 1) instanceof SqlOutParameter) {
                    cs.registerOutParameter(i, ((SqlOutParameter) declaredParameters.get(i - 1)).getSqlType());
                }
            }
        }
        return cs;
    }

}
