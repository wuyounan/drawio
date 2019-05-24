<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" maxLength="32"
			labelCol="1" fieldCol="3" />
		<x:inputC name="name" required="true" label="名称" maxLength="32"
			labelCol="1" fieldCol="3" />
		<x:inputC name="sequence" required="true" label="序号" mask="nnnn"
			spinner="true" labelCol="1" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="remark" required="false" label="备注" maxLength="100"
			labelCol="1" fieldCol="11" />
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="userGroupDetailGrid"></div>