<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id"/>
	<x:inputC name="code" label="common.field.code" required="true" maxLength="100" labelCol="3" fieldCol="9"/>
	<x:selectC name="resourceKind" dictionary="i18nResourceKind" label="common.field.kind" required="true" labelCol="3" fieldCol="9" />
	<c:forEach items="${i18nLanguageList}" var="obj">
		<div class="col-xs-3">
			<label class="hg-form-label"><c:out value="${obj.name}"/>&nbsp;:</label>
		</div>
		<div class="col-xs-9">
			<input name="<c:out value="${obj.typeId}"/>" maxlength="120" value="<c:out value="${requestScope[obj.typeId]}"/>" type="text"/>
		</div>
	</c:forEach>
	<x:inputC name="remark" label="common.field.remark" maxLength="200" labelCol="3" fieldCol="9"/>
</form>
