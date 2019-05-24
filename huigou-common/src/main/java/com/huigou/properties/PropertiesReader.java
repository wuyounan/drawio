package com.huigou.properties;

import com.huigou.exception.ResourceLoadException;

public interface PropertiesReader {

    public PropertiesModel loadConfigFile(String path) throws ResourceLoadException;
}
