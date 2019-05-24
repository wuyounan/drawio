<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="uiElementPermissions" value="${sessionScope.PermissionInterceptorSet}"/>
var UIElementPermission={};
<c:forEach items="${uiElementPermissions}" var="p" >
UIElementPermission['<c:out value="${p.code}"/>']={operationId:'<c:out value="${p.operationId}"/>',kindId:'<c:out value="${p.kindId}"/>'};
</c:forEach>
<c:remove var="PermissionInterceptorSet"  scope="session"/>