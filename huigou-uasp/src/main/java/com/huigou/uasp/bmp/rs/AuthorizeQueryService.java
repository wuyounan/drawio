package com.huigou.uasp.bmp.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.util.JSONUtil;

/**
 * 功能授权查询
 * 
 * @author xx
 */
@Path("/authorize")
@Service("authorizeQueryService")
@Produces({ MediaType.APPLICATION_JSON })
public class AuthorizeQueryService {
    @Autowired
    private AccessApplicationProxy accessApplication;

    @GET
    @Path("/ui/{personId}/{functionId}/")
    @Description(value = "根据人员ID及功能ID查询界面权限资源", target = DocTarget.METHOD)
    public String queryUIPermissionsById(@PathParam("personId") String personId, @PathParam("functionId") String functionId) {
        List<Map<String, Object>> data = null;
        if (personId == null || functionId == null) {
            data = new ArrayList<>();
        } else {
            data = accessApplication.queryUIElementPermissionsByFunction(functionId, personId, true);
        }
        return JSONUtil.toString(data);
    }

    @GET
    @Path("/ui/code/{personId}/{functionCode}/")
    @Description(value = "根据人员ID及功能编码查询界面权限资源", target = DocTarget.METHOD)
    public String queryUIPermissionsByCode(@PathParam("personId") String personId, @PathParam("functionCode") String functionCode) {
        List<Map<String, Object>> data = null;
        if (personId == null || functionCode == null) {
            data = new ArrayList<>();
        } else {
            data = accessApplication.queryUIElementPermissionsByFunction(functionCode, personId, false);
        }
        return JSONUtil.toString(data);
    }

    @GET
    @Path("/fun/{personId}/{functionId}/")
    @Description(value = "根据人员ID及功能ID查询下级功能", target = DocTarget.METHOD)
    public String loadPersonFunPermissions(@PathParam("personId") String personId, @PathParam("functionId") String functionId) {
        List<Map<String, Object>> data = null;
        if (personId == null || functionId == null) {
            data = new ArrayList<>();
        } else {
            data = accessApplication.queryPersonFunctions(personId, functionId);
        }
        return JSONUtil.toString(data);
    }

    @GET
    @Path("/fun/all/{personId}/{functionId}/")
    @Description(value = "根据人员ID及功能ID查询下级全部功能", target = DocTarget.METHOD)
    public String loadPersonAllFunPermissions(@PathParam("personId") String personId, @PathParam("functionId") String functionId) {
        List<Map<String, Object>> data = null;
        if (personId == null || functionId == null) {
            data = new ArrayList<>();
        } else {
            data = accessApplication.queryPersonAllFunctions(personId, functionId);
        }
        return JSONUtil.toString(data);
    }

}