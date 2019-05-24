/********************************
title:      首页Tab效果 插件
Author:     xx
Date :      2017-06-12
*********************************/
(function($) {

	$.fn.contentTabs = function (op){
		var obj=this.data('ui_content_tabs');
		if(!obj){
			new contentTabs(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['addTabItem','removeTabItem','selectTabItem','getActiveTab','getActiveTabId','reload','getIFrame'],function(i,m){
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

	function contentTabs(el,op){
		this.options={};
		this.element=$(el);
		this.centerWidth=0;
		this.set(op);
		this.init();
		this.bindEvent();
		this.element.data('ui_content_tabs',this);
	}
	
	$.extend(contentTabs.prototype,{
		set:function(op){
			this.options=$.extend({
				menuItemExpr:'a.ui_menuItem',
				iframeContentExpr:'div.ui_main_content',
				showActiveExpr:'.ui_tabShowActive',
				closeAllExpr:'.ui_tabCloseAll',
				closeOthereExpr:'.ui_tabCloseOther',
				tabLeftExpr:'.ui_tabLeft',
				tabRightExpr:'.ui_tabRight',
				onBeforeRemoveTabItem:null
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			g.pageTabs=$('.page-tabs',this.element);
			g.pageTabsContent=$('.page-tabs-content',this.element);
			g.iframeContent=$(p.iframeContentExpr);
			g.iframeloading=$('<div class="ui-tab-loading"></div>').appendTo(g.iframeContent);
			//创建一个覆盖全屏的div 强制遮挡操作 避免用户快速操作 创建tab
			g.pageloading=$('<div class="ui-page-screen-over"></div>').appendTo('body');
		},
		bindEvent:function(){
			var g = this, p = this.options;
			//注册默认链接打开事件
			$(p.menuItemExpr).on('click',function(e){
				g.addTabItem({tabid:$(this).data('id'), text:$.trim($(this).text()), url:$(this).data('url')});
			});
			//选择选择卡
			g.pageTabsContent.on('click', '.ui_menuTab', function(){
				g._selectTabItem($(this));
				return false;
			});
			// 关闭选项卡菜单
			g.pageTabsContent.on('click', '.ui_menuTab i', function(){
				g.closeTab($(this).parent());
				return false;
			});
			//关闭其他
			$(p.closeOthereExpr,g.element).on("click", function(){
				g.closeOtherTabs();
			});
			//关闭全部选项卡
			$(p.closeAllExpr,g.element).on("click",function(){
				g.closeAllTabs();
			});
			//定位当前
			$(p.showActiveExpr,g.element).on("click",function(){
				g.showActiveTab();
			});
			//左右移动
			$(p.tabLeftExpr,g.element).on("click",function(){
				g.scrollTabLeft();
				return false;
			});
			$(p.tabRightExpr,g.element).on("click",function(){
				g.scrollTabRight();
				return false;
			});
		},
		_calSumWidth:function(elements){//计算元素集合的总宽度
			var width = 0;
			$(elements).each(function() {
				width += $(this).outerWidth(true);
			});
			return width;
		},
		scrollToTab:function(tabItem){//滚动到指定选项卡
			var g = this, p = this.options;
			var marginLeftVal = g._calSumWidth($(tabItem).prevAll()),marginRightVal = g._calSumWidth($(tabItem).nextAll());
			// 可视区域非tab宽度
			var tabOuterWidth = g._calSumWidth(g.element.children().not(g.pageTabs));
			//可视区域tab宽度
			var visibleWidth = g.element.outerWidth(true) - tabOuterWidth;
			var contentWidth = g.pageTabsContent.outerWidth();
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
				scrollVal = marginLeftVal - $(tabItem).prev().outerWidth(true);
			}
			g.pageTabsContent.animate({marginLeft: 0 - scrollVal + 'px'},"fast");
		},
		scrollTabLeft:function(){//查看左侧隐藏的选项卡
			var g = this, p = this.options;
			var marginLeftVal = Math.abs(parseInt(g.pageTabsContent.css('margin-left')));
			// 可视区域非tab宽度
			var tabOuterWidth = g._calSumWidth(g.element.children().not(g.pageTabs));
			//可视区域tab宽度
			var visibleWidth = g.element.outerWidth(true) - tabOuterWidth;
			//实际滚动宽度
			var scrollVal = 0;
			if (g.pageTabsContent.width() < visibleWidth) {
				return false;
			} else {
				var tabElement =g.pageTabsContent.children(".ui_menuTab:first");
				var offsetVal = 0;
				while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {
					//找到离当前tab最近的元素
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
			g.pageTabsContent.animate({marginLeft: 0 - scrollVal + 'px'},"fast");
		},
		scrollTabRight:function(){//查看右侧隐藏的选项卡
			var g = this, p = this.options;
			var marginLeftVal = Math.abs(parseInt(g.pageTabsContent.css('margin-left')));
			// 可视区域非tab宽度
			var tabOuterWidth = g._calSumWidth(g.element.children().not(g.pageTabs));
			//可视区域tab宽度
			var visibleWidth = g.element.outerWidth(true) - tabOuterWidth;
			//实际滚动宽度
			var scrollVal = 0;
			if (g.pageTabsContent.width() < visibleWidth) {
				return false;
			} else {
				var tabElement =g.pageTabsContent.children(".ui_menuTab:first");
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
				scrollVal = g._calSumWidth($(tabElement).prevAll());
				if (scrollVal > 0) {
					g.pageTabsContent.animate({marginLeft: 0 - scrollVal + 'px'},"fast");
				}
			}
		},
		_getIFrameById:function(tabId){
			var _iframe=null;
			$('iframe',this.iframeContent).each(function() {
				if ($(this).data('id') == tabId) {
					_iframe=$(this);
					return false;
				}
			});
			return _iframe;
		},
		getIFrame:function(tabId){
			return this._getIFrameById(tabId);
		},
		_getTabById:function(tabId){
			var _tabItem=null;
			$('a.ui_menuTab',this.pageTabsContent).each(function() {
				if ($(this).data('id') == tabId) {
					_tabItem=$(this);
					return false;
				}
			})
			return _tabItem;
		},
		getActiveTab:function(){
			return $('a.active',this.pageTabsContent);
		},
		getActiveTabId:function(){
			var _tab=this.getActiveTab();
			return _tab.data('id');
		},
		_selectTabItem:function(_tabItem){
			if (!_tabItem.hasClass('active')) {//不是展示状态
				_tabItem.addClass('active').siblings('.ui_menuTab').removeClass('active');
				var _iframe=this._getIFrameById(_tabItem.data('id')).show();
				_iframe.siblings('iframe').hide();
				try{
					//Chrome 53 - Chrome 55 选项卡页面切换滚动条消失bug处理
					_iframe[0].style.height = '99%';
					_iframe[0].scrollWidth;
					_iframe[0].style.height = '100%';  
				}catch(e){}
				this.scrollToTab(_tabItem);
			}
		},
		selectTabItem:function(tabId){
			var _tabItem=this._getTabById(tabId);
			if(_tabItem){// 选项卡菜单已存在
				this._selectTabItem(_tabItem);
				return true;
			}
			return false;
		},
		addTabItem:function(options){//添加选项卡
			var g = this,tabUrl = options.url,tabId = options.tabid,tabName = options.text;
			if (tabUrl == undefined || $.trim(tabUrl).length == 0) return false;
			var flag=g.selectTabItem(tabId);
			if(!flag){// 选项卡菜单不存在
				var _tabHtml=['<a href="javascript:;" class="ui_menuTab active" data-id="',tabId,'">',tabName,'<i class="fa fa-times-circle"></i></a>'];
				$('a.ui_menuTab',g.pageTabsContent).removeClass('active');
				// 添加选项卡
				_tabItem=$(_tabHtml.join('')).appendTo(g.pageTabsContent);
				$('iframe',g.iframeContent).hide();
				// 添加选项卡对应的iframe
				var _iframeHtml=['<iframe class="ui_iframe" style="width:100%;height:100%;border:none 0;" frameborder="0" allowfullscreen="true" allowtransparency="true" data-id="',tabId,'"></iframe>'];
				g.iframeloading.show();
				g.pageloading.show();//打开强制遮挡 
				var _iframe=$(_iframeHtml.join('')).appendTo(g.iframeContent);
				_iframe.attr("name", tabId).attr("id", tabId).bind('load.tab', function (){
                    g.iframeloading.hide();
                    //延时关闭遮挡层
                    setTimeout(function(){
                    	g.pageloading.hide();
                    },1000);
                    /*iphone在iframe页面的宽度不受父页面影响，避免撑开页面*/
                    if($.isIosPhone) {
                    	var ifrBody=$(_iframe.prop('contentWindow').document.body); 
                    	//重新修改body长宽值
                    	ifrBody.css({width:g.iframeContent.width()-5,height:g.iframeContent.height()-5});
                    }
                });
				setTimeout(function(){
					//加入csrf token
					tabUrl = $.getCSRFUrl(tabUrl);
					_iframe.attr("src", tabUrl);
				},1);
				g.scrollToTab(_tabItem);
			}
		},
		_removeTabItem:function(tabItem){
			var g = this, p = this.options;
			var _iframe=g._getIFrameById($(tabItem).data('id'));
			//关闭前判断是否允许关闭
			if($.isFunction(p.onBeforeRemoveTabItem)){
				var flag=p.onBeforeRemoveTabItem.call(g,tabItem,_iframe);
				if(!flag){
					return false;
				}
			}
			if (_iframe){
				var el = _iframe.get(0);
				try{
          	  	 	el.contentWindow.document.write('');//清空iframe的内容
				    el.contentWindow.close();//避免iframe内存泄漏
          	  	}catch(e){}
          	  	_iframe.attr('src', "about:blank");
          	  	_iframe.remove();
				el=null;
				_iframe=null;
            }
			$(tabItem).remove();
			return true;
		},
		removeTabItem:function(tabid){
			var tabItem=this._getTabById(tabid);
			this.closeTab(tabItem);
		},
		closeTab:function(tabItem){//关闭选项卡菜单
			var g = this, p = this.options,_flag=true;
			var closeTabId = $(tabItem).data('id'),currentWidth = $(tabItem).width();
			// 当前元素处于活动状态
			if ($(tabItem).hasClass('active')) {
				// 当前元素后面有同辈元素，使后面的一个元素处于活动状态
				if ($(tabItem).next('.ui_menuTab').size()) {
					var activeItem=$(tabItem).next('.ui_menuTab:eq(0)');
					var marginLeftVal = parseInt(g.pageTabsContent.css('margin-left'));
					if (marginLeftVal < 0) {
						g.pageTabsContent.animate({marginLeft: (marginLeftVal + currentWidth) + 'px'},"fast");
					}
					_flag=g._removeTabItem(tabItem);
					if(_flag){
						g._selectTabItem(activeItem);
					}
					return;
				}
				// 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
				if ($(tabItem).prev('.ui_menuTab').size()) {
					var activeItem=$(tabItem).prev('.ui_menuTab:last');
					_flag=g._removeTabItem(tabItem);
					if(_flag){
						g._selectTabItem(activeItem);
					}
					return;
				}
			}else{//直接移除当前选项卡
				_flag=g._removeTabItem(tabItem);
				if(_flag){
					g.scrollToTab(g.getActiveTab());
				}else{
					g._selectTabItem(tabItem);
				}
			}
		},
		closeOtherTabs:function(){//关闭其他选项卡
			var g = this, p = this.options;
			var _flag=true,_tabItem=null;
			g.pageTabsContent.children("[data-id]").not(":first").not(".active").each(function() {
				_flag=g._removeTabItem($(this));
				if(!_flag){
					_tabItem=$(this);
				}
	        });
			if(_tabItem){
				g._selectTabItem(_tabItem);
			}else{
				g.pageTabsContent.css("margin-left", "0");
			}
		},
		closeAllTabs:function(){//关闭全部选项卡
			var g = this, p = this.options;
			var _flag=true,_tabItem=null;
			g.pageTabsContent.children("[data-id]").not(":first").each(function() {
				_flag=g._removeTabItem($(this));
				if(!_flag){
					_tabItem=$(this);
				}
	        });
			if(_tabItem){
				g._selectTabItem(_tabItem);
			}else{
				g.pageTabsContent.children("[data-id]:first").each(function() {
					g._selectTabItem($(this));
			    });
			    g.pageTabsContent.css("margin-left", "0");
			}
		},
		showActiveTab:function(){//定位当前选项卡
			this.scrollToTab(this.getActiveTab());
		},
		reload: function (tabid){
            var g = this, p = this.options;
            var iframe =g._getIFrameById(tabid);
            if(iframe){
            	var url = $(iframe).attr("src");
            	g.iframeloading.show();
            	iframe.attr("src", url).unbind('load.tab').bind('load.tab', function (){
            		 g.iframeloading.hide();
                });
            }
        }
	});
})(jQuery);