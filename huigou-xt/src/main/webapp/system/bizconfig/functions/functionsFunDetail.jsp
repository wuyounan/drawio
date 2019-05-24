<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id"/>
	<x:inputC name="code" required="true" label="编码" maxLength="32" labelCol="3" fieldCol="9" match="englishCode" wrapper="select"/>
	<x:inputC name="nameZh" required="true" label="中文描述" maxLength="32" labelCol="3" fieldCol="9" />
	<x:inputC name="nameEn" required="fasle" label="英文描述" maxLength="64" labelCol="3" fieldCol="9" match="englishCode"/>
	<x:selectC list="functionColorList" name="color" required="true" label="颜色" labelCol="3" fieldCol="9"/>
	<x:inputC name="url" required="fasle" label="连接" maxLength="128" labelCol="3" fieldCol="9"/>
</form>
