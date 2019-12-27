package com.huigou.data.domain.listener;

import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.util.ApplicationContextWrapper;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Configurable
public class VersionListener {

    @Deprecated
    public final static String GET_NEXT_SEQ_SQL = "SELECT version_seq.nextval from DUAL";

    private Long getNextId() {
        SQLExecutorDao sqlExecutor = ApplicationContextWrapper.getBean("sqlExecutorDao", SQLExecutorDao.class);
        QueryDescriptor queryDescriptor = sqlExecutor.getQuery("config/uasp/query/bmp/common.xml", "common");
        return sqlExecutor.getSqlQuery().getJDBCDao().queryToLong(String.format(queryDescriptor.getSqlByName("nextSequence"), "version_seq"));
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
