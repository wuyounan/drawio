package com.huigou.uasp.bmp.common.easysearch.domain;

import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchMappingModel;

public interface EasySearchLoadInterface {

    /**
     * @since 1.1.3
     */
    String XML_BASE_DIRECTORY = "config/content/easysearch";
    /**
     * 配置文件默认路径
     */
    String XML_PATH = String.join("/", XML_BASE_DIRECTORY, "easy-search-%s.xml");

    /**
     * 方言配置文件路径
     *
     * @since 1.1.3
     */
    String DIALECT_XML_PATH = String.join("/", XML_BASE_DIRECTORY, "%s", "easy-search-%s.xml");


    /**
     * 加载配置文件
     */
    EasySearchMappingModel loadConfigFile(String path) throws ResourceLoadException;
}
