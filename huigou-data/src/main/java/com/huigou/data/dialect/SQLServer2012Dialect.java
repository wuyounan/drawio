package com.huigou.data.dialect;

public class SQLServer2012Dialect extends org.hibernate.dialect.SQLServer2008Dialect {
    @Override
    public String getSelectGUIDString() {
        return "SELECT REPLACE(NEWID(),'-','')";
    }
}
