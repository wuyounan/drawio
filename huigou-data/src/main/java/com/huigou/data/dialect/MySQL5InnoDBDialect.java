package com.huigou.data.dialect;

/**
 * @author yonghuan
 * @since 1.1.3
 */
public class MySQL5InnoDBDialect extends org.hibernate.dialect.MySQL5InnoDBDialect {

    @Override
    public String getSelectGUIDString() {
        return "select replace(uuid(),'-','')";
    }
}
