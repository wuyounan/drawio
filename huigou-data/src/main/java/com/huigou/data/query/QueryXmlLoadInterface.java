package com.huigou.data.query;

import com.huigou.exception.ResourceLoadException;

public interface QueryXmlLoadInterface {
    public QueryXmlModel loadConfigFile(String path) throws ResourceLoadException;
}
