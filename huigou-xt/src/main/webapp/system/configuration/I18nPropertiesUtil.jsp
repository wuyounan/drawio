<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<x:hidden name="resourceKind" />
<div class="ui-hide">
	<c:forEach items="${i18nLanguageList}" var="obj">
		<div class="i18nLanguage" data-name="<c:out value="${obj.name}"/>"
			data-value="<c:out value="${obj.value}"/>"
			data-type="<c:out value="${obj.typeId}"/>">&nbsp;</div>
	</c:forEach>
</div>
<form class="hg-form" method="post" action="">
	<x:inputC name="code" label="common.field.code" maxLength="32" labelCol="1" />
	<x:inputC name="name" label="common.field.name" maxLength="32" labelCol="1" />
	<div class="col-sm-4 col-xs-12">
		<x:button value="common.button.query" id="doQuery" icon="fa-search"/>
		<x:button value="common.button.resetform" id="resetQueryForm" icon="fa-history"/>
	</div>
</form>
<div class="blank_div clearfix"></div>
<div class="grid"></div>