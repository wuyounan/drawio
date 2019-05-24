<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,dateTime,tree,commonTree,comboDialog" />
<script src='<c:url value="/system/opm/organization/PersonQuery.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="组织机构树" id="mainmenu">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="组织机构列表">
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<div class="hg-form-row">
						<x:inputC name="code" id="code" label="编码" labelCol="1" />
						<x:inputC name="name" id="name" label="名称" labelCol="1" />
						<x:searchButtons />
					</div>
					<div class="hg-form-row">
						<x:checkbox name="showDisabledOrg" id="showDisabledOrg" cssStyle="margin-left:10px;" label="显示已禁用的组织" />
						<x:checkbox name="showAllChildrenOrg" label="显示所有下级组织" cssStyle="margin-left:10px;" />
					</div>
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>