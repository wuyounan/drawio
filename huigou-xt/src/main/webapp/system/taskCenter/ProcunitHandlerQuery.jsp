<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,dateTime,tree,selectOrg,commonTree" />
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/taskCenter/ProcunitHandlerQuery.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<form class="hg-form" method="post" action="" id="queryMainForm">
			<div class="hg-form-row">
				<x:hidden name="administrativeOrgFullId" />
				<x:inputC name="orgName" required="true" label="发起组织" readonly="false" labelCol="1" wrapper="tree"/>
				<x:inputC name="executorPersonMemberName" required="false" label="执行人"  labelCol="1" />
				<div class="col-xs-12 col-sm-1" style="padding-top:7px">
					<x:checkbox name="isNotTask" label="无任务"/>
				</div>
				<x:searchButtons />
			</div>
			<div>
				<div class="hg-form-row">
					<x:selectC name="dateRange" id="selectDateRange" list="dateRangeList" emptyOption="false" label="日期范围" labelCol="1" />
					<x:inputC name="startDate" id="editStartDate" required="false" label="开始日期" wrapper="date" labelCol="1" />
					<x:inputC name="endDate" id="editEndDate" required="false" label="结束日期" wrapper="date" labelCol="1" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="searchContent" required="false" label="关键字" readonly="false" labelCol="1" />
					<x:hidden name="procFullId" />
					<x:inputC name="procName" required="false" label="流程" readonly="false" labelCol="1" wrapper="tree"/>
					<x:inputC name="applicantPersonNemberName"  required="false" label="申请人"  labelCol="1" />
				</div>
			</div>
		</form>
		<div class="blank_div clearfix"></div>
		<div id="maingrid" style="margin:2px;"></div>
	</div>
</body>
</html>