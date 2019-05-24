<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="grid,date" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/log/errorLoginLog.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
		<form class="hg-form ui-hide" method="post" action=""
			id="queryMainForm">
			<x:inputC name="personName" label="操作员" maxLength="32" labelCol="1" />
			<x:inputC name="beginDate" label="开始时间" wrapper="date" labelCol="1" />
			<x:inputC name="endDate" label="结束时间" wrapper="date" labelCol="1" />
			<x:inputC name="ip" label="IP地址" maxLength="32" labelCol="1" />
			<x:searchButtons />
		</form>
		<div class="blank_div clearfix"></div>
		<div id="maingrid" style="margin: 2px;"></div>
	</div>
</body>
</html>
