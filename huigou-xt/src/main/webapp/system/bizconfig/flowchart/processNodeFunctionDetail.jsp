<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="saveNodeFunctionForm">
	<x:hidden name="id"/>
	<x:hidden name="viewId"/>
	<x:hidden name="businessProcessId"/>
	<x:inputC name="code" required="true" match="englishCode" label="编码" labelCol="3" fieldCol="9" maxLength="32" />
	<x:inputC name="name" required="true" label="名称" labelCol="3"  fieldCol="9" maxLength="32" />
	<x:inputC name="icon" required="true" match="englishCode" label="图标" labelCol="3" fieldCol="9" maxLength="32" />
	<x:inputC name="url" required="false" label="URL" labelCol="3"  fieldCol="9" maxLength="128" />
	<x:inputC name="param" required="false" label="参数" labelCol="3"  fieldCol="9"  maxLength="128" />
</form>

