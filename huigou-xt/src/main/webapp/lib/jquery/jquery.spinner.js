/*---------------------------------------------------------------------------*\
|  title:         上下点击框                                                                                                                                                                |
|  Author:        xx                                                          |
\*---------------------------------------------------------------------------*/
(function($) {
	$.fn.spinner = function(op){
		return this.each(function() {
			var obj=$.data(this,'spinner');
			if(!obj){
				new spinner($(this),op);
			}else{
				if (typeof op == "string") {
					$.each(['enable','disable'],function(i,m){
						if(op==m){
							args = Array.prototype.slice.call(args, 1);
							value=obj[m].apply(obj,args);
							return false;
						}
					});
				}else{
					obj.set(op);
				}
			}
		}); 
	};
	function spinner($el,op){
		this.options={};
		this.set(op);
		this.element=$el;
		this.disabled = false;
		this.timer = null;
		this.init();
		$el.data('spinner',this);
	}
	$.extend(spinner.prototype, {
		set:function(op){
			this.options=$.extend({
				step    : 1,//默认步长
				min     : null,//最小值
				max     : null,//最大值
				countWidth: false,
				onUp    : function($el){
					var _step=this.options.step;
					var value=this.checkNumber($el.val());
					value=isNaN(value)?_step:value+_step;
					value=this.options.doCheck.call(this,value);
					//$el.val(value).focus();
					$el.val(value);
					$el.trigger('change');
				},
				onDown  : function($el){
					var _step=this.options.step;
					var value=this.checkNumber($el.val());
					value=isNaN(value)?-_step:value-_step;
					value=this.options.doCheck.call(this,value);
					//$el.val(value).focus();
					$el.val(value);
					$el.trigger('change');
				},
				doCheck : function(value){//格式化value
					var max=parseInt(this.options.max,10),min=parseInt(this.options.min,10);
					if(!isNaN(max)&&value > max){
						value=max;
					}
					if(!isNaN(min)&&value < min){
						value=min;
					} 
					if($.isFunction(this.options.doFormat)){
						value=this.options.doFormat.call(this,value);
					}
					return value;
				},
				doFormat:null,
				default_value:0//默认值
			},this.options, op||{});
		},
		init:function(){
			var $el=this.element;
			var html=['<div class="input-group ui-spinner">'];
			html.push('<span class="input-group-btn-vertical">'); 
			html.push('<button class="btn btn-primary ui-spinner-arrow-up" type="button"><i class="fa fa-caret-up"></i></button>');  
			html.push('<button class="btn btn-primary ui-spinner-arrow-down" type="button"><i class="fa fa-caret-down"></i></button>'); 
			html.push('</span></div>');
			var spinner_wrap=$(html.join('')).insertAfter($el);
			$el.prependTo(spinner_wrap);
			var _self=this;
			spinner_wrap.bind('mousedown.spinner',function(e){
				var $e = $(e.target || e.srcElement);
				$e =$e.is('i')?$e.parent():$e;
				if($e.hasClass('ui-spinner-arrow-up')){
					 _self.up($el);
				 }else if($e.hasClass('ui-spinner-arrow-down')){
					 _self.down($el);
				 }
			}).bind('mouseup.spinner',function(e){
				_self.stop();
			});
			if($el.is(':disabled')||$el.is('[readonly]')){
				this.disable($el);
			}
		},
		stop:function(){
			clearTimeout(this.timer);
			this.timer = null;
		},
		up:function($el){
			this.onClick($el,true);
			this.timer=setTimeout(function(obj){
				return function(){
					obj.up($el);
				};
			}(this),150);
		},
		down:function($el){
			this.onClick($el,false);
			this.timer=setTimeout(function(obj){
				return function(){
					obj.down($el);
				};
			}(this),150);
		},
		onClick:function($el,flag){
			if(this.disabled) return;
			if ($el.val()=='') {
				var defaultValue=this.options.default_value;
				if(typeof this.options.default_value=='function')
					defaultValue=defaultValue.call(window);
				$el.val(defaultValue);
			}
			this.options[flag?'onUp':'onDown'].call(this,$el);
		},
		enable: function() {
			this.disabled = false;
			this.element.removeAttr('readonly').next('span').show();
			if(this.element.parent().hasClass('input-group')){
				this.element.parent().removeClass('ui-input-group-inline');
			}
		},
		disable: function() {
			this.disabled = true;
			this.element.attr('readonly',true).next('span').hide();
			//input-group 存在样式 display:table(span 隐藏后任然占据位置)  需要增加 样式 display:inline
			if(this.element.parent().hasClass('input-group')){
				this.element.parent().addClass('ui-input-group-inline');
			}
		},
		checkNumber:function(value){
			if (Public.isBlank(value)) {
				return 0;
			}
			value=value.toString();
			value=value.replace(/[,]/g, '');
			value=value.replace(/[_]/g, '');
			return parseFloat(value,10);
		}
	});
})(jQuery);