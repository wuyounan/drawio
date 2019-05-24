<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<html>
<head>
<title>日程安排</title>
<x:base include="date,combox,dialog"/>
<link href='<c:url value="/themes/default/fullcalendar/fullcalendar.css"/>' rel='stylesheet' type='text/css'/>
<link href='<c:url value="/themes/default/fullcalendar/fullcalendar.print.css"/>' rel='stylesheet' type='text/css' />
<link href='<c:url value="/themes/default/fullcalendar/theme.css"/>' rel='stylesheet' type='text/css'/>
<link href='<c:url value="/system/personOwn/workcalendar/style.css"/>' rel="stylesheet" type="text/css" />
<script src='<c:url value="/lib/jquery/ui/jquery.ui.core.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.widget.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.mouse.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.draggable.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/ui/jquery.ui.resizable.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/lib/jquery/fullcalendar.min.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/personOwn/workcalendar/workcalendar.js"/>' type="text/javascript"></script>
</head>
<body>
    <div id='calendarMain'>
		<div id='calendar'></div>
	</div>
</body>
</html>
