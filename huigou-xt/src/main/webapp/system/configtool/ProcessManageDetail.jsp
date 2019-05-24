<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="parentId" />
	<x:hidden name="id" />
	<x:hidden name="fullId" />
	<x:hidden name="fullName" />
	<div class="hg-form-row">
		<x:inputC name="code" required="true" label="编码" fieldCol="4" />
		<x:inputC name="name" required="true" label="名称" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:selectC name="nodeKindId" required="true" label="类别" dictionary="processManageNodeKind" fieldCol="4" />
		<x:radioC dictionary="yesorno" name="needTiming" value="1" label="是否计时" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="limitTime" label="限制时间" fieldCol="4" />
		<x:radioC dictionary="processManageStatus" name="status" value="1" label="状态" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="yesorno" name="previewHandler" value="1" label="预览处理人" fieldCol="4" />
		<x:radioC dictionary="yesorno" name="assistantMustApprove" value="0" label="协审需审批" fieldCol="4" />
	</div>
	<!-- <div class="hg-form-row">
		<x:inputC name="approvalRuleProcDefKey" required="false" label="规则ProcID" maxLength="60" fieldCol="10" />
	</div> -->
	<div class="hg-form-row">
		<x:selectC name="mergeHandlerKind" required="false" label="处理人合并" list="mergeHandlerKindList" fieldCol="4" />
		<x:inputC name="sequence" required="false" label="排序号" spinner="true" mask="nnnn" dataOptions="min:1" fieldCol="4" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="description" label="描述" maxLength="256" rows="3" fieldCol="10" />
	</div>
</form>
