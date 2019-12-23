package com.huigou.data.query;

import com.huigou.exception.ResourceLoadException;

public interface QueryXmlLoadInterface {
    QueryXmlModel loadConfigFile(String path) throws ResourceLoadException;
}
