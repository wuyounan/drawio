<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src='<c:url value="/lib/jquery/jquery.base64.js"/>' type='text/javascript'></script>
<style type="text/css">
    .label-span {
        width: 85px;
    }
    .input-span {
        width: 180px
    }
    .row{
        height: 40px;
        line-height: 40px;
    }
    .required-font{
        color:#FF0000;+
        
    }
</style>
<div class="ui-form">
    <form method="post" action="" id="updatePasswordForm">
        <div class='row'>
            <dl>
                <dt class="label-span">原密码<font class="required-font">*</font>&nbsp;:</dt>
                <dd class="input-span">
                    <input type='password' class='text' id='modifOldPassword' required='true' maxlength='20'/>
                </dd>
            </dl>
        </div>
        <div class='row'>
            <dl>
                <dt class="label-span">新密码<font class="required-font">*</font>&nbsp;:</dt>
                <dd class="input-span">
                    <input type='password' class='text' id='modifNewPassword' required='true' maxlength='20'/>
                </dd>
            </dl>
        </div>
        <div class='row'>
            <dl>
                <dt class="label-span">确认密码<font class="required-font">*</font>&nbsp;:</dt>
                <dd class="input-span">
                    <input type='password' class='text' id='modifSurePassword' required='true' maxlength='20'/>
                </dd>
            </dl>
        </div>
        <div class='row'>
            <dl>
                <dt class="label-span">密码强度:</dt>
                <dd class="input-span">
                    <span class='passwordStrength' id='passwordStrength1'>弱</span>
                    <span class='passwordStrength' id='passwordStrength2'>中</span>
                    <span class='passwordStrength' id='passwordStrength3'>强</span>
                </dd>
            </dl>
        </div>
    </form>
</div>