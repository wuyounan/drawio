<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="data-list-warp" id="waitTaskContent">
	<c:forEach items="${tasks}" var="task">
		<div id="task_<c:out value="${task.id}"/>" <c:if test="${task.statusId=='ready'}"> class="ready_task" </c:if>
			catalogId="<c:out value="${task.catalogId}"/>"
			taskKindId="<c:out value="${task.kindId}"/>"
			bizId="<c:out value="${task.bizId}"/>"
			taskId="<c:out value="${task.id}"/>"
			statusId="<c:out value="${task.statusId}"/>"
			name="<c:out value="${task.name}"/>"
			url="<c:out value="${task.executorUrl}"/>">
			<div class="title-view">
				<a href="javascript:void(0);" class="aLink" taskId="<c:out value="${task.id}"/>" title="<c:out value="${task.description}"/>">
					<c:out value="${task.description}" />
				</a>
			</div>
			<div class="date-view">
				<a href="javascript:void(0);" class="aLink" taskId="<c:out value="${task.id}"/>">
					<c:out value="${task.startTime}" />
				</a>
			</div>
		</div>
	</c:forEach>
</div>