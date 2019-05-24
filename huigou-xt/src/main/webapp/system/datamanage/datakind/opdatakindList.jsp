<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <x:base include="dialog,grid,dateTime,combox"/>
    <x:script src="/system/datamanage/datakind/opdatakindList.js"/>
</head>
<body>
<div class="container-fluid">
    <x:title title="common.button.search" hideTable="queryMainForm" isHide="true" />
	<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
		<x:inputC name="code" label="common.field.code" maxLength="32" labelCol="1"/>
		<x:inputC name="name" label="common.field.name" maxLength="32" labelCol="1" />
		<x:selectC name="dataKind" label="数据类型" list="DataResourceKinds" labelCol="1"/>
		<x:searchButtons />
	</form>
    <div id="maingrid"></div>
</div>
</body>
</html>
