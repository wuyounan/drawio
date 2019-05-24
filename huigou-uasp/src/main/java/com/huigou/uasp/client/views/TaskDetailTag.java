package com.huigou.uasp.client.views;

import java.util.HashMap;
import java.util.Map;

import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

/**
 * 根据任务ID读取任务其他信息
 * 
 * @author xx
 */
public class TaskDetailTag extends AbstractTag {

    private static final long serialVersionUID = 1768667722022360825L;

    private static final String TASKID_NAME = "taskId";

    public TaskDetailTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "taskDetail";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        // 参数中获取任务ID
        String taskId = findValue(TASKID_NAME, String.class);
        if (StringUtil.isNotBlank(taskId)) {
            Map<String, Object> map = null;
            TaskDetail detail = this.queryTaskDetail(taskId);
            if (detail != null) {
                map = ClassHelper.toMap(detail);
                map.put("activityModel", "readonly");
                map.put("isApplicantPerson", "false");
                Operator operator = (Operator) ContextUtil.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
                if (operator != null) {
                    String executorPersonId = detail.getExecutorPersonId();
                    if (StringUtil.isNotBlank(executorPersonId)) {
                        // 判断当前用户是否为任务的处理人
                        if (operator.getUserId().equals(executorPersonId)) {
                            map.put("activityModel", "do");
                        }
                    }
                    // 当前用户是否为流程发起人
                    String applicantPersonMemberId = detail.getApplicantPersonMemberId();
                    if (StringUtil.isNotBlank(applicantPersonMemberId)) {
                        String applicantPersonId = OpmUtil.getPersonIdFromPersonMemberId(applicantPersonMemberId);
                        if (operator.getUserId().equals(applicantPersonId)) {
                            map.put("isApplicantPerson", "true");
                        }
                    }
                }
            } else {
                map = new HashMap<String, Object>();
                map.put("activityModel", "detail");
                map.put("id", taskId);
            }
            addParameter("taskDetail", map);
        }
    }

    private TaskDetail queryTaskDetail(String taskId) {
        WorkflowApplication application = SpringBeanFactory.getBean(this.getServletContext(), "workflowApplication", WorkflowApplication.class);
        return application.queryTaskDetail(taskId);
    }
}
