<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="approvalRuleId" />

	<div id='tabPage'>
		<div class="ui-tab-links">
			<ul>
				<li id="baseInfo" divid="baseInfoDiv">基本信息</li>
				<li id="assistant" divid="assistantDiv">协办人列表</li>
				<li id="ccInfo" divid="ccDiv">抄送人列表</li>
				<li id="uiElementPermission" divid="uiElementPermissionDiv">字段权限</li>
			</ul>
		</div>
		<div class="ui-tab-content" style="border: 0; padding: 2px;">
			<div id="baseInfoDiv" class="layout">
				<div class="hg-form-row">
					<x:selectC name="allowAdd" label="允许加减签" dictionary="yesorno"
						emptyOption="false" value="1" fieldCol="4" />
					<x:selectC name="allowAbort" label="允许中止" dictionary="yesorno"
						emptyOption="false" value="1" fieldCol="4" />
				</div>
				<div class="hg-form-row">
					<x:selectC name="allowTransfer" label="允许转交" dictionary="yesorno"
						emptyOption="false" value="1" fieldCol="4" />
					<x:inputC name="helpSection" label="审批要点" fieldCol="4" />
				</div>
				<div class="hg-form-row">
					<x:selectC name="needTiming" label="是否计时" dictionary="yesorno"
						emptyOption="false" value="1" fieldCol="4" />
					<x:inputC name="limitTime" label="限制时间" fieldCol="4" />
				</div>
				<div class="hg-form-row">
					<x:selectC name="sendMessage" label="发送消息" dictionary="yesorno"
						emptyOption="false" value="0" fieldCol="4" />
					<x:selectC name="taskExecuteMode" list="taskExecuteModes"
						label="任务执行模式" emptyOption="true" fieldCol="4" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="limitHandler" label="最少审批人数" spinner="true" mask="nnn" fieldCol="4" />
					<x:inputC name="groupId" spinner="true" mask="nnn"
						dataOptions="min:1" required="true" label="审批分组" fieldCol="4" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="sequence" spinner="true" mask="nnn"
							  dataOptions="min:1" required="true" label="审批序号" fieldCol="4" />
				</div>
			</div>
			<div id="assistantDiv" class="layout" style="margin: 2px;">
				<div id="assistantGrid" style="margin-top: 2px;"></div>
			</div>
			<div id="ccDiv" class="layout" style="margin: 2px;">
				<div id="ccGrid" style="margin-top: 2px;"></div>
			</div>
			<div id="uiElementPermissionDiv" class="layout" style="margin: 2px;">
				<div id="uiElementPermissionGrid" style="margin-top: 2px; width: 100%"></div>
			</div>
		</div>
	</div>
</form>