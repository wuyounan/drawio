<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<html>
<head>
<title>便签</title>
<x:base include="dialog"/>
<link rel="stylesheet" type="text/css" href='<c:url value="/system/personOwn/notes/style.css"/>'/>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.core.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.widget.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.mouse.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.draggable.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.droppable.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/jquery.maxlength.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/personOwn/notes/notes.js"/>' type="text/javascript"></script>
</head>
<body>
<div class='main'>
  <c:if test="${error!=null}">
	<table border=0 cellpadding=0 width="100%" height="100%">
	<tr><td align='left' vAlign='top' style='background-color:infobackground;border: #d63607 dotted 1px;'>
	<img src='<c:url value="/themes/default/images/err.gif"/>'/>&nbsp;查询数据时出错,请联系管理员:<c:out value="${error}"/>
	</td></tr></table>
  </c:if>
  <div class="gallery">
	  <div class="notesPanel" id='notesPanel'>
		   <c:forEach items="${notes}" var="obj">
			<div class='note <c:out value="${obj.color}"/>' style='left:<c:out value="${obj.offestx}"/>px;top:<c:out value="${obj.offesty}"/>px;z-index:<c:out value="${obj.zindex}"/>' id='<c:out value="${obj.id}"/>'>
				<div class='content'><c:out value="${obj.content}"/></div>
				<div class="date">--&nbsp;<c:out value="${obj.timeTitle}"/></div>
			</div>
		   </c:forEach>
		   <div id='doDelete' class='op-state-delete'>&nbsp;</div>
	  </div>
	  <div id='menu_bottom'>
		 <a href='#' title='新增便签' class='newnote' hidefocus onclick='show_add_dialog()'>&nbsp;</a>
		 <a href='<c:url value="/personOwnAction!toListPage.do"/>' title='列表显示' class='listview' hidefocus>&nbsp;</a>
	  </div>
  </div>
</div>
</body>
</html>
