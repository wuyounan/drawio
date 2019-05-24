/********************************
title:      窗体底部按钮对象
Author:     xx
Date :      2017-06-16
*********************************/
(function($) {
	
	$.getFormButton=function(op){
		if($.isArray(op)){
			var buttons=[];
			$.each(op,function(i,o){
				//按钮权限判断
				if(!UICtrl.checkButtonNoAccess(o.id)){
					buttons.push(o);
				}
			});
			op=buttons;
		}
		if($.isArray(op)){
			if(op.length==0){
				return;
			}
		}
		var _package=$('>div.main-package-over','body');
		if(!_package.length){
			_package=$('body');
		}
		var parent=$('>div.container-fluid',_package);
		if(!parent.length){
			parent=$('>div.mainPanel',_package);
		}
		if(!parent.length){
			parent=_package;
		}
		var div=$('<div class="ui-form-bar ui-form-bar-ground"></div>').appendTo(parent);
		div.formButton(op);
		return div;
	};
	
	$.fn.formButton = function (op){
		var obj=this.data('ui_from_button');
		if(!obj){
			new formButton(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['addButton','removeButton','enable','disable'],function(i,m){
					if(op==m){
						args = Array.prototype.slice.call(args, 1);
						value=obj[m].apply(obj,args);
						return false;
					}
				});
				return value;
			}
		}
		return this;
    };

	function formButton(el,op){
		this.options={};
		this.element=$(el);
		this.events={};
		this.set(op);
		this.init();
		this.element.data('ui_from_button',this);
	}
	
	
	$.extend(formButton.prototype,{
		set:function(op){
			if($.isArray(op)){
				op={buttons:op};
			}
			this.options=$.extend({
				className:null,
				holdBar:true,
				defaultClass:'btn-primary',
				buttons:[],
				buttonTagName:'button',
				delayTimes:1500,//点击后按钮失效时间
				buttonHtml:'<button id="{id}" type="button" class="btn {class}{disable}">{icon}{name}</button>'
			},this.options, op||{});
		},
		init:function(){
			this.element.addClass('ui-form-bar');
			if(this.options.holdBar){
				$('<div class="ui-hold-bar"></div>').insertAfter(this.element);
			}
			if(this.options.className) this.element.addClass(this.options.className);
			this.element.append(this.getButtonHtml(this.options.buttons));
			this.addEvent();
		},
		getButtonHtml:function(buttons){
			var html=[],ops=this.options,buttonId,_self=this,buttonName='';
			$.each(buttons,function(i,button){
				buttonId=button.id||'button_'+i;
				//按钮权限判断
				if(UICtrl.checkButtonNoAccess(buttonId)){
					return;
				}
				//增加读取国际化文件 xx add 2017-09-07
				buttonName=$.i18nProp(button.name||'');
				html.push(ops.buttonHtml.replace('{id}',buttonId)
						.replace('{class}',button.className||ops.defaultClass)
		                .replace('{disable}',button.disable?' disable':'')
		                .replace('{icon}','<i class="fa '+button.icon+'"></i>&nbsp;')
		                .replace('{name}',buttonName));
				if($.isFunction(button.event)){
					_self.events[buttonId]=button.event;
				}else if(typeof button.event=='string'){
					if($.isFunction(window[button.event])){
						_self.events[buttonId]=window[button.event];
					}
				}
			});
			return html.join('');
		},
		addEvent:function(){
			var _self=this;
			this.element.bind('click.fromButton',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('i.fa')){
					$clicked=$clicked.parent();
				}
				if($clicked.hasClass('btn')){
					if($clicked.hasClass('disable')) return;
					var id=$clicked.attr('id');
					if($.isFunction(_self.events[id])){
						_self._delayDisable($clicked);
						return _self.events[id].call(window);
					}
				} 
			});
		},
		addButton:function(buttons){
			if(!buttons) return;
			buttons=$.isArray(buttons)?buttons:[buttons];
			this.element.prepend(this.getButtonHtml(buttons));
		},
		removeButton:function(){
			if(arguments.length==0){
				this.element.find('button.btn').remove();
				delete this.events;
				this.events={};
			}else{
				var _self=this,buttonTagName=_self.options.buttonTagName;
				$.each(arguments,function(i,id){
					var _button=_self.element.find(buttonTagName+'[id="'+id+'"]');
					if(_button.length > 0){
						_button.parent().remove();
						delete _self.events[id];
					}
				});
			}
		},
		_delayDisable:function(btn,isRelation){
			var _self=this,id=btn.attr('id');
			var _delayTimes=this.options.delayTimes;
			//点击后禁用按钮， 默认延时启用
			btn.addClass('disabled');
			_self[id+'delayTimes']=setTimeout(function(){
				btn.removeClass('disabled');
			},_delayTimes);
		},
		_delayEnable:function(btn,isRelation){
			var _self=this,p = this.options,itemId=btn.attr('id');
			if(_self[itemId+'delayTimes']){
				clearTimeout(_self[itemId+'delayTimes']);
				_self[itemId+'delayTimes']=null;
			}
			_self[itemId+'delayTimes']=setTimeout(function(){
				btn.removeClass('disabled');
			},p.delayTimes);
		},
		enable: function() {
			var _self=this,buttonTagName=_self.options.buttonTagName;
			if(arguments.length==0){
				this.element.find(buttonTagName+'.ui-button-disabled').removeClass('ui-button-disabled').removeAttr('disabled');
			}else{
				$.each(arguments,function(i,id){
					_self.element.find(buttonTagName+'[id="'+id+'"]').removeClass('ui-button-disabled').removeAttr('disabled');
				});
			}
		},
		disable: function() {
			var _self=this,buttonTagName=_self.options.buttonTagName;
			if(arguments.length==0){
				this.element.find(buttonTagName+'.ui-button-inner').addClass('ui-button-disabled').attr('disabled',true);
			}else{
				$.each(arguments,function(i,id){
					_self.element.find(buttonTagName+'[id="'+id+'"]').addClass('ui-button-disabled').attr('disabled',true);
				});
			}
		}
	});
	
})(jQuery);