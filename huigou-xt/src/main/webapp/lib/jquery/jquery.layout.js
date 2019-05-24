/********************************
title:      bootstrap_layout 插件
Author:     xx
Date :      2017-06-12
*********************************/
(function($) {
	
	$.fn.layout = function (op){
		var obj=this.data('ui_bootstrap_layout');
		if(!obj){
			new bootstrapLayout(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['setCenterTitle','setLeftTitle','setRightTitle','setRightCollapse','setLeftCollapse','setTopHeight','setBottomHeight'],function(i,m){
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

	function bootstrapLayout(el,op){
		this.options={};
		this.element=$(el);
		this.centerWidth=0;
		this.set(op);
		this.init();
		this.bindEvent();
		this.element.data('ui_bootstrap_layout',this);
	}
	
	$.extend(bootstrapLayout.prototype,{
		set:function(op){
			this.options=$.extend({
				height:'100%',
				heightDiff:0,
				topHeight: 50,
		        bottomHeight: 50,
		        leftWidth: 2,
		        minLeftWidth: 2,
		        maxLeftWidth: 5,
		        centerWidth: 12,
		        rightWidth: 2,
		        minRightWidth: 2,
		        maxRightWidth: 5,
		        allowLeftCollapse: true,      //是否允许 左边可以隐藏
		        allowRightCollapse: true,     //是否允许 右边可以隐藏
		        allowLeftResize: true,      //是否允许 左边可以调整大小
		        allowRightResize: true,     //是否允许 右边可以调整大小
		        colXsClass:'col-xs-12',
		        colSmClass:'col-sm-',
		        onSizeChanged:false
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			g.layout = $(this.element).hide();
			//对布局中各个position重新排序  排序规则  top -> left -> center -> right -> bottom
			var _positions={};
			$("> div[position]", g.layout).each(function(){
				_positions[$(this).attr('position')]=$(this);
			});
			$.each(['top','left','center','right','bottom'],function(i,k){
				g.layout.append(_positions[k]);
			});
			//布局对象初始化
			g.layout.addClass('ui-layout');
			g.centerWidth=p.centerWidth;
			if ($("> div[position=top]", g.layout).length > 0){
				g.top = $("> div[position=top]", g.layout).addClass('row-fluid');
				g.topHeight = p.topHeight;
                g.top.height(g.topHeight);
			}
			if ($("> div[position=bottom]", g.layout).length > 0){
                g.bottom = $("> div[position=bottom]", g.layout).addClass('row-fluid');
                g.bottomHeight = p.bottomHeight;
                g.bottom.height(g.bottomHeight);
            }
			if ($("> div[position=left]", g.layout).length > 0){
				 g.leftWidth=p.leftWidth;
				 g.left = $("> div[position=left]", g.layout).wrap('<div class="ui-layout-left"></div>').parent();
				 g.left.addClass(p.colXsClass).addClass(p.colSmClass+g.leftWidth);
				 g.left.content = $("> div[position=left]", g.left).addClass('ui-layout-content').addClass('dom-overflow-y').wrap('<div class="ui-layout-warp"></div>');
				 g.centerWidth-=g.leftWidth;
				 g.left.header = $('<div class="ui-layout-header"><div class="ui-layout-header-inner"></div></div>');
	             g.left.content.before(g.left.header);
	             var lefttitle = g.left.content.attr("title");
	             if(lefttitle){
	            	 g.left.content.attr("title", "");
	                 $("div.ui-layout-header-inner", g.left.header).html(lefttitle);
	             }
	             if(p.allowLeftCollapse){
	            	 g.left.header.prepend('<div class="ui-layout-header-toggle"><a class="btn btn-default btn-xs" href="javascript:void(0);"><i class="fa fa-angle-double-left"></i></a></div>');
	            	 g.leftToggle = $('<a class="ui-layout-toggle-absolute btn btn-warning btn-xs" href="javascript:void(0);"><i class="fa fa-angle-double-right"></i></a>');
	            	 g.leftToggle.appendTo(g.layout);
	             }
			     if(p.allowLeftResize){
			    	 var html=['<div class="ui-spinner ui-layout-header-resize hidden-xs"><span class="input-group-btn-vertical">'];
					 html.push('<button class="btn btn-default btn-xs plus" type="button"><i class="fa fa-angle-right"></i></button>');  
					 html.push('<button class="btn btn-default btn-xs minus" type="button"><i class="fa fa-angle-left"></i></button>'); 
					 html.push('</span></div>'); 
					 g.left.header.prepend(html.join(''));
					 g.left.plus=$('button.plus',g.left.header);
					 g.left.minus=$('button.minus',g.left.header);
			     }
			}
			if ($("> div[position=right]", g.layout).length > 0){
				 g.rightWidth=p.rightWidth;
				 g.right = $("> div[position=right]", g.layout).wrap('<div class="ui-layout-right"></div>').parent();
				 g.right.addClass(p.colXsClass).addClass(p.colSmClass+g.rightWidth);
				 g.right.content = $("> div[position=right]", g.right).addClass('ui-layout-content').addClass('dom-overflow-y').wrap('<div class="ui-layout-warp"></div>');
				 g.centerWidth-=g.rightWidth;
				 g.right.header = $('<div class="ui-layout-header"><div class="ui-layout-header-inner"></div></div>');
	             g.right.content.before(g.right.header);
	             var righttitle = g.right.content.attr("title");
	             if(righttitle){
	                g.right.content.attr("title", "");
	                $("div.ui-layout-header-inner", g.right.header).html(righttitle);
	             }
	             if(p.allowRightCollapse){
	            	 g.right.header.prepend('<div class="ui-layout-header-toggle"><a class="btn btn-default btn-xs" href="javascript:void(0);"><i class="fa fa-angle-double-right"></i></a></div>');
	            	 g.rightToggle = $('<a class="ui-layout-toggle-absolute btn btn-warning btn-xs" href="javascript:void(0);"><i class="fa fa-angle-double-left"></i></a>');
				     g.rightToggle.appendTo(g.layout);
	             }
			     if(p.allowRightResize){
			    	 var html=['<div class="ui-spinner ui-layout-header-resize hidden-xs"><span class="input-group-btn-vertical">'];
					 html.push('<button class="btn btn-default btn-xs plus" type="button"><i class="fa fa-angle-left"></i></button>');  
					 html.push('<button class="btn btn-default btn-xs minus" type="button"><i class="fa fa-angle-right"></i></button>'); 
					 html.push('</span></div>'); 
					 g.right.header.prepend(html.join(''));
					 g.right.plus=$('button.plus',g.right.header);
					 g.right.minus=$('button.minus',g.right.header);
			     }
			}
			if ($("> div[position=center]", g.layout).length > 0){
				 g.center = $("> div[position=center]", g.layout).wrap('<div class="ui-layout-center"></div>').parent();
				 g.center.addClass(p.colXsClass).addClass(p.colSmClass+g.centerWidth);
	             g.center.content = $("> div[position=center]", g.center).addClass('ui-layout-content').addClass('dom-overflow-y').wrap('<div class="ui-layout-warp"></div>');
	             var centertitle = g.center.content.attr("title");
	             if(centertitle){
	            	 g.center.content.attr("title", "");
	                 g.center.header = $('<div class="ui-layout-header"><div class="ui-layout-header-inner" style="padding-left:15px;"></div></div>');
	                 g.center.content.before(g.center.header);
	                 $("div.ui-layout-header-inner", g.center.header).html(centertitle);
	             }
			}
			g.layout.show();
			this._resize();
		},
		bindEvent:function(){
			var g = this, p = this.options;
			if(g.left){
				$(".ui-layout-header-toggle", g.left).click(function (){
					g.setLeftCollapse(true);
			    });
				if(p.allowLeftResize){
					g.left.plus.click(function (){
						g.setLeftCol(1);
					});
					g.left.minus.click(function (){
						g.setLeftCol(-1);
					});
				}
				if(g.leftToggle){
					g.leftToggle.click(function (){
						g.setLeftCollapse(false);
				    });
				}
			}
			if(g.right){
				$(".ui-layout-header-toggle", g.right).click(function (){
			        g.setRightCollapse(true);
			    });
				if(p.allowRightResize){
					g.right.plus.click(function (){
						g.setRightCol(1);
					});
					g.right.minus.click(function (){
						g.setRightCol(-1);
					});
				}
				if(g.rightToggle){
					g.rightToggle.click(function (){
						g.setRightCollapse(false);
				    });
				}
				
			}
			g.layout.find('div.ui-layout-content').on('scroll', function () {
				$.closePicker();
			});
			$.WinReszier.register($.proxy(g._resize, g));
		},
		setLeftCol:function(col){
			var g = this, p = this.options;
			var tempCenterClassName=p.colSmClass+g.centerWidth;
			var tempLeftClassName=p.colSmClass+g.leftWidth;
			var _tempCol=g.leftWidth+col;
			if(_tempCol < p.minLeftWidth){
				_tempCol= p.minLeftWidth;
			}
			if(_tempCol > p.maxLeftWidth){
				_tempCol= p.maxLeftWidth;
			}
			g.centerWidth+=g.leftWidth-_tempCol;
			g.leftWidth=_tempCol;
			g.left.removeClass(tempLeftClassName).addClass(p.colSmClass+g.leftWidth);
			g.center.removeClass(tempCenterClassName).addClass(p.colSmClass+g.centerWidth);
			g._callFunction('onSizeChanged');
		},
		setRightCol:function(col){
			var g = this, p = this.options;
			var tempCenterClassName=p.colSmClass+g.centerWidth;
			var tempRightClassName=p.colSmClass+g.rightWidth;
			var _tempCol=g.rightWidth+col;
			if(_tempCol < p.minRightWidth){
				_tempCol= p.minRightWidth;
			}
			if(_tempCol > p.maxRightWidth){
				_tempCol= p.maxRightWidth;
			}
			g.centerWidth+=g.rightWidth-_tempCol;
			g.rightWidth=_tempCol;
			g.right.removeClass(tempRightClassName).addClass(p.colSmClass+g.rightWidth);
			g.center.removeClass(tempCenterClassName).addClass(p.colSmClass+g.centerWidth);
			g._callFunction('onSizeChanged');
		},
		setLeftCollapse:function(isCollapse){
			var g = this, p = this.options;
			var _isHidden=g.left.is(':hidden');
			var tempClassName=p.colSmClass+g.centerWidth,_offset=null;
			if(isCollapse){
				if(_isHidden) return;
				g.centerWidth+=g.leftWidth;
				_offset=g.left.position();
				g.leftToggle.css({top:_offset.top+6,left:10}).show();
				g.left.hide();
			}else{
				if(!_isHidden) return;
				g.centerWidth-=g.leftWidth;
				g.left.show();
				g.leftToggle.hide();
			}
			g.center.removeClass(tempClassName).addClass(p.colSmClass+g.centerWidth);
			g._callFunction('onSizeChanged');
		},
		setRightCollapse:function(isCollapse){
			var g = this, p = this.options;
			var _isHidden=g.right.is(':hidden');
			var tempClassName=p.colSmClass+g.centerWidth,_offset=null;
			if(isCollapse){
				if(_isHidden) return;
				g.centerWidth+=g.rightWidth;
				_offset=g.right.position();
				g.rightToggle.css({top:_offset.top+6,left:g.layout.width()-20}).show();
				g.right.hide();
			}else{
				if(!_isHidden) return;
				g.centerWidth-=g.rightWidth;
				g.right.show();
				g.rightToggle.hide();
			}
			g.center.removeClass(tempClassName).addClass(p.colSmClass+g.centerWidth);
			g._callFunction('onSizeChanged');
		},
		setTopHeight:function(height){
			var g = this, p = this.options;
			if ($("> div[position=top]", g.layout).length > 0){
				g.topHeight = p.topHeight=height;
                g.top.height(g.topHeight);
                g._resize();
                g._callFunction('onSizeChanged');
			}
		},
		setBottomHeight:function(height){
			var g = this, p = this.options;
			if ($("> div[position=bottom]", g.layout).length > 0){
                g.bottomHeight = p.bottomHeight =height;
                g.bottom.height(g.bottomHeight);
                g._resize();
                g._callFunction('onSizeChanged');
            }
		},
		_callFunction:function(funName){
			var g = this, p = this.options;
			var fn=p[funName];
			if($.isFunction(fn)){
				fn.call(this);
			}
		},
		_parseInt:function(_value){
			_value=parseInt(_value,10);
			if(isNaN(_value)){
				return 0;
			}
			_value=_value.toFixed(0);
			return  parseInt(_value,10);
		},
		_resize:function(){
			var g = this, p = this.options;
			if(!g.layout.parents('body').length){
				$.WinReszier.unregister($.proxy(g._resize, g));
				return false;
			}
			if(g.rightToggle&&g.rightToggle.length>0){
				g.rightToggle.hide();
			}
	        var h = 0,parentHeight = null;
	        var windowHeight = $.windowHeight();
	        var _id=g.layout.attr('id');
	        if (typeof (p.height) == "string" && p.height.indexOf('%') > 0){
	        	var layoutparent = g.layout.parent();
	            if (layoutparent[0].tagName.toLowerCase() == "body"){
	            	parentHeight = windowHeight;
	                parentHeight -= g._parseInt($('body').css('paddingTop'));
	                parentHeight -= g._parseInt($('body').css('paddingBottom'));
	            }else{
	            	var layoutContentParent=g.layout.parents('.ui-layout-content');
	            	if(layoutContentParent.length>0){
	            		parentHeight = layoutContentParent.height();
	            	}else{
	            		parentHeight = layoutparent.height();
	            	}
	            	if(parentHeight==0){
	            		parentHeight=windowHeight;
	            	}
	            }
	            h = parentHeight * parseFloat(p.height) * 0.01;
	            if (layoutparent[0].tagName.toLowerCase() == "body"){
	            	 h -= (g.layout.offset().top - g._parseInt($('body').css('paddingTop')));
	            }
	        }else{
	             h = parseInt(p.height);
	        }
	        h+=p.heightDiff;
	        g.layoutHeight = g.middleHeight = h;
	        if (g.top){
	        	g.middleHeight -= g.top.outerHeight();
	        }
	        if (g.bottom){
	        	g.middleHeight -= g.bottom.outerHeight();
	        }
	        if (g.center){
	        	if(!g.center.content.hasClass('ui-layout-height-auto')){
		        	g.middleHeight >= 0 && g.center.height(g.middleHeight);
		        	var contentHeight = g.middleHeight;
	                if (g.center.header){
	                	contentHeight -= g.center.header.outerHeight()+1;
	                }
	                contentHeight >= 0 && g.center.content.height(contentHeight);
	        	}
	        	g.center.content.removeClass('dom-overflow-y');
	        }
	        if (g.left){
	        	g.middleHeight >= 0 && g.left.height(g.middleHeight);
	        	var contentHeight = g.middleHeight;
	        	if (g.left.header){
	        		contentHeight -= g.left.header.outerHeight()+1;
	        	}
                contentHeight >= 0 && g.left.content.height(contentHeight);
                g.left.content.removeClass('dom-overflow-y');
            }
            if (g.right){
            	if(g.right.is(':hidden')){
                	g.rightToggle.css({left:g.layout.width()-20}).show();
                }
            	g.middleHeight >= 0 && g.right.height(g.middleHeight);
            	var contentHeight = g.middleHeight;
            	if (g.right.header){
            		contentHeight -= g.right.header.outerHeight()+1;
            	}
                contentHeight >= 0 && g.right.content.height(contentHeight);
                g.right.content.removeClass('dom-overflow-y');
            }
            g._callFunction('onSizeChanged');
		},
		setCenterTitle:function(html){
			$("div.ui-layout-header-inner", this.center.header).html(html);
		},
		setLeftTitle:function(html){
			$("div.ui-layout-header-inner", this.left.header).html(html);
		},
		setRightTitle:function(html){
			$("div.ui-layout-header-inner", this.right.header).html(html);
		}
	});
	
})(jQuery);