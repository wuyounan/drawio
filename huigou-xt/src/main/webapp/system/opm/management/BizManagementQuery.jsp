<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,grid,tree" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/management/BizManagementQuery.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/management/BizManagementOrgFilterCondition.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="组织机构树">
				<c:import url="BizManagementOrgFilterCondition.jsp" />
				<x:title title="组织机构" name="group" />
				<div id="orgTreeWrapper">
					<ul id="orgTree"></ul>
				</div>
			</div>
			<div position="center" title="权限管理" id="bizManagementList">
				<div id="bizManagementType" style="margin: 2px;"></div>
			</div>
			<div position="right" title="权限列表">
				<div style="margin: 2px; margin-top: 5px;">
					<div id="maingrid"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>