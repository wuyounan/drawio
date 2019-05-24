package com.huigou.uasp.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.cache.DictUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.ListUtil;
import com.huigou.util.StringUtil;

/**
 * checkbox list 展现标签
 * 
 * @author xx
 */
public class InputCheckBoxListTag extends AbstractTag {

    private static final long serialVersionUID = 5462589368453050747L;

    protected String dictionary;

    protected String filter;

    protected String list;

    protected String keyFieldName;

    protected String valueFieldName;

    protected String checkedFieldName;

    private Object listObject;

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getKeyFieldName() {
        return keyFieldName;
    }

    public void setKeyFieldName(String keyFieldName) {
        this.keyFieldName = keyFieldName;
    }

    public String getValueFieldName() {
        return valueFieldName;
    }

    public void setValueFieldName(String valueFieldName) {
        this.valueFieldName = valueFieldName;
    }

    public String getCheckedFieldName() {
        return checkedFieldName;
    }

    public void setCheckedFieldName(String checkedFieldName) {
        this.checkedFieldName = checkedFieldName;
    }

    public InputCheckBoxListTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "inputCheckBoxList";
    }

    public void evaluateExtraParams() {
        if (list == null && dictionary != null) {
            listObject = DictUtil.getDictionary(dictionary, filter);
        }
        if (list != null) {
            listObject = findValue(list);
            if (listObject == null) {
                listObject = new HashMap<String, String>(1);
            }
        }
        List<Map<String, Object>> dataList = this.transformationList(listObject);
        addParameter("list", dataList);
        super.evaluateExtraParams();
    }

    /**
     * 数据源转换
     * 
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> transformationList(Object obj) {
        if (obj == null) {
            return null;
        }
        Object v = findValue(name);
        if (v == null) {
            v = value;
        }
        String nameValue = ClassHelper.convert(v, String.class, "");
        String[] vs = nameValue.split(",");
        String id = null, name = null, checkedValue = null;
        boolean checked = false;
        // 数据源为map
        if (ClassHelper.isInterface(obj.getClass(), Map.class)) {
            Map<Object, Object> m = (Map<Object, Object>) obj;
            List<Map<String, Object>> list = new ArrayList<>(m.size());
            for (Object key : m.keySet()) {
                id = key.toString();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", id);
                map.put("name", ClassHelper.convert(m.get(key), String.class, ""));
                map.put("checked", ListUtil.contains(vs, id));
                list.add(map);
            }
            return list;
        }
        String keyName = keyFieldName;
        if (StringUtil.isBlank(keyName)) {
            keyName = "id";
        }
        String valueName = valueFieldName;
        if (StringUtil.isBlank(valueName)) {
            valueName = "name";
        }
        String ckeckedName = checkedFieldName;
        if (StringUtil.isBlank(ckeckedName)) {
            ckeckedName = "checked";
        }
        // 数据源为list
        if (ClassHelper.isInterface(obj.getClass(), List.class)) {
            List<Object> l = (List<Object>) obj;
            List<Map<String, Object>> list = new ArrayList<>(l.size());
            for (Object o : l) {
                if (ClassHelper.isInterface(o.getClass(), Map.class)) {
                    Map<String, Object> m = (Map<String, Object>) o;
                    id = ClassHelper.convert(m.get(keyName), String.class, "");
                    name = ClassHelper.convert(m.get(valueName), String.class, "");
                    checkedValue = ClassHelper.convert(m.get(ckeckedName), String.class, "");
                    checked = StringUtil.isBlank(checkedValue) ? ListUtil.contains(vs, id) : (checkedValue.equals("1") || checkedValue.equals("true"));
                } else if (ClassHelper.isBaseType(o.getClass())) {
                    id = name = o.toString();
                    checked = ListUtil.contains(vs, id);
                } else {
                    id = ClassHelper.convert(ClassHelper.getFieldValue(o, keyName), String.class, "");
                    name = ClassHelper.convert(ClassHelper.getFieldValue(o, valueName), String.class, "");
                    checked = ListUtil.contains(vs, id);
                }
                if (StringUtil.isNotBlank(id) && StringUtil.isNotBlank(name)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("checked", checked);
                    list.add(map);
                }
            }
            return list;
        }
        return null;
    }
}
