<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="dialog,grid,dateTime,combox" />
<script src='<c:url value="/system/suggestion/SuggestionBox.js"/>' type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<x:title title="意见箱" hideTable="queryDiv" />
			<form method="post" action="" id="queryMainForm">
				<div class="ui-form" id="queryDiv" style="width: 900px;">
					<x:selectL name="suggestionKind" required="false" label="类别" labelWidth="60"/>
					<x:inputL name="funName" required="false" label="功能" maxLength="128" labelWidth="60" />
					<x:inputL name="personName" required="false" label="提交人" maxLength="64" labelWidth="60" />
					<x:inputL name="createTimeBegin" required="false" label="时间起" wrapper="date" labelWidth="60" />
					<x:inputL name="createTimeEnd" required="false" label="时间止" wrapper="date" labelWidth="60" />
					<dl>
						<x:button value="查 询" onclick="query(this.form)" />
						&nbsp;&nbsp;
						<x:button value="重 置" onclick="resetForm(this.form)" />
						&nbsp;&nbsp;
					</dl>
				</div>
			</form>
			<div class="blank_div"></div>
			<div id="maingrid"></div>
		</div>
	</div>
</body>
</html>
