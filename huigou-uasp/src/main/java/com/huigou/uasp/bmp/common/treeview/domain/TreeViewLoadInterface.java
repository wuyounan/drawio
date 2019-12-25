package com.huigou.uasp.bmp.common.treeview.domain;


import com.huigou.exception.ResourceLoadException;
import com.huigou.uasp.bmp.common.treeview.domain.model.TreeViewMappingModel;

/**
 * 快捷查询调用解析接口
 *
 * @author xx
 */
public interface TreeViewLoadInterface {

    /**
     * @since 1.1.3
     */
    String XML_BASE_DIRECTORY = "config/content/treeview";
    /**
     * 配置文件默认路径
     */
    String XML_PATH = String.join("/", XML_BASE_DIRECTORY, "tree-view-%s.xml");

    /**
     * 方言配置文件路径
     *
     * @since 1.1.3
     */
    String DIALECT_XML_PATH = String.join("/", XML_BASE_DIRECTORY, "%s", "tree-view-%s.xml");

    TreeViewMappingModel loadConfigFile(String path) throws ResourceLoadException;
}
