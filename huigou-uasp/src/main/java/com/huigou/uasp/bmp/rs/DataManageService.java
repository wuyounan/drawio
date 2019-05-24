package com.huigou.uasp.bmp.rs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.context.OrgUnit;
import com.huigou.data.datamanagement.DataManageResourceGroup;
import com.huigou.uasp.bmp.dataManage.application.LoadDataManageApplication;
import com.huigou.util.JSONUtil;

/**
 * 数据管理权限查询服务
 * 
 * @author xx
 */
@Path("/management")
@Service("dataManageService")
@Produces({ MediaType.APPLICATION_JSON })
public class DataManageService {
    @Autowired
    private LoadDataManageApplication loadDataManageApplication;

    @GET
    @Path("/data/{personId}/{code}/")
    @Description(value = "根据人员ID及数据管理权限编码查询权限资源", target = DocTarget.METHOD)
    public String loadDataManage(@PathParam("personId") String personId, @PathParam("code") String code) {
        List<DataManageResourceGroup> groups = loadDataManageApplication.findDataManagement(code, personId);
        return JSONUtil.toString(groups);
    }

    @GET
    @Path("/data/personcode/{personCode}/{code}/")
    @Description(value = "根据人员登录账号及数据管理权限编码查询权限资源", target = DocTarget.METHOD)
    public String loadDataManageByPersonCode(@PathParam("personCode") String personCode, @PathParam("code") String code) {
        List<DataManageResourceGroup> groups = loadDataManageApplication.findDataManagementByPersonCode(code, personCode);
        return JSONUtil.toString(groups);
    }

    @GET
    @Path("/biz/{personId}/{code}/")
    @Description(value = "根据人员ID及业务管理权限编码查询管理的组织", target = DocTarget.METHOD)
    public String loadBizManagement(@PathParam("personId") String personId, @PathParam("code") String code) {
        List<OrgUnit> orgUnits = loadDataManageApplication.findSubordinations(code, personId);
        return JSONUtil.toString(orgUnits);
    }

    @GET
    @Path("/biz/personcode/{personCode}/{code}/")
    @Description(value = "根据人员登录账号及业务管理权限编码查询管理的组织", target = DocTarget.METHOD)
    public String loadBizManagementByPersonCode(@PathParam("personCode") String personCode, @PathParam("code") String code) {
        List<OrgUnit> orgUnits = loadDataManageApplication.findSubordinationsByPersonCode(code, personCode);
        return JSONUtil.toString(orgUnits);
    }

    @GET
    @Path("/sql/{personId}/{code}/")
    @Description(value = "根据权限类别编码及人员ID组合数据管理权限控制SQL", target = DocTarget.METHOD)
    @Produces({ MediaType.TEXT_PLAIN })
    public String loadDataPermissionSql(@PathParam("personId") String personId, @PathParam("code") String code) {
        return loadDataManageApplication.findDataPermissionSql(code, personId);
    }

    @GET
    @Path("/sql/personcode/{personCode}/{code}/")
    @Description(value = "根据权限类别编码及人员编号组合数据管理权限控制SQL", target = DocTarget.METHOD)
    @Produces({ MediaType.TEXT_PLAIN })
    public String loadDataPermissionSqlByPersonCode(@PathParam("personCode") String personCode, @PathParam("code") String code) {
        return loadDataManageApplication.findDataPermissionSqlByPersonCode(code, personCode);
    }
}