<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,combox,tree,attachment" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.toolBar.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/excelimport/ExcelImportLog.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<div id="layout">
			<div position="top">
				<div id="topToolbar" style="margin:2px;"></div>
				<div class="blank_div clearfix"></div>
				<form class="hg-form" method="post" action="" id="queryMainForm">
					<x:hidden name="id" />
					<x:hidden name="code" />
					<c:choose>
						<c:when test="${requestScope.id !=null}">
							<div class="hg-form-row bill-title">
								<div class="subject" id="templetNameSubject">
									<c:out value="${requestScope.name}" />
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="hg-form-row">
								<x:inputC name="templetName" required="true" label="模板名称" labelCol="1" fieldCol="2"/>
								<div class="col-xs-12 col-sm-5">
									<x:button value="选择模板" id="chooseImptemplet" icon="fa-list"/>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</form>
				<div id="maingrid"></div>
			</div>
			<div position="center" style="padding-left:2px;">
				<div id='impResultTab'>
					<div class="ui-tab-links">
						<ul>
							<li id="imp_success_title" data-id="imp_success_grid">导入成功</li>
							<li id="imp_error_title" data-id="imp_error_grid">导入失败</li>
						</ul>
					</div>
					<div class="ui-tab-content">
						<div class="layout" id="imp_success_content">
							<div id='imp_success_tmp_div'>
								<div id="imp_success_grid"></div>
							</div>
						</div>
						<div class="layout" id="imp_error_content">
							<div id='imp_error_tmp_div'>
								<div id="imp_error_grid"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
