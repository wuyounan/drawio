<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,grid,tree,dateTime,combox,comboDialog,formButton" />
    <script src='<c:url value="/biz/mcs/baseinfo/index/indexDetail.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div class="subject" style="margin: 10px auto 10px; font-size: 24px; font-weight: bold; text-align: center;">指标信息</div>
		<form class="hg-form" method="post" action="" id="submitForm">
			<x:hidden name="id" />
			<x:hidden name="classificationId" />
			<x:hidden name="status" value="1" />
			<x:hidden name="sequence" />
			<div class="hg-form-cols">
				<div class="hg-form-row">
					<x:inputC name="code" required="true" label="编码" maxLength="32"  match="englishCode" labelCol="1" fieldCol="3" />
					<x:inputC name="name" required="true" label="名称" maxLength="128" labelCol="1" fieldCol="3" />
					<x:inputC name="displaySequence" required="true" label="显示顺序"  spinner="true"  mask="nnn" dataOptions="min:1" labelCol="1" fieldCol="3" />
				</div>
				<div class="hg-form-row">
					<x:selectC name="indexPeriodKind" required="false" label="周期类型" dictionary="indexPeriodKind" labelCol="1" fieldCol="3" />
					<x:selectC name="indexBizKind"  required="false" label="业务类型" dictionary="indexBizKind"  labelCol="1" fieldCol="3" />
					<x:selectC name="startProcess" required="false" label="启动流程" dictionary="yesorno" labelCol="1" fieldCol="3" />
				</div>
				<div class="hg-form-row">
					<x:selectC name="countyHandle" required="true" label="县级处理" dictionary="yesorno" labelCol="1" fieldCol="3" />
					<x:selectC name="cityHandle"  required="true" label="市级处理" dictionary="yesorno"  labelCol="1" fieldCol="3" />
					<x:selectC name="provinceHandle" required="true" label="省局处理" dictionary="yesorno" labelCol="1" fieldCol="3" />
				</div>
				<div class="hg-form-row">
					<x:textareaC name="description" required="false" label="描述" rows="2" maxLength="256" labelCol="1" fieldCol="11" />
				</div>
				<div class="hg-form-row">
					<x:textareaC name="detailDescription" label="详细信息" maxLength="2014" rows="4" labelCol="1" fieldCol="11" />
				</div>
			</div>
		</form>
		<div class="clearfix"></div>
		<div class="hg-form" style="margin: 1px;">
			<div id="entryGrid"></div>
		</div>
	</div>
</body>
</html>
