<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" >
	<x:hidden name="parentId" />
	<x:hidden name="code" />
	<x:inputC name="name" required="true" maxlength="30" label="common.field.name" labelCol="3" fieldCol="9"/>
	<x:textareaC name="remark" required="false" maxlength="100" label="common.field.remark" labelCol="3" fieldCol="9" rows="3"/>
</form>