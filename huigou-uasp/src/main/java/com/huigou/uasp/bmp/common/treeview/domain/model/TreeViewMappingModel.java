package com.huigou.uasp.bmp.common.treeview.domain.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.huigou.data.query.XMLParseUtil;
import com.huigou.data.query.parser.model.ConditionModel;
import com.huigou.uasp.bmp.treeview.AjaxDocument;
import com.huigou.uasp.bmp.treeview.ConditionDocument.Condition;
import com.huigou.uasp.bmp.treeview.DataModelDocument.DataModel;
import com.huigou.uasp.bmp.treeview.TreeDocument.Tree;
import com.huigou.uasp.bmp.treeview.TreeMappingsDocument.TreeMappings;
import com.huigou.util.ConfigFileVersion;

/**
 * 树查询配置模型
 *
 * @author
 * @version V1.0
 * @ClassName: TreeViewMappingModel
 * @Description: TODO
 * @date 2014-3-11 上午09:58:04
 */
public class TreeViewMappingModel implements Serializable, ConfigFileVersion {

    private static final long serialVersionUID = 173704882069216812L;

    /**
     * 包含查询配置文件
     */
    private final List<Map<String, TreeModel>> trees;

    private Long version;

    private List<String> configFilePaths;

    public TreeViewMappingModel(TreeMappings mapping) {
        this(Arrays.asList(mapping));
    }

    /**
     * @since 1.1.3
     */
    public TreeViewMappingModel(List<TreeMappings> mappings) {
        trees = mappings.stream()
                .map(mapping -> Arrays.stream(mapping.getTreeArray()).collect(Collectors.toMap(Tree::getName, this::parseTreeModel)))
                .collect(Collectors.toList());
    }

    /**
     * 解析树模型
     *
     * @param tree
     * @return
     */
    private TreeModel parseTreeModel(Tree tree) {
        TreeModel model = new TreeModel();
        model.setName(tree.getName());
        model.setDesc(tree.getDesc());
        model.setServiceName(tree.getServiceName());
        model.setAjax(tree.getAjax() != null && tree.getAjax() == AjaxDocument.Ajax.TRUE);
        DataModel dataModel = tree.getDataModel();
        model.setTable(dataModel.getTable());
        model.setPrimarykey(dataModel.getPrimaryKey());
        model.setConnectby(dataModel.getConnectBy());
        model.setLabel(dataModel.getLabel());
        model.setRoot(dataModel.getRoot());
        model.setHiddencol(dataModel.getHiddenCol());
        model.setDefaultCondition(dataModel.getDefaultCondition());
        model.setOrderby(dataModel.getOrderby());
        model.setOrder(dataModel.getOrder());
        for (Condition condition : dataModel.getConditionArray()) {
            // 根据查询条件定义组合查询语句
            ConditionModel conditionModel = ConditionModel.newInstance(condition);
            conditionModel.setFormula(XMLParseUtil.getNodeTextValue(condition));
            model.addConditions(conditionModel);
        }
        return model;
    }

    public TreeModel getTreeModel(String name) {
        return trees.stream()
                .map(queryScheme -> queryScheme.get(name))
                .filter(Objects::nonNull)
                .findFirst()
                .get();
    }

    @Override
    public Long getVersion() {
        return version;
    }

    public void setVersions(Long version) {
        this.version = version;
    }

    @Override
    public String getFilePath() {
        return configFilePaths.get(0);
    }

    /**
     * @deprecated 已被 {@link #setConfigFilePaths(List)} 替代。
     */
    @Deprecated
    public void setConfigFilePath(String configFilePath) {
        this.configFilePaths = Collections.singletonList(configFilePath);
    }

    public void setConfigFilePaths(List<String> configFilePaths) {
        this.configFilePaths = configFilePaths;
    }

    @Override
    public List<String> getFilePaths() {
        return configFilePaths;
    }
}
