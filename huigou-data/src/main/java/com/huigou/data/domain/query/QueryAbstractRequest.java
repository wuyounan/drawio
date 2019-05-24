package com.huigou.data.domain.query;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.huigou.context.Operator;
import com.huigou.data.query.model.QueryModel;
import com.huigou.data.query.parser.model.DataFilterGroup;
import com.huigou.domain.QueryRequest;
import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.JSONUtil;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * 查询抽象模型
 * 
 * @author xx
 */

public abstract class QueryAbstractRequest implements QueryRequest {
    /**
     * 分页排序信息
     */
    private QueryPageRequest pageModel;

    private Operator operator;

    /**
     * 管理权限
     */
    private String sys_manage_type;

    /**
     * 数据查询条件生成 高级自定义查询
     */
    private String data_filter_group;

    private String queryKeyValue;

    private DataFilterGroup dataFilterGroupObject;

    /**
     * 初始化页面查询参数
     * 
     * @param params
     */
    public void initPageModel(Map<String, Object> params) {
        QueryPageRequest model = new QueryPageRequest();
        model.initPageInfo(params);
        this.pageModel = model;
        // 业务管理权限
        String manageType = ClassHelper.convert(params.get(Constants.MANAGE_TYPE), String.class);
        if (!StringUtil.isBlank(manageType)) {
            this.setSys_manage_type(manageType);
        }
        // 高级自定义查询
        String dataFilter = ClassHelper.convert(params.get(Constants.DATA_FILTER), String.class);
        if (!StringUtil.isBlank(dataFilter)) {
            this.setData_filter_group(dataFilter);
        }
    }

    /**
     * 初始化查询模型
     * 
     * @return
     */
    public QueryModel initQueryModel() {
        QueryModel queryModel = new QueryModel();
        if (this.pageModel != null) {
            ClassHelper.copyProperties(this.pageModel, queryModel);
        }
        queryModel.setManageType(this.getSys_manage_type());
        return queryModel;
    }

    public void setPageModel(QueryPageRequest pageModel) {
        this.pageModel = pageModel;
    }

    public QueryPageRequest getPageModel() {
        return this.pageModel;
    }

    public String getSys_manage_type() {
        return sys_manage_type;
    }

    public void setSys_manage_type(String sys_manage_type) {
        this.sys_manage_type = sys_manage_type;
    }

    public String getData_filter_group() {
        return data_filter_group;
    }

    public void setData_filter_group(String data_filter_group) {
        this.data_filter_group = data_filter_group;
    }

    public String getQueryKeyValue() {
        return queryKeyValue;
    }

    public void setQueryKeyValue(String queryKeyValue) {
        this.queryKeyValue = queryKeyValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setDataFilter(DataFilterGroup dataFilterGroup) {
        this.dataFilterGroupObject = dataFilterGroup;
    }

    /**
     * 数据查询条件生成 高级自定义查询
     * 
     * @return
     */
    public DataFilterGroup getDataFilterGroup() {
        if (dataFilterGroupObject != null) {
            return dataFilterGroupObject;
        }
        if (StringUtil.isBlank(this.getData_filter_group())) {
            return null;
        }
        try {
            DataFilterGroup group = JSONUtil.toBean(this.getData_filter_group(), DataFilterGroup.class);
            if (group.isAppended()) {
                return group;
            }
        } catch (Exception e) {
            LogHome.getLog(this).error(e);
        }
        return null;
    }

    /**
     * 转换为参数map
     * 
     * @return
     */
    public Map<String, Object> toParamMap() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class") && !propertyName.equals("sys_manage_type") && !propertyName.equals("data_filter_group")
                    && !propertyName.equals("dataFilterGroup")) {
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod != null) {
                        Object result;
                        result = readMethod.invoke(this, new Object[0]);
                        if (result != null && ClassHelper.isBaseType(result.getClass())) {
                            returnMap.put(propertyName, result);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
            throw new ApplicationException("对象转换Map错误：" + e.getMessage());
        }
        return returnMap;
    }

    protected void checkConstraints() {
    }

}
