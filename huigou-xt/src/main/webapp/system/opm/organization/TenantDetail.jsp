<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="folderId" />
	<x:hidden name="tenantId" />

	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" maxLength="32"
			labelCol="2" fieldCol="4" />
		<x:inputC name="name" required="true" label="名称" maxLength="64"
			labelCol="2" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="contacts" required="true" label="联系人" maxLength="32"
			labelCol="2" fieldCol="4" />
		<x:inputC name="contactNumber" required="true" label="联系电话"
			maxLength="128" match="phone" labelCol="2" fieldCol="4" />
	</div>

	<div class="hg-form-row">
		<x:selectC name="isIndustry" required="true" label="是否行业"
			dictionary="yesorno" emptyOption="true" labelCol="2" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="description" label="描述" rows="3" labelCol="2"
			fieldCol="10" />
	</div>
</form>
