/*********************************************************
title:    pup菜单
Author:     xx
Date :      2015-04-16
**********************************************************/
(function($) {
	$(document).bind('click',function(){//点击文档默认关闭菜单
		$('div.main-contextmenu','body').each(function(){$(this).closeContextMenu().hide();});
	});
    var contextmenu=function(el,op){
	    this.handlers={};
		this.index=0;
		this.options=$.extend({
			id:'a',
			width:100,
			eventType:'contextmenu',//默认触发方法
			onSelect:function(){this._hideMenu();},//菜单点击回调方法
			checkEvent:function(e){//默认右键打开菜单,这里判断事件触发的button
				if(e.type=='mousedown'&&e.button!=2){
					return false;
				} 
				return true;
			},
			onOpenMenu:function(){},//菜单打开前执行方法
			stopEvent:true,//阻止默认事件
			overflow:function(menu,ev){//获取的菜单显示位置函数
				 var top=ev.pageY || document.body.scrollTop+ev.x;
				 var left=ev.pageX || document.body.scrollLeft+ev.y;
				 if((left+menu.width())>($.windowWidth()+$.windowScrollLeft()))
					  left=left-menu.width();
				 if((top+menu.height())>($.windowHeight()+$.windowScrollTop()))
					  top=top-menu.height();
				 return {top:top,left:left};
			},
			autoHide:false //鼠标移动出菜单自动隐藏
		},op||{});
		this.id=this.options.id+'_'+new Date().getTime();
		var menu=$('<div class="main-contextmenu"></div>').appendTo('body');
		var html=this.assembled(this.options.items,0,this.options.width);
		if(html===false){
			return;
		}
		menu.html(html.join(''));
		menu.attr('id','menu_'+this.id).addClass('main-contextmenu');
        $(el).addClass("el-contextmenu");
		var _self=this;
		menu.bind('mousemove',function(e){
			var $el = $(e.target || e.srcElement);
			if(!$el.is('li')) return;
			_self._showMenu($el);
		}).bind('mousedown',function(e){
		    var $el = $(e.target || e.srcElement);
		    if($el.is('span.icon')){
		    	$el=$el.parent();
		    }
		    if($el.is('i.fa')){
		    	$el=$el.parent().parent()
		    }
			if(!$el.is('li')) return false;
			_self._doHandler($el,el);
		    return false;
		});
		$(el).bind(this.options.eventType,function(e,obj){
			if(obj){
				e['data']=obj;
			}
			_self._openMenu(e);
			try{
				if(_self.options.stopEvent){
					e.stopPropagation(); 
					e.preventDefault();
					return false;
				}
			}catch(e){}
		});
		if(this.options.autoHide) menu.bind('mouseleave',function(){_self._hideMenu();});
		$.data(el, "el-contextmenu", this);
	};
	jQuery.extend(contextmenu.prototype,{//组合菜单项
		assembled:function(_items,layer,width){
			var items=_items;
			if($.isFunction(_items)){
				items=_items.call(this);
			}
			if(!items||items.length==0){
				return false;
			}
		    layer=layer||0,width=width||160;
		    width=parseInt(width,10)+'px';
		    var buttonName='';
		    var html=['<ul layer="'+layer+'" class="ui-contextmenu" style="width:'+width+';">'],_obj,id,classes;
		    for (var pi = 0;pi<items.length; pi++){
			   _obj=items[pi],id='li_'+this.id+'_'+this.index;
			   this.index++;
			   if(_obj['handler']){
					this.handlers[id]=_obj['handler'];
			   }
			   classes=[];
			   if(_obj.classes){
				   classes.push(_obj.classes);
			   }
			   html.push('<li id="'+id+'" class="'+classes.join(' ')+'">');
			   html.push('<span class="icon">');
			   if(_obj['icon']){
				   html.push('<i class="fa ',_obj['icon'],'"></i>');
			   }
			   html.push('</span>');
			   if(_obj['items']){
			       html.push('<span class="submenu"></span>');
			       html.push.apply(html,arguments.callee.call(this,_obj.items,layer+1,_obj.width));
			   }
			   //增加读取国际化文件 xx add 2017-09-07
			   buttonName=$.i18nProp(_obj.name||'');
			   html.push(buttonName,'</li>');
			}
			html.push('</ul>');
			return html;
		},
		_openMenu:function(e){
			var menu=$('#menu_'+this.id),offset=false;
			//判断是否打开菜单
			if(typeof this.options.checkEvent=='function'){
				if(this.options.checkEvent.call(this,e)===false)
					return false;//不能打开菜单
			}
			try{
				offset=this.options.overflow.call(this,menu,e);
			}catch(e){
				alert('无法打开菜单,overflow执行错误:'+e.message);
				return false;
			}
			menu.closeContextMenu();
			if(typeof this.options.onOpenMenu=='function')
			   this.options.onOpenMenu.call(this,menu,e);
		    menu.css(offset).show();
		    menu.find('ul[layer="0"]').show();
		},
		_showMenu:function($li){
			var $ul = $li.find('>ul'),parent=$li.parent('ul');
			parent.closeContextMenu();
			if($li.hasClass('disabled')) return;
			if(!!$ul.length){
				if(!$ul.is(':visible')){
				   this.adjustOffset($ul,$li,parent.width());
				}
			}
		},
		adjustOffset:function($ul,$li,w){//此函数用来判断 要显示的层是否超出屏幕大小 如果超过了要修改显示位置
		   //得到要显示的对象大小
		   var x=$ul.width(),y=$ul.height();
		   var offset=$li.offset(),of=$li.position();
           if((offset.left+w+x)>($.windowWidth()+$.windowScrollLeft())){//左边超出了宽度,向右显示
		      of.left=of.left-x+5;
		   }else{
		      of.left=of.left+w-5;
		   }
		   if((offset.top+y)>($.windowHeight()+$.windowScrollTop())){//向下超出了高度,向上显示
		      of.top=of.top+5-y;
		   }else{
		      of.top=of.top-5;
		   }
		   $ul.css(of).show();
		},
		_doHandler:function($el,el){
		   if($el.hasClass('disabled')) return;
		   var id=$el.attr('id');
		   if(this.handlers[id]){
				try{
				   if(typeof this.handlers[id]=='function')
				       this.handlers[id].call(this,el);
				}catch(e){
				   alert('回调方法执行错误:'+e.message);
				}
		   }
		   if(typeof this.options.onSelect=='function')
			   this.options.onSelect.call(this,$el,el);
		},
		_hideMenu:function(){
			$('#menu_'+this.id).closeContextMenu().hide();
		},
		_reLoadMenu:function(items,width){
			this._hideMenu();
			this.options.items=items;
			this.options.width=width;
			var html=this.assembled(this.options.items,0,this.options.width);
			$('#menu_'+this.id).html(html.join(''));
		}
	});
	$.fn.closeContextMenu=function(){//关闭子层(在IE下直接使用hide()方法出现无效问题,故创建该方法)
		 return this.each(function(){
			 var layer=$(this).attr('layer');
			 layer=layer?layer:0;layer=parseInt(layer,10);
			 $(this).find('ul[layer="'+(layer+1)+'"]').each(function(){
				 $(this).closeContextMenu();//第归调用关闭所有子层
				 $(this).hide();
			});
		});
	};
	$.fn.contextMenu = function(o) {
		return this.each(function() {
			if(!$(this).is(".el-contextmenu")) {
				new contextmenu(this, o);
			}
		});
	};
	$.fn.closeMenu = function(o) {
		return this.each(function() {
			if($(this).is(".el-contextmenu")){
				var obj=$.data(this, "el-contextmenu");
				if(obj) obj._hideMenu();
			}
		});
	};
	$.fn.reLoadMenu = function(items,width) {
		return this.each(function() {
			if($(this).is(".el-contextmenu")){
				var obj=$.data(this, "el-contextmenu");
				if(obj) obj._reLoadMenu(items,width);
			}
		});
	};
	
	$.fn.mouseenterMenu = function(op) {
		return this.each(function() {
			var $this=$(this);
			var _hideTimer=null,_showTimer=null,_canHide=true;
	    	var _clearTimer=function(){
				if (_showTimer) {
					clearTimeout(_showTimer);
					this.showTimer = null;
				}
				if (_hideTimer) {
					clearTimeout(_hideTimer);
					this.hideTimer = null;
				}
			};
	    	var _hide=function(){
	    		if(!_canHide) return;
	    		_clearTimer();
	    		_hideTimer = setTimeout(function() {
	    			$this.closeMenu();
				},300);
	    	};
	    	var _show=function(){
	    		_clearTimer();
	    		_showTimer=setTimeout(function(){
	    			$this.triggerHandler('showMenu.mouseenterMenu',{obj:$this});
				},200);
	    	};
	    	
	    	$this.on("mouseenter",function(e){
	    		_show();
			}).on("mouseleave",function(e){
	    		_hide();
			});
	    	var _onOpenMenu=op['onOpenMenu'];
	    	$this.contextMenu($.extend(op,{
	    		eventType:'showMenu.mouseenterMenu',
	    		checkEvent:function(){
					return true;
				},
				autoHide:false,
				onOpenMenu:function(m,e){
					if(!m.hasClass('mouseleaveClass')){
						m.addClass('mouseleaveClass').bind('mouseleave',function(){
							_canHide=true;
							_hide();
						}).bind('mouseenter',function(){
							_canHide=false;
							_clearTimer();
						});
					}
					if($.isFunction(_onOpenMenu)){
						_onOpenMenu.call(this,m,e);
					}
				}
	    	}));
		});
	};
	
})(jQuery);