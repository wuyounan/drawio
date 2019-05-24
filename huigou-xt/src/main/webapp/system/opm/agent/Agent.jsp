<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/agent/Agent.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
		<form class="hg-form ui-hide" method="post" action=""
			id="queryMainForm">
			<x:inputC name="startDate" label="开始日期" wrapper="date" labelCol="1" />
			<x:inputC name="endDate" label="结束日期" wrapper="date" labelCol="1" />
			<x:searchButtons />
		</form>
		<div class="blank_div clearfix"></div>
		<div id="maingrid" style="margin: 2px;"></div>
	</div>
</body>
</html>