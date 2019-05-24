<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/bpm/BpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configtool/approvalRule/ApprovalRule.js?a=1"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configtool/approvalRule/ApprovalRuleUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configtool/approvalRule/ApprovalHandlerDetailConfig.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/organization/SelectOrgCommonPage.js?a=1"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="top">
				<div class="ui-hide" id="treeHideDiv">
					<ul id="orgTree"></ul>
					<ul id="maintree"></ul>
				</div>
				<div class="hg-form">
					<div class="hg-form-row">
						<x:inputC name="orgName" required="true" label="组织机构" labelCol="1" fieldCol="4"/>
						<div class="col-xs-12 col-sm-1">
							<x:button value="选择" icon="fa-outdent" id="chooseOrgDialog" />
						</div>
						<x:inputC name="procName" required="true" label="流程" labelCol="1" fieldCol="4"/>
						<div class="col-xs-12 col-sm-1">
							<x:button value="选择" icon="fa-outdent" id="chooseProcDialog" />
						</div>
					</div>
				</div>
			</div>
			<div position="left" title="流程审批规则树" id="divApprovalRuleTreeArea">
				<ul id="approvalRuleTree"></ul>
			</div>
			<div position="center">
				<x:hidden name="approvalRuleId" />
				<div id="toolBar" style="padding-top:5px;padding-left:5px;"></div>
				<x:title name="group" title="审批规则" hideTable="#detailInfo" id="approvalRuleTitle" isHide="true"/>
				<div class="hg-form" id="queryTable">
					<div class="hg-form-row">
						<x:radioC name="nodeKindId" list="nodeKindList" id="approvalRuleNodeKindId" label="类别" labelCol="2" fieldCol="2" disabled="true"/>
						<x:radioC name="status" list="statusList" id="approvalRuleStatus" label="状态" labelCol="2" fieldCol="2" disabled="true"/>
						<x:inputC name="priority" id="approvalRulePriority" label="优先级" readonly="true" labelCol="2" fieldCol="2" />
					</div>
					<div id="detailInfo" class="ui-hide">
						<div class="hg-form-row">
							<x:inputC name="createTime" label="创建时间" readonly="true" mask="dateTime" />
							<x:inputC name="creatorName" label="创建人" readonly="true" />
							<x:inputC name="lastUpdateTime" label="修改时间" readonly="true" mask="dateTime" />
						</div>
						<div class="hg-form-row">
							<x:inputC name="lastUpdaterName" label="修改人" readonly="true" />
							<x:radioC dictionary="scopeKind" name="scopeKindId" label="适用范围"  disabled="true" fieldCol="3"/>
							
						</div>
						<div class="hg-form-row">
							<x:textareaC name="remark" required="false" id="approvalRuleRemark" label="备注" rows="2" readonly="true" labelCol="2" fieldCol="10"/>
						</div>
					</div>
				</div>
				<div class="blank_div clearfix"></div>
				<div id='approvalTabPage'>
					<div class="ui-tab-links">
						<ul>
							<li id="elementTab">审批要素</li>
							<li id="handlerTab">审批人列表</li>
						</ul>
					</div>
					<div class="ui-tab-content" style="border: 0; padding: 2px;">
						<div class="layout">
							<div id="elementGrid" style="margin: 2px;"></div>
						</div>
						<div class="layout">
							<div id="handlerGrid" style="margin: 2px;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="ui-hide">
		<x:select name="fieldType" dictionary="fieldType" emptyOption="false" cssStyle="display:none;" />
		<x:select name="fieldAuthority" dictionary="fieldAuthority" emptyOption="false" cssStyle="display:none;" />
		<x:select list="operatorKindList" emptyOption="false" id="operatorKindId" cssStyle="display:none;" />
		<x:hidden name="procKey" id="approvalRuleProcKey" />
		<x:hidden name="procUnitId" id="approvalRuleProcUnitId" />
		<x:hidden name="procName" id="approvalRuleProcName" />
		<x:hidden name="procUnitName" id="approvalRuleProcUnitName" />
		<x:hidden name="name" id="approvalRuleName" />
	</div>
</body>
</html>
