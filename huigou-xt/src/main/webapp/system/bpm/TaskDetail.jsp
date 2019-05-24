<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree" />
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>'  type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel" style="padding-top: 10px;">
		<x:hidden name="id" />
		<table class='tableInput' style="width: 380px;">
			<x:layout proportion="20%,*" />
			<tr>
				<x:inputTD name="creatorOgnName" disabled="true"
					label="提交机构" />
			</tr>
			<tr>
				<x:inputTD name="creatorDeptName" disabled="true"
					label="提交部门" />
			</tr>
			<tr>
				<x:inputTD name="creatorPersonMemberName" disabled="true"
					label="提交人" />
			</tr>
			<tr>
				<x:inputTD name="startTime" disabled="true" label="提交时间" />
			</tr>
			<tr>
				<x:textareaTD name="description" disabled="true" label="主题"
					rows="15"></x:textareaTD>
			</tr>
		</table>
	</div>
</body>
</html>