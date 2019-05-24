<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="orgKindId" />
	<x:hidden name="folderId" />
	<div class="hg-form-row">
		<x:inputC name="code" match="englishCode" required="true" label="编码"  maxlength="64" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" maxlength="64" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="orgKindStatus" name="status" value="1" label="状态" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="sequence" required="false" label="排序号" spinner="true" mask="nnn" dataOptions="min:1" fieldCol="10" />
	</div>
</form>