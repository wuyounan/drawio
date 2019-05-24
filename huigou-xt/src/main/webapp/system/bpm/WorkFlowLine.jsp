<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<html>
<head>
<x:script src='/system/bpm/BpmUtil.js'/>
<x:script src='/system/bpm/WorkFlowLine.js'/>
<body>
	<input type='hidden' id="bizId" value="<c:out value="${param.bizId}"/>">
	<div class="container-fluid">
		<div class="time-line-wrap">
			<c:forEach items="${requestScope.procHandlers}" var="node" varStatus="stat">
				<div class="time-line-div" data-proc-unit-id="<c:out value="${node.procUnitId}" />">
					<div class="line-label">
						<span class="label label-lg proc-unit-name">
							<b><c:out value="${node.procUnitName}" /></b>
						</span>
					</div>
					<div class="title"> 
					</div>
					<div class="body">
						<c:set var="handlersCount" value="0" />
						<c:forEach items="${node.procUnitHandlers}" var="handler" varStatus="handlerStat">
							<c:set var="handlersCount" value="${handlersCount+1}" />
							<div class="blocks alert" data-status-id="<c:out value="${handler.statusId}"/>">
								<div class="time">
									<i class="ace-icon fa fa-clock-o"></i>&nbsp;<c:out value="${handler.duration}"/>小时
								</div>
								<div class="name">
									<div class="col-xs-12 col-sm-1 m-b-xs">
										<span class="label m-r-sm handle-status">
											<c:out value="${handler.statusName}" />
										</span>
									</div>
									<div class="col-xs-12 col-sm-3 m-b-xs">
										<c:if test="${handler.subProcUnitName!=''}">
										<span class="label label-default m-r-sm">
											<c:out value="${handler.subProcUnitName}" />
										</span>
										</c:if>
										<span class="black-color m-r-sm">
											<c:out value="${handler.executorPersonMemberName}"/>
										</span>
									</div>
									<div class="col-xs-12 col-sm-2 m-b-xs">
										<span class="label label-info m-r-sm">
											<c:out value="${f:format(handler.startTime,'datetime')}" />
										</span>
									</div>
									<c:if test="${handler.endTime!=''}">
									<div class="col-xs-12 col-sm-2 m-b-xs">
										<span class="label label-success m-r-sm">
											<c:out value="${f:format(handler.endTime,'datetime')}" />
										</span>
									</div>
									</c:if>
									<div class="col-xs-12 col-sm-1 m-b-xs">
										<span class="label label-lg ui-hide handle-result" data-result="<c:out value="${handler.result}"/>">
										</span>
									</div>
								</div>
								<div class="clearfix"></div>
								<div class="substance">
									<div class="col-xs-12"><c:out value="${handler.opinion}"/></div>
									<c:if test="${handler.manuscript!=null}">
									<div class="col-xs-12><img class="manuscript" style="height:60px;width:100%;" src="<c:out value="${handler.manuscript}"/>"></img></div>
									</c:if>
								</div>
								<div class="clearfix"></div>
							</div>
						</c:forEach>
						<c:if test="${handlersCount==0}">
							<div class="alert alert-danger m-b-xs">
								<i class="fa fa-warning"></i>&nbsp;
								<x:message key="common.job.error.handler.empty"/>
							</div>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</body>
</html>