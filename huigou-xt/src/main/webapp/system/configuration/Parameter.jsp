<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox,date,commonTree" />
<x:i18n name="pages"/>
<script src='<c:url value="/system/configuration/Parameter.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title='<x:message key="system.parameter.treetitle"/>'>
				<ul id="maintree"></ul>
			</div>
			<div position="center" title='<x:message key="system.parameter.gridtitle"/>'>
				<x:hidden name="parentId" id="treeParentId" />
				<x:hidden name="id" />
				<x:title title="common.button.search" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:inputC name="code" label="common.field.code" maxLength="32" labelCol="1"/>
					<x:inputC name="name" label="common.field.name" maxLength="32" labelCol="1" />
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
