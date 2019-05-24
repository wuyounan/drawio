<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" id="codeBuildRuleId" />
	<x:inputC name="code" required="true" match="englishCode" label="编码" labelCol="3"  fieldCol="9" maxLength="32" />
	<x:inputC name="name" required="true" label="名称" labelCol="3"  fieldCol="9" maxLength="32" />
	<x:inputC name="rule" required="false" label="编码规则" labelCol="3"  fieldCol="9" maxLength="96" />
	<x:inputC name="currentValue" required="false" label="当前值" labelCol="3"  fieldCol="9" match="number"/>
</form>
