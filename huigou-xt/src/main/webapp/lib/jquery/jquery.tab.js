/********************************
title:      tab
Author:     xx
Date :      2017-06-27
*********************************/
(function($) {
	
	if ($.fn.tab) {
		$.fn.bsTab = $.fn.tab;
	}
	
	$.fn.tab = function (op){
		var obj=this.data('ui_tab_drop');
		if(!obj){
			new tabDrop(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['show','init'],function(i,m){
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

    function tabDrop(el,op){
		this.options={};
		this.element=$(el);
		this.set(op);
		this.init();
		this.bindEvent();
		this.element.data('ui_tab_drop',this);
	}
    
	$.extend(tabDrop.prototype,{
		set:function(op){
			this.options=$.extend({
				isDrop:true,
				text: '<i class="fa fa-bars"></i>',
				dropClassName:'ui-simple-tab',
				className:'ui-simple-float-tab',
				onClick:false
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			var content=$(">div.ui-tab-content",g.element),menu=$('>div.ui-tab-links > ul',g.element);
			//li 内容调整
			$('li',menu).each(function(i){
				$(this).attr('index',"li_"+i).addClass('ui-tab-item');
				var _a=$(this).find('a');
				if(!_a.length){
					_a=$('<a href="javascript:void(0);"></a>').html($(this).html());
					$(this).empty().append(_a);
				}
				if(!p.isDrop){
					_a.before('<div class="left"></div>','<div class="right"></div>');
				}
			});
			$('>div.layout',content).each(function(i){
				$(this).attr('index',"content_li_"+i).hide();
			});
			if(p.isDrop){
				if(!g.dropdown){
					g.element.addClass(p.dropClassName);
					var html=['<div class="btn-group hide tabdrop">'];
					html.push('<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">');
					html.push(p.text);
					html.push('&nbsp;<span class="caret"></span>');
					html.push('</button>');
					html.push('<ul class="dropdown-menu" role="menu"></ul>');
					html.push('</div>');
					g.dropdown = $(html.join('')).prependTo(g.element);
					$.WinReszier.register($.proxy(g.layout, g));
				}
				setTimeout(function(){g.layout();},10);
			}else{
				g.element.addClass(p.className);
			}
			g._show(menu.find('li:first'));
		},
		_dropdownActive:function(){
			if(this.dropdown){
				var flag=this.dropdown.find('.active').length > 0,btn=this.dropdown.find('button.btn');
				if(flag){
					btn.addClass('btn-warning').removeClass('btn-info');
				}else{
					btn.addClass('btn-info').removeClass('btn-warning');
				}
			}
		},
		layout:function(){
			if(!this.element.parents('body').length){
				return false;
			}
			var menu=$('>div.ui-tab-links > ul',this.element);
			var _width=this.element.width(),_height=this.element.height(),allWidth=0,collection = [];
			allWidth += this._parseInt(menu.css('paddingLeft'));
			allWidth += this._parseInt(menu.css('left'));
			var maxWidth=0,allHeight=0;
			menu.find('>li').each(function(){
				collection.push($(this).clone());
				allWidth=MathUtil.add(allWidth,$(this).outerWidth(true));
				maxWidth=Math.max(maxWidth,$(this).width());
				allHeight+=25;
			});
			maxWidth=Math.min(maxWidth,_width-50);
			if(allWidth>_width){
				menu.parent().width(allWidth+_width);
				var _ul=this.dropdown.find('ul').empty().append(collection);
				//修改下拉框显示位置
				_ul.css({left:'-'+maxWidth+'px',zIndex:100});
				this.dropdown.removeClass('hide');
				if(allHeight>_height-30){
					_ul.css({height:_height-30}).addClass('dom-overflow-auto');
				}
			}else{
				menu.parent().css({width:'100%'});
				this.dropdown.addClass('hide');
			}
			menu.css({marginLeft:'0px'});
		},
		bindEvent:function(){
			var g = this, p = this.options;
			var menu=$('>div.ui-tab-links > ul',g.element);
			if(g.dropdown){
				menu=menu.add(g.dropdown);
			}
			menu.unbind('click.jqueryTab').on('click.jqueryTab',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('i.fa')){
					$clicked=$clicked.parent();
				}
				if($clicked.parent().is('li')){
					$clicked=$clicked.parent();
				}
				if(!$clicked.is('li')){
					return;
				}
				g._show($clicked);
				if($.isFunction(p.onClick)){
					p.onClick.call(window,$clicked);
				}
			});
		},
		_parseInt:function(_value){
			_value=parseInt(_value,10);
			if(isNaN(_value)){
				return 0;
			}
			_value=_value.toFixed(0);
			return  parseInt(_value,10);
		},
		_calSumWidth:function(elements){//计算元素集合的总宽度
			var width = 0;
			$(elements).each(function() {
				width += $(this).outerWidth(true);
			});
			return width;
		},
		_show:function(_menuItem){
			var g = this, p = this.options;
			var id=_menuItem.attr('index');
			var menu=$('>div.ui-tab-links > ul',g.element);
			var content=$(">div.ui-tab-content",g.element);
			var _content=$('div[index="content_'+id+'"]',content);
			_content.siblings().hide();
			_content.show();
			$('li.ui-tab-item',g.element).removeClass("active");
			$('li[index="'+id+'"]',g.element).addClass("active");
			//修改滚动条位置显示被选中的栏目
			var tabItem=$('li.active',menu);
			var marginLeftVal = g._calSumWidth($(tabItem).prevAll()),marginRightVal = g._calSumWidth($(tabItem).nextAll());
			//可视区域tab宽度
			var visibleWidth = g.element.width();
			var contentWidth = menu.outerWidth();
			//实际滚动宽度
			var scrollVal = 0;
			if (contentWidth < visibleWidth) {
				scrollVal = 0;
			} else if (marginRightVal <= (visibleWidth - $(tabItem).outerWidth(true) - $(tabItem).next().outerWidth(true))) {
				if ((visibleWidth - $(tabItem).next().outerWidth(true)) > marginRightVal) {
					scrollVal = marginLeftVal;
					var tabElement = tabItem;
					while ((scrollVal - $(tabElement).outerWidth(true)) > (contentWidth - visibleWidth)) {
						scrollVal -= $(tabElement).prev().outerWidth(true);
						tabElement = $(tabElement).prev();
					}
				}
			} else if (marginLeftVal > (visibleWidth - $(tabItem).outerWidth(true) - $(tabItem).prev().outerWidth(true))) {
				scrollVal = marginLeftVal;
			}
			menu.animate({marginLeft: 0 - scrollVal + 'px'},"fast");
		},
		show:function(expr){
			var menu=$('>div.ui-tab-links > ul',this.element);
			var _menuItem=$(expr,menu);
			if(!_menuItem.length){
				return;
			}
			if(_menuItem.is('li.ui-tab-item')){
				this._show(_menuItem);
			}
		}
	});

	
})(jQuery);