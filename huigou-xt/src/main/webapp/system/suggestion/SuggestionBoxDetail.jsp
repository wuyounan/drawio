<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="suggestionBoxForm">
	<div class="ui-form" style="width:500px;">
		<x:selectL name="suggestionKind" required="true" label="类别" labelWidth="60" width="120" emptyOption="false"/>		
		<div class='clear'></div>
		<x:inputL name="funName" required="false" label="功能" maxLength="128" labelWidth="60" width="410"/>	
		<x:textareaL name="content" required="false" label="内容" maxLength="1024" labelWidth="60" width="410" rows="10"/>	
	</div>
</form>
