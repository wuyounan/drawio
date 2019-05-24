<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id"/>
	<div class="hg-form-row">
		<x:selectC name="securityGrade" id="securityGrade" required="true" label="密级" list="securityGradeList" labelCol="3" fieldCol="3" />
		<x:inputC name="lockUserPasswordErrorTime" id="lockUserPasswordErrorTime" required="true" label="密码错误次数锁定用户" spinner="true" mask="n" dataOptions="min:1" labelCol="3" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="passwordMinimumLength" required="true" label="密码最小长度" spinner="true" mask="nn" dataOptions="min:1" labelCol="3"	fieldCol="3" />
		<x:inputC name="numberCount" required="true" label="最少数字个数" spinner="true" mask="nn" dataOptions="min:1" labelCol="3" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="uppercaseCount" required="true" label="最少大写字母个数" spinner="true" mask="nn" dataOptions="min:1" labelCol="3" fieldCol="3" />
		<x:inputC name="lowercaseCount" required="true" label="最少小写字母个数" spinner="true" mask="nn" dataOptions="min:1" labelCol="3" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="specialCharacterCount" id="specialCharacterCount" required="true" label="最少特殊字符个数" spinner="true" mask="nn" dataOptions="min:1" labelCol="3" fieldCol="3" />
		<x:inputC name="passwordValidityInterval" required="true" label="密码有效天数" spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="3" />
	</div>
	<div class="hg-form-row">
	 	<div class="col-xs-4 col-sm-3">
	 		<label class="hg-form-label required" id="initPassword_label" title="初始化密码">初始化密码&nbsp;:</label>
	 	</div>
	 	<div class="col-xs-8 col-sm-3 col-white-bg">	
	 	     <input name="initPassword" id="initPassword"  value="<c:out value="${requestScope.initPassword}"/>"  required="true" label="初始化密码" type="password">
        </div> 
		<x:selectC name="status" id="status" required="true" label="启用" dictionary="yesorno" disabled="true" value="0" labelCol="3" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="intranetSegment" id="intranetSegment" required="true" label="内网网段" labelCol="3" fieldCol="3" match="englishCode"/>
		<x:selectC name="enableInternetLogin" id="enableInternetLogin" required="true" label="允许外网登录" dictionary="yesorno" labelCol="3" fieldCol="3" />
	</div>
	<div class="hg-form-row">
		<x:inputC name="passwordExpireGiveDays" id="passwordExpireGiveDays" required="true" label="密码过期提前提醒天数" spinner="true" mask="nn" dataOptions="min:1" labelCol="3" fieldCol="3" />
		<x:inputC name="autoUnlockTime" id="autoUnlockTime" required="true" label="自动解锁时间（分钟）" spinner="true" mask="nnn" dataOptions="min:1" labelCol="3" fieldCol="3" />
	</div>
</form>