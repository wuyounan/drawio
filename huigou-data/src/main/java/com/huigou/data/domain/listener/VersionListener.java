package com.huigou.data.domain.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.beans.factory.annotation.Configurable;

import com.huigou.data.jdbc.SQLQuery;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.util.ApplicationContextWrapper;

@Configurable
public class VersionListener {
    
    public final static String GET_NEXT_SEQ_SQL = "SELECT version_seq.nextval from DUAL";
    
    private boolean inited = false;

    private SQLQuery sqlQuery;

    private Long getNextId() {
        if (!inited) {
            sqlQuery = ApplicationContextWrapper.getBean("sqlQuery", SQLQuery.class);
            inited = true;
        }
        Long version = sqlQuery.getJDBCDao().queryToLong(GET_NEXT_SEQ_SQL);
        return version;
    }

    @PrePersist
    public void beforeCreate(IdentifiedEntity target) {
        target.setVersion(getNextId());
    }

    @PreUpdate
    public void beforeUpdate(IdentifiedEntity target) {
        target.setVersion(getNextId());
    }

}
