/*--------------------------------------------------------------------*\
|  title:         文件批量上传基于对webUploader的封装                                                                           |
|  Author:        xx                                                  |
\*--------------------------------------------------------------------*/
var uploadFinished=false;//上传成功标志
var inputParams = {
	filetype: "",
	bizId: "",
	bizCode: "",
	attachmentCode: "",
	backurl:""
};

var JQWebUploader = JQWebUploader || {};

JQWebUploader.chunkSize=10 * 1024  * 1024;//分块大小

JQWebUploader.fileNumLimit=15;//允许文件个数

JQWebUploader.fileSingleSizeLimit= 1024 * 1024 * 1024;//允许单文件大小

//文件唯一标示符
JQWebUploader.UniqueFileNameMap={};

JQWebUploader.putUniqueFileName=function(fileId,uniqueFileName){
	JQWebUploader.UniqueFileNameMap[fileId]=uniqueFileName;
};

JQWebUploader.getUniqueFileName=function(fileId){
	return JQWebUploader.UniqueFileNameMap[fileId];
};

//校验文件扩展名
JQWebUploader.checkFileType=function(file){
	var uploadFileType=inputParams.filetype;
	if(Public.isBlank(uploadFileType)){
		 uploadFileType=$('#uploadFileType').val();
	}
	var ext=file.ext;
	var type=uploadFileType.toLowerCase();
	var reg = new RegExp("(^|,)"+ext.toLowerCase()+"(,|$)", "ig");
	if(type!=''&&!reg.test(type)){//判断文件后缀是否合法
		return false;
	}
	return true;
};

//完成上传关闭提示层
JQWebUploader.uploadComplete=function(file){

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
//文件上传参数
JQWebUploader.getUploadParam=function(){
	return inputParams;
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

JQWebUploader.ajaxCallback=function(data,successFun,errorFun){
	var status = parseInt(data.status,10);
	switch (status) {
	   case 0 :
	   case 1:
		   if($.isFunction(successFun)){
			   successFun.call(window,data.data);
		   }
		   break;
	   case 2:
	   case 3:
		   if($.isFunction(errorFun)){
			   errorFun.call(window,data.message);
		   }
		    break;
	   default :
		   if($.isFunction(errorFun)){
			   errorFun.call(window,'错误的数据状态!');
		   }
	   return;
	}
};

WebUploader.Uploader.register({
   "before-send-file": "beforeSendFile",
   "before-send": "beforeSend",
   "after-send-file": "afterSendFile"
}, {
    beforeSendFile: function(file){
    	onBeforeSendFile(file);
    	var uploader=this['owner'];
    	var param=JQWebUploader.getUploadParam();
        //秒传验证
        var task = new $.Deferred();
        var data = $.extend(true, JQWebUploader.getFileParam(file),param);
        $.ajax({type: "POST", url: JQWebUploader.getBackEndUrl('md5Check'), cache: false, dataType: "json", 
        	timeout: 1000, //todo 超时的话，只能认为该文件不曾上传过
        	data:data
        }).then(function(data, textStatus, jqXHR){
        	JQWebUploader.ajaxCallback(data, function(val){//后台调用成功
        		 task.resolve();
                 //拿到上传文件的唯一名称，用于断点续传
        		 JQWebUploader.putUniqueFileName(file.id,val);
        	}, function(msg){//后台返回错误
        		//返回失败给WebUploader，表明该文件不需要上传
        		 task.reject();
        		 uploader.removeFile(file.id);
        		 $("#" + file.id + " .percentage").html('<font color="red">'+msg+'<font>');
        	});
        },function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败文件不需要上传
        	task.reject();
   		 	uploader.removeFile(file.id);
        });
        return $.when(task);
    },
    beforeSend: function(block){
    	var uniqueFileName=JQWebUploader.getUniqueFileName(block.file.id);
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
    	var uniqueFileName= JQWebUploader.getUniqueFileName(file.id);
    	var chunkSize=JQWebUploader.chunkSize;
        var chunksTotal = 0;
        if((chunksTotal = Math.ceil(file.size/chunkSize)) > 1){
        	var param=JQWebUploader.getUploadParam();
        	var data = $.extend({uniqueName: uniqueFileName,chunks: chunksTotal}, JQWebUploader.getFileParam(file),param);
            //合并请求
            var task = new $.Deferred();
            $.ajax({
            	type: "POST", url: JQWebUploader.getBackEndUrl('chunksMerge'), cache: false,dataType: "json", data: data
            }).then(function(data, textStatus, jqXHR){
                task.resolve();
                //检查响应是否正常
                JQWebUploader.ajaxCallback(data, function(param){//后台调用成功
	           	}, function(msg){//后台返回错误
	           		//返回失败给WebUploader，表明该文件不需要上传
	           		 task.reject();
	           		 uploader.removeFile(file.id);
	           		 $("#" + file.id + " .percentage").html('<font color="red">'+msg+'<font>');
	           	});
                file = null;
            }, function(jqXHR, textStatus, errorThrown){
                task.reject();
            });
            return $.when(task);
        }
    }
});
$(document).ready(function() {
	initShowUploadInfo();
	//取消页面滚动条
	$('html').addClass("html-body-overflow");
	//获取业务参数
	for (var item in inputParams) {
        inputParams[item] = Public.getQueryStringByName(item);
    }
	//初始化上传对象
	try{
		initWebUploader();
	}catch(e){
		alert('您的浏览器不支持批量上传！');
		return false;
	}
});

function initWebUploader(){
	//注册上传对象
	var uploader = WebUploader.create({
		swf: web_app.name+"/lib/webUploader/Uploader.swf",
		server : $.getCSRFUrl('webUpload/ajaxUpload.ajax'),
		pick: {id:"#picker",multiple:true},// 是否开起同时选择多个文件能力。 false 只能选择一个文件
		resize: false,
		dnd: "#uploaderFileList",
		disableGlobalDnd: true,
        compress: false,
		prepareNextFile: true,
		chunked: true,
		chunkSize:JQWebUploader.chunkSize,
		threads: true,
		fileNumLimit: JQWebUploader.fileNumLimit,
		fileSingleSizeLimit:JQWebUploader.fileSingleSizeLimit,
		duplicate: false,
		formData:JQWebUploader.getUploadParam()//参数
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
		return true;
	});
	uploader.on("fileQueued",function(file) {
		var chekFlag=JQWebUploader.checkFileType(file);
		var html=['<li id="',file.id,'">'];
        html.push('<span>',file.name,'(',JQWebUploader.renderSizeView(file.size),')</span>&nbsp;');
        html.push('<span class="itemDel">',$.i18nProp("common.button.delete"),'</span>');
        if(chekFlag){
              html.push('<div class="percentage"></div>');
        }else{//文件类型校验错误
        	 html.push('<div class="errorMsg">',$.i18nProp("common.attachment.filetype.error"),'</div>');
        	 uploader.removeFile(file.id);
        }
        html.push('</li>');
	    $("#uploaderFileList").append(html.join(''));
	});
	/**************upload 事件*****************/
	uploader.on("uploadProgress", function(file, percentage){
		 var progress=(percentage*100).toFixed(2);
		 var div=$("#" + file.id + " .percentage");
		 if(!div.hasClass('uploader-progress')){
			 div.addClass('uploader-progress');
			 div.html('<span></span>');
		 }
		 div.find('span').css("width", progress+ "%"); 
		 div.find('span').html(progress + "%");
		 if(parseInt(percentage,10)==1){
			 uploadFinished=true;//标示有文件上传成功
			 setTimeout(function(){
				$("#" + file.id).find('.itemDel').hide();
				div.removeClass('uploader-progress').html("<font color='green'>"+$.i18nProp("common.attachment.upload.success")+"</font>");
			 },1000);
		 }
	});
	
	//上传成功
	uploader.on("uploadSuccess", function(file,data){
		JQWebUploader.ajaxCallback(data,function(param){
			if(!param) return;
     	    if($.isPlainObject(param)){//返回纯粹的对象代表直接上传成功
     	    }
		},function(msg){
			$("#" + file.id + " .percentage").html('<font color="red">'+msg+'<font>');
		});
	});
	uploader.on("uploadError", function(file,msg){
		 $("#" + file.id + " .percentage").html('<font color="red">'+msg+'<font>');
	});
	
	uploader.on("error", function(kind){
		if(kind==='F_EXCEED_SIZE'){
			//选择的文件太大("'+JQWebUploader.renderSizeView(JQWebUploader.fileSingleSizeLimit)+'")无法上传!'
			Public.tips({type:1, content : $.i18nProp('common.attachment.filesize.error',JQWebUploader.renderSizeView(JQWebUploader.fileSingleSizeLimit))});
		}else if(kind==='Q_EXCEED_NUM_LIMIT'){
			//选择的文件超过允许的个数!
			Public.tips({type:1, content : $.i18nProp('common.attachment.filenumber.error')});
		}
	});
	
	uploader.on("uploadFinished", function(){
		 $('#keepUpload').show();//展示继续上传按钮
		 $("#stopUpload").hide();
		 $("#startUpload").hide();
	});
	
	/********************页面按钮事件************************/
	//"上传"-->"暂停"
	$('#startUpload').on('click',function(e){
		var files=uploader.getFiles('inited','interrupt','progress','queued');//获取可以上传的文件数量
		//file.getStatus() 
		if(!files||files.length==0){
			//请选择需要上传的文件!
			Public.tip('common.attachment.file.empty');
			return false;
		}
		uploader.upload();
	    $(this).hide();
	    $("#stopUpload").show();
	    $('div.itemDel').hide();//删除钮隐藏
	});
	
	//"暂停"-->"上传"
	$('#stopUpload').on('click',function(e){
		uploader.stop(true);
	    //"暂停"-->"上传"
	    $(this).hide();
	    $("#startUpload").show();
	    $('div.itemDel').show();
	});
	
	//如果要删除的文件正在上传
	$("#uploaderFileList").on("click", ".itemDel",function() {
	    uploader.removeFile($(this).parent().attr("id")); //从上传文件列表中删除
	    $(this).parent().remove(); //从上传列表dom中删除
	});
	
	//继续上传 刷新本页
	$("#keepUpload").on("click",function() {
		//location.reload();//刷新
		uploader.reset();
		$("#uploaderFileList").html('');
		 $('#keepUpload').hide();//展示继续上传按钮
		 $("#stopUpload").hide();
		 $("#startUpload").show();
	});
}

//文件开始上传前处理显示问题
function onBeforeSendFile(file){
	 var li=$("#" + file.id),div=$("#" + file.id + " .percentage");
	 div.html('<font color="#aaaaaa">'+$.i18nProp('common.attachment.loading')+'<font>');
	//控制滚动条位置
	 var fileListDiv=$("#uploaderFileList");
	 var position=li.position();
	 var viewTop = fileListDiv.scrollTop();
	 var itemTop = viewTop + position.top;
	 var viewBottom = viewTop + fileListDiv.height();
	 var itemBottom = itemTop + li.outerHeight();
	 if(itemTop < viewTop || itemBottom > viewBottom){
		 fileListDiv.scrollTop(itemTop);
	 }
}

function initShowUploadInfo(){
	var uploadFileType=inputParams.filetype;
	if(Public.isBlank(uploadFileType)){
		 uploadFileType=$('#uploadFileType').val();
	}
	$('#showUploadFileType').html($.i18nProp('common.attachment.check.type',uploadFileType));
	var html=[];
	//允许选择文件<font color=red>[',JQWebUploader.fileNumLimit,'个]</font>,
	html.push($.i18nProp('common.attachment.filenumber',JQWebUploader.fileNumLimit));
	//'单文件限制大小<font color=red>[',JQWebUploader.renderSizeView(JQWebUploader.fileSingleSizeLimit),']</font>!'
	html.push($.i18nProp('common.attachment.filesize.check',JQWebUploader.renderSizeView(JQWebUploader.fileSingleSizeLimit)));
	$('#showUploadInfo').html(html.join(''));
}