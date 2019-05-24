<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id"/>
	<x:hidden name="folderId" />
	<x:hidden name="status" />
	<x:textareaC name="content" label="common.field.rejectedReason" maxLength="100" required="true" labelCol="3" fieldCol="9" rows="4"/>
</form>
