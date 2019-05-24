<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox,commonTree" />
<x:i18n name="pages"/>
<script src='<c:url value="/system/configuration/approvalRejectedReasonList.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="approvalRejectedReasonKind"/>
		<div id="layout">
			<div position="left" title='<x:message key="common.field.kind"/>'>
				<ul id="maintree"></ul>
			</div>
			<div position="center" title='<x:message key="job.approval.rejected.reason"/>'>
				<x:hidden name="folderId" id="treeFolderId" />
				<x:title title="common.button.search" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:inputC name="content" label="common.field.content" maxLength="32" labelCol="1" />
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
