<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree" />
<script src='<c:url value="/system/opm/organization/OrgFunExample.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<form method="post" action="" id="queryMainForm">
				<div class="ui-form" id="queryTable">
					<div class="row">
						<x:inputL name="org" id="org" required="false" label="组织" readonly="false" labelWidth="100" width="200" />
						<x:button value="..." onclick="showSelectOrgDialog()" id="btnSelectOrg" cssStyle="min-width:30px;" />
					</div>
					<div class="row">
						<x:inputL name="managerType" id="managerType" required="false" label="管理权限类型" labelWidth="100" width="200" />
						<x:button value="..." onclick="showSelectBizManagementTypeDialog()" id="btnSelectManagerType" cssStyle="min-width:30px;" />
					</div>
					<div class="row" style="height: 30px;">
						<x:button value="取管理权限组织单元" id="btnFindManager" />
						&nbsp; &nbsp;
						<x:button value="取管理权限组织人员" id="btnFindManagerPsm" />
					</div>
				</div>
			</form>
			<div class="blank_div"></div>
			<div id="orgGrid" style="margin: 2px;"></div>

			<div class="blank_div"></div>
			<div id="psmGrid" style="margin: 2px;"></div>
		</div>
	</div>
</body>
</html>