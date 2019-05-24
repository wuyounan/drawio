<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,grid,dateTime,combox,tree,attachment" />
<script
	src='<c:url value="/system/opm/permission/PermissionCheckBill.js"/>'
	type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div class="subject">功 能 权 限 检 查 流 程</div>
		<div class="ui-form" style="width: 100%; left: 0px; padding: 3px;">
			<form method="post" action="" id="submitForm">
				<x:hidden name="id" id="id" />
				<x:hidden name="organId" />
				<x:hidden name="centerId" />
				<x:hidden name="centerName" />
				<x:hidden name="deptId" />
				<x:hidden name="positionId" />
				<x:hidden name="personMemberId" />
				<x:hidden name="positionName" />
				<x:hidden name="fullId" />
				<x:hidden name="status" value="0" />
				<x:hidden name="functionId" />
				<x:hidden name="scopeOrgFullId" />
				<table class='tableInput' style="width: 99%;">
					<x:layout proportion="15%,21%,12%,21%,15%" />
					<tr>
						<x:inputTD name="organName" disabled="true" label="公司名称"
							maxLength="64" />
						<x:inputTD name="billCode" disabled="true" label="单据号码"
							maxLength="32" />
						<x:inputTD name="fillinDate" disabled="true" label="填表日期"
							maxLength="7" wrapper="dateTime" />
					</tr>
					<tr>
						<x:inputTD name="deptName" disabled="true" label="部门"
							maxLength="32" />
						<x:inputTD name="personMemberName" disabled="true" label="姓名"
							maxLength="32" />
						<x:inputTD name="functionName" required="true" label="检查功能"
							wrapper="select" />
					</tr>
					<tr>
						<x:inputTD name="scopeOrgFullName" required="true" label="检查组织范围"
							colspan="5" wrapper="select" />
					</tr>
					<tr style="display: none" id="handlerTr">
						<td class='title'><span class="labelSpan"> 处理人&nbsp;:
								<a href='javascript:void(0);' class="addLink" id="chooseHandler"
								onclick='showChooseHandlerDialog()'>选择</a>&nbsp;&nbsp; <a
								href='javascript:void(0);' class="clearLink" id="clearHandler"
								onclick='clearChooseArray()'>清空</a>
						</span></td>
						<td class="title" colspan="5"><div id="handlerDiv"
								style="min-height: 25px; line-height: 25px;"></div></td>
					</tr>
					<tr>
						<td class='title'><span class="labelSpan"> 检查权限&nbsp;:
						</span></td>
						<td class="title" colspan="5"><div id="checkPermission"
								style="min-height: 25px; line-height: 25px;">
								<a href="javascript:void(null);" class="tLink" kindId="fun">按功能查询权限</a>&nbsp;&nbsp; <a
									href="javascript:void(null);" class="tLink" kindId="org">按组织查询权限</a>
							</div></td>
					</tr>
					<tr>
						<x:textareaTD name="description" label="描述" maxLength="512"
							width="744" rows="3" colspan="5">
						</x:textareaTD>
					</tr>
				</table>
				<div class="blank_div"></div>
				<x:fileList bizCode="PermissionCheck" bizId="id"
					id="permissionCheckFileList" />
			</form>
		</div>
	</div>
</html>
