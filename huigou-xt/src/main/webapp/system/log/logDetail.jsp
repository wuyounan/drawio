<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" id="id" />
	<x:hidden name="statusId" id="statusId" />
	<div class="hg-form-row">
		<x:inputC name="appName" disabled="true" label="系统" />
		<x:inputC name="mac" disabled="true" label="MAC" />
		<x:inputC name="ip" disabled="true" label="IP" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="beginDate" disabled="true" wrapper="dateTime"
			label="操作时间" />
		<x:inputC name="statusName" disabled="true" label="操作状态" />
		<x:inputC name="personMemberName" id="personMemberName"
			disabled="true" label="操作员" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="personSecurityLevelName" disabled="true" label="人员密级"/>
		<x:inputC name="resourceSecurityLevelName" disabled="true"
			label="资源密级" />
		<x:inputC name="machineSecurityLevelName" disabled="true" label="机器密级" />
	</div>
	<div class="hg-form-row">
		<x:selectC name="logType" list="logTypeList" disabled="true"
			label="日志类型" />
		<x:selectC name="operateName" list="operationNameList" disabled="true"
			label="操作类型" />
		<x:inputC name="methodName" disabled="true" label="方法名" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="fullName" disabled="true" label="组织" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="className" disabled="true" label="类名" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="params" disabled="true" label="参数" rows="5"
			fieldCol="10" />
	</div>
	<div class="hg-form-row" id="error">
		<x:textareaC name="errorMessage" disabled="true" label="错误信息" rows="5"
			fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="description" disabled="true" label="摘要" rows="5"
			fieldCol="10" />
	</div>
	<div class="hg-form-row" id="oldImage">
		<x:textareaC name="beforeImage" disabled="true" label="修改前" rows="7"
			fieldCol="10" />
	</div>
	<div class="hg-form-row" id="newImage">
		<x:textareaC name="afterImage" disabled="true" label="修改后" rows="7"
			fieldCol="10" />
	</div>
</form>