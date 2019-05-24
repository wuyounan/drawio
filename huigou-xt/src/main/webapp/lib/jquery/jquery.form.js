/********************************
title:      form表单数据读取处理
Author:     xx
Date :      2017-06-16
*********************************/
(function($) {
	
	 /* 返回控件类型 */
    var getType = function (el) {
        var t = el.type;
        switch (t) {
            case "select":
            case "select-one":
            case "select-multiple":
                t = "select";
                break;
            case "text":
            case "hidden":
            case "textarea":
            case "password":
            case "button":
            case "submit":
                t = "text";
                break;
            case "checkbox":
            case "radio":
                t = t;
                break;
        }
        return t;
    };
    
    /* 返回select控件值 */
    var getOptionVal = function (el) {
        return jQuery.browser.msie && !(el.attributes['value'].specified) ? el.text : el.value;
    };
    
    /* 判断输入值是否存在 */
    var valueExists = function (a, v) {
        return a ? ($.inArray(v, a) > -1) : false;
    };
    
    /* 获取必输标志 */
    $.fn.isRequired = function () {
    	if($(this).hasClass('ui-combox-required')){
    		return true;
    	}
        var required;
        if (document.querySelector) {
            required = $(this).attr("required");
            if (required === undefined || required === false) {
                return undefined;
            }
            return true;
        } else {
            try {
                var outer = $(this).get(0).outerHTML, part = outer.slice(0, outer.search(/\/?['"]?>(?![^<]*<['"])/));
                return /\srequired\b/i.test(part) ? true : undefined;
            } catch (e) {
                /*alert($(this)[0]);*/
                return undefined;
            }
        }
    };
    
    /* 输入框默认验证方法 */
    $.fn.defaultCheckVal = function (match) {
        var required = $(this).isRequired(), label = $(this).attr('label');
        var notCheck = $(this).attr('notCheck');
        if (notCheck == 'true') return true;
        if (!label) label = $(this).attr('title');
        try {
            var fn = $(this).data('combox-before-close');
            if ($.isFunction(fn)) {
                fn.call(window);
            }
        } catch (e) {
        }
        var value = $(this).getValue();
        if (required && value == '') {
            Public.tip('common.warning.notnull',label);
            $(this).onFocus();
            return false;
        }
        match = match || $(this).attr('match');
        /* 控件上有表达式 */
        if (match && value != '') {
            /* 使用该表达式进行验证 */
            var flag = Public.validator(match, value);
            if (!flag) {
                Public.tip(label + Public.validatorErrorMessage(match));
                $(this).onFocus();
                return false;
            }
        }
        return true;
    };
    
    $.fn.onFocus = function () {
    	return this.each(function() {
    		if($(this).is(':visible')){
    			$(this).focus().addClass('error').one("blur",function(){
    				  $(this).removeClass('error');
    			});
    		}else{
    			var obj=$(this).data('ui-combox');
    			if(obj){
    				obj.input.focus().addClass('error').one("blur",function(){
      				  $(this).removeClass('error');
        			});
    			}
    		}
    	});
    };
    
    /* 读取输入控件值 isEncode返回的数据是否执行encodeURI()方法 */
    $.fn.getValue = function (isEncode) {
        var v = [], text, obj = this;
        var getParent = function (selector) {
            return function () {
                var parents = obj.parents(selector);
                if (parents.length > 0) {
                    return parents[0];
                } else {
                    throw new Error("");
                }
            };
        };
        if (this.hasClass("ui-maskedinput")&&!this.hasClass("ui-maskedinput-nocheck")) {//maskedinput 输入校验
            this.checkValue();
        }
        this.each(function () {
            var t = getType(this);
            switch (t) {
                case "radio":
                    var doc = Public.these(getParent('form'), getParent('div.textGrid'), function () {
                        return document;
                    });
                    $("input:radio[name='" + this.name + "']", doc).each(function (i, o) {
                        if (o.checked) {
                            v.push(o.value);
                            return false;
                        }
                    });
                    break;
                case "checkbox":
                    var doc = Public.these(getParent('form'), getParent('div.textGrid'), function () {
                        return document;
                    });
                    var checkboxs=$("input:checkbox[name='" + this.name + "']", doc),values = [];
                    checkboxs.each(function (i, o) {
                        if (o.checked) {
                            values.push(o.value);
                        }
                    });
                    if (values.length > 0) {
                        v.push(values.join(','));
                    } else {
                    	//单个checkbox且value==1未被选中，默认返回值0
                    	if(this.value==1&&checkboxs.length==1){
                    		v.push(0);
                    	}
                    }
                    break;
                case "select":
                    if (this.type == "select-one") {
                        v.push((this.selectedIndex == -1) ? ""
                            : getOptionVal(this[this.selectedIndex]));
                    } else {
                        for (var i = 0; i < this.length; i++) {
                            if (this[i].selected) {
                                v.push(getOptionVal(this[i]));
                            }
                        }
                    }
                    break;
                case "text":
                    var va = this.value;
                    if ($(this).is('[mask="money"]') || $(this).is('[mask="positiveMoney"]')) {
                        va = va.replace(/[,]/g, '');
                    }
                    v.push(va);
                    break;
            }
        });
        text = $.trim(v.join(','));
        return isEncode ? encodeURI(text) : text;
    };
    
    $.fn.setValue = function (v) {
        var obj = this;
        if (Public.isBlank(v)) {
            v = '';
        }
        var getParent = function (selector) {
            return function () {
                var parents = obj.parents(selector);
                if (parents.length > 0) {
                    return parents[0];
                } else {
                    throw new Error("");
                }
            };
        };
        return this.each(function () {
            var t = getType(this), x;
            switch (t) {
                case "checkbox":
                    v = $.isArray(v) ? v : (v + '').split(',');
                    var doc = Public.these(getParent('form'), getParent('div.textGrid'), function () {
                        return document;
                    });
                    var checkboxs=$("input:checkbox[name='" + this.name + "']", doc);
                    checkboxs.each(function (i, o) {
                    	if (valueExists(v, o.value)) {
                            o.checked = true;
                        } else {
                            o.checked = false;
                        }
                    });
                    break;
                case "radio":
                    var doc = Public.these(getParent('form'), getParent('div'),  function () {
                        return document;
                    });
                    $("input:radio[name='" + this.name + "']", doc).each(function (i, o) {
                        if (o.value == v) {
                            o.checked = true;
                        } else {
                            o.checked = false;
                        }
                    });
                    break;
                case "select":
                    var bSelectOne = (this.type == "select-one");
                    var bKeepLooking = true;
                    for (var i = 0; i < this.length; i++) {
                        x = getOptionVal(this[i]);
                        bSelectItem = valueExists(v, x);
                        if (bSelectItem) {
                            this[i].selected = true;
                            if (bSelectOne) {
                                bKeepLooking = false;
                                break;
                            }
                        } else if (!bSelectOne)
                            this[i].selected = false;
                    }
                    if (bSelectOne && bKeepLooking && !!this[0]) {
                        this[0].selected = true;
                    }
                    break;
                case "text":
                    this.value = v || '';
                    break;
            }
        });
    };
    
    /* 返回json对象 */
    $.fn.formToJSON = function (op) {
    	//获取数据前强制关闭选择框
    	$.closePicker();
        op = $.extend({
            encode: true,// 返回的数据是否执行encodeURI()方法
            check: true,// 是否进行输入验证
            checkFunction: null
            // 数据验证方法
        }, op || {});
        var json = {}, flag = true;
        this.filter("form").each(function () {
            var els = this.elements, el, n, stProcessed = {}, jel = null, value = null;
            for (var i = 0, elsMax = els.length; i < elsMax; i++) {
                el = els[i];
                n = el.name;
                if (!n || stProcessed[n])
                    continue;
                jel = $(el), value = jel.getValue();
                if (jel.is('[noaccess]')) {//字段无访问权限不执行取值
                    stProcessed[n] = true;
                    continue;
                }
                if (op.check) {
                    if (typeof op.checkFunction == 'function') {
                        flag = op.checkFunction.call(window, jel, value);
                    } else {
                        flag = jel.defaultCheckVal();
                    }
                    // 验证不通过
                    if (flag === false)
                        return false;
                    else
                        flag = true;
                }
                if (op.encode) {
                    value = Public.encodeURI(value);
                }
                json[n] = value;
                stProcessed[n] = true;
            }
        });
        return flag ? json : false;
    };
    
    /* from 内输入框赋值 */
    $.fn.formSet = function (inHash) {
        this.filter("form").each(function () {
            var els = this.elements, el, n, stProcessed = {}, jel;
            for (var i = 0, elsMax = els.length; i < elsMax; i++) {
                el = els[i];
                n = el.name;
                if (!n || stProcessed[n])
                    continue;
                if (typeof inHash[n] == 'string' || typeof inHash[n] == 'number')
                    $(el).setValue(inHash[n]);
                stProcessed[n] = true;
            }
        });
    };
    
    /* 重置From */
    $.fn.formClean = function (fields) {
    	var fs=[];
    	if(fields){
    		if($.isArray(fields)){
    			fs=fields;
    		}else{
    			fs=fields.split(',');
    		}
    	}
        this.filter("form").each(function () {
            var els = this.elements, el, n, stProcessed = {}, jel;
            for (var i = 0, elsMax = els.length; i < elsMax; i++) {
                el = els[i];
                n = el.name;
                if (fs != '' && $.inArray(n,fs) > -1)
                    continue;
                if (el.type == 'button')
                    continue;
                if (!n || stProcessed[n])
                    continue;
                $(el).setValue('');
                stProcessed[n] = true;
            }
        });
    };
    
    /*form ajax 提交*/
    $.fn.ajaxSubmit = function (opts) {
        opts = opts || {};
        opts.async = opts.async === 'undefined' ? true : opts.async;
        var isCheck = true;
        var attrCheck = $(this).attr('check');
        if (typeof attrCheck != 'undefined') {
            if (typeof attrCheck == 'string' && attrCheck === 'false') {
                isCheck = false;//不验证输入判断条件
            }
            if (typeof attrCheck == 'boolean' && attrCheck === false) {
                isCheck = false;//不验证输入判断条件
            }
            $(this).removeAttr('check');
        } else {
            isCheck = opts.isCheck || true;
        }
        var formData = $(this).formToJSON({check: isCheck});
        if (!formData) return false;
        var url = opts.url;
        if (opts.param === false) return false;
        var param = opts.param || {};
        var successCallback = opts.success;
        var failCallback = opts.fail;
        param = $.extend(formData, param || {});
        if (opts.async == false) {
            Public.syncAjax(url, param, successCallback, failCallback);
        } else {
            Public.ajax(url, param, successCallback, failCallback);
        }
        return true;
    };
	
})(jQuery);