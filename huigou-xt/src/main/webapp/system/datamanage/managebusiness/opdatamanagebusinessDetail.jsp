<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="hg-form" method="post" action="" id="submitForm">
	<div class="hg-form-cols">
		<x:hidden name="id" id="detailPageId"/>
	    <x:hidden name="parentId" id="detailPageParentId"/>
	    <x:hidden name="hasChildren"/>
	    <x:hidden name="sequence"/>
	    <x:hidden name="dataManageId"/>
    	<div class="hg-form-row">
		    <x:inputC name="code" required="true" label="编码" maxLength="16" labelCol="2" fieldCol="4" />
		    <x:inputC name="name" required="true" label="名称" maxLength="32" labelCol="2" fieldCol="4"/>
	    </div>
	    <div class="hg-form-row">
   			<x:radioC name="nodeKindId" value="1" label="节点类别" list="DataManageNodeKinds" labelCol="2" fieldCol="4" />
   			<x:inputC name="dataManageName" required="false" label="数据管理权限" maxLength="32" labelCol="2" fieldCol="4" wrapper="select"/>
   		</div>
   		<div class="hg-form-row">
    		<x:textareaC name="remark" required="false" label="备注" maxLength="120" rows="2" labelCol="2" fieldCol="10"/>
    	</div>
    </div>
</form>
<div class="blank_div clearfix"></div>
<div id="opdatamanagebusinessFieldShowDiv">
	<div id="opdatamanagebusinessFieldTabDiv">
		<div class="ui-tab-links">
			<ul style="left: 20px;">
				<li>权限字段列表</li>
				<li>特殊组织字段</li>
			</ul>
		</div>
		<div class="ui-tab-content" style="padding: 2px; height: 285px;">
			<div class="layout">
				<div id="opdatamanagebusinessFieldGrid"></div>
			</div>
			<div class="layout">
				<div id="opdatamanagebusinessOrgFieldGrid"></div>
			</div>
		</div>
	</div>
</div>