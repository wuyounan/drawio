<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/management/BizManagement.js?v=1"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/management/BizManagementOrgFilterCondition.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/organization/SelectOrgCommonPage.js"/>' type="text/javascript"></script>	
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="typeId" id="typeIdParam"/>
		<x:hidden name="orgRootId" id="delegationOrgRootId"/>
		<x:hidden name="manageType" id="delegationManageType"/>
		<x:hidden name="queryManageCodes"/>
		<div id="layout">
			<div position="left" title="组织机构树">
				<c:import url="BizManagementOrgFilterCondition.jsp" />
				<x:title title="组织机构" name="group" />
				<div id="orgTreeWrapper">
					<ul id="orgTree"></ul>
				</div>
			</div>
			<div position="center" title="权限管理" id="bizManagementList">
				<div style="margin: 2px;margin-top:5px;">
					<div id="bizManagementType"></div>
				</div>
			</div>
			<div position="right" title="权限列表">
				<div style="margin: 2px;margin-top:5px;">
					<div id="maingrid"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>