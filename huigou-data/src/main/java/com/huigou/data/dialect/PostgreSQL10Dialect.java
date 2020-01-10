package com.huigou.data.dialect;

public class PostgreSQL10Dialect  extends org.hibernate.dialect.PostgresPlusDialect {
    @Override
    public String getSelectGUIDString(){
        return "select replace(cast(uuid_generate_v4() as VARCHAR), '-', '')";
    }
}
