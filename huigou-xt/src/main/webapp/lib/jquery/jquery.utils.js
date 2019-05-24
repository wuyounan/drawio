/*******************************************************************************
 * title: 工具类插件 Author: xx
 ******************************************************************************/
(function($) {

	// 删除DOM方法,避免内存泄露
	$.fn.removeAllNode = function() {
		var item = $(this);
		var clearItem = $('#clear-use-memory');
		if (clearItem.length == 0) {
			jQuery('<div/>').hide().attr('id', 'clear-use-memory').appendTo(
					'body');
			clearItem = jQuery('#clear-use-memory');
		}
		item.appendTo(clearItem);
		jQuery('*', clearItem).each(
				function(i, e) {
					(events = jQuery.data(this, 'events'))
							&& jQuery.each(events, function(i, e1) {
								jQuery(e).unbind(i + '.*');
							});
					jQuery.event.remove(this);
					jQuery.removeData(this);
				});
		clearItem[0].innerHTML = '';
		item = null;
	};

	// 在当前对象光标处插入指定的内容
	$.fn.insertAtCaret = function(textFeildValue) {
		var textObj = $(this).get(0);
		if (document.all && textObj.createTextRange && textObj.caretPos) {
			var caretPos = textObj.caretPos;
			caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == '' ? textFeildValue
					+ ''
					: textFeildValue;
		} else if (textObj.setSelectionRange) {
			var rangeStart = textObj.selectionStart;
			var rangeEnd = textObj.selectionEnd;
			var tempStr1 = textObj.value.substring(0, rangeStart);
			var tempStr2 = textObj.value.substring(rangeEnd);
			textObj.value = tempStr1 + textFeildValue + tempStr2;
			textObj.focus();
			var len = textFeildValue.length;
			textObj.setSelectionRange(rangeStart + len, rangeStart + len);
			textObj.blur();
		} else {
			textObj.value += textFeildValue;
		}
		textObj.focus();
	};

	// cookie
	$.cookie = function(name, value, options) {
		if (typeof value != 'undefined') { // name and value given, set cookie
			options = options || {};
			if (value === null) {
				value = '';
				options.expires = -1;
			}
			var expires = '';
			if (options.expires
					&& (typeof options.expires == 'number' || options.expires.toUTCString)) {
				var date;
				if (typeof options.expires == 'number') {
					date = new Date();
					date.setTime(date.getTime()
							+ (options.expires * 24 * 60 * 60 * 1000));
				} else {
					date = options.expires;
				}
				expires = '; expires=' + date.toUTCString(); // use expires
				// attribute,
				// max-age is
				// not supported
				// by IE
			}
			var path = options.path ? '; path=' + options.path : '';
			var domain = options.domain ? '; domain=' + options.domain : '';
			var secure = options.secure ? '; secure' : '';
			document.cookie = [ name, '=', encodeURIComponent(value), expires,
					path, domain, secure ].join('');
		} else { // only name given, get cookie
			var cookieValue = null;
			if (document.cookie && document.cookie != '') {
				var cookies = document.cookie.split(';');
				for (var i = 0; i < cookies.length; i++) {
					var cookie = jQuery.trim(cookies[i]);
					// Does this cookie string begin with the name we want?
					if (cookie.substring(0, name.length + 1) == (name + '=')) {
						cookieValue = decodeURIComponent(cookie
								.substring(name.length + 1));
						break;
					}
				}
			}
			return cookieValue;
		}
	};

	// 设置Cookie 用户服务器csrf验证
	$.CSRFToken = function() {
		// 设置有效时间为1秒足够发起请求
		//var expires = new Date((new Date()).getTime() + 1000);
		// 获取csrfToken作为允许参数,跨域无法获取该参数
		var token = $('#csrfTokenElement').val();
		/*$.cookie('csrfToken', token, {
			expires : expires
		});
		// 延时删除Cookie
		setTimeout(function() {
			var expires = new Date((new Date()).getTime() - 2000);
			$.cookie('csrfToken', '', {
				expires : expires
			});
		}, 100);*/
		return token;
	};
	
	//url或param中获取bizId 与token md5加密
	$.getCSRFTokenAsUrlData = function(url,param) {
		param=param||{};
		var bizId=param['bizId'];
		if (bizId && bizId != null && bizId !="") {
			return $.getCSRFTokenAsBizData(bizId);
		}
		if(url.indexOf("?")!=-1){
			var str = url.substr(url.indexOf("?") + 1);
			$.each(str.split("&"), function(i, p) {
				var ps = p.split("=");
				if (ps.length > 1) {
					if (ps[0] == 'bizId') {
						bizId = ps[1];
						return false;
					}
				}
			});
		}
		return $.getCSRFTokenAsBizData(bizId);
	};

	//业务数据与token md5加密传输
	$.getCSRFTokenAsBizData = function(bizData) {
		var token = $.CSRFToken();
		if (bizData && bizData != null && bizData !="") {
			// 存在bizData校验则使用bizData与token加密后传输
			token = $.md5(bizData + token);
		}
		return token;
	};
	
	// 组合带有csrf token 的url;
	$.getCSRFUrl = function(action, param) {
		param = param || {};
		var urls = [];
		if (action.indexOf(web_app.name + '/') == -1) {
			urls.push(web_app.name, '/', action);
		} else {
			urls.push(action);
		}
		// 存在bizId校验则使用bizId与token加密后传输
		var url = urls.join(''),token = $.getCSRFTokenAsUrlData(action,param);
		param['csrfToken'] =token;
		url += (/\?/.test(url) ? '&' : '?') + $.param(param);
		return url;
	};

	// 所有AJAX请求全部添加csrfToken
	$.ajaxSetup({
		beforeSend : function(xhr) {
			// 发送ajax请求之前向http的head里面加入验证信息
			var token = $.CSRFToken(),_taskId=window['taskId']||'';
			xhr.setRequestHeader("csrfToken", token);
			// 如果存在任务ID加入到head里面后台验证任务状态
			xhr.setRequestHeader("procTaskId", _taskId);
		}
	});

	// 页面绑定$(window).bind('resize', resize);通过该方法只注册一次事件
	$.WinReszier = (function() {
		var registered = [];
		var inited = false, timer = null;
		var resize = function(ev) {
			clearTimeout(timer);
			timer = setTimeout(notify, 100);
		};
		var notify = function() {
			var indexs = [];
			$.each(registered, function(i, fn) {
				var flag = fn.apply();
				// 返回false 可以阻止$.WinReszier.register 注册的事件继续生效
				if (flag === false) {
					indexs.push(i);
				}
			});
			$.each(indexs, function(i, index) {
				delete registered[index];
				registered.splice(index, 1);
			});
		};
		return {
			register : function(fn) {
				registered.push(fn);
				if (inited === false) {
					$(window).bind('resize', resize);
					inited = true;
				}
			},
			unregister : function(fn) {
				for (var i = 0, cnt = registered.length; i < cnt; i++) {
					if (registered[i] == fn) {
						delete registered[i];
						registered.splice(i, 1);
						break;
					}
				}
			}
		}
	}());

	$.fn.placeholder = function(placeholderValue) {
	    return $(this).each(function() {
	        var _self = $(this),
	        supportPlaceholder = 'placeholder' in document.createElement('input');
	        if (placeholderValue) {
	            _self.attr('placeholder', placeholderValue);
	        }
	        if (!supportPlaceholder) {
	            var defaultValue = _self.attr('placeholder');
	            var $imitate = $('<span class="ui-wrap-placeholder"></span>').text(defaultValue);
	            $imitate.css({
	                'height': _self.outerHeight() + 'px',
	                'margin-left': _self.css('margin-left'),
	                'margin-top': _self.css('margin-top'),
	                'font-size': _self.css('font-size'),
	                'font-family': _self.css('font-family'),
	                'font-weight': _self.css('font-weight'),
	                'padding-left': parseInt(_self.css('padding-left')) + 2 + 'px',
	                'line-height': _self.is('textarea') ? _self.css('line-weight') : _self.outerHeight() + 'px',
	                'padding-top': _self.is('textarea') ? parseInt(_self.css('padding-top')) + 2 : 0
	            });
	            _self.before($imitate.click(function() {
	                _self.trigger('focus');
	            }));
	            _self.val().length != 0 && $imitate.hide();
	            _self.focus(function() {
	                $imitate.hide();
	            }).blur(function() {
	                if (/^$/.test(_self.val())) {
	                    $imitate.show();
	                } else {
	                    $imitate.hide();
	                }
	            });
	        }
	    });
	};

	$.fn.subjectCode = function(options) {
	    var p = $.extend({
	        digit: 4,
	        split: '.',
	        mask: '9999',
	        direction: 'auto',
	        parentCode: '',
	        code: ''
	    },
	    options || {});
	    return $(this).each(function() {
	        var _parentValue = p['parentCode'] || '',
	        _value = p['code'] || '',
	        _split = p.split;
	        var _self = $(this),
	        _thisValue = _self.hide().val();
	        if (Public.isNotBlank(_thisValue)) {
	            _value = _thisValue;
	            if (Public.isBlank(_parentValue)&&Public.isNotBlank(_split)) {
		            if (_thisValue.lastIndexOf(_split) > -1) {
		                _parentValue = _thisValue.substring(0, _thisValue.lastIndexOf(_split));
		                _value = _thisValue.substring(_thisValue.lastIndexOf(_split) + 1, _thisValue.length);
		            }
	            }else{
	            	if (Public.isNotBlank(_parentValue)) {
	            		_value=_thisValue.substring(_parentValue.length, _thisValue.length);
	            	}
	            }
	        }
	        if (Public.isNotBlank(_parentValue)) {
	            _parentValue += _split;
	            if (p.direction == 'auto') {
	                _value = _value.beforeSupplement(p.digit); // 前补0
	            }
	        } else {
	            if (p.direction == 'auto') {
	                _value = _value.afterSupplement(p.digit); // 后补0;
	            }
	        }
	        if (p.direction == 'before') {
	            _value = _value.beforeSupplement(p.digit); // 前补0
	        } else if (p.direction == 'after') {
	            _value = _value.afterSupplement(p.digit); // 后补0;
	        }
	        var _viewValue = _parentValue;
	        if (_viewValue.length > 32) {
	            _viewValue = _viewValue.substring(0, 32);
	        }
	        var html = ['<div>'];
	        html.push('<input type="hidden" name="parent-code" value="', _parentValue, '">');
	        html.push('<div class="input-group">');
	        html.push('<span class="input-group-addon" style="padding-right:0px;" title="', _parentValue, '">', _viewValue, '</span>');
	        html.push('<input type="text" name="subject-code" style="padding-left:1px;" class="subject-code no-check-default-value" ', _self.isRequired() ? 'required=true': '', ' value="', _value, '">');
	        html.push('</div>');
	        html.push('</div>');
	        var _div = $(html.join('')).insertAfter(_self);
	        var _s = $('input[name="parent-code"]', _div),
	        _c = $('input[name="subject-code"]', _div);
	        if (_self.is("[readonly]")) {
	            _c.attr('readonly', true).css({
	                backgroundColor: '#fff'
	            });
	            return;
	        }
	        _c.addClass('ui-maskedinput-nocheck').mask(p.mask, {
	            number: /^[0-9|n|.]+$/i.test(p.mask),
	            completed: function(input) {
	                var code = input.val();
	                if (p.direction == 'before' || p.direction == 'auto') {
	                    code = code.beforeSupplement(p.digit);
	                } else if (p.direction == 'after') {
	                    code = code.afterSupplement(p.digit);
	                }
	                input.val(code);
	                _self.val(_s.val() + code);
	            }
	        });
	        window.setTimeout(function() {
	            _c.blur();
	        },0);
	    });
	};

	if (!$.browser) {
		var userAgent = navigator.userAgent.toLowerCase();
		$.browser = {
			version : (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [
					0, '0' ])[1],
			safari : /webkit/.test(userAgent),
			opera : /opera/.test(userAgent),
			msie : /msie/.test(userAgent) && !/opera/.test(userAgent),
			mozilla : /mozilla/.test(userAgent)
					&& !/(compatible|webkit)/.test(userAgent),
			chrom : /chrom(e|ium)/.test(userAgent)
		};
	}

	// 判断访问端是电脑还是手机 return true 手机
	$.isMobile = function() {
		if (/AppleWebKit.*Mobile/i.test(navigator.userAgent)
				|| (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/
						.test(navigator.userAgent))) {
			try {
				if (/Android|Windows Phone|webOS|iPhone|iPod|BlackBerry/i
						.test(navigator.userAgent)) {
					return true;
				} else if (/iPad/i.test(navigator.userAgent)) {
					return true;
				}
			} catch (e) {
			}
		}
		if (/Linux/i.test(navigator.platform)) {
			// 屏幕宽度太小也认为是手机端
			if ($(window).width() < 800) {
				return true;
			}
		}
		return false;
	};

	/**
	 * 因为文本输入框（input）放置在靠近页面的中下方，点击文本框以后，则输入框会被弹出的手机输入法键盘遮盖住。
	 * 使用下面的方法点击时强制滚动之，解决了在不同浏览器下浏览的同样问题。
	 */
	if ($.isMobile()) {
		$.WinReszier.register(function() {
			if (document.activeElement.tagName == "INPUT"
					|| document.activeElement.tagName == "TEXTAREA") {
				window.setTimeout(function() {
					document.activeElement.scrollIntoViewIfNeeded();
				}, 0);
			}
		});
	}

	$.isIosPhone = $.isMobile() && /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent);

	// 关闭页面中使用的各种选择器
	$.closePicker = function() {
		if ($.fn.combox) {
			try {
				$.closeCombox();
			} catch (e) {
			}
		}
		if ($.fn.datepicker) {
			try {
				$.datepicker.close();
			} catch (e) {
			}
		}
		if ($.fn.monthpicker) {
			try {
				$.closeMonthpicker();
			} catch (e) {
			}
		}
	};

	// 获取页面的宽度 iPhone 中需要单独处理 获取父页面包含dom的宽度
	$.windowWidth = function() {
		var windowWidth;
		if ($.isIosPhone && parent != window) {
			if (parent.contentWidth) {
				windowWidth = parent.contentWidth();
			} else {
				windowWidth = $(window).width();
			}
		} else {
			windowWidth = $(window).width();
		}
		return windowWidth;
	};

	// 获取页面的高度 iPhone中需要单独处理 获取父页面包含dom的高度
	$.windowHeight = function() {
		var windowHeight;
		if ($.isIosPhone && parent != window) {
			if (parent.contentHeight) {
				windowHeight = parent.contentHeight();
			} else {
				windowHeight = $(window).height();
			}
		} else {
			windowHeight = $(window).height();
		}
		return windowHeight;
	};

	// 获取页面滚动高度 iPhone中需要单独处理
	$.windowScrollTop = function() {
		var scrollTop;
		if ($.isIosPhone && parent != window) {
			if (parent.contentScrollTop) {
				scrollTop = parent.contentScrollTop();
				if (scrollTop == 0) {
					scrollTop = $(window).scrollTop();
				}
			} else {
				scrollTop = $(window).scrollTop();
			}
		} else {
			scrollTop = $(window).scrollTop();
		}
		return scrollTop;
	};

	// 获取页面滚动宽度 iPhone中需要单独处理
	$.windowScrollLeft = function() {
		var scrollLeft;
		if ($.isIosPhone && parent != window) {
			if (parent.contentScrollLeft) {
				scrollLeft = parent.contentScrollLeft();
				if (scrollLeft == 0) {
					scrollLeft = $(window).scrollLeft();
				}
			} else {
				scrollLeft = $(window).scrollLeft();
			}
		} else {
			scrollLeft = $(window).scrollLeft();
		}
		return scrollLeft;
	};

})(jQuery);