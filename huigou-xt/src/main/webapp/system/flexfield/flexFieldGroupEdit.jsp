<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,tree,grid,dateTime,combox,layout" />
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.formButton.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.flexField.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/flexfield/flexFieldGroupEdit.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="container-fluid">
		<form class="hg-form" method="post" action="" id="submitForm">
			<x:hidden name="id"/>
			<x:hidden name="folderId"/>
			<div class="hg-form-cols">
				<div class="hg-form-row">
					<x:inputC name="code" required="true" label="编码" maxLength="32" />
					<x:inputC name="name" required="true" label="名称" maxLength="64" />
					<x:radioC dictionary="yesorno" name="visible" value="1" label="是否显示" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="bizCode" required="true" label="业务编码" maxLength="22" id="detailBizCode" />
					<x:radioC dictionary="ShowModel" name="showModel" value="1" label="显示模式" />
					<x:inputC name="cols" required="true" label="每行列数" maxLength="22" spinner="true" mask="nn" dataOptions="min:1,max:12" />
				</div>
				<div class="hg-form-row">
					<x:inputC name="remark" label="备注" maxLength="512" fieldCol="10"/>
				</div>
			</div>
		</form>
		<div class="blank_div clearfix"></div>
		<div id="mainGrid"></div>
	</div>
</body>
</html>
