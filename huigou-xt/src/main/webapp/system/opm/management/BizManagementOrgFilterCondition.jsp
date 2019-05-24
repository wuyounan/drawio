<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<x:title title="过滤条件" name="group" />
<div class="hg-form-row">
	<x:checkbox name="showDisabledOrg" id="showDisabledOrg"
		cssStyle="margin-left:10px;" label="显示已禁用的组织"></x:checkbox>
</div>
<div class="hg-form-row">
	<x:checkbox name="showMasterPsm" id="showMasterPsm"
		cssStyle="margin-left:10px;" label="只显示主岗位"></x:checkbox>
</div>
<div class="hg-form-row">
	<x:checkbox name="showVirtualOrg" id="showVirtualOrg"
		cssStyle="margin-left:10px;" label="显示虚拟组织"></x:checkbox>
</div>
