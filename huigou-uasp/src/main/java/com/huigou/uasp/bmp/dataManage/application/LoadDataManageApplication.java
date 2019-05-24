package com.huigou.uasp.bmp.dataManage.application;

import java.util.List;

import com.huigou.context.OrgUnit;
import com.huigou.data.datamanagement.DataManageResourceGroup;

/**
 * 数据管理权限查询
 * 
 * @author xx
 */
public interface LoadDataManageApplication {
    /**
     * 查询文件配置地址
     */
    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/dataManagement.xml";

    /**
     * 根据权限类别编码及人员ID查询数据权限资源
     * 
     * @param dataCode
     *            数据权限编码
     * @param personId
     *            人员ID
     * @return
     */
    List<DataManageResourceGroup> findDataManagement(String dataCode, String personId);

    /**
     * 根据权限类别编码及人员编号查询数据权限资源
     * 
     * @param dataCode
     *            数据权限编码
     * @param personCode
     *            人员编号
     * @return
     */
    List<DataManageResourceGroup> findDataManagementByPersonCode(String dataCode, String personCode);

    /**
     * 查找管理的组织
     * 
     * @param manageType
     *            管理权限编码
     * @param personId
     *            人员ID
     * @return
     */
    List<OrgUnit> findSubordinations(String manageType, String personId);

    /**
     * 查找管理的组织
     * 
     * @param manageType
     *            管理权限编码
     * @param personId
     *            人员编号
     * @return
     */
    List<OrgUnit> findSubordinationsByPersonCode(String manageType, String personCode);

    /**
     * 根据权限类别编码及人员ID组合数据管理权限控制SQL
     * 
     * @param dataCode
     * @param personId
     * @return
     */
    String findDataPermissionSql(String businessCode, String personId);

    /**
     * 根据权限类别编码及人员编号组合数据管理权限控制SQL
     * 
     * @param dataCode
     * @param personCode
     * @return
     */
    String findDataPermissionSqlByPersonCode(String businessCode, String personCode);

}
