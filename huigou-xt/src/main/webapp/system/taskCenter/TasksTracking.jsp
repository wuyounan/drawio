<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="data-list-warp" id="trackTaskContent">
	<c:forEach items="${trackingTasks}" var="task">
		<div id="task_<c:out value="${task.id}"/>"
			taskId="<c:out value="${task.id}"/>"
			bizId="<c:out value="${task.bizId}"/>"
			name="<c:out value="${task.name}"/>"
			url="<c:out value="${task.executorUrl}"/>">
			<div class="title-view">
				<a href="javascript:void(0);" class="aLink" taskId="<c:out value="${task.id}"/>" title="<c:out value="${task.description}" />">
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