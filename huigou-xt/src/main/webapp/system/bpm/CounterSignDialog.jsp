<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="tree,layout,selectOrg,grid,combox" />
<script src='<c:url value="/system/bpm/BpmUtil.js"/>'  type="text/javascript"></script>
<script src='<c:url value="/system/bpm/CounterSignDialog.js?a=2"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid" style="padding:10px;">
		<c:import url="/system/opm/organization/SelectOrgCommonPage.jsp" />
		<x:hidden name="procUnitId" />
		<x:hidden name="groupId" />
		<x:hidden name="hiProcUnitHandlerInstVersion"/>
	</div>
</body>
</html>