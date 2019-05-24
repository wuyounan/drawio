<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="tabPage">
	<div class="ui-tab-links">
		<ul>
			<li id="l1">基本信息</li>
			<li id="l2">属性</li>
		</ul>
	</div>
	<div class="ui-tab-content">
		<div class="layout" style="height: 340px">
			<form class="hg-form" method="post" action="" id="submitForm">
				<x:hidden name="id" />
				<x:hidden name="parentId" />
				<x:hidden name="status" />
				<x:hidden name="orgKindId" />
				<x:hidden name="version" />
				<x:hidden name="fullCode" />
				<x:hidden name="fullName" />
				<x:hidden name="fullSequence" />
				<x:hidden name="fullOrgKindId" />
				<x:hidden name="typeId" id="typeId" />
				<x:hidden name="templateId" id="templateId" />
				<div class="hg-form-row">
					<x:inputC name="code" required="true" label="编码" id="detailCode" labelCol="3" fieldCol="6" />
					<div class="col-xs-12 col-sm-3">
						<x:button value="..." onclick="showSelectOrgTypeDialog()" id="btnSelectOrgType" />
						<x:button value="..." onclick="showSelectOrgTempalteDialog()" id="btnSelectOrgTemplate" />
					</div>
				</div>
				<div class="hg-form-row">
					<x:inputC name="name" required="true" label="名称" id="detailName" labelCol="3" fieldCol="9" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="longName" id="longName" label="全名称" labelCol="3" fieldCol="9" />
				</div>
				<div class="hg-form-row">
					<x:selectC name="deptLevel" id="deptLevel" label="部门级别" labelCol="3" fieldCol="9" />
				</div>
				<div class="hg-form-row" id="divIsVirtual">
					<x:inputC name="isVirtual" label="虚拟组织" id="isVirtual" labelCol="3" fieldCol="9" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="sequence" label="排序号" labelCol="3" fieldCol="9" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="orgKindName" label="组织类型" disabled="true" labelCol="3" fieldCol="9" />
				</div>
				<div class="hg-form-row">
					<x:textareaC name="description" required="false" label="描述" readonly="false" rows="3" labelCol="3" fieldCol="9" />
				</div>
			</form>
		</div>
		<div class="layout">
			<div id="propertyGrid"></div>
		</div>
	</div>
</div>


