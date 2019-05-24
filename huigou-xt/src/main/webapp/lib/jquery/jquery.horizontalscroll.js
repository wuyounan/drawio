/********************************
title:      水平滚动 插件
Author:     xx
Date :      2018-09-21
*********************************/
(function($) {

	$.fn.horizontalscroll = function (op){
		var obj=this.data('ui_horizontal_scroll');
		if(!obj){
			new horizontalscroll(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['addItem','removeItem','selectItem'],function(i,m){
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

	function horizontalscroll(el,op){
		this.options={};
		this.element=$(el);
		this.centerWidth=0;
		this.set(op);
		this.init();
		this.bindEvent();
		this.element.data('ui_horizontal_scroll',this);
	}
	
	$.extend(horizontalscroll.prototype,{
		set:function(op){
			this.options=$.extend({
				itemExpr:'.ui-horizontal-item',
				buttonClass:'btn-primary',
				isPrevOne:false
			},this.options, op||{});
			if($.isMobile()){
				this.options.isPrevOne=false;
			}
		},
		init:function(){
			var g = this, p = this.options;
			this.element.addClass('ui-horizontal-content').wrap(function(){
				return '<div class="ui-horizontal"><div class="ui-horizontal-warp"></div></div>';
			});
			var _buttonClass=p.buttonClass;
			g.pageItems=this.element.parent();
			g.main=g.pageItems.parent();
			g.leftButton=$('<button class="ui-horizontal-button scroll-left btn '+_buttonClass+'"><i class="fa fa-backward"></i></button>').prependTo(g.main);
			g.rightButton=$('<button class="ui-horizontal-button scroll-right btn '+_buttonClass+'"><i class="fa fa-forward"></i></button>').appendTo(g.main);
			$.WinReszier.register($.proxy(g._resize, g));
			g._resize();
		},
		bindEvent:function(){
			var g = this, p = this.options;
			//左右移动
			g.leftButton.on("click",function(){
				g.scrollTabLeft();
				return false;
			});
			g.rightButton.on("click",function(){
				g.scrollTabRight();
				return false;
			});
		},
		_resize:function(){
			var g = this, p = this.options;
			// 可视区域非tab宽度
			var tabOuterWidth = g._calSumWidth(g.main.children().not(g.pageItems));
			//可视区域tab宽度
			var visibleWidth = g.main.outerWidth(true) - tabOuterWidth;
			if (g.element.width() < visibleWidth) {
				g.leftButton.hide();
				g.rightButton.hide();
				g.pageItems.removeClass('ui-horizontal-padding-left');
			}else{
				g.leftButton.show();
				g.rightButton.show();
				g.pageItems.addClass('ui-horizontal-padding-left');
			}
			g.element.css({marginLeft: 0 });
		},
		_calSumWidth:function(elements){//计算元素集合的总宽度
			var width = 0;
			$(elements).each(function() {
				width += $(this).outerWidth(true);
			});
			return width;
		},
		scrollTabLeft:function(){//查看左侧隐藏的选项卡
			var g = this, p = this.options;
			var marginLeftVal = Math.abs(parseInt(g.element.css('margin-left')));
			// 可视区域非tab宽度
			var tabOuterWidth = g._calSumWidth(g.main.children().not(g.pageItems));
			//可视区域tab宽度
			var visibleWidth = g.main.outerWidth(true) - tabOuterWidth;
			//实际滚动宽度
			var scrollVal = 0;
			if (g.element.width() < visibleWidth) {
				return false;
			} else {
				var tabElement =g.element.children(p.itemExpr+":first");
				var offsetVal = 0;
				while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
					//找到离当前最近的元素
					offsetVal += $(tabElement).outerWidth(true);
					tabElement = $(tabElement).next();
				}
				offsetVal = 0;
				if (g._calSumWidth($(tabElement).prevAll()) > visibleWidth) {
					while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
						offsetVal += $(tabElement).outerWidth(true);
						tabElement = $(tabElement).prev();
					}
					scrollVal = g._calSumWidth($(tabElement).prevAll());
				}
			}
			g.element.animate({marginLeft: 0 - scrollVal + 'px'},"fast");
		},
		scrollTabRight:function(){//查看右侧隐藏的选项卡
			var g = this, p = this.options;
			var marginLeftVal = Math.abs(parseInt(g.element.css('margin-left')));
			// 可视区域非tab宽度
			var tabOuterWidth = g._calSumWidth(g.main.children().not(g.pageItems));
			//可视区域tab宽度
			var visibleWidth = g.main.outerWidth(true) - tabOuterWidth;
			//实际滚动宽度
			var scrollVal = 0;
			if (g.element.width() < visibleWidth) {
				return false;
			} else {
				var tabElement =g.element.children(p.itemExpr+":first");
				var offsetVal = 0;
				while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
					//找到离当前tab最近的元素
					offsetVal += $(tabElement).outerWidth(true);
					tabElement = $(tabElement).next();
				}
				offsetVal = 0;
				while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
					offsetVal += $(tabElement).outerWidth(true);
					tabElement = $(tabElement).next();
				}
				var _prevAll= null;
				if(p.isPrevOne){
					_prevAll= $(tabElement).prev().prevAll();
				}else{
					_prevAll= $(tabElement).prevAll();
				}
				scrollVal = g._calSumWidth(_prevAll);
				if (scrollVal > 0) {
					g.element.animate({marginLeft: 0 - scrollVal + 'px'},"fast");
				}
			}
		}
	});
})(jQuery);