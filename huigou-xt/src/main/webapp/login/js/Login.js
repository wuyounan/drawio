if (self != top) {
    top.location = self.location;
}

var LoginStatus = {
    UNKNOWN_ERROR: 0,
    SUCCESS: 1,
    USER_NOT_EXIST_OR_PASSWORD_ERROR: 2,
    USER_DISABLED: 3,
    USER_LOGIC_DELETE: 4,
    USER_LOCKED: 5,
    LOGIN_LIMIT: 6,
    SECURITY_POLICY: 7
};


var LoginSetting = function (options) {
    this.cookieName = "huigou-login";
    this.data = $.evalJSON($.cookie(this.cookieName) || "{}") || {};
    this.init();
};

LoginSetting.prototype = {
    init: function () {
        var _self = this;
        // 保存用户名
        this.rememberEl = $("#remember").click(function () {
            _self.set("remember", this.checked);
        });
        // 其他设置 ...
    },
    load: function () {
        this.rememberEl.get(0).checked = !!this.data["remember"];
        if (this.rememberEl.get(0).checked) {
            $('#userName').val(CryptoJS.AES.decrypt(this.data["userName"]));
        }
    },
    set: function (name, value) {
        if (this.data[name] != value) {
            this.data[name] = value;
            this.save();
        }
    },
    get: function (name) {
        return this.data[name];
    },
    save: function () {
        $.cookie(this.cookieName, $.toJSON(this.data), {expires: 7, path: '/'});
    }
};

var loginSetting;

$(function () {
    loginSetting = new LoginSetting();
    loginSetting.load();
    bindEvents();
    if ($('#screenOverLoading').length > 0) {
        Public.tips({type: 1, content: '登录超时，请重新登录。'});
        if ($('#jobPagePanelBar').length > 0) {
            window.location.href = web_app.name + '/Login.jsp';
        }
    }
});

function changeVerifyCode(){
	var verifyCode=$('#verifyCode');
	if(verifyCode.length > 0){
		 $('#verifyCodeImg').triggerHandler('click');
		 verifyCode.val('');
	}
}

function bindEvents() {
    $("#btnLogin").click(function () {
        login();
    });
    $("#btnClose").click(function () {
        closeWebPage();
    });
    $("#userName").placeholder($.i18nProp('common.username'));
    $("#password").placeholder($.i18nProp('common.password'));
    var verifyCode=$('#verifyCode');
	if(verifyCode.length > 0){
		verifyCode.placeholder($.i18nProp('common.verifyCode'));
		$('#verifyCodeImg').css({cursor:'pointer'}).on('click',function(){
	        $(this).attr('src',  web_app.name + '/verifycode.load?tm=' + Math['random']());
		});
	}
	//语言切换
	$('.switch-language').on('click',function(e){
		var $el = $(e.target || e.srcElement);
		if($el.is('a')){
			var language=$el.data('language'),lang='',country='';
			language=language.split('_');
			lang=language[0];
			country=language.length>1?language[1]:'';
			Public.ajax( web_app.name +'/switchLanguage.ajax', {lang:lang,country:country}, function (data) {
				window.location.reload();
		    });
		}
	});
    $(document).bind('keydown', function (e) {
        var k = e.charCode || e.keyCode || e.which;
        if (k == '13') {
            login();
        }
    });
}

function login() {
    var params = {
        userName: $.trim($('#userName').val()),
        password: $.trim($('#password').val())
    };

    if (params.userName.length < 1) {
        Public.tip($.i18nProp('common.warning.notnull',$.i18nProp('common.username')));
        $("#userName").focus();
        return;
    }

    if (params.password.length < 1) {
        Public.tip($.i18nProp('common.warning.notnull',$.i18nProp('common.password')));
        $("#password").focus();
        return;
    }
    
    var verifyCodeDom=$('#verifyCode');
	if(verifyCodeDom.length > 0){
		var verifyCode=$.trim(verifyCodeDom.val());
		if (verifyCode.length < 1) {
			Public.tip($.i18nProp('common.warning.notnull',$.i18nProp('common.verifyCode')));
	        verifyCodeDom.focus();
	        return;
	    }
		 params.verifyCode = verifyCode;
	}
    
    params.userName = Public.encodeURI(CryptoJS.AES.encrypt(params.userName)); 
    params.password = Public.encodeURI(CryptoJS.AES.encrypt(params.password));
    
    var tip = Public.tips({content: $.i18nProp('common.field.loading'), autoClose: false});
    $.ajax({
        type: "POST",
        url: web_app.name + '/login.ajax',
        cache: false,
        async: true,
        timeout: 1000 * 50,
        dataType: "json",
        data: params || {},
        // 当异步请求成功时调用
        success: function (data) {
            var result = data.data;
            tip.remove();
            if (result) {
                switch (result.status) {
                    case LoginStatus.UNKNOWN_ERROR:
                    case LoginStatus.USER_NOT_EXIST_OR_PASSWORD_ERROR:
                    case LoginStatus.USER_DISABLED:
                    case LoginStatus.USER_LOGIC_DELETE:
                    case LoginStatus.USER_LOCKED:
                    case LoginStatus.LOGIN_LIMIT:
                    case LoginStatus.SECURITY_POLICY:
                        Public.tips({type: 1, content: result.message});
                        $("#userName").focus();
                        changeVerifyCode();
                        break;
                    case LoginStatus.SUCCESS:
                        if (loginSetting.get("remember")) {
                            loginSetting.set("userName", CryptoJS.AES.encrypt($.trim($('#userName').val())));
                        } else {
                            loginSetting.set("userName", "");
                        }
                        window.location.href = web_app.name + '/Index.jsp';
                        break;
                }
            } else {
                Public.tips({type: 1, content:data.message||'未知错误，请联系管理员。'});
                changeVerifyCode();
                $('#password').val('');
            }
        },
        // 当请求出现错误时调用
        error: function (err) {
            //tip.change({ type: 1, content : '操作失败了哦！', time: 3000 });
            Public.tips({type: 1, content: err.responseText, time: 3000});
            changeVerifyCode();
            $('#password').val('');
        },
        complete: function (XMLHttpRequest, status) { //请求完成后最终执行参数
            if (status == 'timeout') {//超时,status还有success,error等值的情况
                tip.remove();
                changeVerifyCode();
                $('#password').val('');
                Public.tips({type: 1, content: "请求超时，请重新登录。", time: 3000});
            }
        }
    });
}
