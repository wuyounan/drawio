<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,tree,combox,date,commonTree,attachment" />
<x:i18n name="pages"/>
<script src='<c:url value="/system/configuration/i18nPropertiesList.js?v=20180313"/>' type="text/javascript"></script>
<script src='<c:url value="/system/configuration/i18nPropertiesUtil.js?v=20180313"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="left" title='<x:message key="system.i18nProperties.treetitle"/>'>
				<ul id="maintree"></ul>
			</div>
			<div position="center" title='<x:message key="system.i18nProperties.gridtitle"/>'>
				<x:hidden name="parentId" id="treeParentId" />
				<div class="ui-hide">
					<c:forEach items="${i18nLanguageList}" var="obj">
						<div class="i18nLanguage" data-name="<c:out value="${obj.name}"/>" data-value="<c:out value="${obj.value}"/>" data-type="<c:out value="${obj.typeId}"/>">&nbsp;</div>
					</c:forEach>
				</div>
				<x:title title="common.button.search" hideTable="queryMainForm" isHide="true" />
				<form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
					<x:inputC name="code" label="common.field.code" maxLength="32" labelCol="1"/>
					<x:inputC name="name" label="common.field.name" maxLength="32" labelCol="1" />
					<x:selectC name="resourceKind" dictionary="i18nResourceKind" label="common.field.kind" labelCol="1"/>
					<x:searchButtons />
				</form>
				<div class="blank_div clearfix"></div>
				<div id="maingrid" style="margin: 2px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
