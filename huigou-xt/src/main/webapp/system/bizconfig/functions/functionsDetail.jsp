<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container-fluid">
	<div class="layout">
			<div position="top">
				<form class="hg-form" method="post" action="" id="submitForm">
			        <div class="hg-form-cols">
			            <x:hidden name="id" id="functionsId"/>
			            <div class="hg-form-row">
			                <x:inputC name="code" required="true" label="common.field.code" maxLength="64" match="englishCode"/>
			                <x:inputC name="name" required="true" label="common.field.name" maxLength="32" fieldCol="6"/>
			            </div>
			             <div class="hg-form-row">
			                <x:selectC name="isToHide" dictionary="yesorno" required="true" label="无权限时隐藏" labelCol="2" fieldCol="2" />
			                <x:inputC name="checkBeanName" required="false" label="权限校验Bean" maxLength="64" match="englishCode"/>
			                <div class="col-xs-4 hidden-xs"></div>
			            </div>
			            <div class="hg-form-row">
			                <x:textareaC name="remark" required="false" label="common.field.remark" maxLength="128" rows="2" labelCol="2" fieldCol="10"/>
			            </div>
			        </div>
			        <div class="hg-form-row" style="text-align: right;padding-top:5px;">
			        	<x:button value="保 存" icon="fa-save" onclick="doSaveMain(this.form)"/>&nbsp;&nbsp;
						<x:button value="预 览" icon="fa-list-alt" onclick="doView(this.form)"/>&nbsp;&nbsp;
						<x:button value="关 闭" icon="fa-times" onclick="doClose()"/>&nbsp;&nbsp;
			        </div>
			        <div class="clearfix"></div>
			    </form>
			</div>
			<div position="left" title='功能分组'>
				<div class="blank_div"></div>
				<div class="groupgrid" style="margin: 2px;"></div>
			</div>
			<div position="center" title='功能列表'>
				<x:hidden name="functionsGroupId" id="chooseFunctionsGroupId"/>
				<div class="blank_div"></div>
				<div class="detailgrid" style="margin: 2px;"></div>
			</div>
	</div>
</div>
