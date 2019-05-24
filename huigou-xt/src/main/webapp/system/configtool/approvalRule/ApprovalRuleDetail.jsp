<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="orgId" />
	<x:hidden name="parentId" />
	<x:hidden name="fullId" />
	<x:hidden name="fullName" />
	
	<x:hidden name="procId" />
	<x:hidden name="procName" />
	<x:hidden name="procUnitId" />
	<x:hidden name="procUnitName" />
	
	<div class="hg-form-row">
		<x:radioC name="nodeKindId" list="nodeKindList" required="false" label="类别" fieldCol="4" />
		<x:inputC name="name" required="true" label="名称" labelCol="2" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="priority" required="true" label="优先级" fieldCol="4" />
		<x:radioC name="status" list="statusList" label="状态" labelCol="2" fieldCol="4"/>
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="scopeKind" name="scopeKindId" value="1" label="适用范围" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" required="false" label="备注" rows="2" fieldCol="10" />
	</div>
</form>
<div class="blank_div clearfix"></div>
<div id="scopeGrid" style="margin: 2px;"></div>
