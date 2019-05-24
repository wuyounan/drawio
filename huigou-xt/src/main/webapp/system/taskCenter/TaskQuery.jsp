<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,dateTime,tree,selectOrg,commonTree" />
<script src='<c:url value="/lib/locale/task-lang-zh_CN.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/taskCenter/TaskCenterExtend.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/taskCenter/TaskQuery.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:title title="common.button.search" hideTable="queryMainDiv" isHide="true"/>
		<form class="hg-form" method="post" action="" id="queryMainForm">
			<div class="hg-form-row">
				<x:hidden name="administrativeOrgFullId" />
				<x:hidden name="procFullId" />
				<x:inputC name="orgName" required="true" label="发起组织" readonly="false" labelCol="1" wrapper="tree"/>
				<x:selectC name="viewTaskKindList" list="viewTaskKindListData" label="任务类型" labelCol="1" emptyOption="false"/>
				<x:searchButtons />
			</div>
			<div class="ui-hide" id="queryMainDiv">
				<div class="hg-form-row">
					<x:selectC name="dateRange" id="selectDateRange" list="dateRangeList" emptyOption="false" label="日期范围" labelCol="1" />
					<x:inputC name="startDate" id="editStartDate" required="false" label="开始日期" wrapper="date" labelCol="1" />
					<x:inputC name="endDate" id="editEndDate" required="false" label="结束日期" wrapper="date" labelCol="1" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="executorPersonMemberName" required="false" label="执行人"  labelCol="1" />
					<x:inputC name="creatorPersonNemberName" required="false" label="提交人"  labelCol="1" />
					<x:inputC name="applicantPersonNemberName"  required="false" label="申请人"  labelCol="1" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="procName" required="false" label="流程" readonly="false" labelCol="1" wrapper="tree"/>
					<x:inputC name="searchContent" required="false" label="关键字" readonly="false" labelCol="1" />
					<div class="col-xs-12 col-sm-4" style="padding-top:7px">
						<x:checkbox name="onlyQueryApplyProcUnit" label="只查询流程发起环节"/>
						<x:checkbox name="singleProcInstShowOneTask" label="单一流程只显示一个任务"/>
					</div>
				</div>
			</div>
		</form>
		<div class="blank_div clearfix"></div>
		<div id="maingrid" style="margin:2px;"></div>
	</div>
</body>
</html>