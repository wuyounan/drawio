<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,dateTime,tree,selectOrg" />
<script src='<c:url value="/lib/locale/task-lang-zh_CN.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>'	type="text/javascript"></script>
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/taskCenter/TaskCenter.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/taskCenter/TaskCenterExtend.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid hide" id="advancedQueryDiv">
		<div class="col-xs-12 col-sm-5 hidden-xs">
			<div style="height: 420px;" class="dom-overflow-auto">
				<x:title title="任务分类" name="group" hideTable="#myselfTaskSearch" />
				<div id="myselfTaskSearch">
					<ul class="list-group list-group-sm">
						<li class="list-group-item" taskKind="1"><a href="javascript:void(0);"><i class="fa fa-tasks"></i>待办任务</a></li>
						<li class="list-group-item" taskKind="4"><a href="javascript:void(0);"><i class="fa fa-info-circle"></i>待发任务</a></li>
						<li class="list-group-item" taskKind="5"><a href="javascript:void(0);"><i class="fa fa-user-secret"></i>本人发起</a></li>
						<li class="list-group-item" taskKind="3"><a href="javascript:void(0);"><i class="fa fa-send-o"></i>提交任务</a></li>
						<li class="list-group-item" taskKind="2"><a href="javascript:void(0);"><i class="fa fa-list"></i>已办任务</a></li>
						<li class="list-group-item" taskKind="8"><a href="javascript:void(0);"><i class="fa fa-bars"></i>办结任务</a></li>
						<li class="list-group-item" taskKind="6"><a href="javascript:void(0);"><i class="fa fa-star-half-full"></i>我的收藏</a></li>
					</ul>
				</div>
				<div class="blank_div"></div>
				<x:title title="搜索方案" name="file-text-o" hideTable="#shortcutTaskSearch"/>
				<div id="shortcutTaskSearch"><ul class="list-group list-group-sm"></ul></div>
			</div>
		</div>
		<div class="col-xs-12 col-sm-7">
			<div style="height: 420px;" class="dom-overflow-auto">
				<form method="post" class="hg-form" action="" id="queryMainForm">
					<x:title title="通用条件" name="group" hideIndex="0" hideTable="#commonCondition" id="titleConditionArea" />
					<div id="commonCondition">
						<div >
							<x:selectC name="dateRange" id="selectDateRange" list="dateRangeList" emptyOption="false" label="日期范围" fieldCol="8" labelCol="4" />
						</div>
						<div id="customDataRange" style="display: none;">
							<x:inputC name="startDate" id="editStartDate" required="false" label="开始日期" wrapper="date" fieldCol="8" labelCol="4" />
							<x:inputC name="endDate" id="editEndDate" required="false" label="结束日期" wrapper="date" fieldCol="8" labelCol="4" />
						</div>
						<div>
							<x:inputC name="searchContent" required="false" label="在主题、提交人、编号中搜索" readonly="false" fieldCol="12" labelCol="12" />
						</div>
					</div>
					<!-- <div class="clear"></div>
					<x:title title="管理的组织" name="server" hideTable="#administrativeOrg" isHide="true"/>
					<div id="administrativeOrg" style="height:200px;display:none;" class="dom-overflow-auto border-all ui-hide"> 
						<ul id="orgTree"></ul>
					</div> -->
					<div class="clear"></div>
					<x:title title="流程" name="external-link" hideTable="#procTreeDiv" isHide="true" />
					<div id="procTreeDiv" style="height:200px;" class="dom-overflow-auto border-all ui-hide">
						<ul id="procTree"></ul>
					</div>
					<div class="clear"></div>
					<x:title title="任务选项" name="check-circle" hideTable="#queryOpition" isHide="true"/>
					<div id="queryOpition" class="ui-hide">
						<div class="hg-form-row">
							<x:checkbox name="onlyQueryApplyProcUnit" label="只查询流程发起环节"/>
						</div>
						<div class="hg-form-row">
							<x:checkbox name="singleProcInstShowOneTask" label="单一流程只显示一个任务"/>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="col-xs-12">
			<div class="pull-right" style="margin-top:5px;">
				<x:button value="查 询" onclick="query()" icon="fa-search"/>
				<x:button value="保存查询方案" onclick="saveQueryScheme()" icon="fa-save" id="saveQuerySchemeButton"/>
				<x:button value="关 闭" onclick="closeTaskQueryDialog()" icon="fa-close"/>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div id='taskBar' style="margin:0px;"></div>
		<div class="blank_div"></div>
		<div id="maingrid"></div>
	</div>
</body>
</html>