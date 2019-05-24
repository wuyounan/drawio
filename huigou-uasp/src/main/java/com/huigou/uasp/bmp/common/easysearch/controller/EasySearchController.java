package com.huigou.uasp.bmp.common.easysearch.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.cache.DictionaryDesc;
import com.huigou.cache.SystemCache;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bmp.common.easysearch.application.EasySearchApplication;
import com.huigou.uasp.bmp.common.easysearch.domain.EasySearchLoadInterface;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchMappingModel;
import com.huigou.uasp.bmp.common.easysearch.domain.model.EasySearchParse;
import com.huigou.uasp.bmp.common.easysearch.domain.model.QuerySchemeModel;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.ClassHelper;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

/**
 * 快捷查询
 * 
 * @author xx
 */
@Controller
@ControllerMapping("easySearch")
public class EasySearchController extends CommonController {

    private static final String EASY_SEARCH_DATA = "EASY_SEARCH_DATA";

    private static final String SYSTEM_ENUM_DATA_DICTIONARY = "sysEnumData";

    private static final String GET_ENUM_DATA_FUNCTION_NAME = "getData";

    @Autowired
    private EasySearchLoadInterface easySearchManager;

    private EasySearchParse getQuerySchemeModel(SDO sdo) throws ApplicationException {
        EasySearchParse dto = null;
        String configType = sdo.getProperty("configType", String.class);
        String queryName = sdo.getProperty("queryName", String.class);
        EasySearchMappingModel model = easySearchManager.loadConfigFile(configType);
        if (null != model) {
            QuerySchemeModel querySchemeModel = model.getQuerySchemeModel(queryName);
            if (querySchemeModel == null) {
                throw new ApplicationException("未找到名字为" + queryName + "的查询配置。");
            }
            dto = new EasySearchParse();
            ClassHelper.copyProperties(querySchemeModel, dto);
            dto.setSdo(sdo);
        } else {
            throw new ApplicationException("未找到类别为" + configType + "的查询配置。");
        }
        return dto;
    }

    private EasySearchApplication getEasySearchApplication(String serviceName) {
        if (StringUtil.isBlank(serviceName)) {
            serviceName = "easySearchApplication";
        }
        return SpringBeanFactory.getBean(this.getServletContext(), serviceName, EasySearchApplication.class);
    }

    @ControllerMethodMapping("/easySearch")
    public String execute() throws Exception {
        SDO sdo = this.getSDO();
        try {
            EasySearchParse dto = getQuerySchemeModel(sdo);
            EasySearchApplication application = this.getEasySearchApplication(dto.getServiceName());
            dto.setPageSize(sdo.getProperty("pageSize", Integer.class));
            dto.setIntPage(sdo.getProperty("intPage", Integer.class));
            dto.setParamValue(sdo.getProperty("paramValue", String.class));
            application.search(dto, sdo);// 执行查询
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("intPage", dto.getIntPage());
            map.put("sumPage", dto.getSumPage());
            map.put("count", dto.getCount());
            map.put("headLength", dto.getHeadLength());
            map.put("width", dto.getWidth());
            map.put("headList", dto.getFields());
            map.put("datas", dto.getData());
            this.putAttribute(EASY_SEARCH_DATA, map);
            this.putAttribute("isMultipleSelect", sdo.getProperty("isMultipleSelect", Boolean.class));
        } catch (Exception e) {
            this.putAttribute("message", e.getMessage());
            LogHome.getLog(this).error("快捷查询错误;", e);
            return forward("/common/error.jsp");
        }
        return forward("/common/easySearch.jsp");
    }

    public String getComboDialogConfig() {
        SDO sdo = this.getSDO();
        try {
            EasySearchParse dto = getQuerySchemeModel(sdo);
            sdo.putProperty("kindId", dto.getFolderKindId());
            sdo.putProperty("columns", dto.getFields());
            return toResult(sdo);
        } catch (Exception e) {
            return error("选择对话框" + e.getMessage());
        }
    }

    /**
     * 快捷查询对话框
     * 
     * @return
     */
    public String comboGridSearch() {
        SDO sdo = this.getSDO();
        try {
            EasySearchParse dto = getQuerySchemeModel(sdo);
            EasySearchApplication application = this.getEasySearchApplication(dto.getServiceName());
            dto.setParamValue(sdo.getProperty("paramValue", String.class));
            dto.setFolderId(sdo.getProperty("folderId", String.class));
            Map<String, Object> map = application.comboGridSearch(dto, sdo);
            return toResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            return error("选择对话框" + e.getMessage());
        }
    }

    /**
     * 数据字典查询
     * 
     * @return
     */
    public String dictionary() {
        SDO sdo = this.getSDO();
        String dictionary = sdo.getProperty("dictionary", String.class);
        String filter = sdo.getProperty("filter", String.class);
        Map<String, String> map = DictUtil.getDictionary(dictionary, filter);
        if (map == null || map.size() == 0) {
            return error("未找到对应数据字典值!");
        }
        return toResult(map);
    }

    @ControllerMethodMapping(value = { "/easySearch/dictionaryForEnum", "/enumData" })
    public String dictionaryForEnum() {
        SDO sdo = this.getSDO();
        String enumKind = sdo.getProperty("enumKind", String.class);
        Map<String, DictionaryDesc> map = SystemCache.getDictionary(SYSTEM_ENUM_DATA_DICTIONARY);
        if (map == null || map.size() == 0) {
            return error("未找到对应数据字典值!");
        }
        DictionaryDesc desc = map.get(enumKind);
        Assert.notNull(desc, String.format("未找到枚举“%s”对应的设置。", enumKind));
        String className = desc.getName();
        String methodName = StringUtil.tryThese(desc.getTypeId(), GET_ENUM_DATA_FUNCTION_NAME);
        try {
            Class<?> clazz = Class.forName(className.trim());
            // Assert.state(ListEnumData.class.isAssignableFrom(clazz), "无效的枚举类, 未实现ListEnumData接口。");
            Method method = clazz.getMethod(methodName);
            toResult(method.invoke(null));
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }

        return toResult(map);
    }
}
