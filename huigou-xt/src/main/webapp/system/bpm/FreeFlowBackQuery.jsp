<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime" />
<script src='<c:url value="/javaScript/UICtrl.js"/>' type="text/javascript"></script>
<script src='<c:url value="/javaScript/WEB_APP.js"/>' type="text/javascript"></script>
<script src='<c:url value="/javaScript/common.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/bpm/FreeFlowBackQuery.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<div class="blank_div"></div>
			<div id="maingrid"></div>
			<div class="blank_div"></div>
			<div>
				<table>
					<tr style="height: 30px;">
						<td>
							<input type="radio" name="backModel" value="replenish"  checked="checked" />
							<span title="打回：仅修改文档，修改完成后回到本节点。"><x:message key="common.job.back.replenish"/></span>
						</td>
					</tr>
					<tr style="height: 30px;">
						<td>
							<input type="radio" name="backModel" value="back"/>
							 <span title="回退：回退到指定节点，节点后面的审批人需重新审批。"><x:message key="common.job.back.back"/></span>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>