<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox,commonTree,comboDialog" />
<x:script src="/system/opm/js/OpmUtil.js"/>
<x:script src="/system/datamanage/management/opdatamanagementList.js"/>
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
						<li class="tabli" id="menuDataAuthorize">数据权限授权</li>
						<li class="tabli" id="menuDataQuery">包含数据权限</li>
						<li class="tabli" id="menuDataResourceQuery">包含数据权限资源</li>
					</ul>
				</div>
				<div class="ui-tab-content" style="border: 0; padding: 2px;">
					<div class="layout" style="height: 100%;">
						<x:title title="搜索" hideTable="queryDataManageForm" id="navTitle01" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryDataManageForm">
						    <x:hidden name="fullId"/>
							<x:inputC name="name" label="权限类别" labelCol="1" wrapper="tree"/>
							<x:inputC name="code" label="关键字" labelCol="1" />
							<div class="col-xs-12 col-md-3" style="padding-left: 20px;">
								<x:button value="查 询" id="queryDataManageBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetDataManageBtn" icon="fa-history"/>
							</div>
						</form>
						<div class="blank_div clearfix"></div>
						<div id="maingrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" style="height: 100%;">
						<x:title title="搜索" hideTable="queryInheritDataManageGridTable" id="navTitle02" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryInheritDataManageGridTable">
							<x:hidden name="fullId"/>
							<x:inputC name="name" label="权限类别" labelCol="1" wrapper="tree"/>
							<x:inputC name="code" label="关键字" labelCol="1" />
							<x:inputC name="orgName" label="授权组织" labelCol="1" />
							<div class="col-xs-12 col-md-3" style="padding-left: 20px;">
								<x:button value="查 询" id="queryInheritDataManageBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetInheritDataManageBtn" icon="fa-history"/>
							</div>
						</form>
						<div id="inheritDataManageGrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" style="height: 100%;">
						<x:title title="搜索" hideTable="queryDataResourceManageGridTable" id="navTitle03" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryDataResourceManageGridTable">
							<x:inputC name="code" label="编码" labelCol="1" />
							<x:inputC name="name" label="名称" labelCol="1" />
							<x:inputC name="resourceValue" label="资源值" labelCol="1" />
							<div class="col-xs-12 col-md-3" style="padding-left: 20px;">
								<x:button value="查 询" id="queryDataResourceBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetDataResourceBtn" icon="fa-history"/>
							</div>
						</form>
						<div id="dataResourceManageGrid" style="margin: 2px;"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>