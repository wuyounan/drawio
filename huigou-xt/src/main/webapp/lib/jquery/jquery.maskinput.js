/*---------------------------------------------------------------------------*\
|  title:         格式化输入控件(输入框根据给定格式输入)                                  |
|  Author:        xx                                                          |
|  Created:       2011-8-18                                                   |
|  LastModified:  2011-8-19                                                   |
|                                                                             |
|  使用如:       $('#test01').mask('9999-99-99');                               |
|                $('#test02').mask('9999-99-99 99:99:99');                    |
|                $("textarea[name='time_end']").mask('9999-99-99');           |
|                $('#test03').mask('nnnnnn.9999',{number:true});              |
|                $('#test04').mask('nnnnnnnnn.9999',{number:true,money:true});|
\*---------------------------------------------------------------------------*/
(function($) {
	$.fn.caret=function(begin,end){
		if(typeof begin == 'number'){
			end = (typeof end == 'number')?end:begin;  
			return this.each(function(){
				if(this.setSelectionRange){
					this.focus();
					this.setSelectionRange(begin,end);
				}else if (this.createTextRange){
					var range = this.createTextRange();
					range.collapse(true);
					range.moveEnd('character', end);
					range.moveStart('character', begin);
					range.select();
				}
			});
		}else{
			if (this[0].setSelectionRange){
				begin = this[0].selectionStart;
				end = this[0].selectionEnd;
			}else if(document.selection && document.selection.createRange){
				try{//textare 执行以下代码 如果是 input type=text 会抛出异常
					var range = document.selection.createRange();
					var range_all = document.body.createTextRange();
					range_all.moveToElementText(this[0]);
					//两个range，一个是已经选择的text(range)，一个是整个textarea(range_all)
					//range_all.compareEndPoints()比较两个端点，如果range_all比range更往左(further to the left)，则     
					//返回小于0的值，则range_all往右移一点，直到两个range的start相同。
					for (begin=0; range_all.compareEndPoints("StartToStart", range) < 0; begin++)
						range_all.moveStart('character', 1);
					// 计算'\n'
					for (var i = 0; i <= begin; i ++){
						if (this[0].value.charAt(i) == '\n')
							begin++;
					}
					// create a selection of the whole textarea
					range_all = document.body.createTextRange();
					range_all.moveToElementText(this[0]);
					 // calculate selection end point by moving beginning of range_all to end of range
					for (end = 0; range_all.compareEndPoints('StartToEnd', range) < 0; end ++)
						 range_all.moveStart('character', 1);
						// get number of line breaks from textarea begin to selection end and add them to end
					for (var i = 0; i <= end; i ++){
						if (this[0].value.charAt(i) == '\n')
							  end ++;
					}
				}catch(e){//input type=text  执行
					var range = document.selection.createRange();			
					begin = 0 - range.duplicate().moveStart('character', -100000);
					end = begin + range.text.length;
				}
			}
			return {begin:begin,end:end};
		}       
	};

	//字符对应的正则表达式
	var maskCharMap={
		'0':"[0]",
		'1':"[0-1]",
		'2':"[0-2]",
		'3':"[0-3]",
		'4':"[0-4]",
		'5':"[0-5]",
		'6':"[0-6]",
		'7':"[0-7]",
		'8':"[0-8]",
		'9':"[0-9]",
		'n':"[0-9|-]",
		'#':"[0-9]",
		'a':"[a-z]",
		'A':"[A-Z]",
		'Z':"[A-Za-z]",
		'*':"[A-Za-z0-9|-]"
	};

    $.mask={
		addPlaceholder : function(c,r){
			maskCharMap[c]=r;
		}
	};
	
	$.fn.unmask=function(){
		return this.each(function() {
			if($(this).hasClass("ui-maskedinput")){
				var obj=$.data(this,'ui-maskedinput');
				if(obj) obj.destroy();
			}
		});
	};

	$.fn.mask = function(m,o) {
		return this.each(function() {
			if(!$(this).hasClass("ui-maskedinput")) new maskInput(this,m,o);
		});
	};
	
	$.fn.checkValue = function() {
		return this.each(function() {
			if($(this).hasClass("ui-maskedinput")){
				var obj=$.data(this,'ui-maskedinput');
				if(obj) obj.checkVal();
			}
		});
	};

	$.fn.changeMask = function(m,o) {
		return this.each(function() {
			if($(this).hasClass("ui-maskedinput")){
				var obj=$.data(this,'ui-maskedinput');
				if(obj) obj.changeMask(m,o);
			}
		});
	};

	var maskInput = function (el,mask,settings){
		this.element=$(el);//需要格式的对象
		settings=settings||{};
		this.mask=mask;
		this.settings =$.extend({
			placeholder: "_",//格式显示符号		
			unkeydown:false,//阻止键盘输入
			completed: null,
			onWriteBuffer:null
		},settings);
		var masks=mask.split("");
		this.buffer=new Array(mask.length);
		this.locked=new Array(mask.length);
		this.valid=false;
		this.ignore=false;
		this.firstNonMaskPos=null;
		var newMask=[];
		//循环mask寻找对应正则表达式
		for(var i=0;i<masks.length;i++){
			var c=masks[i];
			newMask.push(maskCharMap[c]||((/[A-Za-z0-9]/.test(c)?"":"\\")+c));
			this.locked[i]=(maskCharMap[c]==null);
			this.buffer[i]=this.locked[i]?c:this.settings.placeholder;									
			if(!this.locked[i] && this.firstNonMaskPos==null)
				this.firstNonMaskPos=i;
		}
		this.re = new RegExp("^"+newMask.join('')+"$");//生成正则表达式
		var _self=this;
		this.ev={//可用事件
			_focus:function(){
				if(_self.element.is(':disabled')||_self.element.is('[readonly]')){
					return;
				}
				_self.focusEvent();
			},
			_blur:function(){
				if(_self.element.is(':disabled')||_self.element.is('[readonly]')){
					return;
				}
				_self.checkVal();
				if(_self.settings.completed)
					_self.settings.completed.call(window,_self.element);
			},
			_keydown:function(e){
				if(_self.element.is(':disabled')||_self.element.is('[readonly]')){
					return;
				}
				if(_self.settings.unkeydown) return false;
				return _self.keydownEvent(e);
			},
			_keypress:function(e){
				if(_self.element.is(':disabled')||_self.element.is('[readonly]')){
					return;
				}
				if(_self.settings.unkeydown) return false;
				return _self.keypressEvent(e);
			},
			_paste:function(){
				if(_self.element.is(':disabled')||_self.element.is('[readonly]')){
					return;
				}
				if(_self.settings.unkeydown) return false;
				setTimeout(function(){_self.checkVal();},0);
			}
		};
		try{
			if($.isMobile()){
				$.each(['focus','blur'],function(i,p){
					_self.element.bind(p,_self.ev['_'+p]);
				});
			}else{
				$.each(['focus','blur','keydown','keypress'],function(i,p){
					_self.element.bind(p,_self.ev['_'+p]);
				});
			}
		}catch(e){
			$.each(['focus','blur','keydown','keypress'],function(i,p){
				_self.element.bind(p,_self.ev['_'+p]);
			});
		}
		if ($.browser.msie){
			this.element.get(0).onpaste=this.ev._paste;
		}else if ($.browser.mozilla){
			this.element.get(0).addEventListener('input',this.ev._paste,false);
		}
		this.element.addClass('ui-maskedinput');
		$.data(this.element[0], "ui-maskedinput", this);
		if(this.element.val()!=''&&this.element.val()!='********')
			this.checkVal();
	};

	$.extend(maskInput.prototype, {
		destroy: function() {
			var _self=this;
			$.each(['focus','blur','keydown','keypress'],function(i,p){
				_self.element.unbind(p,_self.ev['_'+p]);
			});
			if ($.browser.msie) 
				this.element.get(0).onpaste= null;                     
			else if ($.browser.mozilla)
				this.element.get(0).removeEventListener('input',this.ev._paste,false);
			$.removeData(this.element[0], "ui-maskedinput");
			this.element.removeClass("ui-maskedinput");
			this.element=null;
		},
		focusEvent:function(){
			this.checkVal();
			if(!$.isMobile()){
				this.writeBuffer();
			}
			
			try{this.element.get(0).style.imeMode="disabled";}catch(e){}//强制把输入法关掉(IE only)
			setTimeout(function(obj){
				return function(){
					obj.caret();
					setTimeout(function(){
						obj.element.select();//默认全部选中
					},100);
				};
			}(this),0);
		},
		checkVal:function(){	
			var pos=this.firstNonMaskPos;
			var test=this.element.val();
			if(this.settings.number){
				test=test.replace(/[,]/g,'');
				test=this.formatToBuffer(test);
				for(var i=0;i<this.mask.length;i++){
					if(!this.locked[i]) this.buffer[i]=test.charAt(i);
				}
				var re1 = new RegExp(this.settings.placeholder+'|,','g');
				var tm=test.replace(re1,'');
				if(this.settings.money){
					tm=this.formatToMoney(tm);
				}else if(this.settings.positiveMoney){
					tm=tm.numberAbs();
					tm=this.formatToMoney(tm);
				}else{
					tm=this.formatToNumber(tm);
				}
				if(!tm.match(this.re)){
					this.valid=false;
				}else{
					this.valid=true;
				}
				this.element.val(tm);
			}else{		
				for(var i=0;i<this.mask.length;i++){					
					if(!this.locked[i]){
						this.buffer[i]=this.settings.placeholder;
						while(pos++<test.length){
							var reChar=new RegExp(maskCharMap[this.mask.charAt(i)]);
							if(test.charAt(pos-1).match(reChar)){
								this.buffer[i]=test.charAt(pos-1);								
								break;
							}									
						}
					}
				}
				var s=this.writeBuffer();
				if(!s.match(this.re)){
					this.element.val('');
					this.clearBuffer(0,this.mask.length);
					this.valid=false;
				}else{
					this.valid=true;
				}
			}
		},
		caret:function(begin,end){
			return this.element.caret(begin,end);
		},
		writeBuffer:function(){
			var val=this.buffer.join('');
			var oldVal=this.element.val();
			this.element.val(val);
			var fn=this.settings.onWriteBuffer;
			if(fn&&$.isFunction(fn)){
				fn.call(window,val,oldVal);
			}
			return val;
		},
		clearBuffer:function(start,end){
			for(var i=start;i<end&&i<this.mask.length;i++){
				if(!this.locked[i])
					this.buffer[i]=this.settings.placeholder;
			}				
		},
		formatToBuffer:function(text){
			var ms=this.mask.split('.'),vs=text.split('.'),tm=vs[0];
			var vi=tm.split('');vi=vi.reverse();//反转数组的内容
			var a=[],p=this.settings.placeholder;
            $.each(ms[0].split(''),function(i,s){
				a.push(vi[i]?vi[i]:p);
			});
			a=a.reverse();
			if(ms.length>1){
				a.push('.');
				var d=vs.length>1?vs[1].split(''):[];
				$.each(ms[1].split(''),function(i,s){
					a.push(d[i]?d[i]:p);
				});
			}
			return a.join('');
		},
		formatToNumber:function(text){
			var val=parseFloat(text,10);
			if(isNaN(val)) return '';
			var ms=this.mask.split('.'),d=0;
			if(ms.length>1) d=ms[1].length;
			return val.toFixed(d);
		},
		formatToMoney:function(text){
			var val=parseFloat(text,10);
			if(isNaN(val)) return '';
			var isMinus=val<0;val=this.formatToNumber(Math.abs(val)+'');
			var vs=val.split('.'),vi=vs[0].split('').reverse(),a=[];
			$.each(vi,function(i,o){
				a.push.apply(a,[o].concat(((i+1)%3==0&&vi[i+1])?[',']:[]));
			});
			a=a.reverse();
			if(vs.length>1) a.push.apply(a,['.'].concat(vs[1].split('')));
			return isMinus?'-'+a.join(''):a.join('');
		},
		seekNext:function(pos){				
			while(++pos<this.mask.length){
				if(!this.locked[pos])
					return pos;
			}
			return this.mask.length;
		},
		keydownEvent:function(event){
			var e=event||window.event;
			var pos=this.caret(),k =e.charCode||e.keyCode||e.which;
			if(k == 13) return false;
			this.ignore=(k < 16 || (k > 16 && k < 32 ) || (k > 32 && k < 41));
			if((pos.begin-pos.end)!=0 && (!this.ignore || k==8 || k==46)){
				this.clearBuffer(pos.begin,pos.end);
			}
			//console.log("onkeydown" + " keyCode:"+e.keyCode+" charCode:"+e.charCode+' k:'+ k);
			if(k==8){//backspace
				while(pos.begin-->=0){
					if(!this.locked[pos.begin]){
						this.buffer[pos.begin]=this.settings.placeholder;
						if($.browser.opera){							
							var s=this.writeBuffer();
							this.element.val(s.substring(0,pos.begin)+" "+s.substring(pos.begin));
							this.caret(pos.begin+1);					
						}else{
							this.writeBuffer();
							this.caret(Math.max(this.firstNonMaskPos,pos.begin));					
						}									
						return false;								
					}
				}						
			}else if(k==46){//delete
				this.clearBuffer(pos.begin,pos.begin+1);
				this.writeBuffer();
				this.caret(Math.max(this.firstNonMaskPos,pos.begin+1));					
				return false;
			}else if (k==27){//escape
				this.clearBuffer(0,this.mask.length);
				this.writeBuffer();
				this.caret(this.firstNonMaskPos);					
				return false;
			}else if(k==190||k==110){//.
				if(this.settings.number&&this.mask.indexOf('.')!=-1){
					var d=this.mask.indexOf('.');
					if(d+1<this.mask.length){
						this.caret(d+1);
					}
					return false;
				}
			}else if(k== 229){//.  特殊输入法处理
				/*if(this.settings.number&&this.mask.indexOf('.')!=-1){
					var d=this.mask.indexOf('.');
					this.caret(d);
					return false;
				}*/
			}
		},
		keypressEvent:function(event){
			var e=event||window.event;
			var k=e.charCode||e.keyCode||e.which;
			if(this.ignore){
				this.ignore=false;
				return (k == 8)? false: null;
			}
			var pos=this.caret();
			if(e.ctrlKey || e.altKey){//Ignore
				return true;
			}else if ((k>=41 && k<=122) ||k==32 || k>186){//typeable characters
				var p=this.seekNext(pos.begin-1);
				if(p<this.mask.length){
					if(new RegExp(maskCharMap[this.mask.charAt(p)]).test(String.fromCharCode(k))){
						this.buffer[p]=String.fromCharCode(k);									
						this.writeBuffer();
						var next=this.seekNext(p);
						this.caret(next);
					}				
				}
			}				
			return false;				
		},
		changeMask:function(mask,settings){
			settings=settings||{};
			this.settings ={};
			this.mask=mask;
			this.settings =$.extend({
				placeholder: "_",//格式显示符号		
				unkeydown:false,//阻止键盘输入
				completed: null
			},settings);
			var masks=mask.split("");
			this.buffer=new Array(mask.length);
			this.locked=new Array(mask.length);
			this.valid=false;
			this.ignore=false;
			this.firstNonMaskPos=null;
			var newMask=[];
			//循环mask寻找对应正则表达式
			for(var i=0;i<masks.length;i++){
				var c=masks[i];
				newMask.push(maskCharMap[c]||((/[A-Za-z0-9]/.test(c)?"":"\\")+c));
				this.locked[i]=(maskCharMap[c]==null);
				this.buffer[i]=this.locked[i]?c:this.settings.placeholder;									
				if(!this.locked[i] && this.firstNonMaskPos==null)
					this.firstNonMaskPos=i;
			}
			this.re = new RegExp("^"+newMask.join('')+"$");//生成正则表达式
		}
	});
})(jQuery);