<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,grid,tree" /> 
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/permission/SelectRoleDialog.js"/>' type="text/javascript"></script>
<title>选择角色</title>
</head>
<body>
	<div class="container-fluid">
		<div id="pageLayout">
			<div position="left" title="角色树">
				<ul id="maintree">
				</ul>
			</div>
			<div position="center" title="列表">
				<x:title title="搜索" hideTable="queryTable" isHide="true" />
				<form class="hg-form ui-hide" method="post" action=""
					id="queryTable">
					<div class="col-xs-2">
						<label class="hg-form-label" id="keyword_label">关键字&nbsp;:</label>
					</div>
					<div class="col-xs-4">
						<input type="text" id="keyword" name="keyword"/>
					</div>
					<div class="col-md-4 col-xs-4">
						<x:button value="查 询" onclick="query(this.form)" />
							&nbsp;&nbsp;
							<x:button value="重 置" onclick="resetForm(this.form)" />
						</div>
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>