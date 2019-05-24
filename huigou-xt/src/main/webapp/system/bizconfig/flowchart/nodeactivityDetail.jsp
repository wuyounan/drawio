<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div style="padding: 5px;">
    <div id="mainInforDiv">
    	<div class="bill_info" style="color:#aaaaaa">
			<span style="float: left; margin-left: 10px;">
				创建人：<c:out value="${ProcessNode_.creator.createdByName}" />&nbsp;&nbsp;
				创建时间：<c:out value="${f:format(ProcessNode_.creator.createdDate,'date')}" />
			</span>
		</div>
		<form class="hg-form" method="post" action="" id="submitForm">
			<div class="hg-form-cols">
				<x:hidden name="id"/>
				<x:hidden name="viewId"/>
				<x:hidden name="objectKindCode"/>
				<div class="hg-form-row">
					<x:inputC name="name" required="true" label="名称" maxLength="50" labelCol="3" fieldCol="9"/>
				</div>
				<div class="hg-form-row">
					<x:inputC name="enName" required="false" label="English" maxLength="50" labelCol="3" fieldCol="9"/>
				</div>
				<div class="hg-form-row">
					<x:inputC name="code" required="true" label="序号" maxLength="4" labelCol="3" fieldCol="3"/>
					<x:hidden name="ownerId"/>
			   	    <x:inputC name="ownerName" required="false" label="所有者" maxLength="64" labelCol="3" fieldCol="3"/>
				</div>
				<div class="hg-form-row">
					<x:inputC name="functionCode" required="false" label="功能编码" maxLength="50" labelCol="3" fieldCol="3"/>
					<x:inputC name="nodeCode" required="false" label="节点编码" maxLength="50" labelCol="3" fieldCol="3"/>
				</div>
			    <div class="hg-form-row">
					<x:inputC name="xaxis" required="false" label="横坐标" maxLength="16" labelCol="3" fieldCol="3"/>
					<x:inputC name="yaxis" required="false" label="纵坐标" maxLength="16" labelCol="3" fieldCol="3"/>
				</div>
				<div class="hg-form-row">
					<x:textareaC name="remark" required="false" label="备注" maxLength="120" rows="3" labelCol="3" fieldCol="9"/>
				</div>
			</div>
		</form>
	</div>
	<div class="blank_div"></div>
    <div class="nodeFunctionGrid"></div>
</div>