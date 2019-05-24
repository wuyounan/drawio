package com.huigou.domain;

import java.util.Collection;

public interface IdentifiedEntity {
    String getId();

    void setId(String id);

    Long getVersion();

    void setVersion(Long version);

    boolean isNew();

    //void fromMap(Map<String, Object> params);

    void setUpdateFields_(Collection<String> names);

    //void setUpdateFields_(String... updateFields);
}
