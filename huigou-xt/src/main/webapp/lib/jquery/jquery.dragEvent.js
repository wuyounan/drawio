/*---------------------------------------------------------------------------*\
|  title:         UI控件拖动处理类                                                                                                                                                  |
|  Author:        xx                                                          |
|  Created:       2014-02-14                                                  |
|  jquery.combox.js,jquery.datepicker.js 中已引用                                                                                               |
\*---------------------------------------------------------------------------*/
(function($) {
	$.fn.drag = function(options) {
		options = $.extend({
			handle : null,//拖动响应对象
			opacity :false,
			isResize:false,//是否是改变大小
			// callbacks
			drag: null,
			start: null,
			stop: null,
			beforeDrag:null
		}, options || {});
		return this.each(function() {
			var wrap = this, handle = options.handle||false;
			if(!(handle instanceof jQuery)){
				handle=handle? $(options.handle, this): $(this);
			}
			options = $.extend(options,{wrap:wrap,handle:handle});
			handle.bind('mousedown', function(event) {
				_initDrag(event,options);
			}).css('cursor','move');
		});
	};
	
	//绑定普通拖动功能
	function _initDrag(event,options) {
		var limit={}, startWidth=0, startHeight=0, startLeft=0, startTop=0,isResize=options.isResize;
		var _$w = $(window), _doc = document, _root = _doc.documentElement;
		var _isSetCapture = 'setCapture' in _root, _isLosecapture = 'onlosecapture' in _root;
		var _dragEvent = {},wrap=options.wrap,handle=options.handle;
		// 对话框准备拖动
		_dragEvent.onStart = function(x, y) {
			if(isResize){
				startWidth = wrap.offsetWidth;
				startHeight = wrap.offsetHeight;
			}else{
				startLeft = wrap.offsetLeft;
				startTop = wrap.offsetTop;
				_isSetCapture && handle[0].setCapture();
			}
			_isLosecapture && handle.bind('losecapture.dragEvent',function(_self){ 
				return function(e){_self.end(e);};
			}(this));
			if(options.opacity){
				$(wrap).css("opacity", options.opacity);
			}
			if($.isFunction(options.start)){
				options.start(x,y);
			}
		};
		// 对话框拖动进行中
		_dragEvent.onMove = function(x, y) {
			try {_doc.selection.empty();} catch (e) {}
			if($.isFunction(options.beforeDrag)){
				if(options.beforeDrag(x,y)===false){
					return;
				}
			}
			if( isResize ){
				var style = wrap.style,
					width = x + startWidth,
					height = y + startHeight;
				style.width = Math.max(0,width) + 'px';
				style.height = Math.max(0,height) + 'px';
			}else{
				var style = wrap.style, left = x + startLeft, top = y + startTop;
				left = Math.max(limit.minX, Math.min(limit.maxX, left)),
				top = Math.max(limit.minY, Math.min(limit.maxY, top));
				style.left = left + 'px';
				style.top = top + 'px';
				
			}
			if($.isFunction(options.drag)){
				options.drag(x,y);
			}
		};
		// 对话框拖动结束
		_dragEvent.onEnd = function(x, y) {
			_isLosecapture && handle.unbind('losecapture.dragEvent');
			_isSetCapture && handle[0].releaseCapture();
			if(options.opacity){
				$(wrap).css("opacity",1);
			}
			if($.isFunction(options.stop)){
				options.stop(x,y);
			}
		};
		limit = (function(fixed) {
			var ow = wrap.offsetWidth,
			// 向下拖动时不能将标题栏拖出可视区域
			oh = handle[0].offsetHeight || 20,
			ww = _$w.width(),
			wh = _$w.height(),
			dl = fixed ? 0 : _$w.scrollLeft(),
			dt = fixed ? 0 : _$w.scrollTop();
			// 坐标最大值限制(在可视区域内)
			maxX = ww - ow + dl;
			maxY = wh - oh + dt;
			return {
				minX : dl,
				minY : dt,
				maxX : maxX,
				maxY : maxY
			};
		})(wrap.style.position === 'fixed');
		new dragEvent(event,_dragEvent);
	};
	
	function dragEvent(event, op) {
		this.clientX = null;
		this.clientY = null;
		this.options = {};
		this.set(op);
		this.start(event);
	}
	$.extend(dragEvent.prototype, {
		set : function(op) {
			this.options = $.extend({
				onStart : function() {},
				onMove : function() {},
				onEnd : function() {}
			}, this.options, op || {});
		},
		start : function(event) {
			var _self=this;
			$(document).bind('mousemove.dragEvent',function(e){
				_self.move(e);
			}).bind('mouseup.dragEvent',function(e){
				_self.end(e);
			});
			this.clientX = event.clientX;
			this.clientY = event.clientY;
			this.options.onStart.call(this, event.clientX, event.clientY);
			return false;
		},
		move : function(event) {
			this.options.onMove.call(this, event.clientX - this.clientX,event.clientY - this.clientY);
			return false;
		},
		end : function(event) {
			$(document).unbind('mousemove.dragEvent').unbind('mouseup.dragEvent');
			this.options.onEnd.call(this, event.clientX, event.clientY);
			return false;
		}
	});
})(jQuery);