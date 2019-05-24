<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="grid" />
<script src='<c:url value="/system/bpm/BackQuery.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div class="blank_div"></div>
		<div id="maingrid"></div>
		<div class="blank_div"></div>
		<div class="row-fluid">
			<input type="radio" name="backModel" value="back" checked="checked" />&nbsp;回退：回退到指定节点，节点后面的审批人需重新审批。
		</div>
		<div class="row-fluid <c:if test="${canReplenish=='false'}">hide</c:if>">
			<input type="radio" name="backModel" value="replenish" />&nbsp;打回：仅修改文档，修改完成后回到本节点。
		</div>
		<div class="blank_div" style="margin:5px;"></div>
	</div>
</body>
</html>