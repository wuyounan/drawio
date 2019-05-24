<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="id" />
	<x:hidden name="clientId" />
	<x:hidden name="agentId" />
	<x:hidden name="createdDate" type="datetime" />
	<div class="hg-form-row">
		<x:inputC name="clientName" required="true" label="委托人" wrapper="select" fieldCol="4" />
		<x:inputC name="agentName" required="true" label="代理人" wrapper="select" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="startDate" required="true" label="开始时间" wrapper="dateTime" fieldCol="4" />
		<x:inputC name="endDate" required="true" label="结束时间" wrapper="dateTime" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="agentStatus" name="status" value="1" label="状态" fieldCol="4" />
		<x:selectC dictionary="procAgentKind" name="procAgentKindId" value="2" label="代理方式" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" label="备注" rows="2" fieldCol="10" />
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="detailGrid" style="margin-top: 2px;"></div>