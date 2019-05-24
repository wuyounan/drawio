<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<x:script src="/system/opm/permission/RolePermission.js"/>
</head>
<body>
	<div class="container-fluid" style="padding-top: 10px;">
		<x:hidden name="roleId" />
		<div position="center" id='tabPage' style="overflow:hidden;">
				<div class="ui-tab-links">
					<ul>
						<li class="tabli" id="menuPermission">权限列表</li>
						<li class="tabli" id="menuRoleAuthorization">角色授权</li>
						<li class="tabli" id="menuPerson">用户信息</li>
					</ul>
				</div>
				<div class="ui-tab-content" style="border: 0; padding: 2px;">
					<div class="layout" id="permissionQuery" style="height: 100%; display: block;">
						<x:title title="搜索" hideTable="queryPermissionGridTable" id="navTitle01" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryPermissionGridTable">
							<x:inputC name="keyword" label="名称" labelCol="1" />
							<div class="col-xs-12 col-sm-2" style="padding-top:7px">
								<x:checkbox name="onlyFunction" label="只显示功能权限"/>
							</div>
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryPermissionMainBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetPermissionMainBtn" icon="fa-history"/>
							</div>
						</form>
						<div id="permissiongrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" id="roleAuthorizationQuery" style="height: 100%;">
						<x:title title="搜索" hideTable="queryRoleAuthorizationGridTable" id="navTitle02" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryRoleAuthorizationGridTable">
							<x:inputC name="keyword" label="名称" labelCol="1" />
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryRoleAuthorizationMainBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetRoleAuthorizationMainBtn" icon="fa-history"/>
							</div>
						</form>
						<div id="roleAuthorizationQuerygrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" id="personQuery" style="height: 100%;">
						<x:title title="搜索" hideTable="queryPersonTable" id="navTitle03" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryPersonTable">
							<x:inputC name="keyword" label="名称" labelCol="1" />
							<div class="col-xs-12 col-sm-2" style="padding-top:7px">
								<x:checkbox name="singlePerson" label="只显示一个人员"/>
							</div>
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryPersonMainBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetPersonMainBtn" icon="fa-history"/>
							</div>
						</form>
						<div class="blank_div"></div>
						<div id="persongrid" style="margin: 2px;"></div>
					</div>
				</div>
		</div>
	</div>
</body>
</html>
