package com.huigou.uasp.client.views;

import java.util.Map;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.uasp.bmp.common.BizBillStatus;
import com.huigou.util.StringUtil;

public class BillTitleTag extends AbstractTag {

    private static final long serialVersionUID = -1584163120230030634L;

    private String title;

    protected String needStatus;

    protected String needPerson;

    protected String statusList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNeedStatus() {
        return needStatus;
    }

    public void setNeedStatus(String needStatus) {
        this.needStatus = needStatus;
    }

    public String getNeedPerson() {
        return needPerson;
    }

    public void setNeedPerson(String needPerson) {
        this.needPerson = needPerson;
    }

    public String getStatusList() {
        return statusList;
    }

    public void setStatusList(String statusList) {
        this.statusList = statusList;
    }

    public BillTitleTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "billTitle";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        boolean showStatus = false;
        if (null != id) {
            addParameter("id", id);
        }
        if (null != title) {
            String titleValue = this.findValue(title, String.class, title);
            titleValue = MessageSourceContext.getMessage(titleValue);
            addParameter("title", titleValue);
        }
        if (null != needStatus) {
            showStatus = this.isTrue(needStatus);
            addParameter("needStatus", showStatus);
        }
        if (null != needPerson) {
            addParameter("needPerson", this.isTrue(needPerson));
        } else {
            addParameter("needPerson", true);
        }
        String billCode = findValue("billCode", String.class);
        if (StringUtil.isNotBlank(billCode)) {
            addParameter("billCode", billCode);
        }
        String personMemberName = findValue("personMemberName", String.class);
        if (StringUtil.isNotBlank(personMemberName)) {
            addParameter("personMemberName", personMemberName);
        }

        String showOrganName = SystemCache.getParameter("wf.bizBill.creator.showOrganName", String.class);
        if (Boolean.valueOf(showOrganName)) {
            String organName = findValue("organName", String.class);
            if (StringUtil.isNotBlank(organName)) {
                addParameter("organName", organName);
            }
        }

        String deptName = findValue("deptName", String.class);
        if (StringUtil.isNotBlank(deptName)) {
            addParameter("deptName", deptName);
        }

        String showPositionName = SystemCache.getParameter("wf.bizBill.creator.showPositionName", String.class);
        if (Boolean.valueOf(showPositionName)) {
            String positionName = findValue("positionName", String.class);
            if (StringUtil.isNotBlank(positionName)) {
                addParameter("positionName", positionName);
            }
        }
        String statusTextView = findValue("statusTextView", String.class);
        // 需要显示状态
        if (showStatus && StringUtil.isBlank(statusTextView)) {
            Object listObject = findValue(statusList == null ? "statusList" : statusList);
            if (listObject == null) {
                listObject = BizBillStatus.getMap();
            }
            // 获取后台的状态数据
            Map<String, String> tmp = this.transformationMap(listObject);
            if (tmp != null) {
                String status = findValue("statusId", String.class);
                if (StringUtil.isBlank(status)) {
                    status = findValue("status", String.class);
                }
                if (StringUtil.isNotBlank(status)) {
                    statusTextView = tmp.get(status);
                }
            }
        }
        if (StringUtil.isNotBlank(statusTextView)) {
            addParameter("statusTextView", statusTextView);
        }
        Object obj = findValue("fillinDate");
        if (obj != null) {
            Object formatData = TaglibUtil.formatData(obj, "datetime");
            addParameter("fillinDate", formatData);
        }
    }

}
