package com.huigou.uasp.bmp.opm.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huigou.cache.SystemCache;
import com.huigou.context.ContextUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.exception.ApplicationException;
import com.huigou.shiro.authc.LimitLoginAccountException;
import com.huigou.shiro.token.StandardUserToken;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.annotation.SkipAuth;
import com.huigou.uasp.bmp.operator.OperatorApplication;
import com.huigou.uasp.bmp.opm.LoginStatus;
import com.huigou.uasp.bmp.opm.domain.model.org.LoginModel;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.TenantApplicationProxy;
import com.huigou.uasp.bmp.securitypolicy.exception.SecurityPolicyException;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.exception.VerifyCodeException;
import com.huigou.uasp.exception.VisitErrorException;
import com.huigou.uasp.log.application.LoginLogApplication;
import com.huigou.uasp.log.domain.model.HistoricSession;
import com.huigou.uasp.log.domain.model.LogoutKind;
import com.huigou.util.AesUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.ContentTypeHelper;
import com.huigou.util.SDO;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;
import com.huigou.util.VerifyCodeImage;

@Controller
@ControllerMapping("authentication")
public class AuthenticationController extends CommonController {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    
    private final static String USER_NAME_KEY = "userName";
    
    private final static String PASSWORD_KEY = "password";
    
    private final static String LOGIN_MODEL_KEY = "loginModel";

    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private TenantApplicationProxy tenantApplicationProxy;

    @Resource(name = "shiroSessionDAO")
    private AbstractSessionDAO sessionDAO;

    @Autowired
    private LoginLogApplication loginLogApplication;

    @Autowired
    private OperatorApplication operatorApplication;

    @SkipAuth
    @ControllerMethodMapping("/switchOperator")
    public String switchOperator() {
        SDO params = this.getSDO();
        String personMemberId = params.getProperty("psmId", String.class);
        Operator operator = this.operatorApplication.createOperatorByPersonMemberId(personMemberId);
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE, operator);
        return this.success();
    }

    @SkipAuth
    @ControllerMethodMapping("/switchTenant")
    public String switchTenant() {
        SDO params = this.getSDO();
        String tenantId = params.getString("tenantId");

        Tenant tenant = tenantApplicationProxy.loadTenant(tenantId);
        Assert.notNull(tenant, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, tenantId, "租户"));

        Subject subject = SecurityUtils.getSubject();
        Operator operator = (Operator) subject.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);

        operator.setTenantId(tenant.getId());
        operator.setTenantName(tenant.getName());
        operator.setRootOrgId(tenant.getOrgId());
        operator.setRootOrgFullId(tenant.getRootFullId());

        subject.getSession().setAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE, operator);

        return this.success();
    }

    private LoginStatus transformAuthenticationException(AuthenticationException aex) {
        AuthenticationException ex = ThreadLocalUtil.getVariable(Constants.AUTHENTICATION_EXCEPTION_KEY, AuthenticationException.class);
        if (ex == null) {
            ex = aex;
        }

        LoginStatus loginStatus = LoginStatus.UNKNOWN_ERROR;
        if (ex instanceof UnknownAccountException || ex instanceof IncorrectCredentialsException) {
            loginStatus = LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR;
        } else if (ex instanceof LockedAccountException || ex instanceof ExcessiveAttemptsException) {
            loginStatus = LoginStatus.USER_LOCKED;
            loginStatus.setAdditionMessage(ex.getMessage());
        } else if (ex instanceof DisabledAccountException) {
            loginStatus = LoginStatus.USER_DISABLED;
            loginStatus.setAdditionMessage(ex.getMessage());
        } else if (ex instanceof LimitLoginAccountException) {
            loginStatus = LoginStatus.LOGIN_LIMIT;
            loginStatus.setAdditionMessage(ex.getMessage());
        } else if (ex instanceof SecurityPolicyException) {
            loginStatus = LoginStatus.SECURITY_POLICY;
            loginStatus.setAdditionMessage(ex.getMessage());
        } else if (ex instanceof AuthenticationException) {
            loginStatus = LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR;
        }
        ThreadLocalUtil.removeVariable(Constants.AUTHENTICATION_EXCEPTION_KEY);
        return loginStatus;
    }

    private void logLoginError(String loginName, LoginStatus loginStatus) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        HistoricSession historicSession = loginLogApplication.loadHistoricSession(String.valueOf(session.getId()));
        if (historicSession == null) {
            Org mainPersonMember = this.orgApplication.loadMainOrgByLoginName(loginName.toUpperCase());
            if (mainPersonMember != null) {
                historicSession = HistoricSession.newInstance(mainPersonMember);
            } else {
                Operator operator = null;
                historicSession = HistoricSession.newInstance(operator);
                historicSession.setLoginName(loginName);
            }
            historicSession.setStatus(loginStatus.getId());
            historicSession.setId(String.valueOf(session.getId()));
        }
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isNotBlank(historicSession.getErrorMessage())) {
            sb.append(historicSession.getErrorMessage());
        }
        sb.append(loginName).append("登录，错误信息：").append(loginStatus.getMessage()).append(StringPool.SEMICOLON);
        historicSession.setErrorMessage(sb.toString());
        this.loginLogApplication.saveHistoricSession(historicSession);
    }

    private Map<String, Object> postProcessLogin(String loginName, String loginModel, LoginStatus loginStatus) {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Object> result = loginStatus.getLoginStatus();
        if (StringUtil.isNotBlank(loginStatus.getAdditionMessage())) {
            result.put("message", loginStatus.getAdditionMessage());
        } else {
            result.put("message", loginStatus.getMessage());
        }

        if (loginStatus == LoginStatus.SUCCESS) {
            result.put("mainPersonMember", ThreadLocalUtil.getVariable("_mainPersonMember_"));
            if (loginModel.equals(LoginModel.ANDROID.name())) {
                result.put("token", subject.getSession().getId());
            }
        } else {
            try {
                logLoginError(loginName, loginStatus);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("登录出错:用户名=%s", loginName),  e);
                }
            }
        }
        return result;
    }

    private void preProcessLogin(SDO sdo) {
        Subject subject = SecurityUtils.getSubject();
        String isVerifyCode = StringUtil.tryThese(SystemCache.getParameter("verify.code.image", String.class), "false");
        if (isVerifyCode.equals("true")) {// 使用页面验证
            boolean verifyCodePassed = false;
            String verifyCode = sdo.getString("verifyCode");// 页面输入的验证码
            // 与session数据对比
            String sessionVerifyCode = (String) subject.getSession().getAttribute(Constants.SECURITY_CODE);
            if (StringUtil.isNotBlank(verifyCode) && StringUtil.isNotBlank(sessionVerifyCode) && verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
                verifyCodePassed = true;
            }
            if (!verifyCodePassed) {
                if (logger.isErrorEnabled()) {
                    String loginName = sdo.getString(USER_NAME_KEY);
                    logger.error(String.format("验证码认证出错，userName=%s，verifyCode=%s，sessionVerifyCode=%s。", loginName, verifyCode, sessionVerifyCode));
                }
                throw new VerifyCodeException();
            }
        } else {
            if (ContextUtil.isAppRequest()){
                return;
            }
            boolean verifyCsrfPassed = false;
            // 从 head中取 ajax 操作可获取
            String csrfToken = this.getRequest().getHeader(Constants.CSRF_TOKEN);
            String sessionCSRFToken = (String) subject.getSession().getAttribute(Constants.CSRF_TOKEN);
            if (StringUtil.isNotBlank(csrfToken) && StringUtil.isNotBlank(sessionCSRFToken) && csrfToken.equals(sessionCSRFToken)) {
                verifyCsrfPassed = true;
            }
            if (!verifyCsrfPassed) {
                if (logger.isErrorEnabled()) {
                    String loginName = sdo.getString(USER_NAME_KEY);
                    logger.error(String.format("跨站点请求伪造，userName=%s，csrfToken=%s，sessionCSRFToken=%s。", loginName, csrfToken, sessionCSRFToken));
                }
                throw new VisitErrorException();
            }
        }
    }

    @SkipAuth
    @ControllerMethodMapping(value = "/verifycode", method = RequestMethod.GET)
    public void getVerifyCodeImg() {
        Subject subject = SecurityUtils.getSubject();
        HttpServletResponse response = this.getResponse();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(ContentTypeHelper.getContentType("png"));
        subject.getSession().removeAttribute(Constants.SECURITY_CODE);
        // 创建验证码图片对象
        VerifyCodeImage image = new VerifyCodeImage();
        // 创建验证码
        image.createBufferedImage();
        // 将认证码存入SESSION
        subject.getSession().setAttribute(Constants.SECURITY_CODE, image.getCode());
        // 输出图象到页面
        try {
            image.write(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            logger.error("证码出错：", e);
        }
    }

    @SkipAuth
    @ControllerMethodMapping(value = "/login", method = RequestMethod.POST)
    public String login() {
        SDO sdo = this.getSDO();
        
        String loginName = sdo.getString(USER_NAME_KEY);
        String password = sdo.getString(PASSWORD_KEY);
        String loginModel = sdo.getString(LOGIN_MODEL_KEY);

        Assert.hasText(loginName, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, USER_NAME_KEY));
        Assert.hasText(password, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, PASSWORD_KEY));

        if (StringUtil.isBlank(loginModel)) {
            loginModel = LoginModel.PC.name();
        }

        preProcessLogin(sdo);

        String md5Password;
        try {
            loginName = AesUtil.decrypt(loginName);
            password = AesUtil.decrypt(password);
            md5Password = Util.MD5(password);
        } catch (Exception e) {
            throw new ApplicationException("加密密码时发生错误。", e);
        }

        StandardUserToken token = new StandardUserToken(loginName, md5Password);

        LoginStatus loginStatus;
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            loginStatus = LoginStatus.SUCCESS;
        } catch (AuthenticationException ex) {
            if (ex instanceof AuthenticationException) {
                loginStatus = transformAuthenticationException(ex);
            } else {
                loginStatus = LoginStatus.UNKNOWN_ERROR;
            }
        }

        Map<String, Object> result = postProcessLogin(loginName, loginModel, loginStatus);

        return toResult(result);
    }

    private String logoutForApp() {
        SDO sdo = this.getSDO();
        Map<String, Object> map = sdo.getProperties();

        String token = ClassHelper.convert(map.get(Constants.TOKEN), String.class);
        if (StringUtil.isBlank(token)) {
            throw new ApplicationException("token参数不能为空。");
        }

        try {
            Session session = this.sessionDAO.readSession(token);
            if (session != null) {
                this.sessionDAO.delete(session);
            }
            return success("退出成功");
        } catch (UnknownSessionException e) {
            return error(String.format("没有找到“%s”对应的Session", token));
        }
    }

    @SkipAuth
    @ControllerMethodMapping("/logout")
    public String logout() {
        if (ContextUtil.isAppRequest()) {
            return logoutForApp();
        }

        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return Constants.LOGON_OUT_PAGE;
        }

        Session session = subject.getSession();
        loginLogApplication.logout(session, LogoutKind.NORMAL);

        subject.logout();
        return Constants.LOGON_OUT_PAGE;
    }

    @SkipAuth
    @ControllerMethodMapping("/switchLanguage")
    public String switchLanguage() {
        SDO params = this.getSDO();
        String lang = params.getString("lang");
        String country = params.getString("country");
        MessageSourceContext.setLocale(this.getRequest(), this.getResponse(), lang, country);
        return this.success();
    }

}
