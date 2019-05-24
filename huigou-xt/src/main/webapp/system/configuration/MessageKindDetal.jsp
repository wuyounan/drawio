<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="submitForm">
	<div class="ui-form" style="width: 400px">
		<x:hidden name="messageKindId" id="messageKindId"/>
		<x:inputL name="code" required="true" label="编码" maxLength="20" labelWidth="50"/>		
		<x:inputL name="name" required="true" label="名称" maxLength="50" labelWidth="50"/>	
		<x:inputL name="sequence" required="true" label="序号" maxLength="3"  mask="nnn" spinner="true" labelWidth="40" width="60"/>
		<div class="clear"></div>		
		<x:textareaL name="params" label="参数" rows="4" labelWidth="50" width="300"/>
		<x:textareaL name="remark" label="备注" rows="2" labelWidth="50" width="300"/>
	</div>
</form>
