<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/biz/mcs/report/cubesviewer/cubesViewerDefinition.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="类别" id="mainmenu">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="cubesViewer列表">
				<x:hidden name="folderId" />
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:inputC name="code" label="编码" maxLength="64" labelCol="1" />
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
