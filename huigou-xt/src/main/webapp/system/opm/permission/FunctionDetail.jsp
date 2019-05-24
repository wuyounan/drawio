<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="parentId" />
	<x:inputC name="code" required="true" label="编码" maxlength="64"  fieldCol="10" />
	<x:inputC name="name" required="true" label="名称" maxlength="64"  fieldCol="10" />
	<x:inputC name="description" label="描述" maxlength="64"  fieldCol="10" />
	<x:selectC name="nodeKindId" required="true" list="nodeKindList" label="类型"  fieldCol="10" />
	<x:inputC name="url" label="Url" readonly="false" maxlength="128"  fieldCol="10" />
	<div class="col-xs-2 col-md-2">
		<label class="hg-form-label required" id="code_label">图标&nbsp;:</label>
	</div>
	<div class="col-xs-8 col-md-8">
		<x:input name="icon" label="图标" required="true"/>
	</div>		
	<div class="col-xs-2 col-md-2">
		<span style="padding:10px;font-size:26px;" class="show-icon"></span>
	</div>
	<div class="clear"></div>
	<x:inputC name="sequence" label="排序号" spinner="true" mask="nnn" dataOptions="min:1"  fieldCol="10" />
	<x:textareaC name="remark" label="备注" rows="2"  fieldCol="10"/>
</form>