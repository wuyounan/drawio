package com.huigou.uasp.bmp.rs;

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

import com.huigou.cache.DictUtil;
import com.huigou.cache.SystemCache;
import com.huigou.uasp.bmp.fn.CodeGenerator;
import com.huigou.util.JSONUtil;

/**
 * 系统配置查询
 * 
 * @author xx
 */
@Path("/configuration")
@Service("configurationService")
@Produces({ MediaType.APPLICATION_JSON })
public class ConfigurationService {
    @Autowired
    private CodeGenerator codeGenerator;

    @GET
    @Path("/dictionary/{code}/")
    @Description(value = "根据编码获取数据字典数据", target = DocTarget.METHOD)
    public String loadDictionary(@PathParam("code") String code) {
        Map<String, String> map = DictUtil.getDictionary(code);
        return JSONUtil.toString(map);
    }

    @GET
    @Path("/parameter/{code}/")
    @Description(value = "根据编码获取系统参数", target = DocTarget.METHOD)
    @Produces({ MediaType.TEXT_PLAIN })
    public String loadParameter(@PathParam("code") String code) {
        return SystemCache.getParameter(code, String.class);
    }

    @GET
    @Path("/billcode/{code}/")
    @Description(value = "根据编码获取系统参数", target = DocTarget.METHOD)
    @Produces({ MediaType.TEXT_PLAIN })
    public String loadBillCode(@PathParam("code") String code) {
        return codeGenerator.getNextCode(code);
    }

}