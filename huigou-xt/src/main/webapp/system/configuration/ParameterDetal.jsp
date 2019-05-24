<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" id="parameterId" />
	<x:hidden name="folderId" />
	<x:hidden name="status" />

	<x:inputC name="code" required="true" label="编码" match="englishCode" maxLength="64" labelCol="2" fieldCol="10"/>
	<x:inputC name="name" required="true" label="名称" maxLength="64"  labelCol="2" fieldCol="10"/>
	<x:inputC name="value" required="true" label="参数值" maxLength="512" labelCol="2" fieldCol="10"/>
	<x:inputC name="remark" label="备注" maxLength="256" labelCol="2" fieldCol="10"/>
</form>
