<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <x:base include="layout,dialog,grid,dateTime,tree,combox"/>
    <script src='<c:url value="/lib/jquery/jquery.commonTree.js"/>' type="text/javascript"></script>
    <script src='<c:url value="/lib/jquery/jquery.flexField.js"/>' type="text/javascript"></script>
    <script src='<c:url value="/system/classification/useBizClassificationDetail.js"/>' type="text/javascript"></script>
    <script src='<c:url value="/system/classification/showTenantBizClassification.js"/>' type="text/javascript"></script>
</head>
<body>
<div class="mainPanel">
    <div id="mainWrapperDiv">
         <c:import url="/system/classification/useBizClassificationDetail.jsp"/>
    </div>
</div>
</body>
</html>
