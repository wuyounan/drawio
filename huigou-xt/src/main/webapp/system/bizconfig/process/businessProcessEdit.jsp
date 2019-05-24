<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="/WEB-INF/JSTLFunction.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div style="padding: 5px;">
    <x:title title="基础信息" hideTable="#mainInforDiv" name="group" />
    <div id="mainInforDiv">
        <div class="bill_info" style="color:#aaaaaa">
			<span style="float: left; margin-left: 10px;">
				上级流程：<c:out value="${parentName}" /> &nbsp;&nbsp;
				创建人：<c:out value="${BusinessProcess_.creator.createdByName}" />&nbsp;&nbsp;
				创建时间：<c:out value="${f:format(BusinessProcess_.creator.createdDate,'date')}" />
			</span>
			<span style="float: right; margin-right: 10px;">
				修改人：<c:out value="${BusinessProcess_.modifier.lastModifiedByName}" />&nbsp;&nbsp;
				修改时间：<c:out value="${f:format(BusinessProcess_.modifier.lastModifiedDate,'date')}" />
			</span>
		</div>
		<form class="hg-form" method="post" action="" id="submitForm">
				<x:hidden name="id" id="detailPageId" />
				<x:hidden name="parentId" id="detailPageParentId" />
				<x:hidden name="parentName" id="detailPageParentName" />
				<x:hidden name="sequence" />
				<x:hidden name="fullId" />
				<x:hidden name="hasChildren" />
				<x:hidden name="isFlowChart" />
			<div class="hg-form-cols">
				<div class="hg-form-row">
					<x:inputC name="name" required="true" label="名称" maxlength="32" labelCol="1" fieldCol="3" id="detailPageName"/>
					<x:inputC name="code" required="true" label="编码" maxLength="64" match="englishCode" labelCol="1" fieldCol="3" id="detailPageCode"/>
					<div class="col-md-4 hidden-xs"></div>
				</div>
				<x:hidden name="ownerId"/>
				<div class="hg-form-row">
					<x:selectC name="isFinal" dictionary="yesorno" label="末端流程" id="detailPageIsFinal" labelCol="1" fieldCol="3"/>
					<x:inputC name="ownerName" required="false" label="所有者" maxLength="32" labelCol="1" fieldCol="3"/>
					<x:inputC name="userCode" required="false" label="权限Bean" match="englishCode" maxLength="32" labelCol="1" fieldCol="3"/>
				</div>
				<div class="hg-form-row">
					<x:textareaC name="flowAim" required="false" label="流程说明"  maxlength="256" rows="3" labelCol="1" fieldCol="11"/>
                </div>
                <div class="hg-form-row">
                    <x:textareaC name="flowRange" required="false" label="流程范围"  maxlength="256" rows="3" labelCol="1" fieldCol="11"/>
                </div>
				<div class="hg-form-row">
					<x:textareaC name="remark" required="false" label="备注" labelCol="1" fieldCol="11" maxlength="120" rows="3" />
				</div>
			</div>
		</form>
    </div>
</div>
