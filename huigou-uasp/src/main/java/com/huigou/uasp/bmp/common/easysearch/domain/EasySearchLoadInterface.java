package com.huigou.uasp.bmp.common.easysearch.domain;

import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchMappingModel;

public interface EasySearchLoadInterface {
    /**
     * 配置文件默认路径
     */
    public final static String XML_PATH = "config/content/easysearch/easy-search-%s.xml";

    public EasySearchMappingModel loadConfigFile(String path) throws ResourceLoadException;
}
