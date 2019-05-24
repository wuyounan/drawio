<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<x:base include="layout,dialog,grid,dateTime,tree" />
<script src='<c:url value="/system/cfg/CommonTree.js"/>' type="text/javascript"></script>
<script src='<c:url value="/system/cfg/SelectCommonTreeDialog.js"/>' type="text/javascript"></script>
<title>选择通用树</title>
</head>
<div style="overflow-y: auto; overflow-x: hidden; height: 100%; height: 100%; margin-top: 10px;">
    <ul id="maintree">
    </ul>
</div>