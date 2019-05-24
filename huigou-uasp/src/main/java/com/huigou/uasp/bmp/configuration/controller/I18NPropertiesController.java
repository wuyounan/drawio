package com.huigou.uasp.bmp.configuration.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.i18n.model.I18nInitResourceInterface;
import com.huigou.exception.ApplicationException;
import com.huigou.properties.PropertiesModel;
import com.huigou.properties.PropertiesReader;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.annotation.SkipAuth;
import com.huigou.uasp.bmp.configuration.application.I18npropertiesApplication;
import com.huigou.uasp.bmp.configuration.domain.model.I18nproperties;
import com.huigou.uasp.bmp.configuration.domain.query.I18npropertiesQueryRequest;
import com.huigou.uasp.bmp.configuration.model.I18nDefaultConfig;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.JSONUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 国际化配置文件读取返回
 * 存在业务功能引用不能添加权限
 * 
 * @author xx
 */
@Controller
@ControllerMapping("i18nProperties")
public class I18NPropertiesController extends CommonController {
    private static final String PATH = "i18n/%s.properties";

    @Autowired
    private PropertiesReader propertiesReader;

    @Autowired
    private I18npropertiesApplication i18npropertiesApplication;

    @Autowired
    private I18nDefaultConfig i18nDefaultConfig;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    /**
     * 读取资源文件
     * 
     * @param name
     * @return
     * @throws ApplicationException
     */
    private Map<String, String> getProperties(String name) throws ApplicationException {
        if (StringUtil.isBlank(name)) {
            return new HashMap<String, String>();
        }
        Locale locale = MessageSourceContext.getLocale();
        String[] names = name.split(",");
        Map<String, String> map = new HashMap<String, String>();
        // 组合文件顺序加载列表
        List<String> fileNames = new ArrayList<String>();
        for (String n : names) {
            fileNames.add(String.format("%s_%s", n, "zh"));// 中文默认加载
            fileNames.add(String.format("%s_%s", n, locale.getLanguage()));
            fileNames.add(String.format("%s_%s_%s", n, locale.getLanguage(), locale.getCountry()));
        }
        // 读取配置文件
        for (String fileName : fileNames) {
            try {
                PropertiesModel model = propertiesReader.loadConfigFile(String.format(PATH, fileName));
                map.putAll(model.toMap());
            } catch (Exception e) {
                // 读取文集错误这里不处理 可能文件不存在
            }
        }
        return map;
    }

    /**
     * 读取数据字典配置到页面
     * 
     * @param name
     * @return
     * @throws ApplicationException
     */
    private Map<String, String> getDictionary(String dictionary) throws ApplicationException {
        if (StringUtil.isBlank(dictionary)) {
            return new HashMap<String, String>();
        }
        String[] dictionarys = dictionary.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String n : dictionarys) {
            List<Map<String, Object>> l = DictUtil.getDictionaryList(n);
            if (l != null && l.size() > 0) {
                for (Map<String, Object> m : l) {
                    map.put(m.get("i18NKey").toString(), m.get("name").toString());
                }
            }
        }
        return map;
    }

    /**
     * 页面调用 使用jsonp的方式返回数据
     * 
     * @return
     * @throws Exception
     */
    @SkipAuth
    @ControllerMethodMapping("/i18n")
    public String execute() throws Exception {
        SDO sdo = this.getSDO();
        String name = sdo.getString("name");
        String dictionary = sdo.getString("dictionary");
        if (StringUtil.isBlank(name)) {
            name = i18nDefaultConfig.getDefaultName();
            dictionary = i18nDefaultConfig.getDefaultDictionary();
        }
        Map<String, String> propertiesMap = this.getProperties(name);
        Map<String, String> dictionaryMap = this.getDictionary(dictionary);
        propertiesMap.putAll(dictionaryMap);
        // 使用jsonp的方式返回数据
        String callback = sdo.getProperty("callback", String.class, "i18nParseData");
        String jsonStr = JSONUtil.toString(propertiesMap);
        String outMsg = String.format("%s(%s)", callback, jsonStr);
        HttpServletResponse response = this.getResponse();
        response.setContentType("application/javascript;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().write(outMsg);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NONE;
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到国际化资源维护列表页面")
    public String forward() {
        this.putAttribute("i18nLanguageList", DictUtil.getDictionaryList("i18nLanguage"));
        return forward("i18nPropertiesList");
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到国际化资源维护明细页面")
    public String showInsertI18nproperties() {
        this.putAttribute("i18nLanguageList", DictUtil.getDictionaryList("i18nLanguage"));
        return forward("i18nPropertiesDetail");
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加国际化资源维护")
    public String insertI18nproperties() {
        SDO params = this.getSDO();
        I18nproperties i18nproperties = params.toObject(I18nproperties.class);
        String id = i18npropertiesApplication.saveI18nproperties(i18nproperties);
        return success(id);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改国际化资源维护页面")
    public String showUpdateI18nproperties() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        I18nproperties i18nproperties = i18npropertiesApplication.loadI18nproperties(id);
        this.putAttribute("i18nLanguageList", DictUtil.getDictionaryList("i18nLanguage"));
        return forward("i18nPropertiesDetail", i18nproperties);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改国际化资源")
    public String updateI18nproperties() {
        SDO params = this.getSDO();
        I18nproperties i18nproperties = params.toObject(I18nproperties.class);
        i18npropertiesApplication.saveI18nproperties(i18nproperties);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除国际化资源")
    public String deleteI18nproperties() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        i18npropertiesApplication.deleteI18nproperties(ids);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询国际化资源")
    public String slicedQueryI18nproperties() {
        SDO params = this.getSDO();
        I18npropertiesQueryRequest queryRequest = params.toQueryRequest(I18npropertiesQueryRequest.class);
        Map<String, Object> data = i18npropertiesApplication.slicedQueryI18nproperties(queryRequest);
        return this.toResult(data);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到初始化国际化资源维护页面")
    public String showInitI18nproperties() {
        return forward("i18nPropertiesInit");
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.INIT, description = "初始化国际化资源")
    public String initI18nproperties() {
        SDO params = this.getSDO();
        String folderId = params.getString("folderId");
        String resourcekind = params.getString("resourcekind");
        i18npropertiesApplication.initI18nproperties(folderId, resourcekind);
        return this.toResult("");
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动国际化资源")
    public String moveI18nProperties() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getProperty(FOLDER_ID_KEY_NAME, String.class);
        i18npropertiesApplication.moveI18nProperties(ids, folderId);
        return success();
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.CACHE, description = "同步国际化资源")
    public String syncCacheI18nproperties() {
        i18npropertiesApplication.syncCache();
        return success(MessageSourceContext.getMessage(MessageConstants.DATA_OPERATION_SUCCESS));
    }

    public String showUpdateDialog() {
        SDO params = this.getSDO();
        this.putAttribute("i18nLanguageList", DictUtil.getDictionaryList("i18nLanguage"));
        String resourcekind = params.getString("resourceKind");
        Assert.hasText(resourcekind, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "resourcekind"));
        I18nInitResourceInterface resourceInterface = ApplicationContextWrapper.getBean(resourcekind, I18nInitResourceInterface.class);
        Assert.notNull(resourceInterface, String.format("错误的类型%s!", resourcekind));
        return forward("I18nPropertiesUtil", params);
    }

}
