/********************************
title:      表单向导插件
Author:     xx
Date:		2018-11-08
demo:	$('#wizard').wizard({
			onLeaveStep:function(nextStepIdx,stepIdx,stepContent,step){
				//ajax step.data('hasContent'); 是否已加载内容
				if(nextStepIdx==2){
					this.setError(true);
					return false;
				}
				console.log('onLeaveStep:nextStepIdx:'+nextStepIdx+'|stepIdx:'+stepIdx);
				return true;
			},
			onShowStep:function(stepIdx,stepContent,step){
				console.log('onShowStep:'+stepIdx);
				return true;
			},
			onFinish:function(){
				alert('onFinish');
			}
		});
*********************************/
(function($) {
	
	$.fn.wizard = function (op){
		var obj=this.data('ui_wizard');
		if(!obj){
			new Wizard(this,op);
		}else{
			if (typeof op == "string") {
				var value=null,args = arguments;
				$.each(['doForwardProgress','doBackwardProgress','loadContent','setError'],function(i,m){
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

    function Wizard(el,op){
		this.options={};
		this.element=$(el);
		this.set(op);
		this.init();
		this.bindEvent();
		this.element.data('ui_wizard',this);
	}
    
	$.extend(Wizard.prototype,{
		set:function(op){
			this.options=$.extend({
				 selected: 0,  // 当前步骤数
		         keyNavigation: true, // 是否启用键盘事件
		         enableAllSteps: false,
		         transitionEffect: 'def', // /fade/slide/def
		         url:null, // ajax url地址 可为function
		         param:null,//ajax 执行参数 可为function
		         isVertical:false,//是否垂直显示
		         contentCache:true, // ajax加载内容是否缓存
		         cycleSteps: false, // 循环步进导航
		         enableFinishButton: false, // 完成按钮始终启用
		         needButton:true,//是否生成默认按钮
		         labelNext:'下一步',//Next
		         labelPrevious:'上一步',//Previous
		         labelFinish:'完成',//Finish
		         onLeaveStep: null, // 当离开一步时触发
		         onShowStep: null,  // 当显示一步时触发
		         onFinish: null  // 完成时触发
			},this.options, op||{});
		},
		init:function(){
			var g = this, p = this.options;
			g.curStepIdx = p.selected;
			g.element.addClass('ui-wizard').addClass('ui-wizard').addClass(p.isVertical?'vertical':'horizontal');
			g.steps = $(">ul > li", g.element);
			var allDivs =g.element.children('div');      
			g.element.children('ul').addClass("anchor");
            allDivs.addClass("content");
            g.loader = $('<div>加载中...</div>').addClass("loader");
            g.elmStepContainer = $('<div></div>').addClass("step-container");
            g.elmStepContainer.append(allDivs);
            g.elmStepContainer.append(g.loader);
            g.element.append(g.elmStepContainer);
            if(p.needButton){
            	g.elmActionBar = $('<div></div>').addClass("action-bar");
            	g.btNext = $('<a>'+p.labelNext+'</a>').attr("href","#").addClass("button-next");
                g.btPrevious = $('<a>'+p.labelPrevious+'</a>').attr("href","#").addClass("button-previous");
                g.btFinish = $('<a>'+p.labelFinish+'</a>').attr("href","#").addClass("button-finish");
                g.element.append(g.elmActionBar);
                g.elmActionBar.append(g.btFinish).append(g.btNext).append(g.btPrevious); 
            }
            g.prepareSteps();
            //加载默认页
            g.loadContent(g.curStepIdx,false);   
		},
		bindEvent:function(){
			var g = this, p = this.options;
			if(p.needButton){
				g.btNext.on('click',function() {
	                 g.doForwardProgress();
	                 return false;
	            }); 
	            g.btPrevious.on('click',function() {
	                g.doBackwardProgress();
	                return false;
	            }); 
	            g.btFinish.on('click',function() {
	                 if($(this).hasClass('button-disabled')){
	                	 return false;
	                 }
	                 if($.isFunction(p.onFinish)) {
	                	 if(!p.onFinish.call(g)){
	                		 return false;
	                     }
	                 }  
	                 return false;
	            }); 
			}
            g.steps.on("click", function(e){
                 if(g.steps.index(this) == g.curStepIdx){
                   return false;                    
                 }
                 var nextStepIdx = g.steps.index(this);
                 var isDone = g.steps.eq(nextStepIdx).data("isDone") - 0;
                 if(isDone == 1){
                	 g.loadContent(nextStepIdx,true);                    
                 }
                 return false;
           });
           if(p.keyNavigation){//使用按键事件
        	   $(document).keyup(function(e){
                	var k =e.charCode||e.keyCode||e.which;
                    if(k==39){ // Right
                    	g.doForwardProgress();
                    }else if(k==37){ // Left
                    	g.doBackwardProgress();
                    }
               });
           }
		},
		prepareSteps:function(){
			var g = this, p = this.options;
			if(!p.enableAllSteps){
				g.steps.removeClass("selected").removeClass("done").addClass("disabled"); 
                g.steps.data("isDone",0);                 
            }else{
            	g.steps.removeClass("selected").removeClass("disabled").addClass("done"); 
            	g.steps.data("isDone",1); 
            }
			g.steps.each(function(i){
				g._getContent(this).hide();
            });
		},
		_getContent:function(_target){
			var g = this, p = this.options;
			if (typeof _target == "string"){
				return $(_target, g.element);
			}else if(typeof _target == "object"){
				return g._getContent($(_target).data("target"));
			}
			return null;
		},
		loadContent:function(stepIdx,_trigger){//_trigger 是否执行离开事件
			var g = this, p = this.options;
			if(_trigger){
				if(!g._triggerLeaveStep(stepIdx)){
					return false;
				}
			}
			var _selStep = g.steps.eq(stepIdx);
            var _url = p.url,_param=p.param||{};
            if($.isFunction(_url)){
            	_url=_url.call(window,stepIdx,_selStep);
            	if(!_url) return false;
            }
            var hasContent =  _selStep.data('hasContent');
            if(_url && _url.length>0){
            	//存在数据并需要缓存
                if(p.contentCache && hasContent){
                    g.showStep(stepIdx);                          
                }else{
                	if($.isFunction(_param)){
                		_param=_param.call(window,stepIdx);
                		if(!param) return false;
                	}
                	_param=$.extend({},_param,{stepNum : stepIdx});
                    $.ajax({url: _url,type: "POST",data:_param,dataType: "text",
	                    beforeSend: function(xhr){ 
	                	    g.loader.show();
	                	    //发送ajax请求之前向http的head里面加入验证信息 
	                	    xhr.setRequestHeader("csrfToken", $.CSRFToken());
	                    },
	                    error: function(){
	                	    g.loader.hide();
	                    },
	                    success: function(data){
	                	    g.loader.hide(); 
	                        _selStep.data('hasContent',true);
	                        g._getContent(_selStep).html(data);
	                        g.showStep(stepIdx);
	                    }
                    }); 
               }
             }else{
            	 g.showStep(stepIdx);
             }
		},
		showStep:function(stepIdx){
			var g = this, p = this.options;
			var _selStep = g.steps.eq(stepIdx);
            var _curStep = g.steps.eq(g.curStepIdx);
            var _selContainer=g._getContent(_selStep);
            var _curContainer=g._getContent(_curStep);
            g.elmStepContainer.height(_selContainer.outerHeight());               
            if(p.transitionEffect == 'slide'){
            	_curContainer.slideUp("fast",function(e){
            		_selContainer.slideDown("fast");
                    g.curStepIdx = stepIdx;                        
                    g.setupStep(_curStep,_selStep);
                });
            } else if(p.transitionEffect == 'fade'){                      
            	_curContainer.fadeOut("fast",function(e){
            		_selContainer.fadeIn("fast");
                    g.curStepIdx =  stepIdx;                        
                    g.setupStep(_curStep,_selStep);                       
                });                    
            } else{
            	_curContainer.hide(); 
            	_selContainer.show();
                g.curStepIdx =  stepIdx;                        
                g.setupStep(_curStep,_selStep);
            }
		},
		setupStep:function(_curStep,_selStep){
			var g = this, p = this.options;
			_curStep.removeClass("selected");
			_curStep.addClass("done");
            
			_selStep.removeClass("disabled");
			_selStep.removeClass("done");
			_selStep.addClass("selected");
			_selStep.data("isDone",1);
			
            g.adjustButton();
            if($.isFunction(p.onShowStep)) {
               if(!p.onShowStep.call(g,g.curStepIdx,g._getContent(_curStep),_curStep)){
            	   return false;
               }
            }
		},
		doForwardProgress:function(){
			var g = this, p = this.options;
			var nextStepIdx = g.curStepIdx + 1;
			var stepsLength = g.steps.length;
            if(stepsLength <= nextStepIdx){
            	if(!p.cycleSteps){
            		return false;
            	}                  
            	nextStepIdx = 0;
            }
            g.loadContent(nextStepIdx,true);
		},
		doBackwardProgress:function(){
			var g = this, p = this.options;
			var nextStepIdx = g.curStepIdx-1;
            if(0 > nextStepIdx){
            	 if(!p.cycleSteps){
            		 return false;
            	 }
            	 nextStepIdx = steps.length - 1;
            }
            g.loadContent(nextStepIdx,true);
		},
		adjustButton:function(){
			var g = this, p = this.options;
			var stepsLength =g.steps.length;
			if(!p.cycleSteps){                
                if(0 >= g.curStepIdx){
                	$(g.btPrevious).addClass("button-disabled");
                }else{
                    $(g.btPrevious).removeClass("button-disabled");
                }
                if(stepsLength-1 <= g.curStepIdx){
                	$(g.btNext).addClass("button-disabled");
                }else{
                	$(g.btNext).removeClass("button-disabled");
                }
            }
            // Finish Button 
            if(!g.steps.hasClass('disabled') || p.enableFinishButton){
                $(g.btFinish).removeClass("button-disabled");
            }else{
                $(g.btFinish).addClass("button-disabled");
            }      
		},
		_triggerLeaveStep:function(stepIdx){
			var g = this, p = this.options;
			var _selStep = g.steps.eq(stepIdx);
            var _curStep = g.steps.eq(g.curStepIdx);
            if(stepIdx != g.curStepIdx){
            	if($.isFunction(p.onLeaveStep)) {
	                if(p.onLeaveStep.call(g,stepIdx,g.curStepIdx,g._getContent(_curStep),_curStep)===false){
	                	return false;
	                }
                }
            }
            return true;
		},
		setError:function(stepnum,iserror){
			var g = this, p = this.options;
			if (typeof stepnum == "number"){
				if(iserror){                    
					$(g.steps.eq(stepnum)).addClass('error')
	            }else{
	                $(g.steps.eq(stepnum)).removeClass("error");
	            } 
			}else{
				g.setError(g.curStepIdx,stepnum);
			}
		}
	});

	
})(jQuery);