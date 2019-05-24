<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,grid,combox,layout,tree,commonTree" />
<script src='<c:url value="/system/remind/MessageRemind.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="layout">
			<div position="left" title="消息提醒类别">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="消息提醒列表">
				<x:hidden name="parentId" id="treeParentId" />
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:inputC name="code" label="编码" maxLength="32" labelCol="1" />
					<x:inputC name="name" label="名称" maxLength="64" labelCol="1" />
					<x:inputC name="remindTitle" label="标题" maxLength="50" labelCol="1" />
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
