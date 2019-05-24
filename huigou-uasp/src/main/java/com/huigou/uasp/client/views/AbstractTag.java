package com.huigou.uasp.client.views;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.huigou.context.MessageSourceContext;
import com.huigou.freemarker.FreemarkerUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * JSP 标签基类
 * 
 * @author xxin
 */
public abstract class AbstractTag extends TagSupport {

    private static final long serialVersionUID = 6894059715256442733L;

    protected String id;

    protected String property;

    protected String cssClass;

    protected String cssStyle;

    protected String disabled;

    protected String label;

    protected String name;

    protected String value;

    protected String title;

    protected String required;

    protected String maxlength;

    protected String maxLength;

    protected String readonly;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getRequired() {
        return required;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    private Map<String, Object> parameters = null;

    public Map<String, Object> getParameters() {
        return parameters;
    }

    protected AbstractTag() {

    }

    /**
     * 模板
     * 
     * @return
     */
    protected abstract String getDefaultTemplate();

    /**
     * 组合模板路径
     * 
     * @return
     */
    protected String getTemplate() {
        String template = this.getDefaultTemplate();
        if (!template.endsWith(".ftl")) {
            template = String.format("/simple/%s.ftl", template);
        }
        return template;
    }

    /**
     * 获取执行参数
     */
    protected void evaluateExtraParams() {
        addParameter("id", StringUtil.tryThese(id, name));
        if (null != name) {
            addParameter("name", name);
        }
        if (null == name && property != null) {
            addParameter("name", property);
        }
        if (null != cssClass) {
            addParameter("cssClass", cssClass);
        }
        if (null != cssStyle) {
            addParameter("cssStyle", cssStyle);
        }
        if (null != value && name != null) {
            Object dbValue = this.findValue(name);
            if (dbValue != null) {
                addParameter("value", ClassHelper.convert(dbValue, String.class));
            } else {
                addParameter("value", value);
            }
        }
        if (parameters.containsKey("value")) {
            parameters.put("nameValue", parameters.get("value"));
        } else {
            if (name != null) {
                addParameter("nameValue", findValue(name, String.class));
            } else if (property != null) {
                addParameter("nameValue", findValue(property, String.class));
            }
        }
        if (null != disabled) {
            addParameter("disabled", this.isTrue(disabled));
        }
        if (null != label) {
            addParameter("label", this.getMessage(label));
        }
        if (null != title) {
            addParameter("title", this.getMessage(title));
        }
        if (null != required) {
            addParameter("required", this.isTrue(required));
        }
        // 兼容 maxlength 及 maxLength
        if (null != maxlength) {
            addParameter("maxlength", maxlength);
        } else {
            if (null != maxLength) {
                addParameter("maxlength", maxLength);
            }
        }
        if (null != readonly) {
            addParameter("readonly", this.isTrue(readonly));
        }
        // 标签唯一标示
        addParameter("tagId", String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 获取上下文环境
     * 
     * @return
     */
    protected ServletContext getServletContext() {
        return this.pageContext.getServletContext();
    }

    /**
     * 获取请求
     * 
     * @return
     */
    protected HttpServletRequest getRequest() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return request;
    }

    public boolean isTrue(Object key) {
        if (key == null) {
            return false;
        }
        return ClassHelper.convert(key, Boolean.class);
    }

    /**
     * 上下文中找参数值
     * 
     * @param key
     * @param cls
     * @return
     */
    public <T> T findValue(String key, Class<T> cls) {
        Object obj = this.findValue(key);
        return ClassHelper.convert(obj, cls);
    }

    public <T> T findValue(String key, Class<T> cls, Object defaultValue) {
        Object obj = this.findValue(key);
        return ClassHelper.convert(obj, cls, defaultValue);
    }

    public Object findValue(String key) {
        // HttpServletRequest -->HttpSession-->ServletContext
        HttpServletRequest request = this.getRequest();
        Object obj = request.getAttribute(key);
        if (obj == null) {
            obj = request.getParameter(key);
        }
        if (obj == null) {
            obj = request.getSession().getAttribute(key);
        }
        if (obj == null) {
            obj = this.getServletContext().getAttribute(key);
        }
        return obj;
    }

    protected void addParameter(String key, Object obj) {
        parameters.put(key, obj);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> transformationMap(Object obj) {
        if (obj == null) {
            return null;
        }
        if (ClassHelper.isInterface(obj.getClass(), Map.class)) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            Map<Object, Object> m = (Map<Object, Object>) obj;
            for (Object key : m.keySet()) {
                map.put(key.toString(), ClassHelper.convert(m.get(key), String.class, ""));
            }
            return map;
        }
        return null;
    }

    protected String getMessage(String key, Object... args) {
        return MessageSourceContext.getMessage(key, args);
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter jspwriter = pageContext.getOut();
        parameters = new HashMap<String, Object>();
        try {
            this.evaluateExtraParams();
            String template = this.getTemplate();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("parameters", parameters);
            String content = FreemarkerUtil.generate(template, variables);
            jspwriter.print(content);
        } catch (Exception ioexception) {
            throw new JspException(ioexception);
        }
        return SKIP_BODY;
    }

}
