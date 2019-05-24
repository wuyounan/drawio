<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:inputC name="code" id="code" required="true" label="编码" labelCol="3" fieldCol="9" />
	<x:inputC name="name" id="name" required="true" label="名称" labelCol="3" fieldCol="9" />
	<x:inputC name="ip" id="ip" required="true" label="IP地址" labelCol="3" fieldCol="9" match="ip"/>
	<x:inputC name="mac" id="mac" required="true" label="MAC地址" labelCol="3" fieldCol="9" match="letter"/>
	<x:selectC list="securityGradeList" name="securityGrade" id="securityGrade" required="true" label="密级" labelCol="3" fieldCol="9" />
	<x:selectC dictionary="status" name="status" id="status" required="true" label="状态" labelCol="3" fieldCol="9" />
	<x:textareaC name="remark" id="remark" required="false" rows="3" label="备注" labelCol="3" fieldCol="9" />
</form>