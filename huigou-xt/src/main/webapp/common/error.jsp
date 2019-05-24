<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<table border=0 cellpadding=0   width="90%" height="90%"><tr><td  vAlign='middle'>
<div class="tips"><x:message key="error.info"/></div>
<c:out value="${message}"/>
</td></tr></table>