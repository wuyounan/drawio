<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,dateTime,tree" />
<script src='<c:url value="/system/taskCenter/SelectTaskDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel" style="padding-top:10px;">
		<div id="pageLayout">
			<div position="left" title="任务" id="mainmenu">
				<x:title title="我的事务" name="group" hideTable="#myselfTaskSearch" />
				<div id="myselfTaskSearch">
					<ul class="list-group list-group-sm">
						<li class="list-group-item" taskKind="1"><a href="javascript:void(0);"><i class="fa fa-tasks"></i>待办任务</a></li>
						<li class="list-group-item" taskKind="4"><a href="javascript:void(0);"><i class="fa fa-info-circle"></i>待发任务</a></li>
						<li class="list-group-item" taskKind="5"><a href="javascript:void(0);"><i class="fa fa-user-secret"></i>本人发起</a></li>
						<li class="list-group-item" taskKind="3"><a href="javascript:void(0);"><i class="fa fa-send-o"></i>提交任务</a></li>
						<li class="list-group-item" taskKind="2"><a href="javascript:void(0);"><i class="fa fa-list"></i>已办任务</a></li>
						<li class="list-group-item" taskKind="6"><a href="javascript:void(0);"><i class="fa fa-star-half-full"></i>我的收藏</a></li>
					</ul>
				</div>
				<div class="blank_div"></div>
				<x:title title="任务搜索" hideTable="#queryMainForm" id="titleConditionArea" isHide="true"/>
				<form class="hg-form" method="post" action="" id="queryMainForm" style="display:none">
						<div class="hg-form-row">
							<x:selectC name="dateRange" id="selectDateRange" list="dateRangeList" emptyOption="false" label="日期范围" fieldCol="8" labelCol="4"/>
						</div>
						<div id="customDataRange" class="hg-form-row" style="display: none;">
							<x:inputC name="startDate" id="editStartDate" required="false" label="开始日期" wrapper="date" fieldCol="8" labelCol="4" />
							<x:inputC name="endDate" id="editEndDate" required="false" label="开始日期" wrapper="date" fieldCol="8" labelCol="4" />
						</div>
						<div class="hg-form-row">
							<x:inputC name="viewTaskKindList" id="selectViewTaskKind" required="false" label="任务分类" fieldCol="8" labelCol="4"/>
							<span style="display:none;">
								<x:select name="viewTaskKindList1" list="viewTaskKindList" id="selectViewTaskKind1" emptyOption="false" />
							</span>
						</div>
						<div class="hg-form-row">
							<x:inputC name="searchContent" required="false" label="在主题、提交人、编号中搜索" readonly="false" fieldCol="12" labelCol="12" />
						</div>
						<div class="hg-form-row" style="text-align:center">
							<x:button value="查 询" icon="fa-search" id="btnQuery" onclick="query(this.form)" />
						</div>
						<div style="height:20px;display:inline-block;"></div>
				</form>
			</div>
			<div position="center" class="dom-overflow">
				<div id="maingrid"></div>
				<div class="blank_div clearfix"></div>
				<div id="selectedgrid"></div>
			</div>
		</div>
	</div>

</body>
</html>