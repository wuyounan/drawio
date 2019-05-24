<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<title>流程图DEMO</title>
	<!--[if lt IE 9]>
		<?import namespace="v" implementation="#default#VML" ?>
	<![endif]-->
	<x:base include="base,dialog,grid,combox,layout" />
	<link href='<c:url value="/lib/flowChart/css/flowChart.css"/>' rel="stylesheet" type="text/css" />
	<link href='<c:url value="/themes/blue/blue.css"/>' rel="stylesheet" type="text/css" />
	<!--<script type="text/javascript" src='<c:url value="/lib/flowChart/js/html2canvas.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/js/canvas2image.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/canvg/canvg.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/canvg/rgbcolor.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/lib/flowChart/canvg/StackBlur.js"/>'></script>-->
    <script type="text/javascript" src='<c:url value="/lib/jquery/jquery.contextmenu.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/js/flowChart.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/js/flowChartFunc.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/jquery.flowChart.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/lib/flowChart/demo.js"/>'></script>
	</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<div id="layout">
				<div position="center"  id="chartCenter" style="overflow:hidden;border:0px;">
					<div id="flowChartDiv" style="border:0px;"></div>
				</div>
			</div>
		</div>
	</div>
	<div id="inputDiv" style="display:none">
		<form method="post" action="" id="addNodeForm">
			<div class="ui-form" style="width:400px">
				<x:radioL name="nodeKind" required="true" label="节点类型" labelWidth="80" width="160" list="flowNodeKind"/>
				<div class="row ruleKindDiv">
					<x:radioL name="ruleKind" required="false" label="规则类型" labelWidth="80" width="190" list="flowRuleKind"/> 
				</div>
				<div class="row">
					<x:radioL name="yesorno" required="false" label="插入节点" labelWidth="80" width="130" dictionary="yesorno"/> 
				</div>
				<x:inputL name="name" required="true" label="名称" maxLength="50" labelWidth="80" width="265"/>
				<div class="row axisDiv">
					<x:inputL name="xaxis" required="false" label="横坐标" maxLength="16" width="80" labelWidth="80"/>	
					<x:inputL name="yaxis" required="false" label="纵坐标" maxLength="16" width="80" labelWidth="80"/>	
				</div>
				<div class="clear"></div>
				<div class="row prompt" style="text-align:left;color:#FE8463;"></div>
				<div class="clear"></div>
				<dl style="text-align: right;width:400px">
					<x:button value="确 定" id="addNode"/>
					&nbsp;&nbsp;
					<x:button value="插入行" id="addYaxis"/>
					&nbsp;&nbsp;
					<x:button value="插入列" id="addXaxis"/>
					&nbsp;&nbsp;
					<x:button value="取 消" id="cancelDialog" />
					&nbsp;&nbsp;
				</dl>
			</div>
		</form>
	</div>
</body>
</html>