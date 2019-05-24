var Public = Public || {};
Public.isReadOnly = false;//判断当前页面是否只读(查看模式)
Public.manageTypeParmName = 'sys_Manage_Type';//系统业务管理权限参数名称
/**只读方法匹配正则表达式**/
Public.readOnlyAttributes = ['save*', 'update*', 'delete*', 'add*', 'insert*', 'edit*', 'remove*', 'batchUpdate*'];
//页面初始化方法
$(document).ready(function () {
	 //从参数中读取只读参数
	var _isReadOnly=$('#mainPageReadOnlyFlag').val();
	if (Public.isBlank(_isReadOnly)) {
		_isReadOnly=Public.getQueryStringByName("isReadOnly");
	}
    Public.isReadOnly = _isReadOnly === 'true';
    //延迟显示内容
    setTimeout(function () {
        $('#ui-screen-over-loading').hide();
        Public.mobilePackage();
    }, 500);
    //默认事件过滤
    $(document).keydown(function (event) {
        var k = event.charCode || event.keyCode || event.which;
        var $el = $(event.target || event.srcElement);
        if (k == '8') {
        	 //如果标签不是input或者textarea则阻止Backspace  
            if (!$el.is('input') && !$el.is('textarea')) {  
            	return Public.stopEvent(event);
            } 
            //input或者textarea输入框如果不可编辑则阻止Backspace  
            if (($el.is('input') || $el.is('textarea')) && ($el[0].readOnly == true || $el[0].disabled == true)) {  
            	return Public.stopEvent(event);
            }
        }
        if(k==13 && !$el.is('textarea')){// 回车
        	return Public.stopEvent(event);
		}
    });
    Public.autoInitializeUI();
});

//手机端增加包裹控制(使用div滚动条不使用body滚动条)
Public.mobilePackage=function(){
	//iphone 手机点击后会自动置顶问题
	if(!$.isIosPhone){
		return;
	}
	var mainPackage=$('body').find('>div.main-package-over');
	if(mainPackage.length){
		mainPackage.on('scroll', function () {$.closePicker();});
		$('body').addClass('dom-overflow');//阻止body滚动
		var _setHeight=function(){
			mainPackage.addClass('dom-overflow-auto').height($.windowHeight());//包裹div设置为滚动
		};
		_setHeight();
		$.WinReszier.register(function(){_setHeight();});
	}
};

Public.stopEvent=function(event){
	event.preventDefault();
    event.stopPropagation();
    return false;
};
//判断输入的name 是否匹配只读方法正则表达式
Public.testReadOnlyAttributes = function (name) {
    if (Public.isBlank(name)) {
        return false;
    }
    var flag = false, re = null;
    $.each(Public.readOnlyAttributes, function (i, o) {
        re = new RegExp(o, "i");
        if (re.test(name)) {
            flag = true;
            return true;
        }
    });
    return flag;
};
//自动加载UI插件
Public.autoInitializeUI = function ($doc) {
    $doc = $doc || $('body');
    if(window['UICtrl']){
    	UICtrl.autoGroupAreaToggle($doc);
    }
    if ($.fn.combox) {
        $('select', $doc).each(function (i, o) {
            if ($(this).hasClass('noWrapper')) return;
            try { $(o).combox();} catch (e) {return false;}
        });
    }
    $('textarea', $doc).each(function (i, o) {
    	if ($.fn.maxLength) {
        	$(o).maxLength();
        }
    });
    $('input[type="text"]', $doc).each(function (i, o) {
        var $o = $(o), dataOptions = $.trim($o.attr('dataOptions'));
        if ($.fn.mask) {
            var mask = $o.attr('mask');
            if (mask == 'money') {
                $o.mask('nnnnnnnnnnn.99', {number: true, money: true});
            } else if (mask == 'positiveMoney') {
                $o.mask('9999999999.99', {number: true, positiveMoney: true});
            } else if (mask == 'date') {
                $o.mask('9999-99-99');
            } else if (mask == 'dateTime') {
                $o.mask('9999-99-99 99:99');
            } else if (mask == 'number') {
                var length = parseInt($o.attr('maxLength')), precision = parseInt($o.attr('precision'));
                var tempMask = [];
                if (length && !isNaN(length)) {
                    for (var i = 0; i < length; i++) {
                        tempMask.push('n');
                    }
                }
                if (precision && !isNaN(precision) && precision > 0) {
                    tempMask.push('.');
                    for (var i = 0; i < precision; i++) {
                        tempMask.push('n');
                    }
                }
                if (tempMask.length > 0) {
                    $o.mask(tempMask.join(''), {number: true});
                }
            } else if (mask) {
                $o.mask(mask, {number: /^[0-9|n|.]+$/i.test(mask)});
            }
        }
        dataOptions = Public.getJSONDataSource(dataOptions);
        if ($o.is('[date]')) {
            if ($.fn.datepicker) {
                $o.datepicker().mask('9999-99-99');
            }
        } else if ($o.is('[dateTime]')) {
            if ($.fn.datepicker) {
                $o.datepicker({useTime: true}).mask('9999-99-99 99:99');
            }
        } else if ($o.is('[spinner]')) {
            if ($.fn.spinner) {
                $o.spinner(dataOptions);
            }
        } else if ($o.is('[select]')) {
            if ($.fn.combox && dataOptions) {
                $o.searchbox(dataOptions);
            }
        } else if ($o.is('[combo]')) {
            if ($.fn.combox && dataOptions) {
                $o.combox(dataOptions);
            }
        } else if ($o.is('[tree]')) {
            if ($.fn.combox && dataOptions) {
                if (dataOptions.name == 'org') {
                    $o.orgTree(dataOptions);
                } else {
                    $o.treebox(dataOptions);
                }
            }
        } else if ($o.is('[dictionary]')) {
            if ($.fn.combox && dataOptions) {
                $o.remotebox(dataOptions);
            }
        }else if ($o.is('[month]')) {
            if ($.fn.monthpicker) {
            	$o.monthpicker().mask('9999-99');
            }
        }else if ($o.is('[year]')) {
        	 if ($.fn.yearbox) {
        		 $o.yearbox();
        	 }
        }
    });
    if($doc.is('body')){
    	//兼容IE加入延迟处理(ie8/9页面初始化会延迟)
    	setTimeout(function(){
    		Public.autoElementWrapperHeight($doc);
    	},10);
    }else{
    	//控件显示高度动态计算
        Public.autoElementWrapperHeight($doc);
    }
};
Public.getJSONDataSource = function (dataSource) {//读取Json数据源
    try {
        if (dataSource && typeof dataSource == 'string') {
            if (dataSource.substring(0, 1) != "{") {
                dataSource = ["{", dataSource, "}"].join('');
            }
            dataSource = (new Function("return " + dataSource))();
        }
        return dataSource;
    } catch (e) {
        return {};
    }
};
//控件显示高度动态计算
Public.autoElementWrapperHeight = function (doc) {
	//返回false 可以阻止$.WinReszier.register 注册的事件继续生效
	var _fn=function($doc){
		return function(){
			if(!$doc.length){
				return false;
			}
			//判断是否存在于文档流中
			if($doc.is('body')){
				if(!$doc.parent().length){
					return false;
				}
			}else{
				if(!$doc.parents('body').length){
					return false;
				}
			}
			var _automaticHeightDiv=$('div.ui-automatic-height',$doc);
			if(!_automaticHeightDiv.length){
				return false;
			}
			var _flag=false;
			_automaticHeightDiv.each(function(){
				if($(this).is(':hidden')){
					return false;
				}
				//判断是否存在父节点
				var _parent=$(this).parent(),_parentWidth=_parent.width();
				if(_parent.hasClass('hg-form-row')){
					var _prev=$(this).prev(),_divs=null;
					var _width=$(this).width();
					if(_prev.length>0){
						_width+=_prev.width();
					}
					if(_width>_parentWidth-10){
						if(!_prev.length){
							return true
						}
						_divs=_prev;
					}else{
						//全部同辈元素
						_divs=$(this).siblings('div');
						if(!_divs.length){
							return true
						}
					}
					_divs=_divs.add(this);
					_divs.height(20);//先设置变小 再重新计算
					var heights = _divs.map(function() {
				        return this.scrollHeight;
				    }).get();
					heights.push(37);
					_divs.height(Math.max.apply(null, heights));
					_flag=true;
				}
			});
			return _flag;
		}
	}(doc);
	if(!_fn()){
		return false;
	}
	if($(doc).is('body')){
		$.WinReszier.register(_fn);
	}
};

Public.closeWebPage = function () {
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
            window.opener = null;
            window.close();
        }else {
            window.open('', '_top');
            window.top.close();
        }
    }else if (navigator.userAgent.indexOf("Firefox") > 0) {
        window.location.href = 'about:blank';
        window.close();
    }else {
        window.opener = null;
        window.open('', '_self', '');
        window.close();
    }
};
/*装入指定 URL 的另外文档来替换当前文档*/
Public.locationHref=function(action,param){
	param=param||{};
	var cp={a:new Date().getTime()};
	if(param['isReadOnly']===true){
		cp['isReadOnly']=true;
	}
	cp=$.extend(cp,param);
	var url=$.getCSRFUrl(action,cp);
	window.location=url;
	/*var url=DataUtil.composeURLByParam(action,cp);
	$('#screenOverLoading').show();
	var tempForm=$('<form method="post"></form>');
	tempForm.attr('action',url);
	if($.isPlainObject(param)){
		$.each(param,function(p,v){
			v=String(v);
			var hideInput=$('<input type="hidden" name="'+p+'"/>');
			hideInput.val(encodeURI(v));
			tempForm.append(hideInput);
		});
	}
	var _csrfToken=$('<input type="hidden" name="csrfToken"/>');
	_csrfToken.val($.getCSRFTokenAsBizData(param['bizId']));
	tempForm.append(_csrfToken);
	tempForm.appendTo('body');
	tempForm.trigger('submit');*/
};
//使用post方式打开 open窗口
Public.openPostWindow = function (url, param) {
	param=param||{};
	if (url.indexOf(web_app.name + '/') == -1) {
		url=web_app.name+'/'+url;
	}
	if($.isMobile()){
		url=$.getCSRFUrl(url,param);
		window.open(url);
	}else{
	    if (!$.browser.msie) {
	        var name = "form" + new Date().getTime();
	        var tempForm = $('<form method="post"></form>');
	        tempForm.attr('action', url);
	        tempForm.attr('target', name);
	        $.each(param, function (p, v) {
	        	v=String(v);
	            var hideInput = $('<input type="hidden" name="' + p + '"/>');
	            hideInput.val(encodeURI(v));
	            tempForm.append(hideInput);
	        });
	        tempForm.appendTo('body');
	        tempForm.on('submit', function () {
	            window.open('about:blank', name);
	        });
	        var _csrfToken=$('<input type="hidden" name="csrfToken"/>');
	    	_csrfToken.val($.getCSRFTokenAsUrlData(url, param));
	    	tempForm.append(_csrfToken);
	        tempForm.trigger('submit');
	        tempForm.remove();
	    } else {
	        var newWin = window.open('about:blank', '_blank');
	        var body = $(newWin.document.body);
	        body.html('<form method="post" ></form>');
	        var tempForm = $('form', body);
	        tempForm.attr('action', url);
	        var html = [];
	        $.each(param, function (p, v) {
	        	v=String(v);
	            html.push('<input type="hidden" name="', p, '" value="', encodeURI(v), '"/>');
	        });
	        html.push('<input type="hidden" name="csrfToken" value="', $.getCSRFTokenAsUrlData(url, param), '"/>');
	        tempForm.html(html.join(''));
	        tempForm.trigger('submit');
	    }
	}
    return false;
};

Public.isBlank = function (obj) {
    if (typeof obj == 'undefined') {
        return true;
    }
    if (obj == null) {
        return true;
    }
    if (!(obj+'').length) {
        return true;
    }
    return false;
};

Public.isNotBlank = function(obj){
	return !Public.isBlank(obj);
};

Public.encodeJSONURI = function (data) {
	return Public.encodeURI($.toJSON(data));
};

Public.encodeURI = function (msg) {
    if (Public.isBlank(msg)) {
        return '';
    }
    var value = encodeURI(msg);
    value = value.replace(/\&/g, "%26");
    value = value.replace(/\+/g, "%2B");
    return encodeURI(value);
};

/*将参数中的方法逐个调用，返回第一个成功执行的方法的返回值 */
Public.these = function () {
    var returnValue = null;
    for (var i = 0; i < arguments.length; i++) {
        var fn = arguments[i];
        try {
            returnValue = fn();
            break;
        } catch (e) {
        }
    }
    return returnValue;
};

//检测图片是否存在
Public.checkImgExists = function (src, callback) {
    var ImgObj = new Image(); //判断图片是否存在
    ImgObj.src = src; //没有图片，则返回-1
    /*if (ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0)) {
        return true;
    } else {
    	callback.call(window);
    }*/
    ImgObj.onerror = function () {//图片不存在时显示默认
        callback.call(window);
    };
};

//对象比较大小
Public.compareObject = function (o1, o2) {
    if (typeof o1 != typeof o2) return false;
    if (typeof o1 == 'object') {
        for (var o in o1) {
            if (typeof o2[o] == 'undefined')return false;
            if (!Public.compareObject(o1[o], o2[o]))return false;
        }
        for (var o in o2) {
            if (typeof o1[o] == 'undefined')return false;
            if (!Public.compareObject(o2[o], o1[o]))return false;
        }
        return true;
    } else {
        return o1 === o2;
    }
};

//字符串转数字
Public.toNumber = function (value, def) {
	def=def||0;
	if (Public.isBlank(value)) {
		return def;
	}
	value=value.toString();
	value=value.replace(/[,]/g, '');
	var num=parseFloat(value,10);
	if(isNaN(num)){
		return def
	}
    return num;
};

/*默认对话框宽度*/
function getDefaultDialogWidth() {
    var width = $.windowWidth();
    if(width < 700){
    	width = width * 95 / 100;
    }else{
    	width = width * 80 / 100;
    }
    return width;
}
/*默认对话框高度*/
function getDefaultDialogHeight() {
    var height = $.windowHeight();
    height = height * 80 / 100;
    return height;
}

/* 获取URL参数值 */
Public.getQueryStringByName = function (name) {
    var param, url = location.search, theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0, len = strs.length; i < len; i++) {
            param = strs[i].split("=");
            theRequest[param[0]] = decodeURIComponent(param[1]);
        }
    }
    return name ? theRequest[name] : theRequest;
};

/*管理权限鉴权方法*/
Public.authenticationManageType = function (manageType, fullId, fn) {
    var url = web_app.name + '/authenticationManageType.ajax';
    Public.ajax(url, {manageType: manageType, fullId: fullId}, function (data) {
        if ($.isFunction(fn)) {
            fn.call(window, ((data + '') == 'true'));
        }
    });
};

/* 格式化金额 */
Public.currency = function (val) {
    if (Public.isBlank(val)) {
        return '';
    }
    if (typeof val == 'string') {
        try {
            val = val.replace(/[,]/g, '');
        } catch (e) {

        }
    }
    val = parseFloat(val, 10);
    if (isNaN(val)) {
        return '';
    }
    if (val === 0) {
        return '0.00';
    }
    val = val.toFixed(2);
    var reg = /(\d{1,3})(?=(\d{3})+(?:$|\D))/g;
    return val.replace(reg, "$1,");
};
/* 格式化金额 */
Public.number = function (val, mask) {
    val = parseFloat(val, 10);
    if (isNaN(val))
        return '';
    var d = parseInt(mask);
    if (typeof mask == 'string') {
        var ms = mask.split('.');
        if (ms.length > 1)
            d = ms[1].length;
    }
    if (isNaN(d))
        return '';
    return val.toFixed(d);
};
/* 金额格式转浮点数*/
Public.moneyToFloat = function (amount) {
    amount = parseFloat((amount + "").replace(/[,]/g, ""));
    return amount;
};
/*判断是否为日期格式*/
Public.isDate = function (dateString) {
    if (Public.isBlank(dateString)) return true;
    var r = dateString.match(/^(\d{1,4})(-|\/|\.)(\d{1,2})\2(\d{1,2})$/);
    if (r == null) {
        return false;
    }
    var d = new Date(r[1], r[3] - 1, r[4]);
    var num = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
    return (num != 0);
};
/*判断是否为日期加时间格式*/
Public.isDateTime = function (dateString) {
    if (Public.isBlank(dateString)) return true;
    var r = dateString.match(/^(\d{1,4})(-|\/|\.)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/);
    if (r == null) {
        return false;
    }
    var d = new Date(r[1], r[3] - 1, r[4], r[5], r[6], 0);
    var num = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
    return (num != 0);
};
Public.isDateTimeSecond = function (dateString) {
    if (Public.isBlank(dateString)) return true;
    var r = dateString.match(/^(\d{1,4})(-|\/|\.)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
    if (r == null) {
        return false;
    }
    var d = new Date(r[1], r[3] - 1, r[4], r[5], r[6], r[7]);
    var num = (d.getFullYear() == r[1]
        && (d.getMonth() + 1) == r[3]
        && d.getDate() == r[4]
        && d.getHours() == r[5]
        && d.getMinutes() == r[6]
        && d.getSeconds() == r[7]
    );
    return num
};
/* 根据类型验证输入的数据 */
Public.validator = function (type, value) {
    if (Public.isBlank(value)) {
        return true;
    }
    switch (type) {
        case 'email':
            return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i
                .test(value);
        case 'url':
            return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
                .test(value);
        case 'ip':
            return /^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([1-9]|([1-9]\d)|(0)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))(\/\d+)?$/
                .test(value);
        case 'length':
            var len = $.trim(value).length;
            return len >= arguments[2] && len <= arguments[3];
        case 'phone':
            return /^(\+?(86))?0?1\d{10}$/i.test(value);
        case 'qq':
            return /[1-9][0-9]{4,}/i.test(value);
        case 'number':
            return /^[0-9|\-|\.]*$/i.test(value);
        case 'identity':
            return /^\d{15}|\d{}18$/i.test(value);
        case 'letter':
            return /^[A-Za-z0-9|-]+$/i.test(value);
        case 'englishCode':
        	return /^[^\u4e00-\u9fa5]+$/i.test(value);
        default:
            var reg = new RegExp(type, "g");
            return reg.test(value);
    }
};

Public.validatorErrorMessage = function (type) {
	if (Public.isBlank(type)) {
        return $.i18nProp('common.warning.validator.default');
    }
    switch (type) {
        case 'email':
        case 'url':
        case 'ip':
        case 'phone':
        case 'identity':
            return $.i18nProp('common.warning.validator.default');
        case 'qq':
        case 'number':
            return $.i18nProp('common.warning.validator.number');
        case 'letter':
            return $.i18nProp('common.warning.validator.letter');
        case 'englishCode':
        	return $.i18nProp('common.warning.validator.englishCode');
        default:
        	return $.i18nProp('common.warning.validator.default');
    }
};
/* 将时间对象 按格式转化为字符串 */
Public.formatDate = function (da, fmt) {
    if (!da)
        return '';
    fmt = fmt || '%Y-%M-%D';
    var s = {};
    var m = da.getMonth();
    var d = da.getDate();
    var y = da.getFullYear();
    var hr = da.getHours();
    var min = da.getMinutes();
    var sec = da.getSeconds();
    s["%d"] = d;
    s["%D"] = (d < 10) ? ("0" + d) : d;
    s["%H"] = (hr < 10) ? ("0" + hr) : hr;
    s["%h"] = hr;
    s["%M"] = (m < 9) ? ("0" + (1 + m)) : (1 + m);
    s["%m"] = (1 + m);
    s["%I"] = (min < 10) ? ("0" + min) : min;
    s["%i"] = min;
    s["%n"] = "\n";
    s["%s"] = sec;
    s["%S"] = (sec < 10) ? ("0" + sec) : sec;
    s["%t"] = "\t";
    s["%y"] = ('' + y).substr(2, 2);
    s["%Y"] = y;
    s["%%"] = "%";
    var re = /%./g, a = fmt.match(re);
    for (var i = 0; i < a.length; i++) {
        var tmp = s[a[i]];
        if (tmp) {
            re = new RegExp(a[i], 'g');
            fmt = fmt.replace(re, tmp);
        }
    }
    return fmt;
};
/* 解析字符串为时间对象str 字符串 fmt 格式化参数 */
Public.parseDate = function (str, fmt) {
    var today = new Date();
    if (!str || str == '')
        return today;
    fmt = fmt || '%Y-%M-%D';
    var y = 0, m = -1, d = 0;
    var a = str.split(/\W+/);
    var b = fmt.match(/%./g);
    var i = 0, j = 0;
    var hr = 0, min = 0, ss = 0;
    for (i = 0; i < a.length; ++i) {
        if (!a[i])
            continue;
        switch (b[i]) {
            case "%d":
            case "%D":
                d = parseInt(a[i], 10);
                break;
            case "%M":
            case "%m":
                m = parseInt(a[i], 10) - 1;
                break;
            case "%Y":
            case "%y":
                y = parseInt(a[i], 10);
                (y < 100) && (y += (y > 29) ? 1900 : 2000);
                break;
            case "%H":
            case "%h":
                hr = parseInt(a[i], 10);
                break;
            case "%I":
            case "%i":
                min = parseInt(a[i], 10);
                break;
            case "%S":
            case "%s":
                ss = parseInt(a[i], 10);
                break;
        }
    }
    if (isNaN(y))
        y = today.getFullYear();
    if (isNaN(m))
        m = today.getMonth();
    if (isNaN(d))
        d = today.getDate();
    if (isNaN(hr))
        hr = today.getHours();
    if (isNaN(min))
        min = today.getMinutes();
    if (isNaN(ss))
        ss = today.getSeconds();
    if (y != 0 && m != -1 && d != 0)
        return new Date(y, m, d, hr, min, ss);
    y = 0;
    m = -1;
    d = 0;
    for (i = 0; i < a.length; ++i) {
        if (parseInt(a[i], 10) <= 12 && m == -1) {
            m = a[i] - 1;
        } else if (parseInt(a[i], 10) > 31 && y == 0) {
            y = parseInt(a[i], 10);
            (y < 100) && (y += (y > 29) ? 1900 : 2000);
        } else if (d == 0) {
            d = a[i];
        }
    }
    if (y == 0)
        y = today.getFullYear();
    if (m != -1 && d != 0)
        return new Date(y, m, d, hr, min, ss);
    return today;
};
//日期大小比较
Public.compareDate = function (max, min) {
    var re = /-/g;
    var rs = / /g;
    var ra = /:/g;
    var newmax = max.replace(re, "");
    newmax = newmax.replace(rs, "");
    newmax = newmax.replace(ra, "");
    var newmin = min.replace(re, "");
    newmin = newmin.replace(rs, "");
    newmin = newmin.replace(ra, "");
    if (newmax > newmin) {
        return 1;
    } else if (newmax == newmin) {
        return 0;
    } else {
        return -1;
    }
};

//简单对话框实现
Public.dialog = function (options) {
	var dialogTitle=$.i18nProp(options.title || '');
    var html = ['<div class="ui-public-dialog">',
        '<div class="ui-public-dialog-title">',
        '<span class="icos_title"><i class="fa fa-commenting"></i></span>',
        '<span class="msg_title">', dialogTitle , '</span>',
        '<span class="icos"><a href="javascript:void(0)" hidefocus  class="ui-public-dialog-close">&nbsp;</a></span>',
        '</div>',
        '<div class="ui-public-dialog-content">'];
    html.push(options.content || '');
    html.push('</div></div>');
    var div = $(html.join('')).appendTo('body');
    div.drag({handle: 'div.ui-public-dialog-title', opacity: 0.8});
    this.close = function () {
        if ($.isFunction(options.onClose)) {
            if(options.onClose.call(window)===false){
            	return;
            }
        }
        div.removeAllNode();
        screenOver.hide();
    };
    var screenOver = $('#jquery-screen-over');
    if (!screenOver.length) {
    	var _style="position:fixed;left:0;top:0;width:100%;height:100%;background:#001;overflow:hidden;z-index:10000;display:none;";
        screenOver = $('<div id="jquery-screen-over" style="'+_style+'"></div>').appendTo('body');
        if($.isIosPhone){
        	screenOver.on('touchmove', function(event) {
			    event.preventDefault();
			    return false;
			});
		}
    }
    var width = options.width || 300,height = options.height || 300;
    if (height < 300) {
        height = 300;
    }
    var _def=getDefaultDialogWidth();
	if(width>_def-30){
		width=_def/0.9-30;
	}

	var left = 0,top = 0;
    if (options.left != null){
    	left = options.left;
    }else{
    	left = 0.5 * ($.windowWidth() - width);
    }
    if (options.top != null){
    	top = options.top;
    }else{
    	top = 0.5 * ($.windowHeight() - height) + $.windowScrollTop() - 10;
    }
    if (left < 0){
    	left = 0;
    }
    if (top < 0){
    	top = 0;
    }
    var opacity = options.opacity || 0.1;
    var _self = this;
    div.css({width: width, top: top, left: left}).show();
    screenOver.css({filter: 'alpha(opacity=' + (opacity * 100) + ')',opacity: opacity}).show();

    div.find('a.ui-public-dialog-close').on('mousedown', function () {
        _self.close();
    });
    div.on('click', function (e) {
        var $clicked = $(e.target || e.srcElement);
        if($clicked.is('i.fa')){$clicked=$clicked.parent();}
        if ($.isFunction(options.onClick)) {
            options.onClick.call(_self, $clicked,div);
        }
    }).on('keyup', function (e) {
        if ($.isFunction(options.onKeyup)) {
            options.onKeyup.call(_self, e);
        }
    });
    return div;
};

/**********页面页面提示通知封装*************/
Public.tip = function (message) {
	message=$.i18nProp.apply(window,arguments);
	return Public.tips({type: 2, content: message});
};
Public.successTip = function (message) {
	message=$.i18nProp.apply(window,arguments);
	return Public.tips({type: 0, content: message});
};
Public.errorTip = function (message) {
	message=$.i18nProp.apply(window,arguments);
	return Public.tips({type: 1, content: message});
};
Public.infoTip = function (message) {
	message=$.i18nProp.apply(window,arguments);
	return Public.tips({type: 3, content: message});
};
Public.tips = function(options) {
	$.removeTips();
	return $.tips(options);
};
/** ******ajax 请求*********** */
// url:请求地址， params：传递的参数{...}， callback：请求成功回调
Public.ajax = function (url, params, successCallback, failCallback) {
    var tip = Public.tips({autoClose: false});
    return $.ajax({type: "POST", url: url, cache: false, async: true, dataType: "json", data: params || {},
        success: function (data) {
        	tip.remove();
            Public.ajaxCallback(data, successCallback, failCallback);
        },
        error: function (err) {
        	tip.change({ type: 1, content : err.responseText, time: 3000 });
        }
    });
};

Public.syncAjax = function (url, params, successCallback, failCallback) {
    var tip = Public.tips({autoClose: false});
    return $.ajax({type: "POST", url: url, cache: false, async: false, dataType: "json", data: params || {},
        success: function (data) {
        	tip.remove();
            Public.ajaxCallback(data, successCallback, failCallback);
        }
    });
};
// url:请求地址， params：传递的参数{...}， callback：请求成功回调
Public.load = function (url, params, callback) {
	return $.ajax({type: "POST", url: url, cache: false, async: true, dataType: "text", data: params,
        success: function (data) {
            if ($.isFunction(callback)) {
                callback.call(window, data);
            }
        },
        error: function (err) {
        	Public.errorTip(err.responseText);
        }
    });
};
/*ajax 回调方法*/
Public.ajaxCallback = function (data, successCallback, failCallback) {
    var status = parseInt(data.status, 10),tips = $.tips;
    var dialog=UICtrl.getDialog(true);
    switch (status) {
        case 0 :
            if ($.isFunction(successCallback)) {
                successCallback(data.data);
            }
            break;
        case 1:
            tips({content:data.message|| $.i18nProp('common.tip.success'), type: 0, timeOut: 1500});
            if ($.isFunction(successCallback)) {
                successCallback(data.data);
            }
            break;
        case 2:
        case 3:
            if ($.isFunction(failCallback)) {
                if(failCallback(data)===false){
                	return;
                }
            }
            if (!dialog) {
                tips({type: 1, content: data.message});
                return;
            }
            var message = data.message, mkeys = 'TaskListener:';
            message = message || '空指针错误!';
            var index = message.lastIndexOf(mkeys);
            if (index > -1) {
                message = message.substring(index + (mkeys.length));
            }
            mkeys = "ApplicationException:";
            var index = message.lastIndexOf(mkeys);
            if (index > -1) {
                message = message.substring(index + (mkeys.length));
            }
            dialog({title: 'common.dialog.alert',icon: 'alert.gif',fixed: true,lock: true,resize: false,
                ok: function () {
                    if (data.isAuth) {
                        if ($.isFunction(window['doLogout'])) {
                            window['doLogout'].call(window);
                        } else {
                            try {
                                parent.doLogout();
                            } catch (e) {
                            }
                        }
                    }
                    return true;
                },
                content: '<div style="word-break:break-all;padding-top:20px;">' + message + '</div>'
            });
            break;
        default :
            alert('回调状态无法识别,error status!');
            return;
    }
};

Public.getRadioValue =function(radios){
	var value="";
	radios.each(function(){
		if($(this).is(':checked')){
			value=$(this).val();
    		return false;
    	}
	});
	return value;
};

//设置form组件的默认值
Public.updateDefaultValue=function($dom){
	$dom=$($dom)||$('body');
    $dom.find('input[type=text], textarea').each(function(){ this.defaultValue = this.value; });
    $dom.find('input[type=checkbox],input[type=radio]').each(function(){ this.defaultChecked = this.checked; });
    $dom.find('select option').each(function(){ this.defaultSelected = this.selected; });
};

//校验数据是否改变
Public.checkDefaultValueModified=function($dom){
	$dom=$($dom)||$('body');
	var isModified=false;
	$dom.find('input[type=text], textarea').each(function(){
		if($(this).hasClass('no-check-default-value')) return true;
		if(this.defaultValue != this.value){
			isModified=true;
			return false;
		}
	});
	if(isModified) return true;
	$dom.find('input[type=checkbox],input[type=radio]').each(function(){
		if($(this).hasClass('no-check-default-value')) return true;
		if(this.defaultChecked != this.checked){
			isModified=true;
			return false;
		}
	});
	if(isModified) return true;
	$dom.find('select option').each(function(){
		if($(this).hasClass('no-check-default-value')) return true;
		if(this.defaultSelected != this.selected){
			isModified=true;
			return false;
		}
	});
	return isModified;
};
//获取 UUID
Public.getUUID=function(){
	var s = [];
	var hexDigits = "0123456789abcdef";
	for (var i = 0; i < 36; i++) {
	   s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	}
	s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
	s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
	s[8] = s[13] = s[18] = s[23] = "-";
	var uuid = s.join("");
	uuid=uuid.toUpperCase().replace(/-/g,'');
	return uuid;
};

/**获取环境信息**/
var ContextUtil = ContextUtil || {};
ContextUtil.getOperator = function (key) {
    var Operator = {};
    if (!Public.isBlank(window['ContextOperator'])) {
        Operator = window['ContextOperator'];
    } else if (!Public.isBlank(parent['ContextOperator'])) {
        Operator = parent['ContextOperator'];
    }
    if (key) {
        return Operator[key];
    } else {
        return Operator;
    }
};

/**数据操作工具类**/
var DataUtil = DataUtil || {};

/**
 * 得到选择数据的id数组
 */
DataUtil.getSelectedIds = function (options) {
    var data = options.gridManager.getSelectedRows();
    if (!data || data.length < 1) {
        Public.tip(options.nochooseMessage || 'common.warning.nochoose');
        return false;
    }
    var idFieldName = options.idFieldName || "id";
    var ids = new Array();
    for (var i = 0; i < data.length; i++) {
        ids[i] = data[i][idFieldName];
        if ($.isFunction(options.onCheck)) {
            if (options.onCheck.call(window, data[i]) === false) {
                return false;
            }
        }
    }
    return ids;
};

/**
 * 删除
 */
DataUtil.del = function (options) {
    DataUtil.updateById($.extend({message: options.message || $.i18nProp('common.confirm.delete.unrecoverable')}, options));
};

/**
 * 得到更新的数据
 */
DataUtil.getUpdateRow = function(gridManager){
	return DataUtil.getSelectedRow(gridManager);
}

DataUtil.getSelectedRow = function(gridManager){
	var result = gridManager.getSelectedRows();
	if (result.length == 0){
		Public.tip('common.warning.nochoose');
		return false;
	}
	if (result.length > 1){
		Public.tip('common.warning.onlyonechoose');
		return false;
	}
	return result[0];
}

DataUtil.getUpdateRowId = function(gridManager){
	var result = DataUtil.getUpdateRow(gridManager);
	if (result){
		return result.id;
	}
	return result;
}

DataUtil.delSelectedRows = function (options) {
    var data = options.gridManager.getSelectedRows();
    if (!data || data.length < 1) {
    	Public.tip('common.warning.nochoose');
        return false;
    }
    var idFieldName = options.idFieldName || "id";
    var ids = new Array(), flag = true;
    var _do = function () {
        $.each(data, function (i, o) {
        	if ($.isFunction(options.onCheck)) {
                if (options.onCheck.call(window, o) === false) {
                    flag = false;
                    return false;
                }
            }
            if (Public.isBlank(o[idFieldName])) {//没有主键的数据在页面删除
                options.gridManager.options.clearScroll = true;
                options.gridManager.deleteRow(o);
                options.gridManager.deletedRows = null;
                return;
            }
            ids.push(o[idFieldName]);
        });
        if (flag === false) return;
        if (ids.length > 0) {
            var params = options.param || {};
            params.ids = $.toJSON(ids);
            Public.ajax(web_app.name + '/' + options.action, params, function (data) {
                if ($.isFunction(options.onSuccess))
                    options.onSuccess(data);
            });
        } else {
            if ($.isFunction(options.onDelete)) {
                options.onDelete.call(window);
            }
        }
    };
    if (options.inDialog) {
        if (window.confirm(options.message || $.i18nProp('common.confirm.delete'))) {
            _do();
        }
    } else {
        UICtrl.confirm(options.message || $.i18nProp('common.confirm.delete'), function () {
            _do();
        });
    }
};
/**
 * 更新todo
 */
DataUtil.updateById = function (options) {
    options = $.extend({message: false}, options);
    var ids = DataUtil.getSelectedIds(options);
    if (!ids) return;
    var params = options.param || {};
    params.ids = $.toJSON(ids);
    var _do = function () {
        Public.ajax(web_app.name + '/' + options.action, params, function (data) {
            if ($.isFunction(options.onSuccess))
                options.onSuccess(data);
        });
    };
    if (options.message === false) {
        _do();
    } else {
    	if (options.inDialog) {
    		if (window.confirm(options.message)) {
    	        _do();
    	    }
    	}else{
    		UICtrl.confirm(options.message, function () {
                _do();
            });
    	}
    }
};

//校验grid中是否存在数据
DataUtil.hasGridData =function(gridManager){
	if (!gridManager) {
    	Public.tip('common.warning.nochoose');
        return false;
    }
	var rows=gridManager.rows;
	if($.isArray(rows)){
		return rows.length > 0? true:false;
	}
	return false;
};

//获取表格数据
DataUtil.getGridData = function (options) {
	options =$.extend({isAllData:false} ,options || {});
    if (!options.gridManager) {
    	Public.tip('common.warning.nochoose');
        return false;
    }
    var grid = options.gridManager;
    var needCompareObject = true;//是否需要对比新增数据
    if (options['needCompareObject'] === false) {
        needCompareObject = false;
    }
    grid.endEdit();
    var data = [], params = [], flag = true;
    if(options.isAllData){
    	data=grid.getData();
    }else{
    	data=grid.getChanges();
    }
    //只寻找可以编辑的控件
    var editColumns = $.grep(options.gridManager.columns, function (n, i) {
        return $.isPlainObject(n.editor);
    });
    $.each(data, function (i, o) {
        var temp = $.extend({}, o);
        delete temp['__id']; //ligerGrid 中未删除__id属性
        if (needCompareObject && Public.compareObject(UICtrl.getAddGridData(options.gridManager), temp)) {//判断是否改变新增的数据
            return;
        }
        if ($.isFunction(options.onCheck)) {
            flag = options.onCheck.call(window, o);
        } else {
            $.each(editColumns, function (j, c) {
                if (c.editor['required']) {
                    if (Public.isBlank(o[c.name])) {
                        Public.tip('common.warning.notnull',c.display);
                        var obj = grid.getCellObj(o, c);
                        if (obj) {
                            setTimeout(function () {
                                grid._applyEditor(obj);
                            }, 10);
                        }
                        flag = false;
                        return false;
                    }
                }
                if (c.editor['match']) {
                    if (!Public.validator(c.editor['match'], o[c.name])) {
                        Public.tip('common.warning.validator',c.display);
                        var obj = grid.getCellObj(o, c);
                        if (obj) {
                            setTimeout(function () {
                                grid._applyEditor(obj);
                            }, 10);
                        }
                        flag = false;
                        return false;
                    }
                }
            });
        }
        if (flag === false) {
            return false;
        }
        params.push(o);
    });
    return flag ? params : false;
};
/**
 * 更新排序号
 */
DataUtil.updateSequence = function (options) {
    var data = options.gridManager.getData();

    if (!data || data.length < 1) {
        Public.tip('common.warning.nodata.save');
        return;
    }
    var param = options.param || {};
    var updateRowCount = 0;
    var idFieldName = options.idFieldName || "id";
    var sequenceFieldName = options.sequenceFieldName || "sequence";
    var params = new PublicMap();
    for (var i = 0; i < data.length; i++) {
        var txtSequence = "#txtSequence_" + data[i][idFieldName];
        txtSequence = txtSequence.replace(/\@/g,"_");
        var sequeceValue = $.trim($(txtSequence).val());
        if (sequeceValue == "" || isNaN(sequeceValue)) {
            Public.tip('common.warning.validator.number');
            $(txtSequence).focus();
            return;
        }
        var sequence = parseInt(sequeceValue);
        if (data[i][sequenceFieldName] != sequence) {
            params.put(data[i][idFieldName], sequence);
            updateRowCount++;
        }
    }

    if (updateRowCount == 0) {
        Public.tip('common.warning.nodata.modif');
        return;
    }
    param['data']=params.toString();
    Public.ajax(web_app.name + "/" + options.action, param , function (data) {
        if ($.isFunction(options.onSuccess))
            options.onSuccess(data);
    });
};

DataUtil.composeURLByParam = function (action, param) {
    var url = web_app.name + "/" + action;
    if(param){
		url+=(/\?/.test(url) ? '&' : '?')+ $.param(param);
	}
    return url;
};
/**
 * 数据字典工具类
 */
var DictUtil = DictUtil || {};

DictUtil.getNameByValue = function (values, value) {
    var result = '';
    $.each(values, function (i, o) {
        if (o.value == value) {
            result = o.text;
            return false;
        }
    });
    return result;
};

/**
 * Simple Map
 *
 *
 * var m = new Public.Map();
 * m.put('key','value');
 * ...
 * var s = "";
 * m.each(function(key,value,index){
 *      s += index+":"+ key+"="+value+"\n";
 * });
 * alert(s);
 *
 */
var PublicMap=function(){
    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();

    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */
    this.put = function (key, value) {
        if (this.data[key] == null) {
            this.keys.push(key);
        }
        this.data[key] = value;
    };

    /**
     * 获取某键对应的值
     * @param {String} key
     * @return {Object} value
     */
    this.get = function (key) {
        return this.data[key];
    };

    /**
     * 删除某键对应的值
     * @param {String} key
     */
    this.remove = function (key) {
        var index = $.inArray(key, this.keys);
        if (index != -1) {
            this.keys.splice.call(this.keys, index, 1);
            delete this.data[key];
        }
    };
    /**
     * 清除数据
     */
    this.clear = function () {
        this.keys = null;
        this.data = null;
        this.keys = new Array();
        this.data = new Object();
    };
    /**
     * 遍历Map,执行处理函数
     *
     * @param {Function} 回调函数 function(key,value,index){..}
     */
    this.each = function (fn) {
        if (typeof fn != 'function') {
            return;
        }
        var len = this.keys.length;
        for (var i = 0; i < len; i++) {
            var k = this.keys[i];
            fn(k, this.data[k], i);
        }
    };

    /**
     * 获取键值数组(类似Java的entrySet())
     * @return 键值对象{key,value}的数组
     */
    this.entrys = function () {
        var len = this.keys.length;
        var entrys = new Array(len);
        for (var i = 0; i < len; i++) {
            entrys[i] = {
                key: this.keys[i],
                value: this.data[i]
            };
        }
        return entrys;
    };

    this.values = function () {
        var values = [];
        var len = this.keys.length;
        for (var i = 0; i < len; i++) {
            var k = this.keys[i];
            values.push(this.data[k]);
        }
        return values;
    };

    /**
     * 判断Map是否为空
     */
    this.isEmpty = function () {
        return this.keys.length == 0;
    };

    /**
     * 获取键值对数量
     */
    this.size = function () {
        return this.keys.length;
    };

    /**
     * 重写toString
     */
    this.toString = function () {
        var s = "{",l=this.keys.length,k=null;
        for (var i = 0; i < l; i++) {
            k = this.keys[i];
            s +="'"+ k + "':'" + this.data[k] + "'";
            if(i < l-1){
            	 s += ',';
            }
        }
        s += "}";
        return s;
    };
};
