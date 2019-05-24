<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<div class="container-fluid">
	<div class="error-container" id="show_error_info">
		<div class="well">
			<div>
				<h1>
					<span class="error-icon"><i class="fa fa-warning"></i></span><x:message key="error.title"/>
				</h1>
			</div>
			<hr>
			<div>
				<h3><x:message key="error.info"/></h3>
			</div>
			<div>
				<h4 class="error-text">
				<c:choose>
					<c:when test="${tip == null or tip == ''}">
						<x:message key="error.auth.default"/>
					</c:when>
					<c:otherwise>
						<c:out value="${tip}"/>
					</c:otherwise>
				</c:choose>
				</h4>
			</div>
			<div>
				<x:message key="error.auth.content"/>
			</div>
			<hr>
			<div style="text-align: center;">
				<c:choose>
					<c:when test="${requestScope.location}">
						<a href="<c:url value="/logout.do"/>" class="btn btn-info"><i class="fa fa-sign-out"></i>&nbsp;<x:message key="common.relogin"/></a>
					</c:when>
					<c:otherwise>
						<a href="javascript:UICtrl.doLogout()" class="btn btn-info"><i class="fa fa-sign-out"></i>&nbsp;<x:message key="common.relogin"/></a>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div>
	</div>
</div>