<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form class="hg-form" method="post" action="" id="submitForm">
	<x:hidden name="id" />
	<x:hidden name="mainOrgId" />
	<x:hidden name="version" />
	<x:hidden name="status" />
	<x:hidden name="picturePath" />
	<div class="hg-form-row">
		<div class="col-md-8">
			<div class="hg-form-row">
				<x:inputC name="parentFullName" label="上级" readonly="true" fieldCol="10" />
			</div>
			<div class="hg-form-row">
				<x:inputC name="code" id="code" required="true" label="员工编号" maxlength="16" labelCol="2" fieldCol="4" />
				<x:inputC name="name" id="name" required="true" label="姓名" maxlength="30" labelCol="2" fieldCol="4" />
			</div>
			<div class="hg-form-row">
				<x:inputC name="loginName" id="loginName" label="登录名" labelCol="2" fieldCol="4" maxlength="16"/>
				<x:inputC name="englishName" id="englishName" label="英文名称" labelCol="2" fieldCol="4" maxlength="60"/>
			</div>
			<div class="hg-form-row">
				<x:selectC name="certificateKindId" label="证件类型" required="false" dictionary="certificateKind" labelCol="2" fieldCol="4" />
				<x:inputC name="certificateNo" id="certificateNo" label="证件号码" required="false" labelCol="2" fieldCol="4" maxlength="30"/>
			</div>
			<div class="hg-form-row">
				<x:inputC name="caNo" label="CA序号" labelCol="2" fieldCol="4" maxlength="16"/>
				<x:selectC name="caStatus" label="CA状态" dictionary="caStatus" labelCol="2" fieldCol="4" />
			</div>
		</div>
		<div class="col-md-4">
			<div style="text-align: center;margin-top:10px;">
				<img src='<c:url value="/images/photo.jpg"/>' height='144' width='102' border=0 id="showPersonPicture" onerror="src='<c:url value="/images/photo.jpg"/>';"/>
			</div>
			<div style="text-align: center;margin-top:20px;">
				<x:button value="上传照片" id="addPicture" icon="fa-hand-o-up"/>
			</div>
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:selectC name="sex" dictionary="sex" label="性别" labelCol="2" fieldCol="4" />
			<x:selectC name="marriage" dictionary="maritalStatus" label="婚姻状况" labelCol="2" fieldCol="4" />
		</div>
		<div class="col-md-4">
			<x:inputC name="birthday" id="birthday" label="出生日期" wrapper="date" labelCol="4" fieldCol="8" />
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:inputC name="joinDate" id="joinDate" label="工作日期" wrapper="date" labelCol="2" fieldCol="4"/>
			<x:inputC name="title" id="title" label="职称" labelCol="2" fieldCol="4" maxlength="30"/>
		</div>
		<div class="col-md-4">
			<x:selectC name="degree" dictionary="education" label="学历" labelCol="4" fieldCol="8" />
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:inputC name="graduateSchool" id="graduateSchool" label="毕业院校" labelCol="2" fieldCol="4" maxlength="60"/>
			<x:inputC name="speciality" id="speciality" label="专业" labelCol="2" fieldCol="4" maxlength="60"/>
		</div>
		<div class="col-md-4">
			<!--<x:inputC name="schoolLength" id="schoolLength" label="学年制" labelCol="4" fieldCol="8" maxlength="16"/>-->
			<x:selectC name="personKind" dictionary="rolePersonKind" label="人员类别" labelCol="4" fieldCol="8" />
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:inputC name="email" id="email" label="电子邮件" required="true" labelCol="2" fieldCol="4" maxlength="64" match="email"/>
			<x:inputC name="qq" id="qq" label="QQ" labelCol="2" fieldCol="4" match="qq" maxlength="20"/>
		</div>
		<div class="col-md-4">
			<x:inputC name="weixin" label="微信" labelCol="4" fieldCol="8" match="letter" maxlength="32"/>
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:inputC name="officePhone" id="officePhone" label="办公电话" labelCol="2" fieldCol="4" maxlength="30" match="englishCode"/>
			<x:inputC name="mobilePhone" id="mobilePhone" label="移动电话" labelCol="2" fieldCol="4" maxlength="11" match="phone"/>
		</div>
		<div class="col-md-4">
			<x:inputC name="familyPhone" id="familyPhone" label="家庭电话" labelCol="4" fieldCol="8" maxlength="30" match="englishCode"/>
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-4">
			<x:inputC name="zip" id="zip" label="邮编" labelCol="4" fieldCol="8" maxlength="16" match="englishCode"/>
		</div>
		<div class="col-md-8">
			<x:inputC name="familyAddress" id="familyAddress" label="家庭地址" labelCol="2" fieldCol="10" maxlength="120" />
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:selectC name="securityGrade" label="密级" dictionary="securityGrade" labelCol="2" fieldCol="4" />
			<x:selectC name="personSecurityGrade" label="涉密等级" dictionary="personSecurityGrade" labelCol="2" fieldCol="4" />
		</div>
		<div class="col-md-4">
			<x:inputC name="sequence" required="true" label="排序号" spinner="true" mask="nnn" dataOptions="min:1" labelCol="4" fieldCol="8" />
		</div>
	</div>
	<div class="hg-form-row">
		<div class="col-md-8">
			<x:textareaC name="description" label="描述" maxlength="100" rows="2" labelCol="2" fieldCol="10" />
		</div>
	</div>
</form>
