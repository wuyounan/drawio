/********************************
title:      年月选择控件
Author:     xx
Date :      2017-07-19
*********************************/
(function($) {

	$.fn.monthpicker = function (op){
		var obj=this.data('ui-monthpicker');
		if(!obj){
			new Monthpicker(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['enable','disable'],function(i,m){
					if(op==m){
						args = Array.prototype.slice.call(args, 1);
						value=obj[m].apply(obj,args);
						return false;
					}
				});
				return value;
			}else{
				obj.set(op);
			}
		}
		return this;
    };

    function Monthpicker(el,op) {
        this.el = $(el);
        this.set(op);
        this.init();
        this.bindEvent();
        this.disabled=false;
        this.year=(new Date()).getFullYear();
    };

	$.extend(Monthpicker.prototype, {
		set:function(op){
			this.options=$.extend({
				months:['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],
				split:'-',
				topOffset:2,
				triggerEvent : 'click.monthpicker',//显示触发方法
				onMonthSelect:false,
				back:{}
			},this.options, op||{});
		},
        init: function() {
        	var t=this,p=this.options;
            t.id = (new Date()).valueOf();
            t.container = $('<div class="ui-monthpicker" id="monthpicker-' + t.id + '"></div>').appendTo($('body'));
            t.yearWrap=$('<div class="ui-year-wrap"><span class="ui-year-view"></span><span class="ui-year-caret"><b class="caret"></b></span></div>').appendTo(t.container);
            if($.fn.drag){
            	t.container.drag({opacity: 0.8});
            	t.container.css('cursor','move');
			}else{
				t.container.css('cursor','default');
			}
            t.el.data('ui-monthpicker', t);
        },
        bindEvent: function() {
        	var t=this,p=this.options;
            t.el.on(p.triggerEvent, $.proxy(t.show, t));
            if(t.el.parent().hasClass('input-group')){
            	t.el.next('span').on(p.triggerEvent, $.proxy(t.show, t));
            }
            t.container.on('click',function(e) {
            	var $clicked = $(e.target || e.srcElement);
            	if($clicked.is('button')){
            		t.selectMonth.call(t,$clicked);
            		t.close();
            	}
            	e.preventDefault();
                e.stopPropagation();
            });
            t.yearWrap.on('click',function(e) {
            	var years=t.container.find('div.years').show();
            	var selected=years.find('li.selected');
            	if(selected.length>0){
            		var selectedIndex=parseInt(selected.attr('index'));
            		setTimeout(function (){years.scrollTop((selectedIndex-3)*20);},10);
            	}
        		e.stopPropagation();
            });
        },
        selectMonth: function(button) {
        	var t=this,p=this.options;
            var monthIndex = $(button).data('value');
            var month = p.months[monthIndex],year = t.year;
            var value=year + p.split + month;
            if(p.back['year']){
            	$(p.back['year']).val(year);
            }
            if(p.back['month']){
            	$(p.back['month']).val(month);
            }
            if(p.back['value']){
            	$(p.back['value']).val(value);
            }
            if (t.el.is('input')) {
                t.el.val(value);
            }
            if ($.isFunction(p.onMonthSelect)) {
                p.onMonthSelect.call(t, year,month);
            }
        },
        selectYear:function(){
			var t=this,p=this.options,select=t.container.find('div.years');
			if(select.length>0){
				select.removeAllNode();
			}
			select= $('<div class="years"></div>').appendTo(t.container);
			select.bind('mousedown',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('li')){
					t.year=$clicked.attr('id');
	            	t.yearWrap.find('.ui-year-view').html(t.year);
	            	$clicked.addClass('selected').siblings().removeClass('selected');
	            	$(this).hide();
				}
				return false;
			}).bind('mouseleave',function(e){
				$(this).hide();
				return false;
			});
			return select;
		},
        initYears: function() {
        	var t=this,p=this.options,years=[];
			var m = parseInt(t.year);
			if (m < 1000 || m > 9999) {alert("年份值不在 1000 到 9999 之间！");return;}
			var n = m - 50;
			if (n < 1000) n = 1000;
			if (n + 101 > 9999) n = 9974;
            var select=t.selectYear(),option=['<ul>'];
			for (var i = n; i < n + 101; i++){
				option.push('<li id="',i,'" index="',i-n,'" ',(i==m?'class="selected"':''),'>',i,'</li>');
			}
			option.push('<ul>');
			select.html(option.join(''));
            t.yearWrap.find('.ui-year-view').html(m);
        },
        initMonths: function() {
        	var t=this,p=this.options;
        	t.container.find('table').remove();
            var html = ['<table>', '<tr>'];
            $.each(p.months,function(i, month) {
                if (i > 0 && i % 4 === 0) {
                    html.push('</tr>');
                    html.push('<tr>');
                }
                html.push('<td><button data-value="' + i + '">' + month + '</button></td>');
            });
            html.push('</tr>');
            html.push('</table>');
            t.container.append(html.join(''));
        },
        disable: function() {
        	this.disabled = true;
			if(this.el.parent().hasClass('input-group')){
				this.el.parent().addClass('ui-input-group-inline');
			}
			this.el.attr('readonly',true).addClass('ui-text-gray-bg').next('span').hide();
        },
        enable: function() {
			this.disabled = false;
			var optReadonly=this.options.readOnly;
			if(!optReadonly){
				this.el.removeAttr('readonly');
			}else{
				this.el.attr('readonly',true);
			}
			if(this.el.parent().hasClass('input-group')){
				this.el.parent().removeClass('ui-input-group-inline');
			}
			this.el.removeClass('ui-text-gray-bg').next('span').show();
		},
        show: function(e) {
        	var t=this,p=this.options;
        	if(t.disabled){
        		return true;
        	}
            e.preventDefault();
            e.stopPropagation();
            $.closePicker();
            var strYear='',offset = t.el.offset();
            if (t.el.is('input')) {
            	strYear=t.el.val();
            }
            strYear=parseInt(strYear,10);
            if(!isNaN(strYear)){
            	t.year= strYear;
            }
            t.initYears();
            t.initMonths();
            t.container.css({top: offset.top + t.el.outerHeight() + (p.topOffset || 0),left:offset.left}).show();
        },
        close: function() {
            this.container.hide();
        }
    });
	$(document).ready(function() {
		$(document).click(function(e){
			$.closeMonthpicker();
		});
	});
	$.closeMonthpicker=function(){
		$('div.ui-monthpicker').hide();
	};
})(jQuery);