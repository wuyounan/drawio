package com.huigou.uasp.bmp.common.treeview.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huigou.data.jdbc.JDBCDao;
import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.data.query.model.SQLModel;
import com.huigou.uasp.bmp.common.treeview.application.TreeViewApplication;
import com.huigou.uasp.bmp.common.treeview.domain.model.TreeModel;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * 查询树结构
 * 
 * @author gongmm
 */
public class TreeViewApplicationImpl implements TreeViewApplication {

    private JDBCDao jdbcDao;

    private QueryPermissionBuilder permissionBuilder;

    public void setJdbcDao(JDBCDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPermissionBuilder(QueryPermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    /**
     * 组合树结构
     * 
     * @param model
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> treeBuilder(final TreeModel model, final Map<String, Object> param) throws Exception {
        String sql = model.getRootSql(param);
        String manageType = model.getManageType();
        if (!StringUtil.isBlank(manageType)) {
            SQLModel sqlModel = permissionBuilder.applyManagementPermissionForTree(sql, manageType);
            sql = sqlModel.getSql();
            param.putAll(sqlModel.getQueryParams());
        }
        List<Map<String, Object>> root = addPermissionFlag(jdbcDao.queryToMapListByMapParam(sql, param), manageType);
        if (!model.isAjax()) {
            for (Map<String, Object> m : root) {
                int num = ClassHelper.convert(m.get("hasChildren"), Integer.class, 0);
                if (num > 0) {
                    List<Map<String, Object>> children = treeBuilderChildren(m.get(StringUtil.getHumpName(model.getPrimarykey())).toString(), model, param);
                    m.put("children", children);
                }
            }
        }
        return root;
    }

    /**
     * 第归查询子节点
     * 
     * @author
     * @param id
     * @param model
     * @param param
     * @return
     * @throws Exception
     *             List<Map<String,Object>>
     */
    public List<Map<String, Object>> treeBuilderChildren(String id, TreeModel model, Map<String, Object> param) throws Exception {
        String sql = model.getChildrenSql(param);
        String manageType = model.getManageType();
        if (!StringUtil.isBlank(manageType)) {
            SQLModel sqlModel = permissionBuilder.applyManagementPermissionForTree(sql, manageType);
            sql = sqlModel.getSql();
            param.putAll(sqlModel.getQueryParams());
        }
        param.put(TreeModel.TREE_PARENT_ID, id);
        List<Map<String, Object>> list = addPermissionFlag(jdbcDao.queryToMapListByMapParam(sql, param), manageType);
        if (!model.isAjax()) {
            for (Map<String, Object> m : list) {
                int num = ((Number) m.get("hasChildren")).intValue();
                if (num > 0) {
                    List<Map<String, Object>> children = treeBuilderChildren(m.get(StringUtil.getHumpName(model.getPrimarykey())).toString(), model, param);
                    m.put("children", children);
                }
            }
        }
        return list;
    }

    /**
     * 树节点添加权限标志
     * 
     * @param list
     * @param manageType
     *            管理权限类别
     * @return
     */
    private List<Map<String, Object>> addPermissionFlag(List<Map<String, Object>> list, String manageType) {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>(list.size());
        if (StringUtil.isBlank(manageType)) {
            return list;
        }
        boolean flag = true;
        String fullId = null;
        for (Map<String, Object> m : list) {
            flag = true;
            if (!manageType.equals(Constants.NO_CONTROL_AUTHORITY)) {
                fullId = ClassHelper.convert(m.get("fullId"), String.class);
                if (!StringUtil.isBlank(fullId)) {
                    flag = permissionBuilder.hasManagementPermission(manageType, fullId);
                }
            }
            m.put("managerPermissionFlag", flag);
            l.add(m);
        }
        return l;
    }
}
