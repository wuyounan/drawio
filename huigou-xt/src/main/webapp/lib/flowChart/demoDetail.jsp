<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="submitForm">
	<div class="ui-form">
		<x:hidden name="id"/>
		<x:hidden name="parentNodeId"/>
		<x:hidden name="sequence"/>
		<x:inputL name="name" required="true" label="名称" maxLength="50" labelWidth="80" width="265"/>
		<x:selectL name="nodeKind" required="true" label="节点类型" labelWidth="80" width="80" list="flowNodeKind" emptyOption="false"/>
		<x:selectL name="ruleKind" required="false" label="规则类型" labelWidth="80" width="80" list="flowRuleKind"/> 
		<x:inputL name="xaxis" required="false" label="横坐标" maxLength="16" width="80" labelWidth="80"/>	
		<x:inputL name="yaxis" required="false" label="纵坐标" maxLength="16" width="80" labelWidth="80"/>	
	</div>
</form>