package com.huigou.uasp.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.huigou.context.ContextUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.exception.SQLParseException;
import com.huigou.util.Constants;
import com.huigou.util.JSONUtil;
import com.huigou.util.LogHome;

/**
 * 异常统一处理 可单独处理某类异常 如 AuthException登录验证失败
 * 
 * @author xx
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private Logger LOGGER = LogHome.getLog();

    private static final String STATUS_KEY = "status";

    private static final String IS_AUTH_KEY = "isAuth";

    private static final String MESSAGE_KEY = "message";

    private static final String TIP_KEY = "tip";

    private static final String EXCEPTION_KEY = "ex";

    /**
     * 写异常响应
     * 
     * @param response
     * @param message
     * @param isNormalAuth
     */
    private void writeExceptionResponse(HttpServletResponse response, String message, boolean isNormalAuth, Map<String, Object> errorParam) {
        Map<String, Object> exceptionObject = new HashMap<>();

        if (errorParam != null) {
            exceptionObject.putAll(errorParam);
        }

        exceptionObject.put(STATUS_KEY, "2");
        exceptionObject.put(IS_AUTH_KEY, isNormalAuth ? 1 : 0);
        exceptionObject.put(MESSAGE_KEY, message);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().write(JSONUtil.toString(exceptionObject));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            LOGGER.error(ExceptionHandler.class, e);
        }
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        request.setAttribute(Constants.IS_REQUEST_EXCEPTION, true);
        Map<String, Object> model = new HashMap<String, Object>();
        String message = ex.getMessage();
        LOGGER.error(handler, ex);
        if (ex instanceof AppAuthRequestException) {
            this.writeExceptionResponse(response, message, true, null);
            return null;
        } else {
            // 出现SQL异常不暴露系统表结构
            boolean isSql = ex instanceof SQLException || ex instanceof BadSqlGrammarException || ex instanceof SQLParseException
                            || ex instanceof DataAccessResourceFailureException;
            if (isSql) {
                message = "数据库语句执行异常，请联系管理员！";
            }
            if (ex instanceof UnauthenticatedException || ex instanceof AuthorizationException) {
                // 您没有查看或操作的权限{0}!
                // message = MessageSourceContext.getMessage("common.power.exception", StringUtil.isNotBlank(ex.getMessage()) ? ":" + ex.getMessage() : "");
                message = MessageSourceContext.getMessage("common.power.exception", "");
            }
            model.put(EXCEPTION_KEY, ex);
            model.put(MESSAGE_KEY, message);
            model.put(TIP_KEY, message);
            if (ContextUtil.isAjaxRequest()) {
                if (ContextUtil.isLoadRequest()) {
                    return new ModelAndView(ex instanceof AuthException ? Constants.AUTH_ERROR_RESULT_WITHOUT_SUFFIX : Constants.ERROR_RESULT_WITHOUT_SUFFIX,
                                            model);
                } else {
                    if (ex instanceof AJAXRequestException) {
                        this.writeExceptionResponse(response, message, false, ((AJAXRequestException) ex).getErrorParam());
                    } else {
                        boolean isAuth = ex instanceof AuthException || ex instanceof VisitErrorException;
                        this.writeExceptionResponse(response, message, isAuth, null);
                    }
                    return null;
                }
            } else {
                return new ModelAndView(ex instanceof AuthException ? Constants.AUTH_ERROR_RESULT_WITHOUT_SUFFIX : Constants.ERROR_RESULT_WITHOUT_SUFFIX, model);
            }
        }
    }
}