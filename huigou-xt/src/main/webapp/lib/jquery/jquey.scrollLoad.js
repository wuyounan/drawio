/*********************************************
title:     下拉滚动条动态加载数据
Author:    xx  
使用:
$('#showAnnotations').scrollLoad({
		url:web_app.name + '/BugMain/getAnnotations.ajax',
		params:{bugid:bugid},
		onLoadItem:function(obj){
	    	var html=['<div class="scrollLoadData">'];
		    html.push('<b>',obj['operationdate'],'&nbsp;',obj['operatorname'],'&nbsp;:</b>');
		    html.push(obj['content']);
		    html.push('<hr/></div>');
		    return html.join('');
		}
});
*********************************************/
(function($){
	
	$.fn.scrollLoad = function(op){
		return this.each(function() {
			var obj=$.data(this,'scrollLoad');
			if(!obj){
				new scrollLoad(this,op);
			}else{
				if (typeof op == "string") {
					var value=null,args = arguments;
					$.each(['reLoad','clean'],function(i,m){
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
		}); 
	};
	
	function scrollLoad(el,op){
		this.$el=$(el);
		this.options={};
		this.loading=true;//加载中
		this.isOver=false;//数据加载完成
		this.set(op);
		this.init();
		this.$el.data('scrollLoad',this);
	};
	
	$.extend(scrollLoad.prototype, {
		set:function(op){
		    this.options = $.extend({
				url: "",                      	 //json数据获取地址
				getMethod: 'POST',               //Ajax获取数据的方法 POST 或则 GET
				params:{},                       //获取json数据的参数
				total:0,
				size:10,
				pageParmName: 'page',
				sizeParmName: 'pagesize', 
				totalParmName: 'Total', 
				loadHtml:'数据加载中......',		 //等待提示层代码
				fillBox:false,                   //填充显示数据的容器，如果不填则默认为自身容器填充数据
				loadBox:false,                   //提示对话框
				itemClass:'data-view-list',      //显示元素样式
				scrolloffset:25,
				onLoadItem:false,                //显示元素的加载回调方式，返回html
				dataRender:function(data){
					return data['Rows']||data;
				}
		    }, this.options, op||{});
		},
		init:function($el){
			var _self=this,options=this.options,showbox,loadBox;
			if(options.fillBox){
				showbox=$(options.fillBox);
			}else{
				showbox=this.$el;
			}
			this.fillBox=showbox;
			if(options.loadBox){
				loadBox=$(options.loadBox);
			}else{
				loadBox=$(showbox).children('div.loading')
			}
			if(!loadBox.length){
				loadBox=$('<div class="loading"></div>').appendTo(showbox);
				loadBox.html(options.loadHtml).hide();
			}
			this.fillBox.addClass('ui-scroll-load');
			this.loadBox=loadBox;
			if($(showbox).children('div.'+options.itemClass).length==0){
				_self.ajaxGetData(false);
			}else{
				_self.createBox();
			}
		},
		ajaxGetData:function(isScroll){
			var _self=this,options=this.options,params={};
			params[options.sizeParmName]=options.size;
			params[options.totalParmName]=options.total;
			params[options.pageParmName]=options.total/options.size + 1;
			var p=options.params||{};
			if($.isFunction(p)){
				p=p.call(window,this.$el);
			}
			if(p===false) return false;
			params = $.extend(params,p);
			var doError=function(msg){
				$(_self.loadBox).css('background-image','none');
				$(_self.loadBox).append('<span>'+msg+'</span>');
				return false;
			};
			//setTimeout 避免操作频繁
			if(_self.timeout){
				clearTimeout(_self.timeout);
				_self.timeout=null;
			}
			_self.loading=true;
			//loading 显示位置
			_self.loadBox.css({top:_self.fillBox.height()/2+_self.fillBox.scrollTop()});
			_self.loadBox.show();
			_self.timeout=setTimeout(function(){
				$.ajax({
					type: options.getMethod,
					url: options.url,
					data: params,
					dataType:'json',
					success: function(data){
						Public.ajaxCallback(data,function(d){
							if($.isFunction(options.dataRender)){
								d=options.dataRender.call(window,d,options);
								if(d===false){
									return false;
								}
							}
							if(isScroll){
								_self.createListItem(d);
							}else{
								_self.createBox(d);
							}
						});
					 },
					 error:function(e){
						doError(e);
					 }
				});
			},isScroll?500:0);
		},
		createBox:function(data){
			var _self=this,options=this.options;
			if(data){
				_self.createListItem(data); //加载初始化数据
			}
			_self.fillBox.on('scroll',function(){
				var childrenLength=$(this).children('div.'+options.itemClass).length;
				if(options.total==childrenLength||_self.isOver){
					return;
				}
				var scrolltop=$(this)[0].scrollTop,
					scrollheight=$(this)[0].scrollHeight,
					windowheight=$(this).height();
				//ajax加载判断
				if(scrolltop>=(scrollheight-(windowheight+options.scrolloffset))&&!_self.loading){
					options.total=childrenLength;
					_self.ajaxGetData(true);
				}
			});
		},
		createListItem:function(data){
			var _self=this,options=this.options;
			if(data.length==0){
				_self.loading=false;
				_self.isOver=true;
		    	$(_self.loadBox).hide();
				return;
			}
			if(data.length < options.size){
				_self.isOver=true;
			}
			var htmls=$.map(data,function(item){
				if(options.onLoadItem){
					return options.onLoadItem.call(window,item);
				}else{
					return ['<div class="',options.itemClass,'">',item,'</div>'];
				}
			});
			_self.fillBox.append(htmls.join(''));
			_self.loading=false;
		    _self.loadBox.hide();
		},
		reLoad:function(op){
			this.set(op||{});
			this.clean();
			this.options['total']=0;//设置开始为0
			this.isOver=false;
			this.ajaxGetData(false);
		},
		clean:function(){
			//删除原来的数据
			try{
				$('div.'+this.options.itemClass,this.fillBox).removeAllNode();
			}catch(e){
				$('div.'+this.options.itemClass,this.fillBox).remove();
			}
		}
	});

})(jQuery);