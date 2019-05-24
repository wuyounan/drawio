package com.huigou.domain;

import java.util.Map;

import com.huigou.context.Operator;

public interface QueryRequest {
    void initPageModel(Map<String, Object> params);

    void setOperator(Operator operator);
}
