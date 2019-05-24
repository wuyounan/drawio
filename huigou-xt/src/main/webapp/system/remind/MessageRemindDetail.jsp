<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" id="remindId" />
	<x:inputC name="code" required="true" label="编码" maxLength="32" labelCol="3" fieldCol="9" />
	<x:inputC name="name" required="true" label="标题" maxLength="64" labelCol="3" fieldCol="9" />
	<x:textareaC name="remindTitle" required="true" label="提示文本" maxLength="128" rows="2" labelCol="3" fieldCol="9" />
	<x:inputC name="remindUrl" label="连接地址" maxLength="256" labelCol="3" fieldCol="9" />
	<x:textareaC name="executeFunc" label="函数" maxLength="512" rows="2" labelCol="3" fieldCol="9" />
	<div class="hg-form-row">
		<x:radioC name="openKind" label="打开方式 " value="0" list="remindOpenKinds" labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="sequence" required="true" label="序号" spinner="true" labelCol="3" fieldCol="9" />
	</div>
	<div class="hg-form-row">
		<x:radioC name="replaceKind" label="替换类别 " value="0" list="remindReplaceKinds" labelCol="3" fieldCol="9" />
	</div>
</form>