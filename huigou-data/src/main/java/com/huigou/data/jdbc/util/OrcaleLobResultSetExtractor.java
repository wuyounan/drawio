package com.huigou.data.jdbc.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobHandler;

import com.huigou.cache.SystemCache;
import com.huigou.util.ListUtil;
import com.huigou.util.StringUtil;

@SuppressWarnings("rawtypes")
public class OrcaleLobResultSetExtractor extends AbstractLobStreamingResultSetExtractor {
    private LobHandler lobHandler;

    private String clobString;

    private String[] clobColumnNames;

    private Map<String, Object> map;

    public OrcaleLobResultSetExtractor(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public OrcaleLobResultSetExtractor(LobHandler lobHandler, String[] clobColumnNames) {
        this.lobHandler = lobHandler;
        this.clobColumnNames = clobColumnNames;
    }

    public String getClobString() {
        return clobString;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    protected void handleNoRowFound() throws DataAccessException {
        // 覆盖父级方法不抛出异常
        map = new HashMap<String, Object>(1);
    }

    @Override
    protected void streamData(ResultSet resultset) throws SQLException, IOException, DataAccessException {
        if (clobColumnNames == null || clobColumnNames.length == 0) {
            clobString = lobHandler.getClobAsString(resultset, 1);
        } else {
            ResultSetMetaData rsmd = resultset.getMetaData();
            int columnCount = rsmd.getColumnCount();
            map = new HashMap<String, Object>(columnCount);
            String columnName = "";
            String key = "";
            for (int i = 0; i < columnCount; i++) {
                columnName = rsmd.getColumnLabel(i + 1).trim();
                key = StringUtil.getHumpName(columnName);
                if (ListUtil.contains(clobColumnNames, key)) {
                    map.put(key, lobHandler.getClobAsString(resultset, columnName));
                } else {
                    Object value = resultset.getObject(i + 1);
                    String textView = SystemCache.getDictionaryDetailText(key, value);
                    if (textView != null) {
                        map.put(key + "TextView", textView);
                    }
                    map.put(key, RowSetUtil.convertValue(value));
                }
            }
        }

    }
}
