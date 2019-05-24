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
     * 配置文件默认路径
     */
    public final static String XML_PATH = "config/content/treeview/tree-view-%s.xml";

    public TreeViewMappingModel loadConfigFile(String path) throws ResourceLoadException;
}
