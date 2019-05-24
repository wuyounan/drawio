<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method=" post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" labelCol="3" match="englishCode" maxLength="64" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" labelCol="3"  maxLength="64" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:hidden name="bizManagementTypeId" id="bizManagementTypeId" />
		<x:inputC name="bizManagementTypeName" id="bizManagementTypeName"
			label="业务权限" readonly="true" labelCol="3" fieldCol="7" />
		<div class="col-xs-2 col-md-2">
			<x:button value="..." id="btnSelectBizManagementType" cssStyle="min-width:30px;" />
		</div>
	</div>
	<div class="hg-form-row">
		<x:inputC name="sequence" required="false" label="排序号" spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" required="false" label="备注" rows="3" labelCol="3" fieldCol="9"/>
	</div>
</form>