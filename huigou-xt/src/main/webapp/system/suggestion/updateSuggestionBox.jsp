<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="suggestionBoxForm">
	<div class="ui-form" style="width:500px;">
		<x:hidden name="boxId"/>
		<x:selectL name="suggestionKind" required="false" label="类别" labelWidth="60" width="120" disabled="true"/>		
		<div class='clear'></div>
		<x:inputL name="funName" required="false" label="功能" maxLength="128" labelWidth="60" width="410" disabled="true"/>	
		<x:textareaL name="content" required="false" label="内容" maxLength="1024" labelWidth="60" width="410" rows="6" disabled="true"/>	
		<x:textareaL name="remark" required="false" label="批注" maxLength="1024" labelWidth="60" width="410" rows="6"/>
	</div>
</form>
