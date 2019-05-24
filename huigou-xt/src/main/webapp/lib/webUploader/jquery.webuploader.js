/*--------------------------------------------------------------------*\
|  title:         上传按钮对webUploader进行封装                                                                                       |
|  Author:        xx                                                  |
|  LastModified:  2016-01-05                                          |
|  $('#picker').JQWebUploader({param:{bizId:123456}});                |
\*--------------------------------------------------------------------*/
//注册事件
WebUploader.Uploader.register({
   "before-send-file": "beforeSendFile",
   "before-send": "beforeSend",
   "after-send-file": "afterSendFile"
}, {
    beforeSendFile: function(file){
    	var uploader=this['owner'];
    	var jqWebUp=uploader['JQWebUploader'];
    	var param=jqWebUp['getParam']();
        //秒传验证
        var task = new $.Deferred();
        var data = $.extend(true, JQWebUploader.getFileParam(file),param);
        $.ajax({type: "POST", url: JQWebUploader.getBackEndUrl('md5Check'), cache: false, dataType: "json", 
        	timeout: 1000, //TODO 超时的话，只能认为该文件不曾上传过
        	data:data
        }).then(function(data, textStatus, jqXHR){
        	Public.ajaxCallback(data, function(val){//后台调用成功
        		 task.resolve();
                 //拿到上传文件的唯一名称，用于断点续传
                 jqWebUp['uniqueFileName']=val;
        	}, function(){//后台返回错误
        		//返回失败给WebUploader，表明该文件不需要上传
        		 task.reject();
        		 uploader.removeFile(file.id);
        	});
        },function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败文件不需要上传
        	task.reject();
   		 	uploader.removeFile(file.id);
        });
        return $.when(task);
    },
    beforeSend: function(block){
    	var jqWebUp=this['owner']['JQWebUploader'];
    	var uniqueFileName=jqWebUp['uniqueFileName'];
        //分片验证是否已传过，用于断点续传
        var task = new $.Deferred();
        $.ajax({type: "POST", url:  JQWebUploader.getBackEndUrl('chunkCheck'), data: {
        	  uniqueName: uniqueFileName,
              chunkIndex: block.chunk,
              size: block.end - block.start
            },
            cache: false,
            timeout: 1000, //todo 超时的话，只能认为该分片未上传过
            dataType: "json"
        }).then(function(data, textStatus, jqXHR){
            if(data.ifExist){   //若存在，返回失败给WebUploader，表明该分块不需要上传
                task.reject();
            }else{
                task.resolve();
            }
        }, function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败，都触发重新上传
            task.resolve();
        });
        return $.when(task);
    },
    afterSendFile: function(file){
    	var uploader=this['owner'];
    	var jqWebUp=uploader['JQWebUploader'];
    	var uniqueFileName=jqWebUp['uniqueFileName'];
    	var chunkSize=JQWebUploader.chunkSize;
        var chunksTotal = 0;
        if((chunksTotal = Math.ceil(file.size/chunkSize)) > 1){
        	var param=jqWebUp['getParam']();
        	var data = $.extend({uniqueName: uniqueFileName,chunks: chunksTotal}, JQWebUploader.getFileParam(file),param);
            //合并请求
            var task = new $.Deferred();
            $.ajax({
            	type: "POST", url: JQWebUploader.getBackEndUrl('chunksMerge'), cache: false,dataType: "json", data: data
            }).then(function(data, textStatus, jqXHR){
                task.resolve();
                //检查响应是否正常
                Public.ajaxCallback(data, function(param){//后台调用成功
                	  jqWebUp.uploadSuccess(param);
	           	}, function(){//后台返回错误
	           		//返回失败给WebUploader，表明该文件不需要上传
	           		 task.reject();
	           		 uploader.removeFile(file.id);
	           	});
                file = null;
            }, function(jqXHR, textStatus, errorThrown){
                task.reject();
            });
            return $.when(task);
        }
    }
});
var JQWebUploader=function($el,options) {
	 this.options={};
	 this.element=$el;
	 this.set(options);
	 this.init();
	 this.uniqueFileName=null;//文件唯一标识符
	 $el.data('JQWebUploader',this);
};

JQWebUploader.chunkSize=10 * 1024  * 1024;//分块大小
JQWebUploader.fileSingleSizeLimit=1024 * 1024  * 1024;//设定单个上传文件大小限制

//创建上传 WebUploader 对象
JQWebUploader.createWebUploader=function(element){
	var uploader =JQWebUploader['webUploader'];
	if(!uploader){
		uploader = WebUploader.create({
			swf: web_app.name+"/lib/webUploader/Uploader.swf",
			server:$.getCSRFUrl('webUpload/ajaxUpload.ajax'),
			pick: {id:element,multiple:false},// 是否开起同时选择多个文件能力。 false 只能选择一个文件
			resize: false,
			disableGlobalDnd: true,
            compress: false,
			prepareNextFile: true,
			chunked: true, //开启分块上传
			chunkSize:JQWebUploader.chunkSize,//分块大小
			threads: true,
			fileNumLimit: 1,//上传数量限制
			fileSizeLimit: JQWebUploader.fileSingleSizeLimit,//最大文件大小
			fileSingleSizeLimit: JQWebUploader.fileSingleSizeLimit,//限制上传单个文件大小
			duplicate: true,
			auto: true
		});
		//设置对应的上传按钮对象[  WebUploader 无此方法 webUploader.js 1934行添加方法 ]
		uploader.on("beforeSelectFile",function(files,button){
			var jqWebUp=$.data(button,'JQWebUploader');
			this['JQWebUploader']=jqWebUp;
			//动态设置fromData属性
			this.options.formData=jqWebUp.getParam();
		});
		//当文件被加入队列之前触发，此事件的handler返回值为false，则此文件不会被添加进入队列
		uploader.on("beforeFileQueued", function(file){
			var d=file.lastModifiedDate,lastModifiedDate;
			try{
				lastModifiedDate = d.getFullYear()+''+(d.getMonth()+1)+''+d.getDate()+''+d.getHours()+''+d.getMinutes()+''+d.getSeconds();
			}catch(e){
				lastModifiedDate = d+'';
			}	
			file.lastModifiedDate=lastModifiedDate;
			var jqWebUp=this['JQWebUploader'];//上传按钮对象
			var flag=jqWebUp.beforeFileQueued(file);
			return flag;
		});
		//当文件被加入队列以后触发。
		uploader.on("fileQueued", function(file){
			var screenOver=$('#JQWebUploader-screen-over');
			if(!screenOver.length){
				screenOver=$('<div id="JQWebUploader-screen-over" style="position:absolute;top:0px;left:0px;width:0;height:0;z-index:10000;display:none;"></div>').appendTo('body');
			}
			//打开上传提示层
			screenOver.css({width:'100%',height:'100%',background:'#001',filter:'alpha(opacity=0)',opacity:0}).show();
			var div=$('<div id="JQWebUploader'+file.id+'" class="JQWebUploader-loading-div"></div>').appendTo('body');
			var html=['<div class="JQWebUploader-title">',$.i18nProp('common.attachment'),':',file.name,'(',JQWebUploader.renderSizeView(file.size),')</div>'];
			html.push('<div><span class="JQWebUploader-item-upload">',$.i18nProp('common.button.upload'),'</span><span class="JQWebUploader-item-stop">',$.i18nProp('common.button.suspended'),'</span><span class="JQWebUploader-item-del">',$.i18nProp('common.button.cancel'),'</span><div>');
			html.push('<div class="webuploader-progress">&nbsp;',$.i18nProp('common.attachment.loading'),'</div> ');
			div.html(html.join(''));
			//注册事件处理暂停及取消
			div.on('click',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.hasClass('JQWebUploader-item-upload')){//上传
					uploader.upload();
					$clicked.hide();
					$("span.JQWebUploader-item-stop",this).show();
				}else if($clicked.hasClass('JQWebUploader-item-stop')){//暂停
					uploader.stop(true);
					$clicked.hide();
					$("span.JQWebUploader-item-upload",this).show();
				}else if($clicked.hasClass('JQWebUploader-item-del')){//删除
					try {
						uploader.removeFile(file.id);
					} catch (e) {
					}
					JQWebUploader.uploadComplete(file);
				}
				return false;
			});
		});
		//上传过程中触发，携带上传进度。
		uploader.on("uploadProgress", function(file, percentage){
			var progress=(percentage*100).toFixed(2);
			var progress_node_id='JQWebUploader'+file.id;
			var div=$('#'+progress_node_id).find('div.webuploader-progress');
			var span=div.find('span');
			if(!span.length){
				div.html('');
				span=$('<span></span>').appendTo(div);
			}
			div.find('span').css("width", progress+ "%"); 
			div.find('span').html(progress + "%");
			if(parseInt(percentage,10)==1){
				//文件合并中...
				setTimeout(function(){div.find('span').html($.i18nProp("common.attachment.filemerge"));},1000);
			}
		});
		//上传成功
		uploader.on("uploadSuccess", function(file,data){
			var jqWebUp=this['JQWebUploader'];//上传按钮对象
			this['JQWebUploader'] =null;
			if(!data||!$.isPlainObject(data)) return; //没有返回数据不处理
			Public.ajaxCallback(data, function(param){//后台调用成功
				if(!param) return;
          	    if($.isPlainObject(param)){//返回纯粹的对象代表直接上传成功
          	    	jqWebUp.uploadSuccess(param);//上传
          	    }
         	});
		});
		//不管成功或者失败，文件上传完成时触发。
		uploader.on("uploadComplete", function(file){
			//删除已上传的文件
			this.removeFile(file.id);
			JQWebUploader.uploadComplete(file);
		});
		uploader.on("uploadError", function(file,msg){
		
		});
		uploader.on("error", function(kind){
			if(kind=='F_EXCEED_SIZE'||kind=='Q_EXCEED_SIZE_LIMIT'){
				//选择的文件太大("'+JQWebUploader.renderSizeView(1024 * 1024 * 1024)+'")无法上传!
				Public.tips({type:1, content : $.i18nProp('common.attachment.filesize.error',JQWebUploader.renderSizeView(JQWebUploader.fileSingleSizeLimit))});
			}
		});
		JQWebUploader['webUploader']=uploader;
	}else{//添加新按钮
		uploader.addButton({id:element});
	}
};
//完成上传关闭提示层
JQWebUploader.uploadComplete=function(file){
	//关闭提示层
	var screenOver=$('#JQWebUploader-screen-over');
	screenOver.hide();
	var div=$('#JQWebUploader'+file.id);
	div.removeAllNode();
};
//后台访问路径
JQWebUploader.getBackEndUrl=function(method){
	return web_app.name+'/webUpload/'+method+'.ajax';
};
//文件参数
JQWebUploader.getFileParam=function(file){
	return {
		id: file.id,
        name: file.name,
        type: file.type,
        lastModifiedDate:file.lastModifiedDate,
        size: file.size,
        ext:file.ext
	}
};
//文件大小显示格式转换
JQWebUploader.renderSizeView=function(value){
	  if(null==value||value==''){
	    return "0 Bytes";
	  }
	  var unitArr = new Array("Bytes","KB","MB","GB","TB","PB","EB","ZB","YB");
	  var index=0,quotient =  parseFloat(value);
	  while(quotient>=1024){
	    index +=1;
	    quotient=quotient/1024;
	  }
	  var tempNumber = parseInt((quotient * Math.pow(10,2)+0.5))/Math.pow(10,2);   
	  return tempNumber+" "+unitArr[index];
};

$.extend(JQWebUploader.prototype, {
	set:function(op){
		this.options=$.extend({
			param:{},//参数
			filetype:[],//可上传文件名
			getParam:null,
			afterError:function(str) {//上传出错处理
				Public.errorTip(str);
			},
			from:false,//标记添加附件列表
			onInit:false,//初始化后执行
			afterUpload:false,//上传成功处理
			beforChoose:false//选择文件前判断是否允许上传
		},this.options, op||{});
	},
	getParam:function(){
		var options = this.options;
		var param = options.param || {},gp={};
		if($.isFunction(options.getParam)){
			gp=options.getParam.call(this);
		}
		return $.extend(true,param,gp);
	},
	init : function() {
		var options = this.options,_self = this;
		var from = options.from||false,param= options.param || {};
		if(from){
			param=$.extend(true,$(from).fileList('getOptions'),param);
			if(!param['bizId']||param['bizId']==''){Public.tip('bizId为空,不能执行上传!');return;}
			options.afterUpload=options.afterUpload||function(data){
				$(from).fileList('addFile',data);
			};
		}
		options.param=param;
		JQWebUploader.createWebUploader(this.element[0]);
		this._setUploaderButtonStyle();
	},
	_setUploaderButtonStyle:function(){//设置按钮显示样式
		  var height=this.element.outerHeight(),width=this.element.outerWidth();
		  var pick=this.element.find('div.webuploader-pick');
		  pick.css({width:width,height:height}).show();
		  var options = this.options;
		  if($.isFunction(options.onInit)){
			   options.onInit.call(this);
		  }
	},
	beforeFileQueued:function(file){
		var flag=true, options = this.options;
		var ext=file.ext;//取文件后缀
		var type=options.filetype.join(',').toLowerCase();
		var reg = new RegExp("(^|,)"+ext.toLowerCase()+"(,|$)", "ig");
		if(type!=''&&!reg.test(type)){//判断文件后缀是否合法
			//'只能上传'+type+'类型文件!'
			Public.tip('common.attachment.check.type',type);
			return false;
		}
		if($.isFunction(options.beforChoose)){
			flag=options.beforChoose.call(this, file);
		}
		return flag;
	},
	uploadSuccess:function(param){
		var options = this.options;
		if($.isFunction(options.afterUpload)){
			options.afterUpload.call(this, param);
		}
	}
});
(function($) {
	$.fn.JQWebUploader = function(op){
		//element选择为<button>时，Firefox无法选择文件
		return this.each(function() {
			var obj=$.data(this,'JQWebUploader');
			if(!obj){
				new JQWebUploader($(this),op);
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
})(jQuery);