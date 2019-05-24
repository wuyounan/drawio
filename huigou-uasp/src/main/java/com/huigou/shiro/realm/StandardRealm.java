package com.huigou.shiro.realm;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.context.SecurityGrade;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.context.TmspmConifg;
import com.huigou.data.repository.GeneralRepository;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.shiro.authc.LimitLoginAccountException;
import com.huigou.shiro.token.StandardUserToken;
import com.huigou.uasp.bmp.operator.OperatorApplication;
import com.huigou.uasp.bmp.opm.LicenseChecker;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.opm.proxy.CoreApplicationFactory;
import com.huigou.uasp.bmp.securitypolicy.application.SecurityPolicyApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;
import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount.PersonAccountStatus;
import com.huigou.uasp.bmp.securitypolicy.domain.model.SecurityPolicy;
import com.huigou.uasp.bmp.securitypolicy.domain.query.PersonLoginLimitDesc;
import com.huigou.uasp.bmp.securitypolicy.exception.SecurityPolicyException;
import com.huigou.uasp.log.application.LoginLogApplication;
import com.huigou.uasp.log.domain.model.OnlineSession;
import com.huigou.util.CommonUtil;
import com.huigou.util.Constants;
import com.huigou.util.GetClientMacAddr;
import com.huigou.util.StringUtil;

/**
 * 平台默认shiro安全处理域
 * 
 * @author xx
 */
public class StandardRealm extends AuthorizingRealm {

    @Autowired
    private OperatorApplication operatorApplication;

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Autowired
    private LoginLogApplication loginLogApplication;

    @Autowired
    private SecurityPolicyApplication securityPolicyApplication;

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    @Autowired
    private TmspmConifg tmspmConifg;

    private static long LICESENE_INDEX = 0;

    private LicenseChecker getLicenseChecker() {
        return coreApplicationFactory.getLicenseChecker();
    }

    private void checkLicense() {
        if (LICESENE_INDEX >= 1000) {
            long onlineUser = this.loginLogApplication.countOnlinePersons();
            if (!this.getLicenseChecker().checkOnlineUser((int) onlineUser)) {
                throw new ApplicationException("在线用户数已超过注册数。");
            }

            String sql = "select count(*) from act_ru_task";

            long taskCount = this.generalRepository.coungByNativeSql(sql, null);
            if (!this.getLicenseChecker().checkTask((int) taskCount)) {
                throw new ApplicationException("任务数已超过注册数。");
            }

            if (!this.getLicenseChecker().checkValidTime()) {
                throw new ApplicationException("授权已过期。");
            }
            LICESENE_INDEX = 0;
        }
        LICESENE_INDEX++;
    }

    /**
     * Shiro在进行登录验证时候，会检查Realm是否支持该Token，如果不支持跳过当前Realm，继续下一个Realm。
     * 这里返回Realm 能处理的Token super.supports 方法中判断
     */
    @Override
    public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
        return StandardUserToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Operator operator = (Operator) principals.iterator().next();
        if (operator != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            // 1、 添加角色

            // 2、 添加权限
            List<String> permissions = this.accessApplication.queryPersonFunPermissions(operator.getUserId());
            authorizationInfo.addStringPermissions(permissions);
            return authorizationInfo;
        }
        return null;
    }

    @Override
    public String getName() {
        return "StandardRealm";
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        checkLicense();
        String loginName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        // 创建登录用户信息
        Operator operator = this.createOperator(loginName);
        // 获取Person
        Person person = ThreadLocalUtil.getVariable("_mainPerson_", Person.class);
        // 加载安全策略信息
        this.verifySecurity(operator, person, password);
        // 保存登录信息到session
        this.cacheOperator(operator);
        SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(operator, person.getPassword(), getName());
        return result;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    private void processAuthenticationException(AuthenticationException ex) {
        ThreadLocalUtil.putVariable(Constants.AUTHENTICATION_EXCEPTION_KEY, ex);
        throw ex;
    }

    private SecurityPolicy checkSecurityPolicy(SecurityGrade securityGrade) {
        SecurityPolicy securityPolicy = this.securityPolicyApplication.findSecurityGrade(securityGrade.getId(), ValidStatus.ENABLED.getId());
        if (securityPolicy == null) {
            SecurityPolicyException ex = new SecurityPolicyException(String.format("安全策略“%s”没有设置或启用，请联系统系管理员。", securityGrade.getDisplayName()));
            processAuthenticationException(ex);
        }
        return securityPolicy;
    }

    /**
     * 检查账号锁定
     * 
     * @param personAccount
     *            人员账号
     * @param securityPolicy
     *            安全策略
     */
    private void checkLockout(PersonAccount personAccount, String password, String inputPassword, SecurityPolicy securityPolicy) {
        Session session;
        if (ValidStatus.DISABLED.getId().equals(personAccount.getStatus())) {
            Date currentDate = new Date();
            // 自动解锁
            Integer intervalMinutes = (int) ((currentDate.getTime() - personAccount.getLockedDate().getTime()) / CommonUtil.MILLIS_PER_MINUTE);
            if (intervalMinutes >= securityPolicy.getAutoUnlockTime()) {
                personAccount.setStatus(ValidStatus.ENABLED.getId());
                personAccount.setLockedDate(null);
                securityPolicyApplication.savePersonAccount(personAccount);
                session = SecurityUtils.getSubject().getSession();
                session.removeAttribute(SecurityPolicyApplication.LOGIN_TIME_ATTRIBUTE);
            } else {
                LockedAccountException ex = new LockedAccountException(String.format("用户已被锁定，将在%d分钟后自动解锁或者请联系系统管理员。", securityPolicy.getAutoUnlockTime()));
                processAuthenticationException(ex);
            }
        }

        if (!password.equals(inputPassword)) {
            session = SecurityUtils.getSubject().getSession();
            Integer failedLoginAttempts = (Integer) session.getAttribute(SecurityPolicyApplication.LOGIN_TIME_ATTRIBUTE);
            if (failedLoginAttempts == null) {
                failedLoginAttempts = 0;
            }

            session.setAttribute(SecurityPolicyApplication.LOGIN_TIME_ATTRIBUTE, ++failedLoginAttempts);

            if (failedLoginAttempts.equals(securityPolicy.getLockUserPasswordErrorTime())) {
                personAccount.setStatus(ValidStatus.DISABLED.getId());
                personAccount.setLockedDate(new Date());
                securityPolicyApplication.savePersonAccount(personAccount);
                ExcessiveAttemptsException ex = new ExcessiveAttemptsException(String.format("登录出错%d次，用户已被锁定。", failedLoginAttempts));
                processAuthenticationException(ex);
            }

            IncorrectCredentialsException ex = new IncorrectCredentialsException();
            processAuthenticationException(ex);
        }
    }

    /**
     * 检查登录限制
     * 
     * @param person
     *            人员
     */
    private void checkLoginLimit(Person person) {
        List<PersonLoginLimitDesc> personLoginLimits = securityPolicyApplication.queryPersonLoginLimitsByLoginName(person.getLoginName());
        if (personLoginLimits.size() > 0) {
            boolean allowded = false;
            String ip;
            for (PersonLoginLimitDesc personLoginLimit : personLoginLimits) {
                ip = ContextUtil.getRequestIP();

                GetClientMacAddr getClientMacAddr = new GetClientMacAddr(ip);
                String macAddress = "";
                try {
                    macAddress = getClientMacAddr.getRemoteMacAddress();
                } catch (Exception e) {
                    allowded = true;
                }
                if (personLoginLimit.getMachineIp().equals(ip) && StringUtil.isNotBlank(macAddress)
                    && personLoginLimit.getMachineMacAddress().equals(macAddress)) {
                    allowded = true;
                    break;
                }
            }
            if (!allowded) {
                LimitLoginAccountException ex = new LimitLoginAccountException("你不能在当前机器上登录。");
                processAuthenticationException(ex);
            }
        }
    }

    /**
     * 检查密码到期
     * 
     * @param personAccount
     *            人员账号
     * @param securityPolicy
     *            安全策略
     */
    private void checkPasswordExpired(PersonAccount personAccount, SecurityPolicy securityPolicy) {
        Session session = SecurityUtils.getSubject().getSession();
        if (personAccount.isStatus(PersonAccountStatus.INIT)) {
            session.setAttribute(SecurityPolicyApplication.FIRST_LOGIN_UPDATE_PASSWORD_ATTRIBUTE, true);
            return;
        }

        Date lastModifiedPasswordDate = personAccount.getLastModifiedPasswordDate();
        Date currentDate = CommonUtil.getCurrentDate();
        if (lastModifiedPasswordDate == null) {
            lastModifiedPasswordDate = currentDate;
        }
        Integer passwordValidityInterval = securityPolicy.getPasswordValidityInterval();
        if (CommonUtil.getDaysBetween(lastModifiedPasswordDate, currentDate) >= passwordValidityInterval) {
            session.setAttribute(SecurityPolicyApplication.PASSWORD_EXPIRED_ATTRIBUTE, true);
        }

        Date date = CommonUtil.addDays(lastModifiedPasswordDate, passwordValidityInterval);
        Integer passwordWarningDay = CommonUtil.getDaysBetween(currentDate, date);
        if (passwordWarningDay < securityPolicy.getPasswordExpireGiveDays()) {
            session.setAttribute(SecurityPolicyApplication.UPDATE_PASSWORD_PROMPT_ATTRIBUTE, true);
            // 因为对比，天数需要加一
            session.setAttribute(SecurityPolicyApplication.PASSWORD_REMAINDER_DAYS_ATTRIBUTE, passwordWarningDay + 1);
        }
    }

    /**
     * 登录用户安全等级校验
     * 
     * @param operator
     *            操作员
     * @param person
     *            人员
     * @param password
     *            密码
     */
    protected void verifySecurity(Operator operator, Person person, String inputPassword) {
        if (person.getSecurityGrade() == null) {
            if (!person.getPassword().equals(inputPassword)) {
                IncorrectCredentialsException ex = new IncorrectCredentialsException();
                processAuthenticationException(ex);
            }
            return;
        }

        PersonAccount personAccount = this.securityPolicyApplication.loadAndInitPersonAccountByLoginName(person.getLoginName(),
                                                                                                                                       operator.getFullId());

        SecurityPolicy securityPolicy = this.checkSecurityPolicy(person.getSecurityGrade());
        this.checkLockout(personAccount, person.getPassword(), inputPassword, securityPolicy);
        this.checkLoginLimit(person);
        this.checkPasswordExpired(personAccount, securityPolicy);
    }

    /**
     * 创建Operator   
     * 
     * @param loginName
     * @return
     */
    protected Operator createOperator(String loginName) {
        Operator operator = this.operatorApplication.createOperatorByLoginName(loginName);
        // 保存登录用户信息
        OnlineSession onlineSession = OnlineSession.newInstance(operator);
        loginLogApplication.saveOnlineSession(onlineSession);
        return operator;
    }

    /**
     * 登录验证成功将operator 对象写入 subject session
     * 
     * @param operator
     */
    protected void cacheOperator(Operator operator) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE, operator);
        session.removeAttribute(SecurityPolicyApplication.LOGIN_TIME_ATTRIBUTE);
        String token = (String) session.getAttribute(Constants.CSRF_TOKEN);
        if (StringUtil.isBlank(token)) {
            // 设置csrf 校验使用token
            session.setAttribute(Constants.CSRF_TOKEN, CommonUtil.createGUID());
        }
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        clearCache();
        super.onLogout(principals);
    }

    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        this.clearCache(principals);
        this.clearCachedAuthenticationInfo(principals);
        this.clearCachedAuthorizationInfo(principals);
        super.clearCache(principals);
    }

}
