package com.huigou.uasp.bmp.common.treeview.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
 * @ClassName: TreeViewMappingModel
 * @Description: TODO
 * @author
 * @date 2014-3-11 上午09:58:04
 * @version V1.0
 */
public class TreeViewMappingModel implements Serializable, ConfigFileVersion {

    private static final long serialVersionUID = 173704882069216812L;

    private Map<String, TreeModel> trees;// 包含查询配置文件

    private Long version;

    private String configFilePath;

    public TreeViewMappingModel(TreeMappings mapping) {
        trees = new HashMap<String, TreeModel>(mapping.getTreeArray().length);
        for (Tree tree : mapping.getTreeArray()) {
            trees.put(tree.getName(), parseTreeModel(tree));
        }
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
        for (Condition condition : dataModel.getConditionArray()) {// 根据查询条件定义组合查询语句
            ConditionModel conditionModel = ConditionModel.newInstance(condition);
            conditionModel.setFormula(XMLParseUtil.getNodeTextValue(condition));
            model.addConditions(conditionModel);
        }
        return model;
    }

    public TreeModel getTreeModel(String name) {
        return trees.get(name);
    }

    @Override
    public Long getVersion() {
        return version;
    }

    public void setVersions(Long version) {
        this.version = version;
    }

    public String getFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

}
