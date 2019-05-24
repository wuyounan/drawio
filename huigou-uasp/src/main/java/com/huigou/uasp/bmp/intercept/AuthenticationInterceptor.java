package com.huigou.uasp.bmp.intercept;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huigou.context.ApplicationProperties;
import com.huigou.context.ContextUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.exception.ApplicationException;
import com.huigou.shiro.token.CasServiceToken;
import com.huigou.uasp.annotation.SkipAuth;
import com.huigou.uasp.annotation.TaskStatusCheck;
import com.huigou.uasp.bmp.opm.LoginStatus;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.uasp.exception.AppAuthRequestException;
import com.huigou.uasp.exception.AuthException;
import com.huigou.uasp.exception.VisitErrorException;
import com.huigou.uasp.log.application.LoginLogApplication;
import com.huigou.util.Constants;
import com.huigou.util.Md5Builder;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 认证拦截器 检查用户是否登录，csrf防御
 * 
 * @author xx
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    
    
    
    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final String SPLIT_PATTERN = "[, ;\r\n]";// 逗号 空格 分号 换行

    @Autowired
    private LoginLogApplication loginLogApplication;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ActApplication actApplication;

    // csrf验证白名单
    private List<String> csrfWhiteListURL = null;

    private List<String> ssoCsfWhiteListUrls = null;

    // 单点登录情况下csrf验证白名单
    private String ssoCsfWhiteListMethodName;

    // 需要验证任务状态的方法名
    private String taskStatusChekedMethodName;

    private List<String> taskStatusChekedMethoUrls = null;

    public void setCsrfWhiteListURL(List<String> csrfWhiteListURL) {
        this.csrfWhiteListURL = csrfWhiteListURL;
    }

    public void setSsoCsfWhiteListMethodName(String ssoCsfWhiteListMethodName) {
        this.ssoCsfWhiteListMethodName = ssoCsfWhiteListMethodName;
    }

    public String getTaskStatusChekedMethodName() {
        return taskStatusChekedMethodName;
    }

    public void setTaskStatusChekedMethodName(String taskStatusChekedMethodName) {
        this.taskStatusChekedMethodName = taskStatusChekedMethodName;
    }

    public List<String> getSsoCsfWhiteListUrls() {
        if (ssoCsfWhiteListUrls == null) {
            // 解析字符串为可用于匹配的路径
            ssoCsfWhiteListUrls = new ArrayList<String>();
            if (StringUtil.isNotBlank(ssoCsfWhiteListMethodName)) {
                String[] nameArray = ssoCsfWhiteListMethodName.split(SPLIT_PATTERN);
                for (String name : nameArray) {
                    name = name.trim();
                    if (name.length() == 0) {
                        continue;
                    }
                    // 如字符串为 load 匹配路径为/**/load*
                    ssoCsfWhiteListUrls.add(String.format("/**/%s*", name));
                }
            }
        }
        return ssoCsfWhiteListUrls;
    }

    public List<String> getTaskStatusChekedMethoUrls() {
        if (taskStatusChekedMethoUrls == null) {
            // 解析字符串为可用于匹配的路径
            taskStatusChekedMethoUrls = new ArrayList<String>();
            if (StringUtil.isNotBlank(taskStatusChekedMethodName)) {
                String[] nameArray = taskStatusChekedMethodName.split(SPLIT_PATTERN);
                for (String name : nameArray) {
                    name = name.trim();
                    if (name.length() == 0) {
                        continue;
                    }
                    // 如字符串为 load 匹配路径为/**/load*
                    taskStatusChekedMethoUrls.add(String.format("/**/%s*", name));
                }
            }
        }
        return taskStatusChekedMethoUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ContextUtil.isAppRequest()) {
            try {
                SDO params = ContextUtil.getSdoFromJsonBody();
                ContextUtil.getRequest().setAttribute(Constants.SDO, params);
            } catch (Exception e) {
                throw new AppAuthRequestException(e);
            }
        }
        // 执行方法前是否验证任务状态
        TaskStatusCheck taskStatusCheck = null;
        // 判断是否经过认证验证
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 类存在注解定义了不经过认证验证
            Object obj = handlerMethod.getBean();
            SkipAuth classSkipAuth = AnnotationUtils.findAnnotation(obj.getClass(), SkipAuth.class);
            if (classSkipAuth != null) {
                return true;
            }
            // 方法存在注解定义了不经过认证验证
            SkipAuth skipAuth = handlerMethod.getMethodAnnotation(SkipAuth.class);
            if (skipAuth != null) {
                return true;
            }
            taskStatusCheck = handlerMethod.getMethodAnnotation(TaskStatusCheck.class);
        }
        return this.intercept(request, response, taskStatusCheck);
    }

    private boolean intercept(HttpServletRequest request, HttpServletResponse response, TaskStatusCheck taskStatusCheck) throws Exception {
        Operator operator = this.getAuthOperator(request, response);
        Subject subject = SecurityUtils.getSubject();
        // operator 为空判断是否存在单点登录
        if (operator == null) {
            // ajax 请求全部需要登录后执行
            if (ContextUtil.isAjaxRequest()) {
                throw new AuthException();
            }
            if (isCas()) {
                // 系统使用cas单点登录认证
                if (!this.casLogin(subject, request, response)) {
                    return false;
                }
            }
            // 重新获取operator
            operator = (Operator) subject.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
        }
        // 全部判断处理完成后operator还是为空抛出异常
        if (operator == null) {
            throw new AuthException();
        }
        // 处理跨站请求伪造
        boolean verifyCSRF = this.verifyCSRFToken(request);
        // 跨站请求伪造校验失败
        if (!verifyCSRF) {
            // 全部ajax请求抛出异常
            if (ContextUtil.isAjaxRequest()) {
                throw new VisitErrorException();
            }
            // CAS中 处理可以直接产生数据改变的服务
            if (isCas()) {
                // 只有 ssoCsfWhiteListMethodName 中开头的方法放行
                verifyCSRF = this.isSSOCSRFWhiteURL(request.getServletPath());
                if (verifyCSRF) {
                    this.verifySSOAsBizId(request, operator);
                }
            }
        }
        // 全部判断处理完后
        if (!verifyCSRF) {
            throw new VisitErrorException();
        }
        // 验证任务状态
        this.verifyTaskStatusByTaskId(request, taskStatusCheck);
        ThreadLocalUtil.putOperator(operator);
        return true;
    }

    public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, Exception exception) throws Exception {
        ThreadLocalUtil.removeVariableMap();
        ProcessEventContext.remove();
    }

    /**
     * 是否存在cas ticket
     * 
     * @param request
     * @return
     */
    private boolean hasCasTicket(HttpServletRequest request) {
        String casTicket = request.getParameter(Constants.CAS_TICKET_PARAMETER);
        return StringUtil.isNotBlank(casTicket);
    }

    /**
     * CAS登录验证
     * 
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    protected boolean casLogin(Subject subject, HttpServletRequest request, HttpServletResponse response) {
        if (this.hasCasTicket(request)) {
            String casTicket = request.getParameter(Constants.CAS_TICKET_PARAMETER);
            // 返回的地址回带有ticket=参数 这里取原始请求的地址
            String service = (String) subject.getSession().getAttribute(Constants.CAS_SERVICE_URL);
            subject.getSession().removeAttribute(Constants.CAS_SERVICE_URL);
            // 模拟 cas 认证
            try {
                CasServiceToken casToken = new CasServiceToken(casTicket, service);
                subject.login(casToken);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                LoginStatus loginStatus = (LoginStatus) subject.getSession().getAttribute(Constants.CAS_VALIDATOR_ERROR);
                if (loginStatus != null) {
                    throw new AuthException(loginStatus.getMessage());
                } else {
                    throw new AuthException();
                }
            }
        } else { // 参数中没有cas Ticket
            // 增加request.getQueryString() 需要重定向回来后可获取参数 带有参数的链接需要转码后发送
            String backService = applicationProperties.getServiceValidatorUrl(request.getRequestURI() + StringUtil.encode("?" + request.getQueryString()));
            String service = applicationProperties.getServiceValidatorUrl(request.getRequestURI() + "?" + request.getQueryString());
            // 缓存原始请求URL
            subject.getSession().setAttribute(Constants.CAS_SERVICE_URL, service);
            // 重定向到cas服务器登录页面 service参数为回调地址
            String url = applicationProperties.getCasServerValidatorUrl(backService);
            try {
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;

        }
    }

    /**
     * 处理跨站请求伪造
     * 针对需要登录后才能处理的请求,验证CSRFToken校验
     * 
     * @param request
     */
    protected boolean verifyCSRFToken(HttpServletRequest request) {
        // 白名单中的url不进行CSRFToken校验
        if (this.isCSRFWhiteURL(request.getServletPath())) {
            return true;
        }
        if (ContextUtil.isAppRequest()){
            return true;
        }
        // 从 head中取 ajax 操作可获取
        String csrfToken = request.getHeader(Constants.CSRF_TOKEN);
        // 读取请求中参数
        if (StringUtil.isBlank(csrfToken)) {
            csrfToken = request.getParameter(Constants.CSRF_TOKEN);
        }
        if (StringUtil.isBlank(csrfToken)) {
            return false;
        }
        Subject subject = SecurityUtils.getSubject();
        String sessionCSRFToken = (String) subject.getSession().getAttribute(Constants.CSRF_TOKEN);
        if (StringUtil.isBlank(sessionCSRFToken)) {
            return false;
        }
        String checkCSRFToken = sessionCSRFToken;
        String bizId = request.getParameter(Constants.BIZID);
        if (StringUtil.isNotBlank(bizId)) {// 存在bizId
            if (!ContextUtil.isAjaxRequest()) {// 不是ajax请求
                // 存在bizId校验则使用bizId与token加密后传输
                checkCSRFToken = Md5Builder.getMd5(bizId + sessionCSRFToken);
            }
        }
        if (csrfToken.equalsIgnoreCase(checkCSRFToken)) {
            return true;
        } else {
            // 存在csrfToken的不相同强制报错
            throw new VisitErrorException(String.format("存在csrfToken的不相同, csrfToken=[%s],sessionCSRFToken=[%s]", csrfToken, checkCSRFToken));
        }
    }

    /**
     * csrf白名单验证
     * 
     * @param currentURL
     * @return
     */
    private boolean isCSRFWhiteURL(String currentURL) {
        if (csrfWhiteListURL == null || csrfWhiteListURL.size() == 0) {
            return false;
        }
        for (String whiteURL : csrfWhiteListURL) {
            if (pathMatcher.match(whiteURL, currentURL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 单点登录情况下 csrf白名单验证
     * 
     * @param currentURL
     * @return
     */
    private boolean isSSOCSRFWhiteURL(String currentURL) {
        List<String> whiteList = this.getSsoCsfWhiteListUrls();
        if (whiteList == null || whiteList.size() == 0) {
            return false;
        }
        for (String whiteURL : whiteList) {
            if (pathMatcher.match(whiteURL, currentURL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否cas单点登录
     * 
     * @return
     */
    private boolean isCas() {
        return applicationProperties.isCas();
    }

    /**
     * 单点登录链接校验
     */
    private void verifySSOAsBizId(HttpServletRequest request, Operator operator) {
        String bizId = request.getParameter(Constants.BIZID);
        if (StringUtil.isBlank(bizId)) {
            return;
        }
        // 存在bizId 需要根据当前人员判断是否允许查看任务单据
        boolean flag = actApplication.checkVisitTaskByBizIdAndPersonId(bizId, operator.getUserId());
        if (!flag) {
            throw new AuthorizationException();
        }
    }

    /**
     * 判断是否需要验证任务状态
     * 
     * @param currentURL
     * @return
     */
    private boolean isVerifyTaskStatusURL(String currentURL) {
        List<String> list = this.getTaskStatusChekedMethoUrls();
        if (list == null || list.size() == 0) {
            return false;
        }
        for (String whiteURL : list) {
            if (pathMatcher.match(whiteURL, currentURL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ajax操作如果存在任务ID加入到head里面验证任务状态
     */
    private void verifyTaskStatusByTaskId(HttpServletRequest request, TaskStatusCheck taskStatusCheck) {
        if (!ContextUtil.isAjaxRequest()) {// 非ajax操作不验证
            return;
        }
        // 获取taskId
        String taskId = request.getHeader(Constants.PROCTASKID);
        if (StringUtil.isBlank(taskId)) {// 没有任务ID不处理
            return;
        }
        if (taskStatusCheck == null) {// 方法注解为空 默认过滤方法名
            String currentURL = request.getServletPath();
            // 过滤方法名
            if (!this.isVerifyTaskStatusURL(currentURL)) {
                return;
            }
        } else {// 存在注解 判断chek属性 chek==false 不验证
            if (!taskStatusCheck.check()) {// 注解标注为不执行验证
                return;
            }
        }
        // 根据任务判断任务是否为可执行状态
        boolean flag = actApplication.checkVisitTaskStatusByTaskId(taskId);
        if (!flag) {
            throw new ApplicationException(MessageSourceContext.getMessage("common.task.status.exception"));
        }
    }

    /**
     * 获取session中的用户信息
     *
     * @param request
     *            请求
     * @return 操作员
     */
    private Operator getAuthOperator(HttpServletRequest request, HttpServletResponse response) {
        Operator operator = null;
        Subject subject = SecurityUtils.getSubject();
        if (ContextUtil.isAppRequest()) {
            operator = ContextUtil.getOperatorByToken();
        } else if (ContextUtil.isRtxAuth(request)) {
            // TODO 存在两种情况：
            // 1、先直接获取operator对象，A、B在同一机器上访问，实际上访问的人为A，B处理任务实际上是用A的身份处理
            // 2、每次覆盖operator对象，当A借用B的电脑处理任务时，B就可以用A的身份处理任务
            operator = (Operator) subject.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
        } else {
            operator = (Operator) subject.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
        }
        return operator;
    }

}
