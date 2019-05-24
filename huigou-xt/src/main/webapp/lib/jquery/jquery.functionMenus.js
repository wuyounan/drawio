/********************************
title:      index页功能菜单
Author:     xx
Date :      2017-06-15
顶部横向菜单 <div class="navbar-top-toolbar"></div>
*********************************/
(function ($) {
	
	$.fn.functionMenus = function (op) {
	    return this.each(function () {
	        new functionMenus(this,op);
	    });
	};
	
	function functionMenus(el,op){
		this.options={};
		this.element=$(el);
		this.set(op);
		this.init();
		this.bindEvent();
	}
	
	$.extend(functionMenus.prototype,{
		set:function(op){
			this.options=$.extend({
				url:web_app.name + "/menu.ajax",
				parentId:'1',
				onClick:null,
				autoClose:true
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			g.loadData(p.parentId,1,function(html,data){
				g.element.append(html);
				g._initTopToolbar(data);
			});
		},
		getNavLevelClass:function(level){
			var className='';
			if(level<=1){
				className='nav-second-level';
			}else if(level==2){
				className='nav-third-level';
			}else if(level==3){
				className='nav-four-level';
			}else if(level>3){
				className='nav-five-level';
			}
			return className;
		},
		loadData:function(parentId,level,callBack){
			var g = this, p = this.options;
			Public.ajax(p.url,{parentId:parentId},function (data) {
				var html=[''],hasChildren=0;
				$.each(data,function(i,o){
					hasChildren=parseInt(o.hasChildren);
					if(level==1&&hasChildren==0){
						return true;
					}
					html.push('<li class="fun data_empty" data-children="',hasChildren,'" data-level="',level,'">');
					html.push('<a href="javascript:void(0);" title="',o.description,'" data-id="',o.id,'" data-code="',o.code,'" data-url="',o.url,'">');
					if (Public.isBlank(o.icon)) {
						html.push('<i class="fa fa-edit"></i>');//图标
					}else{
						html.push('<i class="fa ',o.icon,'"></i>');//图标
					}
					if(hasChildren>0){
						html.push('<span class="nav-label">',o.name,'</span>');
						html.push('<span class="fa arrow"></span>');
					}else{
						html.push(o.name);
					}
					html.push('</a>');
					if(hasChildren>0){
						html.push('<ul class="nav ',g.getNavLevelClass(level),' collapse" id="sub_',o.id,'">');
						html.push('<li><span class="nav-loading"></span></li>');
						html.push('</ul>');
					}
					html.push('</li>');
				});
				callBack.call(g,html.join(''),data);
			});
		},
		_initTopToolbar:function(data){
			var g = this, p = this.options;
			var _topToolbar=$('div.navbar-top-toolbar');
			if(!_topToolbar.length) return;
			var html=['<ul class="nav navbar-top-links">'],hasChildren=0;
			$.each(data,function(i,o){
				hasChildren=parseInt(o.hasChildren);
				if(hasChildren==0){
					return true;
				}
				html.push('<li class="fun" data-children="',hasChildren,'" data-level="1">');
				html.push('<a href="javascript:void(0);" title="',o.description,'" data-id="',o.id,'" data-code="',o.code,'">');
				if (Public.isBlank(o.icon)) {
					html.push('<i class="fa fa-edit"></i>');
				}else{
					html.push('<i class="fa ',o.icon,'"></i>');//图标
				}
				html.push('<span class="nav-label">',o.name,'</span>');
				html.push('<span class="fa arrow"></span>');
				html.push('</a>');
				html.push('</li>');
			});
			html.push('<li class="drop-down-bar">');
			html.push('<a class="dropdown-toggle" data-toggle="dropdown">');
			html.push('<i class="fa fa-ellipsis-v"></i>');
			html.push('</a>');
			html.push('<ul class="dropdown-menu"></ul>');
			html.push('</li>');
			html.push('</ul>');
			_topToolbar.html(html.join(''));
			$.WinReszier.register($.proxy(g._topToolbarLayout, g));
		    g._topToolbarLayout(_topToolbar);
		    g._topToolbarEvent(_topToolbar);
		},
		_topToolbarLayout:function(topToolbar){
			var _topToolbar=_topToolbar||$('div.navbar-top-toolbar');
			if(!_topToolbar.length) return;
			_topToolbar.width($(window).width()-600);
			var collection = [],_dropdown=_topToolbar.find('li.drop-down-bar');
			_topToolbar.find('li.fun').insertBefore(_dropdown);
			_topToolbar.find('li.fun').each(function(){
				if(this.offsetTop > 0) {
					collection.push(this);
				}
			});
			if (collection.length > 0) {
				collection.splice(0,0,$(collection[0]).prev()[0]);
				collection = $(collection);
				_dropdown.find('>ul.dropdown-menu').empty().append(collection);
				_dropdown.removeClass('ui-hide');
			} else {
				_dropdown.addClass('ui-hide');
			}
		},
		_topToolbarEvent:function(topToolbar){
			var g = this, p = this.options;
			topToolbar.on('click',function(e){
				var _li=g._srcElement(e);
				if(!_li) return true;
				if(_li.hasClass('drop-down-bar')) return true;
				var data=g._getFunData(_li),hasChildren=parseInt(_li.data('children'));
				var _cloneLi=_li.clone();
				if(hasChildren>0){
					var html=[];
					html.push('<ul class="nav ',g.getNavLevelClass(1),' collapse" id="sub_',data.id,'">');
					html.push('<li><span class="nav-loading"></span></li>');
					html.push('</ul>');
					_cloneLi.append(html.join(''));
				}
				g.element.find('li.fun').remove();
				g.element.append(_cloneLi);
				//访问数据库读取菜单
				g.loadData(data['id'],2,function(html){
					$('>ul',_cloneLi).html(html);
				});
				if($("body").hasClass("mini-navbar")){
					navToggle();
				}
				g._toggle(_cloneLi);
			});
		},
		_srcElement:function(e){
			var $clicked = $(e.target || e.srcElement),li=null;
			if($clicked.is('li')){
				return $clicked;
			}
			if($clicked.is('a')){
				li=$clicked.parent();
			}
			if($clicked.is('span')||$clicked.is('i')){
				li=$clicked.parent().parent();
			}
			if(!li) return false;
			return li.is('li')?li:false;
		},
		_getFunData:function(_li){
			var _a=$('>a',_li),data={};
			data['id']=_a.data('id');
			data['code']=_a.data('code');
			data['description']=_a.attr('title');
			if(Public.isBlank(data['description'])){
				data['description']=$.trim(_a.text())
			}
			data['url']=_a.data('url');
			return data;
		},
		bindEvent:function(){
			var g = this, p = this.options;
			g.element.on('click',function(e){
				var _li=g._srcElement(e);
				if(!_li) return true;
				var hasChildren=parseInt(_li.data('children')),data=g._getFunData(_li);
				if(_li.hasClass('fun')){
					if(hasChildren > 0){
						if(_li.hasClass('data_empty')){
							//下级菜单未展开
							var level=parseInt(_li.data('level'))+1;//层级加1
							//访问数据库读取菜单
							g.loadData(data['id'],level,function(html){
								$('>ul',_li).html(html);
								_li.removeClass('data_empty');
							});
						}
						if($.isFunction(p.onClick)){
							p.onClick.call(window,data['id'],data['code'],data['description'],data['url']);
						}
					}else{
						if($.isFunction(p.onClick)){
							p.onClick.call(window,data['id'],data['code'],data['description'],data['url']);
						}
					}
				}else{
					if($.isFunction(p.onClick)){
						p.onClick.call(window,data['id'],data['code'],data['description'],data['url']);
					}
				}
				g._toggle(_li);
				if(Public.isNotBlank(data['url'])){
					if($(window).width() < 769){
						navToggle();
						return true;
					}
					//菜单折叠时
					if($("body").hasClass("collapse-menu")){
						navToggle();
						return true;
					}
				}
				return true;
			});
		},
		_toggle:function(li){
			var _ul=$('>ul',li);
			if(!_ul.length) return;
			if(_ul.hasClass('in')){
				this._close(li)
			}else{
				this._open(li);
			}
		},
		_open:function(li){
			var g = this, p = this.options;
			$('>ul',li).addClass('in');
			li.addClass('active');
			if(p.autoClose){
				li.siblings('.active').each(function(){
					g._close($(this));
				});
			}
		},
		_close:function(li){
			$('>ul',li).removeClass('in');
			li.removeClass('active');
		}
	});

})(jQuery);