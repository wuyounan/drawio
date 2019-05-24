package com.huigou.uasp.client.views;

import com.huigou.cache.SystemCache;
import com.huigou.util.Constants;
import com.huigou.util.StringUtil;

/**
 * Link CSS 引入标签
 * 
 * @author xx
 */
public class LinkImportTag extends AbstractTag {

    private static final long serialVersionUID = -1774319277355515274L;

    private String href;

    private String rel;

    private String type;

    public LinkImportTag() {
        super();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected String getDefaultTemplate() {
        return "linkImport";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (StringUtil.isBlank(href)) {
            return;
        }
        String prefix = Constants.WEB_APP;
        StringBuffer linkUrl = new StringBuffer();
        if (href.startsWith(prefix)) {
            linkUrl.append(href);
        } else {
            linkUrl.append(prefix);
            if (!href.startsWith("/")) {
                linkUrl.append("/");
            }
            linkUrl.append(href);
        }
        String needVersion = StringUtil.tryThese(SystemCache.getParameter("LINK.IMPORT.VERSION", String.class), "true");
        if (needVersion.equalsIgnoreCase("true")) {
            if (href.indexOf("?") > -1) {
                linkUrl.append("&");
            } else {
                linkUrl.append("?");
            }
            linkUrl.append("ver=").append(SystemCache.getStartTime());
        }
        addParameter("href", linkUrl.toString());
        addParameter("rel", rel);
        addParameter("type", type);
    }

}
