<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,dateTime,tree" />
<script src='<c:url value="/system/taskCenter/QueryDimissionPersonTask.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>	
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<div id="layout">
				<div position="left" title="组织机构树" id="mainmenu">
					<div style="overflow-x: hidden; overflow-y: auto; width: 100%;"
						id="divTreeArea">
						<ul id="orgTree">
						</ul>
					</div>
				</div>
				<div position="center" title="任务列表">
					<x:title title="搜索" hideTable="queryTable" />
					<form method="post" action="" id="queryMainForm">
						<div class="ui-form" id="queryTable">
							<div class="row">
								<x:inputL name="startDate" id="editStartDate" required="true"
									label="开始日期" wrapper="date" width="180" />
								<x:inputL name="endDate" id="editEndDate" required="true"
									label="结束日期" wrapper="date" width="180" />
								<x:selectL name="selectViewTaskKind" required="false"
									label="任务分类" list="viewTaskKindList" />
							</div>
							<div class="row">
								<x:inputL name="searchContent" required="false" label="关键字"
									value="在主题、编号中搜索" readonly="false" width="476" />
								&nbsp;&nbsp;
								<x:button value="查 询" id="btnQuery" />
								&nbsp;&nbsp;
								<x:button value="重 置" id="btnReset" />
								&nbsp;&nbsp;
							</div>
						</div>
					</form>
					<div class="blank_div"></div>
					<div id="maingrid" style="margin: 2px;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>