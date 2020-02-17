
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" match="englishCode"
			maxLength="64"  fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" maxLength="32" fieldCol="10" />
	</div>
	<div class="hg-form-row">
	    <x:radioC name="isDefault" label="默认" dictionary="yesorno" fieldCol="10"/>
	</div>
	<div class="hg-form-row">
		<x:inputC name="sequence" label="排序号" spinner="true" mask="nnn" dataOptions="min:1"  fieldCol="10" />
	</div>
</form>