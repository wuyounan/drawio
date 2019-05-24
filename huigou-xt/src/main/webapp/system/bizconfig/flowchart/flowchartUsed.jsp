<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="com.huigou.context.ContextUtil" %>
<%@ page import="com.huigou.util.Constants" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String token = (String)ContextUtil.getSession().getAttribute(Constants.CSRF_TOKEN);
%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta name="viewport" content="width=device-width, initial-scale=1"/>
	<meta http-equiv="expires" content="0">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<!--[if lt IE 9]>
		<?import namespace="v" implementation="#default#VML" ?>
	<![endif]-->
	<!--[if (gt IE 9)|!(IE)]><!--><x:link href="/themes/font-face.css"/><!--<![endif]-->
	<x:base include="base,layout,dialog,grid,combox,layout,tree,comboDialog,flexField,commonTree,formButton,floatdialog" />
	<!--[if lt IE 9]>
	<x:script src='/lib/bootstrap/html5shiv.min.js'/>
	<x:script src='/lib/bootstrap/respond.min.js'/>
	<![endif]-->
	<x:link href="/lib/flowChart/css/flowChart.css"/>
	<x:script src='/lib/jquery/jquery.contextmenu.js'/>
	<x:script src='/lib/flowChart/js/flowChart.js'/>
	<x:script src='/lib/flowChart/js/flowChartFunc.js'/>
	<x:script src='/lib/flowChart/jquery.flowChart.js'/>
	<x:script src='/system/bizconfig/flowchart/flowchartUsed.js'/>
	</head>
	<body class="dom-overflow">
		<input type="hidden" id="csrfTokenElement" value="<%=token%>"/>
		<div class="container-fluid no-padding">
			<x:hidden name="businessProcessId"/>
			<x:hidden name="chartDirection"/>
			<x:hidden name="queryParamUrl"/>
			<x:hidden name="focusNodeCode"/>
			<x:hidden name="taskId" id="taskProcessingId"/>
			<div id="layout">
				<div position="top">
					<div id="businessProcessRemark" class="p-xxs" style="color:#666666;text-indent:50px;">
						<c:out value="${processRemark}" escapeXml="false"/>
					</div>
				</div>
				<div position="center" class="no-padding dom-overflow">
					<div id="flowChartDiv" class="ui-hide" style="border:0px;margin-top:3px;"></div>
				</div>
				<c:if test="${isTaskProcessing}">
				<div position="bottom">
					<div id="bottomToolBar" style="margin: 10px;"></div>
				</div>
				</c:if>
			</div>
		</div>
	</body>
</html>