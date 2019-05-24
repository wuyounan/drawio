package com.huigou.uasp.bmp.common.treeview.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.common.treeview.domain.model.TreeModel;

public interface TreeViewApplication {
    /**
     * 组合树结构
     *
     * @param model
     * @param param
     * @return
     */
    List<Map<String, Object>> treeBuilder(TreeModel model, Map<String, Object> param) throws Exception;

    /**
     * 查询子节点
     *
     * @param model
     * @param param
     * @return
     */
    List<Map<String, Object>> treeBuilderChildren(String id, TreeModel model, Map<String, Object> param) throws Exception;

}
