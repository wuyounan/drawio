<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="submitForm">
	<table class='tableInput' style="width: 595px;">
		<x:layout proportion="15%,35%,15%,35%" />
		<tr>
			<x:inputTD name="appCode" required="false" label="系统编码"
				disabled="true" />
			<x:inputTD name="appName" required="false" label="系统名称"
				disabled="true" />
		</tr>
		<tr>
			<x:inputTD name="className" required="false" label="处理类"
				disabled="true" colspan="3" />
		</tr>
		<tr>
			<x:inputTD name="methodName" required="false" label="方法"
				disabled="true" colspan="3" />
		</tr>
		<tr>
			<x:inputTD name="fullName" required="false" label="操作员" colspan="3"
				disabled="true" />
		</tr>
		<tr>
			<x:inputTD name="beginDate" required="false" label="开始时间"
				disabled="true" />
			<x:inputTD name="endDate" required="false" label="结束时间"
				disabled="true" />
		</tr>
		<tr>
			<x:inputTD name="description" required="false" label="操作描述"
				colspan="3" disabled="true" />
		</tr>

		<tr>
			<x:inputTD name="ip" required="false" label="IP地址" disabled="true"
				colspan="3" />
		</tr>
		<c:choose>
			<c:when test="${status=='0'}">
				<tr>
					<x:textareaTD name="params" label="参数" rows="6" disabled="true"
						colspan="3" />
				</tr>
				<tr>
					<x:textareaTD name="errorMessage" label="错误信息" rows="8"
						disabled="true" colspan="3" />
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<x:textareaTD name="params" label="参数" rows="8" disabled="true"
						colspan="3" />
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
</form>
