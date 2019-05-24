<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" labelCol="1" fieldCol="3" maxLength="32" />
		<x:inputC name="name" required="true" label="名称" labelCol="1" fieldCol="3" maxLength="32" />
		<x:radioC name="kindId" label="类别" dictionary="dictionaryKind" labelCol="1" fieldCol="3" value="1" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="remark" required="false" label="备注" maxLength="128" labelCol="1" fieldCol="11" />
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="dictDetalGrid" style="margin: 2px;"></div>