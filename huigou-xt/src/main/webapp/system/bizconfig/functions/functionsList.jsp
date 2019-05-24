<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <x:base include="dialog,layout,grid,tree,combox,commonTree"/>
    <x:script src='/system/bizconfig/common/BPMCUtil.js'/>
    <x:script src='/system/bizconfig/functions/functionsDetail.js'/>
    <x:script src='/system/bizconfig/functions/functionsList.js'/>
</head>
<body>
<div class="container-fluid" id="mainInfoDiv">
    <x:title title="common.button.query" isHide="true" hideTable="queryMainForm"/>
    <form method="post" class="hg-form ui-hide" action="" id="queryMainForm">
        <x:inputC name="code" required="false" label="common.field.code" labelCol="1"/>
        <x:inputC name="name" required="false" label="common.field.name" labelCol="1"/>
        <x:searchButtons/>
    </form>
    <div class="blank_div"></div>
    <div id="maingrid"></div>
</div>
</body>
</html>
