/********************************
title:     固定表头插件，当表滚动时表头就固定上面
Author:    xx
Date :     2017-11-19
*********************************/
(function($) {

	$.fn.fixedtable = function (op){
		var obj=this.data('ui_fixed_table');
		if(!obj){
			new fixedTable(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['resize'],function(i,m){
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

	function fixedTable(el,op){
		this.options={};
		this.element=$(el);
		this.set(op);
		this.init();
		this.element.data('ui_fixed_table',this);
	}
	$.extend(fixedTable.prototype,{
		set:function(op){
			this.options=$.extend({
				width: '100%',
	            height: 300,
	            fixedColumns:0, 
	            tableLayout: "auto"
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			g.parentDiv= g.element.parent(); //指向当前table的父级DIV，这个DIV要自己手动加上去
			g.tbody=g.element.find('tbody');
			g.parentDiv.wrap("<div></div>").parent().css({"position": "relative"}); //在当前table的父级DIV上，再加一个DIV
			 //在当前table的父级DIV的前面加一个DIV，此DIV用来包装tabelr的表头
            g.fixedDiv = $("<div style='clear:both;overflow:hidden;z-index:2;position:absolute;'></div>").insertBefore(g.parentDiv);
            g.clone = g.element.clone();
            g.clone.find("tbody").remove(); //复制一份table，并将tbody中的内容删除，这样就仅余thead，所以要求表格的表头要放在thead中
            g.clone.removeClass('table');
            if(!$.browser.msie){
            	g.clone.width(9999);
            }
            g.clone.find("colgroup").remove();
            g.clone.removeAttr('id').appendTo(g.fixedDiv).hide(); //将表头添加到fixedDiv中
            g.element.css({"marginTop": 0,"table-layout": p.tableLayout });
            //当前TABLE的父级DIV有水平滚动条，并水平滚动时，同时滚动包装thead的DIV
            g.parentDiv.scroll(function() {
            	if($(this).scrollTop()>0){
            		g.clone.show();
            	}else{
            		g.clone.hide();
            	}
            	g.fixedDiv.scrollLeft($(this).scrollLeft());
            	if(!$.browser.msie){//ie 执行效率太低
            		g.fixedColumnLeft($(this).scrollLeft());
            	}
            });
            g.setHeadWidth();
            g.fixedColumns();
            $.WinReszier.register($.proxy(g.setHeadWidth, g));
		},
		setHeadWidth:function(){
			var g = this, p = this.options;
			g.parentDiv.css({height:p.height,overflow:'auto'});
            var _position = g.parentDiv.position();
            g.fixedDiv.css({left: _position.left,top: _position.top}).width(g.element.width()+2);
            var _trs = g.element.find("thead>tr");
	        g.clone.find("thead>tr").each(function(i) {
	        	var _tr =$(_trs.get(i)),_td;
	            $(this).find('>th').each(function(j) {
	            	_td=$(_tr.find('>th').get(j));
	            	$(this).css({
	            		'width': _td.outerWidth()+'px',
	     	            'height': _td.outerHeight()+'px',
	     	            'padding-top':parseInt(_td.css('padding-top')),
	                    'padding-left':parseInt(_td.css('padding-left')),
	                    'padding-right':parseInt(_td.css('padding-right')),
	                    'padding-bottom':parseInt(_td.css('padding-bottom')),
	                    'border-top':0
	     	        });
	            });
	       });
		},
		fixedColumns:function(){
			var g = this, p = this.options;
			if(p.fixedColumns > 0){
				g.tbody.find('tr').find('> td:lt(' + p.fixedColumns + ')').each(function(index) {
					$(this).addClass('fixed');
			    });
			}
		},
		fixedColumnLeft:function(left){
			this.tbody.find('td.fixed').css({left:left});
		},
		resize:function(op){
			this.set(op);
			this.setHeadWidth();
		}
	});
	
})(jQuery);