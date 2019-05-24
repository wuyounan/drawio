<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,commonTree,comboDialog,selectOrg" />
<script src='<c:url value="/system/opm/management/DelegationBizManagement.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="组织机构树" id="mainmenu">
				<div style="overflow-x: hidden; overflow-y: auto; width: 100%;"
					id="divTreeArea">
					<ul id="orgTree">
					</ul>
				</div>
			</div>
			<div position="center" title="业务权限管理" id="bizManagementList">
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>