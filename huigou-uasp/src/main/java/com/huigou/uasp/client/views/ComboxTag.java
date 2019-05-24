package com.huigou.uasp.client.views;

import java.util.HashMap;
import java.util.Map;

import com.huigou.cache.DictUtil;
import com.huigou.util.StringUtil;

public class ComboxTag extends AbstractTag {
    // 新加入的属性
	private static final long serialVersionUID = -5694980889589915876L;

	protected String dictionary;

    protected String filter;

    protected String list;

    protected String emptyOption;

    private Object listObject;

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getEmptyOption() {
        return emptyOption;
    }

    public void setEmptyOption(String emptyOption) {
        this.emptyOption = emptyOption;
    }

    public ComboxTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "combox";
    }

    private void setListParams() {
        String dictionaryName = dictionary;
        if (StringUtil.isBlank(dictionaryName)) {
            dictionaryName = name;
        }
        if (list == null && !StringUtil.isBlank(dictionaryName)) {
            listObject = DictUtil.getDictionary(dictionaryName, filter);
        }
        if (list != null) {
            listObject = findValue(list);
            if (listObject == null) {
                listObject = new HashMap<String, String>(1);
            }
        }
        if (emptyOption == null || emptyOption.equals("true")) {
            emptyOption = "true";
        }
        if (null == cssClass) {
            addParameter("cssClass", "select");
        }
    }

    @Override
    public void evaluateExtraParams() {
        setListParams();
        super.evaluateExtraParams();
        Map<String, String> tmp = this.transformationMap(listObject);
        addParameter("list", tmp);
        addParameter("emptyOption", this.isTrue(emptyOption));
        boolean isDisabled = this.isTrue(disabled);
        if (isDisabled && tmp != null) {
            Object v = this.getParameters().get("nameValue");
            v = v != null ? tmp.get(v.toString()) : "";
            addParameter("showValue", v);
        }
    }

}
