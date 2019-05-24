/*********************************************************
title:     弹出层显示图片，根据图片及窗口大小调整显示比例
Author:  xx
使用:   $.thickbox({imgURL:url});
增加：   $.fn.photoGallery  图片查看器实现图片缩放、旋转等功能
**********************************************************/
(function($) {
	
	$.thickbox = function(options){
		return new JThickbox(options);
	};
	$.thickboxClose = function(){
		$('#thickbox_icon_close').trigger('click');
	};
	
    var JThickbox=function(options) {
		var _self=this,_op=$.extend({
			isScreen:true,//是否显示遮罩
			screenColor:'#001',    //遮罩透明色
			screenAlpha:0.1,    //遮罩透明度
			width:300,
			height:200,
			doChange:false,
			onClick:false,
			isChange:false,
			html:'',
			imgURL:'',
			isImg:true//是否用于显示图片
		},options||{});
		this.init(_op);
		_self.timeouts=null;
		$.WinReszier.register(function(){
			//对话框可见时
			if($('#thickbox_main_div').is(':visible')){
				if(!_self.timeouts){
					_self.timeouts=setTimeout(function(){$.thickbox(options);},100);
				}
			}
		});
	};
    $.extend(JThickbox.prototype, {
		init : function(options) {
			this.options = options;
			if(this.options.isImg&&this.options.html==''&&this.options.imgURL!=''){
				this.options.html='<img class="viewImg" src="'+this.options.imgURL+'"/>';
			}
			var _div=$('#thickbox_main_div');
			//先删除再重建对话框
			if(_div.length > 0) {
				$.thickboxClose();
			}
			var html = ['<div class="ui-thickbox" id="thickbox_main_div">',
						'<span class="icon_close" title="关闭" id="thickbox_icon_close"></span>',
						'<span class="nav prev" title="上一张"></span>',
						'<span class="nav next" title="下一张"></span>',
					    '<div class="content">',
						this.options.html,
						'</div></div>'];
			this.div = $(html.join('')).appendTo('body');
			var self = this;
			if(this.options.isChange){this.div.add($('span.nav',this.div)).hover(function(){$(this).addClass('hover');},function(){$(this).removeClass('hover');});}
			this.div.bind('click',function(e){
				var $clicked = $(e.target || e.srcElement);
				if($clicked.hasClass('icon_close')){
					self.remove();
				}else if($clicked.hasClass('nav')){
					if($.isFunction(self.options.doChange)){
						self.options.doChange.call(window,$clicked.hasClass('next'));
					}
				}
				if($.isFunction(self.options.onClick)){
					self.options.onClick.call(self,e);
				}
				e.preventDefault();
				e.stopPropagation();
				return false;
			});
			this.show();
		},
		show:function(){
			var screenOver=$('#Jquery_ScreenOver');
			if(!screenOver.length){
				screenOver=$('<div id="Jquery_ScreenOver" style="position:fixed;top:0px;left:0px;width:100%;height:100%;z-index:1000;display:none;"></div>').appendTo('body');
			}
			var d = $(document),w=$.windowWidth(),h=$.windowHeight(),mt=d.scrollTop(),ml=d.scrollLeft();
			if(this.options.isImg){//判断图片大小是否操作显示区域,修改显示大小(按比例调整)
				var  _obj=this,img = new Image();// 创建对象
				img.src = this.options.imgURL;// 改变图片的src
				var isLoad=false;
				var check = function(){
				    if(img.width>0 || img.height>0){// 只要任何一方大于0,表示已经服务器已经返回宽高
				    	setTimeout(showImg,0);
				    }
				};
				_obj.set = setInterval(check,40);// 定时执行获取宽高
				img.onload = function(){
					setTimeout(showImg,0);
				};
				img.onerror = function(){//图片不存在时显示默认
					img.width='80';
					img.height='80';
					img.onerror=null;
					setTimeout(showImg,0);
				};
				var showImg=function(){//显示区域大小
					if(isLoad) return;
					var imgHeight = img.height,
						imgWidth = img.width,
						ratio  = imgWidth / imgHeight,
						wH = 415,wW = 615,
						winHeight,winWidth,
						maxHeight = h*0.95,
						maxWidth = w*0.95;
				
					winWidth = Math.max(wW, imgWidth);
					winHeight = Math.max(wH, imgHeight);
	
					if(winWidth > maxWidth) {
						winWidth = maxWidth;
						winHeight =Math.max(wH, Math.ceil(winWidth / ratio));
						if(imgWidth > winWidth) {
							imgWidth = winWidth;
							imgHeight = Math.ceil(imgWidth / ratio);
						}				
					}
					
					if(winHeight > maxHeight) {
						winHeight = maxHeight;
						winWidth = Math.max(wW, Math.ceil(winHeight * ratio));
						if(imgHeight > winHeight) {
							imgHeight = winHeight;
							imgWidth = Math.ceil(imgHeight * ratio);
						}		 
					}
					if(_obj.div && _obj.div.length > 0){
						_obj.div.find('img.viewImg').css({width:imgWidth,height:imgHeight});
						_obj.div.css({width:winWidth,height:winHeight,top: (h - winHeight+mt) /2,left:(w - winWidth+ml) /2}).show();
						isLoad=true;
						setTimeout(function(){
							//注册图片预览控件
							_obj.div.photoGallery();
						},10);
					}
					clearInterval(_obj.set);
				};
			}else{
				var width=this.options.width,height=this.options.height;
		        var diagtop = (h-height+mt)/2,diagleft = (w-width+ml)/2;
				this.div.css({width:width,height:height,top:diagtop,left:diagleft}).show();
			}
			if(this.options.isScreen){
				screenOver.css({
					background:this.options.screenColor,
					filter:'alpha(opacity='+(this.options.screenAlpha*100)+')',
					opacity:this.options.screenAlpha
				}).show();
			}
		},
		remove : function() {
			clearInterval(this.set);
			if(this.div){
				this.div.unbind('click');
				this.div.remove();
			}
			$('#Jquery_ScreenOver').hide();
			this.div = null;
		}
	});
    
    $.fn.photoGallery = function (op){
		var obj=this.data('ui_photo_gallery');
		if(!obj){
			new photoGallery(this,op);
		}
		return this;
    };
    
    //图片查看器
    function photoGallery(el,op){
    	this.options={};
		this.element=$(el);
		var isFirefox=navigator.userAgent.indexOf("Firefox") > -1 ;
		this.mousewheelEvent=isFirefox ? "DOMMouseScroll" : "mousewheel";
		this.set(op);
		this.element.addClass('ui-gallery');
		this.element.data('ui_photo_gallery',this);
		this.init();
		this.bindEvent();
    }
    
    $.extend(photoGallery.prototype,{
		set:function(op){
			this.options=$.extend( {
	      		//图片缩放倍率
	 	 		ratio : 1.2, 
	 	 		//右下角缩略图宽度
	 	 		thumbnailsWidth : 180, 
				//右下角缩略图高度
	 	 	 	thumbnailsHeight : 120, 
	 	 	 	//HTML模版
	 	 	 	template : {
		 	 	 	//操作工具
		 	 	 	opertation : '<div class="tool">' +
								 	'<div class="toolct">' +
									 	//'<span class="oper_fullscreen" title="查看全屏"><i class="icon-tool-fullscreen"></i></span>' +
										'<span class="oper_bigger" title="放大图片"><i class="icon-tool-bigger"></i></span>' +
										'<span class="oper_smaller" title="缩小图片"><i class="icon-tool-smaller"></i></span>' +
										'<span class="oper_rotate" title="向右旋转"><i class="icon-tool-rotate"></i></span>' +
					     				'<span class="oper_download" title="下载图片"><i class="icon-tool-download"></i></span>' +
									'</div>' +
								 '</div>',
					//缩略图
					thumbnails : '<div class="thumbnails">' +
					  		  	 	'<span class="thumbClose" title="关闭缩略图"><i class="icon-close-small"></i></span>' +
					  		  		'<img ondragstart="return false;"/>' +
					  		  		'<div class="thumbDrag"><span></span></div>' +
					  		 	 "</div>" 	 	
	 	 	 	}
			},this.options, op||{});
		},
		init:function(){
			var t=this,o = this.options;
			t.$gallery = $(this.element);
	      	t.$gallery.append(o.template.opertation).append(o.template.thumbnails); 
	      	t.$image=t.$gallery.find('img.viewImg').removeClass("rotate0 rotate90 rotate180 rotate270");
	      	t.dialogWidth = t.$gallery.width();
       		t.dialogHeight = t.$gallery.height();
	      	t.imageWidth = t.$image.width();
       		t.imageHeight = t.$image.height();
       		t.imgRatio = t.imageWidth/ t.imageHeight;
			t.$thumbnails = t.$gallery.find("div.thumbnails");
	  		t.$thumbImg = t.$thumbnails.find("img").attr("src", t.$image.attr('src'));
	  		t.$thumbnails.find("img").removeAttr("class").removeAttr("style");
	  		t.isVertical = false;
	  		t.$thumbnails.hide();
	  		//缩略图
	      	t.$thumbnails.css({height: o.thumbnailsHeight,width : o.thumbnailsWidth});
	      	t.thumbX=0;
			t.thumbY=0;
			t.dragX=0;
			t.dragY=0;
			t.setImagePosition();
		},
		bindEvent:function(){
			var t=this,o=this.options,$tool = t.$gallery.find(".tool");
			this.$thumbnails.on("mouseenter",function(e){
	  			t.thumbX = -1;
	  	 	}).on("mousedown",function(e){
	  	 		t.thumbX=e.pageX || e.clientX;
				t.thumbY=e.pageY || e.clientY;
	  	  		e.stopPropagation(); 
	  	  		return false;
	  		}).on("mousemove",function(e){
	  	  		if(t.thumbX > 0){
		   	  		var nextDragX=e.pageX || e.clientX;
					var nextDragY=e.pageY || e.clientY;
					var $td= $(this).find(".thumbDrag"),
						p = t.getCssPosition($td),
						thumbImgWidth = t.$thumbImg.width(),
						thumbImgHeight = t.$thumbImg.height(),
						left =p.left +  (nextDragX - t.thumbX),
						top =p.top + (nextDragY - t.thumbY),
						w = $td.width(),
						h = $td.height(),
						it,il,maxL,maxT;
				
					if(t.isVertical){
						thumbImgWidth = [thumbImgHeight, thumbImgHeight = thumbImgWidth][0];
					}
					it = (o.thumbnailsHeight - thumbImgHeight) / 2,
					il = (o.thumbnailsWidth - thumbImgWidth) / 2,
					maxL = o.thumbnailsWidth - w - il - 2, //减去2像素边框部分
					maxT = o.thumbnailsHeight - h - it - 2;
					
					if(left < il ){
						left = il;
					}else if(left > maxL){
						left = maxL;
					}
				
					if(top < it ){
						top = it;
					}else if(top > maxT){
						top = maxT;
					}
					//缩略图显示
					$td.css({left : left,top : top});
					
					t.thumbX=nextDragX;
					t.thumbY=nextDragY;
					
					//处理大图显示
					var imagePosition = t.getCssPosition(t.$image),
						imageWidth = t.$image.width(),
						imageHeight = t.$image.height(),
						cW=t.dialogWidth,cH=t.dialogHeight,
						maxl=0,maxt=0,minl=cW-imageWidth,mint=cH-imageHeight;
					if(t.isVertical){
						imageWidth = [imageHeight, imageHeight = imageWidth][0];
						maxl=(imageWidth-imageHeight)/2,maxt=(imageHeight-imageWidth)/2;
						minl=cW-imageWidth+maxl,mint=cH-imageHeight+maxt;
					}
					
					if(imageWidth < cW){
						left = imagePosition.left;
					}else{
						left = -imageWidth * (left-il) / thumbImgWidth+maxl;
						if(left > maxl){
							left = maxl;
						}else if(left < minl){
							left = minl;
						}
					}
					
					if(imageHeight < cH){
						top = imagePosition.top;
					}else{
						top = -imageHeight * (top-it) / thumbImgHeight+maxt;
						if(top > maxt){
							top = maxt;
						}else if(top < mint){
							top = mint;
						}
					}
				
					t.$image.css({left : left,top : top});
					e.stopPropagation();
		  	  		return false;
	  	  		}
	  	 	}).on("mouseup",function(e){
	  	  		t.thumbX = -1;
	  	 	});
			
			t.$thumbnails.find(".thumbClose").on("click",function(){
		  		t.$thumbnails.hide();
		  	});
			
			//显示工具栏
	  	  	t.$gallery.on("mouseover",function(e){
	  	  		$(this).find("div.tool").show();
	  	  	}).on("mouseenter",function(e){
				t.dragX = -1;
			}).on("mouseout",function(e){
				$(this).find("div.tool").hide();
			}).on("mousedown",function(e){
	  	 		t.dragX=e.pageX || e.clientX;
				t.dragY=e.pageY || e.clientY;
				e.stopPropagation(); 
	  	  		return false;
	  	  	}).on("mousemove",function(e){
	  	  		$(this).find("div.tool").show();
	  	  		if(t.dragX > 0){
		   	  		var nextDragX=e.pageX || e.clientX;
					var nextDragY=e.pageY || e.clientY;
					var p = t.getCssPosition(t.$image),
						left =p.left +  (nextDragX - t.dragX),
						top =p.top + (nextDragY - t.dragY),
						w = t.$image.width(),
						h = t.$image.height(),
						cW=t.dialogWidth,cH=t.dialogHeight,
						maxl=0,maxt=0,minl=cW-w,mint=cH-h;
					if(t.isVertical){
						w = [h, h = w][0];
						maxl=(w-h)/2,maxt=(h-w)/2;
						minl=cW-w+maxl,mint=cH-h+maxt;
					}
					if(w > cW){
						if(left > maxl){
							left = maxl;
						}else if(left < minl){
							left = minl;
						}
					}else{
						left = p.left;
					}
					if(h > cH){
						if(top > maxt){
							top = maxt;
						}else if(top < mint){
							top = mint;
						}
					} else{
						top = p.top;  
					}
					t.$image.css({left : left,top : top});
					t.dragX=nextDragX;
					t.dragY=nextDragY; 	  
					t.setThumbnails(); //缩略图拖拽点
					e.stopPropagation(); 
		  	  		return false;
	  	  		}
	  	 	}).on("mouseup",function(e){
	  	  		t.dragX = -1;
	  	  	});
	  	  	   
	  	  	//处理点击事件
	  	  	$tool.on("click", function(e){
		  	  	var $clicked = $(e.target || e.srcElement);
		  	  	if($clicked.is('i')) $clicked=$clicked.parent();
				if($clicked.hasClass('oper_bigger')){//放大图片
					t.biggerImage();
					return false;
				}
				if($clicked.hasClass('oper_smaller')){//缩小图片
					t.smallerImage();
					return false;
				}
				if($clicked.hasClass('oper_rotate')){//旋转
					t.rotateImage();
					return false;
				}
				if($clicked.hasClass('oper_download')){//下载 
					var imgUrl = t.$image.attr("src");
		  	   		if(!imgUrl) return;
			  	   	var iframe=$('#downFile_hidden_Iframe');
			  		if(iframe.length==0){ 
			  			iframe=$('<iframe name="downFile_hidden_Iframe" style="display:none;"></iframe>').appendTo('body');
			  		}
			  		iframe[0].src=imgUrl;
				}
	  	  	});
	  	 	
	  	  	//鼠标滚轮事件 控制图片缩放
	  	 	if(document.attachEvent){
				document.attachEvent("on"+this.mousewheelEvent, function(e){
					mouseWheelScroll(e);
					return false;
				});
			} else if(document.addEventListener){
				document.addEventListener(this.mousewheelEvent, function(e){
					mouseWheelScroll(e);  
					return false;
				}, false);
			}
			
			function mouseWheelScroll(e){
				var _delta = parseInt(e.wheelDelta || -e.detail);
		    	//向上滚动
		  		if (_delta > 0) {
	        		t.biggerImage();
	        	}else {//向下滚动
	            	t.smallerImage();
	        	}
			}
		},
		getCssPosition:function($el){
			var left=$el.css('left'),top=$el.css('top');
			return {left:parseFloat(left),top:parseFloat(top)};
		},
		setThumbnails:function(){
			var t=this,o=this.options;
			var $img = t.$thumbnails.find("img"),
				sW = $img.width(),
				sH = $img.height(),
				w = t.$image.width(),
				h =  t.$image.height(),
				imf = t.$image.position(),
				imfl = imf.left,
				imft = imf.top,
				cW = t.dialogWidth,
				cH = t.dialogHeight,
				tW,tH,tl,tt;
			if(t.isVertical){
				sW = [sH, sH = sW][0];
				w = [h, h = w][0];
			}
	
			tW = sW / (w / cW);
			if(w < cW){
				tW = sW;
			}
			tH = sH / (h / cH);
			if(h < cH){
				tH = sH;
			}
			tl = (o.thumbnailsWidth - sW)/2 + -imfl/w * sW ;
			if(w < cW){
				tl = (o.thumbnailsWidth - sW)/2;
			}
			tt = (o.thumbnailsHeight - sH)/2 + -imft/h * sH ;
			if(h < cH){
				tt = (o.thumbnailsHeight - sH)/2;
			}
			t.$thumbnails.find(".thumbDrag").css({width: tW,height: tH,left: tl,top: tt});
		},
		biggerImage:function(){//放大图片
			var t=this,o=this.options;
			var w = t.$image.width(),
	  	 		h = t.$image.height(),
	  	 		nextW = w * o.ratio,
	  	 		nextH = h * o.ratio;
			if(nextW - w < 1){
				nextW = Math.ceil(nextW);
			}
	  	 	var percent =  parseInt((nextW / t.imageWidth * 100).toFixed(0),10);
	  	 	if(percent > 90 && percent < 110){
	  	 		percent = 100;
	  	 		nextW = t.imageWidth;
	  	 		nextH = t.imageHeight;
	  		}else if(percent > 1600) {
	  	 		percent = 1600;
	  	 		nextW = t.imageWidth * 16;
	  	 		nextH = t.imageHeight * 16; 
	  	 	}
	  	 	t.$image.width(nextW).height(nextH);
	  	 	
	  	 	t.showPercentTip(percent);
	  	 	t.setImagePosition();
	  	 	t.showThumbnails(nextW, nextH);
		},
		smallerImage:function(){//缩小图片
			var t=this,o=this.options;
			var w = t.$image.width(),
	  	 		h = t.$image.height(),
	  	 		nextW,
	  	 		nextH;
	  	 	var percent =  parseInt((w / o.ratio / t.imageWidth * 100).toFixed(0),10) ;
	  	 	if(percent < 5) {
	 			percent = 5;
	  	 		nextW = t.imageWidth / 20;
	  	 		nextH = t.imageHeight / 20;
	  	 	}else if(percent > 90 && percent < 110){
	  	 		percent = 100;
	  	 		nextW = t.imageWidth;
	  	 		nextH = t.imageHeight;
	  	 	} else{
	  	 		nextW = w / o.ratio;
	  	 		nextH = h / o.ratio; 
	  	 	}
	  	 
	  	 	t.$image.width(nextW).height(nextH);
	  	 	
	  	 	t.showPercentTip(percent);
	  	 	t.setImagePosition();
	  	 	t.showThumbnails(nextW, nextH);
		},
		showPercentTip:function(percent){//显示百分比提示
			this.$gallery.find(".percentTip").remove();
	  		$("<div class='percentTip'><span>"+percent+"%</span></div>").appendTo(this.$gallery).fadeOut(1500);
		},
		showThumbnails:function(width, height){//显示缩略图
			if(this.isVertical){ 
				width = [height, height = width][0];
			}
	  		if(width > this.dialogWidth || height > this.dialogHeight){
	  			this.$thumbnails.show();
	  			this.setThumbnails();
	  		} else{
	  			this.$thumbnails.hide();
	  		}	 
		},
		rotateImage:function(){//图片旋转
			var t=this,o=this.options;
			var rotateClass = t.$image.attr("class").match(/(rotate)(\d*)/);
  	  		if(rotateClass){
  	  			var nextDeg = (rotateClass[2] * 1 + 90) % 360;
				t.$image.removeClass(rotateClass[0]).addClass("rotate" + nextDeg);
  	  			t.$thumbImg.removeClass(rotateClass[0]).addClass("rotate" + nextDeg);
  	  			t.isVertical = nextDeg == 90 || nextDeg == 270;
  	  		} else{
  	  			t.$image.addClass("rotate90");
  	  			t.$thumbImg.addClass("rotate90");
  	  			t.isVertical = true;
  	  		}
  	  		t.resizeImage();
			t.resizeThumbImg();
  	  		t.setImagePosition();
		},
		resizeImage:function(){//重置图片宽高
			var t=this,o=this.options;
			var mW=t.dialogWidth,mH=t.dialogHeight;
	  		if(t.isVertical){
	  			mW = [mH, mH = mW][0];
	  		}
	  		var width = Math.min(t.imageWidth, mW), height = Math.min(t.imageHeight, mH);
	  		if(width / height > t.imgRatio){
	  			width = height * t.imgRatio;
	  		} else{
	  			height = width / t.imgRatio;
	  		}
	  		t.$image.css({width:width,height:height});
	  	},
	  	resizeThumbImg:function(){
	  		var t=this,o=this.options;
	  		var maxW = o.thumbnailsWidth, maxH = o.thumbnailsHeight;
	  		if(t.isVertical){
	  			maxW = [maxH, maxH = maxW][0];
	  		}
	  		t.$thumbImg.css({maxWidth : maxW,maxHeight : maxH});
	  		t.$thumbnails.hide();
	  	},
	  	setImagePosition:function(){//设置图片位置
	  		var t=this,o=this.options;
	  		var w = t.$image.width(),
  	    		h = t.$image.height(),
  	    		dialogWidth = t.dialogWidth,
  	    		dialogHeight = t.dialogHeight;
	  		var left = (dialogWidth - w)/2,top = (dialogHeight - h)/2;
			this.$image.css("left", left +"px").css("top", top+"px");
	  	}
	});
	
})(jQuery);
