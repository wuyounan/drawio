<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/uielement/UIElement.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="界面元素分类树">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="界面元素列表">
				<x:hidden name="parentId" id="treeParentId" />
				<div style="display:none;">
					<x:select name="uiElementOperation" cssStyle="display:none;" list="uiElementOperations" />
				</div>
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form" method="post" action="" id="queryMainForm">
					<x:inputC name="code" label="编码" maxLength="32" labelCol="1" />
					<x:inputC name="name" label="名称" maxLength="32" labelCol="1" />
					<x:selectC name="kindId" label="类别" dictionary="fieldType" labelCol="1" />
					<x:selectC name="status" label="状态" dictionary="uiElementStatus" labelCol="1" />
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
