<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<c:set var="opr" value="${sessionScope.sessionOperatorAttribute}"/>
<div class="ui-float-top-user">
	<i class="fa fa-user-o"></i>&nbsp;
	<span class="hidden-xs"><c:out value="${opr.orgName}"/>.<c:out value="${opr.deptName}"/>.</span><c:out value="${opr.personMemberName}"/>
</div>
<div class="ui-float-top-close">
	<i class="fa fa-close"></i><span class="hidden-xs">&nbsp;<x:message key="common.button.close"/></span>
</div>
<div class="ui-float-top-sign-out">
	<i class="fa fa fa-sign-out"></i><span class="hidden-xs">&nbsp;<x:message key="common.logout"/></span>
</div>
<div class="ui-float-top-pos">
	<i class="fa fa-users"></i><span class="hidden-xs">&nbsp;<x:message key="index.switch.pos"/></span>
</div>