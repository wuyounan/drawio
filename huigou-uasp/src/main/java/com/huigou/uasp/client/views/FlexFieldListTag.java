package com.huigou.uasp.client.views;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huigou.uasp.bmp.flexfield.application.FlexFieldApplication;
import com.huigou.util.ClassHelper;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

public class FlexFieldListTag extends AbstractTag {

    private static final long serialVersionUID = 997546468050998731L;

    private String bizId;

    private String bizCode;

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public FlexFieldListTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "FlexField";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (StringUtil.isBlank(bizCode) || StringUtil.isBlank(bizId)) {
            return;
        }
        HttpServletRequest request = this.getRequest();
        String id = ClassHelper.convert(request.getParameter(bizId), String.class);
        if (id == null) {
            id = ClassHelper.convert(findValue(bizId), String.class);
            if (StringUtil.isBlank(id)) {
                id = bizId;
            }
        }
        if (id == null) {
            return;
        }
        /***** 获取页面编码 *****/
        String code = ClassHelper.convert(request.getParameter(bizCode), String.class);
        if (code == null) {
            code = findValue(bizCode) != null ? ClassHelper.convert(findValue(bizCode), String.class) : bizCode;
        }
        List<Map<String, Object>> group = queryFields(id, code);
        if (null != group && group.size() > 0) {
            addParameter("tagFlexFieldGroupList", group);
        }
    }

    private List<Map<String, Object>> queryFields(String id, String code) {
        FlexFieldApplication application = SpringBeanFactory.getBean(this.getServletContext(), "flexFieldApplication", FlexFieldApplication.class);
        List<Map<String, Object>> group = application.queryFlexFieldBizGroupFieldStorage(code, id);
        return group;
    }
}