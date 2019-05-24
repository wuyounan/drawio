<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<html>
<head>
<x:base include="dialog,grid,layout,date,attachment" />
<x:script src='/lib/jquery/jquery.md5.js'/>
<x:script src='/lib/jquery/jquery.base64.js'/>
<x:script src='/system/userPanel/UpdatePassword.js'/>
<x:script src='/system/userPanel/UsercontrolPanel.js'/>
</head>
<body>
	<div class="container-fluid">
		<div class="navbar navbar-default" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header hidden-xs">
					<p class="navbar-text" style="font-size: 16px; font-weight: 600"><x:message key="user.control.panel"/></p>
				</div>
				<div>
					<ul class="nav navbar-nav" id="userPanelNavbar">
						<li class="active"><a href="javascript:void(0);" data-id="setUserInfo"><i class="fa fa-user-circle-o"></i>&nbsp;<x:message key="index.user.own.info"/></a></li>
						<li><a href="javascript:void(0);" data-id="setUserPassword"><i class="fa fa-lock"></i>&nbsp;<x:message key="index.user.own.password"/></a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel panel-info" id="setUserInfo">
			<div class="panel-heading">
				<h3 class="panel-title">
					<i class="fa fa-user-circle-o"></i>&nbsp;<x:message key="index.user.own.info"/>
				</h3>
			</div>
			<div class="panel-body">
				<div class="col-xs-12 col-md-4">
					<div style="text-align: center;margin-top:10px;">
						<img src='<c:url value="/attachment/downFileBySavePath.ajax?file=${picturePath}"/>' height='240' width='170' border=0 id="showPersonPicture" onerror="src='<c:url value="/images/photo.jpg"/>';"/>
					</div>
					<div style="text-align: center;margin-top:20px;">
						<x:button value="user.add.picture" id="addPicture" icon="fa-hand-o-up" type="btn-info"/>
					</div>
					<div class="alert alert-warning alert-dismissable m-t">
						 <x:message key="user.add.picture.info"/>
                    </div>
				</div>
				<div class="col-xs-12 col-md-8">
					<form method="post" class="hg-form jumbotron-form" action="" id="updatePersonForm">
						<x:hidden name="id"/>
						<x:inputC name="name" required="false" label="common.field.username" disabled="true"	fieldCol="8" labelCol="4" />
						<x:inputC name="loginName" required="false" label="common.field.loginname" disabled="true" fieldCol="8" labelCol="4" />
						<x:inputC name="birthday" required="false" label="common.field.birthday" wrapper="date" fieldCol="8" labelCol="4" />
						<x:inputC name="joinDate" required="false" label="common.field.joindate" wrapper="date" fieldCol="8" labelCol="4" />
						<x:inputC name="homePlace" required="false" label="common.field.homeplace" maxlength="30" fieldCol="8" labelCol="4" />
						<x:inputC name="degree" required="false" label="common.field.degree" maxlength="16" fieldCol="8" labelCol="4" />
						<x:inputC name="graduateSchool" required="false" label="common.field.graduateschool" maxlength="16" fieldCol="8" labelCol="4" />
						<x:inputC name="speciality" required="false" label="common.field.speciality" maxlength="16" fieldCol="8" labelCol="4" />
						<x:inputC name="officePhone" required="false" label="common.field.officephone"  maxlength="30" match="englishCode" fieldCol="8" labelCol="4" />
						<x:inputC name="mobilePhone" required="false" label="common.field.phone"  maxlength="11" match="phone" fieldCol="8" labelCol="4" />
						<x:inputC name="email" required="false" label="common.field.email" maxlength="64" match="email" fieldCol="8" labelCol="4" />
						<x:inputC name="qq" required="false" label="QQ" maxlength="16"  match="qq" fieldCol="8" labelCol="4" />
					</form>
					<div class="blank_div clearfix"></div>
					<div style="min-height: 40px; text-align: center;">
						<x:button value="user.save.info" onclick="saveUserInfo()" icon="fa-save" />
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel panel-danger ui-hide" id="setUserPassword">
			<div class="panel-heading">
				<h3 class="panel-title">
					<i class="fa fa-lock"></i>&nbsp;<x:message key="index.user.own.password"/>
				</h3>
			</div>
			<div class="panel-body">
				<div class="col-xs-12 col-md-4">
					<c:import url="/system/userPanel/UpdatePassword.jsp" />
					<div class="blank_div clearfix"></div>
					<div style="min-height: 40px; text-align: center;">
						<x:button value="user.save.password" onclick="doUpdatePassword()" icon="fa-save"/>
					</div>
				</div>
				<div class="col-xs-12 col-md-8">
					<p style="padding: 5px;"><x:message key="user.password.prompt1"/></p>
					<p style="padding: 5px;"><x:message key="user.password.prompt2"/></p>
					<p style="padding: 5px;"><x:message key="user.password.prompt3"/></p>
					<p style="padding: 5px;"><x:message key="user.password.prompt4"/></p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
