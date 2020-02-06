<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="folderId" />
	<x:hidden name="sequence" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" maxLength="64" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" maxLength="128" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="Status" name="status" value="1" label="状态" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="json" label="json" maxLength="2000" rows="7" fieldCol="10" />
	</div>
</form>
