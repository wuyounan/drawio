<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="hg-form" method="post" action="" id="submitForm">
	<div class="hg-form-cols">
		<x:hidden name="id" id="detailPageId"/>
	    <x:hidden name="parentId" id="detailPageParentId"/>
	    <x:hidden name="hasChildren"/>
	    <x:hidden name="sequence"/>
    	<div class="hg-form-row">
		    <x:inputC name="code" required="true" label="编码" maxLength="16" labelCol="2" fieldCol="4" />
		    <x:inputC name="name" required="true" label="名称" maxLength="32" labelCol="2" fieldCol="4"/>
	    </div>
	    <div class="hg-form-row" id="nodeKindIdDiv">
   			<x:radioC name="nodeKindId" value="1" label="节点类别" list="DataManageNodeKinds" labelCol="2" fieldCol="10" />
   		</div>
   		<div class="hg-form-row">
    		<x:textareaC name="remark" required="false" label="备注" maxLength="120" rows="2" labelCol="2" fieldCol="10"/>
    	</div>
    </div>
</form>
<div class="blank_div clearfix"></div>
<div id="manageTypeKindGridShowDiv" class="ui-hide" style="margin: 2px;">
	<div id="manageTypeKindGrid" ></div>
</div>
