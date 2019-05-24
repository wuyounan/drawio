/********************************
title:      token_field
Author:     xx
*********************************/
(function($) {
	
	$.fn.tokenfield = function (op){
		var obj=this.data('ui_token_field');
		if(!obj){
			new tokenField(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['addData','removeData','getData','getDataIds','clean'],function(i,m){
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

    function tokenField(el,op){
		this.options={};
		this.data=[];
		this.element=$(el);
		this.set(op);
		this.init();
		this.bindEvent();
		this.element.data('ui_token_field',this);
	}
    
	$.extend(tokenField.prototype,{
		set:function(op){
			this.options=$.extend({
				data:[],
				idFieldName:'id',
				nameFieldName:'name',
				titleFieldName:'fullName',
				wrapClassName:'ui-token-field-wrap',
				itemClassName:'ui-token-field-item',
				closeClassName:'icon-close',
				closeHtml:'<i class="icon-close"></i>',
				onClick:false
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			this.element.addClass(p.wrapClassName);
			g.element.find('a.'+p.itemClassName).each(function(){
				var _o={};
				_o[p.idFieldName]=$(this).data['id'];
				_o[p.nameFieldName]=$(this).data['name']||'';
				_o[p.titleFieldName]=$(this).attr['title'];
				g.data.push(_o);
			});
			g.addData();
		},
		bindEvent:function(){
			var g = this, p = this.options;
			g.element.on('click',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.hasClass(p.closeClassName)){
					g._removeById([$clicked.parent().data('id')]);
				}
				if($.isFunction(p.onClick)){
					p.onClick.call(g,$clicked);
				}
			});
		},
		_inArray:function(o){
			var g = this, p = this.options;
			var _id=o[p.idFieldName];
			if(!_id) return true;
			var _flag = false;
			$.each(g.data,function(i,d){
				if(_id==d[p.idFieldName]){
					_flag=true;
					return false;
				}
			});
			return _flag;
		},
		addData:function(_data){
			var g = this, p = this.options;
			_data = _data || p.data;
			if(!$.isArray(_data)) return;
			if(!_data.length) return;
			var html=[];
			$.each(_data,function(i,o){
				//已存在则不添加
				if(g._inArray(o)){
					return true;
				}
				html.push('<a class="',p.itemClassName,'" href="javascript:void(0);" data-id="',o[p.idFieldName],'" data-name="',o[p.nameFieldName]||'','">');
				html.push('<span class="ui-component-text" title="',o[p.titleFieldName]||'','">',o[p.nameFieldName]||'','</span>');
				html.push(p.closeHtml);
				html.push('</a>');
				//添加到数据中
				g.data.push(o);
			});
			g.element.append(html.join(''));
		},
		_removeDom:function(_id){
			var g = this, p = this.options;
			g.element.find('a.'+p.itemClassName).each(function(){
				var domId=$(this).data('id');
				if(domId==_id){
					$(this).remove();
					return false;
				}
			});
		},
		_removeById:function(ids){
			var g = this, p = this.options;
			g.data=$.map(g.data,function(o){
				if($.inArray(o[p.idFieldName],ids)>-1){
					g._removeDom(o[p.idFieldName]);
					return null;
				}
				return o
			});
		},
		removeData:function(_data){
			var g = this, p = this.options;
			if(!_data) return;
			if (typeof _data == "string"){
				g._removeById(_data.split(','));
			}else if(typeof _data == "object"&&_data[p.idFieldName]){
				g._removeById([_data[p.idFieldName]]);
			}else if($.isArray(_data)&&_data.length > 0){
				if (typeof _data[0] == "string"){
					g._removeById(_data);
				}if(typeof _data[0] == "object"&&_data[p.idFieldName]){
					var _ids=$.map(_data,function(o){
						return o[p.idFieldName];
					});
					g._removeById(_ids);
				}
			}
		},
		clean:function(){
			var g = this, p = this.options;
			g.data=[];
			g.element.empty();
		},
		getData:function(){
			return this.data;
		},
		getDataIds:function(){
			var g = this, p = this.options;
			return $.map(g.data,function(o){
				return o[p.idFieldName];
			});
		}
	});

	
})(jQuery);