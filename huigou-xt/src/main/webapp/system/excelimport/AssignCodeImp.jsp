<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${requestScope.hasRemark}">
<c:set var="newline" value="<%= \"\n\" %>" />
<div class="alert alert-block alert-info" style="margin-bottom:5px;padding:10px;">
	<div>
		${fn:replace(templateRemark,newline,"<br>")}
	</div>
</div>
</c:if>
<div>
	<div id="excelImpManagerStatusListDiv" class="hg-form">
		<x:radioC name="excelImpDataStatus" list="statusList" required="false" label="common.field.status" labelCol="1" fieldCol="11" />
	</div>
	<div id="excelImpManagerDiv">
		<x:hidden name="id" id="templetId"/>
		<x:hidden name="code" id="templetCode"/>
		<x:hidden name="name" id="templetName"/>
		<x:hidden name="batchNumber" />
		<div id="impBatchNumberMaingrid"></div>
	</div>
</div>