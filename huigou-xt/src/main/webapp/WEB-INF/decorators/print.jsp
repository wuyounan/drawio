<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.huigou.context.ContextUtil" %>
<%@ page import="com.huigou.util.Constants" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String token = (String)ContextUtil.getSession().getAttribute(Constants.CSRF_TOKEN);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><decorator:title /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="format-detection" content="telephone=no,email=no,address=no"> 
	<x:base include="base"/>
	<x:link href="/themes/css/print.css"/>
	<x:script src='/javaScript/print.js'/>
</head>
<body>
	<input type="hidden" id="csrfTokenElement" value="<%=token%>"/>
	<div id='screenOverLoading' class='ui-tab-loading' style='display:block;top:0;'></div>
	<decorator:body />
	<c:if test="${useDefaultHandler!=false}">
	<div style="margin-top: 5px;" id="jobTaskExecutionList">
		<x:taskExecutionList procUnitId="procUnitId" defaultUnitId="Approve" bizId="bizId" />
	</div>
	</c:if>
</body>
</html>