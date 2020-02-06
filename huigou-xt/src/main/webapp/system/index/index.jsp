<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox,commonTree" />
<script src='<c:url value="/biz/mcs/baseinfo/index/index.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="指标分类树">
				<ul id="mainTree">
				</ul>
			</div>
			<div position="center" title="列表">
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<div class="hg-form-row">
						<x:inputC name="code" label="编号" maxLength="64" labelCol="1" />
						<x:inputC name="name" label="名称" maxLength="64" labelCol="1" />	
						<x:selectC name="indexPeriodKind" label="周期类型" dictionary="indexPeriodKind" labelCol="1" />	
						<div class="col-xs-12 col-sm-3">
							<button type="button" class="btn btn-primary" onclick="query(this.form)"><i class="fa fa-search"></i>&nbsp;查询	</button>&nbsp;&nbsp;	
							<button type="button" class="btn btn-primary" onclick="resetForm(this.form)"><i class="fa fa-history"></i>&nbsp;重置</button>&nbsp;&nbsp;
						</div>
					</div>
				</form>
				<div class="clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
