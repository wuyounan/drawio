package com.huigou.uasp.client.views;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.huigou.context.MessageSourceContext;
import com.huigou.util.StringUtil;

/**
 * 国际化数据后台读取标签
 * 
 * @author xxin
 */
public class MessageTag extends AbstractTag {

    private static final long serialVersionUID = -8713242019004759494L;

    protected String key;

    protected String args;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public int doStartTag() throws JspException {
        String code = StringUtil.tryThese(args, "");
        List<String> values = new ArrayList<>();
        if (StringUtil.isNotBlank(code)) {
            String[] cs = code.split(",");
            for (String v : cs) {
                values.add(this.findValue(v, String.class, v));
            }
        }
        JspWriter jspwriter = pageContext.getOut();
        try {
            String content = MessageSourceContext.getMessage(key, values.toArray());
            jspwriter.print(content);
        } catch (Exception ioexception) {
            ioexception.printStackTrace();
            throw new JspException(ioexception);
        }
        return SKIP_BODY;
    }

    @Override
    protected String getDefaultTemplate() {
        return null;
    }

}
