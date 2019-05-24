<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<div class="hg-form-row">
		<x:hidden name="id" />
		<x:inputC name="code" required="true" label="编码" maxLength="32" labelCol="2" fieldCol="4" />
		<x:inputC name="name" required="true" label="名称" maxLength="32" labelCol="2" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="tableName" label="中间表名称" maxLength="32" labelCol="2" fieldCol="4" />
		<x:inputC name="procedureName" label="Java组件名" maxLength="64" labelCol="2" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" label="导入说明" maxLength="250" labelCol="2" fieldCol="10" rows="3"/>
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="detailGrid" style="margin-top: 2px;"></div>