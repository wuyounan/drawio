<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime" />
<script src='<c:url value="/system/bpm/CounterSignQuery.js"/>'  type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<div id="layout">
				<div position="center" style="padding:3px;">
					<div class="blank_div"></div>
					<div id="maingrid"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>