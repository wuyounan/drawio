<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="grid" />
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/bpm/ApprovalHistory.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel" style="margin:5px;">
		<div id="maingrid"></div>
	</div>
</body>
</html>