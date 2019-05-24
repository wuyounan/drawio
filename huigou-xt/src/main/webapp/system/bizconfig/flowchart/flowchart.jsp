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
	<x:script src='/system/opm/js/OpmUtil.js'/>
	<x:script src='/system/bizconfig/common/BPMCUtil.js'/>
	<x:script src='/system/bizconfig/flowchart/nodeDetail.js'/>
	<x:script src='/system/bizconfig/flowchart/flowchart.js'/>
	</head>
	<body class="dom-overflow">
		<input type="hidden" id="csrfTokenElement" value="<%=token%>"/>
		<div class="container-fluid no-padding">
			<x:hidden name="businessProcessId"/>
			<x:hidden name="chartDirection"/>
			<div id="layout">
				<div position="center" class="no-padding dom-overflow">
					<div id="flowChartDiv" class="ui-hide" style="border:0px;margin-top:3px;"></div>
				</div>
				<div position="right" title="节点列表" id="nodeInfoDiv">
					<div id="queryKindCodeSpan" class="m-xxs">
						<label class="radio-inline"><input type="radio" name="queryKindCode" value="all" checked="true">&nbsp;全部</label>
    					<label class="radio-inline"><input type="radio" name="queryKindCode" value="activity">&nbsp;活动</label>
    					<label class="radio-inline"><input type="radio" name="queryKindCode" value="rule">&nbsp;规则</label>
    					<label class="radio-inline"><input type="radio" name="queryKindCode" value="interface">&nbsp;接口</label>
    					<label class="radio-inline"><input type="radio" name="queryKindCode" value="shadow">&nbsp;影子</label>
					</div>
					<div id="nodeListGrid" style="margin-top:2px;"></div>
				</div>
			</div>
		</div>
		<div id="inputDiv" class="ui-hide">
			<form method="post" action="" class="hg-form">
				<div class="hg-form">
					<x:radioC name="objectKindCode" required="true" label="节点类型" labelCol="3" fieldCol="9" list="flowNodeKind"/>
					<div class="hg-form-row ruleKindDiv" style="display:none;">
						<x:radioC name="ruleKind" required="false" label="规则类型" labelCol="3" fieldCol="9" list="flowRuleKind"/> 
					</div>
					<div class="hg-form-row interfaceKindDiv" style="display:none;">
						<x:radioC name="interfaceKind" required="false" label="接口类型" labelCol="3" fieldCol="9" list="flowInterfaceKind"/> 
					</div>
					<div class="hg-form-row">
						<x:inputC name="code" required="true" label="序号" maxLength="4" labelCol="3" fieldCol="3"/>
						<x:radioC name="yesorno" required="false" label="插入" labelCol="2" fieldCol="4" dictionary="yesorno"/> 
					</div>
					<div class="hg-form-row nameViewDiv">
						<x:inputC name="name" required="false" label="名称" maxLength="16" labelCol="3" fieldCol="9"/>
					</div>
					<div class="hg-form-row ownerViewDiv">
						<x:hidden name="ownerId"/>
				   	    <x:inputC name="ownerName" required="false" label="所有者" maxLength="64" labelCol="3" fieldCol="9"/>
					</div>
					<div class="hg-form-row quoteNameDiv" style="display:none">
						<x:inputC name="quoteId" required="true" label="引用节点" maxLength="16" labelCol="3" fieldCol="9" />
					</div>
					<div class="hg-form-row axisDiv">
						<x:inputC name="xaxis" required="false" label="横坐标" maxLength="16" labelCol="3" fieldCol="3"/>	
						<x:inputC name="yaxis" required="false" label="纵坐标" maxLength="16" labelCol="3" fieldCol="3"/>	
					</div>
					<div class="clearfix"></div>
					<div class="hg-form-row prompt" style="text-align:left;color:#FE8463;"></div>
					<div class="clearfix"></div>
					<div class="col-xs-12" style="text-align:right">
						<x:button value="确 定" id="addNode"/>&nbsp;&nbsp;
						<x:button value="插入行" id="addYaxis"/>&nbsp;&nbsp;
						<x:button value="插入列" id="addXaxis"/>&nbsp;&nbsp;
						<x:button value="取 消" id="cancelDialog" />&nbsp;&nbsp;
					</div>
				</div>
			</form>
		</div>
	</body>
</html>