/*---------------------------------------------------------------------------*\
|  Subject:      在线编辑器                                                                                                                                                                |
|  Author:       xx                                                          |
|  Created:      2009-10-15                                                  |
|  modif:        2014-07-07                                                  |
|  需要JS:        jquery.js  jquery.color.js                                   |
|  作 用:         将TEXTAREA封装为在线编辑器                                                                                                                           |
|  使用如:        $('#textarea2').toAreaEdit();                                |
\*---------------------------------------------------------------------------*/
(function($) {
   $.fn.toAreaEdit=function(op){
      op=op||{};
	  op = $.extend({
			imagesDir:'themes/default/images/editor/',//图片资源路径
			width:$(this).width(),//默认宽
			height:$(this).height()//默认高度
	   }, op);	
	   return this.each(function(){
	       var div=$("<div class='ui-html-edit-toolbar'></div>");
	       var hideTimer=null,doClearTimeout=function(){
	    	   if(hideTimer){
					clearTimeout(hideTimer);
					hideTimer=null;
				}
	       };
           div.css("width",op.width);
		   div.append('<span class="fontSelect"></span><span class="fontSizes"></span>');
		   $(this).hide();
           $.each($.myEditor.buttonName,function(i,o){//生成编辑器按钮
		       var bar=$.myEditor.ToolbarList[o];
		       var span=$('<span><img class=button title="'+bar[1]+'" hspace=1 src="'+(op.imagesDir+bar[2])+'" border=0 unselectable="no"></span>');
               if(o!='seperator'){
			       span.css({width:'18px',cursor:'hand',height:'27px'});
                   span.hover(
					  function () {
						$(this).children('img').toggleClass("buttonOver");
                        $(this).children('img').attr('src',op.imagesDir+bar[3]);
					  },
					  function () {
						$(this).children('img').toggleClass("buttonOver");
						if($.inArray(o,$.myEditor.keepButton)>-1){
							var flag=$(this).attr('flag');
							if(flag!='1'){
								$(this).children('img').attr('src',op.imagesDir+bar[2]);
							}
						}else{
							$(this).children('img').attr('src',op.imagesDir+bar[2]);
						}
					  }
				   ).click(function(){
					   if($.inArray(o,$.myEditor.keepButton)>-1){
						   var flag=$(this).attr('flag');
						   if(flag=='1'){
							   $(this).attr('flag',0);
							   $(this).children('img').attr('src',op.imagesDir+bar[2]);
						   }else{
							   $(this).attr('flag',1);
							   $(this).children('img').attr('src',op.imagesDir+bar[3]);
						   }
					   }
				       formatText(bar[0],this);
				   });
			   }else{
				   span.css({width:'2px',height:'18px'});
				   span.children('img').css({width:'2px',height:'18px'});
			   }
			   div.append(span);
		   });
		   div.insertAfter($(this));
		   (function(){//生成字体类型菜单
		       var fontSelect=$('<span class="showFont" ><input name="FontSelectInput" class=editor style="width:75px;" readonly alt=字体类型 value=选择字体><img src="'+op.imagesDir+'down.gif"  class="dropdownBtn" width="20" height="22"><br><span class=dropdown style="width:100px;"></span></span>');
               $.each($.myEditor.Fonts,function(i,o){
                   var button=$('<button style="width:100px;"><span style="font-size:12px;word-break:keep-all;font-family:'+o+'">'+o+'</span></button>');
				   button.addClass('mouseOut');
				   button.hover(
					  function () {
					    $(this).removeClass("mouseOut");
						$(this).addClass("mouseOver");
					  },
					  function () {
						$(this).addClass("mouseOut");
						$(this).removeClass("mouseOver");
					  }
				   ).click(function(event){
				      formatText('FontName',o);
					  fontSelect.children('.dropdown').hide();
					  fontSelect.children('input[name="FontSelectInput"]').val(o);
					  event.preventDefault();
					  event.stopPropagation();
					  return false;
				   }); 
				   $('.dropdown',fontSelect).append(button).append('<br>');
			   });
			   fontSelect.insertAfter($('.fontSelect',div));
			   fontSelect.hover(
					  function () {
						$(this).children('input[name="FontSelectInput"]').toggleClass("active_editor");
						doClearTimeout();
					  },
					  function () {
						$(this).children('input[name="FontSelectInput"]').toggleClass("active_editor");
						doClearTimeout();
						var dropdownDiv=$(this).children('.dropdown');
						hideTimer=setTimeout(function(){
							dropdownDiv.hide();    
						},500);
						
					  }
				).click(function(){
				      $(this).children('.dropdown').toggle();
			    }); 
		   })();
		   (function(){//生成字体大小菜单
		       var fontSizes=$('<span class="showFont" ><input name="FontSizesInput" class=editor style="width:52px;" readonly alt=字体大小 value=字体大小><img src="'+op.imagesDir+'down.gif"  class="dropdownBtn" width="20" height="22"><br><span class=dropdown style="width:120px;"></span></span>');
               $.each($.myEditor.FontSizes,function(i,o){
                   var button=$('<button style="width:100px;"><font style="font-size:'+(o*5)+'px;" size="' + o + '">字体' + o + '</font></button>');
				   button.addClass('mouseOut');
				   button.hover(
					  function () {
					    $(this).removeClass("mouseOut");
						$(this).addClass("mouseOver");
					  },
					  function () {
						$(this).addClass("mouseOut");
						$(this).removeClass("mouseOver");
					  }
				   ).click(function(event){
				      formatText('FontSize',o);
					  fontSizes.children('input[name="FontSizesInput"]').val('字体 '+o);
					  fontSizes.children('.dropdown').hide();
					  event.preventDefault();
					  event.stopPropagation();
					  return false;
				   }); 
				   $('.dropdown',fontSizes).append(button).append('<br>');
			   });
			   fontSizes.insertAfter($('.fontSizes',div));
			   fontSizes.hover(
					  function () {
						$(this).children('input[name="FontSizesInput"]').toggleClass("active_editor");
						doClearTimeout();
					  },
					  function () {
						$(this).children('input[name="FontSizesInput"]').toggleClass("active_editor");
						doClearTimeout();
						var dropdownDiv=$(this).children('.dropdown');
						hideTimer=setTimeout(function(){
							dropdownDiv.hide();    
						},500);
					  }
				).click(function(){
				      $(this).children('.dropdown').toggle();
			    }); 
		   })();
		   var content=$("<div class='htmlEditEditDiv'><iframe marginheight='1' marginwidth='1' width='100%' height='100%' scrolling='auto' frameborder='0'></iframe></div>");
		   content.css({width:op.width,height:op.height});
           content.children('iframe').css({width:op.width,height:op.height,margin:'0px'});
           content.insertAfter(div);
           var iframeContentWindow = content.children('iframe')[0].contentWindow;
		   var doc=iframeContentWindow.document;
		   doc.open();
		   doc.write($(this).val());// 将TEXTAREA的内容写入iframe中
		   doc.close();
		   //设置ifrmae可以编辑
		   doc.body.contentEditable = true; //ie
		   doc.designMode = "on"; //firefor
		   $(doc).keyup(function (event) {
			   updateTextArea();
		   });		 
		   //修改回车产生很大行间距问题
		   $(doc).keypress(function (event) {
			   if(event.keyCode=='13'){
				   if(this.selection){
					   var sel=this.selection;
					   sel.createRange().pasteHTML('<br>'); 
					   var rng = sel.createRange();// 创建一个选区 
					   rng.moveStart('character',0);// 移动起始点，只是调用，并不实际移动 
					   rng.collapse(true);// 在起始点重叠 
					   rng.select();// 显示光标
				    }else if (this.getSelection){
					   var range = this.getSelection().getRangeAt(0);
					   range.surroundContents(doc.createElement("br"));
					}
					updateTextArea();
					event.preventDefault();
					event.stopPropagation();
					return false;
				 }
		   });
           var formatText=function(id, selected){
		        iframeContentWindow.focus();
				if (id == "FontSize") {//字体大小
				 iframeContentWindow.document.execCommand("FontSize", false, selected);
				}else if (id == "FontName") {//字体类型
				  iframeContentWindow.document.execCommand("FontName", false, selected);
				}else if(id=='showcolor'){//颜色控制
				  //selected在这里为触发该方法的对象
				  try{
					  $(selected).colorPicker({
						setBackground:false,
						callBack:function(v){
						    $(selected).attr('color',v);
                            $(selected).children('img').css('backgroundColor',v);
						}
					 });
				  }catch(e){alert("您的浏览器不支持颜色选择！"+e.message);};//这里引用jquery.color.js
				}else if (id == 'ForeColor' || id == 'BackColor') {//选择颜色
				   //selected在这里为触发该方法的对象 这里找到该对象父亲节点的下一节点 取得 该节点的 color 属性
				   try{
					   var colorObj=$(selected).next();
					   if(colorObj.length>0){
					       var color=colorObj.attr('color');
						   color=color&&color!=''?color:'transparent';
						   iframeContentWindow.document.execCommand(id, false, color);
					   }
					}catch(e){alert("您的浏览器不支持颜色选择！"+e.message);};
				}else if(id=='clearcontent'){//清空编辑器
					  iframeContentWindow.document.body.innerHTML='';
				}else {
				   iframeContentWindow.document.execCommand(id, false, null);
				}
				updateTextArea();
		   };
		   var updateTextArea=function(obj){
		     return function(){
				obj.val(iframeContentWindow.document.body.innerHTML);
		      };
		   }($(this));
	   });
   };
  
    $.myEditor={};
	//可用字体类型
	$.myEditor.Fonts = new Array();
	$.myEditor.Fonts[0] = "宋体";
	$.myEditor.Fonts[1] = "方正舒体";
	$.myEditor.Fonts[2] = "微软雅黑";
	$.myEditor.Fonts[3] = "黑体";
	$.myEditor.Fonts[4] = "华文彩云";
	$.myEditor.Fonts[5] = "华文仿宋";
	$.myEditor.Fonts[6] = "华文细黑";
	$.myEditor.Fonts[7] = "华文新魏";
	$.myEditor.Fonts[8] = "华文行楷";
	$.myEditor.Fonts[9] = "华文中宋";
	$.myEditor.Fonts[10] = "仿宋_GB2312";
	$.myEditor.Fonts[11] = "隶书";
	$.myEditor.Fonts[12] = "仿宋体";
	$.myEditor.Fonts[13] = "幼圆";
	$.myEditor.Fonts[14] = "Arial";
	$.myEditor.Fonts[15] = "Arial black";
	$.myEditor.Fonts[16] = "Arial narrow";
	$.myEditor.Fonts[17] = "Fixedsys";
	$.myEditor.Fonts[18] = "New";
	$.myEditor.Fonts[19] = "Georgia";
	$.myEditor.Fonts[20] = "Verdana";
	$.myEditor.Fonts[21] = "Geneva";
	//可用字体大小
	$.myEditor.FontSizes = new Array();
	$.myEditor.FontSizes[0]  = "2";
	$.myEditor.FontSizes[1]  = "3";
	$.myEditor.FontSizes[2]  = "4";
	$.myEditor.FontSizes[3]  = "5";
	$.myEditor.FontSizes[4]  = "6";
	//页面显示按钮类型
	$.myEditor.buttonName = new Array();
	$.myEditor.buttonName[0]  = "bold";
	$.myEditor.buttonName[1]  = "italic";
	$.myEditor.buttonName[2]  = "underline";
	$.myEditor.buttonName[3]  = "seperator";
	$.myEditor.buttonName[4]  = "subscript";
	$.myEditor.buttonName[5]  = "superscript";
	$.myEditor.buttonName[6]  = "seperator";
	$.myEditor.buttonName[7]  = "justifyleft";
	$.myEditor.buttonName[8]  = "justifycenter";
	$.myEditor.buttonName[9]  = "justifyright";
	$.myEditor.buttonName[10] = "seperator";
	$.myEditor.buttonName[11] = "unorderedlist";
	$.myEditor.buttonName[12] = "orderedlist";
	$.myEditor.buttonName[13] = "outdent";
	$.myEditor.buttonName[14] = "indent";
	$.myEditor.buttonName[15] = "seperator";
	$.myEditor.buttonName[16] = "forecolor";
	$.myEditor.buttonName[17] = "showcolor";
	$.myEditor.buttonName[18] = "backcolor";
	$.myEditor.buttonName[19] = "showcolor";
	$.myEditor.buttonName[20] = "seperator";
	$.myEditor.buttonName[21] = "clearcontent";
	
	//按钮对应图片及功能
	$.myEditor.ToolbarList = {
		//按钮名称        按钮ID                   提示信息              按钮显示图片               鼠标经过按钮显示图片
		"bold":           ['Bold',                 '加粗',               'bold.gif',               'bold_on.gif'],
		"italic":         ['Italic',               '倾斜',               'italics.gif',            'italics_on.gif'],
		"underline":      ['Underline',            '下划线',             'underline.gif',          'underline_on.gif'],
		"seperator":      ['',                     '',                   'seperator.gif',          'seperator.gif'],
		"subscript":      ['Subscript',            '下标',               'subscript.gif',          'subscript_on.gif'],
		"superscript":    ['Superscript',          '上标',               'superscript.gif',        'superscript_on.gif'],
		"justifyleft":    ['Justifyleft',          '左对齐',             'justify_left.gif',       'justify_left_on.gif'],
		"justifycenter":  ['Justifycenter',        '居中',               'justify_center.gif',     'justify_center_on.gif'],
		"justifyright":   ['Justifyright',         '右对齐',             'justify_right.gif',      'justify_right_on.gif'],
		"unorderedlist":  ['InsertUnorderedList',  '项目符号',           'list_unordered.gif',     'list_unordered_on.gif'],
		"orderedlist":    ['InsertOrderedList',    '编号',               'list_ordered.gif',       'list_ordered_on.gif'],
		"outdent":        ['Outdent',              '减少缩进量',         'indent_left.gif',        'indent_left_on.gif'],
		"indent":         ['Indent',               '增加缩进量',         'indent_right.gif',       'indent_right_on.gif'],
		"forecolor":      ['ForeColor',            '字体颜色',           'forecolor.gif',          'forecolor_on.gif'],
		"backcolor":      ['BackColor',            '背景颜色',           'backcolor.gif',          'backcolor_on.gif'],
		"clearcontent":   ['clearcontent',         '清空编辑器',         'removeformat.gif',       'removeformat.gif'],
	    "showcolor":      ['showcolor',            '颜色选择器',         'showcolor.gif',          'showcolor_on.gif']
	};
	$.myEditor.keepButton=['bold','italic','underline','subscript','superscript'];
	
	//使用IFRAME阅览结果
	$.fn.toViewEdit=function(op){
		op=op||{};
		op = $.extend({
			style:'*{line-height:1;}',
			width:$(this).width(),//默认宽
			height:$(this).height()//默认高度
		}, op);
	    return this.each(function(){
	    	$(this).hide();
	    	if($(this).val()==''){
	    		return;
	    	}
	    	var content=$("<div><iframe marginheight='1' marginwidth='1' width='100%' height='100%' scrolling='auto' frameborder='0'></iframe></div>");
			content.css({width:op.width,height:op.height});
	        content.children('iframe').css({width:op.width,height:op.height,margin:'0px'});
	        content.insertAfter($(this));
	        var iframeContentWindow = content.children('iframe')[0].contentWindow;
			var doc=iframeContentWindow.document;
			var html=['<head><style>',op.style,'</style></head>'];
			html.push($(this).val());
		    doc.open();
			doc.write(html.join(''));
			doc.close();
			var  calcPageHeight=function(doc) {
		        var cHeight = Math.max(doc.body.clientHeight, doc.documentElement.clientHeight);
		        var sHeight = Math.max(doc.body.scrollHeight, doc.documentElement.scrollHeight);
		        var height  = Math.max(cHeight, sHeight);
		        return height;
		    };
			var height=calcPageHeight(doc);
			content.height(height+20);
			content.children('iframe').height(height+20);
	    });
	};
})(jQuery);