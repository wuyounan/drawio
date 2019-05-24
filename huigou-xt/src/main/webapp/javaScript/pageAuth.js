/*二级密码权限验证通用处理*/
var PersonalPasswordAuth =function(){};
//空的关闭方法避免错误
PersonalPasswordAuth.prototype.close=function(){};
//验证密码连接
PersonalPasswordAuth.url=web_app.name + '/authenticationPersonalPassword.ajax';
//验证密码时限
PersonalPasswordAuth.checkUrl=web_app.name + '/checkPersonalPasswordTimeLimit.ajax';
//是否意见验证密码
PersonalPasswordAuth.isAuthenticationPassword=false;
//显示屏蔽层
PersonalPasswordAuth.showScreenOver=function(){
	var screenOver=$('#page-authentication-screen-over');
	if(!screenOver.length){
		screenOver=$('<div id="page-authentication-screen-over" style="position:fixed;background:#fff;top:0px;left:0px;width:100%;height:100%;z-index:10000;display:none;"></div>').appendTo('body');
	}
	screenOver.show();
};
PersonalPasswordAuth.hideScreenOver=function(){
	$('#page-authentication-screen-over').hide();
};
//显示密码输入对话框
PersonalPasswordAuth.showDialog=function(op){
	op=op||{};
	//是否需要弹出对话框
	var checkTimeLimit=Public.isBlank(op['checkTimeLimit'])?true:op['checkTimeLimit'];
	var okFun=op['okFun']||function(){
		this.close();
	};
	var closeFun=op['closeFun']||function(){
		UICtrl.closeCurrentTab();
	};
	var param={};
	if($.isFunction(op['getParam'])){
		param=op['getParam'].call(window);
	}
	var html=['<div style="margin-bottom:3px;">','<span style="width:60px;color:Tomato;font-weight:bold;">密&nbsp;&nbsp;码:</span>','<span style="width:10px;display:inline-block;">&nbsp;</span>'];
	html.push('<input type="password" id="page-password-input" style="width:150px;background: none repeat scroll 0 0 #F4F4F4;border: 1px solid #878787;line-height: 16px;padding:4px;color:#999;" value=""/>');
	html.push('</div>');
	html.push('<div style="text-align:center;margin-top:10px;">');
	html.push('<input type="button" value="确 定" class="buttonGray ok"/>');
	html.push('<span style="width:10px;display:inline-block;">&nbsp;</span>');
	html.push('<input type="button" value="关 闭" class="buttonGray close"/>');
	html.push('</div>');
	var options={
		title:'请输入访问密码',
		width: 300,
		opacity:0.1,
		content:html.join(''),
		onClick:function($el){
			if($el.hasClass('ok')){//确定按钮
				PersonalPasswordAuth.authentication(this,param,okFun);
			}else if($el.hasClass('close')){//关闭按钮
				this.close();
				closeFun.call(this);
			}
		},
		onKeyup:function(e){
			var k =e.charCode||e.keyCode||e.which;
			if(k==13){//回车
				PersonalPasswordAuth.authentication(this,param,okFun);
			}
		},
		onClose:function(){
			$(document).unbind('contextmenu.personalPasswordAuth');
		}
	};
	var doShow=function(){
		var div=Public.dialog(options);
		$('a.close',div).hide();
		$('#page-password-input').focus();
		$(document).bind("contextmenu.personalPasswordAuth",function(e){  
            return false;  
        }); 
	};
	//需要验证密码时限
	if(checkTimeLimit){
		//验证密码时限
		Public.ajax(PersonalPasswordAuth.checkUrl, {}, function(data) {
			if(data=='ok'){//不需要验证密码
				if($.isFunction(okFun)){
					//以new PersonalPasswordAuth()执行避免 okFun中的this指针错误
					okFun.call(new PersonalPasswordAuth(),data);
				}
			}else{//打开对话框
				doShow();
			}
		});
	}else{
		doShow();
	}
};
//进行密码验证
PersonalPasswordAuth.authentication=function(obj,param,fn){
	var password=$('#page-password-input').val();
	if(password==''){
		Public.tip('请输入访问密码!');
		return;
	}
	param['password']=password;
	Public.ajax(PersonalPasswordAuth.url,param, function(data) {
		if($.isFunction(fn)){
			fn.call(obj,data);
		}
	});
};