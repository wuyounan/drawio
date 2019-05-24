package com.huigou.uasp.client.views;

import com.huigou.cache.SystemCache;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * JavaScript 引入标签
 * 
 * @author xx
 */
public class ScriptImportTag extends AbstractTag {

    private static final long serialVersionUID = -1774319277355515274L;

    private String src;

    private String type;

    public ScriptImportTag() {
        super();
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected String getDefaultTemplate() {
        return "scriptImport";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (StringUtil.isBlank(src)) {
            return;
        }
        String prefix = Constants.WEB_APP;
        StringBuffer linkUrl = new StringBuffer();
        if (src.startsWith(prefix)) {
            linkUrl.append(src);
        } else {
            linkUrl.append(prefix);
            if (!src.startsWith("/")) {
                linkUrl.append("/");
            }
            linkUrl.append(src);
        }
        String needVersion = StringUtil.tryThese(SystemCache.getParameter("SCRIPT.IMPORT.VERSION", String.class), "true");
        if (needVersion.equalsIgnoreCase("true")) {
            if (src.indexOf("?") > -1) {
                linkUrl.append("&");
            } else {
                linkUrl.append("?");
            }
            linkUrl.append("ver=").append(SystemCache.getStartTime());
        }
        addParameter("src", linkUrl.toString());
        addParameter("type", type);
    }

}
