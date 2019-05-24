<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="submitForm">
	<div class='ui-form'>
		<x:hidden name="id" />
		<x:hidden name="parentId" />
		<x:hidden name="fullId" />
		<x:inputL name="code" required="true" label="编码" readonly="false"
			labelWidth="80" width="200" />
		<x:inputL name="name" required="true" label="名称" readonly="false"
			labelWidth="80" width="200" />
		<x:radioL dictionary="orgFunctionKind" name="kindId" value="1"
			label="类别" labelWidth="80" width="200" />
		<x:radioL dictionary="orgFunctionNodeKind" name="nodeKindId" value="1"
			label="节点类别" labelWidth="80" width="200" />	
		<x:inputL name="sequence" required="false" label="排序号"
			readonly="false" spinner="true" mask="nnn" dataOptions="min:1"
			labelWidth="80" width="100" />
		<x:textareaL name="remark" required="false" label="备注"
			readonly="false" rows="3" labelWidth="80" width="200"></x:textareaL>
	</div>
</form>