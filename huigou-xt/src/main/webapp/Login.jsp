<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List,java.util.Map" %>
<%@ page import="com.huigou.context.ContextUtil" %>
<%@ page import="com.huigou.context.MessageSourceContext" %>
<%@ page import="com.huigou.cache.DictUtil" %>
<%@ page import="com.huigou.util.Constants" %>
<%@ page import="com.huigou.util.CommonUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<%
	String token = (String)ContextUtil.getSession().getAttribute(Constants.CSRF_TOKEN);
    if (token == null || token == ""){
        token = CommonUtil.createGUID();
        ContextUtil.getSession().setAttribute(Constants.CSRF_TOKEN, token);
    }
    MessageSourceContext.setLocale(request, response);
    List<Map<String, Object>> i18nLanguages=DictUtil.getDictionaryList("i18nLanguage");
	request.setAttribute("isI18nLanguage", false);
	if(i18nLanguages!=null&&i18nLanguages.size()>0){
	    request.setAttribute("isI18nLanguage", true);
		request.setAttribute("i18nLanguages", i18nLanguages);
	}
	response.setHeader("Pragma","No-cache");  
	response.setHeader("Cache-Control","No-cache");  
	response.setHeader("Cache-Control", "No-store");
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1, shrink-to-fit=no"/>
	<!--[if lt IE 8]>
	<meta http-equiv="refresh" content="0;url='ie.html'"/>
	<![endif]-->
	<title><c:out value="${f:systemParameter('SYSTEM.NAME')}" /></title>
	<x:link href="/themes/font-face.css"/>
	<x:base include="base" />
	<x:link href="/login/css/login.css"/>
	<x:link href="/themes/extend.css"/>
	<!--[if lt IE 9]>
	<x:script src='/lib/bootstrap/html5shiv.min.js'/>
	<x:script src='/lib/bootstrap/respond.min.js'/>
	<![endif]-->
	<x:script src='/lib/jquery/jquery.base64.js'/>
	<x:script src='/lib/jquery/jquery.md5.js'/>
	<x:script src='/lib/jquery/jquery.aes.js'/>
	<x:script src='/login/js/Login.js'/>
</head>
<body id="login-page" class="dom-overflow">
	<input type="hidden" id="csrfTokenElement" value="<%=token%>"/>
	<div class="container" style="width:100%;">
		<div class="row login-head hidden-xs">
			<c:if test="${f:systemParameter('SYSTEM.LOGO.PATH')!=null}">
				<img src="<c:url value="${f:systemParameter('SYSTEM.LOGO.PATH')}"/>"/>
			</c:if>
		</div>
		<div class="row login-main">
			<div class="col-sm-7 hidden-xs">
				<div id="login-left"></div>
			</div>
			<div class="col-sm-5 col-xs-12">
				<div id="login-box">
					<div id="login-box-holder">
						<div class="row">
							<div class="col-xs-12">
								<div id="login-header">
									<div id="login-logo">
										<i class="fa fa-joomla"></i>&nbsp;
										<c:out value="${f:systemParameter('SYSTEM.NAME')}" />
									</div>
								</div>
								<div id="login-box-inner">
									<form role="form" action=""  name="logonForm" id="logonForm">
										<div class="input-group">
											<span class="input-group-addon"><i class="fa fa-user"></i></span>
											<input class="text" type="text" id="userName" name="userName"/>
										</div>
										<div class="input-group">
											<span class="input-group-addon"><i class="fa fa-key"></i></span>
											<input type="password" class="text" id="password" name="password" autocomplete="off"/>
										</div>
										<c:if test="${f:systemParameter('verify.code.image')=='true'}">
										<div class="input-group">
			                                <input type="text" class="text" id="verifyCode" name="verifyCode" autocomplete="off"/>
			                                <span class="input-group-addon">
			                                   <img src="<c:url value="/verifycode.load"/>" height="40" id="verifyCodeImg" title="点击更换图片"/>
			                                </span>
			                            </div>
										</c:if>
										<div id="remember-me-wrapper">
											<div class="row">
												<div class="col-xs-6">
													<div class="checkbox-nice">
														<input type="checkbox" name="remember" id="remember"/>
														<label for="remember"><x:message key="common.Rememberme"/></label>
													</div>
												</div>
											</div>
										</div>
										<div class="row">
											<div class="col-xs-12">
												<button type="button" class="btn btn-primary col-xs-12" id="btnLogin" name="button" >
													<x:message key="common.login"/>
												</button>
											</div>
										</div>
										<c:if test="${isI18nLanguage}">
										<div class="row">
											<div class="col-xs-12 switch-language" style="text-align:center;padding-top:10px;">
												<c:forEach items="${i18nLanguages}" var="language" varStatus="i">
												<c:set var="languageClassName" value="btn-info" />
												<c:if test="${i.index%2==1}">
													<c:set var="languageClassName" value="btn-warning" />
												</c:if>
												<a class="btn btn-xs <c:out value="${languageClassName}"/>" href="javascript:void(0);" data-language="<c:out value="${language.value}"/>">
													<c:out value="${language.name}"/>
												</a>
						                       	</c:forEach>
						                     </div>
										</div>
										</c:if>
									</form>
								</div>
							</div>
						</div>
					</div>
					<div id="login-box-footer">
						<div class="row">
							<div class="col-xs-12">
								<c:out value="${f:systemParameter('SYSTEM.COPYRIGHT')}" escapeXml="false"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>