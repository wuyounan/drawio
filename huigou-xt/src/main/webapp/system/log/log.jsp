<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,date,tree,combox,commonTree" />
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/log/log.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="组织机构">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="日志列表">
				<x:hidden name="operatorRoleKindId" />
				<x:hidden name="statusId" />
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:inputC name="appName" label="系统" maxLength="32" labelCol="1" />
					<x:inputC name="beginDate" wrapper="date" label="开始时间" labelCol="1"/>
					<x:inputC name="endDate" wrapper="date" label="结束时间" labelCol="1"/>
					<x:selectC name="roleKindId" list="roleKinds" emptyOption="true" label="角色" labelCol="1"/>
					<x:inputC name="personMemberName" label="操作员" maxLength="32" labelCol="1"/>
					<x:selectC name="logType" list="logType" label="类型" labelCol="1"/>
					<x:selectC name="operationType" list="operationType" label="操作" labelCol="1"/>
					<x:inputC name="ip" label="IP" maxLength="40" labelCol="1"/>
					<x:inputC name="description" label="描述" maxLength="128" labelCol="1"/>
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
				<div id="exportGrid" style="margin: 2px; display: none;"></div>
			</div>
		</div>
	</div>
</body>
</html>
