<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/permission/UIElementPermission.js"/>' type="text/javascript"></script>
</head>
<body>
    <div style ="display:none;" >
		<x:select name="uiElementOperation" cssStyle="display:none;" list="uiElementOperations"/>
		<x:select name="kindId" cssStyle="display:none;" dictionary="fieldType"/>
	</div>
	<div class="container-fluid" style="padding-top: 10px;">
		<div id="maingrid" style="margin: 2px;"></div>
	</div>
</body>
</html>
