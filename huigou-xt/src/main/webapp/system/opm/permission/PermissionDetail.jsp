<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="parentId" />
	<x:hidden name="resourceKindId" />
	<x:hidden name="resourceId" />
	<x:selectC name="nodeKindId" list="permissionNodeKind" required="true" label="节点类型" labelCol="3" fieldCol="9" />
	<x:inputC name="resourceName" label="资源" wrapper="select" labelCol="3" fieldCol="9" />
	<x:hidden name="operationId" />
	<x:inputC name="operationName" label="操作" wrapper="select" labelCol="3" fieldCol="9" />
	<x:inputC name="code" required="true" label="编码" id="detailCode" labelCol="3" fieldCol="9" />
	<x:inputC name="name" required="true" label="名称" id="detailName" labelCol="3" fieldCol="9" />
	<x:inputC name="sequence" required="false" label="排序号" spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="9" />
	<x:textareaC name="remark" label="备注" rows="3" labelCol="3" fieldCol="9" />
</form>