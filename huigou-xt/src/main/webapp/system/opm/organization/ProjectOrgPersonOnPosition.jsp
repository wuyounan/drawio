<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,commonTree,comboDialog,selectOrg" />
<script src='<c:url value="/system/opm/organization/ProjectOrgPersonOnPosition.js"/>' 	type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
			<div id="layout">
				<div position="left" title="组织机构树" id="mainmenu">
					<ul id="orgTree"></ul>
				</div>
				<div position="center" title="业务岗位类别" id="bizManagementList">
					<div id="maingrid" style="margin: 2px;"></div>
				</div>
			</div>
	</div>
</body>
</html>