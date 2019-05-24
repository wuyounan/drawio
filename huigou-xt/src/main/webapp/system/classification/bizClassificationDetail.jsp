<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="sequence" />
	<x:hidden name="parentId" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" maxLength="32"
			labelCol="1" fieldCol="3" />
		<x:inputC name="name" required="true" label="名字" maxLength="32"
			labelCol="1" fieldCol="7" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="description" label="描述" maxLength="128" rows="2"
			labelCol="1" fieldCol="11" />
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="classificationDetalGrid" style="margin-top: 2px;"></div>