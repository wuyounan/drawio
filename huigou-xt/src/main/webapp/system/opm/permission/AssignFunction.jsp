<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.formButton.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/permission/AssignFunction.js"/>' type="text/javascript"></script>
<title>分配功能</title>
</head>
<div class="container-fluid">
	<div id="layout">
		<div position="top">
			<div class="hg-form">
				<x:selectC name="permissionRoot" list="permissionsList" required="true" label="权限类别" labelCol="1"/>
				<input type="hidden" id="choosePermissionId">
				<x:inputC name="choosePermissionName" wrapper="tree" label="一级权限" required="true" labelCol="1"/>
			</div>
		</div>
		<div position="left" title="权限树">
			<ul id="functionTree"></ul>
		</div>
		<div position="center" class="ui-layout-height-auto display-table display-inline-xs">
			<div class="display-table-cell display-inline-xs">
				<x:button value="=>" id="divAdd" onclick="addData()"/>
				<x:button value="<="  id="divDelete" onclick="deleteData()"/>
				<x:button value="清空已选" id="divDeleteAll" onclick="doDeleteAll()"/>
			</div>
		</div>  
		<div position="right" title="已选权限">
			<ul id="functionTree2"></ul>
		</div>
	</div>
</div>