package com.huigou.uasp.bmp.common.treeview.domain.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huigou.data.query.SQLExecutor;
import com.huigou.data.query.parser.model.ConditionModel;
import com.huigou.util.StringUtil;

/**
 * 树结构模型类
 * 
 * @author Gerald
 */
public class TreeModel implements Serializable {

    private static final long serialVersionUID = -1704138457417521481L;

    public static final String TREE_PARENT_ID = "treeParentId";

    private String name;

    private String desc;

    private String table;

    private String primarykey;

    private String connectby;

    private String label;

    private String orderby;

    private String order;

    private String root;

    private String hiddencol;

    private String defaultCondition;

    private String manageType;

    private String serviceName;

    private boolean ajax = false;

    private List<ConditionModel> conditions = null;

    public TreeModel() {
        conditions = new ArrayList<ConditionModel>();
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public String getDefaultCondition() {
        return defaultCondition;
    }

    public void setDefaultCondition(String defaultCondition) {
        this.defaultCondition = defaultCondition;
    }

    public String getConnectby() {
        return connectby;
    }

    public void setConnectby(String connectby) {
        this.connectby = connectby;
    }

    public String getHiddencol() {
        return hiddencol;
    }

    public void setHiddencol(String hiddencol) {
        this.hiddencol = hiddencol;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(String primarykey) {
        this.primarykey = primarykey;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<ConditionModel> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionModel> conditions) {
        this.conditions = conditions;
    }

    public void addConditions(ConditionModel cm) {
        this.conditions.add(cm);
    }

    private String getConditionSql(Map<String, Object> param, boolean isAddParam, String alias) {
        StringBuffer conditionSql = new StringBuffer();
        if (!StringUtil.isBlank(defaultCondition)) {
            defaultCondition = defaultCondition.trim();
            String start = defaultCondition.substring(0, 3);
            conditionSql.append(" ");
            if (start.toUpperCase().equals("AND")) {
                conditionSql.append(defaultCondition);
            } else {
                conditionSql.append("and ").append(defaultCondition);
            }
        }
        if (conditions != null && conditions.size() > 0) {
            SQLExecutor executor = new SQLExecutor();
            executor.putAll(param);
            for (ConditionModel condition : conditions) {
                condition.setAlias(alias);
                executor.buildSqlCondition(condition);
            }
            executor.buildSql();
            if (isAddParam) {
                param.putAll(executor.parseParamMap());
            } else {
                executor.parseParamMap();
            }
            conditionSql.append(" ").append(executor.getSql());
        }
        return conditionSql.toString();
    }

    private String getHeadSql(Map<String, Object> param) {
        StringBuffer sb = new StringBuffer("select ");
        sb.append(primarykey).append(",");
        sb.append(connectby).append(",");
        sb.append(label).append(",");
        if (hiddencol != null && !hiddencol.equals("")) {
            String[] fs = hiddencol.split(",");
            for (String f : fs) {
                sb.append(f).append(",");
            }
        }
        sb.append("(select count(0) from ").append(table).append(" a where a.").append(connectby).append("=t.").append(primarykey).append(" ")
          .append(getConditionSql(param, false, "a")).append(")as has_children");
        sb.append(" from ").append(table).append(" t");
        return sb.toString();
    }

    public String getRootSql(Map<String, Object> param) {
        StringBuffer sb = new StringBuffer(getHeadSql(param));
        sb.append(" where ");
        if (root != null && !root.equals("")) {
            sb.append(root);
        } else {
            sb.append(connectby).append(" is null");
        }
        sb.append(" ").append(getConditionSql(param, true, "t")).append(getOrderSql());
        return sb.toString();
    }

    private String getOrderSql() {
        StringBuffer sb = new StringBuffer();
        if (orderby != null && !orderby.equals("")) {
            sb.append(" order by ").append(orderby);
        }
        if (order != null && !order.equals("")) sb.append(" ").append(order);
        return sb.toString();
    }

    public String getChildrenSql(Map<String, Object> param) {
        StringBuffer sb = new StringBuffer(getHeadSql(param));
        sb.append(" where ").append(connectby).append("= :" + TREE_PARENT_ID);
        sb.append(" ").append(getConditionSql(param, true, "t")).append(getOrderSql());
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 通过反射的方式设置参数
     * 
     * @param key
     * @param value
     */
    public void setParameter(String key, String value) {
        Class<?> klass = this.getClass();
        try {
            Field f = klass.getDeclaredField(key.toLowerCase());
            if (f != null) f.set(this, value);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
