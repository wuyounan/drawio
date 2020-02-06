package com.huigou.report.cubesviewer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.huigou.report.cubesviewer.domain.model.Cut;
import com.huigou.report.cubesviewer.domain.model.DateFilter;
import com.huigou.report.cubesviewer.domain.model.DimensionFilter;

@Service("cubesViewerParameter")
public class CubesViewerParameterParser {

    private static final String CUTS_KEY = "cuts";

    private static final String DATE_FILTERS_KEY = "datefilters";

    private static final String REGEX_FORMATTER = "\"%s\"\\s*:\\s*\\[(.*?)\\]\\s*,";

    private JSONArray builFilterJsonArray(String filter, String definitionJson) {
        String regex = String.format(REGEX_FORMATTER, filter);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(definitionJson);

        if (matcher.find()) {
            String cutsString = matcher.group(1);
            return JSONArray.fromObject(String.format("[%s]", cutsString));
        }
        return null;
    }

    /**
     * 解析条目参数
     * 
     * @param entry
     *            条目 cuts or datefilters
     * @param clazz
     *            类型
     * @param definitionJson
     *            定义JSON
     * @return
     */
    private String parseEntryFilterParamter(String entry, Class<?> clazz, String definitionJson) {
        JSONArray filterArray = builFilterJsonArray(entry, definitionJson);
        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject;
        String regex;
        Object object;
        DimensionFilter dimensionFilter;
        if (filterArray != null) {
            regex = String.format(REGEX_FORMATTER, entry);
            for (Object item : filterArray) {
                jsonObject = (JSONObject) item;
                object = JSONObject.toBean(jsonObject, clazz);
                dimensionFilter = (DimensionFilter) object;
                dimensionFilter.parseCustomParameter();
                sb.append(dimensionFilter.render());
                sb.append(",");
            }
            if (sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
            definitionJson = definitionJson.replaceAll(regex, String.format("\"%s\":[%s],", entry, sb.toString()));
        }
        return definitionJson;
    }

    /**
     * 解析过滤参数
     * 
     * @param definitionJson
     *            定义JSON
     * @return
     */
    public String parseFilterParamter(String definitionJson) {
        String result = parseEntryFilterParamter(CUTS_KEY, Cut.class, definitionJson);
        result = parseEntryFilterParamter(DATE_FILTERS_KEY, DateFilter.class, result);
        return result;
    }
}
