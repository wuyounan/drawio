package com.huigou.uasp.bmp.dataManage.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.cache.service.ICache;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.datamanagement.DataManageResourceGroup;
import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.data.query.SQLExecutor;
import com.huigou.data.query.parser.model.PermissionGroup;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.dataManage.application.LoadDataManageApplication;
import com.huigou.uasp.bmp.operator.OperatorApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * 数据管理权限查询
 * 
 * @ClassName: LoadDataManageApplicationImpl
 * @author xx
 * @version V1.0
 */
@Service("loadDataManageApplication")
public class LoadDataManageApplicationImpl extends BaseApplication implements LoadDataManageApplication {
    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private OperatorApplication operatorApplication;

    @Autowired
    private QueryPermissionBuilder queryPermissionBuilder;

    @Override
    public List<DataManageResourceGroup> findDataManagement(String dataCode, String personId) {
        return queryPermissionBuilder.findDataManagementByCode(dataCode, personId);
    }

    @Override
    public List<DataManageResourceGroup> findDataManagementByPersonCode(String dataCode, String personCode) {
        Person person = this.orgApplication.loadPersonByLoginName(personCode.toUpperCase());
        if (person == null) {
            throw new UnknownAccountException();
        }
        return this.findDataManagement(dataCode, person.getId());
    }

    @Override
    public List<OrgUnit> findSubordinations(String manageType, String personId) {
        List<Org> personMemberEntities = this.orgApplication.queryPersonMembersByPersonId(personId);
        List<String> fullIds = new ArrayList<String>(personMemberEntities.size());
        for (Org item : personMemberEntities) {
            fullIds.add(item.getFullId());
        }
        return queryPermissionBuilder.findSubordinations(personId, fullIds, manageType);
    }

    @Override
    public List<OrgUnit> findSubordinationsByPersonCode(String manageType, String personCode) {
        Person person = this.orgApplication.loadPersonByLoginName(personCode.toUpperCase());
        if (person == null) {
            throw new UnknownAccountException();
        }
        return this.findSubordinations(manageType, person.getId());
    }

    /**
     * 根据权限编码解析权限SQL
     * 
     * @param dataCode
     * @return
     */
    private String findDataPermissionSql(String businessCode) {
        SQLExecutor executor = new SQLExecutor();
        executor.setPermissionBuilder(queryPermissionBuilder);
        PermissionGroup permissionGroup = new PermissionGroup();
        permissionGroup.setBusinessCode(businessCode);
        executor.buildSqlPermissions(permissionGroup);
        // 构建查询sql 及条件组合
        executor.buildSql();
        // 参数解析
        Map<String, Object> map = executor.parseParamMap();
        return this.sqlExecutorDao.getParseSqlByMapParam(executor.getSql(), map);
    }

    /**
     * 创建并缓存用户信息
     * 
     * @param keyVlaue
     * @param isId
     * @return
     */
    private Operator getOperatorAsCache(String keyVlaue, boolean isId) {
        if (StringUtil.isBlank(keyVlaue)) {
            return null;
        }
        Operator operator = null;
        ICache icache = this.queryPermissionBuilder.getCache();
        String cacheKey = ThreadLocalUtil.OPERATOR_KEY + "|" + keyVlaue;
        Object element = null;
        try {
            element = icache.get(cacheKey);
            if (element == null) {
                if (isId) {
                    operator = operatorApplication.createOperatorByPersonId(keyVlaue);
                } else {
                    operator = operatorApplication.createOperatorByLoginName(keyVlaue);
                }
                icache.put(cacheKey, operator);
            } else {
                operator = (Operator) element;
            }
            return operator;
        } catch (Exception e) {
            LogHome.getLog(this).error("创建Operator", e);
        }
        return null;
    }

    @Override
    public String findDataPermissionSqlByPersonCode(String businessCode, String personCode) {
        // 创建登录用户对象
        Operator operator = this.getOperatorAsCache(personCode, false);
        if (operator == null) {
            return "and 1=2";
        }
        ThreadLocalUtil.putOperator(operator);
        return findDataPermissionSql(businessCode);
    }

    @Override
    public String findDataPermissionSql(String businessCode, String personId) {
        // 创建登录用户对象
        Operator operator = this.getOperatorAsCache(personId, true);
        if (operator == null) {
            return "and 1=2";
        }
        ThreadLocalUtil.putOperator(operator);
        return findDataPermissionSql(businessCode);
    }

}
