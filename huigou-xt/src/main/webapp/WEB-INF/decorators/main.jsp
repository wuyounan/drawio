<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.huigou.context.ContextUtil" %>
<%@ page import="com.huigou.util.Constants" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<%
	String token = (String)ContextUtil.getSession().getAttribute(Constants.CSRF_TOKEN);
	response.setHeader("Pragma","No-cache");  
	response.setHeader("Cache-Control","No-cache");  
	response.setDateHeader("Expires", -1);  
	response.setHeader("Cache-Control", "No-store");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1, shrink-to-fit=no"/>
		<!-- 360浏览器默认使用Webkit内核 -->
		<meta name="renderer" content="webkit">
		<title><decorator:title default="${f:systemParameter('SYSTEM.NAME')}"/></title>
		<meta http-equiv="expires" content="0">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta name="format-detection" content="telephone=no,email=no,address=no"> 
		<!--[if (gt IE 9)|!(IE)]><!--><x:link href="/themes/font-face.css"/><!--<![endif]-->
		<x:base include="base"/>
		<x:link href="/themes/extend.css"/>
		<!--[if lt IE 9]>
		<x:script src='/lib/bootstrap/html5shiv.min.js'/>
		<x:script src='/lib/bootstrap/respond.min.js'/>
		<![endif]-->
		<decorator:head/>
		<x:script src='/common/OPpermission.jsp'/>
	</head>
	<body>
		<input type="hidden" id="csrfTokenElement" value="<%=token%>"/>
		<x:hidden name="isReadOnly" id="mainPageReadOnlyFlag"/>
		<div id="ui-screen-over-loading" class="ui-main-page-loading"></div>
		<div class="main-package-over">
		<decorator:body/>
		</div>
	</body>
</html>