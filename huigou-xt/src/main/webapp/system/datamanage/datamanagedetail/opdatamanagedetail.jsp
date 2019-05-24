<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" id="detailPageId"/>
	<x:hidden name="dataManageId" id="detailDataManageId"/>
	<div class="hg-form-row">
		<x:inputC name="name" required="true" label="名称" maxLength="50" labelCol="2" fieldCol="10"/>
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" required="false" label="备注" maxLength="120" rows="2" labelCol="2" fieldCol="10"/>
   	</div>
   	<div class="hg-form-row">
   		<div class="col-xs-12 col-white-bg">
   			<x:radio name="resourcekind" label="添加资源类型" list="Resourcekinds"/>
   		</div>
    </div>
</form>
<div class="blank_div clearfix"></div>
<div id="datamanagedetailresourceGrid" ></div>