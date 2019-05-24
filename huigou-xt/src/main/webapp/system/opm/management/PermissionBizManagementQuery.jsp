<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,selectOrg,combox" />
<script src='<c:url value="/system/opm/management/PermissionBizManagementQuery.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="manageType" id="delegationManageType"/>
		<form class="hg-form" method="post" action="">
			<x:hidden name="fullId" />
		    <x:inputC name="managementName" required="false" label="业务权限" readonly="false" labelCol="1" wrapper="tree"/>
			<x:hidden name="manageOrgId" />
		    <x:inputC name="manageOrgName" required="false" label="管理者" readonly="false" labelCol="1" wrapper="tree"/>
		    <x:hidden name="subordinationOrgId" />
		    <x:inputC name="subordinationOrgName" required="false" label="被管理者" readonly="false" labelCol="1" wrapper="tree"/>
		    <div class="clearfix"></div>
		    <x:inputC name="param" label="权限编码" maxLength="32" labelCol="1" />
			<x:inputC name="keyValue" label="关键字" maxLength="32" labelCol="1" />
			<x:searchButtons />
		</form>
		<div class="blank_div"></div>
		<div id="maingrid" style="margin: 2px;"></div>
	</div>
</body>
</html>
