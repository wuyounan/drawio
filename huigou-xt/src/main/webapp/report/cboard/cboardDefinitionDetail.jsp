<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="folderId" />
	<x:hidden name="sequence" />
	<div class="hg-form-row">
		<x:inputC name="code"  required="true" label="编码" maxLength="32" labelCol = "1" fieldCol="3" />
		<x:inputC name="name" required="true" label="名称" maxLength="32"  labelCol = "1" fieldCol="3" />
		<x:radioC dictionary="Status" name="status" value="1" label="状态" labelCol = "1" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" label="备注" maxLength="256" rows="2"  labelCol = "1" fieldCol="11" />
	</div>
</form>
<div class="clearfix"></div>
<div id = "cboardEntry" >
	<ul id="tabHeader" class="nav nav-tabs">
		<li class="active"><a href="#uiGridWrapper" data-toggle="tab">界面参数</a></li>
		<li><a href="#tabGridWrapper" data-toggle="tab">表格列表</a></li>
	</ul>
	
	<div id="tabContent" class="tab-content" style="padding-top: 5px;">
		<div class="tab-pane fade in active" id="uiGridWrapper">
			<div id="uiParamGrid" style="margin: 2px;"></div>
		</div>
		<div class="tab-pane fade" id="tabGridWrapper">
			<div id="tableGrid" style="margin: 2px;"></div>
		</div>
	</div>
</div>


