/*---------------------------------------------------------------------------*\
|  title:         工具提示框                                                                                                                                                               |
|  Author:        xx                                                          |
|  Created:       2013-11-07                                                  |
|  LastModified:  2014-03-06                                                  |
|  需求:<link rel="stylesheet" type="text/css" href="jquery.tooltip.css">       |
|       <script src="jquery.js" type="text/javascript"></script>              |
|  使用如:                                                                      |
|  $('#pp1').tooltip({content: '<span class="loading">加载中...</span>'});      |
|  $('#pp1').tooltip({                                                        |
|		width:100,                                                            |
|		position:'right',                                                     |
|		url:'ajax.html',                                                      |
|		param:function(){                                                     |
|			return {a:1};                                                     |
|		},                                                                    |
|		onShow: function(){                                                   |
|		}                                                                     |
|	});                                                                       |
|   $('#pp1').tooltip('enable'); $('#pp1').tooltip('disable');//调用方法      |
\*---------------------------------------------------------------------------*/
(function($) {
	$.fn.tooltip = function(op){
		return this.each(function() {
			var obj=$.data(this,'tooltip');
			if(!obj){
				new tooltip(this,op);
			}else{
				if (typeof op == "string") {
					var _self=$(this);
					$.each(['init','hide','enable','disable','destroy'],function(i,m){
						if(op==m){
							obj[m].call(obj,_self);
							return false;
						}
					});
				}else{
					obj.set(op);
				}
			}
		}); 
	};
	
	function tooltip(el,op){
		var $el=$(el),_self=this;
		this.id='tip_'+new Date().getTime()+""+Math.round(Math.random()*10000);
		this.canHide=true;
		this.options={};
		this.disabled = false;
		this.set(op);
		this.cacheData=null;
		$el.unbind(".tooltip");
		if(this.options.showEvent){
			$el.bind(this.options.showEvent + ".tooltip", function(e) {
				_self.init($el);
			});
		}
		if(this.options.hideEvent){
			$el.bind(this.options.hideEvent + ".tooltip", function(e) {
				_self.hide();
			});
		}
		$el.data('tooltip',this);
	}
	$.extend(tooltip.prototype, {
		set:function(op){
			this.options=$.extend({
				position  : 'bottom',//提示层显示位置
				content   : null,//提示层显示内容
				getContent: null,//获取内容方法
				url       : null,//AJAX访问地址
				dataType  : 'text',//AJAX返回数据类型
				cache     : true,//是否缓存数据
				param     : {},//AJAX访问参数
				loading   : '<span class="loading">加载中...</span>',//AJAX访问提示内容
				width	  : 'auto',//显示宽度
				showDelay : 200,//显示延时毫秒
				hideDelay : 200,//隐藏延时毫秒
				diffSet   : 10,//显示高度或宽度运行调节的大小
				heightDiff:0,
				showEvent : 'mouseenter',//显示触发方法
				hideEvent : 'mouseleave',//隐藏触发方法
				hold      : true,//鼠标移动到提示层是否显示
				onShow    : null,//显示时执行方法
				onHide    : null//隐藏时执行方法
			},this.options, op||{});
		},
		timer:function(){
			if (this.showTimer) {
				clearTimeout(this.showTimer);
				this.showTimer = null;
			}
			if (this.hideTimer) {
				clearTimeout(this.hideTimer);
				this.hideTimer = null;
			}
		},
		init:function($el){
			if(this.disabled) return;
			var tip=$('#'+this.id),_self=this;
			if(!tip.length){
				var html=['<div class="ui-tooltip" id="',this.id,'">'];
				html.push('<div class="ui-tooltip-overlay"></div>');
				html.push('<div class="ui-tooltip-content"></div>');
				html.push('<div class="ui-tooltip-arrow-outer"></div>');
				html.push('<div class="ui-tooltip-arrow"></div>');
				html.push('</div>');
				tip=$(html.join('')).appendTo("body");
			}
			tip.unbind("mouseenter").unbind("mouseleave");
			if(this.options.hold){
				tip.hover(function () {_self.timer();_self.canHide=false;},function () {_self.canHide=true;_self.hide();});
			}
			$('div.ui-tooltip-content',tip).css({width:this.options.width});
			if(this.options.content != null){
				this.update(tip);
				this.show($el,tip);
			}else if(this.options.url){
				if(this.cacheData){
					this.update(tip,this.cacheData);
					this.show($el,tip);
				}else{
					var param= typeof this.options.param == "function" ? this.options.param.call(window):this.options.param;
					this.update(tip,this.options.loading);
					this.show($el,tip);
					Public[this.options.dataType=='text'?'load':'ajax'](this.options.url,param,function(data){
						_self.update(tip,data);
						_self.show($el,tip);
						if(_self.options.cache){
							_self.cacheData=data;
						}
					});
				}
			}
		},
		show:function($el,tip){
			var _self=this;
			this.timer();
			this.showTimer=setTimeout(function(){
				tip.show();
				_self.positions($el);
				if(typeof _self.options.onShow == "function")
					 _self.options.onShow.call(_self,tip);
				tip.removeClass("ui-tooltip-top ui-tooltip-bottom ui-tooltip-left ui-tooltip-right").addClass("ui-tooltip-" + _self.options.position);
				var outer = tip.children("div.ui-tooltip-arrow-outer"),arrow = tip.children("div.ui-tooltip-arrow");
				var content=tip.children("div.ui-tooltip-content");
				var position_border = "border-" + _self.options.position + "-color",border_css={};
				$.each(['Top','Bottom','Left','Right'],function(i,a){border_css['border'+a+'Color']="";});
				outer.css(border_css).css(position_border,content.css(position_border));//.css({borderColor:'#ff0000'});
				arrow.css(border_css).css(position_border,content.css("backgroundColor"));//.css({borderColor:'#ff0000'});
				tip.find('div.ui-tooltip-overlay').css({width:tip.outerWidth()+10,height:tip.outerHeight()+10});
			},this.options.showDelay);
		},
		update:function(tip,content){
			if(!content||typeof content != "string"){
				if($.isFunction(this.options.getContent)){
					content=this.options.getContent.call(window,content);
				}else{
					content=this.options.content;
				}
			}
			$('div.ui-tooltip-content',tip).html(content);
		},
		positions:function($el,flag){
			var tip=$('#'+this.id);
			var position=this.options.position,tip_width=parseInt(this.options.width);
			var left = $el.offset().left,top = $el.offset().top;
			tip_width=isNaN(tip_width)?tip.outerWidth():(tip_width+12);
			var outer = tip.children("div.ui-tooltip-arrow-outer"),arrow = tip.children("div.ui-tooltip-arrow");
			var temp={},tempLeft=0,tempTop=0,diffSet=this.options.diffSet,heightDiff=this.options.heightDiff;
			$.each(['paddingLeft','marginLeft','paddingTop','marginTop'],function(i,o){
				tv=parseInt($el.css(o));
				tv=isNaN(tv)?0:tv;
				temp[o]=tv;
			});
			var limit = (function() {
				var _$w = $(window),
				ww = $.windowWidth(),
				wh = $.windowHeight(),
				dl = $.windowScrollLeft(),
				dt = $.windowScrollTop();
				// 坐标最大值限制(在可视区域内)
				maxX = ww  + dl;
				maxY = wh  + dt;
				return {
					minX : dl,
					minY : dt,
					maxX : maxX,
					maxY : maxY
				};
			})();
			var adjustTop=function(_top){//调整显示Top
				if((_top+tip.outerHeight()) > (limit.maxY+diffSet)){
					tempTop = (_top+tip.outerHeight())-limit.maxY;
				}
				outer.add(arrow).css({top:tempTop+13+(temp['paddingTop']+temp['marginTop'])});
				return (_top-tempTop+heightDiff);
			};
			var adjustLeft=function(_left){//调整显示Left
				if((_left+tip.outerWidth()) > (limit.maxX+10)){
					tempLeft = (_left+tip.outerWidth())-limit.maxX;
				}
				outer.add(arrow).css({left:tempLeft+13+(temp['paddingLeft']+temp['marginLeft'])});
				return (_left-tempLeft);
			};
			switch (position) {
			case "right":
				left += $el.outerWidth() + 6;
				top -=(temp['paddingTop']+temp['marginTop']);
				if(!flag){
					if((left+tip.outerWidth()) > (limit.maxX+diffSet)){
						this.options.position='left';
						return this.positions($el,true);
					}
				}
				top=adjustTop(top);
				break;
			case "left":
				left -= tip_width + 6;
				top -=(temp['paddingTop']+temp['marginTop']);
				if(!flag){
					if(left < (limit.minX-diffSet)){
						this.options.position='right';
						return this.positions($el,true);
					}
				}
				top=adjustTop(top);
				break;
			case "top":
				top -= tip.outerHeight() + 6;
				left -=(temp['paddingLeft']+temp['marginLeft']);
				if(!flag){
					if(top < (limit.minY-diffSet)){
						this.options.position='bottom';
						return this.positions($el,true);
					}
				}
				left=adjustLeft(left);
				break;
			case "bottom":
				top += $el.outerHeight() + 6;
				left -=(temp['paddingLeft']+temp['marginLeft']);
				if(!flag){
					if((top+tip.outerHeight()) > (limit.maxY+diffSet)){
						this.options.position='top';
						return this.positions($el,true);
					}
				}
				left=adjustLeft(left);
				break;
			}
			tip.css({left:left,top:top});
		},
		hide:function(){
			if(!this.canHide) return;
			var tip=$('#'+this.id),_self=this;
			if (tip.length>0) {
				this.timer();
				this.hideTimer = setTimeout(function() {
					tip.hide();
					if(typeof _self.options.onHide == "function")
						 _self.options.onHide.call(window,tip);
				}, this.options.hideDelay);
			}
		},
		close:function(){
			var tip=$('#'+this.id),_self=this;
			_self.timer();
			tip.hide();
			if(typeof _self.options.onHide == "function")
				 _self.options.onHide.call(window,tip);
		},
		enable: function() {
			this.disabled = false;
		},
		disable: function() {
			this.disabled = true;
		},
		destroy:function($el){
			this.timer();
			var tip=$('#'+this.id);
			if (tip.length>0) {
				tip.remove();
			}
			$el.removeData("tooltip");
			$el.unbind(".tooltip");
		}
	});
})(jQuery);