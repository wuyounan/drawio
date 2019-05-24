<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="hg-form" id="i18nResourceKindDiv">
	<x:selectC name="i18nResourceKind" label="common.field.kind" required="true" labelCol="3" fieldCol="9" />
</div>
<div class="blank_div clearfix"></div>
<div
	style="overflow-x: hidden; overflow-y: auto; width: 300px; height: 300px;">
	<ul id="dialogKindTree"></ul>
</div>
