<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="folderId" />
	<x:inputC name="code" required="true" label="编码" match="englishCode" maxLength="64" labelCol="3" fieldCol="9" />
	<x:inputC name="name" required="true" label="名称" maxLength="32" labelCol="3" fieldCol="9" />
	<x:selectC name="kindId" required="true" label="类别" dictionary="fieldType" emptyOption="false" labelCol="3" fieldCol="9" />
	<x:inputC name="defaultOperationId" label="默认操作" labelCol="3" fieldCol="9" />
	<x:inputC name="sequence" required="true" label="排序号" spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="9" />
</form>
