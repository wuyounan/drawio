package com.huigou.uasp.bmp.intercept;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.operator.OperatorUIElementPermissionBuilder;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.uasp.log.application.LogApplication;
import com.huigou.uasp.log.aspect.BizLogBuilder;
import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

/**
 * 系统权限获取 页面元素使用
 * 
 * @author xx
 */
public class ExecuteContextInterceptor extends HandlerInterceptorAdapter {

    private OperatorUIElementPermissionBuilder operatorUIElementPermissionBuilder;

    public void setOperatorUIElementPermissionBuilder(OperatorUIElementPermissionBuilder operatorUIElementPermissionBuilder) {
        this.operatorUIElementPermissionBuilder = operatorUIElementPermissionBuilder;
    }

    @Autowired
    private LogApplication logApplication;

    /**
     * 字段及按钮权限处理
     * 
     * @author
     * @param permissions
     * @param map
     * @param functionCode
     *            void
     */
    private void handlePermission(Collection<Map<String, Object>> permissions, HttpServletRequest request) {
        if (permissions != null && permissions.size() > 0) {
            request.getSession().setAttribute("PermissionInterceptorSet", permissions);
            Map<String, Object> noaccessField = new HashMap<String, Object>(permissions.size());
            Map<String, Object> noaccessDetail = new HashMap<String, Object>(permissions.size());
            String code = "", operationId = "", uiElmentKindId = "";
            for (Map<String, Object> m : permissions) {
                code = ClassHelper.convert(m.get("code"), String.class);
                operationId = ClassHelper.convert(m.get("operationId"), String.class, "");
                uiElmentKindId = ClassHelper.convert(m.get("kindId"), String.class, "");
                // 判读输入的key 是否为 noaccess
                if (!StringUtil.isBlank(code) && operationId.equals("noaccess")) {
                    if (uiElmentKindId.equals("0")) {// fieldType.equals("0")主集字段
                        noaccessField.put(code, "1");
                    } else if (uiElmentKindId.equals("1")) {// fieldType.equals("1")子集
                        noaccessDetail.put(code, "1");
                    }
                }
            }
            if (noaccessField.size() > 0) {
                ThreadLocalUtil.putVariable("noaccessField", noaccessField);
            }
            if (noaccessDetail.size() > 0) {
                ThreadLocalUtil.putVariable("noaccessDetail", noaccessDetail);
            }
            ThreadLocalUtil.putVariable("permissions", permissions);
        }
    }

    public void intercept(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Operator operator = (Operator) request.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
        String func = null;
        if (!ContextUtil.isAjaxRequest()) {
            String functionId = request.getParameter("functionId");
            String functionCode = request.getParameter("functionCode");
            if (!StringUtil.isBlank(functionId)) {
                func = functionId;
            } else if (!StringUtil.isBlank(functionCode)) {
                func = functionCode;
            }
            // 存在功能ID需要查询字段权限，及按钮权限
            if (!StringUtil.isBlank(func)) {
                try {
                    List<Map<String, Object>> permissions = operatorUIElementPermissionBuilder.queryUIElementPermissionsByFunction(func,
                                                                                                                                   operator.getUserId(),
                                                                                                                                   !StringUtil.isBlank(functionId));
                    handlePermission(permissions, request);
                    // 记录访问日志
                    BizLog bizLog = SpringBeanFactory.getBean(request.getSession().getServletContext(), "bizLog", BizLog.class);
                    BizLogBuilder.buildLogInfo(bizLog, OperationType.FUN, request.getRequestURI(), func);
                    logApplication.savelog(bizLog);
                } catch (Exception e) {
                    throw new ApplicationException(e);
                }
            }
        }
        // 处理任务请求
        if (ContextUtil.isJobRequest()) {
            try {
                String taskId = request.getParameter("taskId");
                if (!StringUtil.isBlank(taskId)) {
                    Collection<Map<String, Object>> permissions = operatorUIElementPermissionBuilder.queryUIElementPermissionsByTaskId(taskId);
                    handlePermission(permissions, request);
                }
            } catch (Exception e) {
                throw new ApplicationException(e);
            }
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.intercept(request, response);
        return true;
    }

    public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, Exception exception) throws Exception {
        ThreadLocalUtil.removeVariableMap();
        ProcessEventContext.remove();
    }
}
