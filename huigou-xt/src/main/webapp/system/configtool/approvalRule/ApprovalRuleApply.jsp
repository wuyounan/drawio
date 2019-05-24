<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configtool/approvalRule/ApprovalRuleApply.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configtool/approvalRule/ApprovalRuleUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<div id="layout">
				<div position="left" title="组织机构树" id="mainmenu">
					<div style="overflow-x: hidden; overflow-y: auto; width: 100%;" id="divTreeArea">
						<ul id="maintree">
						</ul>
					</div>
				</div>
				<div position="center" id="layoutApprovalRule" style="margin: 2px; margin-right: 3px;">
					<div id="elementGrid" style="clear: both; margin-top: 2px;"></div>
					<div style="margin-top: 10px;">
						<x:button value="获取处理人" id="btnGetHandler"/>
						<div style="margin-top: 10px; height: 30px;">
							<strong>审批规则：</strong><span id="approvalRuleName"></span>
						</div>
					</div>
					<div id="handlerGrid" style="clear: both; margin-top: 2px;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
