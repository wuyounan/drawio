package com.huigou.data.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.datamanagement.DataManageFieldsGroup;
import com.huigou.data.datamanagement.DataManageResource;
import com.huigou.data.datamanagement.DataManageResourceGroup;
import com.huigou.data.jdbc.util.ParseSQLParam;
import com.huigou.data.query.model.SQLModel;
import com.huigou.data.query.parser.model.ConditionModel;
import com.huigou.data.query.parser.model.DataFilterGroup;
import com.huigou.data.query.parser.model.DataFilterRule;
import com.huigou.data.query.parser.model.PermissionGroup;
import com.huigou.data.query.parser.model.PermissionModel;
import com.huigou.exception.ApplicationException;
import com.huigou.exception.ExpressExecuteException;
import com.huigou.exception.ResourceLoadException;
import com.huigou.express.ExpressManager;
import com.huigou.express.VariableContainer;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.ListUtil;
import com.huigou.util.LogHome;
import com.huigou.util.Md5Builder;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

/**
 * SQL执行对象
 * 
 * @author xx
 * @date 2017-1-26 上午09:39:34
 * @version V1.0
 */

public class SQLExecutor extends SQLModel {
    private QueryPermissionBuilder permissionBuilder;

    public void setPermissionBuilder(QueryPermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    /**
     * 参数默认类型
     */
    private static String DEFAULT_TYPE = "java.lang.String";

    /**
     * 无权限SQL
     */
    private static String NO_PERMISSION = "1=2";

    private String manageType;

    /**
     * sql 片段集合
     */
    private List<String> sqlList;

    /**
     * 参数名称
     */
    private List<String> paramNames;

    /**
     * 参数类别
     */
    private Map<String, String> paramTypesMap;

    /**
     * IN 参数名称
     */
    private List<String> conditionIn;

    /**
     * LIKE 参数名称
     */
    private List<String> conditionLike;

    /**
     * Half LIKE 参数名称
     */
    private List<String> conditionHalfLike;

    public SQLExecutor() {
        sqlList = new ArrayList<String>();
        paramNames = new ArrayList<String>();
        paramTypesMap = new HashMap<String, String>();
        conditionIn = new ArrayList<String>();
        conditionLike = new ArrayList<String>();
        conditionHalfLike = new ArrayList<String>();
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public Operator getOperator() {
        return ThreadLocalUtil.getOperator();
    }

    public void addSqlList(String sql) {
        if (sql != null && !sql.equals("")) {
            sqlList.add(sql);
        }
    }

    public void addConditionLike(String name) {
        if (name != null) {
            conditionLike.add(name);
        }
    }

    public void addConditionIn(String name) {
        if (name != null) {
            conditionIn.add(name);
        }
    }

    public void addConditionHalfLike(String name) {
        if (name != null) {
            conditionHalfLike.add(name);
        }
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void putParamTypes(String type, String name) {
        this.paramTypesMap.put(name, type);
    }

    public void addParam(String name) {
        paramNames.add(name);
    }

    /**
     * 得到参数数组
     * 
     * @param map
     * @return
     * @throws ApplicationException
     */
    public Object[] getParams(Map<String, Object> map) throws ApplicationException {
        int length = paramNames.size();
        List<Object> values = new ArrayList<Object>(length);
        Object obj = null;
        String type = null;
        for (int i = 0; i < length; i++) {
            obj = map.get(paramNames.get(i));
            if (obj != null && !obj.toString().equals("")) {
                type = StringUtil.tryThese(this.paramTypesMap.get(paramNames.get(i)), DEFAULT_TYPE);
                values.add(ClassHelper.convert(obj, type));
            } else {
                values.add(null);
            }
        }
        return values.toArray();
    }

    /**
     * 得到Map参数
     * 
     * @param map
     * @return
     * @throws ApplicationException
     */
    public Map<String, Object> parseParamMap() {
        Map<String, Object> map = this.getQueryParams();
        int length = paramNames.size();
        Map<String, Object> values = new HashMap<String, Object>(length);
        Object obj = null;
        String name = null;
        String type = null;
        for (int i = 0; i < length; i++) {
            name = paramNames.get(i);
            obj = map.get(name);
            if (obj != null && !obj.toString().equals("")) {
                type = StringUtil.tryThese(this.paramTypesMap.get(name), DEFAULT_TYPE);
                if (isLike(name)) {// 是like查询默认添加%%
                    obj = ClassHelper.convert(obj, String.class);
                    obj = "%" + obj + "%";
                }
                if (isHalfLike(name)) {// 是half like查询默认添加%
                    obj = ClassHelper.convert(obj, String.class);
                    obj = obj + "%";
                }
                if (isIn(name)) {// 是in查询默认添加()
                    StringBuffer inSql = new StringBuffer("(");
                    String[] vals = obj.toString().split(",");
                    int vi = vals.length;
                    for (int j = 0; j < vi; j++) {
                        inSql.append(":").append(name).append(j);
                        if (j < vi - 1) {
                            inSql.append(",");
                        }
                        values.put(name + j, ClassHelper.convert(vals[j], type));
                    }
                    inSql.append(")");
                    String executeSql = this.getSql();
                    // 修改sql \\b 单词分界符避免匹配到其他条件
                    executeSql = executeSql.replaceAll(":" + name + "\\b", inSql.toString());
                    this.setSql(executeSql);
                } else {
                    obj = ClassHelper.convert(obj, type);
                    values.put(name, obj);
                }
            } else {
                values.put(name, null);
            }
        }
        return values;
    }

    /**
     * 判断是否是LIKE查询
     * 
     * @param name
     * @return
     */
    private boolean isLike(String name) {
        if (conditionLike != null && conditionLike.size() > 0) {
            return ListUtil.contains(conditionLike, name);
        }
        return false;
    }

    /**
     * 判断是否是HALF LIKE查询
     * 
     * @param name
     * @return
     */
    private boolean isHalfLike(String name) {
        if (conditionHalfLike != null && conditionHalfLike.size() > 0) {
            return ListUtil.contains(conditionHalfLike, name);
        }
        return false;
    }

    /**
     * 判断是否是in查询
     * 
     * @param name
     * @return
     */
    private boolean isIn(String name) {
        if (conditionIn != null && conditionIn.size() > 0) {
            return ListUtil.contains(conditionIn, name);
        }
        return false;
    }

    /**
     * 构建查询sql
     */
    public void buildSql() {
        // 使用空格组装sql
        String sql = ListUtil.join(this.sqlList, " ");
        this.setSql(sql);
        // 解析出动态参数
        ParseSQLParam parser = new ParseSQLParam();
        parser.parse(sql);
        List<String> names = parser.getParameter();
        this.setParamNames(names);
    }

    /**
     * 判断 param中是否有值
     * 
     * @param name
     * @return
     */
    private boolean hasParamValue(String name) {
        Object obj = this.getParam(name);
        if (obj != null && !obj.toString().equals("")) {// 条件的值不为null
            return true;
        }
        return false;
    }

    /**
     * 处理条件表达式
     * 
     * @param symbol
     * @param name
     */
    private void formatSymbol(String symbol, String name) {
        // 处理 like 和 in 操作符的数据
        if (symbol.equalsIgnoreCase("LIKE")) {
            this.addConditionLike(name);
        }
        if (symbol.equalsIgnoreCase("HALF_LIKE")) {
            this.addConditionHalfLike(name);
        }
        if (symbol.equalsIgnoreCase("IN")) {
            this.addConditionIn(name);
        }
    }

    /**
     * 解析条件sql
     * 
     * @param condition
     */
    public void buildSqlCondition(ConditionModel condition) {
        paramTypesMap.put(condition.getName(), condition.getType());
        if (this.hasParamValue(condition.getName())) {// 参数中有值
            // 解析符号
            this.formatSymbol(condition.getSymbol(), condition.getName());
            // 判读是否追加
            if (condition.isAppendCondition()) {
                this.addSqlList("and");// 固定组合为 and 条件
                this.addSqlList(condition.formatCondition());
            }
        }
    }

    /**
     * 解析条件 高级自定义查询
     * 
     * @param dataFilterGroup
     */
    public void buildSqlDataFilterGroup(DataFilterGroup dataFilterGroup) {
        String sql = this.translateDataFilterGroup(dataFilterGroup);
        if (StringUtil.isNotBlank(sql)) {
            // 固定组合为 and 条件
            this.addSqlList("and");
            // 组合权限条件SQL
            this.addSqlList(sql);
        }
    }

    /**
     * 递归解析高级自定义查询分组
     * 
     * @param group
     * @return
     */
    private String translateDataFilterGroup(DataFilterGroup group) {
        List<String> conditions = new ArrayList<String>();
        if (group == null) return " 1=1 ";
        boolean appended = false;
        List<DataFilterRule> rules = group.getRules();
        List<DataFilterGroup> groups = group.getGroups();
        conditions.add("(");
        if (rules != null && rules.size() > 0) {
            for (DataFilterRule rule : rules) {
                if (appended) {
                    conditions.add(group.getOp());
                }
                // 继续规则
                conditions.add(this.translateDataFilterRule(rule));
                appended = true;
            }
        }
        if (groups != null && groups.size() > 0) {
            for (DataFilterGroup subgroup : groups) {
                if (appended) {
                    conditions.add(group.getOp());
                }
                // 递归解析分组
                conditions.add(this.translateDataFilterGroup(subgroup));
                appended = true;
            }
        }
        conditions.add(")");
        if (appended == false) {
            return " 1=1 ";
        }
        return ListUtil.join(conditions, "");
    }

    /**
     * 解析高级自定义查询条件
     * 
     * @param rule
     * @return
     */
    private String translateDataFilterRule(DataFilterRule rule) {
        if (rule == null) {
            return "1=1";
        }
        String value = ClassHelper.getProperty(rule, rule.getUsed());
        if (StringUtil.isBlank(value)) {
            return "1=1";
        }
        String column = rule.getColumn();
        if (StringUtil.isBlank(column)) {
            return "1=1";
        }
        // 避免name重复设置属性名
        String paramName = Md5Builder.getMd5(String.format("%s%s%s", rule.getName(), value, System.currentTimeMillis()));
        rule.setParamName(paramName);
        if (rule.isLike()) {
            this.putParam(paramName, "%" + value + "%");
        } else if (rule.isStartwith()) {
            this.putParam(paramName, value + "%");
        } else if (rule.isEqual()) {
            this.putParam(paramName, "%" + value);
        } else {
            this.putParam(paramName, value);
        }
        if (rule.isIn()) {
            this.formatSymbol(rule.getFormatSymbol(), rule.getParamName());
        }
        this.putParamTypes(rule.getType(), rule.getParamName());
        return rule.formatCondition();
    }

    /**
     * 解析权限控制sql
     * 
     * @param group
     */
    public void buildSqlPermissions(PermissionGroup group) {
        if (this.getOperator() == null) {
            return;
        }
        // 根节点默认组合为一个权限组
        PermissionGroup newGroup = new PermissionGroup();
        newGroup.addPermissionGroup(group);
        // 构建权限组
        this.buildPermissionGroups(newGroup);
        String permissionSql = this.buildSqlPermissionGroup(newGroup);
        if (StringUtil.isNotBlank(permissionSql)) {
            // 固定组合为 and 条件
            this.addSqlList("and");
            // 组合权限条件SQL
            this.addSqlList(permissionSql);
        }
    }

    /**
     * 按数据管理权限编码重新构建权限组
     * 
     * @param group
     */
    private void buildPermissionGroups(PermissionGroup group) {
        List<PermissionGroup> groups = group.getPermissionGroups();
        List<PermissionGroup> newGroupList = new ArrayList<>(groups.size());
        String businessCode = null;
        String manageCode = null;
        for (PermissionGroup childGroup : groups) {
            businessCode = childGroup.getBusinessCode();
            manageCode = childGroup.getManageCode();
            if (StringUtil.isNotBlank(businessCode)) {// 存在业务类型的解析
                PermissionGroup newGroup = PermissionGroup.newInstance(childGroup);
                // 根据业务类型编码查询是否存在权限字段定义
                DataManageFieldsGroup dataManageFieldsGroup = this.permissionBuilder.findDataManageFieldsByCode(businessCode);
                // 数据管理权限特殊组织权限
                if (dataManageFieldsGroup.hasOrgModels()) {
                    newGroup.addPermissionModels(dataManageFieldsGroup.getOrgModels());
                }
                newGroupList.add(newGroup);
                // 存在数据管理权限
                if (dataManageFieldsGroup.hasDataModels()) {
                    PermissionGroup dataGroup = new PermissionGroup();
                    dataGroup.setManageCode(dataManageFieldsGroup.getManageCode());
                    dataGroup.addPermissionModels(dataManageFieldsGroup.getDataModels());
                    newGroupList.addAll(this.buildPermissionGroupsAsDataManagement(dataGroup));
                }
            } else if (StringUtil.isNotBlank(manageCode)) {// 存在数据管理权限的解析
                newGroupList.addAll(this.buildPermissionGroupsAsDataManagement(childGroup));
            } else {
                newGroupList.add(childGroup);
            }
        }
        // 递归组合权限组
        for (PermissionGroup newGroup : newGroupList) {
            this.buildPermissionGroups(newGroup);
        }
        group.setPermissionGroups(newGroupList);
    }

    /**
     * 解析数据管理权限分组
     * 
     * @param group
     * @return
     */
    private List<PermissionGroup> buildPermissionGroupsAsDataManagement(PermissionGroup group) {
        String manageCode = group.getManageCode();
        List<PermissionGroup> groupList = new ArrayList<>();
        // 存在数据管理权限编码，通过接口获取数据
        List<DataManageResourceGroup> resourceGroups = this.permissionBuilder.findDataManagementByCode(manageCode, this.getOperator().getUserId());
        if (resourceGroups != null && resourceGroups.size() > 0) {
            for (DataManageResourceGroup resourceGroup : resourceGroups) {
                PermissionGroup newGroup = PermissionGroup.newInstance(group);
                newGroup.setResourceGroup(resourceGroup);
                groupList.add(newGroup);
            }
        } else {
            groupList.add(group);
        }
        return groupList;
    }

    /**
     * 按权限组组合条件SQL
     * 
     * @param group
     * @return
     */
    private String buildSqlPermissionGroup(PermissionGroup group) {
        // 判断是否存在数据权限
        if (group.isNoPermissionGroup()) {
            return NO_PERMISSION;
        }
        List<String> conditions = new ArrayList<>();
        List<PermissionModel> models = group.getPermissionModels();
        // 按权限项组合SQL
        conditions.addAll(this.buildPermissionSql(models, group.getResourceGroup()));
        for (PermissionGroup childGroup : group.getPermissionGroups()) {
            // 递归组合权限组SQL
            String condition = this.buildSqlPermissionGroup(childGroup);
            if (StringUtil.isNotBlank(condition)) {
                conditions.add(condition);
            }
        }
        if (conditions.size() == 0) {
            return "";
        } else if (conditions.size() == 1) {
            return conditions.get(0);
        } else {
            StringBuffer sb = new StringBuffer("(");
            sb.append(ListUtil.join(conditions, " " + group.getOperator() + " "));// 条件间使用 or 关联
            sb.append(")");
            return sb.toString();
        }
    }

    /**
     * 按具体权限栏目组合SQL
     * 
     * @param models
     * @param resourceGroup
     * @return
     */
    private List<String> buildPermissionSql(List<PermissionModel> models, DataManageResourceGroup resourceGroup) {
        int permissionLenght = models.size();
        List<String> conditions = new ArrayList<String>(permissionLenght);
        // 权限项
        for (PermissionModel permission : models) {
            if (permission.getColumn() == null) {
                throw new ResourceLoadException(String.format("权限[column]定义不能为空!"));
            }
            // 校验类别是否存在
            if (permission.getKind() == null) {
                throw new ResourceLoadException(String.format("权限[%s]未指定类别Kind!", permission.getColumn()));
            }
            PermissionKind permissionKind = permission.getPermissionKind();
            if (permissionKind == null) {
                throw new ResourceLoadException(String.format("权限[%s]错误的权限类别[%s]!", permission.getColumn(), permission.getKind()));
            }
            // name 为空 默认使用kind_id为name
            if (StringUtil.isBlank(permission.getName())) {
                permission.setName(permissionKind.toString());
            }
            switch (permissionKind) {
            case PERSON_ID:
            case PSM_ID:
            case DEPT_ID:
            case ORGAN_ID:
                if (permission.isAppendCondition()) {
                    conditions.add(permission.formatCondition());
                }
                try {
                    // 从当前登录用户中获取对应参数
                    this.putParam(permission.getName(), ClassHelper.getProperty(this.getOperator(), permissionKind.getPropertyName()));
                } catch (Exception e) {
                    throw new ResourceLoadException(String.format("权限解析[%s]取值错误:%s", permission.getName(), e.getMessage()));
                }
                break;
            case FULL_ID:
                conditions.add(this.parseFullIdManageType(permission, permissionLenght));
                break;
            case DEFINE:
                conditions.add(this.parseDefineFunction(permission));
                break;
            case DATA:
                conditions.add(this.parseDataManagement(permission, resourceGroup));
                break;
            default:
                throw new ResourceLoadException(String.format("[%s]错误的数据权限类型", permission.getName()));
            }
            this.formatSymbol(permission.getSymbol(), permission.getParamName());
            this.putParamTypes(permission.getType(), permission.getParamName());
        }
        return ListUtil.distinct(conditions);
    }

    /**
     * 解析通过业务管理权限组合fullId的查询SQL
     * 
     * @param permission
     * @param conditions
     * @param conditionLenght
     */
    private String parseFullIdManageType(PermissionModel permission, final int permissionLenght) {
        List<String> conditions = new ArrayList<>();
        String manage = StringUtil.tryThese(permission.getManageType(), this.getManageType());
        if (StringUtil.isBlank(manage)) {// 处理动态获取manageType
            String expr = permission.getExpr();
            if (StringUtil.isNotBlank(expr)) {
                Object result = this.evaluateExpr(expr);
                if (result != null) {
                    manage = ClassHelper.convert(result, String.class);
                }
            }
        }
        if (StringUtil.isNotBlank(manage)) {
            // 通过管理权限拼接sql
            List<OrgUnit> list = permissionBuilder.findSubordinations(this.getOperator().getUserId(), this.getOperator().getPersonMemberFullIds(), manage);
            if (list != null && list.size() > 0) {
                int length = list.size();
                OrgUnit orgUnit;
                for (int i = 0; i < length; i++) {
                    orgUnit = list.get(i);
                    conditions.add(permission.formatCondition("like", permission.getName() + i));
                    this.putParam(permission.getName() + i, orgUnit.getFullId() + "%");
                }
            } else {
                // 只有fullId一个权限时获取当前登录用户fullId组合到sql中
                if (permissionLenght == 1) {
                    List<String> personMemberFullIds = this.getOperator().getPersonMemberFullIds();
                    int length = personMemberFullIds.size();
                    for (int i = 0; i < length; i++) {
                        String paraName = String.format("fullId%d", i);
                        conditions.add(permission.formatCondition("like", paraName));
                        this.putParam(paraName, personMemberFullIds.get(i) + "%");
                    }
                }
            }
        }
        return this.joinConditionsAsOr(conditions);
    }

    /**
     * 解析自定义函数 组合SQL
     * 
     * @param permission
     * @param conditions
     * @param conditionLenght
     */
    @SuppressWarnings("unchecked")
    private String parseDefineFunction(PermissionModel permission) {
        List<String> conditions = new ArrayList<>();
        // 取自定义公式并执行
        String expr = permission.getExpr();
        if (StringUtil.isBlank(expr)) {
            throw new ResourceLoadException(String.format("[%s]错误的取值公式[空]", permission.getName()));
        }
        // 执行自定义公式
        Object result = this.evaluateExpr(expr);
        // 返回数据为空 不能查询出数据
        if (result != null) {
            if (result.getClass().isArray()) {
                Object[] objs = (Object[]) result;
                if (objs.length > 0) {
                    if (permission.isLike()) {
                        int length = objs.length;
                        String obj = null;
                        for (int i = 0; i < length; i++) {
                            obj = ClassHelper.convert(objs[i], String.class, "");
                            this.putParam(permission.getName() + i, (permission.isHalfLike() ? "" : "%") + obj + "%");
                            conditions.add(permission.formatCondition("like", permission.getName() + i));
                        }
                        permission.setAppend("false");
                    } else {
                        this.putParam(permission.getName(), ListUtil.join(objs, ","));
                    }
                }
            } else if (ClassHelper.isInterface(result.getClass(), List.class)) {
                List<?> collection = (List<?>) result;
                if (collection.size() > 0) {
                    if (permission.isLike()) {
                        int i = 0;
                        String obj = null;
                        for (Object o : collection) {
                            obj = ClassHelper.convert(o, String.class, "");
                            this.putParam(permission.getName() + i, (permission.isHalfLike() ? "" : "%") + obj + "%");
                            conditions.add(permission.formatCondition("like", permission.getName() + i));
                            i++;
                        }
                        permission.setAppend("false");
                    } else {
                        this.putParam(permission.getName(), ListUtil.join((List<?>) result, ","));
                    }
                }
            } else if (ClassHelper.isInterface(result.getClass(), Map.class)) {
                Map<String, Object> m = (Map<String, Object>) result;
                if (m.size() > 0) {
                    this.putAll(m);
                }
            } else {
                this.putParam(permission.getName(), result);
            }
        }
        if (permission.isAppendCondition()) {
            conditions.add(permission.formatCondition());
        }
        return this.joinConditionsAsOr(conditions);
    }

    /**
     * 执行表达式并进行类型转换
     * 
     * @param expr
     * @return
     */
    private Object evaluateExpr(String expr) {
        String param, value = "";
        Map<String, Object> params = this.getQueryParams();
        try {
            // 函数执行替换时 解析正则表达式
            Pattern evaluateExprPattern = Pattern.compile("@(.+?)[,|)]");
            // 添加参数到执行环节
            VariableContainer.putVariableMap(params);
            VariableContainer.addVariable(Constants.MANAGE_TYPE, manageType);
            Matcher m = evaluateExprPattern.matcher(expr);
            while (m.find()) {
                param = m.group(1).trim();
                if (params != null) {
                    value = ClassHelper.convert(params.get(param), String.class);
                }
                Util.check(!StringUtil.isBlank(value), String.format("没有找到参数“%s”对应的值。", param));
                expr = expr.replace("@" + m.group(1), String.format("'%s'", value));
            }
            // 执行表示式
            return ExpressManager.evaluate(expr);
        } catch (Exception e) {
            LogHome.getLog(this).error("evaluateExpr:" + expr, e);
            throw new ExpressExecuteException(String.format("函数执行[%s]错误:%s", expr, e.getMessage()));
        }
    }

    /**
     * 数据管理权限解析
     * 
     * @param permission
     * @param conditions
     * @param permissionLenght
     */
    private String parseDataManagement(PermissionModel permission, DataManageResourceGroup resourceGroup) {
        if (resourceGroup == null) {
            return NO_PERMISSION;
        }
        String key = permission.getName();
        if (StringUtil.isBlank(key)) {
            throw new ResourceLoadException("数据管理权限name不能为空!");
        }
        List<String> conditions = new ArrayList<>();
        String paramName = Md5Builder.getMd5(key + resourceGroup.getDataManagedetalId());
        permission.setParamName(paramName);
        List<DataManageResource> resources = resourceGroup.getResourcesByCode(key);
        if (resources != null && resources.size() > 0) {
            if (permission.isLike()) {
                int i = 0;
                String value = null;
                for (DataManageResource resource : resources) {
                    value = resource.getFullId();
                    if (StringUtil.isBlank(value)) {
                        value = resource.getKey();
                    }
                    this.putParam(paramName + i, (permission.isHalfLike() ? "" : "%") + value + "%");
                    conditions.add(permission.formatCondition("like", paramName + i));
                    i++;
                }
                permission.setAppend("false");
            } else if (permission.isEqual()) {
                int i = 0;
                for (DataManageResource resource : resources) {
                    this.putParam(paramName + i, resource.getKey());
                    conditions.add(permission.formatCondition("=", paramName + i));
                    i++;
                }
                permission.setAppend("false");
            } else {
                List<String> keys = new ArrayList<>(resources.size());
                for (DataManageResource resource : resources) {
                    keys.add(resource.getKey());
                }
                this.putParam(paramName, ListUtil.join(keys, ","));
            }
            if (permission.isAppendCondition()) {
                conditions.add(permission.formatCondition());
            }
        }
        return this.joinConditionsAsOr(conditions);
    }

    /**
     * 使用OR组合条件
     * 
     * @param conditions
     * @return
     */
    private String joinConditionsAsOr(List<String> conditions) {
        if (conditions.size() == 0) {
            return NO_PERMISSION;
        } else if (conditions.size() == 1) {
            return conditions.get(0);
        } else {
            StringBuffer sb = new StringBuffer("(");
            sb.append(ListUtil.join(conditions, " or "));// 条件间使用 or 关联
            sb.append(")");
            return sb.toString();
        }
    }
}
