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
				<div  class="alert alert-danger">
					<c:choose>
						<c:when test="${tip == null or tip == ''}">
							<x:message key="error.page.default"/>
						</c:when>
						<c:otherwise>
							<c:out value="${tip}"/>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<hr>
			<div>
				<x:message key="error.page.prompt"/>
			</div>
		</div>
	</div>
</div>