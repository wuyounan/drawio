package com.huigou.uasp.client.views;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.uasp.bmp.doc.attachment.application.AttachmentQueryApplication;
import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;
import com.huigou.uasp.bmp.doc.attachment.domain.query.AttachmentConfigurationDesc;
import com.huigou.util.ClassHelper;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

public class FileListTag extends AbstractTag {

    private static final long serialVersionUID = 1087784669323751096L;

    private String bizId;

    private String bizCode;

    private String readOnly;

    private String isWrap;

    private String isClass;

    private String inTable;

    private String springBean;

    private String batchUpload;

    private boolean isClassFlag = false;

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public void setIsWrap(String isWrap) {
        this.isWrap = isWrap;
    }

    public void setIsClass(String isClass) {
        this.isClass = isClass;
    }

    public void setInTable(String inTable) {
        this.inTable = inTable;
    }

    public void setBatchUpload(String batchUpload) {
        this.batchUpload = batchUpload;
    }

    public String getSpringBean() {
        return springBean;
    }

    public void setSpringBean(String springBean) {
        this.springBean = springBean;
    }

    public FileListTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return isClassFlag ? "fileTable" : "fileList";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        HttpServletRequest request = this.getRequest();
        // get biz id
        String id = ClassHelper.convert(request.getParameter(bizId), String.class);
        if (id == null) {
            id = ClassHelper.convert(findValue(bizId), String.class);
            if (StringUtil.isBlank(id)) {
                id = null;
            }
        }
        // get biz kind id
        String code = ClassHelper.convert(request.getParameter(bizCode), String.class);
        if (code == null) {
            code = findValue(bizCode) != null ? ClassHelper.convert(findValue(bizCode), String.class) : bizCode;
        }
        /******** 获取显示样式 *********/
        String requestIsClass = ClassHelper.convert(request.getParameter(isClass), String.class);
        if (StringUtil.isBlank(requestIsClass)) {
            requestIsClass = isClass;
        }

        // 获取接口
        AttachmentQueryApplication application = this.getApplication();
        /******************/
        isClassFlag = this.isTrue(requestIsClass);
        if (isClassFlag) {// 是否分类显示
            List<AttachmentConfigurationDesc> deses = application.queryGroupedAttachments(code, id);
            if (null != deses) {
                addParameter("groupList", deses);
            }
            if (null != inTable) {
                addParameter("inTable", Boolean.valueOf(inTable));
            }
        } else {
            if (null != id) {
                List<Attachment> attachments = application.queryAttachments(code, id);
                for (Attachment item : attachments) {
                    if (item.getBizSubKindId() == null) {
                        item.setBizSubKindId("");
                    }
                }
                if (null != attachments) {
                    addParameter("fileList", attachments);
                }
            }
        }

        if (null != code) {
            addParameter("bizKindId", code);
        }
        if (null != id) {
            addParameter("bizId", id);
        }
        if (null != readOnly) {
            addParameter("readonly", this.isTrue(readOnly));
        }
        /***************/
        String requestIsWrap = ClassHelper.convert(request.getParameter(isWrap), String.class);
        if (StringUtil.isBlank(requestIsWrap)) {
            requestIsWrap = "true";
            if (null != isWrap) {
                requestIsWrap = isWrap;
            }
        }
        boolean isWrapFlag = this.isTrue(requestIsWrap);
        addParameter("isWrap", isWrapFlag);
        String titleValue = title;
        if (StringUtil.isBlank(titleValue)) {
            titleValue = "common.attachment";
        }
        addParameter("title", MessageSourceContext.getMessage(titleValue));
        addParameter("batchUpload", this.isTrue(batchUpload));
        // 获取转换URL
        addParameter("attachmentConvertUrl", SystemCache.getParameter("SYS.Convert.URL", String.class));
    }

    private AttachmentQueryApplication getApplication() {
        springBean = StringUtil.isBlank(springBean) ? "attachmentApplication" : springBean;
        return SpringBeanFactory.getBean(this.getServletContext(), springBean, AttachmentQueryApplication.class);
    }

}