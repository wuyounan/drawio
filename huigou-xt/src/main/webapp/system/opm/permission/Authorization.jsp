<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,commonTree" />
<x:script src="/system/opm/permission/Authorization.js"/>
<x:script src="/system/opm/js/OpmUtil.js"/>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title="组织机构树">
				<div class="ui-grid-query-div" style="position:static;width: 100%;margin:5px 0 0 10px;">
				    <div class="input-group" id="queryOrgGroup">
				    	<input type="text" value="" class="text ui-grid-query-input building-query-input">
				    	<span class="input-group-btn">
				    		<button type="button" class="btn btn-sm ui-grid-query-clear"><i class="fa fa-times-circle"></i></button>
				    		<button type="button" class="btn btn-sm btn-info ui-grid-query-button"><i class="fa fa-search"></i></button>
				    	</span>
				    </div>
				</div>
				<div class="blank_div clearfix"></div>
				<div class="dom-auto-height" id="orgTreeViewDiv">
					<ul id="maintree"></ul>
				</div>
				<div class="ui-hide" id="orgGridViewDiv">
					<div id="orgGridView"></div>
				</div>
			</div>
			<div position="center" id='tabPage' style="margin: 0; border: 0;padding: 0;overflow:hidden;">
				<div class="ui-tab-links">
					<ul>
						<li class="tabli" id="menuRoleAuthorize">角色授权</li>
						<li class="tabli" id="menuRoleQuery">继承角色查询</li>
						<li class="tabli" id="menuPermissionQuery">权限查询</li>
					</ul>
				</div>
				<div class="ui-tab-content" style="border: 0; padding: 2px;">
					<div class="layout" id="roleAuthorize" style="height: 100%;">
						<x:title title="搜索" hideTable="queryRoleForm" id="navTitle01" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryRoleForm">
							<x:inputC name="roleCode" label="编码" labelCol="1" />
							<x:inputC name="roleName" label="名称" labelCol="1" />
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryRoleFormBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetRoleFormBtn" icon="fa-history"/>
							</div>
						</form>
						<div class="blank_div clearfix"></div>
						<div id="maingrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" id="roleQuery" style="height: 100%;">
						<x:title title="搜索" hideTable="queryRoleGridTable" id="navTitle03" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryRoleGridTable">
							<x:inputC name="permissionName" label="名称" labelCol="1" />
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryRoleMainBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetRoleMainBtn" icon="fa-history"/>
							</div>
						</form>
						<div id="roleQuerygrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" id="permissionQuery" style="height: 100%;">
						<x:title title="搜索" hideTable="queryPermissionTable" id="navTitle02" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryPermissionTable">
							<x:inputC name="permissionName" label="名称" labelCol="1" />
							<div class="col-xs-12 col-sm-2" style="padding-top:7px">
								<x:checkbox name="onlyFunction" label="只显示功能"/>
							</div>
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryPermissionMainBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetPermissionMainBtn" icon="fa-history"/>
							</div>
						</form>
						<div class="blank_div"></div>
						<div id="permissiongrid" style="margin: 2px;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>