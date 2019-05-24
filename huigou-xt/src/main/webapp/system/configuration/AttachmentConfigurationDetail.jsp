<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" match="englishCode"  maxLength="32" labelCol="1" fieldCol="3"/>
		<x:inputC name="name" required="true" label="名称" maxLength="32" labelCol="1" fieldCol="3"/>
		<x:radioC name="allowDelete" label="可删除" dictionary="yesorno"
			value="1" labelCol="2" fieldCol="2"/>
	</div>
	<div class="hg-form-row">
		<x:inputC name="remark" required="false" label="备注" maxLength="128" labelCol="1" fieldCol="11"/>
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="attachmentConfigDetailGrid" style="margin: 2px;"></div>