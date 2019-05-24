<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" maxLength="32"
			labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" maxLength="64"
			labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:radioC name="kindId" list="kindList" required="false" label="类别"
			labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="dataSourceConfig" required="false" label="数据源配置"
			rows="5" labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="sequence" required="false" label="排序号" spinner="true"
			mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
</form>
