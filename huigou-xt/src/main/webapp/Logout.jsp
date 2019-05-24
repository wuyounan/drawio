<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.huigou.context.ApplicationProperties" %>
<%@ page import="com.huigou.util.ApplicationContextWrapper" %>
<!DOCTYPE html>
<html>
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>正在注销...</title>  
<%
	ApplicationProperties applicationProperties=ApplicationContextWrapper.getBean("applicationProperties", ApplicationProperties.class);
	//未使用cas 直接调用重定向到登录页
	if(!applicationProperties.isCas()){
	    response.sendRedirect("Login.jsp"); 
	    return;
	}
	String logoutUrl=applicationProperties.getCasServerLogoutUrl();
%>
<!-- 服务器端可能不支持logout后的跳转 这里可使用iframe调用logout后再 location.href='/Login.jsp'-->
<script type="text/javascript">
    location.href="<%=logoutUrl%>";  
</script>  
</head>  
<body>  
</body>  
</html>