/*********************************************************
title:     textarea 控制功能辅助 
Author:  xx    
使用:   $('textarea').maxLength();
**********************************************************/
(function ($) {
	
	/************maxlenth属性****************/
	$.fn.maxLength = function (settings) {
	    if (typeof settings == 'string') {
	        settings = { feedback : settings };
	    }
	    settings = $.extend({}, $.fn.maxLength.defaults, settings);
	    return this.each(function () {
	        var field = this,
	        	$field = $(field),
	        	limit = settings.maxlength ? settings.maxlength : $field.attr('maxlength'),
	            limit=parseInt(limit,10),
	        	$charsLeft =null;
			if(isNaN(limit))
				return;
			if(settings.feedback!=false){
				$charsLeft=$(settings.feedback);
			}
	    	function limitCheck(event) {
	        	var len = field.value.length,
	            keycode = event.keyCode;
				if(keycode!=13&&keycode!=8&&keycode!=46&&keycode!=37&&keycode!=38&&keycode!=39&&keycode!=40){
					if(len>limit-1){
						event.preventDefault();
						event.stopPropagation();
						return false;
					}
				}
	        }
	        var updateCount = function () {
	            var len = field.value.length,
	            diff = limit - len;
				if($charsLeft)
					$charsLeft.html( diff || "0" );
	            if (diff < 0) {
	            	field.value = field.value.substr(0, limit);
	                updateCount();
	            }
	        };
	
	        $field.keyup(updateCount).change(updateCount);
	        $field.keydown(limitCheck);
	        updateCount();
	    });
	};

	$.fn.maxLength.defaults = {
		maxlength : false,
		feedback : false
	};

	/***************textarea 自动适应大小******************/
    $.fn.textareaOnAutoHeight = function (options) {
        this._options = {
            minHeight: 0,
            maxHeight: 1000
        }
        this.init = function () {
            for (var p in options) {
                this._options[p] = options[p];
            }
            if (this._options.minHeight == 0) {
                this._options.minHeight = parseFloat($(this).height());
            }
            for (var p in this._options) {
                if ($(this).attr(p) == null) {
                    $(this).attr(p, this._options[p]);
                }
            }
            $(this).keyup(this.resetHeight).change(this.resetHeight).focus(this.resetHeight);
        }
        this.resetHeight = function () {
            var _minHeight = parseFloat($(this).attr("minHeight"));
            var _maxHeight = parseFloat($(this).attr("maxHeight"));
            if (!$.browser.msie) {
                $(this).height(0);
            }
            var h = parseFloat(this.scrollHeight);
            h = h < _minHeight ? _minHeight : h > _maxHeight ? _maxHeight : h;
            $(this).height(h).scrollTop(h);
            if (h >= _maxHeight) {
                $(this).css("overflow-y", "scroll");
            } else {
                $(this).css("overflow-y", "hidden");
            }
        }
        this.init();
    };

    $.fn.textareaAutoHeight = function (options) {
        return this.each(function () {
            var id = $(this).attr('id');
            this._options = {minHeight: 0, maxHeight: 1000};
            for (var p in options) {
                this._options[p] = options[p];
            }
            var _minHeight = this._options.minHeight;
            var _maxHeight = this._options.maxHeight;
            if (_minHeight == 0) {
                _minHeight = parseFloat($(this).height());
            }
            var po = parseInt($(this).css('padding-top')) + parseInt($(this).css('padding-bottom'));
            var h = $(this)[0].scrollHeight;
            h = isNaN(h) ? 0 : h;
            h = h < _minHeight ? _minHeight : h > _maxHeight ? _maxHeight : h;
            $(this).height(h + po).scrollTop(h + po);
            if (h >= _maxHeight) {
                $(this).css("overflow-y", "scroll");
            } else {
                $(this).css("overflow-y", "hidden");
            }
            $(this).textareaLabelHeight();
        });
    };
    
    /***************修改textarea对应label高度******************/
    $.fn.textareaLabelHeight = function () {
        return this.each(function () {
            var id = $(this).attr('id'),_height=$(this).height();
            var parent=$(this).parent();
            if(parent.hasClass('col-warp')){
            	parent.height(_height);
	            var _labelDom=$(this).parent().prev();
	            var _label=$('#'+id+'_label',_labelDom);
	            if(_label.length>0){
	            	_label.parent().height(_height);
	            }
            }
        });
    };


})(jQuery);