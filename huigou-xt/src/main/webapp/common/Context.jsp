<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="opr" value="${sessionScope.sessionOperatorAttribute}"/>
var ContextOperator={};
ContextOperator['fullId']='<c:out value="${opr.fullId}"/>';
ContextOperator['fullName']='<c:out value="${opr.fullName}"/>';
ContextOperator['fullCode']='<c:out value="${opr.fullCode}"/>';
ContextOperator['orgId']='<c:out value="${opr.orgId}"/>';
ContextOperator['orgCode']='<c:out value="${opr.orgCode}"/>';
ContextOperator['orgName']='<c:out value="${opr.orgName}"/>';
ContextOperator['deptId']='<c:out value="${opr.deptId}"/>';
ContextOperator['deptCode']='<c:out value="${opr.deptCode}"/>';
ContextOperator['deptName']='<c:out value="${opr.deptName}"/>';
ContextOperator['positionId']='<c:out value="${opr.positionId}"/>';
ContextOperator['positionCode']='<c:out value="${opr.positionCode}"/>';
ContextOperator['positionName']='<c:out value="${opr.positionName}"/>';
ContextOperator['personMemberId']='<c:out value="${opr.personMemberId}"/>';
ContextOperator['personMemberCode']='<c:out value="${opr.personMemberCode}"/>';
ContextOperator['personMemberName']='<c:out value="${opr.personMemberName}"/>';
ContextOperator['loginName']='<c:out value="${opr.loginName}"/>';
ContextOperator['orgAdminKind']='<c:out value="${opr.orgAdminKind}"/>';
ContextOperator['id']='<c:out value="${opr.userId}"/>';
var SecurityPrompt = {};
SecurityPrompt['passwordExpired'] = '<c:out value="${sessionScope._passwordExpired_}"/>'; 
SecurityPrompt['updatePasswordPrompt'] = '<c:out value="${sessionScope._updatePasswordPrompt_}"/>'; 
SecurityPrompt['passwordRemainderDays'] = '<c:out value="${sessionScope._passwordRemainderDays_}"/>';
SecurityPrompt['firstLoginUpdatePasswordPrompt'] = '<c:out value="${sessionScope._firstLoginUpdatePassword_}"/>';