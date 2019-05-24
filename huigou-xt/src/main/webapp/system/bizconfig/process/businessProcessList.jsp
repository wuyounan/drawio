<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <x:base include="dialog,layout,grid,tree,datetime,combox,comboDialog,flexField,commonTree,formButton"/>
    <x:script src='/system/opm/js/OpmUtil.js'/>
    <x:script src='/system/bizconfig/common/BPMCUtil.js'/>
    <x:script src='/system/bizconfig/process/businessProcessEdit.js'/>
    <x:script src='/system/bizconfig/process/businessProcess.js'/>
</head>
<body>
<div class="container-fluid">
    <div id="layout">
        <div position="left" title="流程定义">
            <ul id="maintree"></ul>
        </div>
        <div position="center" title="流程定义表" id="mainInfoDiv">
            <x:title title="搜索" hideTable="queryMainForm" isHide="true"/>
            <form class="hg-form ui-hide" method="post" action="" id="queryMainForm">
                <x:inputC name="code" required="false" label="编码" maxLength="16" labelCol="1"/>
                <x:inputC name="name" required="false" label="名称" maxLength="32" labelCol="1"/>
                <x:searchButtons/>
            </form>
            <div class="blank_div clearfix"></div>
            <div id="maingrid" style="margin: 2px;"></div>
        </div>
    </div>
</div>
</body>
</html>
