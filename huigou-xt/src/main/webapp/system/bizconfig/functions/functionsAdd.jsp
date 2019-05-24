<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
		<x:hidden name="id"/>
		<x:inputC name="code" required="true" label="common.field.code" maxLength="32" labelCol="3" fieldCol="9" match="englishCode"/>
		<x:inputC name="name" required="true" label="common.field.name" maxLength="32" labelCol="3" fieldCol="9"/>
	    <x:inputC name="checkBeanBame" required="false" label="权限校验Bean" maxLength="64" labelCol="3" fieldCol="9"  match="englishCode"/>
		<x:inputC name="remark" required="false" label="备注" maxLength="128" labelCol="3" fieldCol="9" />
</form>
