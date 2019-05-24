package com.huigou.shiro.realm;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huigou.context.Operator;
import com.huigou.shiro.authc.LimitLoginAccountException;
import com.huigou.shiro.filter.SingleSignOutHandler;
import com.huigou.shiro.token.CasServiceToken;
import com.huigou.uasp.bmp.opm.LoginStatus;
import com.huigou.util.Constants;

/**
 * cas 单点登录 拷贝来源于 org.apache.shiro.cas.CasRealm
 * 
 * @author xx
 *         由于需要继承StandardRealm 实现用户信息注册机权限加载，考虑不继承原有的 CasRealm
 */
public class CasStandardRealm extends StandardRealm {

    // default name of the CAS attribute for remember me authentication (CAS 3.4.10+)
    public static final String DEFAULT_REMEMBER_ME_ATTRIBUTE_NAME = "longTermAuthenticationRequestTokenUsed";

    public static final String DEFAULT_VALIDATION_PROTOCOL = "CAS";

    private static Logger log = LoggerFactory.getLogger(CasStandardRealm.class);

    // this is the url of the CAS server (example : http://host:port/cas)
    private String casServerUrlPrefix;

    // this is the CAS service url of the application (example : http://host:port/mycontextpath/shiro-cas)
    private String casService;

    private SingleSignOutHandler singleSignOutHandler;

    /*
     * CAS protocol to use for ticket validation : CAS (default) or SAML :
     * - CAS protocol can be used with CAS server version < 3.1 : in this case, no user attributes can be retrieved from the CAS ticket validation response (except if there are some customizations on CAS server side)
     * - SAML protocol can be used with CAS server version >= 3.1 : in this case, user attributes can be extracted from the CAS ticket validation response
     */
    private String validationProtocol = DEFAULT_VALIDATION_PROTOCOL;

    // default name of the CAS attribute for remember me authentication (CAS 3.4.10+)
    private String rememberMeAttributeName = DEFAULT_REMEMBER_ME_ATTRIBUTE_NAME;

    // this class from the CAS client is used to validate a service ticket on CAS server
    private TicketValidator ticketValidator;

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasService() {
        return casService;
    }

    public void setCasService(String casService) {
        this.casService = casService;
    }

    public String getValidationProtocol() {
        return validationProtocol;
    }

    public void setValidationProtocol(String validationProtocol) {
        this.validationProtocol = validationProtocol;
    }

    public String getRememberMeAttributeName() {
        return rememberMeAttributeName;
    }

    public void setRememberMeAttributeName(String rememberMeAttributeName) {
        this.rememberMeAttributeName = rememberMeAttributeName;
    }

    public void setSingleSignOutHandler(SingleSignOutHandler singleSignOutHandler) {
        this.singleSignOutHandler = singleSignOutHandler;
    }

    @Override
    public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
        return CasToken.class;
    }

    @Override
    public String getName() {
        return "CasStandardRealm";
    }

    @Override
    protected void onInit() {
        super.onInit();
        ensureTicketValidator();
    }

    protected TicketValidator ensureTicketValidator() {
        if (this.ticketValidator == null) {
            this.ticketValidator = createTicketValidator();
        }
        return this.ticketValidator;
    }

    protected TicketValidator createTicketValidator() {
        String urlPrefix = getCasServerUrlPrefix();
        if ("saml".equalsIgnoreCase(getValidationProtocol())) {
            return new Saml11TicketValidator(urlPrefix);
        }
        return new Cas20ServiceTicketValidator(urlPrefix);
    }

    /**
     * Authenticates a user and retrieves its information.
     * 
     * @param token
     *            the authentication token
     * @throws AuthenticationException
     *             if there is an error during authentication.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        CasToken casToken = (CasToken) token;
        if (token == null) {
            return null;
        }

        String ticket = (String) casToken.getCredentials();
        if (!StringUtils.hasText(ticket)) {
            return null;
        }
        String service = this.getCasService();
        if (casToken instanceof CasServiceToken) {
            service = ((CasServiceToken) casToken).getService();
        }
        if (!StringUtils.hasText(service)) {
            return null;
        }
        TicketValidator ticketValidator = ensureTicketValidator();
        Session session = SecurityUtils.getSubject().getSession();
        try {
            // contact CAS server to validate service ticket
            Assertion casAssertion = ticketValidator.validate(ticket, service);
            // get principal, user id and attributes
            AttributePrincipal casPrincipal = casAssertion.getPrincipal();
            String loginName = casPrincipal.getName();
            log.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}", new Object[] { ticket, getCasServerUrlPrefix(), loginName });

            Map<String, Object> attributes = casPrincipal.getAttributes();
            // refresh authentication token (user id + remember me)
            casToken.setUserId(loginName);
            String rememberMeAttributeName = getRememberMeAttributeName();
            String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);
            boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
            if (isRemembered) {
                casToken.setRememberMe(true);
            }
            // 创建系统用户
            Operator operator = this.createOperator(loginName);
            // 保存登录用户信息
            this.cacheOperator(operator);
            SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(operator, ticket, getName());
            // 记录ticket和sessionID
            singleSignOutHandler.recordSession(ticket);
            return result;
        } catch (TicketValidationException e) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR);
            log.error("TicketValidationException", e);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        } catch (UnknownAccountException | IncorrectCredentialsException uae) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR);
            log.error("UnknownAccountException", uae);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", uae);
        } catch (LockedAccountException e) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.USER_LOCKED);
            log.error("LockedAccountException", e);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        } catch (LimitLoginAccountException e) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.LOGIN_LIMIT);
            log.error("LimitLoginAccountException", e);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        } catch (ExcessiveAttemptsException e) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.USER_LOCKED);
            log.error("ExcessiveAttemptsException", e);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        } catch (DisabledAccountException e) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.USER_DISABLED);
            log.error("DisabledAccountException", e);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        } catch (AuthenticationException e) {
            session.setAttribute(Constants.CAS_VALIDATOR_ERROR, LoginStatus.SECURITY_POLICY);
            log.error("AuthenticationException", e);
            throw new CasAuthenticationException("Unable to validate ticket [" + ticket + "]", e);
        }
    }

}
