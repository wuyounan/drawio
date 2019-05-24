var UICtrl = UICtrl || {};
UICtrl.convertForPhone= false;
UICtrl.entranceKind = "PC";// 入口类别
UICtrl.ProcessCommand = {};
UICtrl.ProcessCommand.FINISHED = false;// 流程命令结束
//UI 权限类别
var UIPO={READONLY:'readonly',NOACCESS:'noaccess',READWRITE:'readwrite'};
//type 0：主集 ，1：子集 ，2：按钮
var UIPK={FIELD:'0',DETAIL:'1',BUTTON:'2'};

$(document).ready(function() {
	// 判断请求是否来至手机
	UICtrl.convertForPhone=Public.getQueryStringByName("convertForPhone")==='true';
	if(!UICtrl.convertForPhone){
		UICtrl.convertForPhone=$.isMobile();
	} 
	setTimeout(function(){ 
		if(Public.isReadOnly){// 只读模式下修改输入框为只读
			UICtrl.setDisable($('body'));
		}else if(!UICtrl.isApplyProcUnit()){// 不是申请环节默认为只读
			var jobBizBillBody=$('#jobBizBillBody');
			if(jobBizBillBody.length>0){
				UICtrl.setDisable(jobBizBillBody);
				$('div.job-task-execution',jobBizBillBody).each(function(){
			    	UICtrl.setEditable($(this));
			    });
				var fileListDiv=$('div.ui-attachment-list',jobBizBillBody);
				if($.fn.fileList&&fileListDiv.length>0){
					fileListDiv.each(function(){
						//ui-not-allow-disable 不允许禁用
						if(!$(this).hasClass('ui-not-allow-disable')){
							$(this).fileList('disable');
						}
					});
				}
			}
		}
		UICtrl.useUIElementPermission($('body'));
	},10);
	UICtrl.initKeyDownQueryGrid();// 查询界面回车执行查询
	UICtrl.setDialogPath();
	//页面不再框架内
	if(self == top){
		//创建顶部用户岗位切换按钮
		UICtrl.showFloatTopMenu();
	}
});

//查询界面回车执行查询
UICtrl.initKeyDownQueryGrid=function(){
	var form=$('#queryMainForm');
	if($.isFunction(window['query'])&&form.length>0){
		form.bind('keyup.queryForm',function(e){
			if (e.ctrlKey) {
				return;
			}
			var k =e.charCode||e.keyCode||e.which;
			var $el = $(e.target || e.srcElement);
			if(k==13&& !$el.is('textarea')){// 回车
				window['query'](form);
			}
		});
	}
};

// 处理主表上权限字段
UICtrl.useUIElementPermission=function($doc){
	var permission=window['UIElementPermission']||{},obj=null;
	$.each(permission,function(p,o){
		if(o.kindId==UIPK.FIELD){// 主集字段
			obj=$("[name='"+p+"']",$doc);
			if(obj.length>0){//存在字段
				if(o.operationId==UIPO.READONLY){// 只读权限
					UICtrl.disable(obj);
				}else if(o.operationId==UIPO.NOACCESS){// 无权限
					obj.attr(UIPO.N,true);// 添加该标志后在$(form).formToJSON()方法中将不会取值
					if(obj.is('select')){
						$('#'+p+'_text').val('********');
					}
					UICtrl.disable(obj);
				}else if(o.operationId==UIPO.READWRITE){// 存在读写权限
					if(!Public.isReadOnly){
						UICtrl.enable(obj);
						if(o.required){
							UICtrl.setElRequiredFlag(obj,true);
						}
					}
				}
			}else{
				obj=$('#'+p,$doc);// 页面查找对应元素
				if(obj.length>0&&o.operationId==UIPO.NOACCESS){// 无权限
					obj.hide();
				}
			}
		}
	});
};

// 获取不同类别权限字段
// kindId 0：主集 ，1：子集 ，2：按钮 {FIELD:'0',DETAIL:'1',BUTTON:'2'}
UICtrl.getUIElementPermissions = function(operationId,kindId){
	var result=[],permission=window['UIElementPermission']||{};
	$.each(permission,function(p,o){
		if(o.operationId==operationId&&o.kindId==kindId){
			result.push(p);
		}
	});
	return result;
};

//设置UI权限信息
UICtrl.setUIElementPermissions = function(permissions){
	var _UIPermissions=window['UIElementPermission']||{};
	if($.isArray(permissions)){
		$.each(permissions,function(i,p){
			_UIPermissions[p.id]={operationId:p.operation,kindId:p.kind,required:p.required};
		});
	}
	if($.isPlainObject(permissions)){
		$.each(permissions,function(k,o){
			_UIPermissions[k]={operationId:o.operation,kindId:o.kind,required:o.required};
		});
	}
	window['UIElementPermission']=_UIPermissions;
};

// 判断当前页面是否为申请环节
UICtrl.isApplyProcUnit=function(){
	//本地校验方法
	if($.isFunction(window['businessJudgmentUnit'])){
		return window['businessJudgmentUnit']();
	}
	//只读 返回不可编辑
	if(Public.isReadOnly){
		return false;
	}
	// window['isApplyProcUnit'] 存在于 job.js
	if($.isFunction(window['isApplyProcUnit'])){
		return window['isApplyProcUnit']();
	}
	return true;
};
// 判断按钮是否有权限 retrun true 无权限
// gridId 在grid的toolbar 中确定按钮权限时使用
UICtrl.checkButtonNoAccess=function(id,gridId){
	if(Public.isReadOnly){// 只读模式
		return Public.testReadOnlyAttributes(id);
	}
	// gird中的按钮需要加上gridId
	if(!Public.isBlank(gridId)){
		id=gridId+'.'+id;
	}
	if(UICtrl.isApplyProcUnit()){// 非审批环节
		// 无权限的按钮
		var noAccessList=UICtrl.getUIElementPermissions(UIPO.NOACCESS,UIPK.BUTTON);
		if($.inArray(id,noAccessList)>-1){// 该按钮无权限
			return true;
		}
	}else{// 审核环节
		if(Public.testReadOnlyAttributes(id)){// 需要判断的按钮
			// 有权限的按钮
			var readwriteList=UICtrl.getUIElementPermissions(UIPO.READWRITE,UIPK.BUTTON);
			if($.inArray(id,readwriteList)>-1){// 该按钮有权限
				return false;
			}
			return true;
		}
	}
	return false;
};
// 处理明细表权限字段
UICtrl.disposeGridPermissionField=function(options,gridId){
	options=options||{};
	// 取子集中只读字段
	var readOnlyList=UICtrl.getUIElementPermissions(UIPO.READONLY,UIPK.DETAIL);
	// 取子集中无访问权限字段
	var noAccessList=UICtrl.getUIElementPermissions(UIPO.NOACCESS,UIPK.DETAIL);
	// 取子集中有读写权限字段
	var readwriteList=UICtrl.getUIElementPermissions(UIPO.READWRITE,UIPK.DETAIL);
	var columns=options['columns'];
	if (options.enabledEdit === true) {
		if(Public.isReadOnly){// 只读模式不添加事件
			options.enabledEdit=false;
		}else{
			var hasEditorField=false;
			if(UICtrl.isApplyProcUnit()){// 申请环节
				hasEditorField=options.enabledEdit;
				// 在申请页面控制只读数据
				if(readOnlyList.length > 0){
					// 删除字段上的可写配置
					$.each(columns,function(i,o){
						if($.inArray(o.name,readOnlyList)>-1){// 该字段只读
							delete o['editor'];// 删除editor
						}
						if($.inArray(gridId+'.'+o.name,readOnlyList)>-1){// 该字段只读
							delete o['editor'];// 删除editor
						}
						if(o['editor']){// 存在可写字段
							hasEditorField=true;
						}
					});
				}
			}else{// 审核环节
				// 在审核页面控制可写字段
				if(readwriteList.length > 0){
					$.each(columns,function(i,o){
						if($.inArray(o.name,readwriteList)==-1){// 不在可写列表中
							if($.inArray(gridId+'.'+o.name,readwriteList)==-1){// 不在可写列表中
								delete o['editor'];// 删除editor
							}
						}
						if(o['editor']){// 存在可写字段
							hasEditorField=true;
						}
					});
				}
			}
			if(hasEditorField){// 存在可写字段
				// 添加位置寻找方法
				var fn=options['onBeforeEdit'];
			    options['onBeforeEdit'] =function(a,b){
			    	if($.isFunction(fn)){
			    		var flag=fn.call(this,a);
			    		if(flag===false) return false;
			    	}
			    	GridEditorsUtil.beforeEdit.call(this,a,b);
			    };
			}else{
				options.enabledEdit=false;
			}
		}
	}
	if(noAccessList.length>0){// 无权限字段需在表头中删除
		var newColumns=[];
		$.each(columns,function(i,o){
			// 判读是否在数组中
			if($.inArray(o.name,noAccessList)==-1){
				if($.inArray(gridId+'.'+o.name,noAccessList)==-1){
					newColumns.push(o);
				}
			}
		});
		options['columns']=newColumns;
		/*
		 * 暂时不处理后台返回数据 //添加无权限查询执行参数 var parms = options['parms']||{};
		 * parms['permissionNoAccessList'] = noAccessList.join(',');
		 * options['parms']=parms;
		 */
	}
	//处理表头字段国际化名称 add 2017-09-07
	var columnsDisplayi18n=function(columns){
		$.each(columns,function(i,o){
			o['display']=$.i18nProp(o.display||'');
			if($.isArray(o['columns'])){
				columnsDisplayi18n(o['columns']);
			}
		});
	};
	if(options['columns']){
		columnsDisplayi18n(options['columns']);
	}
	// 处理按钮
	var toolbar=options['toolbar'];
	// 存在按钮
	if(!Public.isBlank(toolbar)&&!Public.isBlank(toolbar['items'])&&toolbar['items'].length>0){
		var newToolBar=[],itemId=null;
		$.each(toolbar['items'],function(i,o){
			if(!o){
				return;
			}
			itemId=Public.isBlank(o['itemId'])?o['id']:o['itemId'];
			if(Public.isBlank(itemId)){
				return;
			}
			// 按钮权限判断
			if(!UICtrl.checkButtonNoAccess(itemId,gridId)){
				newToolBar.push(o);
			}
		});
		options['toolbar']['items']=newToolBar;
		if(newToolBar.length==0){
			delete options['toolbar'];
		}else{
			options['toolbar']={items:newToolBar};
		}
	}
	return options;
};
// 设置为不可编辑
UICtrl.setDisable=function($doc){
	$('textarea',$doc).each(function(i,o){
		UICtrl.disable(o);
	});
	$('input[type="text"]',$doc).each(function(i,o){
		UICtrl.disable(o);
	});
	$('input[type="radio"]',$doc).each(function(i,o){
		$(this).attr('disabled',true);
	});
	$('input[type="checkbox"]',$doc).each(function(i,o){
		$(this).attr('disabled',true);
	});
};
// 设置为可编辑
UICtrl.setEditable=function($doc){
	$('textarea',$doc).each(function(i,o){
		UICtrl.enable(o);
	});
	$('input[type="text"]',$doc).each(function(i,o){
		UICtrl.enable(o);
	});
	$('input[type="radio"]',$doc).each(function(i,o){
		$(this).removeAttr('disabled');
	});
	$('input[type="checkbox"]',$doc).each(function(i,o){
		$(this).removeAttr('disabled');
	});
};

UICtrl.enable=function(input) {
	var $input=$(input),_id=$input.attr('id');
	if($input.is('[type="hidden"]')){
    	return;
    }
	var obj=$input.data('ui-combox-obj');
	if(obj){
		obj.enable();
	}
	obj=$input.data('spinner');
	if(obj){
		obj.enable();
	}
	obj=$input.data('ui-monthpicker');
	if(obj){
		obj.enable();
	}
	if($input.hasClass("ui-datepicker-disable")||$input.parent().hasClass("ui-datepicker-disable")){
		$input.datepickerEnable();
	}
	var parentCol=null;
	if($input.parent().is('div.col-gray-bg')){
		parentCol=$input.parent().removeClass('col-gray-bg').addClass('col-white-bg');
	}else if($input.parent().parent().is('div.col-gray-bg')){
		parentCol=$input.parent().parent().removeClass('col-gray-bg').addClass('col-white-bg');
	}
	if($input.parent().hasClass('input-group')){
		$input.next('span').show();
		$input.removeClass('ui-text-gray-bg');
	}
	$input.removeAttr('readonly');
	if($input.isRequired()&&parentCol){
		parentCol.prev().find('.hg-form-label').addClass('required');
	}
};

UICtrl.disable=function(input) {
	var $input=$(input),_id=$input.attr('id');
	if($input.is('[type="hidden"]')){
    	return;
    }
	if($input.hasClass('ui-grid-query-input')){
		return;
	}
	if($input.is('textarea')){
		if($.isMobile()&&!$input.parents('div.ui_content').length){
			$input.textareaAutoHeight();
		}
	}
	var obj=$input.data('ui-combox-obj');
	if(obj){
		obj.disable();
	}
	obj=$input.data('spinner');
	if(obj){
		obj.disable();
	}
	obj=$input.data('ui-monthpicker');
	if(obj){
		obj.disable();
	}
	if($input.hasClass("ui-datepicker")||$input.parent().hasClass("ui-datepicker")){
		$input.datepickerDisabled();
	}
	var parentCol=null;
	if($input.parent().is('div.col-white-bg')){
		parentCol=$input.parent().removeClass('col-white-bg').addClass('col-gray-bg');
	}else if($input.parent().parent().is('div.col-white-bg')){
		parentCol=$input.parent().parent().removeClass('col-white-bg').addClass('col-gray-bg');
	}
	if($input.parent().hasClass('input-group')){
		$input.parent().addClass('ui-input-group-inline');
		$input.next('span').hide();
		$input.addClass('ui-text-gray-bg');
	}
	$input.attr('readonly',true);
	if($input.isRequired()&&parentCol){
		parentCol.prev().find('.hg-form-label').removeClass('required');
	}
};

// 图片等比例缩放
UICtrl.autoResizeImage=function(obj,maxWidth, maxHeight) {
   var img = new Image();// 创建对象
   img.src = $(obj).attr('src');// 改变图片的src
   var isLoad=false;
   var check = function(){
		if(img.width>0 || img.height>0){// 只要任何一方大于0,表示已经服务器已经返回宽高
			setTimeout(showImg,0);
		 }
	};
	var timer = setInterval(check,40);// 定时执行获取宽高
	img.onload = function(){
		setTimeout(showImg,0);
	};
	img.onerror = function(){// 图片不存在时显示默认
		clearInterval(timer);
		isLoad=true;
	};
	var showImg=function(){// 显示区域大小
		if(isLoad) return;
		var nw=img.width,nh=img.height;
		var hRatio=maxHeight/nh,wRatio=maxWidth/nw,ratio=1;
		if(isNaN(hRatio)) hRatio=1;
		if(isNaN(wRatio)) wRatio=1;
		if (wRatio<1 || hRatio<1){
			ratio = (wRatio<=hRatio?wRatio:hRatio);
		}
		if (ratio<1){
			nw = nw * ratio;
			nh = nh * ratio;
		}
		$(obj).css({width:nw,height:nh});
		clearInterval(timer);
		isLoad=true;
	};
};

/** *******扩展$.dialog窗口外部方法********* */
UICtrl.setDialogPath=function(path){
	if ($.dialog) {
		path=path||(web_app.name + '/themes/lhgdialog/');
		$.dialog.setting.path=path;
	}
};

UICtrl.notice = function( options ){
	if(typeof options=='string'){
		options={content:options};
	}
	var opts = options || {},
		api, aConfig, hide, wrap, top,
		duration = opts.duration || 800;
	var event={
		init: function(here){
			api = this;
			aConfig = api.config;
			wrap = api.DOM.wrap;
			top = parseInt(wrap[0].style.top);
			hide = top + wrap[0].offsetHeight;
							
			wrap.css('top', hide + 'px')
			.animate({top: top + 'px'}, duration, function(){
				opts.init && opts.init.call(api, here);
			});
		},
		close: function(here){
			wrap.animate({top: hide + 'px'}, duration, function(){
				opts.close && opts.close.call(this, here);
				aConfig.close = $.noop;
				api.close();
			});
			return false;
		}
	};   
	var config=$.extend({
		title:'系统消息',
		left: '99%',
		top: '100%',
		width: 220, 
		time: 5,
		max: false,
		min: false,
		fixed: true,
		drag: false,
		resize: false
	},opts,event);
	var dialog=UICtrl.getDialog(false);
	return dialog( config );
};
// 对话框大小调整 适应小屏幕显示
UICtrl.computeDialogOptions=function(options){
	var opts = options || {};
	var pageSize=UICtrl.getPageSize();
	var _width=parseFloat(opts['width']);
	var _height=parseFloat(opts['height']);
	if($.isMobile()){
		opts['top']=10;
	}
	if(!isNaN(_width)){
		if(_width > (pageSize.w-20)){
			opts['width']=pageSize.w-35;
			opts['top']=0;
		}
	}
	if(!isNaN(_height)){
		//对话框默认会有80的高度差
		if(_height > (pageSize.h-80)){
			opts['height']=pageSize.h-100;
		}
	}
	return opts;
};

UICtrl.getDialog = function (needParent){
	var dialog = $.dialog;
	if(needParent){
		try {
	        dialog = parent.$.dialog;
	        if (!dialog) {
	            dialog = $.dialog;
	        }
	    } catch (e) {
	        dialog = $.dialog;
	    }
	}
    return dialog;
};
/***********对话框加载的页面****************** */
UICtrl.showDialog = function( options ){
	var opts = UICtrl.computeDialogOptions(options);
	var event={
		close: function(){
			var $content = this.DOM.content,returnValue;
			if($.isFunction(opts['close'])){
				returnValue = opts['close'].call(this, $content);
			}
			if(returnValue===false){
				return false;
			}else{
				$('body').removeClass('ui-dialog-overflow-hidden');
			}
			return true;
		},
		init: function(){
			$('body').addClass('ui-dialog-overflow-hidden');
			var $content=this.DOM.content;
			Public.autoInitializeUI($content);// 自动初始化控件
			if($.isFunction(opts['init'])){
				opts['init'].call(this,$content);
			}
		},
		ok:opts['ok']===false?false:function () {
			var $content=this.DOM.content,returnValue;
			if($.isFunction(opts['ok'])){
				returnValue=opts['ok'].call(this,$content);
			}
			return returnValue===true?true:false;
		},
		cancel:opts['close']===false?false:function(){
			$.closePicker();
			return true;
		}
	};
	var config = $.extend({
		lock: true,
		content:'',
		min:false,
		max:false,
		width:getDefaultDialogWidth(),
		resize: false,
		height:'auto'
	},opts,event);
	var dialog=UICtrl.getDialog(false);
	return dialog( config ).focus();
};

UICtrl.showAjaxDialog = function( options ){
	var opts = UICtrl.computeDialogOptions(options);
	var url=opts['url'],param=opts['param']||{},id=opts.id;
	var event={
		close: function(){
			var $content=this.DOM.content,returnValue;
			if($.isFunction(opts['close'])){
				returnValue=opts['close'].call(this,$content);
			}
			if(returnValue===false){
				return false;
			}else{
				$('body').removeClass('ui-dialog-overflow-hidden');
			}
			return true;
		},
		init: function(){
			$('body').addClass('ui-dialog-overflow-hidden');
			var $content=this.DOM.content;
			// 自动初始化控件
			Public.autoInitializeUI($content);
			// 应用界面元素权限
			UICtrl.useUIElementPermission($content);
			if($.isFunction(opts['init'])){
				opts['init'].call(this, $content);
			}			
		},
		ok:opts['ok']===false?false:function () {
			var $content=this.DOM.content,returnValue;
			if($.isFunction(opts['ok'])){
				returnValue=opts['ok'].call(this,$content);
			}
			return returnValue===true?true:false;
		},
		cancel:opts['close']===false?false:function(){
			$.closePicker();
			return true;
		}
	};
	var config = $.extend({
		lock: true,
		cancelVal: 'common.button.close',
		okVal:'common.button.save',
		min:false,
		max:false,
		resize: false,
		width:getDefaultDialogWidth(),
		height:'auto'
	},opts,event);
	var dialog=UICtrl.getDialog(false);
	Public.load(url, param, function(data){
		config['content'] = data;
		setTimeout(function(){window[ id ? id : 'ajaxDialog' ] = dialog( config ).focus();},0);
	});
};
/** **********通过IFRAME加载页面************* */
UICtrl.showFrameDialog = function(options){
	var opts = UICtrl.computeDialogOptions(options);
	var url=opts['url'],param=opts['param'];
	if(!url) return;
	if(param){
		url+=(/\?/.test(url) ? '&' : '?')+ $.param(param);
	}
	var event={
		close: function(){
			$('body').removeClass('ui-dialog-overflow-hidden');
			if($.isFunction(opts['close'])){
				opts['close'].apply(this,arguments);
		    }
		},
		init: function(){
			$('body').addClass('ui-dialog-overflow-hidden');
			if($.isFunction(opts['init'])){
				opts['init'].apply(this,arguments);
			}
		},
		ok:opts['ok']==false?false:function () {
			var returnValue;
			if($.isFunction(opts['ok'])){
				returnValue = opts['ok'].call(this);
			}
			return returnValue === true ? true : false;
		},
		cancel:!opts['cancel']?undefined:true
	};
	var config = $.extend({
		content: 'url:'+url,
		width:'auto',
		height:'auto',
		min:false,
		max:false,
		resize: false,
		lock: true
		},opts,event);
	var dialog=UICtrl.getDialog(false);
	return dialog( config );
};
UICtrl.getText = function(msg){
	var span=$('<span></span>').html(msg);
	var text=$.trim(span.text());
	span.remove();
	return text;
};
UICtrl.alert = function (msg, fn){
	msg=$.i18nProp(msg);
	if(UICtrl.convertForPhone){
		msg=UICtrl.getText(msg);
		window.alert(msg);
		if($.isFunction(fn)){
			fn.call(window);
		}
	}else{
		var dialog=UICtrl.getDialog(true);
		dialog.alert('<div style="padding-top:20px;">'+msg+'</div>',function(){
		   if($.isFunction(fn)){
			   fn.call(window);
		   }
		});
	}
};

UICtrl.confirm = function (msg,ok,cancel){
	msg=$.i18nProp(msg);
	if(UICtrl.convertForPhone){
		msg=UICtrl.getText(msg);
		if(window.confirm(msg)){
			if($.isFunction(ok)){
				ok.call(window);
			}
		}else{
			if($.isFunction(cancel)){
				cancel.call(window);
			}
		}
	}else{
		var dialog=UICtrl.getDialog(true);
	    dialog.confirm('<div style="padding-top:20px;">'+msg+'</div>', function(){
			if($.isFunction(ok)){
				ok.call(window);
			}
		}, function(){
			if($.isFunction(cancel)){
				cancel.call(window);
			}
		});
	}
};

UICtrl.prompt = function (msg,fn){
	var dialog=UICtrl.getDialog(true);
	dialog.prompt('<div style="padding-top:20px;">'+msg+'</div>',function(value){
	   if($.isFunction(fn)){
		   fn.call(window,value);
	   }
	});
};

UICtrl.layout = function (el,options){
	options=options||{};
	var sizeChanged=options['onSizeChanged'];
	options['onSizeChanged']=function(){
		if($.isFunction(sizeChanged)){
			sizeChanged.call(window);
		}
		if (window['gridManager']) {// 解决左右移动时，grid自适应
			UICtrl.onGridResize(window['gridManager']);
	    }
	};
	$(el).layout(options);
};

UICtrl.initDefaultLayout = function(){
	UICtrl.layout("#layout");
};

/** *****ligerUI 封装********* */
UICtrl.tree = function (el,options){
	el=$(el);
	el.ligerTree(options);
	return el.ligerGetTreeManager();
};

UICtrl.grid = function (el,options){
	if(!$(el).length) return;
	el=$(el).wrap("<div class='row-fluid'></div>");
	options=UICtrl.disposeGridPermissionField(options,el.attr('id'));
	UICtrl.adjustGridWidthInDialog(el,options);
	UICtrl.createGridToolbar(el,options);
	// 为查询添加管理权限类别
	var manageType=options.manageType;
	if(!Public.isBlank(manageType)){
		var parms = options.parms||{};
		parms[Public.manageTypeParmName]=manageType;
		options.parms=parms;
	}
	var _windowWidth=$.windowWidth();
	$(options.columns).each(function (i, column){
		if(_windowWidth < 700){//xx add 屏幕宽度太小不启用frozen
			column['frozen'] = false;
        }
    });
	el.addClass('xx-ui-grid').ligerGrid(options);
	var _gridManager=el.ligerGetGridManager();
	UICtrl.buildColumnSettingBtn(_gridManager);
	return _gridManager;
};

UICtrl.reRenderGridColumns = function (gridManager,columns){
	var _windowWidth=$.windowWidth();
	$(columns).each(function (i, column){
		if(_windowWidth < 700){//xx add 屏幕宽度太小不启用frozen
			column['frozen'] = false;
        }
    });
	gridManager.set('columns', columns); 
	gridManager.reRender();
};

UICtrl.getGridManager = function(el){
	return $(el).ligerGetGridManager();
};

UICtrl.onGridResize = function(gridManager){
	if(gridManager){
		gridManager._onResize();
		/*try{
			gridManager.reRender();
    	}catch(e){
    		gridManager._onResize.ligerDefer(gridManager, 50);
    	}*/
	}
};

UICtrl.getGridColumns = function(gridManager){
	var columns= gridManager.columns;
	//删除复选框
	if(gridManager.enabledCheckbox()){
		columns.splice(0,1);
	}
	return columns;
};
UICtrl.setGridColumns = function(gridManager,columns){
	gridManager.set('columns', columns);
	gridManager.reRender();
};

//初始化表头设置按钮
UICtrl.buildColumnSettingBtn=function(g){
	if(!g) return;
	if(g.toolbar&&g.toolbar.length > 0){
		if($.fn.gridColumnSetting){
			var _btn=$("<div class='l-bar-group'><i class='fa fa-cog'></i></div>").insertBefore($('.l-bar-selectpagesize', g.toolbar));
			_btn.gridColumnSetting({gridManager:g});
		}
	}
};

//大小调整
UICtrl.adjustGridWidthInDialog=function(el,options){
	var _width=options.width||'100%';
	//div.ui_content 为dialog中元素
	var $dialogDom=$(el).parents('div.ui_content');
	//grid是否在dialog中
	if($dialogDom.length>0){
		var _$dialogWidth=$dialogDom.width();
		if (typeof (_width) == "string" && _width.indexOf('%') > 0){
			var gridparent = $(el).parent();
			var parentWidth = gridparent.width();
			_width = parentWidth * parseFloat(_width) * 0.01;
		}else{
			_width = parseInt(_width);
		}
		if(_width>=_$dialogWidth){
			options.width=_$dialogWidth;
		}
	}
};

UICtrl.createGridToolbar=function(el,options){
	var toolbarItems=[],_items=[];
	if(options['toolbar']){
		toolbarItems=options['toolbar'].items;
	}
	if(!toolbarItems.length){
		return;
	}
	$.each(toolbarItems,function(i,item){
		if(item.line){
			return true;
		}
		_items.push({
			id:'toolbar_'+item.id,
			name:item.name||item.text,
			icon:item.icon||item.img,
			event:item.event||item.click,
			title:item.title,
			className:item.className
		});
	});
	var _toolbar=$('<div class="row-fluid grid-tool-bar" checkAccess="false"></div>').insertBefore(el);
	_toolbar.toolBar(_items);
	delete options['toolbar'];
};

UICtrl.gridSearch = function (grid, param){
	var parms = grid.options.parms;
	grid.options.parms = $.extend(true, parms, param);
	grid.options.newPage = 1;
	if (grid.isDataChanged && !confirm(grid.options.isContinueByDataChanged)){
		return false;
	}
	grid.loadData();
};

// 导出
UICtrl.gridExport = function (grid, param){
	param=param||{};
	var staticName={head:'exportHead',able:'exportAble',type:'exportType'};
	var p=grid.options,jsonParam={};
	if(!grid['currentData']){
		Public.tips({type:1, content : $.i18nProp('common.warning.export.error')});
		return false;
	}
	var total=parseInt(grid.currentData[p.record]);
	// 总数判断
	if (isNaN(total)||total < 1){
		Public.tips({type:1, content : $.i18nProp('common.warning.export.error')});
		return false;
	}
	// 获取查询参数
	if (p.parms){
        var parms = $.isFunction(p.parms) ? p.parms() : p.parms;
        if (parms.length){
            $(parms).each(function (){
            	jsonParam[this.name]=this.value;
            });
        }else if (typeof parms == "object"){
            for (var name in parms){
            	jsonParam[name]=parms[name];
            }
        }
    }
	// 页码
    if (p.usePager){
    	jsonParam[p.pageParmName]=p.newPage?p.newPage:1;
    	jsonParam[p.pagesizeParmName]=p.pageSize;
    }
    // 排序
    if (p.sortName){
    	jsonParam[p.sortnameParmName]=p.sortName;
    	jsonParam[p.sortorderParmName]=p.sortOrder;
    }
    param=$.extend(true,jsonParam,param);
	// 获取表头
	var exportHead=param[staticName.head];
	if($.isFunction(exportHead)){
		exportHead=exportHead.call(window);
	}
	// 根据grid表头创建数据
	if(typeof exportHead=='undefined'){
		var _initBuildGridHeader=function(){
			var g = grid, p = grid.options,xmls=['<tables><table>'];
			for (var level = 1; level <= g._columnMaxLevel; level++){
				var columns = g.getColumns(level);           // 获取level层次的列集合
	            var islast = level == g._columnMaxLevel;     // 是否最末级
	            xmls.push("<row>");
	            $(columns).each(function (i, column){
	            	xmls.push(_createHeaderCol(column));
	            });
	            xmls.push("</row>");
	        }
			xmls.push('</table></tables>');
			return xmls.join('');
		};
		var _createHeaderCol=function(column){
			 if (column.issystem) return '';// 系统列
		     if (column.isAllowHide == false) return '';// 隐藏列
		     if (column[staticName.able]===false) return '';// 定义不能导出该列
		     if (column._hide) return '';// 该列被隐藏
			 var xmls=[],fieldName=column['name'];
			 if (column['sortField']){// 存在字段转换的问题
				 fieldName=column['sortField'];
			 }
			 if (column['exportField']){// 存在字段转换的问题
				 fieldName=column['exportField'];
			 }
			 xmls.push("<col field='",fieldName,"' ");
			 if (column['__colSpan']) xmls.push("colSpan='", column['__colSpan'],"' ");
			 if (column['__rowSpan']) xmls.push("rowSpan='", column['__rowSpan'],"' ");
			 if (column['type']) xmls.push("type='", column['type'],"' ");
			 if (column['dictionary']) xmls.push("dictionary='", column['dictionary'],"' ");
			 if (column['backgroundColor']) xmls.push("backgroundColor='", column['backgroundColor'],"' ");
			 xmls.push("index='",column['columnindex'],"' ");
			 xmls.push(">",column['display'],"</col>");
	         return xmls.join('');
	    };
		exportHead=_initBuildGridHeader();
	}
	param[staticName.head]=Public.encodeURI(exportHead);
	param[staticName.type]=param[staticName.type]||'all';
	var fileName=param.fileName?param.fileName:(p.title?p.title:'');
	UICtrl.downFileByAjax(p.url,param,fileName);
};

UICtrl.setGridRowAutoHeight=function(grid){
	var div=$(grid);
	var tr1s=$('div.l-grid-body1',div).find('tr');
	var tr2s=$('div.l-grid-body2',div).find('tr');
	if(tr1s.length==0||tr2s.length==0){
		return;
	}
	$("div.l-grid-row-cell-inner",div).css("height", "auto");
	tr1s.css("height", "auto");
	tr2s.css("height", "auto");
	var tmpTr=null;
	tr1s.each(function(i){
		tmpTr=$(tr2s.get(i));
		var maxHeight=Math.max($(this).height(),tmpTr.height());
		$(this).height(maxHeight);
		tmpTr.height(maxHeight);
	});
};

UICtrl.getAddGridData=function(grid){
	 var autoAddRow=grid.options.autoAddRow,data={};
	 if(autoAddRow){
		 data={};
		 $.each(grid.columns,function(i,o){
			 if(o.name){
				 data[o.name]=''; 
			 }
		 });
	 }else{
		 return false;
	 }
	 return $.extend(data,autoAddRow);
};

UICtrl.addGridRow=function(grid,data){
	data=data||{};
	var rowData=UICtrl.getAddGridData(grid);
    if(rowData===false) return;
    data=$.extend(rowData,data);
    setTimeout(function(){
    	grid.gridbody.scrollTop(9999);
    },0);
    grid.options.clearScroll=true;
    return grid.addRow(data,null,false);
};


UICtrl.downFileByAjax=function(url,param,fileName){
	Public.ajax(url,param, function(data) {
		UICtrl.downFile({action:'attachment/downFileByTmpdir.ajax',param:{
			file:Public.encodeURI(data.file),
			fileName:Public.encodeURI(fileName)
		}});
	});
};
UICtrl.downFile=function(op){
	var iframe=$('#downFile_hidden_Iframe');
	if(iframe.length==0){ 
		iframe=$('<iframe name="downFile_hidden_Iframe" style="display:none;"></iframe>').appendTo('body');
	}
	var param=op.param||{},action=op.action;
	var url=$.getCSRFUrl(action,param);
	iframe.attr("src", url);  
};

// 获取页面的高度、宽度
UICtrl.getPageSize=function() {
    return {w:$.windowWidth(),h:$.windowHeight()};
};

// 搜索区域 收缩/展开
UICtrl.setSearchAreaToggle = function (grid,doc,autoHide) {
	if(typeof doc =='undefined'){
		doc=$('#navTitle');
		autoHide=true;
	}else if(typeof doc == 'boolean'){
		autoHide=doc;
		doc=$('#navTitle');
	}
	if(!doc.length) return;
	var button=$("a.togglebtn",doc).addClass('grid-toggle');
	if(!button.length) return;
	var hideTable=button.attr('hideTable');
	hideTable=hideTable.startsWith('#')?hideTable:'#'+hideTable;
	var table=$(hideTable);
	if(autoHide!=false&&table.is(':visible')){
		table.addClass('ui-hide');
		button.find('i.fa').removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
		UICtrl.onGridResize(grid);
	}
	$("span.titleSpan",doc).add(button).on('click', function () {
    	button.find('i.fa').toggleClass('fa-angle-double-down').toggleClass('fa-angle-double-up');
    	table.toggleClass('ui-hide');
    	UICtrl.onGridResize(grid);
    	if(!table.hasClass('ui-hide')){
    		//控件显示高度动态计算
        	Public.autoElementWrapperHeight(table);
    	}
    });
};
// 分组收缩/展开
UICtrl.autoGroupAreaToggle = function (doc) {
	doc=doc||$('body');
    $("a.togglebtn",doc).on('click', function () {
    	if($(this).hasClass('grid-toggle')){
    		return true;
    	}
    	var hideTable=$(this).attr('hideTable');
    	var hideIndex=$(this).attr('hideIndex');
    	if(!Public.isBlank(hideTable)){
    		$.each(hideTable.split(','),function(i,expr){
    			$(expr).toggleClass('ui-hide');
    		});
    	}
    	if(!Public.isBlank(hideIndex)){
    		var index=parseInt(hideIndex);
    		if(!isNaN(index)&&index>0){
    			var parent=$(this).parent().parent();
    			if(parent.is('td')){// 寻找TR
    				parent=parent.parent();
    			}
    			parent.nextAll(":lt("+index+")").toggleClass('ui-hide');
    		}
    	}
    	$(this).find('i.fa').toggleClass('fa-angle-double-down').toggleClass('fa-angle-double-up');
    });
};

// 默认按钮定义 text 用国际化编码代替
UICtrl.defaultToolbarOptions={
	addFolderHandler:{id:'AddFolder',text:'common.button.addfolder',img:'fa-plus',className:'btn-gray'},
	addHandler:{id:'Add',text:'common.button.add',img:'fa-plus',className:'btn-gray'},
	addBatchHandler:{id:'AddBatch',text:'common.button.addbatch',img:'fa-plus-square',className:'btn-gray'},
	updateHandler:{id:'Update',text:'common.button.update',img:'fa-edit',className:'btn-gray'},
	deleteHandler:{id:'Delete',text:'common.button.delete',img:'fa-trash-o',className:'btn-gray'},
	saveSortIDHandler:{id:'SaveID',text:'common.button.saveid',img:'fa-floppy-o',className:'btn-gray'},
	saveHandler:{id:'Save',text:'common.button.save',img:'fa-floppy-o',className:'btn-gray'},
	enableHandler:{id:'Enable',text:'common.button.enable',img:'fa-thumbs-o-up',className:'btn-gray'},
	disableHandler:{id:'Disable',text:'common.button.disable',img:'fa-thumbs-down',className:'btn-gray'},
	moveHandler:{id:'Move',text:'common.button.move',img:'fa-arrows',className:'btn-gray'},
	exportExcelHandler:{id:'ExportExcel',text:'common.button.exportexcel',img:'fa-cloud-download',className:'btn-gray'},
	copyHandler:{id:'CopyNew',text:'common.button.copynew',img:'fa-files-o',className:'btn-gray'},
	viewHandler:{id:'View',text:'common.button.view',img:'fa-list-alt',className:'btn-gray'},
	saveImpHandler:{id:'SaveImp',text:'common.button.saveimp',img:'fa-upload',className:'btn-gray'},
	printHandler:{id:'Print',text:'common.button.print',img:'fa-print',className:'btn-gray'}
};
// 组合页面Grid操作按钮
UICtrl.getDefaultToolbarOptions = function (options) {
    var op = options || {},items = [];
    $.each(op,function(p,o){
    	if(o===false) return;
    	var title=o['title']||'';
    	if($.isFunction(o)&&UICtrl.defaultToolbarOptions[p]){
    		var title=UICtrl.defaultToolbarOptions[p]['title']||'';
    		with (UICtrl.defaultToolbarOptions[p]){
    			items.push({ id: "menu"+id, text:text, click:o, img:img,itemId:p,title:title,className:className});
    		}
    	}else{
    		items.push({ id: "menu"+o['id'], text:o['text'], click:o['click'], img:o['img'],itemId:p,title:title,className:o['className']});
    	}
    });
    if(items.length===0){
    	return false;
    }
    return { items: items };
};

UICtrl.getTotalSummary=function(){
   return {
		render: function (suminf, column, data){
			if(!data) return'';
			if(column.type=='money'){
	    		return '<div>' + Public.currency(data.totalFields[column.name]) + '</div>';
			}else{
				return '<div>' + data.totalFields[column.name] + '</div>';
			}
		},
		align: 'right'
   };
};

// 关闭Tab项,如果tabid不指定，那么关闭当前显示的
UICtrl.closeCurrentTab = function (tabId,fn) {
	if(parent.closeTabItem){
		try{parent.closeTabItem(tabId);}catch(e){}
	}else{
		if($.isFunction(fn)){
			fn.call(window);
		}else{
			Public.closeWebPage();
		}
	}
};

// 刷新父窗口
UICtrl.reloadParentTab = function(parentTabId){
	try{parent.reloadParentTab(parentTabId);}catch(e){}
};

// 刷新指定窗口
UICtrl.reloadTabById = function(tabId){
	try{parent.reloadTabById(tabId);}catch(e){}
};

//获取当前激活窗口的ID
UICtrl.getCurrentTabId = function(){
	try{return parent.getCurrentTabId();}catch(e){return '';}
};

// 关闭Tab项并且刷新父窗口和首页窗口
UICtrl.closeAndReloadTabs = function(parentTabId,tabId,fn){
	tabId=tabId||UICtrl.getCurrentTabId();
	if(parentTabId=='TaskCenter'){
		UICtrl.reloadParentTab('homepage');
		UICtrl.reloadParentTab(parentTabId);
	}else{
		$.each(parentTabId.split(','),function(i,o){
			UICtrl.reloadParentTab(o);
		});
	}
	UICtrl.closeCurrentTab(tabId,fn);
};

//创建新的tab页
UICtrl.addTabItem = function(item){
	if(parent.addTabItem){
		parent.addTabItem(item);
	}else{
		Public.openPostWindow(item.url);
	}
};

//退出
UICtrl.doLogout = function(){
	if(parent.doLogout){
		parent.doLogout();
	}else{
		window.location.href = web_app.name +'/logout.do';
	}
};

UICtrl.sequenceRender = function(item,idFieldName) {
	var idFieldName=idFieldName||'id';
	return "<input type='text' id='txtSequence_" + item[idFieldName] + "' class='textbox' value='" + item.sequence + "' />";
};

UICtrl.getStatusInfo=function(status) {
	status = $.isPlainObject(status) ? status.status:status;
    switch (parseInt(status)) {
        case -1:
        	 return "<div class='status-draft' title='草稿'/>";
             break;
        case 0:
            return "<div class='status-disable' title='禁用'/>";
            break;          
        case 1:
            return "<div class='status-enable' title='启用'/>";
            break;
    }
};

UICtrl.showMoveTreeDialog=function(op){
	op=op||{};
	if(op.gridManager){
		var rows = op.gridManager.getSelectedRows();
		if (!rows || rows.length < 1) {
			Public.tip('common.warning.nochoose');
			return;
		}
	}
	UICtrl.showDialog({title:op.title||'common.button.move',width:300,
		content:'<div style="overflow-x: hidden; overflow-y: auto; width:280px;height:250px;"><ul id="dialogMoveTree"></ul></div>',
		init:function(){
			$('#dialogMoveTree').commonTree({kindId : op.kindId,IsShowMenu : false,parentId:op.parentId||0});
		},
		ok:function(){
			var parentId=$('#dialogMoveTree').commonTree('getSelectedId');
			if(!parentId){
				Public.tip('common.warning.not.nodetree');
				return false;
			}
			var node=$('#dialogMoveTree').commonTree('getSelected');
			var flag=op.save.call(window, parentId, node);
			if(flag===false){
				return false;
			}
			this.close();
		}
	});
};

//国际化资源初始化页面
UICtrl.showInitI18nproperties=function(i18nResourceKind,fn){
	UICtrl.showMoveTreeDialog({title:'common.button.addinit',kindId:CommonTreeKind.i18nProperties,
		save:function(parentId){
			Public.ajax(web_app.name + "/i18nProperties/initI18nproperties.ajax",{folderId:parentId,resourcekind:i18nResourceKind},function(){
				if($.isFunction(fn)){
					fn.call(window);
				}
			});
		}
	});
};

// 控制多选
UICtrl.checkSelectedRows = function(gridManager){
	var rows = gridManager.getSelectedRows();
	if (rows.length == 0) {
		Public.tip('common.warning.nochoose');
		return false;
	} else if (rows.length > 1) {
		Public.tip('common.warning.onlychoose');
		return false;
	}
	return rows[0];
};
// 创建查询输入框
UICtrl.createQueryBtn=function(el,event){
	var html=['<div class="ui-grid-query-div">'];
	html.push('<div class="input-group">');
	html.push('<input type="text" value="" class="text ui-grid-query-input">');
	html.push('<span class="input-group-btn">');
	html.push('<a href="javascript:void(0);" type="button" class="btn btn-sm ui-grid-query-clear"><i class="fa fa-times-circle"></i></a>');
	html.push('<button type="button" class="btn btn-sm btn-info ui-grid-query-button"><i class="fa fa-search"></i></button>');
	html.push('</span>');
	html.push('</div>');
	html.push('</div>');
	var div=$(html.join('')).appendTo(el);
	div.find('.ui-grid-query-button').on('click',function(){
		if($.isFunction(event)){
			event.call(window,div.find('input').val());
		}
	});
	div.find('.ui-grid-query-clear').on('click',function(){
		div.find('input').val('');
	});
	div.find('input').bind('keyup.queryForm',function(e){
		var k =e.charCode||e.keyCode||e.which;
		if(k==13){// 回车
			if($.isFunction(event)){
				event.call(window,div.find('input').val());
			}
		}
	});
};
// 在grid上创建查询按钮
UICtrl.createGridQueryBtn=function(grid,title,event){
	if($.isFunction(title)){
		event=title;
		title='div.l-panel-header';
	}
	if($(grid).find(title).length>0){
		UICtrl.createQueryBtn($(grid).find(title),event);
	}
};

UICtrl.createGridToolBarQueryBtn=function(grid,event){
	UICtrl.createQueryBtn($(grid).parent().find('div.grid-tool-bar'),event);
};

UICtrl.autoWrapperHeight = function (fn) {
	var pageSize = UICtrl.getPageSize();
	$('div.dom-auto-height').height(pageSize.h - 40);
	if(fn&&$.isFunction(fn)){
		fn.call(window,pageSize);
	}
	$.WinReszier.register(function(){
		var _size = UICtrl.getPageSize();
		if(_size.h>10){
			$('div.dom-auto-height').height(_size.h - 40);	
			if(fn&&$.isFunction(fn)){
				fn.call(window,_size);
			}
		}
	});
};

/**
 * 合并单元格
 */
UICtrl.mergeCell = function(columnName, $grid,frozen){
	$grid=$($grid);
	var gridManager = $grid.ligerGetGridManager();
	var _gridbody=gridManager.gridbody;
	if(frozen){
		_gridbody=gridManager.f.gridbody;
	}
	var columnId = null,columns = gridManager.options.columns;
	$.each(columns, function(i){
		if (this.name === columnName) {
			columnId = this.__id;
			return false;
		}
	});
	var k,cellValue = "",cellId = "";
	$("td[id$='" + columnId + "']", _gridbody).each(function(index){
		if (cellValue == $("div", this).text()) {
			k++;
			$(this).addClass("l-remove");
			$("td[id='" + cellId + "']", _gridbody).attr("rowspan", k);
		}
		else {
			k = 1;
			cellValue = $("div", this).text();
			//得到点击处的id 
			cellId = $(this).attr("id"); 
		}
	});
	$(".l-remove",_gridbody).remove();
};

UICtrl.mergeCellByValue = function(columnName,valueColumn,$grid,frozen){
	$grid=$($grid);
	var gridManager = $grid.ligerGetGridManager();
	var _gridbody=gridManager.gridbody;
	if(frozen){
		_gridbody=gridManager.f.gridbody;
	}
	var rows=gridManager.getData(),row=null;
	if(!rows.length) return;
	var columnId = null,columns = gridManager.options.columns;
	$.each(columns, function(i){
		if (this.name === columnName) {
			columnId = this.__id;
			return false;
		}
	});
	var k,cellValue = "",cellId = "";
	$("td[id$='" + columnId + "']", _gridbody).each(function(index){
		row=rows[index];
		if (cellValue == row[valueColumn]) {
			k++;
			$(this).addClass("l-remove");
			$("td[id='" + cellId + "']", _gridbody).attr("rowspan", k);
		}
		else {
			k = 1;
			cellValue = row[valueColumn];
			//得到点击处的id 
			cellId = $(this).attr("id"); 
		}
	});
	$(".l-remove",_gridbody).remove();
};

//设置以下输入框必填
UICtrl.setRequiredFlag = function($doc,isRequired){
	$('textarea',$doc).each(function(i,o){
		UICtrl.setElRequiredFlag(this,isRequired);
	});
	$('input[type="text"]',$doc).each(function(i,o){
		UICtrl.setElRequiredFlag(this,isRequired);
	});
	$('select', $doc).each(function (i, o) {
		UICtrl.setElRequiredFlag(this,isRequired);
    });
};
//设置单控件必填
UICtrl.setElRequiredFlag=function(input,isRequired) {
	var $input=$(input);
	if($input.is('[type="hidden"]')){
    	return;
    }
	var parentCol=null;
	if($input.parent().is('div.col-white-bg')){
		parentCol=$input.parent();
	}else if($input.parent().parent().is('div.col-white-bg')){
		parentCol=$input.parent().parent();
	}
	if(parentCol&&parentCol.length>0){
		parentCol.prev().find('label.hg-form-label')[isRequired?'addClass':'removeClass']('required');
	}
	if(isRequired){
		$input.attr("required",true);
	}else{
		$input.removeAttr("required");
	}
	//选择框需要单独处理
	if($input.data('ui-combox-input')){  
		$input.data('ui-combox-input')[isRequired?'addClass':'removeClass']('ui-combox-required');
	}
	if($input.hasClass('ui-combox-input')){
		$input[isRequired?'addClass':'removeClass']('ui-combox-required');
	}
};

//切换岗位
UICtrl.switchOperator=function(fn){
	var url = web_app.name + '/org/queryPersonMembersByPersonId.ajax';
	Public.ajax(url, {}, function (data) {
		var html = ['<table width="100%" border=0 cellspacing=0 cellpadding=0 >', '<thead>', '<tr>'];
	    html.push('<th>',$.i18nProp('common.switch.operator.org'),'</th>');
	    html.push('<th>',$.i18nProp('common.switch.operator.dept'),'</th>');
	    html.push('<th>',$.i18nProp('common.switch.operator.pos'),'</th>');
	    html.push('</tr>', '</thead>');
	    $.each(data, function (i, o) {
	        html.push('<tr class="list" psmId="', o['id'], '">');
	        html.push('<td>', o['orgName'], '</td>');
	        html.push('<td>', o['deptName'], '</td>');
	        html.push('<td>', o['positionName'], '</td>');
	        html.push('</tr>');
	    });
	    html.push('</table>');
	    var options = {
	        title: $.i18nProp('common.switch.operator'),
	        content: html.join(''),
	        width: 400,
	        opacity: 0.1,
	        onClick: function ($el) {
	            if ($el.is('td')) {
	            	var psmId = $el.parent().attr('psmId');
	                Public.ajax(web_app.name + '/switchOperator.ajax', {psmId: psmId}, function (data) {
	                	if($.isFunction(fn)){
		            		fn.call(window,data);
		            	}
	                });
	            }
	        }
	    };
	    Public.dialog(options);
	});
};

//创建顶部用户岗位切换按钮
UICtrl.showFloatTopMenu = function () {
	var _button=$('<div class="ui-float-top-button"><i class="fa fa-address-card-o"></i></div>').appendTo('body');
	_button.on('click',function(){
		_showFloatTopMenu();
	});
	var _showFloatTopMenu=function(){
		var _menu=$('<div class="ui-float-top-menu"></div>').appendTo('body');
		Public.load(web_app.name + '/common/data.jsp',{},function(data){
			_menu.append(data);
		});
		_menu.on('click',function(e){
			var $clicked = $(e.target || e.srcElement);
			if($clicked.is('i.fa')||$clicked.is('span')){
				$clicked=$clicked.parent();
			}
			if($clicked.hasClass('ui-float-top-close')){
				_menu.removeAllNode();
			}
			if($clicked.hasClass('ui-float-top-pos')){
				UICtrl.switchOperator(function(){
					window.location.reload();
				});
			}
			if($clicked.hasClass('ui-float-top-sign-out')){
				Public.load(web_app.name + '/logout.do',{},function(data){
					Public.closeWebPage();
				});
			}
		});
	};
};
