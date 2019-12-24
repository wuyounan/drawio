package com.huigou.data.query;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.cache.service.ICache;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.datamanagement.DataManageChooseOrgKind;
import com.huigou.data.datamanagement.DataManageFieldsGroup;
import com.huigou.data.datamanagement.DataManageResource;
import com.huigou.data.datamanagement.DataManageResourceGroup;
import com.huigou.data.datamanagement.DataResourceKind;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.SQLModel;
import com.huigou.data.query.parser.model.PermissionModel;
import com.huigou.exception.ApplicationException;
import com.huigou.exception.ResourceLoadException;
import com.huigou.express.ExpressManager;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.LogHome;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;

@Service("queryPermissionBuilder")
public class QueryPermissionBuilderImpl implements QueryPermissionBuilder {

    private static final String ORG_FUN_CLASS_NAME = "com.huigou.uasp.bmp.fn.impl.OrgFun";

    private static final String FIND_SUBORDINATIONS_FOR_NEAREST_MANAGER_METHOD_NAME = "findSubordinationsForNearestManager";

    private static final String FIND_SUBORDINATIONS_BY_ORG_MANAGE_TYPE_METHOD_NAME = "findSubordinationsByOrgManageType";

    @Resource(name = "permissionCache")
    private ICache icache;

    @Autowired
    protected SQLExecutorDao sqlExecutorDao;

    private Object orgFun;

    private Method findSubordinationsByOrgManageTypeMethod;

    private Method findSubordinationsForNearestManagerMethod;

    public ICache getCache() {
        return icache;
    }

    private synchronized void initFindSubordinationsByOrgManageType() {
        if (this.findSubordinationsByOrgManageTypeMethod == null) {
            try {
                Class<?> clazz = Class.forName(ORG_FUN_CLASS_NAME);
                // 不带参数的方法调用
                orgFun = clazz.newInstance();
                Method method = clazz.getMethod("setSQLQuery", SQLQuery.class);
                method.invoke(orgFun, this.sqlExecutorDao.getSqlQuery());

                this.findSubordinationsByOrgManageTypeMethod = clazz.getMethod(FIND_SUBORDINATIONS_BY_ORG_MANAGE_TYPE_METHOD_NAME, Object.class, String.class);
                this.findSubordinationsForNearestManagerMethod = clazz.getMethod(FIND_SUBORDINATIONS_FOR_NEAREST_MANAGER_METHOD_NAME, Object.class,
                                                                                 String.class, Object.class, Boolean.class);

            } catch (Exception e) {
                throw new ApplicationException("初始化orgFun出错。");
            }
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<OrgUnit> findSubordinations(String personId, List<String> fullIds, String manageType) {
        // manageType admin@inOrg=aaa,includeAllParent=false,isPersonMember=true
        if (StringUtil.isBlank(personId)) {
            return null;
        }
        List<OrgUnit> list = null;
        String cacheKey = Constants.PERMISSION_MANAGE_TYPE + "|" + personId + "|" + manageType;
        Object element = null;
        try {
            element = icache.get(cacheKey);
            if (element == null) {
                initFindSubordinationsByOrgManageType();

                ManageTypeParameter manageTypeParameter = new ManageTypeParameter();
                manageTypeParameter.parse(manageType);
                if (manageTypeParameter.getIncludeAllParent()) {
                    list = (List<OrgUnit>) this.findSubordinationsByOrgManageTypeMethod.invoke(this.orgFun, fullIds, manageTypeParameter.getManageType());
                } else {
                    list = (List<OrgUnit>) this.findSubordinationsForNearestManagerMethod.invoke(orgFun, fullIds, manageTypeParameter.getManageType(),
                                                                                                 manageTypeParameter.getInOrg(),
                                                                                                 manageTypeParameter.getIsPersonMember());
                }

                icache.put(cacheKey, (Serializable) list);
            } else {
                list = (List<OrgUnit>) element;
            }
            return list;
        } catch (Exception e) {
            LogHome.getLog(this).error("获取数据权限", e);
        }
        return null;
    }

    @Override
    public SQLModel applyManagementPermission(String sql, String manageType) {
        SQLModel sqlModel = new SQLModel();
        Operator operator = ThreadLocalUtil.getOperator();
        if (operator == null || StringUtil.isBlank(operator.getFullId())) {
            sqlModel.setSql(sql);
            return sqlModel;
        }

        if (manageType.equals(Constants.NO_CONTROL_AUTHORITY)) {
            sqlModel.setSql(sql);
            return sqlModel;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("select * from (").append(sql).append(")");
        sb.append(" where 1=1 ");
        if (StringUtil.hasField(sql, "full_id")) {
            List<OrgUnit> list = findSubordinations(operator.getUserId(), operator.getPersonMemberFullIds(), manageType);

            if (list != null && list.size() > 0) {
                int length = list.size();
                Map<String, Object> map = new HashMap<String, Object>(length + 2);
                sb.append(" and (");
                if (StringUtil.hasField(sql, "person_member_id")) {// 默认自己数据权限
                    sb.append(" person_member_id like :authorityPersonId");
                    map.put("authorityPersonId", operator.getUserId() + "%");
                    sb.append(" or ");
                } else if (StringUtil.hasField(sql, "person_id")) {// 默认自己数据权限
                    sb.append(" person_id = :authorityPersonId");
                    map.put("authorityPersonId", operator.getUserId());
                    sb.append(" or ");
                }
                OrgUnit orgUnit;
                for (int i = 0; i < length; i++) {
                    orgUnit = list.get(i);
                    sb.append(" full_id like :authorityFullId").append(i);
                    map.put("authorityFullId" + i, orgUnit.getFullId() + "%");
                    if (i < length - 1) {
                        sb.append(" or ");
                    }
                }
                sb.append(")");
                sqlModel.setQueryParams(map);
            } else {
                Map<String, Object> map = new HashMap<String, Object>(3);
                sb.append(" and ");
                if (StringUtil.hasField(sql, "person_member_id")) {// 默认自己数据权限
                    sb.append(" person_member_id like :authorityPersonId");
                    map.put("authorityPersonId", operator.getUserId() + "%");
                } else if (StringUtil.hasField(sql, "person_id")) {// 默认自己数据权限
                    sb.append(" person_id = :authorityPersonId");
                    map.put("authorityPersonId", operator.getUserId());
                } else {
                    sb.append(" full_id like :authorityFullId");
                    map.put("authorityFullId", "%" + operator.getUserId() + "%");
                }
                sqlModel.setQueryParams(map);
            }
        }
        sqlModel.setSql(sb.toString());
        return sqlModel;
    }

    @Override
    public SQLModel applyManagementPermissionForTree(String sql, String manageType) {
        SQLModel sqlModel = new SQLModel();
        Operator operator = ThreadLocalUtil.getOperator();
        if (operator == null || StringUtil.isBlank(operator.getFullId())) {
            sqlModel.setSql(sql);
            return sqlModel;
        }
        if (manageType.equals(Constants.NO_CONTROL_AUTHORITY)) {
            sqlModel.setSql(sql);
            return sqlModel;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select * from (").append(sql).append(") t_t_ ");
        sb.append(" where 1=1 ");
        List<OrgUnit> list = findSubordinations(operator.getUserId(), operator.getPersonMemberFullIds(), manageType);
        if (StringUtil.hasField(sql, "full_id")) {
            if (null != list && list.size() > 0) {
                int length = list.size();
                Map<String, Object> map = new HashMap<>(length);
                sb.append(" and (");
                for (int i = 0; i < length; i++) {
                    OrgUnit ou = list.get(i);
                    sb.append(" full_id like :authorityFullId").append(i);
                    sb.append(" or ");
                    sb.append(":authorityFullId").append(i).append(" like concat(full_id, '%')");
                    map.put("authorityFullId" + i, ou.getFullId() + "%");
                    if (i < length - 1) {
                        sb.append(" or ");
                    }
                }
                sb.append(")");
                sqlModel.setQueryParams(map);
            } else {
                sb.append(" and (");
                sb.append(" full_id like :authorityPersonFullId");
                sb.append(" or ");
                sb.append(":authorityPersonFullId").append(" like concat(full_id, '%')");
                sb.append(")");
                Map<String, Object> map = new HashMap<String, Object>(1);
                map.put("authorityPersonFullId", operator.getFullId());
                sqlModel.setQueryParams(map);
            }
        }
        sqlModel.setSql(sb.toString());
        return sqlModel;
    }

    @Override
    public boolean hasManagementPermission(String manageType, String fullId) {
        Assert.hasText(manageType, "参数manageType不能为空。");
        Assert.hasText(fullId, "参数fullId不能为空。");

        if (manageType.equals(Constants.NO_CONTROL_AUTHORITY)) {
            return true;
        }

        Operator operator = ThreadLocalUtil.getOperator();
        if (operator == null) {
            return false;
        }

        List<OrgUnit> list = findSubordinations(operator.getUserId(), operator.getPersonMemberFullIds(), manageType);
        if (list != null && list.size() > 0) {
            for (OrgUnit orgUnit : list) {
                if (fullId.indexOf(orgUnit.getFullId()) != -1) {
                    return true;
                }
            }
        }
        // 控制自己数据权限
        if (fullId.indexOf("/" + operator.getUserId() + "@") != -1) {
            return true;
        }
        return false;
    }

    @Override
    public void removeCache() {
        icache.removeAll();
    }

    @Override
    public void removeCacheByKind(String kind) {
        icache.remove(kind, null);
    }

    class ManageTypeParameter {

        private final String FIND_SUBORDINATIONS_PARAM_KEY = "System.ManageType.FindSubordinations.Params";

        private String manageType;

        private Object inOrg;

        private Boolean includeAllParent = true;

        private Boolean isPersonMember = false;

        public void parse(String inputManageType) {
            Assert.hasText(inputManageType, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "inputManageType"));
            String splits[] = inputManageType.split(StringPool.AT);
            this.manageType = splits[0];

            String manageTypeParam = splits.length > 1 ? splits[1] : "";
            String key, value;
            String[] keyValuePair;

            String systemSetting = com.huigou.cache.SystemCache.getParameter(FIND_SUBORDINATIONS_PARAM_KEY, String.class);

            if (StringUtil.isNotBlank(systemSetting)) {
                if (StringUtil.isBlank(manageTypeParam)) {
                    manageTypeParam = systemSetting;
                } else {
                    manageTypeParam = manageTypeParam + "," + systemSetting;
                }
            }

            if (StringUtil.isNotBlank(manageTypeParam)) {
                String[] manageTypeParamSplits = manageTypeParam.split(StringPool.COMMA);
                Map<String, Object> params = new HashMap<String, Object>(manageTypeParamSplits.length);
                for (String item : manageTypeParamSplits) {
                    keyValuePair = item.split(StringPool.EQUAL);
                    key = keyValuePair[0];
                    value = keyValuePair[1];
                    params.put(key, value);
                }
                if (params.containsKey("inOrg")) {
                    this.inOrg = ClassHelper.convert(params.get("inOrg"), String.class, inOrg);
                }
                if (params.containsKey("includeAllParent")) {
                    this.includeAllParent = ClassHelper.convert(params.get("includeAllParent"), Boolean.class, includeAllParent);
                }
                if (params.containsKey("isPersonMember")) {
                    this.isPersonMember = ClassHelper.convert(params.get("isPersonMember"), Boolean.class, this.isPersonMember);
                }
            }

        }

        public String getManageType() {
            return manageType;
        }

        public Object getInOrg() {
            return inOrg;
        }

        public Boolean getIncludeAllParent() {
            return includeAllParent;
        }

        public Boolean getIsPersonMember() {
            return isPersonMember;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DataManageResourceGroup> findDataManagementByCode(String code, String personId) {
        if (StringUtil.isBlank(personId) || StringUtil.isBlank(code)) {
            return null;
        }
        List<DataManageResourceGroup> list = null;
        String cacheKey = Constants.DATA_PERMISSION_MANAGE + "|" + personId + "|" + code;
        Object element = null;
        try {
            element = icache.get(cacheKey);
            if (element == null) {
                list = this.doQueryDataManagement(code, personId);
                if (list == null) {
                    list = new ArrayList<>();
                }
                icache.put(cacheKey, (Serializable) list);
            } else {
                list = (List<DataManageResourceGroup>) element;
            }
            return list;
        } catch (Exception e) {
            LogHome.getLog(this).error("获取数据权限", e);
        }
        return null;
    }

    @Override
    public DataManageFieldsGroup findDataManageFieldsByCode(String code) {
        if (StringUtil.isBlank(code)) {
            return new DataManageFieldsGroup();
        }
        DataManageFieldsGroup group = null;
        String cacheKey = Constants.DATA_PERMISSION_MANAGE_FIELDS + "|" + code;
        Object element = null;
        try {
            element = icache.get(cacheKey);
            if (element == null) {
                group = this.doQueryDataManageFields(code);
                if (group == null) {
                    group = new DataManageFieldsGroup();
                }
                icache.put(cacheKey, (Serializable) group);
            } else {
                group = (DataManageFieldsGroup) element;
            }
            return group;
        } catch (Exception e) {
            LogHome.getLog(this).error("获取数据权限字段", e);
        }
        return null;
    }

    /**
     * 根据权限类别编码及人员ID查询数据权限资源
     * 
     * @param code
     * @param personId
     * @return
     */
    private List<DataManageResourceGroup> doQueryDataManagement(String code, String personId) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(DATA_MANAGEMENT_XML_FILE_PATH, "dataManagement");
        // 查询包含的数据角色ID
        String sql = queryDescriptor.getSqlByName("queryPersonDataManageDetailByCode");
        List<String> managedetalIds = this.sqlExecutorDao.queryToList(sql, String.class, code, personId);
        if (managedetalIds == null || managedetalIds.size() == 0) {
            return null;
        }
        List<DataManageResourceGroup> groups = new ArrayList<>(managedetalIds.size());
        // 查询数据角色包含的资源信息
        String sqlResource = queryDescriptor.getSqlByName("queryDataManageDetailResource");
        for (String managedetalId : managedetalIds) {
            DataManageResourceGroup group = new DataManageResourceGroup();
            group.setDataManagedetalId(managedetalId);
            List<DataManageResource> resources = this.sqlExecutorDao.queryToList(sqlResource, DataManageResource.class, managedetalId);
            if (resources != null && resources.size() > 0) {
                for (DataManageResource resource : resources) {
                    // 解析数据资源信息
                    group.putResources(resource.getDataKindCode(), this.parseDataManageResource(resource, personId));
                }
                group.distinctResources();
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * 数据解析
     * 
     * @param resource
     * @return
     */
    private List<DataManageResource> parseDataManageResource(DataManageResource resource, String personId) {
        List<DataManageResource> resources = new ArrayList<>();
        DataResourceKind dataResourceKind = DataResourceKind.findById(resource.getDataKind());
        if (dataResourceKind == DataResourceKind.ORG) {
            DataManageChooseOrgKind chooseOrgKind = DataManageChooseOrgKind.findById(resource.getOrgDataKind());
            if (chooseOrgKind == DataManageChooseOrgKind.MANAGETYPE) {// 业务管理权限
                resources.addAll(this.parseOrgManageType(resource, personId));
            } else if (chooseOrgKind == DataManageChooseOrgKind.ORGFUN) {// 自定义函数
                resources.addAll(this.parseOrgExpr(resource, personId));
            } else {
                resources.add(resource);
            }
        } else {
            resources.add(resource);
        }
        return resources;
    }

    /**
     * 解析执行组织机构取值函数
     * 
     * @param resource
     * @return
     */
    private List<DataManageResource> parseOrgExpr(DataManageResource resource, String personId) {
        String expr = resource.getKey();
        if (StringUtil.isBlank(expr)) {
            throw new ResourceLoadException(String.format("[%s]错误的取值公式[空]", resource.getDataKindCode()));
        }
        try {
            List<DataManageResource> data = new ArrayList<>();
            ExpressManager.addVariable("personId", personId);
            Object result = ExpressManager.evaluate(expr);
            if (result != null) {
                if (ClassHelper.isInterface(result.getClass(), List.class)) {
                    List<?> collection = (List<?>) result;
                    for (Object o : collection) {
                        if (o.getClass() == DataManageResource.class) {
                            data.add((DataManageResource) o);
                        }
                    }
                } else if (result.getClass() == DataManageResource.class) {
                    data.add((DataManageResource) result);
                }
            } else {
                DataManageResource dr = new DataManageResource();
                dr.setFullId("null");
                dr.setFullName("null");
                dr.setDataKindCode("null");
                dr.setDataKind("null");
                data.add(dr);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
    }

    /**
     * 解析执行业务管理权限管理组织信息
     * 
     * @param resource
     * @return
     */
    private List<DataManageResource> parseOrgManageType(DataManageResource resource, String personId) {
        String sql = "select t.full_id  from sa_oporg t where t.status = 1  and t.person_id=?";
        List<String> fullIds = this.sqlExecutorDao.queryToList(sql, String.class, personId);
        List<DataManageResource> dataResources = new ArrayList<>();
        List<OrgUnit> list = this.findSubordinations(personId, fullIds, resource.getKey());
        if (list != null && list.size() > 0) {
            for (OrgUnit orgUnit : list) {
                DataManageResource data = new DataManageResource();
                data.setKey(orgUnit.getFullId());
                data.setValue(orgUnit.getFullName());
                data.setFullId(orgUnit.getFullId());
                data.setFullName(orgUnit.getFullName());
                data.setDataKindCode(resource.getDataKindCode());
                data.setDataKind(resource.getDataKindCode());
                dataResources.add(data);
            }
        }
        return dataResources;
    }

    /**
     * 根据业务类别编码查询权限控制字段
     * 
     * @param code
     * @return
     */
    private DataManageFieldsGroup doQueryDataManageFields(String code) {
        DataManageFieldsGroup group = new DataManageFieldsGroup();
        group.setBusinessCode(code);
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(DATA_MANAGEMENT_XML_FILE_PATH, "opdatamanagebusinessField");
        // 查询使用的数据管理权限
        String sql = queryDescriptor.getSqlByName("queryManageCodeByBusinessCode");
        String manageCode = this.sqlExecutorDao.queryToString(sql, code);
        if (StringUtil.isNotBlank(manageCode)) {
            group.setManageCode(manageCode);
        }
        // 查询权限包含的字段
        sql = queryDescriptor.getSqlByName("queryFieldsByBusinessCode");
        List<Map<String, Object>> list = this.sqlExecutorDao.queryToListMap(sql, code);
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Map<String, Object> m : list) {
            Integer isOrgCondition = ClassHelper.convert(m.get("isOrgCondition"), Integer.class, 0);
            String dataKindId = ClassHelper.convert(m.get("dataKindId"), String.class);
            // 当数据管理权限字段定义不存在则不加入
            if (StringUtil.isBlank(dataKindId) && isOrgCondition == 0) {
                continue;
            }
            PermissionModel model = new PermissionModel();
            // 默认为数据管理权限需要拼接
            model.setAppend("true");
            model.setName(ClassHelper.convert(m.get("dataKindCode"), String.class));
            model.setColumn(ClassHelper.convert(m.get("tableColumn"), String.class));
            model.setAlias(ClassHelper.convert(m.get("tableAlias"), String.class));
            model.setFormula(ClassHelper.convert(m.get("formula"), String.class));
            model.setType(ClassHelper.convert(m.get("columnDataType"), String.class));
            model.setSymbol(ClassHelper.convert(m.get("columnSymbol"), String.class));
            if (isOrgCondition == 0) {
                model.setKind(PermissionKind.DATA.getId());
                group.addDataModels(model);
            } else {
                model.setKind(ClassHelper.convert(m.get("dataKind"), String.class));
                model.setManageType(ClassHelper.convert(m.get("manageType"), String.class));
                group.addOrgModels(model);
            }
        }
        return group;
    }
}
