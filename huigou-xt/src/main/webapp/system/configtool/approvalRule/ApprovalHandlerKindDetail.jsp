<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:inputC name="code" required="true" label="编码" maxLength="32" labelCol="3" fieldCol="9" />
	<x:inputC name="name" required="true" label="名称" maxLength="64" labelCol="3" fieldCol="9"/>
	<x:selectC name="dataSourceId" list="dataSourceList" required="true" emptyOption="false" label="数据源" labelCol="3" fieldCol="9"/>
	<x:textareaC name="dataSourceConfig"  label="数据源配置" rows="5" labelCol="3" fieldCol="9"/>
	<x:inputC name="sequence"  label="排序号" maxLength="22" spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="9"/>
</form>
