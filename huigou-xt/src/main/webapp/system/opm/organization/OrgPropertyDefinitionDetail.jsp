<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:selectC name="orgKindId" list="orgKindList" required="true" label="类别" fieldCol="10">
	</x:selectC>
	<x:inputC name="name" required="true" label="属性" match="englishCode" maxLength="32"   fieldCol="10" />
	<x:inputC name="description" required="true" label="描述" maxLength="32"  fieldCol="10" />
	<x:textareaC name="dataSource" label="数据源" rows="4"  fieldCol="10"></x:textareaC>
	<x:inputC name="sequence" label="排序号" maxLength="22" spinner="true" mask="nnn" dataOptions="min:1"  fieldCol="10" />
</form>
