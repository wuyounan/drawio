<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/function/BaseFunctionType.js"/>' type="text/javascript"></script>
</head>
<div class="mainPanel">
	<div id="mainWrapperDiv">
		<div id="layout">
			<div position="left" title="基础职能角色类别" id="mainmenu">
				<div style="overflow-x: hidden; overflow-y: auto; width: 100%;" id="divTreeArea">
					<ul id="maintree"></ul>
				</div>
			</div>
			<div position="center" title="基础职能角色列表">
				<x:title title="搜索" hideTable="queryTable" />
				<form method="post" action="" id="queryMainForm">
					<div class='ui-form' id='queryTable'>
						<x:inputL name="code" required="false" label="编码" readonly="false" labelWidth="50" />
						<x:inputL name="name" required="false" label="名称" labelWidth="50" />
						&nbsp;&nbsp;
						<x:button value="查 询" id="btnQuery" />
						<x:button value="重 置" id="btnReset" />
						&nbsp;&nbsp;
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