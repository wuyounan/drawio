<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<x:hidden name="parentId" id="typeParentId" />
<form method="post" class="hg-form" action="" id="queryMainForm">
	<x:inputC name="keyValue" label="关键字" maxLength="32" labelCol="1" />
	<div class="col-xs-12 col-md-4" style="padding-left: 20px;">
		<x:button value="查 询" id="queryMainBtn" icon="fa-search" />
		&nbsp;&nbsp;
		<x:button value="重 置" id="resetMainBtn" icon="fa-history" />
	</div>
</form>
<div class="blank_div clearfix"></div>
<div class="grid" style="margin: 2px;"></div>