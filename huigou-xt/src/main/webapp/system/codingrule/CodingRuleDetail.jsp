<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" labelCol="1" fieldCol="3" maxLength="32" />
		<x:inputC name="name" required="true" label="名称" labelCol="1" fieldCol="3" maxLength="64" />
		<x:inputC name="delimiter" label="分隔符" labelCol="1" fieldCol="3" maxLength="1" />
	</div>
	<div class="hg-form-row">
	   <x:radioC name="isAddNoBreak" label="新增不允许断号" dictionary="yesorno" value="1" labelCol="2" fieldCol="2"></x:radioC>
	   <x:radioC name="isAddShow" label="新增显示" dictionary="yesorno" value="1" labelCol="2" fieldCol="2"></x:radioC>
	   <x:radioC name="isModifiable" label="支持修改" dictionary="yesorno" value="1" labelCol="2" fieldCol="2"></x:radioC>
	</div>
	<div class="hg-form-row">
	   <x:radioC name="isBreakCode" label="支持断号" dictionary="yesorno" value="1" labelCol="2" fieldCol="2"></x:radioC>
	    <x:radioC name="isSelectBreakCode" label="断号用户选择" dictionary="yesorno" value="1" labelCol="2" fieldCol="2"></x:radioC>
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark"  label="备注"   rows="2" labelCol="1" fieldCol="11"  maxLength="256"/>
	</div>
</form>
<div class="clearfix"></div>
<div id="detailGrid" style="margin: 2px;"></div>