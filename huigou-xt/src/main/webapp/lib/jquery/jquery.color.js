/*---------------------------------------------------------------------------*\
|  Subject:       简单颜色选择器                                                                                                                                                        |
|  Author:        xx                                                          |
|  Created:       2009-10-15                                                  |
|  作 用:          目前在HTML EDIT 插件中使用                                                                                                                         |
|  使用如:         $(selected).colorPicker({setBackground:false,
						callBack:function(v){
						    $(selected).attr('color',v);
                            $(selected).children('img').css('backgroundColor',v);
						}
					 });                              
\*---------------------------------------------------------------------------*/
(function($) {
	var loc={Top:-1,Left:-1,Width:-1,Height:-1};
	var isOn=false;
	$.fn.colorPicker=function(vIn) {
		var obj=this;
		if (vIn) {
			if (typeof vIn.constructor == 'object') {
				vIn = { setBackground : vIn };
			}
		}
		else {
			vIn = {};
		}
		var opt=setDefaults();
		$.extend(opt,vIn);
		$.each(opt,function(n,v) {
			if(v!=false&&n!='align'&&n!='callBack') {
				if (typeof v=='string' || isDOM(v))
					opt[n] = $(v);
				else if (v==true) {
					opt[n] = obj;
				}
				else if (!isJQuery(v))
					opt[n] = false;
			}
		});
		if (opt.align.toLowerCase()!='left') opt.align='right';
		var cp = $('#jquery_ColorPicker');
		var cf=$('#jquery_CP_Frame');
		if (cp.length==0) {
			cp = $('<div id="jquery_ColorPicker" style="width:197px;"></div>');
			cp.css({"position":"absolute","z-index":"10001","background-color":"#FFFFFF","border":"1px solid #CCCCCC","padding":"1px","cursor":"pointer","display":"none"});
			var hc = ["FF","CC","99","66","33","00"];
			var i=0,j=0;
			var r,g,b,c;
			var s = new Array();
			s[0]='<div class="color-dialog-titlebar" style="background-color:#ffcc00;width:198px;font-size:9pt;height:16px;padding-top:3px;"><span style="width:178px;float:left;">&nbsp;颜色选择器</span><span style="cursor:hand;font-weight:bold;color:#ffffff;" title="Close" class="jquery_ColorClose">×</span></div>';
			s[0] += '<table cellspacing="1" cellpadding="0" style="table-layout:fixed;width:197px;"><tr>';
			for(r=0;r<6;r++) {
				for(g=0;g<6;g++) {
					for(b=0;b<6;b++) {
						c = hc[r] + hc[g] + hc[b];
						if (i%18==0 && i>0) {
							s[j+1] = "</tr><tr>";
							j++;
						}
						s[j+1] = '<td class="color" bgcolor="#'+c+'" height="10" width="10" style="width:10px"></td>';
						i++;
						j++;
					}
				}
			}
			s[j+1] = '</tr><tr><td height="10" colspan="12" id="jquery_ColorPicker_Select" style="font-family:Tamoha;font-size:10px;text-align:center;cursor:default;"></td><td class="color" bgcolor="" height="10" colspan="3" title="清除" align="center" style="font-family:Tamoha;font-size:10px">清除</td><td class="color" bgcolor="transparent" height="10" colspan="3" title="透明" align="center" style="font-family:Tamoha;font-size:10px">透明</td></tr></table>';
			cp.html(s.join(''));
			cp.appendTo('body');

			if($.browser.msie&&cf.length==0) {
				cf=$('<iframe scrolling="no" frameborder="0" style="position:absolute;z-index:99;display:none" id="jquery_CP_Frame"></iframe>');
				cf.appendTo('body');
			}
		}
       
		var tl=GetLoc(obj[0]);
		if (cp.is(':visible')&&tl.Top==loc.Top&&tl.Left==loc.Left) {
			cp.hide();
			if($.browser.msie) cf.hide();
		}
		else {
			loc = tl;
			cp.css({"left":tl.Left+(opt.align.toLowerCase()!='left'?tl.Width:0)+"px","top":(tl.Top+tl.Height)+"px"});
			tl = GetLoc(cp[0]);
			cf.css({"left":tl.Left,"top":tl.Top,"width":tl.Width,"height":tl.Height});
			cp.show();
		
			if($.browser.msie) cf.show();
			$('.jquery_ColorClose').hover(
				function () {
				   $(this).css('color','#ff0000');
				},
				function () {
				   $(this).css('color','#ffffff');	
				}
			).click(function(event){
				$('#jquery_ColorPicker').hide();      
			}); 
			$('.color',cp).unbind("mouseover").unbind("click").mouseover(function() {
				setSelect(this.bgColor.toUpperCase());
			}).click(function() {
				setColorValue(this.bgColor.toUpperCase(),opt);
			});
			//定义延迟关闭方式
			var closeTimer=null;
			function doClearTimeout(){
				if(closeTimer){
					clearTimeout(closeTimer);
					closeTimer=null;
				}
			}
			$('#jquery_ColorPicker').bind('mouseenter',function(){
				doClearTimeout();
			}).bind('mouseleave',function(){
				doClearTimeout();
				closeTimer=setTimeout(function(){
					$('#jquery_ColorPicker').hide();    
				},500);
			});
		}
	};

	function compareObj(obj) {
		var oloc=GetLoc(obj);
		return loc.Top==oloc.Top&&loc.Left==oloc.Left&&loc.Width==oloc.Width&&loc.Height==oloc.Height;
	}
	function setDefaults() {
		return {
			setBackground: true,
			setValue: false,
			setColor: false,
			setText: false,
			align: 'left',
			callBack:false
		};
	}

	function setColorValue(v,vIn) {
		v=v=='TRANSPARENT'?'transparent':v;
		$('#jquery_ColorPicker').hide();
		if(vIn.setBackground!=false) {
			vIn.setBackground.css('backgroundColor',v);
		}
		if(vIn.setColor!=false) {
			vIn.setColor.css('color',v);
		}
		if(vIn.setValue!=false) {
			vIn.setValue.val(v);
		}
		if(vIn.setText!=false) {
			vIn.setText.text(v);
		}
		if(vIn.callBack!=false)
			vIn.callBack(v);
	}

	function GetLoc(element) {
		if ( arguments.length != 1 || element == null ) { 
			return null;
		} 
		var offset=$(element).offset();
		var offsetTop = offset.top; 
		var offsetLeft = offset.left; 
		var offsetWidth = $(element).width(); 
		var offsetHeight = $(element).height(); 
		return { Top: offsetTop, Left: offsetLeft, Width: offsetWidth, Height: offsetHeight };
	}

	function setSelect(v) {
		v=v=='TRANSPARENT'?'transparent':v;
		$("#jquery_ColorPicker_Select").css('background-color',v).text(v);
	}

	function isDOM(vIn) {
		return (typeof vIn=='object' && !!vIn.nodeName);
	}

	function isJQuery(vIn) {
		return (typeof vIn=='object' && !!vIn.attr);
	}
})(jQuery);
