<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,combox,tree,commonTree,attachment" />
<script src='<c:url value="/system/excelimport/AssignCodeImp.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/excelimport/ExcelImportTemplate.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="模板分类">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="模板列表">
				<x:hidden name="parentId" id="treeParentId" />
				<x:title title="模板表" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:hidden name="id" />
					<x:inputC name="code" label="编码" maxLength="32" labelCol="1" />
					<x:inputC name="name" label="名称" maxLength="32" labelCol="1" />
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
