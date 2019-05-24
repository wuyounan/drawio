<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id"/>
	<x:inputC name="code" required="true" label="函数" match="englishCode" maxLength="64" labelCol="2" fieldCol="10"/>
	<x:inputC name="name" required="true" label="名称" maxLength="64"  labelCol="2" fieldCol="10"/>
	<x:selectC name="isLast" required="true" dictionary="yesorno" label="末级"  labelCol="2" fieldCol="10"/>
	<x:textareaC name="remark" label="备注" maxLength="256" labelCol="2" fieldCol="10" rows="3"/>
</form>
