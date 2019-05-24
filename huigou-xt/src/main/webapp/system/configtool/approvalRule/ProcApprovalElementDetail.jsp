
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="submitForm">
	<div class="ui-form" style="width:860px;">
		
		<x:hidden name="id"/>
		
		<x:inputL name="procId" required="false" label="流程ID" maxLength="64"/>		
		<x:inputL name="procName" required="false" label="流程名称" maxLength="128"/>		
		<x:inputL name="procUnitId" required="false" label="环节ID" maxLength="64"/>		
		<x:inputL name="procUnitName" required="false" label="环节名称" maxLength="128"/>		
		<x:inputL name="elementId" required="false" label="审批要素ID" maxLength="22"/>		
		<x:inputL name="sequence" required="false" label="排序号" maxLength="22"/>
	</div>
</form>
