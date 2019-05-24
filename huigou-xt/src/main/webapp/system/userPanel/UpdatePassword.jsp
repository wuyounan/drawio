<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form method="post" class="hg-form" action="" id="updatePasswordForm">
	<div class="col-xs-4 col-md-4">
		<label class="hg-form-label required"><x:message key="user.password.old"/>&nbsp;:</label>
	</div>
	<div class="col-xs-8 col-md-8">
		<input type="password" name="old" required="true" maxlength="15" label="原密码" />
	</div>
	<div class="col-xs-4 col-md-4">
		<label class="hg-form-label required"><x:message key="user.password.new"/>&nbsp;:</label>
	</div>
	<div class="col-xs-8 col-md-8">
		<input type="password" name="new" required="true" maxlength="15" label="新密码" />
	</div>
	<div class="col-xs-4 col-md-4">
		<label class="hg-form-label required"><x:message key="user.password.required"/>&nbsp;:</label>
	</div>
	<div class="col-xs-8 col-md-8">
		<input type="password" name="confirm" required="true" maxlength="15" label="确认密码" />
	</div>
	<div class="col-xs-12 col-md-12" style="text-align: center;">
		<span class='password-strength' id='passwordStrength1'><x:message key="user.password.weak"/></span>&nbsp;
		<span class='password-strength' id='passwordStrength2'><x:message key="user.password.middling"/></span>&nbsp; 
		<span class='password-strength' id='passwordStrength3'><x:message key="user.password.strong"/></span>&nbsp;
	</div>
</form>