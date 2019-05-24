<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:inputC name="code" required="true" label="编码"
		fieldCol="10" />
	<x:inputC name="name" required="true" label="名称"
		fieldCol="10" />
	<x:inputC name="classPrefix" required="true" label="类前缀"
		fieldCol="10" />
	<x:inputC name="sequence" label="排序号" readonly="false" spinner="true"
		mask="nnn" dataOptions="min:1" fieldCol="10" />
</form>