<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1, shrink-to-fit=no" />
<title>预警指标明细展示</title>

<x:base include="datepicker,date,tree,combox" />
<link rel="stylesheet" href='<c:url value="/themes/lhgdialog/idialog.css"/>' type="text/css" />
<link rel="stylesheet" href='<c:url value="/lib/bootstrap-table/bootstrap-table.css"/>' type="text/css" />
<script src='<c:url value="/lib/bootstrap-table/bootstrap-table.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.base64.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/bootstrap-table/extensions/export/bootstrap-table-export.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/bootstrap-table/extensions/tableExport/tableExport.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/lhgdialog.js?self=true"/>' type="text/javascript" id='lhgdialoglink'></script>
<!-- <script src='<c:url value="/biz/mcs/hana/common/dialog/commonDialog.js"/>' type="text/javascript"></script> -->
<script src='<c:url value="/system/index/indexAlertAllDetail.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<form class="hg-form" method="post" action="" id="queryMainForm">
			<x:hidden name="bizOrgId" />
			<x:hidden name="indexEntryId" />
			<x:hidden name="indexEntryTableId" />
			<div class="hg-form-row">
				<x:inputC name="startDate" required="true" wrapper="date" label="开始日期" labelCol="1" fieldCol="2" />
				<x:inputC name="endDate" required="true" wrapper="date" label="结束日期" labelCol="1" fieldCol="2" />
				<x:inputC name="bizOrgName" required="false" label="单位" labelCol="1" fieldCol="2" />
			</div>
			<div class="hg-form-row">
				<x:inputC name="indexEntryName" required="true" label="预警指标" labelCol="1" fieldCol="2" />
				<x:inputC name="indexEntryTableName" required="true" label="统计表" labelCol="1" fieldCol="2" />
				<x:searchButtons />
			</div>
		</form>
		<div class="clearfix"></div>
		<div id="maingrid" style="width: 100%"></div>
	</div>
</body>
</html>
