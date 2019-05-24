/*
 * Description: 左侧浮动层滑动弹出菜单
 * Author:      xx
 * Created:     2017-05-04
 */
(function($) {

    $.fn.floatDialog = function (op){
		var obj=this.data('ui_float_dialog');
		if(!obj){
			new floatDialog(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['show','close'],function(i,m){
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

	$.floatDialog = function (p){
		p=$.extend({
			openWidth:60, 
			openHeight:60,
			openLeft:-2,
			openTop:206,
			openHtml:'<a class="ui-open-btn" href="javascript:void(0);">&gt;</a>'
		},p||{});
		var $open=$('<div class="ui-float-open"></div>').appendTo('body');
		$open.html(p.openHtml);
		$open.css({left:p.openLeft,top:p.openTop,width:p.openWidth,height:p.openHeight});
		$open.floatDialog(p);
		return $open;
	};

	function floatDialog(el,op){
		this.options={};
		this.set(op);
		this.$open=$(el);
		this.$open.data('ui_float_dialog',this);
		this.init();
	}
	
	$.extend(floatDialog.prototype,{
		set:function(op){
			this.options=$.extend({
				hideOpen:true,
				width:300,
				height:380,
				left:0,
				top:117,
				param:{},
				title:'',
				url:null,
				content:null,
				onInit:false,
				onClose:false,
				onClick:false
			},this.options, op||{});
		},
		init:function(){
			var t=this,p=this.options;
			var html=['<div class="ui-float-dialog">'];
			html.push('<a class="ui-float-close" href="javascript:void(0);">X</a>');
			html.push('<h2 class="ui-float-title"></h2>');
			html.push('<div class="ui-float-content"></div>');
			html.push('</div>');
			this.$dialog=$(html.join('')).appendTo('body');
			this.$dialog.find('a.ui-float-close').on('click',function(){
				t.close();
			});
			this.$dialog.on('click',function(e){
				if($.isFunction(p.onClick)){
					p.onClick.call(this,e);
				}
			});
			this.$dialog.find('>*').hide();
			this.$dialog.hide();
			this.$open.on('click',function(){
				t.show();
			});
		},
		close:function(buttons){
			var t=this,p=this.options;
			if ($.browser.msie){
	        	if(parseInt($.browser.version)<=8){
	        		this.$dialog.find('>*').hide();
	        	    this.$dialog.hide();
					if(p.hideOpen){
						this.$open.show();
					}
	        	   	return;
	        	}
	        }
	    	this.$dialog.find('>*').hide();
	    	this.$dialog.delay(50).animate({
	    		width: 10
		    }, 300,function(){
		      	$(this).hide();
				if(p.hideOpen){
					t.$open.show();
					var left=t.$open.attr('left');
					t.$open.animate({left:left}, 300);
				}
		    });
			if($.isFunction(p.onClose)){
				p.onClose.call(t,t.$dialog);
			}
		},
		show:function(){
			var t=this,p=this.options;
			$.each(['top','left','height'],function(i,o){
				if(p[o]){
					t.$dialog.css(o,p[o]);
				}
			});
			t.$dialog.width(50);
			if(p.content != null){
				t.$dialog.find('div.ui-float-content').html(p.content);
			}
			if(p.url){
				var param= typeof p.param == "function" ? p.param.call(window):p.param||{};
				t.$dialog.find('div.ui-float-content').html('<span class="loading">加载中...</span>');
				Public.load(p.url,param,function(data){
					t.$dialog.find('div.ui-float-content').html(data);
					if($.isFunction(p.onInit)){
						p.onInit.call(t,t.$dialog);
					}
				});
			}else{
				if($.isFunction(p.onInit)){
					p.onInit.call(t,t.$dialog);
				}
			}
			setTimeout(function(){
				t.$dialog.find('.ui-float-title').text(p.title);
				if ($.browser.msie){
					if(parseInt($.browser.version)<=8){
						t.$dialog.find('>*').show();
						t.$dialog.width(p['width']).show();
						if(p.hideOpen){
							t.$open.hide();
						}
						return;
					}
				}
				t.$dialog.show();
				if(p.hideOpen){
					var left=t.$open.offset().left;
					var width=t.$open.width()+10;
					t.$open.animate({left:width*-1}, 100,function(){
						t.$open.attr('left',left).hide();
					});
				}
				t.$dialog.delay(50).animate({
					width:p['width']
				}, 300,function(){
					t.$dialog.find('>*').show();
				});
				
			},10);
		}
	});

})(jQuery);