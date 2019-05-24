<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="">
	<table class='tableInput'>
		<COLGROUP>
			<COL style="width: 80px;">
			<COL>
		</COLGROUP>
		<tr>
			<x:inputL name="code" required="true" label="编码" />
		</tr>
		<tr>
			<x:inputL name="name" required="true" label="名称" />
		</tr>
	</table>
</form>