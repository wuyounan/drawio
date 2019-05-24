<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree,combox" />
<script src='<c:url value="/lib/jquery/jquery.comboDialog.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/opm/js/OpmUtil.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.flexField.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/classification/useBizClassificationDetail.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/classification/showBizClassificationDetail.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="layout">
			<div position="left" title="组织机构" id="mainmenu">
				<ul id="orgTree"></ul>
			</div>
			<div position="center" style="margin-top: 2px;">
				<c:import url="/system/classification/useBizClassificationDetail.jsp" />
			</div>
		</div>
	</div>
</body>
</html>
