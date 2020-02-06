<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,grid,combox" />
<script src='<c:url value="/biz/mcs/baseinfo/index/indexClassificationDim.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
		<form class="hg-form ui-hide" method="post" action=""
			id="queryMainForm">
			<x:inputC name="code" label="编码" labelCol="1" />
			<x:inputC name="name" label="名称" labelCol="1" />
			<x:searchButtons />
		</form>
		<div class="blank_div"></div>
		<div id="maingrid" style="margin: 2px;"></div>
	</div>
</body>
</html>
