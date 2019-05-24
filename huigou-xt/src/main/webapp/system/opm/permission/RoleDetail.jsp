<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="parentId" />
	<x:hidden name="id" />
	<x:hidden name="tenantId" />
	<x:inputC name="code" required="true" match="englishCode" label="编码" maxLength="64" fieldCol="10" />
	<x:inputC name="name" required="true" label="名称"  maxLength="64" fieldCol="10" />
	<div class="hg-form-row">
		<x:radioC dictionary="roleStatus" name="status" value="1" label="状态" fieldCol="10"/>
	</div>
	<div class="hg-form-row">
		<x:selectC name="kindId" list="roleKindList" required="true" label="角色类别"  fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="treeNodeKind" name="nodeKindId" value="1" label="节点类别"  fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:selectC name="rolePersonKind"  required="fasle" label="角色人员类别" labelCol="3" fieldCol="4" />
		<x:inputC name="sequence" label="排序号" spinner="true" mask="nnn" dataOptions="min:1" labelCol="2" fieldCol="3"/>
	</div>
	<div class="hg-form-row">
		<x:textareaC name="description" label="描述" maxLength="128" readonly="false" rows="3" fieldCol="10"></x:textareaC>
	</div>
</form>