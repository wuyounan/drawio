<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<div class="hg-form-row">
		<x:hidden name="id" />
		<x:inputC name="fieldName" required="true" label="英文名称" maxLength="30" />
		<x:inputC name="description" required="true" label="中文名称" maxLength="64" />
		<x:selectC dictionary="fieldTypeList" value="1" name="fieldType" label="字段类型" required="true" emptyOption="false" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="fieldLength" label="字段长度" maxLength="22" mask="nnn" />
		<x:inputC name="fieldPrecision" label="字段精度" maxLength="22" mask="nn" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="defaultValue" label="默认值" maxLength="50" />
		<x:inputC name="minValue" label="最小值" maxLength="18" />
		<x:inputC name="maxValue" label="最大值" maxLength="18" />
	</div>
	<div class="hg-form-row">
		<x:selectC dictionary="editControlType" value="1" id="editControlType" name="controlType" required="true" label="控件类型" emptyOption="false" />
		<x:selectC dictionary="dataSourceKind" value="1" name="dataSourceKindId" label="数据源类型" emptyOption="false" id="editDataSourceKindId" />
		<div class="col-xs-12 col-md-4" style="padding-left:20px;padding-top:5px;"><x:checkbox name="newLine" label="在新行显示" value="1" /></div>
	</div>
	<div class="hg-form-row">
		<x:textareaC name="dataSource" label="数据源" maxLength="1000" rows="3" fieldCol="10" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="labelWidth" label="标签宽度" maxLength="22" mask="nnn" />
		<x:inputC name="controlWidth" label="控件宽度" maxLength="22" mask="nnn" />
	</div>
	<div class="hg-form-row">
		<x:radioC dictionary="yesorno" name="nullable" value="1" label="允许为空" />
		<x:radioC dictionary="yesorno" name="readOnly" value="0" label="是否只读" />
		<x:radioC dictionary="yesorno" name="visible" value="1" label="是否显示" />
	</div>
	<div class="hg-form-row">
		<x:textareaC name="remark" label="备注" maxLength="512" rows="3" fieldCol="10" />
	</div>
</form>
