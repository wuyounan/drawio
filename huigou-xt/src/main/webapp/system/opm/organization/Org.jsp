<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,combox,grid,tree,datetime,attachment,selectOrg,comboDialog,flexField" />
<script src='<c:url value="/system/opm/organization/Org.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<x:hidden name="orgRootId" id="delegationOrgRootId"/>
		<x:hidden name="manageType" id="delegationManageType"/>
		<div id="layout">
			<div position="left" title="组织机构树">
				<ul id="maintree">
				</ul>
			</div>
			<div position="center" title="组织机构列表">
				<x:title title="搜索" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<div class="hg-form-row">
						<x:inputC name="code" label="编码" labelCol="1" />
						<x:inputC name="name" label="名称" labelCol="1" />
						<div class="col-md-4 col-xs-12">
							<x:button value="查 询" id="btnQuery" />&nbsp;&nbsp; 
							<x:button value="重置" id="btnReset" />
						</div>
					</div>
					<div class="hg-form-row">
						<x:checkbox name="showDisabledOrg" id="showDisabledOrg" cssStyle="margin-left:10px;" label="显示已禁用的组织"/>
						<x:checkbox name="showVirtualOrg" id="showVirtualOrg" cssStyle="margin-left:10px;" label="显示虚拟组织"/>
						<x:checkbox name="showAllChildrenOrg" label="显示所有下级组织" cssStyle="margin-left:10px;"/>
						<x:checkbox name="showOrg" label="组织" cssStyle="margin-left:50px;"/>
						<x:checkbox name="showDept" label="部门" cssStyle="margin-left:10px;"/>
						<x:checkbox name="showPos" label="岗位" cssStyle="margin-left:10px;"/>
						<x:checkbox name="showPsm" label="人员" cssStyle="margin-left:10px;"/>
					</div>
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin:2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>