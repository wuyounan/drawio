<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox,selectOrg,comboDialog," />
<script src='<c:url value="/javaScript/common.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.contextmenu.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configtool/approvalRule/FlowTaskHandlerManage.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div class="hg-form">
			<x:inputC name="bizCode" required="false" label="单据编号" labelCol="1" />
			<div class="col-xs-12 col-md-4">
				<x:button value="查询" id="btnQueryTask" />
			</div>
		</div>
		<div class="blank_div clearfix"></div>
		<div id='pageTab' style="margin: 0px;">
			<div class="ui-tab-links">
				<ul id="menu_ul">
					<li id="flowTaskList" divid="flowTaskListDiv">流程任务列表</li>
					<li id="procUnitHandlerList" divid="procUnitHandlerListDiv">处理人列表</li>
					<!-- 
						<li id="hiProcUnitHandlerList" divid="hiProcUnitHandlerListDiv">历史处理人列表</li>
						 -->
				</ul>
			</div>
			<div class="ui-tab-content" style="padding: 2px; padding-right: 0;">
				<div class="layout" id='flowTaskListDiv'>
					<div id="taskGrid" style="margin:2px;"></div>
				</div>
				<div class="layout" id='procUnitHandlerListDiv'>
					<div id="procUnitHandlerGrid" style="margin:2px;"></div>
				</div>
				<!-- 
					<div class="layout" id='hiProcUnitHandlerListDiv'>
						<div id="hiProcUnitHandlerGrid"></div>
					</div>
					 -->
			</div>
		</div>
	</div>
</body>
</html>
