<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="opdatamanageFieldForm">
	<x:hidden name="id"/>
	<x:hidden name="datamanagebusinessId"/>
	<x:hidden name="isOrgCondition" id="pageIsOrgCondition"/>
	<div class="hg-form-row">
		<x:inputC name="dataKindName" required="true" label="资源名称" maxLength="32" labelCol="2" fieldCol="4" wrapper="select"/>
		<x:inputC name="dataKindCode" readonly="true" label="资源编码" maxLength="32" labelCol="2" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="tableColumn" required="true" label="字段名" maxLength="32" labelCol="2" fieldCol="4" match="englishCode"/>
		<x:inputC name="tableAlias" required="true" label="数据库别名" maxLength="32" labelCol="2" fieldCol="4" match="englishCode"/>
	</div>
	<div class="hg-form-row">
		<x:selectC name="columnSymbol" list="DataFieldSymbolKinds" label="操作符" required="true"  labelCol="2" fieldCol="4"/>
		<x:selectC name="columnDataType" list="DataTypeKinds" label="数据类型" required="true"  labelCol="2" fieldCol="4"/>
	</div>
	<div class="hg-form-row">
		<x:textareaC name="formula" required="false" label="拼接模板" maxLength="120" rows="2" labelCol="2" fieldCol="10" />
	</div>
</form>
