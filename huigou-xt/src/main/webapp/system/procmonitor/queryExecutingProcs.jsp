<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/system/procmonitor/askUtil.js"/>'
	type="text/javascript"></script>
<script
	src='<c:url value="/system/procmonitor/queryExecutingProcs.js"/>'
	type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
		<form class="hg-form ui-hide" method="post" action=""
			id="queryMainForm">
			<div class="ui-form" id="queryDiv">
				<x:hidden name="procId" />
				<x:inputC name="procID_Name" label="业务类型" />
				<x:inputC name="startTime" label="发起时间起" wrapper="date" />
				<x:inputC name="endTime" label="发起时间止" wrapper="date" />
				<x:inputC name="procName" label="流程名称" />
				<x:inputC name="deptName" label="发起部门" />
				<x:searchButtons />
			</div>
		</form>
		<div class="blank_div clearfix"></div>
		<div id="maingrid" style="margin: 2px;"></div>
	</div>
</body>
</html>
