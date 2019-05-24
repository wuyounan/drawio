<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="hg-form" method="post" action="" id="submitForm">
    <x:hidden name="id"/>
    <x:hidden name="parentId"/>
    <x:hidden name="sequence"/>
    <x:inputC name="parentName" required="false" label="上级流程" readonly="true" labelCol="3" fieldCol="9"/>
    <x:inputC name="code" required="true" label="编码" maxLength="128" labelCol="3" fieldCol="9"/>
    <x:inputC name="name" required="true" label="名称" maxLength="32" labelCol="3" fieldCol="9"/>
    <x:hidden name="ownerId"/>
    <x:inputC name="ownerName" required="false" label="所有者" maxLength="32" labelCol="3" fieldCol="9" />
    <div class="col-xs-12 col-md-12" style="margin-left: 15px;">
        <x:checkbox name="isFinal" label="末端流程"/>
    </div>
    <x:textareaC name="remark" required="false" label="备注" maxLength="120" labelCol="3" fieldCol="9" rows="4"/>
</form>
