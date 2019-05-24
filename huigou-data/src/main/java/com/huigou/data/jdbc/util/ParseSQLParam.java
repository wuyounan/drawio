package com.huigou.data.jdbc.util;

import java.util.ArrayList;
import java.util.List;

import com.huigou.data.exception.SQLParseException;

/**
 * 解析带参数的SQL
 * 
 * @author xx
 */
public class ParseSQLParam {
    private StringBuffer sql = new StringBuffer();

    private List<String> parameter = new ArrayList<String>(4);

    private List<Object> values = new ArrayList<Object>(4);

    public String getParseSql() {
        return sql.toString();
    }

    public List<String> getParameter() {
        return parameter;
    }

    public List<Object> getValues() {
        return values;
    }

    public void addValue(Object value) {
        this.values.add(value);
    }

    public static int firstIndexOfChar(String sqlString, String string, int startindex) {
        int matchAt = -1;
        for (int i = 0; i < string.length(); i++) {
            int curMatch = sqlString.indexOf(string.charAt(i), startindex);
            if (curMatch < 0) continue;
            if (matchAt == -1) matchAt = curMatch;
            else
                matchAt = Math.min(matchAt, curMatch);
        }
        return matchAt;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public void other(char character) {
        sql.append(character);
    }

    public void namedParameter(String name) {
        parameter.add(name);
        sql.append('?');
    }

    public void parse(String sqlString) throws SQLParseException {
        int stringLength = sqlString.length();
        boolean inQuote = false;
        for (int indx = 0; indx < stringLength; indx++) {
            char c = sqlString.charAt(indx);
            if (inQuote) {
                if ('\'' == c) inQuote = false;
                other(c);
                continue;
            }
            if ('\'' == c) {
                inQuote = true;
                other(c);
                continue;
            }
            if (c == ':') {
                int right = firstIndexOfChar(sqlString, " \n\r\f\t,()=<>&|+-=/*'^![]#~\\", indx + 1);
                int chopLocation = right >= 0 ? right : sqlString.length();
                String param = sqlString.substring(indx + 1, chopLocation);
                if (isEmpty(param)) {
                    throw new SQLParseException("Space is not allowed after parameter prefix ':' '" + sqlString + "'");
                }
                namedParameter(param);
                indx = chopLocation - 1;
                continue;
            }
            if (c == '?') {
                if (indx < stringLength - 1 && Character.isDigit(sqlString.charAt(indx + 1))) {
                    int right = firstIndexOfChar(sqlString, " \n\r\f\t,()=<>&|+-=/*'^![]#~\\", indx + 1);
                    int chopLocation = right >= 0 ? right : sqlString.length();
                    String param = sqlString.substring(indx + 1, chopLocation);
                    try {
                        new Integer(param);
                    } catch (NumberFormatException e) {
                        throw new SQLParseException("JPA-style positional param was not an integral ordinal");
                    }
                    namedParameter(param);
                    indx = chopLocation - 1;
                    continue;
                }
                other(c);
            } else {
                other(c);
            }
        }
    }
}
