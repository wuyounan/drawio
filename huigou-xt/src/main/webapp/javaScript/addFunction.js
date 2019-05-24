/**首页添加自定义功能 xx 2017-07-10**/
var AddFunction =function(op){
	this._functionIds=[];
	this._scroll_leftSystems_timer=null;
	this.options={};
	this._screenId=0;
	this.isInit=false;
	this.set(op);
	this.init();
};
$.extend(AddFunction.prototype,{
	set:function(op){
		this.options=$.extend({
			onQueryPersonFunctions:false,
			onAddPersonFunction:false,
			onDeleteFunction:false,
			queryPersonFunctionsUrl:web_app.name + "/personDesktop/queryPersonFunctions.ajax",
			queryPersonDesktopFunctionsUrl:web_app.name + "/personDesktop/queryPersonDesktopFunctions.ajax",
			savePersonDesktopScreenAndFunctionsUrl:web_app.name + "/personDesktop/savePersonDesktopScreenAndFunctions.ajax"
		},this.options, op||{});
	},
	init:function(){
		var html=[];
		html.push("<div class='function-dialog col-padding-sm row-fluid ui-hide'>");
		html.push("<div class='col-md-3 col-xs-12'>");
		html.push("<div class='left-systems dom-overflow-auto'></div>");
		html.push("</div>");
		html.push("<div class='col-md-9 col-xs-12'>");
		html.push("<div class='right-functions ui-component-wrap dom-overflow-auto'>请选择功能！</div>");
		html.push("</div>");
		html.push("</div>");
		this.div=$(html.join('')).appendTo('body');
		var _self=this,left=_self.div.find('div.left-systems');
		var right=_self.div.find('div.right-functions');
		left.on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is('i.fa')) $clicked=$clicked.parent();
			if($clicked.is('a')){//点击模块需要更新应用列表区域
				left.find('div.system').removeClass('clicked');
				$clicked.parent().addClass('clicked');
				_self._init_right_functions($clicked.data('id'));
			}
		});
		right.on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is('i.fa')) $clicked=$clicked.parent();
			if($clicked.hasClass('ui-component-text')){//点击模块需要更新应用列表区域
				var mfun=$clicked.parent();
				var obj={
					id:mfun.attr('id'),
					name:mfun.attr('title'),
					functionId:mfun.attr('id'),
					url:mfun.attr('link'),
					icon:mfun.attr('icon')
				}
				_self._add_function(obj);
			}
		});
	},
	show:function(){
		var _this=this,_div=this.div;
		var dialog=Public.dialog({width:700,top:50,title:'快捷方式设置',onClose:function(){
			_div.appendTo('body').addClass('ui-hide');
			_div.find('div.left-systems').find('div.system').removeClass('clicked');
			_div.find('div.right-functions').html('');
		}});
		$('div.ui-public-dialog-content',dialog).append(_div);
		_div.removeClass('ui-hide');
		dialog.css('zIndex',101);
		$('#jquery-screen-over').css('zIndex',100);
		this._init_left_systems();
	},
	_ajax:function(url,param,callBack){
		var _serf=this;this.event=null;this.timer=null;
		$.ajax({type:'POST',url:url,data:param,dataType:'json',
			error:function(){
				alert('网路连接异常,请检查URL连接。');
			},
			success: function(data){
				Public.ajaxCallback(data, callBack);
			}
		});
	},
	_querySystemFunctions:function(parentId, fn){
		var _self=this;
		this._ajax(this.options.queryPersonFunctionsUrl,{ parentId: parentId }, function(data){
			fn.call(_self,data);
		});
	},
	_init_left_systems:function(){
		if(this.isInit) return;
		var _self=this,left=_self.div.find('div.left-systems'),sys=[];
		var icon, title;
		this._querySystemFunctions(1, function(data){
			$.each(data,function(i,o){
				title = o.description ? o.description : o.name;
				icon =  Public.isBlank(o.icon)?"fa-edit":o.icon;
				sys.push('<div href="javascript:void(0);" hidefocus class="system"><a href="javascript:void(0);" data-id="',o.id,'"><i class="fa ',icon,'"></i>&nbsp;',title,'</a></div>');
			});
			left.html(sys.join(''));
			_self.isInit=true;
		});
	},
	_init_right_functions:function(parentId){
		var _self=this,right=_self.div.find('div.right-functions').html('');
		this._querySystemFunctions(parentId,function(data){
			var html=[];
			var icon, title;
			$.each(data,function(x,o){
				if(_self._check_function_exist(o.id)){
					return;
				}
				title = o.description ? o.description : o.name;
				icon =  Public.isBlank(o.icon)?"fa-edit":o.icon;
				html.push('<a class="ui-component-item" href="javascript:void(0);" title="',title,'" id="',o.id,'" link="',o.url,'" icon="',icon,'" code="',o.code,'">');
				html.push('<span class="ui-component-text">');
				html.push('<i class="fa ',icon,'"></i>&nbsp;');
				html.push(title,'</span>');
				html.push('</a>');
			});
			right.html(html.join(''));
		});
	},
	_add_function:function(obj){
		if(this._check_function_exist(obj.id)){
			return;
		}
		this._functionIds.push(obj.id);
		var url=this.options.savePersonDesktopScreenAndFunctionsUrl;
		var _self=this;
		var param={screenId:_self._screenId, functionIds:$.toJSON(_self._functionIds) };
		this._ajax(url,param, function(data){
			_self._screenId=data;
			$("#"+obj.id,_self.div).hide();
			if($.isFunction(_self.options.onAddPersonFunction)){
				_self.options.onAddPersonFunction.call(window,obj);
			}
		});
	},
	deleteFunction:function(id){
		var _self=this;
		$.each(_self._functionIds,function(i,item){  
	        if(item==id){
	        	_self._functionIds.splice(i,1);
		    }
		});
		var url=this.options.savePersonDesktopScreenAndFunctionsUrl;
		var param={screenId:_self._screenId, functionIds:$.toJSON(_self._functionIds) };
		this._ajax(url,param, function(data){
			if($.isFunction(_self.options.onDeleteFunction)){
				_self.options.onDeleteFunction.call(window,id);
			}
		});
	},
	queryPersonFunctions:function(){
		var _self=this;
		this._ajax(this.options.queryPersonDesktopFunctionsUrl,{}, function(data){
			if(data.length > 0){
				_self._screenId=data[0].id;
			}
			$.each(data,function(i,o){
				_self._functionIds.push(o.functionId);
			});
			if($.isFunction(_self.options.onQueryPersonFunctions)){
				_self.options.onQueryPersonFunctions.call(window,data);
			}
		});
	},
	_check_function_exist:function(id){
		var flag=false;
		$.each(this._functionIds,function(i,funId){
			if(funId==id){
				flag=true;
				return false;
			}
		});
		return flag;
	}
});
//默认初始化方法
AddFunction.initAddFunction=function(){
	var parseFuncData=function(data){
		var html = [];
		var icon =  data.icon;
		if(icon==""){
			icon = "fa-edit";
		}
		html.push('<div title="',data.name,'" id="main_fun_',data.functionId,'" class="function-item">');
		html.push('<a href="javascript:void(0);" class="icon-close" funId="',data.functionId,'" title="删除"></a>');//删除
		html.push('<a href="javascript:void(0);" url="',data.url,'" funId="',data.functionId,'" code="',data.code,'" class="function-item-text" >');
		html.push('<i class="fa ',icon,'"></i>&nbsp;');
		html.push('<span>',data.name,'</span>');							
		html.push('</a>');
		html.push('</div>');
		return html.join('');
	};
	
	//addFunction.js
	var addFunction= new AddFunction({
		onQueryPersonFunctions:function(data){
			var html = [];
			$.each(data,function(i,o){
				html.push(parseFuncData(o));
			});
			var div=$('#mainFunctionBox').html(html.join(''));
			div.on('click',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('a.icon-close')){
					var funId=$clicked.attr('funId');
					addFunction.deleteFunction(funId);
					return;
				}
				var a=$clicked.parent('a');
				if(a.hasClass('function-item-text')){
					var funId=a.attr('funId'),url=a.attr('url'),code=a.attr('code');
					url = web_app.name+ "/" +url;
	                if (url.indexOf("?") >= 0){
	                    url += "&functionId=" + funId;
	                }else{
	                    url += "?functionId=" + funId;
	                }
	                UICtrl.addTabItem({tabid:code, text: $.trim(a.text()), url: url});
				}
			});
		},
		onAddPersonFunction:function(data){
			$('#mainFunctionBox').append(parseFuncData(data));
		},
		onDeleteFunction:function(id){
			$('#main_fun_'+id).remove();
		}
	});
	addFunction.queryPersonFunctions();
	$('#mainAddFunction').click(function(e){
		addFunction.show();
		return false;
	});
};