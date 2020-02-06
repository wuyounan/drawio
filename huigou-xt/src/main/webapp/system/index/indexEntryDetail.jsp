<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="entrySubmitForm">
	<x:hidden name="id" id="entryId" />
	<x:hidden name="indexId" />
	<x:hidden name="sequence" />
	<div class="hg-form-cols">
		<div class="hg-form-row">
			<x:selectC name="timeDim" required="true" label="时间维度"
				dictionary="timeDim" labelCol="2" fieldCol="4" />
			<x:selectC name="organDim" required="true" label="组织维度"
				dictionary="organDim" labelCol="2" fieldCol="4" />
		</div>
		<div class="hg-form-row">
			<x:inputC name="upperLimit" required="false" label="正常上限"
				mask="nnnnn.nn" spinner="true" labelCol="2" fieldCol="4" />
			<x:inputC name="lowerLimit" required="false" label="正常下限"
				mask="nnnnn.nn" spinner="true" labelCol="2" fieldCol="4" />
		</div>
		<div class="hg-form-row">
			<x:textareaC name="formula" required="false" label="公式" rows="2"
				maxLength="64" labelCol="2" fieldCol="10" />
		</div>
		<div class="hg-form-row">
			<x:selectC name="viewKind" required="false" label="显示类型"
				dictionary="viewKind" labelCol="2" fieldCol="4" />
			<x:inputC name="url" required="false" label="Url" maxLength="64"
				labelCol="2" fieldCol="4" />
		</div>
	</div>
</form>
<div id="indexDetailViewDiv">
	<div class="blank_div clearfix"></div>
	<ul id="tabHeader" class="nav nav-tabs">
		<li class="active"><a href="#formulaGridWrapper"
			data-toggle="tab"> 公式参数 </a></li>
		<li><a href="#uiGridWrapper" data-toggle="tab">界面参数</a></li>
		<li><a href="#tabGridWrapper" data-toggle="tab">表格列表</a></li>
	</ul>
	<div id="tabContent" class="tab-content" style="padding-top: 5px;">
		<div class="tab-pane fade in active" id="formulaGridWrapper">
			<div id="formulaParamGrid" style="margin: 2px;"></div>
		</div>
		<div class="tab-pane fade" id="uiGridWrapper">
			<div id="uiParamGrid" style="margin: 2px;"></div>
		</div>
		<div class="tab-pane fade" id="tabGridWrapper">
			<div id="tabGrid" style="margin: 2px;"></div>
		</div>
	</div>
</div>