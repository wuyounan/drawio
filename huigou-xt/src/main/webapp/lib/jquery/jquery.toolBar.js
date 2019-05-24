/********************************
title:      工具栏
Author:     xx
Date :      2014-03-06
*********************************/
(function($) {

	$.fn.toolBar = function (op){
		var obj=this.data('ui_tool_bar');
		if(!obj){
			new toolBar(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['setCheckAccess','addItem','prependItem','removeItem','enable','disable','changeEvent'],function(i,m){
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

	function toolBar(el,op){
		this.options={};
		this.element=$(el);
		this.events={};
		this.items={};
		this.set(op);
		this.init();
		this.element.data('ui_tool_bar',this);
	}
	$.extend(toolBar.prototype,{
		set:function(op){
			if($.isArray(op)){
				op={items:op};
			}
			//根据容器上的属性判断是否需要进行权限判断
			var checkAccess=true;
			if(this.element.attr('checkAccess')=='false'){
				checkAccess=false;
			}
			this.options=$.extend({
				className:null,
				items:[],
				dropup:false,//显示更多按钮时 是否向上打开菜单
				checkAccess:checkAccess,//是否判断权限
				defaultClass:'btn-gray',
				delayTimes:1500,//点击后按钮失效时间
				itemHtml:'<button id="{id}" title="{title}" type="button" class="btn {class}{disabled}" delay="{delay}" relation="{relation}">{icon}{name}</button>'
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			g.element.addClass('btn-toolbar').addClass('ui-toolbar').addClass('dom-overflow');
			if(g.options.className) g.element.addClass(p.className);
			g.btnGroup=$('<div class="btn-group"></div>').appendTo(g.element);
			g.btnGroup.append(g._createItems(p.items));
			var _html=['<div class="btn-group hide">'];
			_html.push('<button type="button" class="btn ',p.defaultClass,' dropdown-toggle" data-toggle="dropdown">');
			_html.push($.i18nProp('common.button.more'),'&nbsp;<b class="caret"></b>');
			_html.push('</button>');
			_html.push('<ul class="dropdown-menu"><div class="btn-group-vertical"></div></ul></div>');
			g.dropdown = $(_html.join('')).appendTo(g.btnGroup);
			//更多按钮向上展示
			if(g.options.dropup){
				g.dropdown.addClass('dropup');
			}
			g._bindEvent();
			$.WinReszier.register($.proxy(g._layout, g));
			//等待按钮加载完成，延迟计算布局
			setTimeout(function(){g._layout();},100);
		},
		_layout:function(){
			if(!this.element.parents('body').length){
				return false;
			}
			var collection = [];
			this.btnGroup.find('div.btn-group').before(this.dropdown.find('div.btn-group-vertical>button'));
			this.btnGroup.find('>button').each(function(){
				if(this.offsetTop > 0) {
					collection.push(this);
				}
			});
			if (collection.length > 0) {
				collection.splice(0,0,$(collection[0]).prev()[0]);
				collection = $(collection);
				this.dropdown.find('ul>div.btn-group-vertical').empty().append(collection);
				this.dropdown.removeClass('hide');
			} else {
				this.dropdown.addClass('hide');
			}
			this.element.removeClass('dom-overflow');
		},
		_createItems:function(items){
			var html=[],ops=this.options,itemId,_self=this;
			var itemName='';
			$.each(items,function(i,item){
				itemId=item.id||'item_'+i;
				if(item.line){
					return true;
				}
				//按钮权限判断
				if(ops.checkAccess&&UICtrl.checkButtonNoAccess(itemId)){
					return true;
				}
				//增加读取国际化文件  add 2017-09-07
				itemName=itemName=$.i18nProp(item.name||'');
				html.push(ops.itemHtml.replace('{id}',itemId)
						.replace('{title}',_self._getItemTitle(item))
		                .replace('{class}',item.className||ops.defaultClass)
		                .replace('{disabled}',item.disabled?' disabled':'')
		                .replace('{delay}',item.delay||'false')
		                .replace('{relation}',item.relation||'')
		                .replace('{icon}','<i class="fa '+item.icon+'"></i>')
		                .replace('{name}',itemName));
				if($.isFunction(item.event)){
					_self.events[itemId]=item.event;
				}else if(typeof item.event=='string'){
					if($.isFunction(window[item.event])){
						_self.events[itemId]=window[item.event];
					}
				}
				_self.items[itemId]=item;
			});
			return html.join('');
		},
		_bindEvent:function(){
			var _self=this;
			this.element.bind('click.toolBar',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('i.fa')){
					$clicked=$clicked.parent();
				}
				if($clicked.hasClass('btn')){
					if($clicked.hasClass('disabled')) return;
					var id=$clicked.attr('id');
					if($.isFunction(_self.events[id])){
						//正常的点击事件需要处理关联按钮
						_self._delayDisable($clicked,true);
						return _self.events[id].call(window,id);
					}
				}
			});
		},
		_getItem:function(itemId){
			return this.element.find('button[id="'+itemId+'"]');
		},
		_getItemTitle:function(item){
			return $.i18nProp(item.title||'');
		},
		//isRelation是否处理关联按钮
		_delayDisable:function(btn,isRelation){
			var _self=this,id=btn.attr('id'),delay=btn.attr('delay');
			var _delayTimes=this.options.delayTimes;
			if(delay==='true'){//增加延时时间
				_delayTimes=_delayTimes*3;
			}
			//点击后禁用按钮， 默认延时启用
			btn.addClass('disabled');
			_self[id+'delayTimes']=setTimeout(function(){
				btn.removeClass('disabled');
			},_delayTimes);
			if(isRelation){
				var relation=btn.attr('relation');
				if(relation){
					$.each(relation.split(','),function(i,_id){
						var relationBtn=_self._getItem(_id);
						if(relationBtn.length > 0){
							//关联按钮触发操作不用再处理关联按钮
							_self._delayDisable(relationBtn,false);
						}
					});
				}
			}
		},
		//isRelation是否处理关联按钮
		_delayEnable:function(btn,isRelation){
			var _self=this,p = this.options,itemId=btn.attr('id');
			if(_self[itemId+'delayTimes']){
				clearTimeout(_self[itemId+'delayTimes']);
				_self[itemId+'delayTimes']=null;
			}
			_self[itemId+'delayTimes']=setTimeout(function(){
				btn.removeClass('disabled');
			},p.delayTimes);
			if(isRelation){
				var relation=btn.attr('relation');
				if(relation){
					$.each(relation.split(','),function(i,_id){
						var relationBtn=_self._getItem(_id);
						if(relationBtn.length > 0){
							_self._delayEnable(relationBtn,false);
						}
					});
				}
			}
		},
		_checkAddItem:function(items){
			if(!items) return false;
			var datas=$.isArray(items)?items:[items];
			//增加判断存在了就不添加了
			var _self=this,itemId=null;
			return $.map(datas,function(item){
				itemId=item.id;
				if(!itemId) return item;
				if($.isFunction(_self.events[itemId])){
					return null;//存在则不添加
				}
				return item;
			});
		},
		addItem:function(items){
			var datas=this._checkAddItem(items);
			if(!datas||!datas.length) return;
			this.btnGroup.append(this._createItems(datas));
			this._layout();
		},
		prependItem:function(items){
			var datas=this._checkAddItem(items);
			if(!datas||!datas.length) return;
			this.btnGroup.prepend(this._createItems(datas));
			this._layout();
		},
		removeItem:function(){
			if(arguments.length==0){
				this.element.find('button').remove();
				delete this.events;
				this.events={};
			}else{
				var _self=this,_item=null;
				$.each(arguments,function(i,itemId){
					_item=_self._getItem(itemId);
					if(_item.length > 0){
						_item.remove();
						delete _self.events[itemId];
					}
				});
			}
			this._layout();
		},
		enable: function() {
			if(arguments.length==0){
				this.element.find('button').removeClass('disabled');
			}else{
				var _self=this,_btn=null,_delay='';
				$.each(arguments,function(i,itemId){
					_btn=_self._getItem(itemId);
					if(!_btn.length||!_btn.hasClass('disabled')){
						return true;
					}
					_delay=_btn.attr('delay');
					if(_delay==='true'){
						_self._delayEnable(_btn,true);
					}else{
						_btn.removeClass('disabled');
					}
				});
			}
		},
		disable: function() {
			if(arguments.length==0){
				this.element.find('button').addClass('disabled');
			}else{
				var _self=this,_btn=null;
				$.each(arguments,function(i,itemId){
					_btn=_self._getItem(itemId);
					if(!_btn.length||_btn.hasClass('disabled')){
						return true;
					}
					_btn.addClass('disabled');
				});
			}
		},
		changeEvent:function(id,fn){
			this.events[id]=fn;
		},
		setCheckAccess:function(checkAccess){
			if(typeof checkAccess!='undefined'){
				this.options.checkAccess=checkAccess;
			}
		}
	});
	
})(jQuery);