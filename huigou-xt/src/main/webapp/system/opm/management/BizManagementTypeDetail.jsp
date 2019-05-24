<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="parentId" />
	<x:hidden name="fullId" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" match="englishCode" maxLength="64" labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" labelCol="3"  maxLength="64" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<div class="hg-form-row">
			<x:radioC dictionary="bizManagementType" name="kindId" value="1" label="类别" labelCol="3" fieldCol="9" />
		</div>
		<div class="hg-form-row">
			<x:radioC dictionary="bizManagementNodeKind" name="nodeKindId" value="1" label="节点类别" labelCol="3" fieldCol="9" />
		</div>
		<div class="hg-form-row">
			<x:inputC name="sequence" label="排序号"  spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="9" />
		</div>
		<div class="hg-form-row">
			<x:textareaC name="remark" label="备注"  rows="3" labelCol="3" fieldCol="9"></x:textareaC>
		</div>
</form>