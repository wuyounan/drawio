package com.huigou.uasp.client.views;

import java.util.HashMap;

import com.huigou.cache.DictUtil;

public class InputRadioTag extends AbstractTag {
    
	private static final long serialVersionUID = 5462589368453050747L;

	protected String dictionary;

    protected String filter;

    protected String list;

    private Object listObject;

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public void setList(String list) {
        this.list = list;
    }

    public InputRadioTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "inputRadio";
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
        addParameter("list", this.transformationMap(listObject));
        super.evaluateExtraParams();
    }
}
