<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/permission/Role.js"/>' type="text/javascript"></script>
<title>角色管理</title>
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="enableTspm" />
		<x:hidden name="isUseTspm" />
		<div id="layout">
			<div position="left" title="角色树">
				<ul id="maintree">
				</ul>
			</div>
			<div position="center" >
				<x:hidden name="mainRoleId" />
				<x:hidden name="mainrRoleKindId" />
				<div style="margin-top:5px;">
					<div id="maingrid"></div>
				</div>
			</div>
			<div position="right" title="权限列表">
				<div id="permissiongrid" style="margin:2px;margin-left:-2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>