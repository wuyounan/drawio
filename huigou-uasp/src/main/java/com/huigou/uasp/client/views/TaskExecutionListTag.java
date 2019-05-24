package com.huigou.uasp.client.views;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huigou.cache.DictUtil;
import com.huigou.context.Operator;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.util.Constants;
import com.huigou.util.JSONUtil;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

public class TaskExecutionListTag extends AbstractTag {

    private static final long serialVersionUID = 7877791369067652462L;

    private static final String PROCUNITID_NAME = "procUnitId";

    private static final String DEFAULT_PROCUNITID_NAME = "Approve";

    private static final String HIDDEN_TASK_EXECUTION = "hiddenTaskExecutionList";

    private static final String TASKID_NAME = "taskId";

    private static final String READONLY = "isReadOnly";

    private String defaultResultCode;

    private String procUnitId;

    private String bizId;

    private String defaultUnitId;

    private String hasResult;

    private String taskId;

    public String getDefaultResultCode() {
        return defaultResultCode;
    }

    public void setDefaultResultCode(String defaultResultCode) {
        this.defaultResultCode = defaultResultCode;
    }

    public void setProcUnitId(String procUnitId) {
        this.procUnitId = procUnitId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public void setDefaultUnitId(String defaultUnitId) {
        this.defaultUnitId = defaultUnitId;
    }

    public void setHasResult(String hasResult) {
        this.hasResult = hasResult;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskExecutionListTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "taskExecutionList";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        // 访问错误不显示审批意见
        Object isRequestExceptionObj = this.findValue(Constants.IS_REQUEST_EXCEPTION, Boolean.class, false);
        boolean isRequestException = this.isTrue(isRequestExceptionObj);
        if (isRequestException) {
            return;
        }
        // 通过参数判断不显示审批意见
        Object hiddenTaskExecutionListObj = this.findValue(HIDDEN_TASK_EXECUTION, Boolean.class, false);
        boolean hiddenTaskExecutionList = this.isTrue(hiddenTaskExecutionListObj);
        if (hiddenTaskExecutionList) {
            return;
        }
        // 页面只读
        Object isReadOnlyObj = this.findValue(READONLY, Boolean.class, false);
        addParameter(READONLY, isReadOnlyObj);
        // 业务ID取值
        String bizIdValue = StringUtil.tryThese(findValue(bizId, String.class, ""), bizId);
        // currentProcUnitId 与 defaultUnitId 可能不同如:请假核销环节处理时 查询审批环节处理人数据
        String currentProcUnitId = this.findValue(PROCUNITID_NAME, String.class, "");
        // 从request请求中获取当前的处理环节ID 用于任务处理过程
        taskId = this.findValue(TASKID_NAME, String.class, "");
        String approvalProcUnitId = StringUtil.tryThese(defaultUnitId, currentProcUnitId, findValue(procUnitId, String.class, ""), procUnitId);
        // 起始环节为空处理 默认为审批
        if (approvalProcUnitId.equals(PROCUNITID_NAME)) {
            approvalProcUnitId = DEFAULT_PROCUNITID_NAME;
        }
        if (StringUtil.isNotBlank(bizIdValue)) {
            if (!("bizId".equalsIgnoreCase(bizIdValue) && ActivityKind.isApplyActivity(currentProcUnitId))) {
                List<Map<String, Object>> list = queryProcUnitHandler(bizIdValue, approvalProcUnitId, currentProcUnitId);
                if (null != list && list.size() > 0) {
                    addParameter("taskExecutionList", list);
                }
            }
            addParameter("bizId", bizIdValue);
        }
        if (null != approvalProcUnitId) {
            addParameter("procUnitId", approvalProcUnitId);
        }
        // 是否显示处理结果hasResult
        if (null != hasResult) {
            addParameter("hasResult", this.isTrue(hasResult));
        }
        Map<String, String> map = DictUtil.getDictionary("handleResult");
        String resultCode = defaultResultCode;
        if (StringUtil.isBlank(resultCode)) {
            resultCode = "1,2,3";
        }
        String[] resultCodes = resultCode.split(",");
        Map<String, String> handleResult = new LinkedHashMap<String, String>(resultCodes.length);
        for (String code : resultCodes) {
            handleResult.put(code, map.get(code));
        }
        addParameter("handleResultData", JSONUtil.toString(handleResult));
    }

    private List<Map<String, Object>> queryProcUnitHandler(String bizId, String approvalProcUnitId, String procUnitId) {
        HttpServletRequest request = this.getRequest();
        Operator operator = (Operator) request.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
        if (operator == null) {
            return null;
        }
        ProcUnitHandlerApplication procUnitHandlerApplication = SpringBeanFactory.getBean(this.getServletContext(), "procUnitHandlerApplication",
                                                                                          ProcUnitHandlerApplication.class);
        List<Map<String, Object>> group = procUnitHandlerApplication.groupProcUnitHandlers(bizId, approvalProcUnitId, procUnitId, taskId, operator.getUserId());

        return group;
    }
}