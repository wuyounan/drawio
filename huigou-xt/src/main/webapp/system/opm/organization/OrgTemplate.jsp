<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree" />
<script src='<c:url value="/system/opm/organization/OrgTemplate.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="机构模板树">
				<ul id="maintree"></ul>
			</div>
			<div position="center" title="机构模板列表">
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action=""
					id="queryMainForm">
					<x:inputC name="code" required="false" label="编码" labelCol="1" />
					<x:inputC name="name" required="false" label="名称" labelCol="1" />
					<div class="col-md-2 col-xs-12">
						<div class="m-t-sm">
							<x:checkbox name="isFullLike" label="查询所有"/>&nbsp;&nbsp;
						</div>
					</div>
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>