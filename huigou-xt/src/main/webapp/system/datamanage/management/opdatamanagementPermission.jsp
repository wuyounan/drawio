<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<x:script src="/system/datamanage/management/opdatamanagementPermission.js"/>
</head>
<body>
	<div class="container-fluid" style="padding-top: 10px;">
		<x:hidden name="dataManagedetalId" />
		<div position="center" id='tabPage' style="overflow:hidden;">
				<div class="ui-tab-links">
					<ul>
						<li class="tabli" id="menuResources">数据资源列表</li>
						<li class="tabli" id="menuAuthorization">授权情况</li>
						<!-- <li class="tabli" id="menuPerson">用户信息</li> -->
					</ul>
				</div>
				<div class="ui-tab-content" style="border: 0; padding: 2px;">
					<div class="layout" style="height: 100%; display: block;">
						<div id="resourcesgrid" style="margin: 2px;"></div>
					</div>
					<div class="layout" style="height: 100%;">
						<x:title title="搜索" hideTable="queryAuthorizationForm" id="navTitle02" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryAuthorizationForm">
							<x:inputC name="keyword" label="名称" labelCol="1" />
							<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
								<x:button value="查 询" id="queryAuthorizationFormBtn" icon="fa-search"/>
								&nbsp;&nbsp;
								<x:button value="重 置" id="resetAuthorizationFormBtn" icon="fa-history"/>
							</div>
						</form>
						<div id="dataAuthorizationQuerygrid" style="margin: 2px;"></div>
					</div>
					<!-- <div class="layout" id="personQuery" style="height: 100%;">
						<x:title title="搜索" hideTable="queryPersonForm" id="navTitle03" isHide="true" />
						<form class="hg-form ui-hide" method="post" action="" id="queryPersonForm">
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
					</div> -->
				</div>
		</div>
	</div>
</body>
</html>
