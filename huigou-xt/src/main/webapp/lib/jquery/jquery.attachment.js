/*---------------------------------------------------------------------------*\
|  title:         上传按钮及文件列表控件                                                         |
|  Author:  xx                                                                                       |
|  LastModified:  2013-03-03                                                                     |
|  JSP标签:<x:fileList id="filelist" bizCode="SAL_ORDER" bizId='${orderid}'/> |
|  JS调用: $('#filelist').fileList({button:'#upfilebutton'});                                  |
|  普通使用:                                                                                                 |
|  $.uploadDialog({title:'上传APP',param:{versionid:versionid},backurl:'',afterUpload:function(data){}});
\*---------------------------------------------------------------------------*/
(function($) {
	/*******************上传按钮对话框***********************/
    var JUploadDialog=function(options) {
		options=$.extend({
			title:'上传文件',
			isScreen:true,//是否显示遮罩
			screenColor:'#001',    //遮罩透明色
			screenAlpha:0,    //遮罩透明度
			param:{},//参数
			url:web_app.name+'/attachment/upload.ajax?a='+new Date().getTime(),//上传处理URL
			filetype:[],//可上传文件名
			backurl:'doSave',//上传成功后执行url默认doSave保存到数据库
			from:false,//标记添加附件列表
			afterError:false,//上传出错处理
			afterUpload:false//上传成功处理
		},options||{});
		this.init(options);
	};
    $.extend(JUploadDialog.prototype, {
		init : function(options) {
			this.options = options;
			var self = this;
			var url = options.url || '',
				title =  options.title || '上传文件',
				backurl =  options.backurl || '',
				from = options.from||false,
				param = options.param || {},
				target = 'jquery_upload_iframe_' + new Date().getTime();
				options.afterError = options.afterError || function(str) {
					alert(str);
				};
			if(from){
				param=$.extend($(from).fileList('getOptions'),param);
				if(!param['bizId']||param['bizId']==''){alert('bizId为空,不能执行上传。');return;}
				options.afterUpload=options.afterUpload||function(data){
					$(from).fileList('addFile',data);
				};
			}
			var html = ['<div class="JUploadDialog">',
						'<div class="title">',
						'<span class="msg_title">',title,'</span>',
						'<span class="icos"><a href="javascript:void 0" hidefocus  class="close">X</a></span>',
					    '</div>',
					    '<div class="content">',
						'<iframe name="' , target , '" style="display:none;"></iframe>',
						'<form method="post" enctype="multipart/form-data" target="' , target , '">',
						'<table cellpadding="2" style="width:100%;"><tr><td style="text-align:left;height:30px;">',
				        '<input type="hidden" name="backurl" value="',backurl,'">'];
			for(var p in param){
				html.push('<input type="hidden" name="',p,'" value="',param[p],'">');
			}
			html.push('<input type="text" readonly class="text path" style="width:200px;"/>' ,
						'<span class="uploadInput"><input type="file" class="file" name="upload"/><input type="button" class="button" value="浏览..." /></span></td></tr>',
						'<tr><td align=center><input type="button" class="button upload" value="上 传" />&nbsp;&nbsp;<input type="button" class="button close" value="取 消"/></td></tr>',
						'</table></form>',
						'<div></div></div></div>');
			var div = $(html.join('')).appendTo('body');
			try{
				div.drag({ handle: '.title',opacity: 0.8});
				$('.title',div).css('cursor','move');
			}catch(e){
				$('.title',div).css('cursor','default');
			}
			this.div = div;
			this.iframe = $('iframe', div);
			this.form = $('form', div);
           
			div.bind('click.JUploadDialog',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.is('a.close')){
					self.remove();
					e.preventDefault();
					e.stopPropagation();
					return false;
				}else if($clicked.is('input.close')){
					self.remove();
					e.preventDefault();
					e.stopPropagation();
					return false;
				}else if($clicked.is('input.upload')){
					self.submit();
					e.preventDefault();
					e.stopPropagation();
					return false;
				}
			});
			div.find('input.file').bind('change',function(){
				div.find('input.path').val($(this).val());
			});
			this.show();
		},
		show:function(){
			var screenOver=$('#Jquery_ScreenOver');
			if(!screenOver.length){
				screenOver=$('<div id="Jquery_ScreenOver" style="position:absolute;top:0px;left:0px;width:0;height:0;z-index:1000;display:none;"></div>').appendTo('body');
			}
			var d = $(document),w=d.width(),h=d.height(),mt=d.scrollTop(),ml=d.scrollLeft();
			var rootEl=document.compatMode=='CSS1Compat'?document.documentElement:document.body;	//根元素
			var width=this.options.width||300;
			var diagtop = (h-100+mt)/2;
			var diagleft = (w-width+ml)/2;
			this.div.css({width:width,top:diagtop,left:diagleft}).show();
			if(this.options.isScreen){
				screenOver.css({
					width:(rootEl.scrollLeft==0?rootEl.clientWidth:rootEl.scrollWidth)+'px',
					height:(rootEl.scrollTop==0?rootEl.clientHeight:rootEl.scrollHeight)+'px',
					background:this.options.screenColor,
					filter:'alpha(opacity='+(this.options.screenAlpha*100)+')',
					opacity:this.options.screenAlpha
				}).show();
			}
		},
		submit : function() {
			var self = this,
				iframe = self.iframe,
				form = self.form[0];
			    file=$('input.file',self.div).val();
			if(file==''){
				alert('请选择需要上传的文件!');
				return false;
			}
			var ext=file.substring(file.lastIndexOf(".")+1);//取文件后缀
			var type=self.options.filetype.join(',').toLowerCase();
			var reg = new RegExp("(^|,)"+ext.toLowerCase()+"(,|$)", "ig");
			if(type!=''&&!reg.test(type)){//判断文件后缀是否合法
				alert('只能上传'+type+'类型文件!');
				return false;
			}
			iframe.bind('load', function() {
				iframe.unbind();
				var data=null, str = $.iframeDoc(iframe).body.innerHTML;
				try {
					data = $.json(str);
					if(data.error){
						self.options.afterError.call(self, data.error);
						return;
					}
				} catch (e) {
					self.options.afterError.call(self, str);
					self.reSetUpButton();
				}
				if (data) {
					self.remove();
					Public.ajaxCallback(data,function(d){
						if(typeof self.options.afterUpload=='function')
							self.options.afterUpload.call(self, d);
					});
				}
			});
			this.div.find('input.upload').attr('disabled','true').val('上传中...');
			form.action=self.options.url;
			form.submit();
			return self;
		},
		reSetUpButton: function(){
			this.div.find('input.upload').removeAttr('disabled').val('上 传');
		},
		remove : function() {
			this.div.unbind('click.JUploadDialog');
			this.div.removeAllNode();
			$('#Jquery_ScreenOver').hide();
			this.div = null;
			this.iframe =null;
			this.form = null;
		}
	});
	$.iframeDoc=function(iframe) {
		iframe = $(iframe).get(0);
		return iframe.contentDocument || iframe.contentWindow.document;
	};
	$.json=function(text) {
		var match;
		if ((match = /\{[\s\S]*\}|\[[\s\S]*\]/.exec(text))) {
			text = match[0];
		}
		var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;
		cx.lastIndex = 0;
		if (cx.test(text)) {
			text = text.replace(cx, function (a) {
				return '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
			});
		}
		if (/^[\],:{}\s]*$/.
		test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
		replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
		replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
			return eval('(' + text + ')');
		}
		throw 'JSON parse error';
	};
	$.uploadDialog = function(options){
		return new JUploadDialog(options);
	};
	/*****************上传按钮对象************************/
	$.fn.uploadButton = function(op){
		return this.each(function() {
			var obj=$.data(this,'JUploadButton');
			if(!obj){
				new JUploadButton($(this),op);
			}else{
				if (typeof op == "string") {
					var _self=$(this);
					$.each(['enable','disable'],function(i,m){
						if(op==m){
							obj[m].call(obj,_self);
							return false;
						}
					});
				}else{
					obj.set(op);
				}
			}
		}); 
	};
	 var JUploadButton=function($el,options) {
		 this.options={};
		 this.element=$el;
		 this.set(options);
		 this.init();
		 this.id='UB'+new Date().getTime()+"_"+Math.round(Math.random()*10000);
		 $el.data('JUploadButton',this);
	};
	$.extend(JUploadButton.prototype, {
		set:function(op){
			this.options=$.extend({
				param:{},//参数
				getParam:null,
				url:web_app.name+'/attachment/upload.ajax',//上传处理URL
				filetype:[],//可上传文件名
				backurl:'doSave',//上传成功后执行url默认doSave保存到数据库
				fontSize:12,
				from:false,//标记添加附件列表
				afterError:false,//上传出错处理
				afterUpload:false,//上传成功处理
				mouseover:false,
				mouseout:false,
				beforChoose:false//选择文件前判断是否允许上传
			},this.options, op||{});
		},
		init : function() {
			var self = this,opt=this.options;
			this.element.on('mouseenter',function(){
				var height=$(this).outerHeight(),width=$(this).outerWidth();
				var offset=$(this).offset();
				var uploadSpan=$('#'+self.id);
				if(uploadSpan.length==0){
					uploadSpan=$('<span class="ui-upload-button-span"><input class="ui-upload-button" name="upload" type="file" size="1" hidefocus></span>').appendTo('body');
					uploadSpan.attr('id',self.id);
				}
				uploadSpan.css({left:offset.left,top:offset.top,width:width,height:height}).show();
			    var button=uploadSpan.find('input').css({height:height,width:width}).attr('title',self.element.attr('title'));
				button.unbind('change.JUploadButton').bind('change.JUploadButton',function(){
					if($(this).val()!=''){
						if($.isFunction(opt.beforChoose)){
							if(opt.beforChoose.call(self,$(this).val())===false){
								return false;
							}
						}
						self.submit($(this).val());
						if($.isFunction(opt.mouseout)){
							opt.mouseout.call(window);
						}
						if(self.element.parents().parent().is('li.item')){
						   	self.element.trigger('mouseout');
						}
			    	}
				});
				uploadSpan.on('mouseenter',function(){
					if($.isFunction(opt.mouseover)){
						opt.mouseover.call(window);
					}
					if(self.element.parents().parent().is('li.item')){
						self.element.trigger('mousemove');
					}
				}).on('mouseleave',function(){
					$(this).hide();
					if($.isFunction(opt.mouseout)){
					   	opt.mouseout.call(window);
					}
					if(self.element.parents().parent().is('li.item')){
					   	self.element.trigger('mouseout');
					}
				});
			});
		},
		submit : function(file) {
			var options = this.options,self = this;
			var url = options.url || '',
			backurl =  options.backurl || '',
			from = options.from||false,
			param = options.param || {},
			target = 'jquery_upload_iframe_' + new Date().getTime();
			options.afterError = options.afterError || function(str) {
				Public.errorTip(str);
			};
			var uploadSpan=$('#'+self.id);
			var ext=file.substring(file.lastIndexOf(".")+1);//取文件后缀
			var type=self.options.filetype.join(',').toLowerCase();
			var reg = new RegExp("(^|,)"+ext.toLowerCase()+"(,|$)", "ig");
			if(type!=''&&!reg.test(type)){//判断文件后缀是否合法
				Public.tip('common.attachment.check.type',type);
				return false;
			}
			if(from){
				param=$.extend($(from).fileList('getOptions'),param);
				if(!param['bizId']||param['bizId']==''){Public.tip('bizId为空,不能执行上传!');return;}
				options.afterUpload=options.afterUpload||function(data){
					$(from).fileList('addFile',data);
				};
			}else{
				if($.isFunction(param)){
					param=param.call(window);
				}
			}
			if(param===false) return;
			var gp={};
			if($.isFunction(options.getParam)){
				gp=options.getParam.call(this);
			}
			if(gp===false) return;
			param=$.extend(true,param,gp);
			if(param.backurl){
				backurl=param.backurl;
				delete param.backurl;
			}
			var html = ['<div style="display:none;">',
						'<iframe name="' , target , '" style="display:none;"></iframe>',
						'<form method="post" enctype="multipart/form-data" target="' , target , '">',
				        '<input type="hidden" name="backurl" value="',backurl,'">'];
			for(var p in param){
				html.push('<input type="hidden" name="',p,'" value="',param[p],'">');
			}
			html.push('</form>','</div>');
			var div = $(html.join('')).appendTo('body');
			var form =  $('form', div)[0];
			uploadSpan.appendTo(form);
			var iframe = $('iframe', div);
			var tip=Public.tips({content:$.i18nProp('common.attachment.loading'),autoClose:false});
			iframe.bind('load', function() {
				iframe.unbind();
				tip.remove();
				var data=null, str = $.iframeDoc(iframe).body.innerHTML;
				self.remove(div);
				try {
					data = $.json(str);
					if(data.error){
						self.options.afterError.call(self, data.error);
						return;
					}
				} catch (e) {
					self.options.afterError.call(self, str);
				}
				if (data) {
					Public.ajaxCallback(data,function(d){
						if(typeof self.options.afterUpload=='function')
							self.options.afterUpload.call(self, d);
					});
				}
			});
			//加入csrf token
			form.action=$.getCSRFUrl(url);
			form.submit();
			return self;
		},
		remove : function(div) {
			div.removeAllNode();
			//this.init();
		}
	});
	/*******************附件列表对象*************************/
	var AttachmentManager=function(el,op){
		this.element=el;
		this.disabled = false;
		this.isClass=$(el).attr('isClass')==='true';
		$(el).addClass('ui-attachment-list').data('ui-attachment-list',this);
		this.options ={};
		this.set(op);
		this.init();
	};
	$.extend(AttachmentManager.prototype, {
		set:function(op){
			var $el=$(this.element);
			this.options=$.extend({
				readOnly: $el.attr('readOnly')||false,//只读不能编辑
				bizCode: $el.attr('bizCode')||false,
				bizId: $el.attr('bizId')||false,
				showImg: $el.attr('showImg')||true,
				isCheck: true,//删除时是否验证权限
				downloadEnable: true,//是否允许下载附件 可以为一个函数
				showTipEnable: true,//是否生成一个tip 可以为一个函数
				deleteEnable: true,//是否允许删除附件 可以为一个函数
				afterUpload: false,//上传成功处理
				queryUrl: web_app.name+'/attachment/doQuery.ajax',//查询附件地址
				deUrl: web_app.name+'/attachment/doDelete.ajax',//删除附件地址
				delAllUrl: web_app.name+'/attachment/doDeleteAll.ajax',//删除全部附件地址
				downUrl: web_app.name+'/attachment/downFile.ajax',//下载附件地址
				saveSortIDUrl: web_app.name+'/attachment/updateAttachmentsSequence.ajax',//保存排序号
				button: false,
				autoload: false,//初始化时查询数据库
				fileHTML: '<div class="file" id="{id}" createdByName="{createdByName}" fileKind="{fileKind}" fileSize="{fileSize}" createdDate="{createdDate}"  {attachmentCode}><span class="{fileKind}">&nbsp;</span>&nbsp;<a href="javascript:void(0);" hidefocus title="{title}">{fileName}</a></div>',
				queryBack: function(data){//查询回调
					var _self=this;
					$(this.element).find('div.file').tooltip('destroy');
					if(this.isClass){//分组管理的数据
						var list=$(this.element).find('div.groupFileList').empty(),groupDivMap={};
						var attachmentCode=null,groupDiv=null,_creator=null;
						list.each(function(){
							attachmentCode=$(this).attr('attachmentCode');
							groupDiv=$(_self.element).find('div.'+attachmentCode);
							groupDivMap[attachmentCode]=groupDiv;
						});
						$.each(data,function(i,o){
							attachmentCode=o['attachmentCode'];
							groupDiv=groupDivMap[attachmentCode];
							_creator=o['creator']||{};
							if(groupDiv){
								_self.addFile($.extend(o,{createdByName:_creator['createdByName'],createdDate:_creator['createdDate']}),groupDiv);
							}
						});
					}else{
						var list=$(this.element).find('div.fileListMain').empty(),html=new Array();
						var fileHTML=this.options.fileHTML,_creator=null;
						$.each(data,function(i,o){
							_creator=o['creator']||{};
							html.push(fileHTML.replace('{id}',o.id)
					                    .replaceAll('{fileKind}',o.fileKind)
					                    .replace('{createdByName}',_creator.createdByName)
					                    .replace('{fileSize}',o.fileSize)
					                    .replace('{attachmentCode}','')
					                    .replace('{title}',o.fileName)
					                    .replace('{createdDate}',_creator.createdDate)
					                    .replace('{fileName}',o.fileName));
						});
						list.html(html.join(''));
						this.addTip();
					}
				},
				delBack: function(id,obj){//删除回调
					if(obj) $(obj).remove();
				},
				delAllBack:function(){
					this.query();
				}
			},this.options, op||{});
			if(Public.isReadOnly){
				this.options.readOnly=true;
			}
			this.bindUploadButton();
		},
		init:function(){
			var $element=$(this.element),_self=this;
			var fileListMain=$element.find('div.fileListMain');
			if(!this.isClass){
				this.bindContextMenu();
			}
			/*if(!$element.hasClass('ui-attachment-list')) $element.addClass('ui-attachment-list');
			if(!$element.find('div.fileListMain').length) $element.append('<div class="fileListMain"></div>');
			if(!$element.find('iframe').length) $element.append('<iframe name="iframe_' +new Date().getTime()+ '" style="display:none;"></iframe>');*/
			if(this.options.autoload) this.query();
			this.iframe=$element.find('iframe').get(0);
			$element.dblclick(function(e){//双击下载
				var $el = $(e.target || e.srcElement);
				if($el.is('input[type="file"]')){//增加该判断处理文件选择按钮在div内的情况
					return;
				}
				var div=$el.is('div.file')?$el:$el.parent('div.file',$element);
				if(div.length>0){
					//div.find('span').attr('class')用于判断是否为图片
					_self.downFile(div.attr('id'),div.attr('fileKind'));
				}
				e.stopPropagation(); 
				e.preventDefault();
				return false;
			}).click(function(e){//点击选中
				var $el = $(e.target || e.srcElement);
				if($el.is('i.fa')) $el=$el.parent();
				fileListMain.closeMenu();
				if($el.is('input[type="file"]')){//增加该判断处理文件选择按钮在div内的情况
					return;
				}
				if($el.hasClass('addFile')||$el.hasClass('addFieldGroup')||$el.hasClass('addBatchFile')){//上传附件按钮不可用时提示
					if(!_self.options.bizId){
						Public.errorTip('common.attachment.check.save');
						return false;
					}
				}
				if($el.hasClass('delFile')){//删除按钮
					var obj=$el.parent(),id=obj.attr('id');
					_self.doDel(id,obj);//删除
					return false;
				}
				if($el.hasClass('toggle')){//显示隐藏
					$el.find('i').toggleClass('fa-chevron-down');
					$element.find('div.fileListMain').toggle();
					$element.toggleClass('ui-attachment-list-border');
					fileListMain.closeMenu();
					return false;
				}
				if($el.hasClass('moreList')){//在窗口中查看
					var groupFileList=$el.parents('div.groupFileList');
					if(!groupFileList.length){
						_self.showAttachmentDialog($element.find('div.fileListMain'));
					}else{//分组显示附件
						_self.showAttachmentDialog(groupFileList);
					}
					return false;
				}
				if($el.hasClass('addBatchFile')){//批量上传
					AttachmentUtil.batchUpload({
						title:$.i18nProp('common.button.batchupload'),
						params:{bizCode:_self.options.bizCode,bizId:_self.options.bizId},
						closeHandler:function(){
							var uploadFinished = this.iframe.contentWindow.uploadFinished;
							if(uploadFinished){
								_self.query();
							}
						}
					});
					return false;
				}
				if(!_self.isClass){//没有分类显示时(div显示模式)
					var div=$el.is('div.file')?$el:$el.parent('div.file',$element);
					$("div.file",$element).removeClass('fileSelected');
					if(div.length>0){
						div.addClass('fileSelected');
					}
				}
				e.stopPropagation(); 
				e.preventDefault();
				return false;
			});
			this.addTip();
			this.initGroupFileList();
		},
		bindContextMenu:function(){//绑定右键菜单
			var $element=$(this.element),_self=this;
			var readOnly = _self.options.readOnly;
			var deleteEnable = _self.options.deleteEnable;
			var downloadEnable = _self.options.downloadEnable;
			var fileListMain=$element.find('div.fileListMain');
			try{
				var items = [];
				if (_self.options.downloadEnable){
					items.push({name:"common.button.download",classes:'temp',icon:'fa-cloud-download',handler:function(){
						var div=$element.find('div.fileSelected');
						if(div.length>0){
							_self.downFile(div.attr('id'));
						}
					}});
				}
				if (!_self.disabled && _self.options.deleteEnable){
					items.push({name:"common.button.delete",classes:'temp',icon:'fa-trash',handler:function(){
						var div=$element.find('div.fileSelected');
						if(div.length>0){
							_self.doDel(div.attr('id'),div);
						}
					}});
				};
				items.push({classes:'separator'},
					{name:"common.button.refresh",icon:'fa-refresh',handler:function(){
						_self.query();
				}});
				//注册右键菜单
				fileListMain.contextMenu({
					width:"100px",
					items: items,
					onOpenMenu:function(m,e){
						var $el = $(e.target || e.srcElement);
						var div=$el.is('div.file')?$el:$el.parent('div.file',$element);
						$("div.file",$element).removeClass('fileSelected');
						if(div.length>0){
							div.addClass('fileSelected');
							m.find('li.temp').removeClass('disabled');
						}else{
							m.find('li.temp').addClass('disabled');
						}
					},
					onSelect:function(){
						this._hideMenu();
					},
					checkEvent:function(){
						return !_self.disabled;
					}
				});
				fileListMain.reLoadMenu(items);
			}catch(e){alert('无法注册右键菜单:'+e.message);}
		},
		//添加提示框
		addTip:function(obj){
			obj=obj||$(this.element).find('div.file');
			var position='top',_self=this;
			if(this.isClass){//控制提示框显示位置
				position='right';
				//小屏幕显示
				if($(window).width() < 768){
					position='top';
				}
			}
			var readOnly=_self.options.readOnly;
			if(_self.disabled){readOnly=true;}
			var bizCode=_self.options.bizCode,bizId=this.options.bizId;
			var isCheck=_self.options.isCheck;
			var showTipEnable=_self.options.showTipEnable;
			var downloadEnable=_self.options.downloadEnable;
			if($.isFunction(downloadEnable)){
				downloadEnable=downloadEnable.call(this);
			}
			obj.each(function(){
				var file=$(this),id=file.attr('id');
				var addFlag=showTipEnable;
				if($.isFunction(addFlag)){
					addFlag=addFlag.call(this,file);
				}
				if(!addFlag){
					return;
				}
				file.find('a').tooltip({position:position,width:300,  
					content:function(){
						var html=['<table class="table-bordered table-fixed" id="tip_',id,'">'];
						html.push('<tr>','<td class="title" style="width:30%;height:25px;">',$.i18nProp('common.button.operate'),':</td><td style="width:70%;">');
						html.push('<div class="operating">');
						if(_self.options.downloadEnable){
							html.push('<a href="javascript:void(0);"  onclick="AttachmentUtil.downFileByAttachmentId(\'', id,'\')">');
							html.push('<i class="fa fa-cloud-download"></i>',$.i18nProp('common.button.download'),'</a>','&nbsp;&nbsp;');
						}
						if(!_self.disabled && _self.options.deleteEnable){
							html.push('<a href="javascript:void(0);" class="aLink ui-icon-trash-link" fileId="',id,'">');
							html.push('<i class="fa fa-trash"></i>',$.i18nProp('common.button.delete'),'</a>','&nbsp;&nbsp;');
						}
						html.push('<a href="javascript:void(0);" class="ui-icon-view-link" fileId="',id,'">');
						html.push('<i class="fa fa-search-plus"></i>',$.i18nProp('common.button.preview'),'</a>','&nbsp;&nbsp;');
						html.push('<a href="javascript:void(0);" class="ui-icon-close-tip"><i class="fa fa-times-circle"></i>',$.i18nProp('common.button.close'),'</a>','&nbsp;&nbsp;');
						html.push('</div>','</td>','</tr>');
						html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.attachment.uploaddate'),':</td><td >',file.attr('createdDate'),'</td>','</tr>');
						html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.attachment.uploader'),':</td><td >',file.attr('createdByName'),'</td>','</tr>');
						html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.attachment.filesize'),':</td><td >',file.attr('fileSize'),'</td>','</tr>');
						if(file.find('span.error').length>0){//文件存在错误
							html.push('<tr>','<td class="title" style="height:25px;">',$.i18nProp('common.field.status'),':</td><td style="color:red;">',$.i18nProp('common.attachment.fileerror'),'</td>','</tr>');
						}
						html.push('</table>');
						return html.join('');
					},
					onShow:function(tip){
						var _tipObj=this;
						file.attr('tipShow',true);
						//删除
						$(tip).on('click.tooltipClick',function(e){
							var $clicked = $(e.target || e.srcElement);
							if($clicked.is('i.fa')){
								$clicked=$clicked.parent();
							}
							if($clicked.hasClass('ui-icon-trash-link')){//删除
								var fileId=$clicked.attr('fileId');
								var fileObj=$('#'+fileId);
								if(fileObj.length>0&&fileObj.hasClass('file')){
									_self.doDel(fileId,fileObj);
									_tipObj.close();
								}
							}else if($clicked.hasClass('ui-icon-close-tip')){//关闭
								_tipObj.close();
								return false;
							}else if($clicked.hasClass('ui-icon-view-link')){//预览
								var fileId=$clicked.attr('fileId');
								var file=$('#'+fileId),fileKind=file.attr('fileKind');
								if($.isMobile()){//手机端通过打开新窗口进行预览
									if($.isIosPhone||AttachmentUtil.isImgForThickbox(fileKind)||AttachmentUtil.isPDFFile(fileKind)){
										Public.openPostWindow(web_app.name +'/outputFile.ajax',{id:fileId});
									}else{
										AttachmentUtil.downFileByAttachmentId(fileId);
									}
								}else{
									if(AttachmentUtil.isImgForThickbox(fileKind)){
										var isChange=_self.isClass?false:true;//分类显示时不用切换图片
										$.thickbox({imgURL:$.getCSRFUrl('attachmentDownFile.ajax',{id:fileId}),isChange:isChange,doChange:function(flag){
											_self.findThickboxImg(id,flag);
										}});
									}else if(AttachmentUtil.isPDFFile(fileKind)){
										Public.openPostWindow(web_app.name +'/outputFile.ajax',{id:fileId});
									}else{
										AttachmentUtil.downFileByAttachmentId(fileId);
									}
								}
								return false;
							}
						});
					},
					onHide:function(tip){
						file.removeAttr('tipShow');
						var parant=file.parent();
						$(tip).off('click.tooltipClick').remove();//删除绑定事件
						/*if(parant.hasClass('groupFileList')){
							setTimeout(function(){
								_self.closeShowAttachmentFilePop(parant);
							},0);
						}*/
					}
				});
			});
		},
		query:function(){
			var op=this.options,url=op.queryUrl,_self=this;
			if(!op.bizCode||!op.bizId) return;
			Public.ajax(url,{bizCode:op.bizCode,bizId:op.bizId}, function(data) {
				if(typeof _self.options.queryBack=='function')
					_self.options.queryBack.call(_self,data);
			});
		},
		doDel:function(id,obj,fn){
			var op=this.options,url=op.deUrl,_self=this;
			var deleteEnable=op.deleteEnable;
			if($.isFunction(deleteEnable)){
				deleteEnable=deleteEnable.call(this,id);
			}
			if(!deleteEnable){
				return false;
			}
			var confirmFlag = window.confirm($.i18nProp('common.confirm.delete.unrecoverable'));
			if(confirmFlag){
				Public.ajax(url,{id:id,isCheck:op.isCheck}, function(data) {
					var div=$(obj).parent();
					if(typeof _self.options.delBack=='function'){
						_self.options.delBack.call(_self,id,obj);
					}
					if($.isFunction(fn)){
						fn.call(window);
					}
					if(div.hasClass('groupFileList')){
						_self.initGroupFileDiv(div);
					}
				});
			}
		},
		doDellAll:function(){
			var op=this.options,url=op.delAllUrl,_self=this;
			var deleteEnable=op.deleteEnable;
			if($.isFunction(deleteEnable)){
				deleteEnable=deleteEnable.call(this,id);
			}
			if(!deleteEnable){
				return false;
			}
			UICtrl.confirm('common.confirm.delete.unrecoverable', function() {
				Public.ajax(url,{bizCode:op.bizCode,bizId:op.bizId,isCheck:op.isCheck}, function(data) {
					if(typeof _self.options.delAllBack=='function')
						_self.options.delAllBack.call(_self,data);
				});
			});
		},
		addFile:function(data,div){
			var fileName=data.fileName,id=data.id;
			if(!fileName||!id) return;
			var html=[],fileHTML=this.options.fileHTML;
			html.push(fileHTML.replace('{id}',data.id)
                    .replaceAll('{fileKind}',data.fileKind)
                    .replace('{createdByName}',data.createdByName)
				    .replace('{fileSize}',data.fileSize)
				    .replace('{title}',data.fileName)
				    .replace('{attachmentCode}',div?'attachmentCode='+div.attr('attachmentCode'):'')
				    .replace('{createdDate}',data.createdDate)
                    .replace('{fileName}',data.fileName));
			var file=$(html.join(''));
			if(!div){
				$(this.element).find('div.fileListMain').append(file);
			}else{//存在指定的div表示采用的分类显示(table布局)
				var isMore=div.attr('isMore');
				if(isMore=='0'){//不允许多个文件
					div.html('');//清除其他文件显示
				}
				//file.prepend('<span class="delFile" title="'+$.i18nProp('common.button.delete')+'">&nbsp;</span>'); 
				div.append(file);
				if(!div.find('.moreList').length){
					div.prepend('<div class="groupFileMore"><a class="btn btn-info btn-xs moreList" title="'+$.i18nProp('common.attachment.more')+'"><i class="fa fa-edit"></i>'+$.i18nProp('common.button.view')+'</a></div>');
				}
				this.initGroupFileDiv(div);
			}
			this.addTip(file);
		},
		findThickboxImg:function(id,flag){
			var div=$(this.element).find('div[id="'+id+'"]');
			if(!div.length) return;
			var self = this,ext,divid;
			div[flag?"nextAll":"prevAll"]().each(function(){
				ext=$(this).attr('fileKind');
				if(AttachmentUtil.isImgForThickbox(ext)){
					divid=$(this).attr('id');
					$(this).trigger('click');
					self.downFile(divid,ext);
					return false;
				}
			});
		},
		downFile:function(id,ext){
			var downloadEnable=this.options.downloadEnable;
			if($.isFunction(downloadEnable)){
				downloadEnable=downloadEnable.call(this);
			}
			if(!downloadEnable){
				AttachmentUtil.onOpenViewFile(id,'','',true);
				return false;
			}
			/***
			 * ext 为点击附件图标时输入，右键菜单点击不传该参数
			 * isImgForThickbox判断待下载的文件是否为图片
			 * 通过$.thickbox 显示图片
			 * **/
			if(AttachmentUtil.isImgForThickbox(ext)&&this.options.showImg){
				//try{
					var self = this,isChange=true;
					if(self.isClass){//分类显示时不用切换图片
						isChange=false;
					}
					$.thickbox({imgURL:$.getCSRFUrl('attachmentDownFile.ajax',{id:id}),isChange:isChange,doChange:function(flag){
						self.findThickboxImg(id,flag);
					}});
				//}catch(e){
				//	AttachmentUtil.downFileByAttachmentId(id);
				//}
			}else{
				AttachmentUtil.downFileByAttachmentId(id);
			}
		},
		bindUploadButton:function(){
			var $el=$(this.element),_self=this;
			if(this.options.readOnly){
				this.disable();
				return;
			}
			var afterUpload=this.options.afterUpload;
			if(this.options.bizId){
				//如果定义了上传按钮
				if(this.options.button){
					var button=$(this.options.button);
					if(button.length>0){
						//2016-1-8 修改为WebUploader上传
						button.removeAttr('disabled').addClass('click_upload');
						AttachmentUtil.registerUploadButton(button,{
							from:$el,filetype:_self.options.filetype||[],
							afterUpload:function(data){
								$el.fileList('addFile',data);
								if($.isFunction(afterUpload)){
									afterUpload.call(window,data);
								}
							}
						});
					}
				}else{
					this.options.button=$el.find('.addFile');
					if(this.options.button.length>0){
						//2016-1-8 修改为WebUploader上传
						this.options.button.show().addClass('click_upload');
						AttachmentUtil.registerUploadButton(this.options.button,{
							from:$el,filetype:_self.options.filetype||[],
							mouseover:function(){
								_self.options.button.addClass('addFileHover');
							},
							mouseout:function(){
								_self.options.button.removeClass('addFileHover');
							},
							afterUpload:function(data){
								$el.fileList('addFile',data);
								if($.isFunction(afterUpload)){
									afterUpload.call(window,data);
								}
							}
						});
					}
					if(this.isClass){
						//分组管理的附件
						var addFieldGroups=$el.find('.addFieldGroup').show();
						if(addFieldGroups.length>0){
							addFieldGroups.each(function(){
								var addEl=$(this);
								var attachmentCode=addEl.attr('attachmentCode');
								var isMore=addEl.attr('isMore');
								var filetype=addEl.attr('filetype');
								if(Public.isBlank(filetype)){
									filetype=_self.options.filetype||[];
								}else{
									filetype=filetype.split(',');
								}
								//2016-1-8 修改为WebUploader上传
								AttachmentUtil.registerUploadButton(addEl,{
									from:$el,filetype:filetype,param:{attachmentCode:attachmentCode,isMore:isMore},
									mouseover:function(){
										addEl.addClass('addFieldGroupHover');
									},
									mouseout:function(){
										addEl.removeClass('addFieldGroupHover');
									},
									afterUpload:function(data){
										var div=$(_self.element).find('div.'+attachmentCode);
										_self.addFile(data,div);
										if($.isFunction(afterUpload)){
											afterUpload.call(window,data);
										}
									}
								});
							});
						}
					}
				}
				this.disabled = false;
			}else{
				this.disable(true);
			}
		},
		initGroupFileList:function(){
			var groups=$(this.element).find('div.groupFileList');
			var _self=this;
			groups.each(function(){
				_self.initGroupFileDiv($(this));
				//2018-02-24 屏蔽 由于自动展开在手机端显示异常
				/*$(this).on('mouseenter',function(){
					if($(this).find('div.file').length<2){//没有文件或只有一个文件不做处理
						return;
					}
					if($(this).hasClass('show-attachment-file-pop-div')){
						_self.clearTimeout($(this));
						return;
					}
					//弹出显示其他附件
					_self.initPopUpDiv($(this));
				}).on('mouseleave',function(){
					if($(this).hasClass('show-attachment-file-pop-div')){
						_self.closeShowAttachmentFilePop($(this));
					}
				});*/
			});
		},
		clearTimeout:function(div){
			var timer=div.data('pop-timer');
			if(timer){
				div.removeData('pop-timer');
				clearTimeout(timer);
			}
			timer = null;
		},
		closeShowAttachmentFilePop:function(div){
			this.clearTimeout(div);
			if(!div.hasClass('show-attachment-file-pop-div')){
				return;
			}
			var _self=this;
			var timer=setTimeout(function(){
				var flag=false;
				//判断提示层是否打开
				div.find('div.file').each(function(){
					var tipShow=$(this).attr('tipShow');
					if(tipShow){
						flag=true;
						return false;
					}
				});
				if(flag){
					return;
				}
				div.addClass('groupFileListMore').removeClass('show-attachment-file-pop-div').width('100%');
				//if($.browser.msie&&$.browser.version<=7){//处理IE7显示异常
					var pop_div=div.data('pop-div');
					var pop_div_parent=div.data('pop-div-parent');
					if(pop_div_parent){
						pop_div_parent.append(div);
						div.removeData('pop-div-parent');
					}
					if(pop_div){
						div.removeData('pop-div');
						pop_div.remove();
					}
					div.unbind('click.popDiv').unbind('dblclick.popDiv');
				//}
				div.parent().height('auto');
				_self.initGroupFileDiv(div);
			},500);
			div.data('pop-timer',timer);
		},
		initPopUpDiv:function(div){
			var _self=this;
			this.clearTimeout(div);
			var timer=setTimeout(function(){
				var offset=div.offset(),height=div.height(),width=div.parent().width()+3;
				div.parent().height(height);
				var parentOf=$(_self.element).offset();
				div.removeClass('groupFileListMore').addClass('show-attachment-file-pop-div').css({
					left:offset.left-parentOf.left-1,
					top:offset.top-parentOf.top,
					width:width
				}).show();
				//if($.browser.msie&&$.browser.version<=7){//处理IE7显示异常
					div.data('pop-div-parent',div.parent());
					var pop_div=$('<div class="ui-attachment-list ui-attachment-pop-div"></div>').appendTo('body');
					div.data('pop-div',pop_div);
					pop_div.css({
						position:'absolute',
						left:offset.left-1,
						top:offset.top-20,
						width:width+1,
						zIndex:10000,
						height:div.height()
					}).empty().show();
					div.appendTo(pop_div).css({position:'static'});
					div.find('div.file').show();
					//xx add 2015-03-11 制度的分组附件数量太多页面无法显示，增加弹出对话框处理
					var dialogViewHtml=['<div class="operating">'];
					dialogViewHtml.push('<a href="javascript:void(0);" class="aLink showDialogView">');
					dialogViewHtml.push('<i class="fa fa-list-alt"></i>&nbsp;');
					dialogViewHtml.push($.i18nProp('common.attachment.dialog.view'));
					dialogViewHtml.push('</a>');
					dialogViewHtml.push('</div>');
					pop_div.prepend(dialogViewHtml.join(''));
					//end 
					$('a.showDialogView',pop_div).on('click',function(){
						_self.showAttachmentDialog(div);
					});
					div.bind('click.popDiv',function(e){
						var $el = $(e.target || e.srcElement);
						if($el.hasClass('delFile')){//删除按钮
							var obj=$el.parent(),id=obj.attr('id');
							_self.doDel(id,obj);//删除
							return;
						}
						e.stopPropagation(); 
						e.preventDefault();
						return false;
					}).bind('dblclick.popDiv',function(e){
						var $el = $(e.target || e.srcElement);
						var div=$el.is('div.file')?$el:$el.parents('div.file');
						if(div.length>0){
							/**div.find('span').attr('class')用于判断是否为图片**/
							_self.downFile(div.attr('id'),div.attr('fileKind'));
						}
						e.stopPropagation(); 
						e.preventDefault();
						return false;
					});
				//}
			},300);
			div.data('pop-timer',timer);
		},
		//打开窗口显示附件
		showAttachmentDialog:function(groupDiv){
			if(!groupDiv.length) return;
			var files=groupDiv.find('div.file');
			var _self=this,readOnly=_self.options.readOnly;
			if(this.disabled){readOnly=true;}
			var bizCode=_self.options.bizCode,bizId=_self.options.bizId;
			var downloadEnable = _self.options.downloadEnable;
			var deleteEnable = _self.options.deleteEnable;
			if($.isFunction(downloadEnable)){
				downloadEnable=downloadEnable.call(this);
			}
			var html=['<div class="ui-attachment-list dom-overflow-auto">'];
			files.each(function(i,o){
				var id=$(this).attr('id'),fileKind=$(this).attr('fileKind'),createdByName=$(this).attr('createdByName'),createdDate=$(this).attr('createdDate');
				html.push('<div class="fileListView" id="dialog',id,'" title="',$.i18nProp('common.attachment.uploader'),':',createdByName,';',createdDate,'">');
				html.push('<div class="operating">')
				if(_self.options.downloadEnable){
					html.push('<a href="javascript:void(0);" class="aLink" onclick="AttachmentUtil.downFileByAttachmentId(\'',id,'\')" title="',$.i18nProp('common.button.download'),'"><i class="fa fa-cloud-download"></i></a>');
				}
				//html.push('<a href="javascript:void(0);" class="aLink" onclick="AttachmentUtil.onOpenViewFile(\'',id,'\',\'',bizCode,'\',\'',bizId,'\','+!downloadEnable+')" title="预览"><i class="fa fa-search-plus"></i></a>');
				if(!_self.disabled && _self.options.deleteEnable){
					html.push('<a href="javascript:void(0);" class="aLink ui-icon-trash" fileId="',id,'"  title="',$.i18nProp('common.button.delete'),'"><i class="fa fa-trash"></i></a>');
				}
				html.push('</div>');
				if(!_self.disabled && _self.options.deleteEnable){
					html.push('<div class="spinner">');
					html.push('<input type="text" class="text" fileId="',id,'"  value="',(i+1)*10,'"/>');
					html.push('</div>');
				}
				html.push('<div class="file"><span class="',fileKind,'">&nbsp;</span>',$.trim($(this).find('a').text()),'</div>');
				html.push('</div>');
			});
			html.push('</div>');
			html.push('<div style="text-align:right;margin-top:5px;">');
			if(!_self.options.readOnly){//添加保存按钮
				html.push('<button type="button" class="btn btn-primary do-save"><i class="fa fa-save"></i>&nbsp;',$.i18nProp('common.button.saveid'),'</button>&nbsp;&nbsp;');
			}
			html.push('<button type="button" class="btn btn-primary do-close"><i class="fa fa-close"></i>&nbsp;',$.i18nProp('common.button.close'),'</button>');
			html.push('</div>');
			var dialog=Public.dialog({title:'文件列表',width:500,opacity:0.1,
				content:html.join(''),
				onClick:function($clicked){
					if($clicked.hasClass('ui-icon-trash')){//删除
						var fileId=$clicked.attr('fileId');
						var file=$('#'+fileId);
						if(file.length>0&&file.hasClass('file')){
							_self.doDel(fileId,file,function(){
								$('#dialog'+fileId).remove();
							});
						}
					}
					if($clicked.hasClass('do-save')){//保存排序号
						var datas={},dialogSelf=this,flag=true;
						dialog.find('input.text').each(function(){
							if($(this).val()==''){
								flag=false;
								Public.tip('common.warning.nosortid');
								$(this).focus();
							}
							datas[$(this).attr('fileId')]=$(this).val();
						});
						if(!flag) return;
						_self.saveSortIDHandler(groupDiv,datas,function(){
							dialogSelf.close();
						});
					}
					if($clicked.hasClass('do-close')){
						this.close();
					}
				}
			});
			//控制弹出框大小
			var _height=dialog.find('div.ui-attachment-list').height();
			if(_height>300){
				dialog.find('div.ui-attachment-list').height(300);
			}
			dialog.find('input.text').spinner({countWidth:40,min:1,max:100}).mask('99',{number:true});
		},
		initGroupFileDiv:function(div){
			var files=div.find('div.file');
			if(files.length>1){
				div.addClass('groupFileListMore');
			}else{
				div.removeClass('groupFileListMore');
			}
		},
		saveSortIDHandler:function(groupDiv,datas,fn){//保存附件排序号
			var url=this.options.saveSortIDUrl,_self=this;
			if(!$.isPlainObject(datas)){
				return;
			}
			if($.isEmptyObject(datas)){
				return
			}
			Public.ajax(url,{data:$.toJSON(datas)}, function(data) {
				//页面数据排序
				 var a=new Array();
			     groupDiv.find('div.file').each(function(i){
			         a.push($(this).attr("id"));
			     });
			     a=a.sort(function(id1,id2){
			     	var x=parseInt(datas[id1] ,10);
			     	var y=parseInt(datas[id2] ,10);
			     	return x == y ? 0 : (x > y ? 1 : -1);
			     });
			     //重新组合显示
			     $.each(a, function(i){
			 		groupDiv.append(jQuery('#'+a[i]));
			 	 });
			 	 if(groupDiv.hasClass('groupFileList')){
					_self.initGroupFileDiv(groupDiv);
				 }
				 if($.isFunction(fn)){
					fn.call(this);
				 }
			});
		},
		enable: function() {
			this.disabled = false;
			var $el=$(this.element);
			$el.find('.addFile').show();
			$el.find('.ui-upload-button-span').show();
			$el.find('.addFieldGroup').show();
			$el.find('.delFile').show();
			$el.find('.addBatchFile').show();
			if(this.options.readOnly){
				this.options.readOnly=false;
				this.bindUploadButton();
			}
			this.bindContextMenu();
		},
		disable: function(flag) {
			this.disabled = true;
			var $el=$(this.element);
			$el.find('.ui-upload-button-span').hide();
			if(!flag){//是否显示上传按钮
				$el.find('.addFile').hide();
				$el.find('.addFieldGroup').hide();
				$el.find('.addBatchFile').hide();
			}
			$el.find('.delFile').hide();
			this.bindContextMenu();
		},
		getOptions:function(){
			return {bizId:this.options.bizId,bizCode:this.options.bizCode,checkNeedMoreSql:this.options.checkNeedMoreSql||''};
		},
		refresh:function(op){
			if(!Public.isBlank(op)){
				this.options=$.extend({},this.options,op);
			}
			this.query();
		}
	});
	
	//初始化附件列表
	$.fn.fileList = function(op){
		var obj=this.data('ui-attachment-list');
		if(!obj){
			new AttachmentManager(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['enable','disable','addFile','refresh','getOptions'],function(i,m){
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
	
	/*************附件预览对象**************/
	var AttachmentPreview=function(el,op){
		this.element=el;
		this.options ={};
		this.set(op);
		this.init();
		$(el).data('ui-attachment-preview',this);
	};
	$.extend(AttachmentPreview.prototype, {
		set:function(op){
			this.options=$.extend({
				appendTo:null,//显示位置
				isReadOnly:false,//是否允许下载打印 可以为函数
				previewParent:null,
				canEdit:false//是否允许在线编辑
			},this.options, op||{});
		},
		init:function(){
			var files=$('div.file',this.element);
			if(!files.length) return;//没有文件不执行操作
			var html=['<div id="previewFileBusinessDiv">'];
			html.push('<input type="hidden" id="previewAttachmentId" />');
			html.push('<input type="hidden" id="previewAttachmentKind" />');
			html.push('<div class="previewToolBar" ></div>');
			html.push('</div>');
			var div=null;
			if(this.options.appendTo){
				div=$(html.join('')).appendTo(this.options.appendTo);
			}else{
				div=$(html.join('')).insertAfter(this.element);
			}
			this.element.hide();//隐藏原来的文件
			this.initToolBar(div,files);
			this.bindEvent(div);
			this.doPreview($(files[0]));//默认显示第一个附件
		},
		setPreviewDivHeight:function(){
			var div=$('#previewFileBusinessDiv');
			var img=$("img:first", div);
			if(img.length>0){
				div.css('height','auto');
			}else{
				var height = UICtrl.getPageSize().h;
				div.height(height-65);
			}
		},
		getPreviewParent:function(){
			var parent=this.options.previewParent;
			if(parent){
				if($.isFunction(parent)){
					parent=parent.call(winodw);
				}
			}else{
				parent=$('#jobPageCenter');
				if(!parent.length) parent=$(document);
			}
			return parent;
		},
		initToolBar:function(div,files){
			var _self=this,parent=this.getPreviewParent();
			var toolBar=$('div.previewToolBar',div);
			var screenOverDiv=$('<div class="ui-tab-loading" style="display:none;z-index: 100; background:#ffffff;">').insertAfter(div);
			toolBar.toolBar([
			     {id:'preview_allView',name:'全屏显示正文',icon:'table', event:function(){
			     	_self.setPreviewDivHeight();//调整显示大小
			     	$("iframe:first", div).show();
			     	$("img:first", div).show();
			     	div.css({position:'absolute',top:'-1px',left:0,width:'100%',zIndex:1002});
			     	//取消父级滚动条
			     	parent.scrollTop(0).css('overflowY','hidden');
			     	//$('#preview_allView').hide();$('#preview_allViewLine').hide();
			     	//$('#preview_noAllView').show();$('#preview_noAllViewLine').show();
			     	screenOverDiv.show();
			     }},
			     {line:true,id:"preview_allViewLine"},	 
			     {id:'preview_noAllView',name:'恢复正常',icon:'tables', event:function(){
			     	_self.setPreviewDivHeight();
			     	$("iframe:first", div).show();
			     	$("img:first", div).show();
			     	div.css({position:'static'});
			        parent.css('overflowY','auto');
			       // $('#preview_noAllView').hide();$('#preview_noAllViewLine').hide();
			       // $('#preview_allView').show();$('#preview_allViewLine').show();
			        screenOverDiv.hide();
			     }},
			     {line:true,id:"preview_noAllViewLine"},	 
			     {id:'preview_hideView',name:'隐藏正文',icon:'delete', event:function(){
			     	div.css({position:'static',height:25});
			     	parent.css('overflowY','auto');
			     	$("iframe:first", div).hide();//隐藏IFRAME
			     	$("img:first", div).hide();
			     	//$('#preview_noAllView').hide();$('#preview_noAllViewLine').hide();
			        //$('#preview_allView').show();$('#preview_allViewLine').show();
			        screenOverDiv.hide();
			     }},
			     {line:true},
			    {id:'preview_refresh',name:'刷新',icon:'refresh', event:function(){
			     	_self.doPreview();//默认显示第一个附件
			     }},
			     {line:true}
			]);
			var canEdit=this.options.canEdit;
			if($.isFunction(canEdit)){
				canEdit=canEdit.call(this);
			}
			if(canEdit){//判断是否允许在线编辑
				toolBar.toolBar('addItem',[
				   	{id:'preview_document',name:'编辑正文',icon:'edit', event:function(){
				     	AttachmentUtil.editFileByAttachmentId($('#previewAttachmentId').val(),$('#previewAttachmentKind').val());
				    }},
				    {line:true}
				]);
			}
			var isReadOnly=this.options.isReadOnly;
			if($.isFunction(isReadOnly)){
				isReadOnly=isReadOnly.call(this);
			}
			if(!isReadOnly){
				toolBar.toolBar('addItem',[
				    {id:'preview_downFile',name:'下载',icon:'down', event:function(){
				     	AttachmentUtil.downFileByAttachmentId($('#previewAttachmentId').val());
				     }},
				     {line:true}
				]);
			}
			
			//$('#preview_noAllView').hide();$('#preview_noAllViewLine').hide();
			if(files.length>1){//存在多个文件增加菜单
				toolBar.toolBar('addItem',[
					  {id:'preview_move',name:'文件列表',icon:'add',event: function(){}},
					  {line:true}
				]);
				var items=[];
				files.each(function(){
					var file=$(this),kind=file.attr('fileKind');
					items.push({name:$.trim(file.find('a').text()),icon:kind,handler:function(){
						_self.doPreview(file);
						if(screenOverDiv.is(':visible')){
							div.css({position:'absolute',top:'-1px',left:0,width:'100%',zIndex:1002});
						}
					}});
					items.push({classes:'separator'});
				});
				var more=$('#preview_move').contextMenu({
					width:"210",
					eventType:'click',
					autoHide:true,
					overflow:function(){
						var of=more.offset(),height=more.height()+2;
						return {left:of.left,top:of.top+height};
					},
					items:items,
					onSelect:function(){
						this._hideMenu();
					}
				});
			}
		},
		addConvertPreviewIframe:function(file,div){
			AttachmentUtil.clearView(div);
			var fileId=file.attr('id');
			var isReadOnly=this.options.isReadOnly;
			if($.isFunction(isReadOnly)){
				isReadOnly=isReadOnly.call(this,file);
			}
			var convertUrl=$('#attachmentConvertUrl').val();
			var method='/attachment.do?method=convertAttachment&attachmentId='+fileId+'&a='+new Date().getTime();
			method+="&wmode=transparent&isReadOnly="+(isReadOnly?"true":"false");//是否只读
			AttachmentUtil.addConvertPreviewFileIFrame(div,convertUrl+method);
			this.setPreviewDivHeight();//调整显示大小
		},
		addImgPreview:function(file,div){
			AttachmentUtil.clearView(div);
			var fileId=file.attr('id');
			var src=$.getCSRFUrl('attachment/downFile.ajax',{id:fileId});
			var pic=$("<img src='"+src+"'  border=0 />").appendTo(div);
			var maxWidth=this.getPreviewParent().width();
		 	UICtrl.autoResizeImage(pic,maxWidth*0.98);
		 	this.setPreviewDivHeight();//调整显示大小
		},
		bindEvent:function(div){
			var _self=this;
			//窗口改变事件
			$(window).resize(function (){
			 	var iframe=$("iframe:first", div);
			 	var pic=$("img:first", div);
			 	if(iframe.length>0&&iframe.is(':visible')){//iframe显示出来才执行大小调整
			 		_self.setPreviewDivHeight();
			 	}
			 	if(pic.length>0){
			 		var maxWidth=_self.getPreviewParent().width();
		 			UICtrl.autoResizeImage(pic,maxWidth*0.98);
			 	}
			});
			_self.getPreviewParent().on('scroll', function () {
				 try{
		        	$('ul.main-contextmenu','body').each(function(){$(this).closeContextMenu().hide();});
		        }catch(e){
		        }
			});
		},
		doPreview:function(file){
			if(!file){
				var files=$('div.file',this.element);
				files.each(function(){
					if($(this).attr('isPreview')){
						file=$(this);
						return false;
					}
				});
			}
			if(!file) return;
			var fileKind=file.attr('fileKind'),div=$('#previewFileBusinessDiv');
			file.attr('isPreview',true);
			if(AttachmentUtil.isImgForThickbox(fileKind)){//图片
				this.addImgPreview(file,div);
			}else{//其他文件
				this.addConvertPreviewIframe(file,div);
			}
			$('#previewAttachmentId').val(file.attr('id'));
			$('#previewAttachmentKind').val(fileKind);
		}
	});
	
	$.fn.filePreview = function(op){
		var obj=this.data('ui-attachment-preview');
		if(!obj){
			new AttachmentPreview(this,op);
		}else{
			obj.set(op);
		}
		return this;
	};
})(jQuery);
		
var AttachmentUtil = AttachmentUtil || {};
/**根据文件后缀判断文件是否为图片**/
AttachmentUtil.isImgForThickbox=function(ext){
	ext=ext||'';
	if(ext==''){
		return false;
	}else {
		if(/^(gif|jpg|jpeg|png|bmp)$/.test(ext.toLowerCase())) {
			return true;
		}
	}
	return false;
};
/**根据文件后缀判断文件是否为office文件**/
AttachmentUtil.isOfficeFile=function(ext){
	ext=ext||'';
	if(ext==''){
		return false;
	}else {
		if(/^(doc|docx|xls|xlsx|ppt|pptx)$/.test(ext.toLowerCase())) {
			return true;
		}
	}
	return false;
};
/**根据文件后缀判断文件是否为excel文件**/
AttachmentUtil.isExcelFile=function(ext){
	ext=ext||'';
	if(ext==''){
		return false;
	}else {
		if(/^(xls|xlsx)$/.test(ext.toLowerCase())) {
			return true;
		}
	}
	return false;
};
/**根据文件后缀判断文件是否为PDF文件**/
AttachmentUtil.isPDFFile=function(ext){
	ext=ext||'';
	if(ext==''){
		return false;
	}else {
		if(/^(pdf)$/.test(ext.toLowerCase())) {
			return true;
		}
	}
	return false;
};
/**双击打开文件**/
AttachmentUtil.onOpenViewFile=function(id,bizCode,bizId,readOnly){
	Public.openPostWindow(web_app.name +'/attachmentPreview.do',{id:id,bizCode:bizCode,bizId:bizId,isReadOnly:readOnly});
};
/**根据附件ID下载文件**/
AttachmentUtil.downFileByAttachmentId=function(id){
	//iphone 无法直接下载，调用预览方法
	if($.isIosPhone){
		Public.openPostWindow(web_app.name +'/outputFile.ajax',{id:id});
	}else{
		var iframe=$('#downFile_hidden_Iframe');
		if(iframe.length==0){ 
			iframe=$('<iframe name="downFile_hidden_Iframe" style="display:none;"></iframe>').appendTo('body');
		}
		iframe[0].src=$.getCSRFUrl('attachmentDownFile.ajax',{id:id});
	}
};
/**编辑正文**/
AttachmentUtil.editFileByAttachmentId=function(id,fileKind){
	// RecordID 文档id,FileType 文档类型,EditType 编辑类型,UserName 用户名
	if(AttachmentUtil.isOfficeFile(fileKind)){
		Public.openPostWindow(web_app.name+'/webOffice/forward.load',{id:id,fileType:fileKind});
	}else{
		Public.tips({type:1,content:'该文件不允许编辑!',time:5000});
	}
};
/**页面预览文件iframe**/
AttachmentUtil.addConvertPreviewFileIFrame=function(convertViewCenter,convertUrl){
	convertViewCenter.css({position:'relative'});
	var convertForPhone=Public.getQueryStringByName("convertForPhone");
	if(convertForPhone==='true'){
		convertUrl+="&convertForPhone=true";
	}
	var iframeloading=$("div.ui-tab-loading", convertViewCenter);
	if(!iframeloading.length){
		iframeloading=$("<div class='ui-tab-loading' style='top:0;'></div>").appendTo(convertViewCenter);
	}
	iframeloading.show();
	var convertViewIFrame = $("iframe:first", convertViewCenter);
	if(convertViewIFrame.length>0){
		try{ 
			convertViewIFrame[0].src = 'about:blank';  
			convertViewIFrame[0].contentWindow.document.write('');//清空iframe的内容
		    convertViewIFrame[0].contentWindow.close();//避免iframe内存泄漏
		}catch(e){
		}  
		convertViewIFrame.remove();
	}
	convertViewIFrame=$("<iframe src='about:blank' style='width:100%;height:100%;' frameborder='0' allowtransparency='true'></iframe>").appendTo(convertViewCenter);
	if(convertUrl!=''){
        var fmState = function() {
			 var state = null;
			 if (document.readyState) {
			      try {
			      	//判断Iframe内document 是否加载完成
			          state = convertViewIFrame[0].contentWindow.document.readyState;
			      } catch(e) {
			          state = null;
			      }
			     if (state == "complete"||state=='interactive') {
			     	 convertViewIFrame[0].className='doc_finish';
					 iframeloading.hide();
					 convertViewIFrame.css('height','100%');
			         return;
			     }
			     window.setTimeout(fmState, 10);
		    }
		};
		convertViewIFrame.css('height',convertViewCenter.height());
		convertViewIFrame.attr("src", convertUrl).bind('load', function () {
			this.className='doc_finish';
			iframeloading.hide();
			convertViewIFrame.css('height','100%');
			if (fmState.TimeoutInt) window.clearTimeout(fmState.timeoutInt);
        });
		if (fmState.TimeoutInt) window.clearTimeout(fmState.timeoutInt);
		fmState.timeoutInt = window.setTimeout(fmState, 400);
	}else{
		iframeloading.hide();
	}
};
AttachmentUtil.clearView=function(div){//清除以前显示的数据
		var convertViewIFrame = $("iframe:first", div);
		if(convertViewIFrame.length>0){
			try{ 
				convertViewIFrame[0].src = 'about:blank';  
				convertViewIFrame[0].contentWindow.document.write('');//清空iframe的内容
				convertViewIFrame[0].contentWindow.close();//避免iframe内存泄漏
			}catch(e){
			}  
			convertViewIFrame.remove();
		}
		var img = $("img:first", div);
		if(img.length>0){
			img.remove();
		}
};
/**注册上传文件按钮**/
AttachmentUtil.registerUploadButton=function(button,op){
	/**
	 * 通过flash上传，IE的内存逐步上涨，每到一定值就不上传文件了，关闭window.open打开的上传文件窗口后，发现内存未释放;
	 * 使用flash，以二进制+分片分片的方式，内存要占用1.5~2倍。
	 * **/
	if($.browser.msie){//ie存在未知错误使用传统上传方式（文件无法上传,内存无法释放,上传文件不完整）
		$(button).uploadButton(op);//传统上传方式
		return;
	}
	try{
		if(!WebUploader.Uploader.support()){//Web Uploader 不支持的浏览器！
			$(button).uploadButton(op);//传统上传方式
			return;
		}
		if(button.is(':input')&&!button.is('button')){
			$(button).uploadButton(op);//传统上传方式
		}else{
			$(button).JQWebUploader(op);//webupload 上传方式
		}
	}catch(e){
		$(button).uploadButton(op);//传统上传方式
	}
};
/**批量上传**/
AttachmentUtil.batchUpload=function(options){
	if($.browser.msie){//ie存在未知错误使用传统上传方式（文件无法上传,内存无法释放,上传文件不完整）
		Public.tip('上传控件不支持您的浏览器!');
		return;
	}
	if(!WebUploader.Uploader.support()){//Web Uploader 不支持的浏览器！
		Public.tip('上传控件不支持您的浏览器!');
		return;
	}
	options=options||{};
	if(!options['parent']){
		$.dialogMaskIndex();
	}
	UICtrl.showFrameDialog({
        url: web_app.name + "/webUpload/forwardBatchUpload.do",
		param: options.params,
        title: options.title,
        width: 520,
        height:400,
        ok: false,
        close: options.closeHandler,
        parent: options.parent,
        lock: typeof options['lock'] == 'undefined' ? true : options['lock'],
        cancelVal: 'common.button.close',
        cancel: true
    });
};