<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>cboard</title>
<script src='<c:url value="/biz/mcs/util/mcsUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/biz/mcs/report/cboard/cboardView.js"/>' type="text/javascript"></script>
<style type="text/css">
@media ( max-width :500px) {
	.bootstrap-table .form-control {
		width: 120px;
	}
}
</style>
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="bizOrgId" />
		<div id="params"></div>
		<div class="clearfix"></div>
		<div id="tables" style="margin-top: 20px;"></div>
	</div>
</body>
</html>