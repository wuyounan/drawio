<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.huigou.data.datamanagement.DataResourceKind" %>
<c:set var="ORG" value="<%=DataResourceKind.ORG.getId()%>"/>
<c:set var="INPUT" value="<%=DataResourceKind.INPUT.getId()%>"/>
<form class="hg-form" method="post" action="" id="detailResourceSubmitForm">
    <x:hidden name="dataKindId"/>
    <x:hidden name="dataKind"/>
    <x:hidden name="dataSource"/>
    <x:hidden name="fullId"/>
    <x:hidden name="fullName"/>
    <c:choose>
		<c:when test="${requestScope.dataKind eq ORG}">
			<x:selectC name="orgDataKind" required="false" emptyOption="false" label="资源类型" list="dataManageChooseOrgKinds" labelCol="3" fieldCol="9"/>
			<div id="orgChooseDiv">
				<x:hidden name="resourceKey"/>
	   			<x:inputC name="resourceValue" required="true" wrapper="select" label="资源值" maxLength="32" labelCol="3" fieldCol="9"/>
	   		</div>
	   		<div id="orgDictionaryDiv" class="ui-hide">
	   			<x:selectC name="dictOrgDataKind" required="false" emptyOption="false" label="资源值" dictionary="sysOrgDataKind" labelCol="3" fieldCol="9"/>
	   		</div>
	   		<div id="orgManageTypeDiv" class="ui-hide">
	   			<x:inputC name="orgManageType" required="false" label="权限编码" maxLength="32" labelCol="3" fieldCol="9"/>
				<x:inputC name="orgManageTypeName" required="false" label="权限说明" maxLength="32" labelCol="3" fieldCol="9"/>
	   		</div>
		</c:when>
		<c:when test="${requestScope.dataKind eq INPUT}">
			<x:inputC name="resourceKey" required="true" label="资源数据" maxLength="32" labelCol="3" fieldCol="9"/>
			<x:inputC name="resourceValue" required="true" label="资源说明" maxLength="32" labelCol="3" fieldCol="9"/>
		</c:when>
		<c:otherwise>
			<x:hidden name="resourceKey"/>
	   		<x:inputC name="resourceValue" required="true" wrapper="select" label="资源值" maxLength="32" labelCol="3" fieldCol="9"/>
		</c:otherwise>
	</c:choose>
    
</form>
