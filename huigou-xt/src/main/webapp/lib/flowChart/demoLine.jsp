<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" action="" id="lineSubmitForm">
	<div id="parentNodeList" style="height:300px;overflow-x:hidden;overflow-y:auto;">
	<c:forEach items="${lineDatas}" var="line">
	<div id='navTitle' class="navTitle">
		<span class='group'>&nbsp;</span>&nbsp;<c:out value="${line.parentName}"/>
	</div>
	<div class="navline"></div>
	<div class="ui-form">
		<dl>
			<dt style="width:50px">名称&nbsp;:</dt>
			<dd><input class="text" value="<c:out value="${line.name}"/>" maxlength="32" type="text" name="name"></dd>
		</dl>
		<dl>
			<dt style="width:50px">类型<font color=#ff0000>*</font>&nbsp;:</dt>
			<dd style="width:80px">
				<select value="<c:out value="${line.lineType}"/>">
					<c:forEach items="${lineTypes}" var="type" >
					<option value="<c:out value="${type.key}"/>"><c:out value="${type.value}"/></option>
					</c:forEach>
				</select>
			</dd>
		</dl>
	</div>	
	</c:forEach>
	</div>
	<div id="showAllFlowNode" style="height:300px;overflow-x:hidden;overflow-y:auto;display:none;">
	</div>
	<div class="ui-form">
		<dl style="text-align: right;width:400px">
			<span class="ui-button">
				<span class="ui-button-left"></span><span class="ui-button-right"></span>
				<input type="button" name="" value="保 存" class="ui-button-inner" onclick="" hidefocus="">
			</span>
			&nbsp;&nbsp;
			<span class="ui-button">
				<span class="ui-button-left"></span><span class="ui-button-right"></span>
				<input type="button" name="" value="添加上一步" class="ui-button-inner" id="addParentButton" hidefocus="">
			</span>
			&nbsp;&nbsp;
	</div>
</form>