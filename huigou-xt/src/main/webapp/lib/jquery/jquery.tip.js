/********************************
title:      页面提示通知
Author:     xx
Date :      2017-07-27
*********************************/
(function($) {

	$.tips=function(options) {
		return new Tips(options);
	};
	
	$.removeTips=function(){
		$('div.ui-tips').each(function(){
			var obj=$(this).data('ui-tip');
			if(obj){
				obj.remove();
			}
		});
	};
	
    function Tips(options) {
    	this.set(options);
    	this.init();
    }
    
    $.extend(Tips.prototype,{
		set:function(options){
		    this.options = $.extend({
				color : '#001', // 遮罩透明色
	    		alpha : 0, // 遮罩透明度
	    		content : $.i18nProp('common.field.loading'),// 提示语言
	    		autoClose : true,// 是否自动关闭
	    		time : null,// 延迟关闭时间
	    		lock : null,//是否锁屏
	    		type : 3, // 类别
	    		top : 20
		    }, options);
		},
    	init:function() {
    		var _self = this, opts = this.options, time;
    		if(typeof opts.lock !='boolean'){
    			this.options.lock=Public.isBlank(opts.type);
    		}
    		if (opts.autoClose) {
    			if(!opts.time){
    				switch (opts.type) {
    	    		case 0:
    	    			opts.time=3000;
    	    			break;
    	    		case 1:
    	    			opts.time=7000;
    	    			break;
    	    		case 2:
    	    			opts.time=6000;
    	    			break;
    	    		default :
    	    			opts.time=3000;
    	    			break;
    	    		}
    			}
    		}
    		this.create();
    	},
    	create:function() {
    		var opts = this.options,_self=this;
    		if(opts.lock){
    			this._divScreenOver = $('<div class="ui-main-page-loading ui-tips-screen-over"/>').css({
    				background : opts.color,
    				zIndex : 100000,
    				opacity : opts.alpha,
    				filter : "Alpha(Opacity=" + opts.alpha * 100 + ")"
    			}).hide().appendTo('body');
    		}
    		this.obj = $('<div class="ui-tips"><div class="ui-tips-message"></div><button type="button" class="ui-tips-close">&times;</button></div>').appendTo('body');
    		this.messageElement=this.obj.find('.ui-tips-message');
    		this.setType(opts.type);
    		this.obj.find('button').bind('click',function(){
    			_self.remove();
    		});
    		if(opts.autoClose) {
    			this.hideEta = new Date().getTime() + opts.time;
    			this.progressElement= $('<div/>').addClass('ui-tips-progress').appendTo(this.obj);
    			this.obj.hover(function(){
    				_self.hoverTime=opts.time-(_self.hideEta - (new Date().getTime()));
	    			_self.intervals&&clearInterval(_self.intervals);
	    		},function(){
	    			//重新设置时间  减去已过去的时间
	    			_self.hideEta = new Date().getTime()+opts.time-_self.hoverTime;
	    			_self.time();
	    		});
    		}
    		this.time();
    		this.content(opts.content);
    		this.show();
    		this.obj.data('ui-tip',this);
    	},
    	updateProgress:function() {
    		var opts = this.options,_self=this;
    		if(opts.autoClose){
    			var percentage = ((_self.hideEta - (new Date().getTime())) / opts.time) * 100;
    	        this.progressElement.width(percentage + '%');
    		}
        },
    	change:function(op){
    		this.options=$.extend({},this.options, op||{});
    		if(op.content){
    			this.content(op.content);
    		}
    		if(typeof op.type !='undefined'){
    			this.setType(op.type);
    		}
    		this.time();
    		this.removeLock();
    	},
    	content:function(content) {
    		this.messageElement.html(content);
    	},
    	time:function(time){
    		var _self = this,opts = this.options;
    		_self.intervals&&clearInterval(_self.intervals);
    		_self.intervals=setInterval(function(){
    			_self.setPos();
    			if(opts.autoClose){
	    			_self.updateProgress();
	    			if((_self.hideEta - (new Date().getTime())) <=0){
	    				_self.remove();
	    			}
    			}
    		},100);
    	},
    	setType:function(type){
    		var _obj=this.obj;
    		$.each(['success','info','warning','error'],function(i,o){
    			_obj.removeClass('ui-tips-'+o);
    		});
    		_obj.find('i').remove();
    		switch (type) {
    		case 0:
    			_obj.addClass('ui-tips-success');
    			_obj.append('<i class="fa fa-check"></i>');
    			break;
    		case 1:
    			_obj.addClass('ui-tips-error');
    			_obj.append('<i class="fa fa-frown-o"></i>');
    			break;
    		case 2:
    			_obj.addClass('ui-tips-warning');
    			_obj.append('<i class="fa fa-warning"></i>');
    			break;
    		default :
    			_obj.addClass('ui-tips-info');
    			_obj.append('<i class="fa fa-info-circle"></i>');
    			break;
    		}
    	},
    	setPos:function() {
    		this.obj.css({top :this.options.top+$.windowScrollTop()})
    	},
    	show:function(){
    		var _self = this, opts = this.options;
    		_self.showTimes=window.setTimeout(function() {
    			_self.setPos();
    			_self.obj.show();
        		if(opts.lock){
        			_self._divScreenOver.show();
        		}
    		}, 300);
    	},
    	remove:function() {
    		this.intervals&&clearInterval(this.intervals);
    		this.showTimes&&clearTimeout(this.showTimes);
    		this.removeLock();
    		this.obj.fadeOut(200, function() {
    			$(this).removeAllNode();
    		});
    	},
    	removeLock:function(){
    		if(this.options.lock&&this._divScreenOver){
    			this._divScreenOver.remove();
    			this._divScreenOver=null;
    		}
    	}
    });
	
})(jQuery);