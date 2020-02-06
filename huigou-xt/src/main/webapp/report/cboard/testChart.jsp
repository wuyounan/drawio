<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.huigou.context.ContextUtil"%>
<%@ page import="com.huigou.util.Constants"%>
<html>
<head>
<x:base include="echart,date" />
<link rel="stylesheet"
	href='<c:url value="/lib/bootstrap-table/bootstrap-table.css"/>'
	type="text/css" />
<link rel="stylesheet"
	href='<c:url value="/lib/bootstrap-table/extensions/fixed-column/bootstrap-fixed-column.css"/>'
	type="text/css" />
<link rel="stylesheet" href='<c:url value="/themes/css/mcs.css"/>'
	type="text/css" />
<script src='<c:url value="/lib/bootstrap-table/bootstrap-table.js"/>'
	type="text/javascript"></script>
<script
	src='<c:url value="/lib/bootstrap-table/extensions/fixed-column/bootstrap-fixed-column.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.base64.js"/>'
	type="text/javascript"></script>
<script
	src='<c:url value="/lib/bootstrap-table/extensions/export/bootstrap-table-export.js"/>'
	type="text/javascript"></script>
<script
	src='<c:url value="/lib/bootstrap-table/extensions/tableExport/tableExport.js"/>'
	type="text/javascript"></script>
<script
	src='<c:url value="/lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"/>'
	type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.monthpicker.js"/>'></script>
<script src='<c:url value="/lib/jquery/jquery.maskinput.js"/>'></script>
<script src='<c:url value="/javaScript/mcsUtil.js"/>'></script>
<script src='<c:url value="/javaScript/paramUtil.js"/>'></script>
<script src='<c:url value="/cboard/dashboard/testChart.js"/>'
	type="text/javascript"></script>

<style type="text/css">
#bigCaseTable {
	table-layout: fixed;
	min-width: 950px;
}

.fixed-table-container thead tr:first-child th:first-child {
	border-left: none;
}

.fixed-table-container thead th:first-child {
	border-left: 1px solid #dddddd;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<x:title title="搜索" hideTable="queryMainForm" />
		<form class="hg-form" method="post" action="" id="queryMainForm">
			<x:inputC name="start_date" required="true" wrapper="date"
				label="开始日期" labelCol="1" fieldCol="2" />
			<x:inputC name="end_date" required="true" wrapper="date" label="结束日期"
				labelCol="1" fieldCol="2" />
			<x:searchButtons />
		</form>

		<div class="col-xs-12 work-content" id="thisProvinceCaseOutYoYWrapper">
			<div class="col-xs-12" id="chart1"
				style="width: 100%; height: 400px;"></div>
			<div class="col-xs-12">
				<table id="chartTable"></table>
			</div>
		</div>
</body>
</html>
