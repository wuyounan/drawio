<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="submitForm">
	<table class='tableInput' style="width: 99%;">
		<x:layout proportion="110px,190px" />
		<tr>
			<x:hidden name="sourceOrgId"/>
			<x:inputTD name="sourceOrgName" required="true" label="源组织"
				maxLength="128" wrapper="select" />
		</tr>
		<tr>
		<x:hidden name="destOrgId"/>
			<x:inputTD name="destOrgName" required="true" label="目标组织"
				maxLength="128" wrapper="select" />
		</tr>
	</table>
</form>
