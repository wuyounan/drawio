<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="config" value="${requestScope.ProcUnitConfig}"/>
//流程
var Process = {};
//流程环节配置
var ProcUnitConfig = {};
ProcUnitConfig["kindId"]= "<c:out value="${config.fullId}"/>";// "chief";
ProcUnitConfig["allowAdd"]=false;
ProcUnitConfig["allowSubtract"]=false;
ProcUnitConfig["allowTransfer"]=false;
ProcUnitConfig["allowAbort"]=false;
ProcUnitConfig["helpSection"]="#leave#approval";
ProcUnitConfig["chiefId"]=1234;
ProcUnitConfig["groupId"]=2;
//任务信息
var Task = {};