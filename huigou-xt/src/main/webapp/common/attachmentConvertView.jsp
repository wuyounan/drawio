<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<title>系统附件预览</title>
<x:base include="dialog,layout,attachment" />
<script src='<c:url value="/lib/jquery/jquery.mousewheel.js"/>'	type="text/javascript"></script>
<script src='<c:url value="/common/attachmentConvertView.js?a=1"/>'	type="text/javascript"></script>
</head>
<body>
	<div class="mainPanel">
		<div id="mainWrapperDiv">
			<x:hidden name="convertUrl"/>
			<x:hidden name="attachmentId"/>
			<x:hidden name="attachmentKind"/>
			<input type="hidden"  id="isReadOnly" value="${param.isReadOnly}"/>
			<input type="hidden"  id="convertForPhoneId" value="${param.convertForPhone}"/>
			<iframe id="downFile_hidden_Iframe" style="display:none;width:0px;height:0px;"></iframe>
			<div id="layout">
				<div position="top" style="padding-top:5px">
					<span style="margin-left:20px;font-size: 26px;font-weight: bold;white-space: nowrap;float: left;" id="fileNameView">
						${requestScope.fileName}(<c:out value="${requestScope.fileSize}" />)
					</span>
					<c:if test="${param.isReadOnly!='true'}">
					<div style="float:right;margin-right: 20px">
						<x:button value="下 载" onclick="downFile()" cssStyle="min-width:60px;font-weight: bold;" />
					</div>
					</c:if>
				</div>
				<c:if test="${attachmentList!= null && fn:length(attachmentList) > 1}">
				<div position="left" >
					<div style="overflow-x: hidden; overflow-y: auto; width: 100%;"  id="divTreeArea">
						<c:forEach items="${attachmentList}" var="item">
							<div style="margin-left:5px; " class="list_view<c:if test="${item.id==requestScope.attachmentId}"> divChoose</c:if>">
								<a href="javascript:void(0);" id="${item.id}"  fileKind="${item.fileKind}"  class="GridStyle">
									<span class="fileKind ${item.fileKind}">&nbsp;</span>&nbsp;<c:out value="${item.fileName}" />(<c:out value="${item.fileSize}" />)
								</a>
							</div>
						</c:forEach>
					</div>
				</div>
				</c:if>
				<div position="center"  id="convertViewCenter" style="-webkit-overflow-scrolling: touch; overflow:auto;">
					<div class='div-screen-over'  id="divScreenOver"></div>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
</html>