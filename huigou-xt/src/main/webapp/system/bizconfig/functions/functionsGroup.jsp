<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<!DOCTYPE html>
<html>
<head>
<x:base include="dialog,dateTime,tree,combox" />
<x:script src='/system/bizconfig/functions/functionsGroup.js'/>
</head>
<body>
	<div class="container-fluid" id="functionGroupDiv">
		<c:forEach items="${functionGroupList}" var="group">
			<c:if test="${group.funsLength>0}">
			<div class="alert p-t-xl function-group <c:out value="${group.color}"/>" style="position: relative;">
				<div class="alert-title">
					<i class="fa fa-info-circle"></i>&nbsp;
					<c:out value="${group.name}" />
				</div>
				<c:forEach items="${group.funs}" var="fun">
					<div class="input-group function-button">
						<span class="input-group-btn">
							<button data-url="<c:out value="${fun.url}"/>"
								data-code="<c:out value="${fun.code}"/>"
								data-name="<c:out value="${fun.name}"/>"
								class="btn dim btn-middling-dim <c:out value="${fun.color}"/><c:out value="${fun.disabled}"/>"
								type="button" title="<c:out value="${fun.name}"/>">
								<i class="fa <c:out value="${fun.icon}"/>"></i>
							</button>
						</span>
						<button type="button" data-url="<c:out value="${fun.url}"/>"
							data-code="<c:out value="${fun.code}"/>"
							data-name="<c:out value="${fun.name}"/>"
							class="btn dim btn-middling btn-outline form-control <c:out value="${fun.color}"/><c:out value="${fun.disabled}"/>"
							title="<c:out value="${fun.name}"/>">
							<c:out value="${fun.name}" />
							<span class="function-sequence <c:out value="${fun.color}"/>"><c:out value="${fun.sequence}"/></span>
						</button>
					</div>
				</c:forEach>
				<div style="clear: both"></div>
			</div>
			</c:if>
		</c:forEach>
	</div>
</body>
</html>