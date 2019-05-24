<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="hg-form" method="post" action="" id="submitForm">
    <x:hidden name="id"/>
    <x:inputC name="code" required="true" label="common.field.code" maxLength="32" labelCol="2" fieldCol="10"/>
    <x:inputC name="name" required="true" label="common.field.name" maxLength="32" labelCol="2" fieldCol="10"/>
    <x:selectC name="dataKind" required="true" label="数据类型" list="DataResourceKinds" labelCol="2" fieldCol="10"/>
    <x:textareaC name="dataSource" required="false" rows="4" label="数据源" maxLength="200" labelCol="2" fieldCol="10"/>
    <x:textareaC name="remark" required="false" rows="2" label="备注" maxLength="128" labelCol="2" fieldCol="10"/>
</form>
